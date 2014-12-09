import java.io.PrintStream;
import java.util.*;
/*import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;
import java.util.HashMap;*/

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.TerminalNode;

public class SuperVisitor extends LexBaseVisitor<Node> {
	public ArrayList<String> outputList;
    /* use stacks to keep track of recursive elements and a list to track function counts */
	protected Stack factorStack;
	protected Stack exprStack;
    protected Stack breakStack;
    protected Stack continueStack;
    protected Stack functionStack;
	protected List<Integer> trackLinkNum;
	protected Map<String, String> functionMap;
	protected Map<String, ArrayList<String>> tempMap;


	private String functionRecord;
    private int linkNum;
	private int labelCount;
    private int tempIndex;

    public Map<String,String> compOp = new HashMap<String, String>(){{
       put("<","GE"); put(">","LE"); put("=","NE"); put("!=","EQ"); put("<=","GT"); put(">=","LT");}}; 	
    
    public Map<String,String> basicOp = new HashMap<String, String>(){{
       put("+","ADD"); put("-","SUB"); put("*","MULT"); put("/","DIV");}};

	public SuperVisitor(SymbolTable table, Map<String, String> functionMap) {
		this.functionMap = functionMap;
        //Initialize all the required data structures. 
        initialize();
	}

    /**********************************************/
    protected void initialize(){
        outputList = new ArrayList();
        factorStack = new Stack<ArrayList<Node>>();
        exprStack = new Stack<ArrayList<Node>>();
        breakStack = new Stack();
        continueStack = new Stack();
        functionStack = new Stack();
        functionMap = new LinkedHashMap<String, String>();
        tempMap = new LinkedHashMap<String, ArrayList<String>>();
        trackLinkNum = new ArrayList<Integer>();
        functionRecord = "GLOBAL";
        labelCount = 0;
        linkNum = 0;
        tempIndex = 0;

    }
	
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
                  
                
	public Node visitPrimary(@NotNull LexParser.PrimaryContext primarycontext) {
		if (primarycontext.expr() != null)
			return visit(primarycontext.expr());
		if (primarycontext.id() != null)
			return findIdNode(primarycontext.id().getText(), functionRecord);
		Node newNode = null;
        if ((primarycontext.INTLITERAL() == null) && (primarycontext.FLOATLITERAL() == null)) {
			newNode = new Node(createTemp(), "F");
            outputList.add((new StringBuilder()).append("STOREF ").append(primarycontext.FLOATLITERAL().getText()).append(" ").append(newNode.content).toString());
        }
        else {
			newNode = new Node(createTemp(), "I");
            outputList.add((new StringBuilder()).append("STOREI ").append(primarycontext.INTLITERAL().getText()).append(" ").append(newNode.content).toString());
			return newNode;
        }
		return newNode;
	}


    /*  Step 6 - added visitFunc, visitCall, visitExpr , visitReturn  */
	public Node visitFunc_decl(@NotNull LexParser.Func_declContext func_declcontext) {
		ArrayList newTempList = new ArrayList();

        outputList.add((new StringBuilder()).append("LABEL ").append(func_declcontext.id().getText()).toString());
		functionRecord = func_declcontext.id().getText();
/*--------------------lou modify-----------------*/  

      //  createTemp();    //leaving an extra slot for all the comparison in the each scope
      // tempIndex += 1;
	//	((ArrayList)tempMap.get(functionRecord)).add("$T"
	//			+ Integer.toString(tempIndex));




		tempMap.put(functionRecord, newTempList);

       
		outputList.add("LINK ");
		visitChildren(func_declcontext);
        createTemp(); 
		tempIndex = 0;

        LexParser.funcHub.get(this.functionRecord).pushTempList(tempMap.get(functionRecord));
		if (func_declcontext.any_type().getText().equals("VOID")) {
			outputList.add("RET");
		}
		return null;
	}	

	public Node visitCall_expr(@NotNull LexParser.Call_exprContext call_exprcontext) {
        Stack revStack = new Stack();
		//Integer linkNum = Integer.valueOf(linkNum);
		trackLinkNum.add(linkNum);
		linkNum = 0;
    	if (call_exprcontext.expr_list() != null) {
      		visit(call_exprcontext.expr_list());
    	}

    	outputList.add("PUSH ");
    	for (int i = 0; i < linkNum; i++) {
            revStack.push((String)functionStack.pop());
    	}
        for (int i = 0; i < linkNum; i++) {
      		outputList.add((new StringBuilder()).append("PUSH ").append((String)revStack.pop()).toString());
    	}
    	outputList.add("JSR " + call_exprcontext.id().getText());
    	for (int i = 0; i < linkNum; i++) {
      		outputList.add("POP ");
    	}
		linkNum = trackLinkNum.remove(trackLinkNum.size()-1);
	    Node newNode = new Node(createTemp(), ((String)functionMap.get(functionRecord)));
        outputList.add((new StringBuilder()).append("POP ").append(newNode.content).toString());
   	    return newNode;
  	}

 	public Node visitExpr_list(@NotNull LexParser.Expr_listContext expr_listcontext) {
    	Node exprNode = (Node)visit(expr_listcontext.expr());

    	functionStack.push(exprNode.content);
    	linkNum += 1;
    	if (!"".equals(expr_listcontext.expr_list_tail().getText())) {
      		visit(expr_listcontext.expr_list_tail());
    	}
    	return null;
  	}

  	public Node visitExpr_list_tail(@NotNull LexParser.Expr_list_tailContext expr_list_tailcontext) {
    	Node exprNode = (Node)visit(expr_list_tailcontext.expr());
	    functionStack.push(exprNode.content);
    	linkNum += 1;
    	if (!"".equals(expr_list_tailcontext.expr_list_tail().getText())) {
      		visit(expr_list_tailcontext.expr_list_tail());
    	}
    	return null;
  	}

	public Node visitReturn_stmt(@NotNull LexParser.Return_stmtContext return_stmtcontext) {
	    Node exprNode = (Node)visit(return_stmtcontext.expr());
    	Node tempNode = new Node(createTemp(), exprNode.type);
        outputList.add((new StringBuilder()).append("STORE").append(exprNode.type.trim()).append(" ").append(exprNode).append(" ").append(tempNode).toString());
        outputList.add((new StringBuilder()).append("STORE").append(exprNode.type.trim()).append(" ").append(tempNode).append(" $R").toString());
    	this.outputList.add("RET");
    	return null;
  	}
    /*************************************/

	// : expr_prefix factor
	public Node visitExpr(@NotNull LexParser.ExprContext exprcontext) {
		if (!"".equals(exprcontext.expr_prefix().getText())) {
			ArrayList exprList = new ArrayList();
			exprStack.push(exprList);
			Node exprNode = (Node) visit(exprcontext.expr_prefix());
			Node factorNode = (Node) visit(exprcontext.factor());
			((ArrayList) exprStack.peek()).add(factorNode);
			Node resolveNode = resolve((ArrayList) exprStack.pop());
			return resolveNode;
		}

		Node factorNode = (Node) visit(exprcontext.factor());
		return factorNode;
	}

	// expr_prefix factor addop | empty
	public Node visitExpr_prefix(@NotNull LexParser.Expr_prefixContext expr_prefixcontext) {
		if (!"".equals(expr_prefixcontext.expr_prefix().getText())) {
			visit(expr_prefixcontext.expr_prefix());
		}
        Node opNode, factorNode;
        opNode = new Node(expr_prefixcontext.addop().getText(), "Op");
        factorNode = (Node) visit(expr_prefixcontext.factor());
		((ArrayList) exprStack.peek()).add(factorNode);
		((ArrayList) exprStack.peek()).add(opNode);
		return null;
	}

	// factor_prefix postfix_expr
	public Node visitFactor(@NotNull LexParser.FactorContext factorcontext) {
		if (!"".equals(factorcontext.factor_prefix().getText())) {
            Node exprNode, postfixNode;
			ArrayList factorList = new ArrayList();
			factorStack.push(factorList);
            exprNode = (Node) visit(factorcontext.factor_prefix());
            postfixNode = (Node) visit(factorcontext.postfix_expr());
			((ArrayList) factorStack.peek()).add(postfixNode);
			return (Node)(resolve((ArrayList) factorStack.pop()));
		}
		return (Node) visit(factorcontext.postfix_expr());
	}

	// factor_prefix postfix_expr mulop | empty
	public Node visitFactor_prefix(@NotNull LexParser.Factor_prefixContext factor_prefixcontext) {
		if (!"".equals(factor_prefixcontext.factor_prefix().getText())) {
			visit(factor_prefixcontext.factor_prefix());
		}
		Node opNode = new Node(factor_prefixcontext.mulop().getText(), "Op");
		Node postfixNode = (Node) visit(factor_prefixcontext.postfix_expr());
		((ArrayList) factorStack.peek()).add(postfixNode);
		((ArrayList) factorStack.peek()).add(opNode);
		return null;
	}

	// 'WRITE' '(' id_list ')' ';'
	public Node visitWrite_stmt(@NotNull LexParser.Write_stmtContext write_stmtcontext) {
		String[] tokens = write_stmtcontext.id_list().getText().split(",");
		for (int i = 0; i < tokens.length; i++) {
			Node newNode = findIdNode(tokens[i], functionRecord);
            outputList.add((new StringBuilder()).append("WRITE").append(newNode.type).append(" ").append(newNode.content).toString());			
		}
		return null;
	}

	public Node visitRead_stmt(@NotNull LexParser.Read_stmtContext read_stmtcontext) {
		String[] tokens = read_stmtcontext.id_list().getText().split(",");
		for (int i = 0; i < tokens.length; i++) {
			Node newNode = findIdNode(tokens[i], functionRecord);
            outputList.add((new StringBuilder()).append("READ").append(newNode.type).append(" ").append(newNode.content).toString());
		}
		return null;
	}

	public Node visitAssign_expr(@NotNull LexParser.Assign_exprContext assign_exprcontext) {
		Node exprNode = (Node) visit(assign_exprcontext.expr());
		Node newNode = findIdNode(assign_exprcontext.id().getText(), functionRecord);
        outputList.add((new StringBuilder()).append("STORE").append(newNode.type).append(" ").append(exprNode.content).append(" ").append(newNode.content).toString());
		return null;
	}

	/*----------------Added for step 5-------------------------------*/
	// 'IF' '(' cond ')' decl stmt_list else_part 'ENDIF'
	public Node visitIf_stmt(@NotNull LexParser.If_stmtContext if_stmtcontext)	{		
		if (!"".equals(if_stmtcontext.else_part().getText()))
		{
			Node compNode1 = visit(if_stmtcontext.cond());
            String lbl, lbl1, lbl2;
			lbl1 = addLabel();
            outputList.add((new StringBuilder()).append(compNode1.content).append(" ").append(lbl1).toString());
			visit(if_stmtcontext.stmt_list());
            lbl2 = addLabel();
			// labelStack.push(newLabel2);
            outputList.add((new StringBuilder()).append("JUMP ").append(lbl2).toString());
            outputList.add((new StringBuilder()).append("LABEL ").append(lbl1).toString());
			visit(if_stmtcontext.else_part());
            outputList.add((new StringBuilder()).append("LABEL ").append(lbl2).toString());
		} else {
			Node compNode2 = (Node) visit(if_stmtcontext.cond());
			String lbl3 = addLabel();
            outputList.add((new StringBuilder()).append(compNode2.content).append(" ").append(lbl3).toString());
            visit(if_stmtcontext.stmt_list());
            outputList.add((new StringBuilder()).append("LABEL ").append(lbl3).toString());

		}
		return null;
	}

	public Node  visitAug_if_stmt(@NotNull LexParser.Aug_if_stmtContext aug_if_stmtcontext) {		
		if (!"".equals(aug_if_stmtcontext.aug_else_part().getText()))
		{
			Node compNode1 = visit(aug_if_stmtcontext.cond());
            String lbl, lbl1, lbl2;
			lbl = addLabel();
            outputList.add((new StringBuilder()).append(compNode1.content).append(" ").append(lbl).toString());
			visit(aug_if_stmtcontext.aug_stmt_list());
			lbl2 = addLabel();

            outputList.add((new StringBuilder()).append("JUMP ").append(lbl2).toString());
            outputList.add((new StringBuilder()).append("LABEL ").append(lbl).toString());
			visit(aug_if_stmtcontext.aug_else_part());
            outputList.add((new StringBuilder()).append("LABEL ").append(lbl2).toString());
		} else {
			Node compNode2 = (Node) visit(aug_if_stmtcontext.cond());
			String lbl3 = addLabel();
            outputList.add((new StringBuilder()).append(compNode2.content).append(" ").append(lbl3).toString());
			visit(aug_if_stmtcontext.aug_stmt_list());
            outputList.add((new StringBuilder()).append("LABEL ").append(lbl3).toString());
		}
		return null;
	}	

    public Node visitWhile_stmt(@NotNull LexParser.While_stmtContext while_stmtcontext) {
        String lbl1, lbl2, lbl3;
        Node compNode1;
		lbl1 = addLabel();
        outputList.add((new StringBuilder()).append("LABEL ").append(lbl1).toString());
		lbl2 = addLabel();
		compNode1 = (Node) visit(while_stmtcontext.cond());
        
        //push the corresponding labels to breakStack and continueStack
        breakStack.push((new StringBuilder()).append("JUMP ").append(lbl2).toString());
        continueStack.push((new StringBuilder()).append("JUMP ").append(lbl1).toString());
        outputList.add((new StringBuilder()).append(compNode1.content).append(" ").append(lbl2).toString());
        visit(while_stmtcontext.aug_stmt_list());
        outputList.add((new StringBuilder()).append("JUMP ").append(lbl1).toString());
        outputList.add((new StringBuilder()).append("LABEL ").append(lbl2).toString());

		return null;
	}

	public Node visitCond(@NotNull LexParser.CondContext condcontext) {
        Node exprNode1,exprNode2, newNode;
        String comptype;
		exprNode1 = visit(condcontext.expr());
		exprNode2 = visit(condcontext.expr1());
		comptype = condcontext.compop().getText().toUpperCase();
        newNode = new Node((new StringBuilder()).append((String)compOp.get(comptype)).append(exprNode1.type).append(" ").append(exprNode1.content).append(" ").append(exprNode2.content).toString(), (String)compOp.get(comptype));
        return newNode; 
	}

    public Node visitAug_stmt(@NotNull LexParser.Aug_stmtContext aug_stmtcontext) {
      if (aug_stmtcontext.base_stmt() != null)
         return visit(aug_stmtcontext.base_stmt());
      if (aug_stmtcontext.while_stmt() != null)
         return visit(aug_stmtcontext.while_stmt());
      if (aug_stmtcontext.aug_if_stmt() != null)
         return visit(aug_stmtcontext.aug_if_stmt());

      if (aug_stmtcontext.getText().contains("BREAK")) {
          //System.out.println("inside break");
          //System.out.println(breakStack.peek());
          outputList.add((String)breakStack.peek());
          return null;
      }  
      if (aug_stmtcontext.getText().contains("CONTINUE")) {
        outputList.add((String)continueStack.peek());
        return null;
      }
      return null; 
    }
    
    /***************************************************************/
	public String addLabel() {
		labelCount += 1;
        return (new StringBuilder()).append("label").append(Integer.toString(labelCount)).toString();
		//return ("label" + Integer.toString(labelCount));

	}

	public String createTemp() {
		tempIndex += 1;
		((ArrayList)tempMap.get(functionRecord)).add((new StringBuilder()).append("$T").append(Integer.toString(tempIndex)).toString());
		return "$T" + Integer.toString(tempIndex);
	}

	// clear
	public Node resolve(ArrayList<Node> nodesList) {
	    String concatResult = null;
		Node op1, op, op2;
		while (nodesList.size() >= 3) {
			op1 = (Node) nodesList.get(0);
			op = (Node) nodesList.get(1);
			op2 = (Node) nodesList.get(2);
                      
			Node newNode = new Node(createTemp(), op1.type);
            //String s = (new StringBuilder()).append("ADD").append(node.type).append(" ").append(node.content).append(" ").append(node3.content).append(" ").append(node4.content).toString();
			concatResult = basicOp.get(op.content.toUpperCase()) + op1.type + " " + op1.content + " " + op2.content + " " + newNode.content;
			outputList.add(concatResult);
			
			nodesList.remove(0);
			nodesList.remove(0);
			nodesList.remove(0);
			nodesList.add(0, newNode);
		}

		Node returnValue = (Node) nodesList.get(0);
		nodesList.removeAll(nodesList);
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
