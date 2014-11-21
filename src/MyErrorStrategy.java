import org.antlr.v4.runtime.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MyErrorStrategy extends DefaultErrorStrategy{
	//public MyErrorHandler(){
	//	super();
	//}
	@Override
	public void reportError(Parser parser,RecognitionException re){
		System.out.println("Not Accepted");
        System.exit(1);
	}
    @Override
    public Token recoverInline(Parser parser) throws RuntimeException{
        InputMismatchException e = new InputMismatchException(parser);
            for (ParserRuleContext context = parser.getContext(); context != null; context = context.getParent()) {
                context.exception = e;
            }

        //throw new ParseCancellationException(e);
      throw new RuntimeException(e);
    }
    @Override
    public void sync(Parser parser)
    {
    }
}
