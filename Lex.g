grammar Lex;

options {
    k = 2;
    language = Java;
}
@header {
    import java.util.LinkedHashMap;
    import java.util.Map;
    import java.util.Iterator;
    import java.util.Set; 
}
@members {
    public static SymbolTable symtab = new SymbolTable();
    public static Map<String, String> functionMap = new LinkedHashMap<String, String>();
    int count = 0;
    Scope currscope;
	int paramvarCount = 0;
	int localvarCount = 0;    
}

/* Program */
@rulecatch {
 	catch (RecognitionException e) {
        System.out.println("Error caught");
     	throw e;
	}
}
program           
	:	'PROGRAM' id 'BEGIN' pgm_body 'END'         
//		{ for(String str: symtab.checkDuplicate())
//              System.out.println("SHADOW WARNING "+str);
//          System.out.println(symtab.toString());
//          Set entrySet = functionMap.entrySet();
//          Iterator it = entrySet.iterator();
//          while(it.hasNext())
//              System.out.println(it.next());
              //{  
                //System.out.println(symtab.toString());
                //System.out.println("Local Count" + localvarCount);
				//System.out.println("Parm Count" + paramvarCount);}  
	;
id
	:	IDENTIFIER
	;
pgm_body          
	:	 decl func_declarations
	;

decl		      
	: string_decl decl
    | var_decl decl 
    | /* */
	;

/* Global String Declaration */
string_decl       
	:	'STRING' id ':=' str ';' {  currscope = symtab.popScope();
	                                if(!currscope.Scopetype.equalsIgnoreCase("GLOBAL")) {
                                        ++localvarCount; 									
							            currscope.addsymbol($id.text, "STRING", $str.text, "@L" + Integer.toString(localvarCount)); 
									}
                                    else 
							            currscope.addsymbol($id.text, "STRING", $str.text);                                  
                                    symtab.pushScopeBack(currscope); }
	;
str               
	: STRINGLITERAL
;


/* Variable Declaration */
var_decl          
	: var_type id_list ';' {  currscope = symtab.popScope();
	                          if(!currscope.Scopetype.equalsIgnoreCase("GLOBAL")) {
								  String[] idlist = ($id_list.text).split(",");
								  int i;
		                          for (i = 0; i < idlist.length; i++) {
								      ++localvarCount; 
								      currscope.addsymbol(idlist[i].trim(), $var_type.text, null, "@L" + Integer.toString(localvarCount)); 
								  }
							  }
                              else 
							      currscope.addsymbols($id_list.text, $var_type.text, null);
                              symtab.pushScopeBack(currscope); }
	;
var_type	      
	:	'FLOAT' | 'INT'
	;
any_type          
	:	var_type | 'VOID'
	; 
id_list           
	:	id id_tail
	;
id_tail  
   : ',' id id_tail 
   | /* */
   ;

/* Function Paramater List */
param_decl_list   
    : param_decl param_decl_tail
    | /* */
    ;

param_decl        
	: var_type id {  
                     currscope = symtab.popScope();
					 ++paramvarCount;
					 currscope.addsymbol($id.text,$var_type.text, null, "@P" + Integer.toString(paramvarCount));
                     symtab.pushScopeBack(currscope); }
	;


param_decl_tail
    : ',' param_decl param_decl_tail 
    | /* */
    ;



/* Function Declarations */
func_declarations 
    : func_decl func_declarations 
    | /* */
    ;


func_decl  
    :  'FUNCTION' any_type id { if($any_type.text.equalsIgnoreCase("INT")) functionMap.put($id.text, "I");
	                            if($any_type.text.equalsIgnoreCase("FLOAT")) functionMap.put($id.text, "F");
								if ($any_type.text.equalsIgnoreCase("VOID")) functionMap.put($id.text, "V");
	                            symtab.pushScope($id.text);
								localvarCount = 0;
								paramvarCount = 0;}'(' param_decl_list')' 'BEGIN' func_body 'END'
    ;



func_body         
	:	decl stmt_list
    ; 

/* Statement List */

stmt_list
    : stmt stmt_list 
    | /* */
    ;


stmt             
    :	base_stmt | if_stmt | while_stmt
	;
base_stmt         
    :	assign_stmt | read_stmt | write_stmt | return_stmt
	;

/* Basic Statements */
assign_stmt       
    :	assign_expr ';'
	;
assign_expr       
    :	id ':=' expr
	;
read_stmt         
    :	'READ' '(' id_list ')' ';'
	;
write_stmt        
    :	'WRITE' '(' id_list ')' ';'
	;
return_stmt       
    :	'RETURN' expr ';'
	;

/* Expressions */
expr
    : expr_prefix factor
    ;

expr_prefix
    : expr_prefix factor addop
    | /* */
    ;

factor
    : factor_prefix postfix_expr
    ;

factor_prefix
    : factor_prefix postfix_expr mulop
    | /* */
    ;



postfix_expr      
	:	primary | call_expr
	;
call_expr         
  :	id '(' expr_list ')'
	;
expr_list         
    :	expr expr_list_tail
    |   /* */
	;
 
expr_list_tail
     : ',' expr expr_list_tail 
     | /* */
     ;


primary           
    :	'(' expr ')' | id | INTLITERAL | FLOATLITERAL
	;
addop             
    :	'+' | '-'
	;
mulop             
    :	'*' | '/'
	;





/* Complex Statements and Condition */ 
if_stmt
    : { count = count + 1;
        symtab.pushScope("BLOCK "+ String.valueOf(count));} 'IF' '(' cond ')' decl stmt_list else_part 'ENDIF'
    ;


else_part 
    :  { count = count + 1;
         symtab.pushScope("BLOCK "+ String.valueOf(count));} 'ELSE' decl stmt_list 
    | /* */
    ;


cond              
    :	expr compop expr1
	;

expr1
   : expr
   ;

compop            
    :	'<' | '>' | '=' | '!=' | '<=' | '>='
	;

/* ECE 573 students use this version of do_while_stmt */
while_stmt
	:	{ count = count + 1;
          symtab.pushScope("BLOCK "+ String.valueOf(count));} 'WHILE' '(' cond ')' decl aug_stmt_list 'ENDWHILE'
    ;

/* CONTINUE and BREAK statements. ECE 573 students only */
aug_stmt_list 
    : aug_stmt aug_stmt_list 
    | /* */
    ;

aug_stmt          
    :	base_stmt | aug_if_stmt | while_stmt | 'CONTINUE' ';' | 'BREAK' ';'
	;

/* Augmented IF statements for ECE 573 students */ 
aug_if_stmt 
    : { count = count + 1;
        symtab.pushScope("BLOCK "+ String.valueOf(count));} 'IF' '(' cond ')' decl aug_stmt_list aug_else_part 'ENDIF'
    ;


aug_else_part 
    : 	{ count = count + 1;
          symtab.pushScope("BLOCK "+ String.valueOf(count));} 'ELSE'  decl aug_stmt_list
    | /* */
    ;



// Parser Rules

// Lexer Rules

KEYWORD : ('PROGRAM'|'BEGIN'|'END'|'FUNCTION'|'READ'|'WRITE'|
'IF'|'ELSE'|'ENDIF'|'WHILE'|'ENDWHILE'|'CONTINUE'|'BREAK'|
'RETURN'|'INT'|'VOID'|'STRING'|'FLOAT');

IDENTIFIER : ('_'|WORDS)('_'|WORDS|DIGITS)* ;

OPERATOR : ('=' | '+' |'-' | '*' | '/' | '=='| '!=' | '<' | '>' | '(' |')'| '<=' | '>='| ':=' | ';' | ',');

INTLITERAL : [0-9]+ ;

FLOATLITERAL :((DIGITS)*'.'DIGITS+)|((DIGITS)+'.'); 

STRINGLITERAL : '"' .*? '"' ;

DIGITS : [0-9] ;

WORDS : [a-zA-Z]+ ;

COMMENT : '--'~('\n')*'\n'->channel(HIDDEN);

//OPERATOR : ('=' | '+' |'-' | '*' | '/' | '=='| '!=' | '<' | '>' | '(' |')'| '<=' | '>='| ':=' | ';' | ',');

WS : (' ' | '\t' | '\r'| '\n' | '\f')+ ->channel(HIDDEN);
