import java.io.PrintStream;
import java.util.*;
import org.antlr.v4.runtime.tree.TerminalNode;

public class SuperVisitor extends LexBaseVisitor
{

    public SuperVisitor(SymbolTable symboltable, Map map)
    {
        outputList = new ArrayList();
        factorStack = new Stack();
        exprStack = new Stack();
        breakStack = new Stack();
        continueStack = new Stack();
        tableMap = new LinkedHashMap();
        functionMap = new LinkedHashMap();
        tempMap = new LinkedHashMap();
        functionPushCountStack = new Stack();
        functionStack = new Stack();
        functionRecord = "GLOBAL";
        labelCount = 0;
        pushCount = 0;
        localIndex = 0;
        tempIndex = 0;
        paramIndex = 0;
        compOp = new  Object()     /* anonymous class not found */
    class _anm1 {}

;
        functionMap = map;
        localIndex = 0;
        paramIndex = 0;
    }

    public Node findIdNode(String s, String s1)
    {
        String s2 = "I";
        for(Iterator iterator = LexParser.symtab.scopestack.subList(0, LexParser.symtab.scopestack.size()).iterator(); iterator.hasNext();)
        {
            Scope scope = (Scope)iterator.next();
            if(scope.Scopetype.equalsIgnoreCase(s1))
            {
                Symbol symbol = (Symbol)scope.symbolMap.get(s);
                if(symbol == null && scope.Scopetype.equalsIgnoreCase("GLOBAL"))
                {
                    System.out.println((new StringBuilder()).append(" variable ").append(s).append(" not found in anyscope in findIdNode").toString());
                    return null;
                }
                if(symbol != null)
                {
                    if(symbol.type.contains("INT"))
                        s2 = "I";
                    if(symbol.type.contains("FLOAT"))
                        s2 = "F";
                    if(symbol.type.contains("STRING"))
                        s2 = "S";
                    if(symbol.nodePrefix == null)
                        return new Node(s, s2);
                    else
                        return new Node(symbol.nodePrefix, s2);
                }
            }
        }

        return findIdNode(s, "GLOBAL");
    }

    public Node visitPrimary(LexParser.PrimaryContext primarycontext)
    {
        if(primarycontext.expr() != null)
            return (Node)visit(primarycontext.expr());
        if(primarycontext.id() != null)
            return findIdNode(primarycontext.id().getText(), functionRecord);
        if(primarycontext.INTLITERAL() != null)
        {
            Node node = new Node(createTemp(), "I");
            outputList.add((new StringBuilder()).append("STOREI ").append(primarycontext.INTLITERAL().getText()).append(" ").append(node.content).toString());
            return node;
        } else
        {
            Node node1 = new Node(createTemp(), "F");
            outputList.add((new StringBuilder()).append("STOREF ").append(primarycontext.FLOATLITERAL().getText()).append(" ").append(node1.content).toString());
            return node1;
        }
    }

    public Node visitFunc_decl(LexParser.Func_declContext func_declcontext)
    {
        ArrayList arraylist = new ArrayList();
        outputList.add((new StringBuilder()).append("LABEL ").append(func_declcontext.id().getText()).toString());
        functionRecord = func_declcontext.id().getText();
        tempMap.put(functionRecord, arraylist);
        outputList.add("LINK ");
        visitChildren(func_declcontext);
        createTemp();
        if(func_declcontext.any_type().getText().equals("VOID"))
            outputList.add("RET");
        return null;
    }

    public Node visitCall_expr(LexParser.Call_exprContext call_exprcontext)
    {
        Stack stack = new Stack();
        functionPushCountStack.push(Integer.valueOf(pushCount));
        pushCount = 0;
        if(call_exprcontext.expr_list() != null)
            visit(call_exprcontext.expr_list());
        outputList.add("PUSH ");
        for(int i = 0; i < pushCount; i++)
            stack.push((String)functionStack.pop());

        for(int j = 0; j < pushCount; j++)
            outputList.add((new StringBuilder()).append("PUSH ").append((String)stack.pop()).toString());

        outputList.add((new StringBuilder()).append("JSR ").append(call_exprcontext.id().getText()).toString());
        for(int k = 0; k < pushCount; k++)
            outputList.add("POP ");

        pushCount = ((Integer)functionPushCountStack.pop()).intValue();
        Node node = new Node(createTemp(), (String)functionMap.get(functionRecord));
        outputList.add((new StringBuilder()).append("POP ").append(node.content).toString());
        return node;
    }

    public Node visitExpr_list(LexParser.Expr_listContext expr_listcontext)
    {
        Node node = (Node)visit(expr_listcontext.expr());
        functionStack.push(node.content);
        pushCount++;
        if(!"".equals(expr_listcontext.expr_list_tail().getText()))
            visit(expr_listcontext.expr_list_tail());
        return null;
    }

    public Node visitExpr_list_tail(LexParser.Expr_list_tailContext expr_list_tailcontext)
    {
        Node node = (Node)visit(expr_list_tailcontext.expr());
        functionStack.push(node.content);
        pushCount++;
        if(!"".equals(expr_list_tailcontext.expr_list_tail().getText()))
            visit(expr_list_tailcontext.expr_list_tail());
        return null;
    }

    public Node visitReturn_stmt(LexParser.Return_stmtContext return_stmtcontext)
    {
        Node node = (Node)visit(return_stmtcontext.expr());
        Node node1 = new Node(createTemp(), node.type);
        outputList.add((new StringBuilder()).append("STORE").append(node.type.trim()).append(" ").append(node).append(" ").append(node1).toString());
        outputList.add((new StringBuilder()).append("STORE").append(node.type.trim()).append(" ").append(node1).append(" $R").toString());
        outputList.add("RET");
        return null;
    }

    public Node visitExpr(LexParser.ExprContext exprcontext)
    {
        if(!"".equals(exprcontext.expr_prefix().getText()))
        {
            ArrayList arraylist = new ArrayList();
            exprStack.push(arraylist);
            Node node1 = (Node)visit(exprcontext.expr_prefix());
            Node node2 = (Node)visit(exprcontext.factor());
            ((ArrayList)exprStack.peek()).add(node2);
            Node node3 = resolve((ArrayList)exprStack.pop());
            return node3;
        } else
        {
            Node node = (Node)visit(exprcontext.factor());
            return node;
        }
    }

    public Node visitExpr_prefix(LexParser.Expr_prefixContext expr_prefixcontext)
    {
        if(!"".equals(expr_prefixcontext.expr_prefix().getText()))
            visit(expr_prefixcontext.expr_prefix());
        Node node = new Node(expr_prefixcontext.addop().getText(), "Op");
        Node node1 = (Node)visit(expr_prefixcontext.factor());
        ((ArrayList)exprStack.peek()).add(node1);
        ((ArrayList)exprStack.peek()).add(node);
        return null;
    }

    public Node visitFactor(LexParser.FactorContext factorcontext)
    {
        if(!"".equals(factorcontext.factor_prefix().getText()))
        {
            ArrayList arraylist = new ArrayList();
            factorStack.push(arraylist);
            Node node = (Node)visit(factorcontext.factor_prefix());
            Node node1 = (Node)visit(factorcontext.postfix_expr());
            ((ArrayList)factorStack.peek()).add(node1);
            Node node2 = resolve((ArrayList)factorStack.pop());
            return node2;
        } else
        {
            return (Node)visit(factorcontext.postfix_expr());
        }
    }

    public Node visitFactor_prefix(LexParser.Factor_prefixContext factor_prefixcontext)
    {
        if(!"".equals(factor_prefixcontext.factor_prefix().getText()))
            visit(factor_prefixcontext.factor_prefix());
        Node node = new Node(factor_prefixcontext.mulop().getText(), "Op");
        Node node1 = (Node)visit(factor_prefixcontext.postfix_expr());
        ((ArrayList)factorStack.peek()).add(node1);
        ((ArrayList)factorStack.peek()).add(node);
        return null;
    }

    public Node visitWrite_stmt(LexParser.Write_stmtContext write_stmtcontext)
    {
        String as[] = write_stmtcontext.id_list().getText().split(",");
        for(int i = 0; i < as.length; i++)
        {
            Node node = findIdNode(as[i], functionRecord);
            outputList.add((new StringBuilder()).append("WRITE").append(node.type).append(" ").append(node.content).toString());
        }

        return null;
    }

    public Node visitRead_stmt(LexParser.Read_stmtContext read_stmtcontext)
    {
        String as[] = read_stmtcontext.id_list().getText().split(",");
        for(int i = 0; i < as.length; i++)
        {
            Node node = findIdNode(as[i], functionRecord);
            outputList.add((new StringBuilder()).append("READ").append(node.type).append(" ").append(node.content).toString());
        }

        return null;
    }

    public Node visitAssign_expr(LexParser.Assign_exprContext assign_exprcontext)
    {
        Node node = (Node)visit(assign_exprcontext.expr());
        Node node1 = findIdNode(assign_exprcontext.id().getText(), functionRecord);
        outputList.add((new StringBuilder()).append("STORE").append(node1.type).append(" ").append(node.content).append(" ").append(node1.content).toString());
        return null;
    }

    public Node visitIf_stmt(LexParser.If_stmtContext if_stmtcontext)
    {
        if(!"".equals(if_stmtcontext.else_part().getText()))
        {
            Node node = (Node)visit(if_stmtcontext.cond());
            String s = addLabel();
            outputList.add((new StringBuilder()).append(node.content).append(" ").append(s).toString());
            visit(if_stmtcontext.stmt_list());
            String s2 = addLabel();
            outputList.add((new StringBuilder()).append("JUMP ").append(s2).toString());
            outputList.add((new StringBuilder()).append("LABEL ").append(s).toString());
            visit(if_stmtcontext.else_part());
            outputList.add((new StringBuilder()).append("LABEL ").append(s2).toString());
        } else
        {
            Node node1 = (Node)visit(if_stmtcontext.cond());
            String s1 = addLabel();
            outputList.add((new StringBuilder()).append(node1.content).append(" ").append(s1).toString());
            visit(if_stmtcontext.stmt_list());
            outputList.add((new StringBuilder()).append("LABEL ").append(s1).toString());
        }
        return null;
    }

    public Node visitAug_if_stmt(LexParser.Aug_if_stmtContext aug_if_stmtcontext)
    {
        if(!"".equals(aug_if_stmtcontext.aug_else_part().getText()))
        {
            Node node = (Node)visit(aug_if_stmtcontext.cond());
            String s = addLabel();
            outputList.add((new StringBuilder()).append(node.content).append(" ").append(s).toString());
            visit(aug_if_stmtcontext.aug_stmt_list());
            String s2 = addLabel();
            outputList.add((new StringBuilder()).append("JUMP ").append(s2).toString());
            outputList.add((new StringBuilder()).append("LABEL ").append(s).toString());
            visit(aug_if_stmtcontext.aug_else_part());
            outputList.add((new StringBuilder()).append("LABEL ").append(s2).toString());
        } else
        {
            Node node1 = (Node)visit(aug_if_stmtcontext.cond());
            String s1 = addLabel();
            outputList.add((new StringBuilder()).append(node1.content).append(" ").append(s1).toString());
            visit(aug_if_stmtcontext.aug_stmt_list());
            outputList.add((new StringBuilder()).append("LABEL ").append(s1).toString());
        }
        return null;
    }

    public Node visitWhile_stmt(LexParser.While_stmtContext while_stmtcontext)
    {
        String s = addLabel();
        outputList.add((new StringBuilder()).append("LABEL ").append(s).toString());
        String s1 = addLabel();
        Node node = (Node)visit(while_stmtcontext.cond());
        breakStack.push((new StringBuilder()).append("JUMP ").append(s1).toString());
        continueStack.push((new StringBuilder()).append("JUMP ").append(s).toString());
        outputList.add((new StringBuilder()).append(node.content).append(" ").append(s1).toString());
        visit(while_stmtcontext.aug_stmt_list());
        outputList.add((new StringBuilder()).append("JUMP ").append(s).toString());
        outputList.add((new StringBuilder()).append("LABEL ").append(s1).toString());
        return null;
    }

    public Node visitCond(LexParser.CondContext condcontext)
    {
        Node node = (Node)visit(condcontext.expr());
        Node node1 = (Node)visit(condcontext.expr1());
        String s = condcontext.compop().getText().toUpperCase();
        return new Node((new StringBuilder()).append((String)compOp.get(s)).append(node.type).append(" ").append(node.content).append(" ").append(node1.content).toString(), (String)compOp.get(s));
    }

    public Node visitAug_stmt(LexParser.Aug_stmtContext aug_stmtcontext)
    {
        if(aug_stmtcontext.base_stmt() != null)
            return (Node)visit(aug_stmtcontext.base_stmt());
        if(aug_stmtcontext.while_stmt() != null)
            return (Node)visit(aug_stmtcontext.while_stmt());
        if(aug_stmtcontext.aug_if_stmt() != null)
            return (Node)visit(aug_stmtcontext.aug_if_stmt());
        if(aug_stmtcontext.getText().contains("BREAK"))
        {
            outputList.add((String)breakStack.peek());
            return null;
        }
        if(aug_stmtcontext.getText().contains("CONTINUE"))
        {
            outputList.add((String)continueStack.peek());
            return null;
        } else
        {
            return null;
        }
    }

    public String addLabel()
    {
        labelCount++;
        return (new StringBuilder()).append("label").append(Integer.toString(labelCount)).toString();
    }

    public String createTemp()
    {
        tempIndex++;
        ((ArrayList)tempMap.get(functionRecord)).add((new StringBuilder()).append("$T").append(Integer.toString(tempIndex)).toString());
        return (new StringBuilder()).append("$T").append(Integer.toString(tempIndex)).toString();
    }

    public Node resolve(ArrayList arraylist)
    {
        Node node4;
        for(; arraylist.size() >= 3; arraylist.add(0, node4))
        {
            Node node = (Node)arraylist.get(0);
            Node node2 = (Node)arraylist.get(1);
            Node node3 = (Node)arraylist.get(2);
            node4 = new Node(createTemp(), node.type);
            if(node2.content.equalsIgnoreCase("+"))
            {
                String s = (new StringBuilder()).append("ADD").append(node.type).append(" ").append(node.content).append(" ").append(node3.content).append(" ").append(node4.content).toString();
                outputList.add(s);
            } else
            if(node2.content.equalsIgnoreCase("-"))
            {
                String s1 = (new StringBuilder()).append("SUB").append(node.type).append(" ").append(node.content).append(" ").append(node3.content).append(" ").append(node4.content).toString();
                outputList.add(s1);
            } else
            if(node2.content.equalsIgnoreCase("*"))
            {
                String s2 = (new StringBuilder()).append("MULT").append(node.type).append(" ").append(node.content).append(" ").append(node3.content).append(" ").append(node4.content).toString();
                outputList.add(s2);
            } else
            if(node2.content.equalsIgnoreCase("/"))
            {
                String s3 = (new StringBuilder()).append("DIV").append(node.type).append(" ").append(node.content).append(" ").append(node3.content).append(" ").append(node4.content).toString();
                outputList.add(s3);
            }
            arraylist.remove(0);
            arraylist.remove(0);
            arraylist.remove(0);
        }

        Node node1 = (Node)arraylist.get(0);
        arraylist.removeAll(arraylist);
        return node1;
    }

    public String return3AC()
    {
        String s = "";
        for(int i = 0; i < outputList.size() - 1; i++)
        {
            s = (new StringBuilder()).append(s).append(";").append((String)outputList.get(i)).toString();
            s = (new StringBuilder()).append(s).append("\n").toString();
        }

        s = (new StringBuilder()).append(s).append(";").append((String)outputList.get(outputList.size() - 1)).toString();
        System.out.println((new StringBuilder()).append(";IR code\n").append(s).toString());
        return s;
    }

    public volatile Object visitExpr_list(LexParser.Expr_listContext expr_listcontext)
    {
        return visitExpr_list(expr_listcontext);
    }

    public volatile Object visitExpr_list_tail(LexParser.Expr_list_tailContext expr_list_tailcontext)
    {
        return visitExpr_list_tail(expr_list_tailcontext);
    }

    public volatile Object visitIf_stmt(LexParser.If_stmtContext if_stmtcontext)
    {
        return visitIf_stmt(if_stmtcontext);
    }

    public volatile Object visitReturn_stmt(LexParser.Return_stmtContext return_stmtcontext)
    {
        return visitReturn_stmt(return_stmtcontext);
    }

    public volatile Object visitFunc_decl(LexParser.Func_declContext func_declcontext)
    {
        return visitFunc_decl(func_declcontext);
    }

    public volatile Object visitWrite_stmt(LexParser.Write_stmtContext write_stmtcontext)
    {
        return visitWrite_stmt(write_stmtcontext);
    }

    public volatile Object visitWhile_stmt(LexParser.While_stmtContext while_stmtcontext)
    {
        return visitWhile_stmt(while_stmtcontext);
    }

    public volatile Object visitFactor(LexParser.FactorContext factorcontext)
    {
        return visitFactor(factorcontext);
    }

    public volatile Object visitAug_stmt(LexParser.Aug_stmtContext aug_stmtcontext)
    {
        return visitAug_stmt(aug_stmtcontext);
    }

    public volatile Object visitAug_if_stmt(LexParser.Aug_if_stmtContext aug_if_stmtcontext)
    {
        return visitAug_if_stmt(aug_if_stmtcontext);
    }

    public volatile Object visitExpr_prefix(LexParser.Expr_prefixContext expr_prefixcontext)
    {
        return visitExpr_prefix(expr_prefixcontext);
    }

    public volatile Object visitFactor_prefix(LexParser.Factor_prefixContext factor_prefixcontext)
    {
        return visitFactor_prefix(factor_prefixcontext);
    }

    public volatile Object visitPrimary(LexParser.PrimaryContext primarycontext)
    {
        return visitPrimary(primarycontext);
    }

    public volatile Object visitCall_expr(LexParser.Call_exprContext call_exprcontext)
    {
        return visitCall_expr(call_exprcontext);
    }

    public volatile Object visitRead_stmt(LexParser.Read_stmtContext read_stmtcontext)
    {
        return visitRead_stmt(read_stmtcontext);
    }

    public volatile Object visitAssign_expr(LexParser.Assign_exprContext assign_exprcontext)
    {
        return visitAssign_expr(assign_exprcontext);
    }

    public volatile Object visitCond(LexParser.CondContext condcontext)
    {
        return visitCond(condcontext);
    }

    public volatile Object visitExpr(LexParser.ExprContext exprcontext)
    {
        return visitExpr(exprcontext);
    }

    public ArrayList outputList;
    private Stack factorStack;
    private Stack exprStack;
    private Stack breakStack;
    private Stack continueStack;
    protected Map tableMap;
    protected Map functionMap;
    protected Map tempMap;
    private Stack functionPushCountStack;
    private Stack functionStack;
    private String functionRecord;
    private int labelCount;
    private int pushCount;
    private int localIndex;
    private int tempIndex;
    private int paramIndex;
    public Map compOp;
}