import java.io.IOException;
import java.io.PrintStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Micro
{
  /*public static Node findIdNode(String id, String scopeName) {
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
  }*/













  public static void main(String[] args)
  {
    try
    {
	  //CharStream inputstream = new ANTLRFileStream(args[0].trim());      
      ANTLRFileStream inputstream = new ANTLRFileStream(args[0].trim());
      LexLexer lexer = new LexLexer(inputstream);
      TokenStream tokenStream = new CommonTokenStream(lexer);
      LexParser parser = new LexParser(tokenStream);
      parser.setErrorHandler(new MyErrorStrategy());
      //parser.program();
     

  
      ParseTree tree = parser.program();
      /*Node node = Micro.findIdNode("n", "F");
      System.out.println(node);
      node = Micro.findIdNode("haha", "F");
      System.out.println(node);
      node = Micro.findIdNode("input", "main");
      System.out.println(node);
      node = Micro.findIdNode("hahaha", "GLOBAL");
      System.out.println(node);*/
      ParseTreeWalker walker = new ParseTreeWalker();



/*reactivate following*/
   //    SuperVisitor visitor = new SuperVisitor(LexParser.symtab, LexParser.functionMap); //?
  //    visitor.visit(tree);
  //    visitor.return3AC();
  //    CodeTrans ircode = new CodeTrans(visitor.outputList, LexParser.symtab, visitor.tableMap, visitor.tempMap);
  //   ircode.generateTiny();
  //    ircode.printTiny();



    }
    catch (IOException e)
    {
      System.out.println("file not found");
    }
    /*catch (ArrayIndexOutOfBoundsException e)
    {
      System.out.println("You didn't include the argument");
    }*/
    catch (IllegalMonitorStateException e)
    {
      System.out.println("Not accepted");
    }
    catch (IllegalArgumentException e)
    {
      System.out.println(e.getMessage());
    }
  }
}


