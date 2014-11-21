// Generated from Lex.g by ANTLR 4.4

    import java.util.LinkedHashMap;
    import java.util.Map;
    import java.util.Iterator;
    import java.util.Set; 

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link LexParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface LexVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link LexParser#expr_list_tail}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_list_tail(@NotNull LexParser.Expr_list_tailContext ctx);
	/**
	 * Visit a parse tree produced by {@link LexParser#id_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitId_list(@NotNull LexParser.Id_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link LexParser#decl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecl(@NotNull LexParser.DeclContext ctx);
	/**
	 * Visit a parse tree produced by {@link LexParser#param_decl_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParam_decl_list(@NotNull LexParser.Param_decl_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link LexParser#assign_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssign_expr(@NotNull LexParser.Assign_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link LexParser#any_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAny_type(@NotNull LexParser.Any_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link LexParser#postfix_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPostfix_expr(@NotNull LexParser.Postfix_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link LexParser#addop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddop(@NotNull LexParser.AddopContext ctx);
	/**
	 * Visit a parse tree produced by {@link LexParser#if_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf_stmt(@NotNull LexParser.If_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link LexParser#func_declarations}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_declarations(@NotNull LexParser.Func_declarationsContext ctx);
	/**
	 * Visit a parse tree produced by {@link LexParser#else_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElse_part(@NotNull LexParser.Else_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link LexParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(@NotNull LexParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link LexParser#cond}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCond(@NotNull LexParser.CondContext ctx);
	/**
	 * Visit a parse tree produced by {@link LexParser#base_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBase_stmt(@NotNull LexParser.Base_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link LexParser#func_body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_body(@NotNull LexParser.Func_bodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link LexParser#compop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompop(@NotNull LexParser.CompopContext ctx);
	/**
	 * Visit a parse tree produced by {@link LexParser#var_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVar_type(@NotNull LexParser.Var_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link LexParser#mulop}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulop(@NotNull LexParser.MulopContext ctx);
	/**
	 * Visit a parse tree produced by {@link LexParser#expr_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_list(@NotNull LexParser.Expr_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link LexParser#stmt_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStmt_list(@NotNull LexParser.Stmt_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link LexParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr(@NotNull LexParser.ExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link LexParser#id}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitId(@NotNull LexParser.IdContext ctx);
	/**
	 * Visit a parse tree produced by {@link LexParser#expr1}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr1(@NotNull LexParser.Expr1Context ctx);
	/**
	 * Visit a parse tree produced by {@link LexParser#factor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFactor(@NotNull LexParser.FactorContext ctx);
	/**
	 * Visit a parse tree produced by {@link LexParser#assign_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssign_stmt(@NotNull LexParser.Assign_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link LexParser#return_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturn_stmt(@NotNull LexParser.Return_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link LexParser#aug_if_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAug_if_stmt(@NotNull LexParser.Aug_if_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link LexParser#while_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhile_stmt(@NotNull LexParser.While_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link LexParser#aug_else_part}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAug_else_part(@NotNull LexParser.Aug_else_partContext ctx);
	/**
	 * Visit a parse tree produced by {@link LexParser#pgm_body}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPgm_body(@NotNull LexParser.Pgm_bodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link LexParser#param_decl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParam_decl(@NotNull LexParser.Param_declContext ctx);
	/**
	 * Visit a parse tree produced by {@link LexParser#call_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCall_expr(@NotNull LexParser.Call_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link LexParser#string_decl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString_decl(@NotNull LexParser.String_declContext ctx);
	/**
	 * Visit a parse tree produced by {@link LexParser#aug_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAug_stmt(@NotNull LexParser.Aug_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link LexParser#aug_stmt_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAug_stmt_list(@NotNull LexParser.Aug_stmt_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link LexParser#str}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStr(@NotNull LexParser.StrContext ctx);
	/**
	 * Visit a parse tree produced by {@link LexParser#read_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRead_stmt(@NotNull LexParser.Read_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link LexParser#func_decl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc_decl(@NotNull LexParser.Func_declContext ctx);
	/**
	 * Visit a parse tree produced by {@link LexParser#factor_prefix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFactor_prefix(@NotNull LexParser.Factor_prefixContext ctx);
	/**
	 * Visit a parse tree produced by {@link LexParser#id_tail}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitId_tail(@NotNull LexParser.Id_tailContext ctx);
	/**
	 * Visit a parse tree produced by {@link LexParser#var_decl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVar_decl(@NotNull LexParser.Var_declContext ctx);
	/**
	 * Visit a parse tree produced by {@link LexParser#param_decl_tail}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParam_decl_tail(@NotNull LexParser.Param_decl_tailContext ctx);
	/**
	 * Visit a parse tree produced by {@link LexParser#write_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWrite_stmt(@NotNull LexParser.Write_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link LexParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStmt(@NotNull LexParser.StmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link LexParser#expr_prefix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_prefix(@NotNull LexParser.Expr_prefixContext ctx);
	/**
	 * Visit a parse tree produced by {@link LexParser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimary(@NotNull LexParser.PrimaryContext ctx);
}