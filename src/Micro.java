import java.io.IOException;
import java.io.PrintStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Micro
{
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
      ParseTreeWalker walker = new ParseTreeWalker();
      //ExtractMicroBaseListener extractor = new ExtractMicroBaseListener(parser);
      //LexBaseListener exactor = new LexBaseListener (parser);
      //walker.walk(extractor, tree);
      SuperVisitor visitor = new SuperVisitor(LexParser.symtab, LexParser.functionMap); //?
      visitor.visit(tree);
      visitor.return3AC();
      CodeTrans ircode = new CodeTrans(LexParser.symtab,visitor.outputList);
      ircode.printIR();



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


