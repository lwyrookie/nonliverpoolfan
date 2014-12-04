import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;
import java.util.HashMap;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.TerminalNode;

public class SuperVisitor extends LexBaseVisitor<Node> {
	public ArrayList<String> outputList = new ArrayList<String>();
	private Stack<ArrayList<Node>> factorStack = new Stack<ArrayList<Node>>();
	private Stack<ArrayList<Node>> exprStack = new Stack<ArrayList<Node>>();
    private Stack<String> breakStack = new Stack();
    private Stack<String> continueStack = new Stack();
	protected Map<String, Map<String, Node>> tableMap = new LinkedHashMap<String, Map<String, Node>>(); // scope
																										// name
																										// +
																										// symbol
																										// map
																										// of
																										// that
																										// scope
	protected Map<String, String> functionMap = new LinkedHashMap<String, String>();
	protected Map<String, ArrayList<String>> tempMap = new LinkedHashMap<String, ArrayList<String>>();
    private Stack<Integer> functionPushCountStack = new Stack();
    private Stack<String> functionStack = new Stack();

	private String functionRecord = "GLOBAL";
	private int labelCount = 0;
    private int pushCount = 0;
    private int localIndex = 0;
    private int tempIndex = 0;
    private int paramIndex = 0;

    public Map<String,String> compOp = new HashMap<String, String>(){{
       put("<","GE"); put(">","LE"); put("=","NE"); put("!=","EQ"); put("<=","GT"); put(">=","LT");}}; 	
    


	public SuperVisitor(SymbolTable table, Map<String, String> functionMap) {
		this.functionMap = functionMap;

		    localIndex = 0;
            paramIndex = 0;
	}

    /**********************************************/

	
	public Node findIdNode(String id, String scopeName) {
        String nodetype = "I";
		for (Scope scope : (LexParser.symtab).scopestack.subList(0, (LexParser.symtab).scopestack.size())) {

            if(scope.Scopetype.equalsIgnoreCase(scopeName)) {
               Symbol sym = scope.symbolMap.get(id);

               if( sym == null && scope.Scopetype.equalsIgnoreCase("GLOBAL")) {
				  System.out.println(" variable "+id+ " not found in anyscope in findIdNode");
				  return null;
			   }
               if(sym != null) {
                  if(sym.type.contains("INT")) nodetype = "I";
                  if(sym.type.contains("FLOAT")) nodetype = "F";
                  if(sym.type.contains("STRING")) nodetype = "S";
                  if(sym.nodePrefix == null) 
                     return new Node(id, nodetype);
                  return new Node(sym.nodePrefix, nodetype);
               }
            }             
        }
        return findIdNode(id, "GLOBAL");
     }
                  
                

	public Node visitPrimary(@NotNull LexParser.PrimaryContext ctx) {
		if (ctx.expr() != null)
			return visit(ctx.expr());
		if (ctx.id() != null)
			return findIdNode(ctx.id().getText(), functionRecord);
		if (ctx.INTLITERAL() != null) {
			Node newNode = new Node(createTemp(), "I");
			outputList.add("STOREI " + ctx.INTLITERAL().getText() + " "
					+ newNode.content);

			return newNode;
		}
        
		Node newNode = new Node(createTemp(), "F");
		outputList.add("STOREF " + ctx.FLOATLITERAL().getText() + " "
				+ newNode.content);

		return newNode;
	}
    /*  Step 6 - added visitFunc, visitCall, visitExpr , visitReturn  */
	public Node visitFunc_decl(@NotNull LexParser.Func_declContext ctx) {
		ArrayList newTempList = new ArrayList();

		outputList.add("LABEL " + ctx.id().getText());
		functionRecord = ctx.id().getText();
/*--------------------lou modify-----------------*/  

      //  createTemp();    //leaving an extra slot for all the comparison in the each scope
      // tempIndex += 1;
	//	((ArrayList)tempMap.get(functionRecord)).add("$T"
	//			+ Integer.toString(tempIndex));




		tempMap.put(functionRecord, newTempList);
       
		outputList.add("LINK ");
		visitChildren(ctx);
        createTemp(); 
		//tempIndex = 0;
		if (ctx.any_type().getText().equals("VOID")) {
			outputList.add("RET");
		}
		return null;
	}

   	public Node visitCall_expr(@NotNull LexParser.Call_exprContext ctx) {
        Stack<String> revStack = new Stack();
    	functionPushCountStack.push(Integer.valueOf(pushCount));
    	pushCount = 0;
    	if (ctx.expr_list() != null) {
      		visit(ctx.expr_list());
    	}

    	outputList.add("PUSH ");
    	for (int i = 0; i < pushCount; i++) {
            revStack.push((String)functionStack.pop());
    	}
        for (int i = 0; i < pushCount; i++) {
      		outputList.add("PUSH " + (String)revStack.pop());
    	}
    	outputList.add("JSR " + ctx.id().getText());
    	for (int i = 0; i < pushCount; i++) {
      		outputList.add("POP ");
    	}
    	pushCount = ((Integer)functionPushCountStack.pop()).intValue();

	    Node newNode = new Node(createTemp(), ((String)functionMap.get(functionRecord)));
    	outputList.add("POP " + newNode.content);
   	    return newNode;
  	}

 	public Node visitExpr_list(@NotNull LexParser.Expr_listContext ctx) {
    	Node exprNode = (Node)visit(ctx.expr());

    	functionStack.push(exprNode.content);
    	pushCount += 1;
    	if (!"".equals(ctx.expr_list_tail().getText())) {
      		visit(ctx.expr_list_tail());
    	}
    	return null;
  	}

  	public Node visitExpr_list_tail(@NotNull LexParser.Expr_list_tailContext ctx) {
    	Node exprNode = (Node)visit(ctx.expr());
	    functionStack.push(exprNode.content);
    	pushCount += 1;
    	if (!"".equals(ctx.expr_list_tail().getText())) {
      		visit(ctx.expr_list_tail());
    	}
    	return null;

  	}

	public Node visitReturn_stmt(@NotNull LexParser.Return_stmtContext ctx) {
	    Node exprNode = (Node)visit(ctx.expr());
    	Node tempNode = new Node(createTemp(), exprNode.type);

		outputList.add("STORE" + exprNode.type.trim() + " " + exprNode + " " + tempNode);
      	outputList.add("STORE" + exprNode.type.trim() + " " + tempNode + " $R");
    	this.outputList.add("RET");
    	return null;
  	}
    /*************************************/

	// : expr_prefix factor
	public Node visitExpr(@NotNull LexParser.ExprContext ctx) {
		if (!"".equals(ctx.expr_prefix().getText())) {
			ArrayList exprList = new ArrayList();
			// inexpr = exprList;
			exprStack.push(exprList);
			Node exprNode = (Node) visit(ctx.expr_prefix());
			Node factorNode = (Node) visit(ctx.factor());
			((ArrayList) exprStack.peek()).add(factorNode);
			// inexpr.add(factorNode);
			Node resolveNode = resolve((ArrayList) exprStack.pop());
			// Node resolveNode = resolve(inexpr);

			return resolveNode;
		}

		Node factorNode = (Node) visit(ctx.factor());
		return factorNode;
	}

	// expr_prefix factor addop | empty
	public Node visitExpr_prefix(@NotNull LexParser.Expr_prefixContext ctx) {
		if (!"".equals(ctx.expr_prefix().getText())) {
			visit(ctx.expr_prefix());
		}
		Node opNode = new Node(ctx.addop().getText(), "Op");
		Node factorNode = (Node) visit(ctx.factor());
		((ArrayList) exprStack.peek()).add(factorNode);
		((ArrayList) exprStack.peek()).add(opNode);
		return null;
	}

	// factor_prefix postfix_expr
	public Node visitFactor(@NotNull LexParser.FactorContext ctx) {
		if (!"".equals(ctx.factor_prefix().getText())) {
			ArrayList factorList = new ArrayList();
			factorStack.push(factorList);
			Node exprNode = (Node) visit(ctx.factor_prefix());
			Node postfixNode = (Node) visit(ctx.postfix_expr());
			((ArrayList) factorStack.peek()).add(postfixNode);
			Node resolveNode = resolve((ArrayList) factorStack.pop());

			return resolveNode;
		}

		return (Node) visit(ctx.postfix_expr());
	}

	// factor_prefix postfix_expr mulop | empty
	public Node visitFactor_prefix(@NotNull LexParser.Factor_prefixContext ctx) {
		if (!"".equals(ctx.factor_prefix().getText())) {
			visit(ctx.factor_prefix());
		}
		Node opNode = new Node(ctx.mulop().getText(), "Op");
		Node postfixNode = (Node) visit(ctx.postfix_expr());
		((ArrayList) factorStack.peek()).add(postfixNode);
		((ArrayList) factorStack.peek()).add(opNode);

		return null;
	}

	// 'WRITE' '(' id_list ')' ';'
	public Node visitWrite_stmt(@NotNull LexParser.Write_stmtContext ctx) {
		String[] idArray = ctx.id_list().getText().split(",");
		for (int i = 0; i < idArray.length; i++) {
			Node newNode = findIdNode(idArray[i], functionRecord);
			outputList.add("WRITE" + newNode.type + " " + newNode.content);			
		}
		return null;
	}

	public Node visitRead_stmt(@NotNull LexParser.Read_stmtContext ctx) {
		String[] idArray = ctx.id_list().getText().split(",");
		for (int i = 0; i < idArray.length; i++) {
			Node newNode = findIdNode(idArray[i], functionRecord);
			outputList.add("READ" + newNode.type + " " + newNode.content);	
		}
		return null;
	}

	public Node visitAssign_expr(@NotNull LexParser.Assign_exprContext ctx) {
		Node exprNode = (Node) visit(ctx.expr());
		Node newNode = findIdNode(ctx.id().getText(), functionRecord);
		outputList.add("STORE" + newNode.type + " " + exprNode.content+ " "
					+ newNode.content);	
		return null;
	}

	/*----------------Added for step 5-------------------------------*/
	// 'IF' '(' cond ')' decl stmt_list else_part 'ENDIF'
	public Node visitIf_stmt(@NotNull LexParser.If_stmtContext ctx) // Return
																	// type need
																	// to be
																	// rechecked
	{
		
		if (!"".equals(ctx.else_part().getText()))// with else
		{
			Node comp = visit(ctx.cond());
			String newLabel = addLabel();
			outputList.add(comp.content + " " + newLabel);
			visit(ctx.stmt_list());
			String newLabel2 = addLabel();
			// labelStack.push(newLabel2);
			outputList.add("JUMP " + newLabel2);
			outputList.add("LABEL " + newLabel);
			visit(ctx.else_part());
			outputList.add("LABEL " + newLabel2);

		} else {
			Node comp = (Node) visit(ctx.cond());
			String newLabel2 = addLabel();
			outputList.add(comp.content + " " + newLabel2);
			visit(ctx.stmt_list());
			outputList.add("LABEL " + newLabel2);

		}
		return null;
	}

	public Node  visitAug_if_stmt(@NotNull LexParser.Aug_if_stmtContext ctx)  // Return
	// type need
	// to be
	// rechecked
	{
		
		if (!"".equals(ctx.aug_else_part().getText()))// with else
		{
			Node comp = visit(ctx.cond());
			String newLabel = addLabel();
			outputList.add(comp.content + " " + newLabel);
			visit(ctx.aug_stmt_list());
			String newLabel2 = addLabel();
			// labelStack.push(newLabel2);
			outputList.add("JUMP " + newLabel2);
			outputList.add("LABEL " + newLabel);
			visit(ctx.aug_else_part());
			outputList.add("LABEL " + newLabel2);

		} else {
			Node comp = (Node) visit(ctx.cond());
			String newLabel2 = addLabel();
			outputList.add(comp.content + " " + newLabel2);
			visit(ctx.aug_stmt_list());
			outputList.add("LABEL " + newLabel2);

		}
		return null;
	}

	public Node visitWhile_stmt(@NotNull LexParser.While_stmtContext ctx) {
		String label1 = addLabel();
		outputList.add("LABEL " + label1);
		String label2 = addLabel();
		Node comp = (Node) visit(ctx.cond());
        
        //push the corresponding labels to breakStack and continueStack
        breakStack.push("JUMP "+ label2);
        continueStack.push("JUMP "+ label1);
		outputList.add(comp.content + " " + label2);
		visit(ctx.aug_stmt_list());
		outputList.add("JUMP " + label1);
		outputList.add("LABEL " + label2);
		return null;
	}

	public Node visitCond(@NotNull LexParser.CondContext ctx) {
		Node exprnode = visit(ctx.expr());
		Node exprnode1 = visit(ctx.expr1()); // recompile for visitexpr1
		String comptype = ctx.compop().getText().toUpperCase();
		return new Node(compOp.get(comptype) + exprnode.type + " " + exprnode.content + " " + exprnode1.content, compOp.get(comptype));
	}

    public Node visitAug_stmt(@NotNull LexParser.Aug_stmtContext ctx) {
      if (ctx.base_stmt() != null)
         return visit(ctx.base_stmt());
      if (ctx.while_stmt() != null)
         return visit(ctx.while_stmt());
      if (ctx.aug_if_stmt() != null)
         return visit(ctx.aug_if_stmt());

      if (ctx.getText().contains("BREAK")) {
          //System.out.println("inside break");
          //System.out.println(breakStack.peek());
          outputList.add((String)breakStack.peek());
          return null;
      }  
      if (ctx.getText().contains("CONTINUE")) {
        outputList.add((String)continueStack.peek());
        return null;
      }
      return null; 
    }

	public String addLabel() {
		labelCount += 1;
		return ("label" + Integer.toString(labelCount));

	}

	public String createTemp() {
		tempIndex += 1;
		((ArrayList)tempMap.get(functionRecord)).add("$T"
				+ Integer.toString(tempIndex));
		return "$T" + Integer.toString(tempIndex);
	}

	// clear
	public Node resolve(ArrayList<Node> input) {
		while (input.size() >= 3) {
			Node op1 = (Node) input.get(0);
			Node op = (Node) input.get(1);
			Node op2 = (Node) input.get(2);

			Node newNode = new Node(createTemp(), op1.type);
			if (op.content.equalsIgnoreCase("+")) {
				String output = "ADD" + op1.type + " " + op1.content + " " + op2.content + " " + newNode.content;
				outputList.add(output);
			} else if (op.content.equalsIgnoreCase("-")) {
			    String output = "SUB" + op1.type + " " + op1.content + " " + op2.content + " " + newNode.content;
				outputList.add(output);
			} else if (op.content.equalsIgnoreCase("*")) {
			    String output = "MULT" + op1.type + " " + op1.content + " " + op2.content + " " + newNode.content;
				outputList.add(output);
			} else if (op.content.equalsIgnoreCase("/")) {
			    String output = "DIV" + op1.type + " " + op1.content + " " + op2.content + " " + newNode.content;
				outputList.add(output);
			}
			input.remove(0);
			input.remove(0);
			input.remove(0);
			input.add(0, newNode);
		}

		Node returnValue = (Node) input.get(0);
		input.removeAll(input);
		return returnValue;
	}

	public String return3AC() {
		String result = "";
		for (int i = 0; i < outputList.size() - 1; i++) {

			result = result + ";" + (String) outputList.get(i);
			result = result + "\n";
		}
		result = result + ";"
				+ (String) outputList.get(outputList.size() - 1);
		System.out.println(";IR code\n" + result);
		return result;
	}

}
