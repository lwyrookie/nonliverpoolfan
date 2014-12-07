import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;
import java.util.HashMap;


    public class CodeTrans {
	public ArrayList<String> TinyOut = new ArrayList<String>(); // Stores the TinyOutlist
	//public LinkedHashMap<String, String> IrRegMap = new LinkedHashMap<String, String>(); //Stores the mapping between $T and r;
    //private int RegCount = -1;			



    private ArrayList<String> IR;
   // private SymbolTable table;
   // private int paramIndex; //modify
   // private int labelIndex = 0;
    private String labelIndicator = null;
  //  private int linknum = 0;   //from count
 //   private int RPosition = 0;
   // protected Map<String, Map<String, String>> TR = new LinkedHashMap();
 //   protected Map<String, Map<String, Node>> tableMap = new LinkedHashMap();
   // protected Map<String, Integer> linkCount = new LinkedHashMap();
    protected Map<String, ArrayList<String>> tempMap = new LinkedHashMap();	
   





   protected HashMap<String,String> opCommands = new HashMap<String,String>(){{
       put("ADDI","addi"); put("SUBI","subi"); put("MULTI","muli"); put("DIVI","divi"); put("ADDF","addr"); put("SUBF","subr"); put("MULTF","mulr"); put("DIVF","divr");}};   

   protected HashMap<String, String> compCommands = new HashMap<String, String>(){{
   put("LEI","jle"); put("GEI","jge"); put("NEI","jne"); put("EQI","jeq"); put("GTI","jgt"); put("LTI","jlt"); put("LEF","jle"); put("GEF","jge"); put("NEF","jne"); put("EQF","jeq"); put("GTF","jgt"); put("LTF","jlt"); }};   
   
   protected HashMap<String,String> rwCommands = new HashMap<String,String>(){{
       put("READI","sys readi"); put("WRITEI","sys writei"); put("READF","sys readr"); put("WRITEF","sys writer"); put("WRITES","sys writes");}}; 
   
  

 	

    
  



   
     //Map TR as something <scope, ($P1, $17)> as well as registernumber LOCAL temp, tempMap only stores temporary variables;
   // public  CodeTrans(ArrayList<String> outputList, SymbolTable table, Map<String, Map<String, Node>> tableMap, Map<String, ArrayList<String>> tempMap) {

public  CodeTrans(ArrayList<String> outputList, Map<String, ArrayList<String>> tempMap) {
    this.IR = outputList;
   // this.table = table; 
    //this.paramIndex = 1;  
  //  this.RPosition = this.paramIndex;
  //  this.tableMap = tableMap;
    this.tempMap = tempMap;

  /*  for (String key : this.tableMap.keySet()) {
      Map newTR = new LinkedHashMap();
    for (Node each : (tableMap.get(key)).values()) {
      

        if ((each.content.contains("$P")) && (!this.TR.containsKey(each.content))) {
          newTR.put(each.content, "$" + Integer.toString(Integer.parseInt(each.content.substring(2)) + this.paramIndex));

          this.RPosition = (Integer.parseInt(each.content.substring(2)) + this.paramIndex + 1); 
        }
        else if (each.content.contains("$L")) {
          newTR.put(each.content, "$" + Integer.toString(-Integer.parseInt(each.content.substring(2)) - this.labelIndex));
          this.linknum += 1;
        }
        newTR.put("$R", "$" + Integer.toString(this.RPosition));
        this.TR.put(key, newTR);
      }
      this.linkCount.put(key, Integer.valueOf(this.linknum));
      this.linknum = 0;
      this.RPosition = this.paramIndex+1;
}*/

   


    }
  

    										











/*

	public void generateTiny() {


		for (Scope scope : table.scopestack.subList(0, table.scopestack.size())) { //Do we need this here?
			if (scope.Scopetype.equalsIgnoreCase("GLOBAL")) { // only scope
																// global exists
				for (String key : scope.symbolMap.keySet()) {
					if (((Symbol) scope.symbolMap.get(key)).getType().contains(
							"INT")) {
						TinyOut.add("var " + key);
					}
					if (((Symbol) scope.symbolMap.get(key)).getType().contains(
							"FLOAT")) {
						TinyOut.add("var " + key);
					}
					
					if (((Symbol) scope.symbolMap.get(key)).getType().contains(
							"STRING")) {
						TinyOut.add("str " + key +" " +((Symbol) scope.symbolMap.get(key)).getValue()); //str newline "\n"
					}
					
					
				}
			}

		}

*/



       public void generateTiny() {
         Scope scope = (LexParser.symtab).get("GLOBAL");
         for (String key : scope.symbolMap.keySet()) 
            TinyOut.add ( typeTrans(scope.symbolMap.get(key).getType())+key);  
          
         TinyOut.add("push");
         TinyOut.add("jsr main");
         TinyOut.add("sys halt");

		
		for (int i=0; i<IR.size(); i++){   
              
             String irNode = IR.get(i);
             String[] SubString = IR.get(i).split("\\s+");
             String command =SubString[0];


          if (opCommands.containsKey(command)){
             System.out.println("Check opcommand match:"+ command);
             getTinyOp(SubString);       
          }

         else if (rwCommands.containsKey(command)){
             System.out.println("Check rwcommand match: "+ command);
             getTinyRw(SubString);
          }
         else if (compCommands.containsKey(command)){
             System.out.println("Check compcommand match: "+ command);
             getTinyComp(SubString);

         }
         else if (command.contains("STORE")){
             System.out.println("Check store command match: "+ command);
             getTinyStore(SubString);

         }
         else if (command.contains("PUSH") || command.contains("POP")){
             System.out.println("Check PUSH/POP command match: "+ command);
             getTinyPp(SubString);
         }

         else if (command.contains("LINK")) {
         TinyOut.add("link " + LexParser.funcHub.get(this.labelIndicator).locNum );
        }

         else if (command.contains("RET")) {
         TinyOut.add("unlnk");
         TinyOut.add("ret");
        }
  
         else if (command.contains("LABEL")) {
          TinyOut.add("label " + SubString[1] + " ");
         if (!SubString[1].contains("label")) {
          this.labelIndicator = SubString[1];
        }
      }
      else if (command.contains("JUMP")) {
        TinyOut.add("jmp " + SubString[1] + " ");
      }


     else if (command.contains("JSR")) {
          TinyOut.add("jsr " + SubString[1] ); 
      }
      
      
      TinyOut.add("end");

 	}









    }// end of for loop, take care, some more commands need be put in




/*
//storei
 


              if (SubString[0].equalsIgnoreCase("STOREI")) {
        if ((SubString[1].contains("$")) && (SubString[2].contains("$"))) {
          TinyOut.add("move " + createTemp(SubString[1]) + " " + createTemp(SubString[2]));
        }
        else if (SubString[1].contains("$")) {
          TinyOut.add("move " + createTemp(SubString[1]) + " " + SubString[2]);
        }
        else if (SubString[2].contains("$")) {
          TinyOut.add("move " + SubString[1] + " " + createTemp(SubString[2]));
        }
        else {
          TinyOut.add("move " + SubString[1] + " " + createTemp(SubString[2]));  //actually never run into this
          TinyOut.add("move " + createTemp(SubString[2]) + " " + SubString[2]);
        }
      }

      else if (SubString[0].equalsIgnoreCase("STOREF")) {
        if ((SubString[1].contains("$")) && (SubString[2].contains("$"))) {
          TinyOut.add("move " + createTemp(SubString[1]) + " " + createTemp(SubString[2]));
        }
        else if (SubString[1].contains("$")) {
          TinyOut.add("move " + createTemp(SubString[1]) + " " + SubString[2]);
        }
        else if (SubString[2].contains("$")) {
          TinyOut.add("move " + SubString[1] + " " + createTemp(SubString[2]));
        }
        else {
          TinyOut.add("move " + SubString[1] + " " + createTemp(SubString[2]));
          TinyOut.add("move " + createTemp(SubString[2]) + " " + SubString[2]);
        }
      }

*/



/*
//+,-*,/
      else if (SubString[0].equalsIgnoreCase("MULTI")) {
        if ((SubString[1].contains("$")) && (SubString[2].contains("$"))) {
          TinyOut.add("move " + createTemp(SubString[1]) + " " + createTemp(SubString[3]));
          TinyOut.add("muli " + createTemp(SubString[2]) + " " + createTemp(SubString[3]));
        }
        else if (SubString[1].contains("$")) {
          TinyOut.add("move " + createTemp(SubString[1]) + " " + createTemp(SubString[3]));
          TinyOut.add("muli " + SubString[2] + " " + createTemp(SubString[3])); // did not see anything like this also
        }
        else if (SubString[2].contains("$")) {
          TinyOut.add("move " + SubString[1] + " " + createTemp(SubString[3]));
          TinyOut.add("muli " + createTemp(SubString[2]) + " " + createTemp(SubString[3]));
        }
        else {
          TinyOut.add("move " + SubString[1] + " " + createTemp(SubString[3]));  //definitely returns register not enough, what the heck?
          TinyOut.add("muli " + SubString[2] + " " + createTemp(SubString[3]));
        }
      }
      else if (SubString[0].equalsIgnoreCase("ADDI")) {
        if ((SubString[1].contains("$")) && (SubString[2].contains("$"))) {
          TinyOut.add("move " + createTemp(SubString[1]) + " " + createTemp(SubString[3]));
          TinyOut.add("addi " + createTemp(SubString[2]) + " " + createTemp(SubString[3]));
        }
        else if (SubString[1].contains("$")) {
          TinyOut.add("move " + createTemp(SubString[1]) + " " + createTemp(SubString[3]));
          TinyOut.add("addi " + SubString[2] + " " + createTemp(SubString[3]));
        }
        else if (SubString[2].contains("$")) {
          TinyOut.add("move " + SubString[1] + " " + createTemp(SubString[3]));
          TinyOut.add("addi " + createTemp(SubString[2]) + " " + createTemp(SubString[3]));
        }
        else {
          TinyOut.add("move " + SubString[1] + " " + createTemp(SubString[3]));
          TinyOut.add("addi " + SubString[2] + " " + createTemp(SubString[3]));
        }
      }
      else if (SubString[0].equalsIgnoreCase("DIVI")) {
        if ((SubString[1].contains("$")) && (SubString[2].contains("$"))) {
          TinyOut.add("move " + createTemp(SubString[1]) + " " + createTemp(SubString[3]));
          TinyOut.add("divi " + createTemp(SubString[2]) + " " + createTemp(SubString[3]));
        }
        else if (SubString[1].contains("$")) {
          TinyOut.add("move " + createTemp(SubString[1]) + " " + createTemp(SubString[3]));
          TinyOut.add("divi " + SubString[2] + " " + createTemp(SubString[3]));
        }
        else if (SubString[2].contains("$")) {
          TinyOut.add("move " + SubString[1] + " " + createTemp(SubString[3]));
          TinyOut.add("divi " + createTemp(SubString[2]) + " " + createTemp(SubString[3]));
        }
        else {
          TinyOut.add("move " + SubString[1] + " " + createTemp(SubString[3]));
          TinyOut.add("divi " + SubString[2] + " " + createTemp(SubString[3]));
        }
      }
      else if (SubString[0].equalsIgnoreCase("SUBI")) {
        if ((SubString[1].contains("$")) && (SubString[2].contains("$"))) {
          TinyOut.add("move " + createTemp(SubString[1]) + " " + createTemp(SubString[3]));
          TinyOut.add("subi " + createTemp(SubString[2]) + " " + createTemp(SubString[3]));
        }
        else if (SubString[1].contains("$")) {
          TinyOut.add("move " + createTemp(SubString[1]) + " " + createTemp(SubString[3]));
          TinyOut.add("subi " + SubString[2] + " " + createTemp(SubString[3]));
        }
        else if (SubString[2].contains("$")) {
          TinyOut.add("move " + SubString[1] + " " + createTemp(SubString[3]));
          TinyOut.add("subi " + createTemp(SubString[2]) + " " + createTemp(SubString[3]));
        }
        else {
          TinyOut.add("move " + SubString[1] + " " + createTemp(SubString[3]));
          TinyOut.add("subi " + SubString[2] + " " + createTemp(SubString[3]));
        }
      }


     






      else if (SubString[0].equalsIgnoreCase("MULTF")) {
        if ((SubString[1].contains("$")) && (SubString[2].contains("$"))) {
          TinyOut.add("move " + createTemp(SubString[1]) + " " + createTemp(SubString[3]));
          TinyOut.add("mulr " + createTemp(SubString[2]) + " " + createTemp(SubString[3]));
        }
        else if (SubString[1].contains("$")) {
          TinyOut.add("move " + createTemp(SubString[1]) + " " + createTemp(SubString[3]));
          TinyOut.add("mulr " + SubString[2] + " " + createTemp(SubString[3]));
        }
        else if (SubString[2].contains("$")) {
          TinyOut.add("move " + SubString[1] + " " + createTemp(SubString[3]));
          TinyOut.add("mulr " + createTemp(SubString[2]) + " " + createTemp(SubString[3]));
        }
        else {
          TinyOut.add("move " + SubString[1] + " " + createTemp(SubString[3]));
          TinyOut.add("mulr " + SubString[2] + " " + createTemp(SubString[3]));
        }
      }
      else if (SubString[0].equalsIgnoreCase("ADDF")) {
        if ((SubString[1].contains("$")) && (SubString[2].contains("$"))) {
          TinyOut.add("move " + createTemp(SubString[1]) + " " + createTemp(SubString[3]));
          TinyOut.add("addr " + createTemp(SubString[2]) + " " + createTemp(SubString[3]));
        }
        else if (SubString[1].contains("$")) {
          TinyOut.add("move " + createTemp(SubString[1]) + " " + createTemp(SubString[3]));
          TinyOut.add("addr " + SubString[2] + " " + createTemp(SubString[3]));
        }
        else if (SubString[2].contains("$")) {
          TinyOut.add("move " + SubString[1] + " " + createTemp(SubString[3]));
          TinyOut.add("addr " + createTemp(SubString[2]) + " " + createTemp(SubString[3]));
        }
        else {
          TinyOut.add("move " + SubString[1] + " " + createTemp(SubString[3]));
          TinyOut.add("addr " + SubString[2] + " " + createTemp(SubString[3]));
        }
      }
      else if (SubString[0].equalsIgnoreCase("DIVF")) {
        if ((SubString[1].contains("$")) && (SubString[2].contains("$"))) {
          TinyOut.add("move " + createTemp(SubString[1]) + " " + createTemp(SubString[3]));
          TinyOut.add("divr " + createTemp(SubString[2]) + " " + createTemp(SubString[3]));
        }
        else if (SubString[1].contains("$")) {
          TinyOut.add("move " + createTemp(SubString[1]) + " " + createTemp(SubString[3]));
          TinyOut.add("divr " + SubString[2] + " " + createTemp(SubString[3]));
        }
        else if (SubString[2].contains("$")) {
          TinyOut.add("move " + SubString[1] + " " + createTemp(SubString[3]));
          TinyOut.add("divr " + createTemp(SubString[2]) + " " + createTemp(SubString[3]));
        }
        else {
          TinyOut.add("move " + SubString[1] + " " + createTemp(SubString[3]));
          TinyOut.add("divr " + SubString[2] + " " + createTemp(SubString[3]));
        }
      }
      else if (SubString[0].equalsIgnoreCase("SUBF")) {
        if ((SubString[1].contains("$")) && (SubString[2].contains("$"))) {
          TinyOut.add("move " + createTemp(SubString[1]) + " " + createTemp(SubString[3]));
          TinyOut.add("subr " + createTemp(SubString[2]) + " " + createTemp(SubString[3]));
        }
        else if (SubString[1].contains("$")) {
          TinyOut.add("move " + createTemp(SubString[1]) + " " + createTemp(SubString[3]));
          TinyOut.add("subr " + SubString[2] + " " + createTemp(SubString[3]));
        }
        else if (SubString[2].contains("$")) {
          TinyOut.add("move " + SubString[1] + " " + createTemp(SubString[3]));
          TinyOut.add("subr " + createTemp(SubString[2]) + " " + createTemp(SubString[3]));
        }
        else {
          TinyOut.add("move " + SubString[1] + " " + createTemp(SubString[3]));
          TinyOut.add("subr " + SubString[2] + " " + createTemp(SubString[3]));
        }
      }



*/


/*

//  ---------------------------write----------------------------------------
        else if (SubString[0].equalsIgnoreCase("WRITEF")) {
        if (SubString[1].contains("$")) {
          TinyOut.add("sys writer " + createTemp(SubString[1]) + " ");
        }
        else {
          TinyOut.add("sys writer " + SubString[1] + " ");
        }

      }

      else if (SubString[0].equalsIgnoreCase("WRITEI")) {
        if (SubString[1].contains("$")) {
          TinyOut.add("sys writei " + createTemp(SubString[1]));
        }
        else {
          TinyOut.add("sys writei " + SubString[1]);
        }

      }
      else if (SubString[0].equalsIgnoreCase("WRITES")) {
        TinyOut.add("sys writes " + SubString[1]);
      }

//--------------------------------------










//------------------------------read-------------------------------
      else if (SubString[0].equalsIgnoreCase("READI")) {
        if (SubString[1].contains("$")) {
          TinyOut.add("sys readi " + createTemp(SubString[1]));
        }
        else {
          TinyOut.add("sys readi " + SubString[1]);
        }

      }

      else if (SubString[0].equalsIgnoreCase("READF")) {
        if (SubString[1].contains("$")) {
          TinyOut.add("sys readr " + createTemp(SubString[1]) + " ");
        }
        else {
          TinyOut.add("sys readr " + SubString[1] + " ");
        }
      }

*/














/*

//---------------------compare--------------------------------------
      else if (SubString[0].equalsIgnoreCase("LEI")) {
        if ((SubString[1].contains("$")) && (SubString[2].contains("$"))) {
          if ((SubString[1].contains("$P")) || (SubString[1].contains("$L")) || (SubString[2].contains("$P")) || (SubString[2].contains("$L"))) {
            TinyOut.add("move " + createTemp(SubString[2]) + " " + createTemp((tempMap.get(labelIndicator)).get((tempMap.get(labelIndicator)).size() - 1)));
            TinyOut.add("cmpi " + createTemp(SubString[1]) + " " + createTemp((tempMap.get(labelIndicator)).get((tempMap.get(labelIndicator)).size() - 1)));
          }
          else {
            TinyOut.add("cmpi " + createTemp(SubString[1]) + " " + createTemp(SubString[2]));
          }
          TinyOut.add("jle " + SubString[3]);
        }
        else if (SubString[1].contains("$")) {
          TinyOut.add("move " + SubString[2] + " " + createTemp(SubString[2]));
          TinyOut.add("cmpi " + createTemp(SubString[1]) + " " + createTemp(SubString[2]));
          TinyOut.add("jle " + SubString[3]);
        }
        else if (SubString[2].contains("$")) {
          TinyOut.add("cmpi " + SubString[1] + " " + createTemp(SubString[2]));
          TinyOut.add("jle " + SubString[3]);
        }
        else {
          TinyOut.add("move " + SubString[2] + " " + createTemp(SubString[2]));
          TinyOut.add("cmpi " + SubString[1] + " " + createTemp(SubString[2]));
          TinyOut.add("jle " + SubString[3]);
        }

      }



      else if (SubString[0].equalsIgnoreCase("GEI")) {
        if ((SubString[1].contains("$")) && (SubString[2].contains("$"))) {
          if ((SubString[1].contains("$P")) || (SubString[1].contains("$L")) || (SubString[2].contains("$P")) || (SubString[2].contains("$L"))) {
            TinyOut.add("move " + createTemp(SubString[2]) + " " + createTemp((tempMap.get(labelIndicator)).get((tempMap.get(labelIndicator)).size() - 1)));
            TinyOut.add("cmpi " + createTemp(SubString[1]) + " " + createTemp((tempMap.get(labelIndicator)).get((tempMap.get(labelIndicator)).size() - 1)));
          }
          else {
            TinyOut.add("cmpi " + createTemp(SubString[1]) + " " + createTemp(SubString[2]));
          }
          TinyOut.add("jge " + SubString[3]);
        }
        else if (SubString[1].contains("$")) {
          TinyOut.add("move " + SubString[2] + " " + createTemp(SubString[2]));
          TinyOut.add("cmpi " + createTemp(SubString[1]) + " " + createTemp(SubString[2]));
          TinyOut.add("jge " + SubString[3]);
        }
        else if (SubString[2].contains("$")) {
          TinyOut.add("cmpi " + SubString[1] + " " + createTemp(SubString[2]));
          TinyOut.add("jge " + SubString[3]);
        }
        else {
          TinyOut.add("move " + SubString[2] + " " + createTemp(SubString[2]));
          TinyOut.add("cmpi " + SubString[1] + " " + createTemp(SubString[2]));
          TinyOut.add("jge " + SubString[3]);
        }

      }
      else if (SubString[0].equalsIgnoreCase("NEI")) {
        if ((SubString[1].contains("$")) && (SubString[2].contains("$"))) {
          if ((SubString[1].contains("$P")) || (SubString[1].contains("$L")) || (SubString[2].contains("$P")) || (SubString[2].contains("$L"))) {
            TinyOut.add("move " + createTemp(SubString[2]) + " " + createTemp((tempMap.get(labelIndicator)).get((tempMap.get(labelIndicator)).size() - 1)));
            TinyOut.add("cmpi " + createTemp(SubString[1]) + " " + createTemp((tempMap.get(labelIndicator)).get((tempMap.get(labelIndicator)).size() - 1)));
          }
          else {
            TinyOut.add("cmpi " + createTemp(SubString[1]) + " " + createTemp(SubString[2]));
          }
          TinyOut.add("jne " + SubString[3]);
        }
        else if (SubString[1].contains("$")) {
          TinyOut.add("move " + SubString[2] + " " + createTemp(SubString[2]));
          TinyOut.add("cmpi " + createTemp(SubString[1]) + " " + createTemp(SubString[2]));
          TinyOut.add("jne " + SubString[3]);
        }
        else if (SubString[2].contains("$")) {
          TinyOut.add("cmpi " + SubString[1] + " " + createTemp(SubString[2]));
          TinyOut.add("jne " + SubString[3]);
        }
        else {
          TinyOut.add("move " + SubString[2] + " " + createTemp(SubString[2]));
          TinyOut.add("cmpi " + SubString[1] + " " + createTemp(SubString[2]));
          TinyOut.add("jne " + SubString[3]);
        }

      }
      else if (SubString[0].equalsIgnoreCase("EQI")) {
        if ((SubString[1].contains("$")) && (SubString[2].contains("$"))) {
          if ((SubString[1].contains("$P")) || (SubString[1].contains("$L")) || (SubString[2].contains("$P")) || (SubString[2].contains("$L"))) {
            TinyOut.add("move " + createTemp(SubString[2]) + " " + createTemp((tempMap.get(labelIndicator)).get((tempMap.get(labelIndicator)).size() - 1)));
            TinyOut.add("cmpi " + createTemp(SubString[1]) + " " + createTemp((tempMap.get(labelIndicator)).get((tempMap.get(labelIndicator)).size() - 1)));
          }
          else {
            TinyOut.add("cmpi " + createTemp(SubString[1]) + " " + createTemp(SubString[2]));
          }
          TinyOut.add("jeq " + SubString[3]);
        }
        else if (SubString[1].contains("$")) {
          TinyOut.add("move " + SubString[2] + " " + createTemp(SubString[2]));
          TinyOut.add("cmpi " + createTemp(SubString[1]) + " " + createTemp(SubString[2]));
          TinyOut.add("jeq " + SubString[3]);
        }
        else if (SubString[2].contains("$")) {
          TinyOut.add("cmpi " + SubString[1] + " " + createTemp(SubString[2]));
          TinyOut.add("jeq " + SubString[3]);
        }
        else {
          TinyOut.add("move " + SubString[2] + " " + createTemp(SubString[2]));
          TinyOut.add("cmpi " + SubString[1] + " " + createTemp(SubString[2]));
          TinyOut.add("jeq " + SubString[3]);
        }

      }
      else if (SubString[0].equalsIgnoreCase("GTI")) {
        if ((SubString[1].contains("$")) && (SubString[2].contains("$"))) {
          if ((SubString[1].contains("$P")) || (SubString[1].contains("$L")) || (SubString[2].contains("$P")) || (SubString[2].contains("$L"))) {
            TinyOut.add("move " + createTemp(SubString[2]) + " " + createTemp((tempMap.get(labelIndicator)).get((tempMap.get(labelIndicator)).size() - 1)));
            TinyOut.add("cmpi " + createTemp(SubString[1]) + " " + createTemp((tempMap.get(labelIndicator)).get((tempMap.get(labelIndicator)).size() - 1)));
          }
          else {
            TinyOut.add("cmpi " + createTemp(SubString[1]) + " " + createTemp(SubString[2]));
          }
          TinyOut.add("jgt " + SubString[3]);
        }
        else if (SubString[1].contains("$")) {
          TinyOut.add("move " + SubString[2] + " " + createTemp(SubString[2]));
          TinyOut.add("cmpi " + createTemp(SubString[1]) + " " + createTemp(SubString[2]));
          TinyOut.add("jgt " + SubString[3]);
        }
        else if (SubString[2].contains("$")) {
          TinyOut.add("cmpi " + SubString[1] + " " + createTemp(SubString[2]));
          TinyOut.add("jgt " + SubString[3]);
        }
        else {
          TinyOut.add("move " + SubString[2] + " " + createTemp(SubString[2]));
          TinyOut.add("cmpi " + SubString[1] + " " + createTemp(SubString[2]));
          TinyOut.add("jgt " + SubString[3]);
        }

      }
      else if (SubString[0].equalsIgnoreCase("LTI")) {
        if ((SubString[1].contains("$")) && (SubString[2].contains("$"))) {
          if ((SubString[1].contains("$P")) || (SubString[1].contains("$L")) || (SubString[2].contains("$P")) || (SubString[2].contains("$L"))) {
            TinyOut.add("move " + createTemp(SubString[2]) + " " + createTemp((tempMap.get(labelIndicator)).get((tempMap.get(labelIndicator)).size() - 1)));
            TinyOut.add("cmpi " + createTemp(SubString[1]) + " " + createTemp((tempMap.get(labelIndicator)).get((tempMap.get(labelIndicator)).size() - 1)));
          }
          else {
            TinyOut.add("cmpi " + createTemp(SubString[1]) + " " + createTemp(SubString[2]));
          }
          TinyOut.add("jlt " + SubString[3]);
        }
        else if (SubString[1].contains("$")) {
          TinyOut.add("move " + SubString[2] + " " + createTemp(SubString[2]));
          TinyOut.add("cmpi " + createTemp(SubString[1]) + " " + createTemp(SubString[2]));
          TinyOut.add("jlt " + SubString[3]);
        }
        else if (SubString[2].contains("$")) {
          TinyOut.add("cmpi " + SubString[1] + " " + createTemp(SubString[2]));
          TinyOut.add("jlt " + SubString[3]);
        }
        else {
          TinyOut.add("move " + SubString[2] + " " + createTemp(SubString[2]));
          TinyOut.add("cmpi " + SubString[1] + " " + createTemp(SubString[2]));
          TinyOut.add("jlt " + SubString[3]);
        }

      }






      else if (SubString[0].equalsIgnoreCase("LEF")) {
        if ((SubString[1].contains("$")) && (SubString[2].contains("$"))) {
          if ((SubString[1].contains("$P")) || (SubString[1].contains("$L")) || (SubString[2].contains("$P")) || (SubString[2].contains("$L"))) {
            TinyOut.add("move " + createTemp(SubString[2]) + " " + createTemp((tempMap.get(labelIndicator)).get((tempMap.get(labelIndicator)).size() - 1)));
            TinyOut.add("cmpr " + createTemp(SubString[1]) + " " + createTemp((tempMap.get(labelIndicator)).get((tempMap.get(labelIndicator)).size() - 1)));
          }
          else {
            TinyOut.add("cmpr " + createTemp(SubString[1]) + " " + createTemp(SubString[2]));
          }
          TinyOut.add("jle " + SubString[3]);
        }
        else if (SubString[1].contains("$")) {
          TinyOut.add("move " + SubString[2] + " " + createTemp(SubString[2]));
          TinyOut.add("cmpr " + createTemp(SubString[1]) + " " + createTemp(SubString[2]));
          TinyOut.add("jle " + SubString[3]);
        }
        else if (SubString[2].contains("$")) {
          TinyOut.add("cmpr " + SubString[1] + " " + createTemp(SubString[2]));
          TinyOut.add("jle " + SubString[3]);
        }
        else {
          TinyOut.add("move " + SubString[2] + " " + createTemp(SubString[2]));
          TinyOut.add("cmpr " + SubString[1] + " " + createTemp(SubString[2]));
          TinyOut.add("jle " + SubString[3]);
        }

      }
      else if (SubString[0].equalsIgnoreCase("GEF")) {
        if ((SubString[1].contains("$")) && (SubString[2].contains("$"))) {
          if ((SubString[1].contains("$P")) || (SubString[1].contains("$L")) || (SubString[2].contains("$P")) || (SubString[2].contains("$L"))) {
            TinyOut.add("move " + createTemp(SubString[2]) + " " + createTemp((tempMap.get(labelIndicator)).get((tempMap.get(labelIndicator)).size() - 1)));
            TinyOut.add("cmpr " + createTemp(SubString[1]) + " " + createTemp((tempMap.get(labelIndicator)).get((tempMap.get(labelIndicator)).size() - 1)));
          }
          else {
            TinyOut.add("cmpr " + createTemp(SubString[1]) + " " + createTemp(SubString[2]));
          }
          TinyOut.add("jge " + SubString[3]);
        }
        else if (SubString[1].contains("$")) {
          TinyOut.add("move " + SubString[2] + " " + createTemp(SubString[2]));
          TinyOut.add("cmpr " + createTemp(SubString[1]) + " " + createTemp(SubString[2]));
          TinyOut.add("jge " + SubString[3]);
        }
        else if (SubString[2].contains("$")) {
          TinyOut.add("cmpr " + SubString[1] + " " + createTemp(SubString[2]));
          TinyOut.add("jge " + SubString[3]);
        }
        else {
          TinyOut.add("move " + SubString[2] + " " + createTemp(SubString[2]));
          TinyOut.add("cmpr " + SubString[1] + " " + createTemp(SubString[2]));
          TinyOut.add("jge " + SubString[3]);
        }

      }
      else if (SubString[0].equalsIgnoreCase("NEF")) {
        if ((SubString[1].contains("$")) && (SubString[2].contains("$"))) {
          if ((SubString[1].contains("$P")) || (SubString[1].contains("$L")) || (SubString[2].contains("$P")) || (SubString[2].contains("$L"))) {
            TinyOut.add("move " + createTemp(SubString[2]) + " " + createTemp((tempMap.get(labelIndicator)).get((tempMap.get(labelIndicator)).size() - 1)));
            TinyOut.add("cmpr " + createTemp(SubString[1]) + " " + createTemp((tempMap.get(labelIndicator)).get((tempMap.get(labelIndicator)).size() - 1)));
          }
          else {
            TinyOut.add("cmpr " + createTemp(SubString[1]) + " " + createTemp(SubString[2]));
          }
          TinyOut.add("jne " + SubString[3]);
        }
        else if (SubString[1].contains("$")) {
          TinyOut.add("move " + SubString[2] + " " + createTemp(SubString[2]));
          TinyOut.add("cmpr " + createTemp(SubString[1]) + " " + createTemp(SubString[2]));
          TinyOut.add("jne " + SubString[3]);
        }
        else if (SubString[2].contains("$")) {
          TinyOut.add("cmpr " + SubString[1] + " " + createTemp(SubString[2]));
          TinyOut.add("jne " + SubString[3]);
        }
        else {
          TinyOut.add("move " + SubString[2] + " " + createTemp(SubString[2]));
          TinyOut.add("cmpr " + SubString[1] + " " + createTemp(SubString[2]));
          TinyOut.add("jne " + SubString[3]);
        }

      }
      else if (SubString[0].equalsIgnoreCase("EQF")) {
        if ((SubString[1].contains("$")) && (SubString[2].contains("$"))) {
          if ((SubString[1].contains("$P")) || (SubString[1].contains("$L")) || (SubString[2].contains("$P")) || (SubString[2].contains("$L"))) {
            TinyOut.add("move " + createTemp(SubString[2]) + " " + createTemp((tempMap.get(labelIndicator)).get((tempMap.get(labelIndicator)).size() - 1)));
            TinyOut.add("cmpr " + createTemp(SubString[1]) + " " + createTemp((tempMap.get(labelIndicator)).get((tempMap.get(labelIndicator)).size() - 1)));
          }
          else {
            TinyOut.add("cmpr " + createTemp(SubString[1]) + " " + createTemp(SubString[2]));
          }
          TinyOut.add("jeq " + SubString[3]);
        }
        else if (SubString[1].contains("$")) {
          TinyOut.add("move " + SubString[2] + " " + createTemp(SubString[2]));
          TinyOut.add("cmpr " + createTemp(SubString[1]) + " " + createTemp(SubString[2]));
          TinyOut.add("jeq " + SubString[3]);
        }
        else if (SubString[2].contains("$")) {
          TinyOut.add("cmpr " + SubString[1] + " " + createTemp(SubString[2]));
          TinyOut.add("jeq " + SubString[3]);
        }
        else {
          TinyOut.add("move " + SubString[2] + " " + createTemp(SubString[2]));
          TinyOut.add("cmpr " + SubString[1] + " " + createTemp(SubString[2]));
          TinyOut.add("jeq " + SubString[3]);
        }

      }
      else if (SubString[0].equalsIgnoreCase("GTF")) {
        if ((SubString[1].contains("$")) && (SubString[2].contains("$"))) {
          if ((SubString[1].contains("$P")) || (SubString[1].contains("$L")) || (SubString[2].contains("$P")) || (SubString[2].contains("$L"))) {
            TinyOut.add("move " + createTemp(SubString[2]) + " " + createTemp((tempMap.get(labelIndicator)).get((tempMap.get(labelIndicator)).size() - 1)));
            TinyOut.add("cmpr " + createTemp(SubString[1]) + " " + createTemp((tempMap.get(labelIndicator)).get((tempMap.get(labelIndicator)).size() - 1)));
          }
          else {
            TinyOut.add("cmpr " + createTemp(SubString[1]) + " " + createTemp(SubString[2]));
          }
          TinyOut.add("jgt " + SubString[3]);
        }
        else if (SubString[1].contains("$")) {
          TinyOut.add("move " + SubString[2] + " " + createTemp(SubString[2]));
          TinyOut.add("cmpr " + createTemp(SubString[1]) + " " + createTemp(SubString[2]));
          TinyOut.add("jgt " + SubString[3]);
        }
        else if (SubString[2].contains("$")) {
          TinyOut.add("cmpr " + SubString[1] + " " + createTemp(SubString[2]));
          TinyOut.add("jgt " + SubString[3]);
        }
        else {
          TinyOut.add("move " + SubString[2] + " " + createTemp(SubString[2]));
          TinyOut.add("cmpr " + SubString[1] + " " + createTemp(SubString[2]));
          TinyOut.add("jgt " + SubString[3]);
        }

      }
      else if (SubString[0].equalsIgnoreCase("LTF")) {
        if ((SubString[1].contains("$")) && (SubString[2].contains("$"))) {
          if ((SubString[1].contains("$P")) || (SubString[1].contains("$L")) || (SubString[2].contains("$P")) || (SubString[2].contains("$L"))) {
            TinyOut.add("move " + createTemp(SubString[2]) + " " + createTemp((tempMap.get(labelIndicator)).get((tempMap.get(labelIndicator)).size() - 1)));
            TinyOut.add("cmpr " + createTemp(SubString[1]) + " " + createTemp((tempMap.get(labelIndicator)).get((tempMap.get(labelIndicator)).size() - 1)));
          }
          else {
            TinyOut.add("cmpr " + createTemp(SubString[1]) + " " + createTemp(SubString[2]));
          }
          TinyOut.add("jlt " + SubString[3]);
        }
        else if (SubString[1].contains("$")) {
          TinyOut.add("move " + SubString[2] + " " + createTemp(SubString[2]));
          TinyOut.add("cmpr " + createTemp(SubString[1]) + " " + createTemp(SubString[2]));
          TinyOut.add("jlt " + SubString[3]);
        }
        else if (SubString[2].contains("$")) {
          TinyOut.add("cmpr " + SubString[1] + " " + createTemp(SubString[2]));
          TinyOut.add("jlt " + SubString[3]);
        }
        else {
          TinyOut.add("move " + SubString[2] + " " + createTemp(SubString[2]));
          TinyOut.add("cmpr " + SubString[1] + " " + createTemp(SubString[2]));
          TinyOut.add("jlt " + SubString[3]);
        }

      }




*/


//------------------------------------------------


/*
      else if (SubString[0].equalsIgnoreCase("LABEL")) {
        TinyOut.add("label " + SubString[1] + " ");
        if (!SubString[1].contains("label")) {
          this.labelIndicator = SubString[1];
        }
      }
      else if (SubString[0].equalsIgnoreCase("JUMP")) {
        TinyOut.add("jmp " + SubString[1] + " ");
      }



 else if (SubString[0].equalsIgnoreCase("jsr")) {
   
        TinyOut.add("jsr " + SubString[1] );
  
      }
*/


/*
//---------------push & pop----------------------------------
      else if (SubString[0].equalsIgnoreCase("push")) {
        if (SubString.length == 1) {
          TinyOut.add("push");
        }
        else if (SubString[1].contains("$")) {
          TinyOut.add("push " + createTemp(SubString[1]) );
        }
        else {
          TinyOut.add("push " + SubString[1] );
        }

      }
      else if (SubString[0].equalsIgnoreCase("pop")) {
        if (SubString.length == 1) {
          TinyOut.add("pop");
        }
        else if (SubString[1].contains("$")) {
          TinyOut.add("pop " + createTemp(SubString[1]) );
        }
        else {
          TinyOut.add("pop " + SubString[1] );
        }

      }

*/


/*--
//-------------------------------------
      else if (SubString[0].equalsIgnoreCase("link")) {
        TinyOut.add("link " + this.linkCount.get(this.labelIndicator) );
      }

      else if (SubString[0].equalsIgnoreCase("RET")) {
        TinyOut.add("unlnk");
        TinyOut.add("ret");
      }
    }

*/


	





 
/*

   public String createTemp(String temp) {
   if (temp.contains("$T")){
    if (IrRegMap.get(temp) != null) {
      return (IrRegMap.get(temp));
    }

    this.RegCount += 1;
    String R = "r" + Integer.toString(RegCount);
    IrRegMap.put(temp, R);
    return R;
  }


//$p, $L case
  else{
     if (((Map)this.TR.get(this.labelIndicator)).containsKey(temp)) 
      return (String)((Map)this.TR.get(this.labelIndicator)).get(temp);
    
  
  }
  return ("Error");
  
  }

*/



     public String createTiny(String temp) {
       Function targetFunc = LexParser.funcHub.get(labelIndicator);
       if (temp.contains("$T")){
          
            String r = targetFunc.functioninfo.get("$T"+Integer.toString(targetFunc.locNum));
/*
            if (IrRegMap.get(temp) != null) 
             return (IrRegMap.get(temp));
            
           this.RegCount += 1;
           r = "r" + Integer.toString(RegCount);
           IrRegMap.put(temp, r);
*/         if (targetFunc.functioninfo.containsKey(temp))
           return targetFunc.functioninfo.get(temp);
           else
           return r;

       }

       else if ( (temp.contains("$P")) || (temp.contains("$L")) ){
          // if (((Map)this.TR.get(this.labelIndicator)).containsKey(temp)) 
           //return (String)((Map)this.TR.get(this.labelIndicator)).get(temp);
           if (targetFunc.functioninfo.containsKey(temp))
             return targetFunc.functioninfo.get(temp);
      
       }
     //intliteral
       else 
       return temp;  //may have error here

      return null;

     }









       private void getTinyOp(String[] SubString){

       TinyOut.add("move " + createTiny(SubString[1]) + " " + createTiny(SubString[3]));
       TinyOut.add(opCommands.get(SubString[0]) + " "+ createTiny(SubString[2]) + " " + createTiny(SubString[3]));
     //  return null ;
      }



       private void getTinyRw(String[] SubString){

       TinyOut.add(rwCommands.get(SubString[0])+" " + createTiny(SubString[1]));
      // return null ;

      }
       

  
      private void getTinyComp(String[] SubString){
      System.out.println("INT compare check: "+ SubString[0]);
            //String suffix = (SubString[0].charAt((SubString[0].length() - 1))); 
            String suffix = SubString[0].substring(SubString[0].length() - 1);
            suffix ="r";
 
            System.out.println("suffix is: " + suffix);

            String tempReg = createTiny(SubString[2]); //may be a memory address
            if(!(SubString[2].contains("$T")))  {
               tempReg = createTiny((tempMap.get(labelIndicator)).get((tempMap.get(labelIndicator)).size() - 1));
               TinyOut.add("move " + createTiny(SubString[2]) + " " + tempReg);
            }
            TinyOut.add("cmp"+suffix+" " + createTiny(SubString[1]) + " " + tempReg);
            TinyOut.add(compCommands.get(SubString[0])+ " " + SubString[3]);
      

     //  return null ;

      }
    
      

      private void getTinyStore(String[] SubString){
       //if  store  a b case, both oprands in global, 
         String cheatSubString = SubString[2];
         System.out.println("STORE before substring2");
          if((LexParser.symtab).get("GLOBAL").symbolMap.containsKey(SubString[1]) && (LexParser.symtab).get("GLOBAL").symbolMap.containsKey(SubString[2])){
         System.out.println("STORE special case, both global, force change to new substring: " + cheatSubString);
               cheatSubString ="$T"+SubString[2];
               TinyOut.add("move " + createTiny(SubString[1]) + " " + createTiny(cheatSubString));   
           }

          TinyOut.add("move " + createTiny(cheatSubString) + " " + SubString[2]);
        
     //  return null ;

      }




     public  void getTinyPp(String[] SubString){

        String ppCom = SubString[0].toLowerCase();
        if (SubString.length == 1) 
          TinyOut.add(ppCom);
        else  
          TinyOut.add(ppCom+ " " + createTiny(SubString[1]) );   
        return;
      }



     private String typeTrans(String type){
        	if (type.contains("STRING"))
        		return "str ";
        	else 
        		return "var ";
        }







 
      public void printTiny() {
      String result = "";
      for (int i = 0; i < TinyOut.size()-1; i++) {
     
      result = result + TinyOut.get(i);
      result = result + "\n";
    }
      result = result+ TinyOut.get(TinyOut.size()-1);
     System.out.println(";tiny code\n"+result);
     //return result;    
  }


}//end of class
