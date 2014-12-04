import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;


    public class CodeTrans {
	public ArrayList<String> TinyOut = new ArrayList<String>(); // Stores the TinyOutlist
	public LinkedHashMap<String, String> IrRegMap = new LinkedHashMap<String, String>(); //Stores the mapping between $T and r;
    private int RegCount = -1;			


    private ArrayList<String> IR;
    private SymbolTable table;
    private int paramIndex; //modify
    private int labelIndex = 0;
    private String labelIndicator = null;
    private int linknum = 0;   //from count
    private int RPosition = 0;
    protected Map<String, Map<String, String>> TR = new LinkedHashMap();
    protected Map<String, Map<String, Node>> tableMap = new LinkedHashMap();
    protected Map<String, Integer> linkCount = new LinkedHashMap();
    protected Map<String, ArrayList<String>> tempMap = new LinkedHashMap();	
   
  
   
     //Map TR as something <scope, ($P1, $17)> as well as registernumber LOCAL temp, tempMap only stores temporary variables;
    public  CodeTrans(ArrayList<String> outputList, SymbolTable table, Map<String, Map<String, Node>> tableMap, Map<String, ArrayList<String>> tempMap) {
    this.IR = outputList;
    this.table = table; 
    this.paramIndex = 1;  
    this.RPosition = this.paramIndex;
    this.tableMap = tableMap;
    this.tempMap = tempMap;

    for (String key : this.tableMap.keySet()) {
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
}


    }
  

    										

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
			
			else {
				;  //only global scope now
			}
		}



    
       TinyOut.add("push");
       TinyOut.add("jsr main");
       TinyOut.add("sys halt");

         




		
		for (int i=0; i<IR.size(); i++){   
            String[] SubString = IR.get(i).split("\\s+"); 
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
          TinyOut.add("move " + SubString[1] + " " + createTemp(SubString[2]));
          TinyOut.add("move " + createTemp(SubString[2]) + " " + SubString[2]);
        }
      }
      else if (SubString[0].equalsIgnoreCase("MULTI")) {
        if ((SubString[1].contains("$")) && (SubString[2].contains("$"))) {
          TinyOut.add("move " + createTemp(SubString[1]) + " " + createTemp(SubString[3]));
          TinyOut.add("muli " + createTemp(SubString[2]) + " " + createTemp(SubString[3]));
        }
        else if (SubString[1].contains("$")) {
          TinyOut.add("move " + createTemp(SubString[1]) + " " + createTemp(SubString[3]));
          TinyOut.add("muli " + SubString[2] + " " + createTemp(SubString[3]));
        }
        else if (SubString[2].contains("$")) {
          TinyOut.add("move " + SubString[1] + " " + createTemp(SubString[3]));
          TinyOut.add("muli " + createTemp(SubString[2]) + " " + createTemp(SubString[3]));
        }
        else {
          TinyOut.add("move " + SubString[1] + " " + createTemp(SubString[3]));
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
      else if (SubString[0].equalsIgnoreCase("READI")) {
        if (SubString[1].contains("$")) {
          TinyOut.add("sys readi " + createTemp(SubString[1]));
        }
        else {
          TinyOut.add("sys readi " + SubString[1]);
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
      else if (SubString[0].equalsIgnoreCase("WRITEF")) {
        if (SubString[1].contains("$")) {
          TinyOut.add("sys writer " + createTemp(SubString[1]) + " ");
        }
        else {
          TinyOut.add("sys writer " + SubString[1] + " ");
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
      else if (SubString[0].equalsIgnoreCase("LABEL")) {
        TinyOut.add("label " + SubString[1] + " ");
        if (!SubString[1].contains("label")) {
          this.labelIndicator = SubString[1];
        }
      }
      else if (SubString[0].equalsIgnoreCase("JUMP")) {
        TinyOut.add("jmp " + SubString[1] + " ");
      }
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




 else if (SubString[0].equalsIgnoreCase("jsr")) {
   
        TinyOut.add("jsr " + SubString[1] );
  
      }
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
      else if (SubString[0].equalsIgnoreCase("link")) {
        TinyOut.add("link " + this.linkCount.get(this.labelIndicator) );
      }
      else if (SubString[0].equalsIgnoreCase("RET")) {
        TinyOut.add("unlnk");
        TinyOut.add("ret");
      }
    }

    TinyOut.add("end");

	}	





 
// return r# /$#

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
