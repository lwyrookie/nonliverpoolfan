import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.TerminalNode;

public class SuperVisitor extends LexBaseVisitor<Node> {
	public ArrayList<String> outputList = new ArrayList<String>();
	private Stack<ArrayList<Node>> factorStack = new Stack<ArrayList<Node>>();
	private Stack<ArrayList<Node>> exprStack = new Stack<ArrayList<Node>>();
	// private ArrayList<Node> infactor = new ArrayList<Node>();
	// private ArrayList<Node> inexpr = new ArrayList<Node>();
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
	protected Map<String, Integer> functionMap = new LinkedHashMap<String, Integer>();
	protected Map<String, ArrayList<String>> tempMap = new LinkedHashMap<String, ArrayList<String>>();
    private Stack<Integer> functionPushCountStack = new Stack();
    private Stack<String> functionStack = new Stack();

	private String functionRecord = "GLOBAL";
	private int labelCount = 0;
    private int pushCount = 0;
    private int localIndex = 0;
    private int tempIndex = 0;
    private int paramIndex = 0;



	public SuperVisitor(SymbolTable table, Map<String, Integer> functionMap) {
		this.functionMap = functionMap;

		for (Scope scope : table.scopestack.subList(0, table.scopestack.size())) {
			Map<String, Node> varMap = new LinkedHashMap<String, Node>();
			if (scope.Scopetype.equalsIgnoreCase("GLOBAL")) {
				for (String key : scope.symbolMap.keySet()) {
					// System.out.println(key);
					if (((Symbol) scope.symbolMap.get(key)).getType().contains("INT")) {
						varMap.put(key, new Node(key, 1)); // String node
					} else if (((Symbol) scope.symbolMap.get(key)).getType().contains("FLOAT")) {
						varMap.put(key, new Node(key, 2));
					} else if (((Symbol) scope.symbolMap.get(key)).getType().contains("STRING")) {
						varMap.put(key, new Node(key, 5)); // string equals five
					} else {
						System.out.println("error adding key to varMap");
					}
				}
			} else {
                /*    Step 6 - added else part for function calls   */
			    for (String key : scope.symbolMap.keySet()) {
                  if (key.contains("@P")) {
                    //System.out.println("Adding parameter "+key+ " in varMap");
                    if (((Symbol) scope.symbolMap.get(key)).getType().contains("INT")) {
                      varMap.put(key.substring(2), new Node(newFuncVar(key), 1));
                    }
                    else if (((Symbol) scope.symbolMap.get(key)).getType().contains("FLOAT")) {
                      varMap.put(key.substring(2), new Node(newFuncVar(key), 2));
                    }
                    else if (((Symbol) scope.symbolMap.get(key)).getType().contains("STRING")) {
                      varMap.put(key.substring(2), new Node(key, 5));
                    }
                    else {
                      System.out.println("error adding key to varMap inside function scope");
                    }
                  }
                  else if (((Symbol) scope.symbolMap.get(key)).getType().contains("INT")) {
                    varMap.put(key, new Node(newFuncVar(key), 1));
                  }
                  else if (((Symbol) scope.symbolMap.get(key)).getType().contains("FLOAT")) {
                    varMap.put(key, new Node(newFuncVar(key), 2));
                  }
                  else if (((Symbol) scope.symbolMap.get(key)).getType().contains("STRING")) {
                    varMap.put(key, new Node(key, 5));
                  }
                  else {
                    System.out.println("error adding key to varMap inside function scope");
                  }
                }
			  }
            //System.out.println(varMap);
			this.tableMap.put(scope.Scopetype, varMap);
            this.localIndex = 0;
            this.paramIndex = 0;
		}
	}

	private String newFuncVar(String key) {
        if(key.contains("@P")){
    	
      		this.paramIndex += 1;
      		return "$P" + Integer.toString(this.paramIndex);
       }
	
	    this.localIndex += 1;
    	return "$L" + Integer.toString(this.localIndex);
  	}
    /**********************************************/

	// Scopetype is something like GLOBAL, BLCOK X, FUNCTION NAME
	public Node findIdNode(String id, String scopeName) {
		if ((this.tableMap.get(scopeName)).get(id) == null) {
			if ((this.tableMap.get("GLOBAL")).get(id) == null) {
				System.out.println(" variable "+id+ " not found in " +scopeName+ " - findIdNode");
				return null;
			}
			return this.tableMap.get("GLOBAL").get(id);
		}
        return this.tableMap.get(scopeName).get(id);
	}

	public Node visitPrimary(@NotNull LexParser.PrimaryContext ctx) {
		if (ctx.expr() != null)
			return visit(ctx.expr());
		if (ctx.id() != null)
			return findIdNode(ctx.id().getText(), this.functionRecord);
		if (ctx.INTLITERAL() != null) {
			Node newNode = new Node(createTemp(), 1);
			this.outputList.add("STOREI " + ctx.INTLITERAL().getText() + " "
					+ newNode.content);

			return newNode;
		}

		Node newNode = new Node(createTemp(), 2);
		this.outputList.add("STOREF " + ctx.FLOATLITERAL().getText() + " "
				+ newNode.content);

		return newNode;
	}
    /*  Step 6 - added visitFunc, visitCall, visitExpr , visitReturn  */
	public Node visitFunc_decl(@NotNull LexParser.Func_declContext ctx) {
		ArrayList newTempList = new ArrayList();

		this.outputList.add("LABEL " + ctx.id().getText());
		this.functionRecord = ctx.id().getText();
		this.tempMap.put(this.functionRecord, newTempList);
		this.outputList.add("LINK ");
		visitChildren(ctx);
		//this.tempIndex = 0;
		if (ctx.any_type().getText().equals("VOID")) {
			this.outputList.add("RET");
		}
		return null;
	}

   	public Node visitCall_expr(@NotNull LexParser.Call_exprContext ctx) {
        Stack<String> revStack = new Stack();
    	this.functionPushCountStack.push(Integer.valueOf(this.pushCount));
    	this.pushCount = 0;
    	if (ctx.expr_list() != null) {
      		visit(ctx.expr_list());
    	}

    	this.outputList.add("PUSH ");
    	for (int i = 0; i < this.pushCount; i++) {
            revStack.push((String)this.functionStack.pop());
    	}
        for (int i = 0; i < this.pushCount; i++) {
      		this.outputList.add("PUSH " + (String)revStack.pop());
    	}
    	this.outputList.add("JSR " + ctx.id().getText());
    	for (int i = 0; i < this.pushCount; i++) {
      		this.outputList.add("POP ");
    	}
    	this.pushCount = ((Integer)this.functionPushCountStack.pop()).intValue();

	    Node newNode = new Node(createTemp(), ((Integer)this.functionMap.get(this.functionRecord)).intValue());
    	this.outputList.add("POP " + newNode.content);
   	    return newNode;
  	}

 	public Node visitExpr_list(@NotNull LexParser.Expr_listContext ctx) {
    	Node exprNode = (Node)visit(ctx.expr());

    	this.functionStack.push(exprNode.content);
    	this.pushCount += 1;
    	if (!"".equals(ctx.expr_list_tail().getText())) {
      		visit(ctx.expr_list_tail());
    	}
    	return null;
  	}

  	public Node visitExpr_list_tail(@NotNull LexParser.Expr_list_tailContext ctx) {
    	Node exprNode = (Node)visit(ctx.expr());
	    this.functionStack.push(exprNode.content);
    	this.pushCount += 1;
    	if (!"".equals(ctx.expr_list_tail().getText())) {
      		visit(ctx.expr_list_tail());
    	}
    	return null;
  	}

	public Node visitReturn_stmt(@NotNull LexParser.Return_stmtContext ctx) {
	    Node exprNode = (Node)visit(ctx.expr());
    	Node tempNode = new Node(createTemp(), exprNode.type);
    	if (exprNode.type == 1) {
      		this.outputList.add("STOREI " + exprNode + " " + tempNode);
      		this.outputList.add("STOREI " + tempNode + " $R");
    	}
    	else {
      		this.outputList.add("STOREF " + exprNode + " " + tempNode);
      		this.outputList.add("STOREF " + tempNode + " $R");
    	}
    	this.outputList.add("RET");
    	return null;
  	}
    /*************************************/

	// : expr_prefix factor
	public Node visitExpr(@NotNull LexParser.ExprContext ctx) {
		if (!"".equals(ctx.expr_prefix().getText())) {
			ArrayList exprList = new ArrayList();
			// this.inexpr = exprList;
			this.exprStack.push(exprList);
			Node exprNode = (Node) visit(ctx.expr_prefix());
			Node factorNode = (Node) visit(ctx.factor());
			((ArrayList) this.exprStack.peek()).add(factorNode);
			// this.inexpr.add(factorNode);
			Node resolveNode = resolve((ArrayList) this.exprStack.pop());
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
		Node opNode = new Node(ctx.addop().getText(), 3);
		Node factorNode = (Node) visit(ctx.factor());
		((ArrayList) this.exprStack.peek()).add(factorNode);
		((ArrayList) this.exprStack.peek()).add(opNode);
		// this.inexpr.add(factorNode);
		// this.inexpr.add(opNode);

		return null;
	}

	// factor_prefix postfix_expr
	public Node visitFactor(@NotNull LexParser.FactorContext ctx) {
		if (!"".equals(ctx.factor_prefix().getText())) {
			ArrayList factorList = new ArrayList();
			this.factorStack.push(factorList);
			Node exprNode = (Node) visit(ctx.factor_prefix());
			Node postfixNode = (Node) visit(ctx.postfix_expr());
			((ArrayList) this.factorStack.peek()).add(postfixNode);
			Node resolveNode = resolve((ArrayList) this.factorStack.pop());

			return resolveNode;
		}

		return (Node) visit(ctx.postfix_expr());
	}

	// factor_prefix postfix_expr mulop | empty
	public Node visitFactor_prefix(@NotNull LexParser.Factor_prefixContext ctx) {
		if (!"".equals(ctx.factor_prefix().getText())) {
			visit(ctx.factor_prefix());
		}
		Node opNode = new Node(ctx.mulop().getText(), 3);
		Node postfixNode = (Node) visit(ctx.postfix_expr());
		((ArrayList) this.factorStack.peek()).add(postfixNode);
		((ArrayList) this.factorStack.peek()).add(opNode);

		return null;
	}

	// 'WRITE' '(' id_list ')' ';'
	public Node visitWrite_stmt(@NotNull LexParser.Write_stmtContext ctx) {
		String[] idArray = ctx.id_list().getText().split(",");
		for (int i = 0; i < idArray.length; i++) {
			Node newNode = findIdNode(idArray[i], this.functionRecord);
			if (newNode.type == 1) {
				this.outputList.add("WRITEI " + newNode.content);
			} else if (newNode.type == 5) {
				this.outputList.add("WRITES " + newNode.content);
			} else {
				this.outputList.add("WRITEF " + newNode.content);
			}
		}

		return null;
	}

	public Node visitRead_stmt(@NotNull LexParser.Read_stmtContext ctx) {
		String[] idArray = ctx.id_list().getText().split(",");
		for (int i = 0; i < idArray.length; i++) {
			Node newNode = findIdNode(idArray[i], this.functionRecord);
			if (newNode.type == 1) {
				this.outputList.add("READI " + newNode.content);
			} else {
				this.outputList.add("READF " + newNode.content);
			}
		}

		return null;
	}

	public Node visitAssign_expr(@NotNull LexParser.Assign_exprContext ctx) {
		Node exprNode = (Node) visit(ctx.expr());
		Node newNode = findIdNode(ctx.id().getText(), this.functionRecord);
		if (newNode.type == 1) {
			this.outputList.add("STOREI " + exprNode.content + " "
					+ newNode.content);
		} else {
			this.outputList.add("STOREF " + exprNode.content + " "
					+ newNode.content);
		}

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
			this.outputList.add(comp.content + " " + newLabel);
			visit(ctx.stmt_list());
			String newLabel2 = addLabel();
			// this.labelStack.push(newLabel2);
			this.outputList.add("JUMP " + newLabel2);
			this.outputList.add("LABEL " + newLabel);
			visit(ctx.else_part());
			this.outputList.add("LABEL " + newLabel2);

		} else {
			Node comp = (Node) visit(ctx.cond());
			String newLabel2 = addLabel();
			this.outputList.add(comp.content + " " + newLabel2);
			visit(ctx.stmt_list());
			this.outputList.add("LABEL " + newLabel2);

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
			this.outputList.add(comp.content + " " + newLabel);
			visit(ctx.aug_stmt_list());
			String newLabel2 = addLabel();
			// this.labelStack.push(newLabel2);
			this.outputList.add("JUMP " + newLabel2);
			this.outputList.add("LABEL " + newLabel);
			visit(ctx.aug_else_part());
			this.outputList.add("LABEL " + newLabel2);

		} else {
			Node comp = (Node) visit(ctx.cond());
			String newLabel2 = addLabel();
			this.outputList.add(comp.content + " " + newLabel2);
			visit(ctx.aug_stmt_list());
			this.outputList.add("LABEL " + newLabel2);

		}
		return null;
	}

	public Node visitWhile_stmt(@NotNull LexParser.While_stmtContext ctx) {
		String label1 = addLabel();
		this.outputList.add("LABEL " + label1);
		String label2 = addLabel();
		Node comp = (Node) visit(ctx.cond());
        
        //push the corresponding labels to breakStack and continueStack
        this.breakStack.push("JUMP "+ label2);
        this.continueStack.push("JUMP "+ label1);
		this.outputList.add(comp.content + " " + label2);
		visit(ctx.aug_stmt_list());
		this.outputList.add("JUMP " + label1);
		this.outputList.add("LABEL " + label2);
		return null;
	}

	public Node visitCond(@NotNull LexParser.CondContext ctx) {
		Node exprnode = visit(ctx.expr());
		Node exprnode1 = visit(ctx.expr1()); // recompile for visitexpr1
		String comptype = ctx.compop().getText();
        if (exprnode.type == 1){
		  if (comptype.equalsIgnoreCase("<")) {
			return new Node("GEI " + exprnode.content + " " + exprnode1.content, 4);
		  }
		  if (comptype.equalsIgnoreCase(">")) {
			return new Node("LEI " + exprnode.content + " " + exprnode1.content, 4);
		  }
		  if (comptype.equalsIgnoreCase("=")) {
			return new Node("NEI " + exprnode.content + " " + exprnode1.content, 4);
		  }
		  if (comptype.equalsIgnoreCase("!=")) {
			return new Node("EQI " + exprnode.content + " " + exprnode1.content, 4);
		  }
		  if (comptype.equalsIgnoreCase("<=")) {
			return new Node("GTI " + exprnode.content + " " + exprnode1.content, 4);
		  }
		  if (comptype.equalsIgnoreCase(">=")) {
			return new Node("LTI " + exprnode.content + " " + exprnode1.content, 4);
		  }
        }
        else{
		  if (comptype.equalsIgnoreCase("<")) {
			return new Node("GEF " + exprnode.content + " " + exprnode1.content, 4);
		  }
		  if (comptype.equalsIgnoreCase(">")) {
			return new Node("LEF " + exprnode.content + " " + exprnode1.content, 4);
		  }
		  if (comptype.equalsIgnoreCase("=")) {
			return new Node("NEF " + exprnode.content + " " + exprnode1.content, 4);
		  }
		  if (comptype.equalsIgnoreCase("!=")) {
			return new Node("EQF " + exprnode.content + " " + exprnode1.content, 4);
		  }
		  if (comptype.equalsIgnoreCase("<=")) {
			return new Node("GTF " + exprnode.content + " " + exprnode1.content, 4);
		  }
		  if (comptype.equalsIgnoreCase(">=")) {
			return new Node("LTF " + exprnode.content + " " + exprnode1.content, 4);
		  }
        }
        return null; 
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
          //System.out.println(this.breakStack.peek());
          this.outputList.add((String)this.breakStack.peek());
          return null;
      }  
      if (ctx.getText().contains("CONTINUE")) {
        this.outputList.add((String)this.continueStack.peek());
        return null;
      }
      return null; 
    }

	public String addLabel() {
		labelCount += 1;
		return ("label" + Integer.toString(labelCount));

	}

	public String createTemp() {
		this.tempIndex += 1;
		((ArrayList)this.tempMap.get(this.functionRecord)).add("$T"
				+ Integer.toString(this.tempIndex));
		return "$T" + Integer.toString(this.tempIndex);
	}

	// clear
	public Node resolve(ArrayList<Node> input) {
		while (input.size() >= 3) {
			Node op1 = (Node) input.get(0);
			Node op = (Node) input.get(1);
			Node op2 = (Node) input.get(2);

			Node newNode = new Node(createTemp(), op1.type);
			if (op.content.equalsIgnoreCase("+")) {
				if (op1.type == 1) {
					String output = "ADDI " + op1.content + " " + op2.content
							+ " " + newNode.content;
					this.outputList.add(output);
				} else {
					String output = "ADDF " + op1.content + " " + op2.content
							+ " " + newNode.content;
					this.outputList.add(output);
				}

			} else if (op.content.equalsIgnoreCase("-")) {
				if (op1.type == 1) {
					String output = "SUBI " + op1.content + " " + op2.content
							+ " " + newNode.content;
					this.outputList.add(output);
				} else {
					String output = "SUBF " + op1.content + " " + op2.content
							+ " " + newNode.content;
					this.outputList.add(output);
				}

			} else if (op.content.equalsIgnoreCase("*")) {
				if (op1.type == 1) {
					String output = "MULTI " + op1.content + " " + op2.content
							+ " " + newNode.content;
					this.outputList.add(output);
				} else {
					String output = "MULTF " + op1.content + " " + op2.content
							+ " " + newNode.content;
					this.outputList.add(output);
				}

			} else if (op1.type == 1) {
				String output = "DIVI " + op1.content + " " + op2.content + " "
						+ newNode.content;
				this.outputList.add(output);
			} else {
				String output = "DIVF " + op1.content + " " + op2.content + " "
						+ newNode.content;
				this.outputList.add(output);
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
		for (int i = 0; i < this.outputList.size() - 1; i++) {

			result = result + ";" + (String) this.outputList.get(i);
			result = result + "\n";
		}
		result = result + ";"
				+ (String) this.outputList.get(outputList.size() - 1);
		System.out.println(";IR code\n" + result);
		return result;
	}

}
