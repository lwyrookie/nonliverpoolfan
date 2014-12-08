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

            outputList.add((new StringBuilder()).append("JUMP ").append(lb2).toString());
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
		outputList.add("LABEL " + lbl1);
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
        Node exprNode1, exprNode2, newNode;
        String comptype;
		exprNode1 = visit(condcontext.expr());
		exprNode2 = visit(condcontext.expr1());
		comptype = condcontext.compop().getText().toUpperCase();
        newNode = new Node((new StringBuilder()).append((String)compOp.get(comptype)).append(exprnode1.type).append(" ").append(exprnode1.content).append(" ").append(exprnode2.content).toString(), (String)compOp.get(comptype));
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



	public Node visitAssign_expr(@NotNull LexParser.Assign_exprContext assign_exprcontext) {
		Node exprNode = (Node) visit(assign_exprcontext.expr());
		Node newNode = findIdNode(assign_exprcontext.id().getText(), functionRecord);
        outputList.add((new StringBuilder()).append("STORE").append(newNode.type).append(" ").append(exprNode.content).append(" ").append(newNode.content).toString());
		return null;
	}




	public Node visitCall_expr(@NotNull LexParser.Call_exprContext call_exprcontext) {
        Stack revStack = new Stack();
		Integer linkNum = Integer.valueOf(pushCount);
		trackLinkNum.add(linkNum);
		linkNum = 0;
    	//functionPushCountStack.push(Integer.valueOf(pushCount));
    	pushCount = 0;
    	if (call_exprcontext.expr_list() != null) {
      		visit(call_exprcontext.expr_list());
    	}

    	outputList.add("PUSH ");
    	for (int i = 0; i < pushCount; i++) {
            revStack.push((String)functionStack.pop());
    	}
        for (int i = 0; i < pushCount; i++) {
      		outputList.add((new StringBuilder()).append("PUSH ").append((String)revStack.pop()).toString());
    	}
    	outputList.add("JSR " + call_exprcontext.id().getText());
    	for (int i = 0; i < pushCount; i++) {
      		outputList.add("POP ");
    	}
		pushCount = trackLinkNum.remove(trackLinkNum.size()-1);
    	//pushCount = ((Integer)functionPushCountStack.pop()).intValue();

	    Node newNode = new Node(createTemp(), ((String)functionMap.get(functionRecord)));
        outputList.add((new StringBuilder()).append("POP ").append(newNode.content).toString());
   	    return newNode;
  	}







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
