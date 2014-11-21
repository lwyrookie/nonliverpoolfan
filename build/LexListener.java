// Generated from Lex.g by ANTLR 4.4

    import java.util.LinkedHashMap;
    import java.util.Map;
    import java.util.Iterator;
    import java.util.Set; 

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link LexParser}.
 */
public interface LexListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link LexParser#expr_list_tail}.
	 * @param ctx the parse tree
	 */
	void enterExpr_list_tail(@NotNull LexParser.Expr_list_tailContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexParser#expr_list_tail}.
	 * @param ctx the parse tree
	 */
	void exitExpr_list_tail(@NotNull LexParser.Expr_list_tailContext ctx);
	/**
	 * Enter a parse tree produced by {@link LexParser#id_list}.
	 * @param ctx the parse tree
	 */
	void enterId_list(@NotNull LexParser.Id_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexParser#id_list}.
	 * @param ctx the parse tree
	 */
	void exitId_list(@NotNull LexParser.Id_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link LexParser#decl}.
	 * @param ctx the parse tree
	 */
	void enterDecl(@NotNull LexParser.DeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexParser#decl}.
	 * @param ctx the parse tree
	 */
	void exitDecl(@NotNull LexParser.DeclContext ctx);
	/**
	 * Enter a parse tree produced by {@link LexParser#param_decl_list}.
	 * @param ctx the parse tree
	 */
	void enterParam_decl_list(@NotNull LexParser.Param_decl_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexParser#param_decl_list}.
	 * @param ctx the parse tree
	 */
	void exitParam_decl_list(@NotNull LexParser.Param_decl_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link LexParser#assign_expr}.
	 * @param ctx the parse tree
	 */
	void enterAssign_expr(@NotNull LexParser.Assign_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexParser#assign_expr}.
	 * @param ctx the parse tree
	 */
	void exitAssign_expr(@NotNull LexParser.Assign_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link LexParser#any_type}.
	 * @param ctx the parse tree
	 */
	void enterAny_type(@NotNull LexParser.Any_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexParser#any_type}.
	 * @param ctx the parse tree
	 */
	void exitAny_type(@NotNull LexParser.Any_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link LexParser#postfix_expr}.
	 * @param ctx the parse tree
	 */
	void enterPostfix_expr(@NotNull LexParser.Postfix_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexParser#postfix_expr}.
	 * @param ctx the parse tree
	 */
	void exitPostfix_expr(@NotNull LexParser.Postfix_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link LexParser#addop}.
	 * @param ctx the parse tree
	 */
	void enterAddop(@NotNull LexParser.AddopContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexParser#addop}.
	 * @param ctx the parse tree
	 */
	void exitAddop(@NotNull LexParser.AddopContext ctx);
	/**
	 * Enter a parse tree produced by {@link LexParser#if_stmt}.
	 * @param ctx the parse tree
	 */
	void enterIf_stmt(@NotNull LexParser.If_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexParser#if_stmt}.
	 * @param ctx the parse tree
	 */
	void exitIf_stmt(@NotNull LexParser.If_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link LexParser#func_declarations}.
	 * @param ctx the parse tree
	 */
	void enterFunc_declarations(@NotNull LexParser.Func_declarationsContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexParser#func_declarations}.
	 * @param ctx the parse tree
	 */
	void exitFunc_declarations(@NotNull LexParser.Func_declarationsContext ctx);
	/**
	 * Enter a parse tree produced by {@link LexParser#else_part}.
	 * @param ctx the parse tree
	 */
	void enterElse_part(@NotNull LexParser.Else_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexParser#else_part}.
	 * @param ctx the parse tree
	 */
	void exitElse_part(@NotNull LexParser.Else_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link LexParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(@NotNull LexParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(@NotNull LexParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link LexParser#cond}.
	 * @param ctx the parse tree
	 */
	void enterCond(@NotNull LexParser.CondContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexParser#cond}.
	 * @param ctx the parse tree
	 */
	void exitCond(@NotNull LexParser.CondContext ctx);
	/**
	 * Enter a parse tree produced by {@link LexParser#base_stmt}.
	 * @param ctx the parse tree
	 */
	void enterBase_stmt(@NotNull LexParser.Base_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexParser#base_stmt}.
	 * @param ctx the parse tree
	 */
	void exitBase_stmt(@NotNull LexParser.Base_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link LexParser#func_body}.
	 * @param ctx the parse tree
	 */
	void enterFunc_body(@NotNull LexParser.Func_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexParser#func_body}.
	 * @param ctx the parse tree
	 */
	void exitFunc_body(@NotNull LexParser.Func_bodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link LexParser#compop}.
	 * @param ctx the parse tree
	 */
	void enterCompop(@NotNull LexParser.CompopContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexParser#compop}.
	 * @param ctx the parse tree
	 */
	void exitCompop(@NotNull LexParser.CompopContext ctx);
	/**
	 * Enter a parse tree produced by {@link LexParser#var_type}.
	 * @param ctx the parse tree
	 */
	void enterVar_type(@NotNull LexParser.Var_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexParser#var_type}.
	 * @param ctx the parse tree
	 */
	void exitVar_type(@NotNull LexParser.Var_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link LexParser#mulop}.
	 * @param ctx the parse tree
	 */
	void enterMulop(@NotNull LexParser.MulopContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexParser#mulop}.
	 * @param ctx the parse tree
	 */
	void exitMulop(@NotNull LexParser.MulopContext ctx);
	/**
	 * Enter a parse tree produced by {@link LexParser#expr_list}.
	 * @param ctx the parse tree
	 */
	void enterExpr_list(@NotNull LexParser.Expr_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexParser#expr_list}.
	 * @param ctx the parse tree
	 */
	void exitExpr_list(@NotNull LexParser.Expr_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link LexParser#stmt_list}.
	 * @param ctx the parse tree
	 */
	void enterStmt_list(@NotNull LexParser.Stmt_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexParser#stmt_list}.
	 * @param ctx the parse tree
	 */
	void exitStmt_list(@NotNull LexParser.Stmt_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link LexParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(@NotNull LexParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(@NotNull LexParser.ExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link LexParser#id}.
	 * @param ctx the parse tree
	 */
	void enterId(@NotNull LexParser.IdContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexParser#id}.
	 * @param ctx the parse tree
	 */
	void exitId(@NotNull LexParser.IdContext ctx);
	/**
	 * Enter a parse tree produced by {@link LexParser#expr1}.
	 * @param ctx the parse tree
	 */
	void enterExpr1(@NotNull LexParser.Expr1Context ctx);
	/**
	 * Exit a parse tree produced by {@link LexParser#expr1}.
	 * @param ctx the parse tree
	 */
	void exitExpr1(@NotNull LexParser.Expr1Context ctx);
	/**
	 * Enter a parse tree produced by {@link LexParser#factor}.
	 * @param ctx the parse tree
	 */
	void enterFactor(@NotNull LexParser.FactorContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexParser#factor}.
	 * @param ctx the parse tree
	 */
	void exitFactor(@NotNull LexParser.FactorContext ctx);
	/**
	 * Enter a parse tree produced by {@link LexParser#assign_stmt}.
	 * @param ctx the parse tree
	 */
	void enterAssign_stmt(@NotNull LexParser.Assign_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexParser#assign_stmt}.
	 * @param ctx the parse tree
	 */
	void exitAssign_stmt(@NotNull LexParser.Assign_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link LexParser#return_stmt}.
	 * @param ctx the parse tree
	 */
	void enterReturn_stmt(@NotNull LexParser.Return_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexParser#return_stmt}.
	 * @param ctx the parse tree
	 */
	void exitReturn_stmt(@NotNull LexParser.Return_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link LexParser#aug_if_stmt}.
	 * @param ctx the parse tree
	 */
	void enterAug_if_stmt(@NotNull LexParser.Aug_if_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexParser#aug_if_stmt}.
	 * @param ctx the parse tree
	 */
	void exitAug_if_stmt(@NotNull LexParser.Aug_if_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link LexParser#while_stmt}.
	 * @param ctx the parse tree
	 */
	void enterWhile_stmt(@NotNull LexParser.While_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexParser#while_stmt}.
	 * @param ctx the parse tree
	 */
	void exitWhile_stmt(@NotNull LexParser.While_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link LexParser#aug_else_part}.
	 * @param ctx the parse tree
	 */
	void enterAug_else_part(@NotNull LexParser.Aug_else_partContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexParser#aug_else_part}.
	 * @param ctx the parse tree
	 */
	void exitAug_else_part(@NotNull LexParser.Aug_else_partContext ctx);
	/**
	 * Enter a parse tree produced by {@link LexParser#pgm_body}.
	 * @param ctx the parse tree
	 */
	void enterPgm_body(@NotNull LexParser.Pgm_bodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexParser#pgm_body}.
	 * @param ctx the parse tree
	 */
	void exitPgm_body(@NotNull LexParser.Pgm_bodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link LexParser#param_decl}.
	 * @param ctx the parse tree
	 */
	void enterParam_decl(@NotNull LexParser.Param_declContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexParser#param_decl}.
	 * @param ctx the parse tree
	 */
	void exitParam_decl(@NotNull LexParser.Param_declContext ctx);
	/**
	 * Enter a parse tree produced by {@link LexParser#call_expr}.
	 * @param ctx the parse tree
	 */
	void enterCall_expr(@NotNull LexParser.Call_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexParser#call_expr}.
	 * @param ctx the parse tree
	 */
	void exitCall_expr(@NotNull LexParser.Call_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link LexParser#string_decl}.
	 * @param ctx the parse tree
	 */
	void enterString_decl(@NotNull LexParser.String_declContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexParser#string_decl}.
	 * @param ctx the parse tree
	 */
	void exitString_decl(@NotNull LexParser.String_declContext ctx);
	/**
	 * Enter a parse tree produced by {@link LexParser#aug_stmt}.
	 * @param ctx the parse tree
	 */
	void enterAug_stmt(@NotNull LexParser.Aug_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexParser#aug_stmt}.
	 * @param ctx the parse tree
	 */
	void exitAug_stmt(@NotNull LexParser.Aug_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link LexParser#aug_stmt_list}.
	 * @param ctx the parse tree
	 */
	void enterAug_stmt_list(@NotNull LexParser.Aug_stmt_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexParser#aug_stmt_list}.
	 * @param ctx the parse tree
	 */
	void exitAug_stmt_list(@NotNull LexParser.Aug_stmt_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link LexParser#str}.
	 * @param ctx the parse tree
	 */
	void enterStr(@NotNull LexParser.StrContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexParser#str}.
	 * @param ctx the parse tree
	 */
	void exitStr(@NotNull LexParser.StrContext ctx);
	/**
	 * Enter a parse tree produced by {@link LexParser#read_stmt}.
	 * @param ctx the parse tree
	 */
	void enterRead_stmt(@NotNull LexParser.Read_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexParser#read_stmt}.
	 * @param ctx the parse tree
	 */
	void exitRead_stmt(@NotNull LexParser.Read_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link LexParser#func_decl}.
	 * @param ctx the parse tree
	 */
	void enterFunc_decl(@NotNull LexParser.Func_declContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexParser#func_decl}.
	 * @param ctx the parse tree
	 */
	void exitFunc_decl(@NotNull LexParser.Func_declContext ctx);
	/**
	 * Enter a parse tree produced by {@link LexParser#factor_prefix}.
	 * @param ctx the parse tree
	 */
	void enterFactor_prefix(@NotNull LexParser.Factor_prefixContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexParser#factor_prefix}.
	 * @param ctx the parse tree
	 */
	void exitFactor_prefix(@NotNull LexParser.Factor_prefixContext ctx);
	/**
	 * Enter a parse tree produced by {@link LexParser#id_tail}.
	 * @param ctx the parse tree
	 */
	void enterId_tail(@NotNull LexParser.Id_tailContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexParser#id_tail}.
	 * @param ctx the parse tree
	 */
	void exitId_tail(@NotNull LexParser.Id_tailContext ctx);
	/**
	 * Enter a parse tree produced by {@link LexParser#var_decl}.
	 * @param ctx the parse tree
	 */
	void enterVar_decl(@NotNull LexParser.Var_declContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexParser#var_decl}.
	 * @param ctx the parse tree
	 */
	void exitVar_decl(@NotNull LexParser.Var_declContext ctx);
	/**
	 * Enter a parse tree produced by {@link LexParser#param_decl_tail}.
	 * @param ctx the parse tree
	 */
	void enterParam_decl_tail(@NotNull LexParser.Param_decl_tailContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexParser#param_decl_tail}.
	 * @param ctx the parse tree
	 */
	void exitParam_decl_tail(@NotNull LexParser.Param_decl_tailContext ctx);
	/**
	 * Enter a parse tree produced by {@link LexParser#write_stmt}.
	 * @param ctx the parse tree
	 */
	void enterWrite_stmt(@NotNull LexParser.Write_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexParser#write_stmt}.
	 * @param ctx the parse tree
	 */
	void exitWrite_stmt(@NotNull LexParser.Write_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link LexParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterStmt(@NotNull LexParser.StmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitStmt(@NotNull LexParser.StmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link LexParser#expr_prefix}.
	 * @param ctx the parse tree
	 */
	void enterExpr_prefix(@NotNull LexParser.Expr_prefixContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexParser#expr_prefix}.
	 * @param ctx the parse tree
	 */
	void exitExpr_prefix(@NotNull LexParser.Expr_prefixContext ctx);
	/**
	 * Enter a parse tree produced by {@link LexParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterPrimary(@NotNull LexParser.PrimaryContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitPrimary(@NotNull LexParser.PrimaryContext ctx);
}