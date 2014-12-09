import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;
import java.util.HashMap;

    public class CodeTrans {
	public ArrayList<String> TinyOut = new ArrayList<String>(); // Stores the TinyOutlist		
    private ArrayList<String> IR;
    private String functionName = null;
    protected Map<String, ArrayList<String>> tempMap = new LinkedHashMap();	

   protected HashMap<String,String> opCommands = new HashMap<String,String>(){{
       put("ADDI","addi"); put("SUBI","subi"); put("MULTI","muli"); put("DIVI","divi"); put("ADDF","addr"); put("SUBF","subr"); put("MULTF","mulr"); put("DIVF","divr");}};   

   protected HashMap<String, String> compCommands = new HashMap<String, String>(){{
   put("LEI","jle"); put("GEI","jge"); put("NEI","jne"); put("EQI","jeq"); put("GTI","jgt"); put("LTI","jlt"); put("LEF","jle"); put("GEF","jge"); put("NEF","jne"); put("EQF","jeq"); put("GTF","jgt"); put("LTF","jlt"); }};   
   
   protected HashMap<String,String> rwCommands = new HashMap<String,String>(){{
       put("READI","sys readi"); put("WRITEI","sys writei"); put("READF","sys readr"); put("WRITEF","sys writer"); put("WRITES","sys writes");}}; 
   

    public  CodeTrans(ArrayList<String> outputList, Map<String, ArrayList<String>> tempMap) {
       this.IR = outputList;   this.tempMap = tempMap;
      }
  
      public void generateTiny() {

         Scope scope = (LexParser.symtab).get("GLOBAL");
         for (String key : scope.symbolMap.keySet()) {
            if (scope.symbolMap.get(key).getType().contains("STRING"))
            TinyOut.add ( typeTrans(scope.symbolMap.get(key).getType())+key+" "+ scope.symbolMap.get(key).getValue()); 
            else   TinyOut.add ( typeTrans(scope.symbolMap.get(key).getType())+key);  
          }
         TinyOut.add("push");
         TinyOut.add("jsr main");
         TinyOut.add("sys halt");

		
		for (int i=0; i<IR.size(); i++){   
              
             String irNode = IR.get(i);
             String[] SubString = IR.get(i).split("\\s+");
             String command =SubString[0];


          if (opCommands.containsKey(command)){
          //   System.out.println("Check opcommand match:"+ command);
             getTinyOp(SubString);       
          }

         else if (rwCommands.containsKey(command)){
          //   System.out.println("Check rwcommand match: "+ command);
             getTinyRw(SubString);
          }
         else if (compCommands.containsKey(command)){
          //   System.out.println("Check compcommand match: "+ command);
             getTinyComp(SubString);

         }
         else if (command.contains("STORE")){
          //   System.out.println("Check store command match: "+ command);
             getTinyStore(SubString);

         }
         else if (command.contains("PUSH") || command.contains("POP")){
          //   System.out.println("Check PUSH/POP command match: "+ command);
             getTinyPp(SubString);
         }

         else if (command.contains("LINK")) {
         TinyOut.add("link " + LexParser.funcHub.get(this.functionName).locNum );
        }

         else if (command.contains("RET")) {
         TinyOut.add("unlnk");
         TinyOut.add("ret");
        }
  
         else if (command.contains("LABEL")) {
          TinyOut.add("label " + SubString[1] + " ");
         if (!SubString[1].contains("label")) {
          this.functionName = SubString[1];
        }
      }
         else if (command.contains("JUMP")) 
           TinyOut.add("jmp " + SubString[1] + " ");

       else if (command.contains("JSR")) {
           pushToMem();
           TinyOut.add("jsr " + SubString[1] ); 
           popToReg();
       }
    
     }//for end
       TinyOut.add("end");

   }

     private void pushToMem(){
       for (int i=0; i<15; i++)
       TinyOut.add("push "+"r"+Integer.toString(i));
     }
     
     private void popToReg(){
       for (int i=14; i>=0; i--)
       TinyOut.add("pop "+"r"+Integer.toString(i));
     }


     public String createTiny(String temp) {
       Function targetFunc = LexParser.funcHub.get(functionName);
       if (temp.contains("$T")){
        String r = targetFunc.tempToReg.get("$T"+Integer.toString(targetFunc.tempNum));
       if (targetFunc.tempToReg.containsKey(temp))
           return targetFunc.tempToReg.get(temp);
       else  return r;

       }

       else if ( (temp.contains("$P"))||(temp.contains("$L")) || (temp.contains("$R")) ){
           //System.out.println("%L hasspens?"+temp);
           if (targetFunc.functioninfo.containsKey(temp))
             return targetFunc.functioninfo.get(temp);
      
       }
       else  return temp;  //may have error here
       return null;

     }


      private void getTinyOp(String[] SubString){
         TinyOut.add("move " + createTiny(SubString[1]) + " " + createTiny(SubString[3]));
         TinyOut.add(opCommands.get(SubString[0]) + " "+ createTiny(SubString[2]) + " " + createTiny(SubString[3]));
      }


       private void getTinyRw(String[] SubString){
       TinyOut.add(rwCommands.get(SubString[0])+" " + createTiny(SubString[1]));
      }
       

      private void getTinyComp(String[] SubString){
            String suffix;
            if(! SubString[0].contains("I"))
            suffix ="r";
            else suffix="i";
            
            String tempReg = createTiny(SubString[2]); //may be a memory address
            if(!(SubString[2].contains("$T")))  {
               tempReg = createTiny((tempMap.get(functionName)).get((tempMap.get(functionName)).size() - 1));
               TinyOut.add("move " + createTiny(SubString[2]) + " " + tempReg);
            }
            TinyOut.add("cmp"+suffix+" " + createTiny(SubString[1]) + " " + tempReg);
            TinyOut.add(compCommands.get(SubString[0])+ " " + SubString[3]);
      }

      private void getTinyStore(String[] SubString){
         String cheatSubString = SubString[2];
          if((LexParser.symtab).get("GLOBAL").symbolMap.containsKey(SubString[1]) && (LexParser.symtab).get("GLOBAL").symbolMap.containsKey(SubString[2])){
        // System.out.println("STORE special case, both global, force change to new substring: " + cheatSubString);
               cheatSubString ="$T"+SubString[2];
               TinyOut.add("move " + createTiny(SubString[1]) + " " + createTiny(cheatSubString));  
               TinyOut.add("move " + createTiny(cheatSubString) + " " + SubString[2]); 
           }
          else
               TinyOut.add("move " + createTiny(SubString[1]) + " " + createTiny(cheatSubString));  

      }


     public  void getTinyPp(String[] SubString){
        String ppCom = SubString[0].toLowerCase();
        if (SubString.length == 1) 
          TinyOut.add(ppCom);
        else  
          TinyOut.add(ppCom+ " " + createTiny(SubString[1]) );   
      }



     private String typeTrans(String type){
        	if (type.contains("STRING"))
        		return "str ";
        	else  return "var ";
        }


    public void printTiny() {
       String result = "";
       for (int i = 0; i < TinyOut.size()-1; i++) {
         result = result + TinyOut.get(i);
         result = result + "\n";
       }
      result = result+ TinyOut.get(TinyOut.size()-1);
      System.out.println(";tiny code\n"+result);
    }
}//class end 
