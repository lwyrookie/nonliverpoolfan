import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
import java.util.Arrays;

public class RegisterAllocate {
    public ArrayList<IRCFGNode> CFGList;
	private ArrayList<String> IRList;
	
	public RegisterAllocate(ArrayList<String> outputList){
	     this.IRList = outputList;
         CFGList = new ArrayList<IRCFGNode>();
         PredandSucc();
         GenandKill();
         calLiveness();
         printCFG();
	}
	
	public void PredandSucc() {
	     Map<String, Integer> lblMap = new HashMap<String, Integer>();
		 Map<String, Integer> jmpMap = new HashMap<String, Integer>();
		 String[] irElements;
		 int jmpIndex = 0;
		 IRCFGNode jmpNode = null;
		 /* use prev and curr to iterate through the list. similar to using
		    two pointers in traversing a linked list */
		 IRCFGNode prevNode = null;
		 //IRCFGNode currNode = null;
         /* remove semicolon at the beginning of the IRCode when adding to IRCFGNode */
		 for (int i=0; i<IRList.size(); i++){   
		      IRCFGNode currNode = new IRCFGNode(IRList.get(i).substring(1));
              if(!(prevNode == null)) {                  
				  //if((this.getJumpType(prevNode.IRCode) == 0) || (this.getJumpType(prevNode.IRCode) == 4)) continue;
				  /* for a normal instruction link directly */
		          //if(this.getJumpType(prevNode.IRCode) == 5)
                  if(!(this.getJumpType(prevNode.IRCode) == 1)|| !(this.getJumpType(prevNode.IRCode) == 4))  {
                      //System.out.println(i + "  " + (i-1) + " jmptype "+this.getJumpType(currNode.IRCode) );
					  prevNode.successors.add(i);
					  currNode.predecessors.add(i-1);
				  }
		          irElements = currNode.IRCode.split(" ");
				  /* UnConditional JUMP */
				  if(this.getJumpType(currNode.IRCode) == 1) {
					  String lbl = irElements[1];
					  /* Check if we have already seen the definition of the label */
					  if(lblMap.containsKey(lbl)) {
						  jmpIndex = lblMap.get(lbl);
						  jmpNode = CFGList.get(jmpIndex);					  
						  currNode.successors.add(jmpIndex);
					      jmpNode.predecessors.add(i);
					  }
					  else {
						  jmpMap.put(lbl,i);
					  }
				  }
				  /* Conditional JUMPS */
				  if(this.getJumpType(currNode.IRCode) == 2) {
					  String lbl = irElements[3];
					  /* Check if we have already seen the definition of the label */
					  if(lblMap.containsKey(lbl)) {
						  jmpIndex = lblMap.get(lbl);
						  jmpNode = CFGList.get(jmpIndex);					  
						  currNode.successors.add(jmpIndex);
					      jmpNode.predecessors.add(i);
					  }
					  else {
						  jmpMap.put(lbl,i);
					  }
				  }
				  /* LABEL */
				  if(this.getJumpType(currNode.IRCode) == 3) {
					  String lbl = irElements[1];
                      //System.out.println("Inside LABEL (lbl):"+lbl+" jmpMap contains "+jmpMap.containsKey(lbl) + " get "+jmpMap.get(lbl));
					  /* Check if we have already seen the definition of the label */
					  if(jmpMap.containsKey(lbl)) {
						  jmpIndex = jmpMap.get(lbl);
						  jmpNode = CFGList.get(jmpIndex);					  
						  currNode.predecessors.add(jmpIndex);
					      jmpNode.successors.add(i);
					  }
					  else {
						  lblMap.put(lbl,i);
                           
                          /*for (Map.Entry<String, Integer> entry : jmpMap.entrySet()) {
                          String key = entry.getKey().toString();;
                          Integer value = entry.getValue();
                          System.out.println("[lbl] key, " + key + " value " + value );
                          }*/
					  }
			      }
              }
			      prevNode = currNode;
                  //System.out.println("Adding "+currNode.IRCode + " to (size) "+CFGList.size());
			      CFGList.add(currNode);		    
		  }
		 
	}
	//JUMP - 1, LABEL - 2, Comparisons - 3
	public int getJumpType(String irCode) {
	     if( irCode != null && !irCode.isEmpty()) {
	        String[] irElements = irCode.split(" ");
            //System.out.println("inside fun "+irCode+ "irelements[0] "+irElements[0]);
	        if(irElements[0].equalsIgnoreCase("JUMP")) return 1;
		    if(irElements[0].contains("NE") || irElements[0].contains("GE") || irElements[0].contains("LE")) return 2;
		    if(irElements[0].equalsIgnoreCase("LABEL")) return 3;
			if(irElements[0].equalsIgnoreCase("RET")) return 4;
		    else return 5;
		}
		else return 0;
	}
	
	public void GenandKill() {
	        IRCFGNode cfgNode = null;
			String irCode = null;
            String[] codeSeg;
			for (int i=0; i<CFGList.size(); i++){
                cfgNode = CFGList.get(i);
				irCode = cfgNode.IRCode;
                codeSeg = irCode.split(" ");
				if(irCode.startsWith("ADDI") || irCode.startsWith("SUBI") || irCode.startsWith("MULTI") || irCode.startsWith("DIVI")
                   || irCode.startsWith("ADDF") || irCode.startsWith("SUBF") || irCode.startsWith("MULTF") || irCode.startsWith("DIVF")) {
				   cfgNode.gen.add(codeSeg[1]);
				   cfgNode.gen.add(codeSeg[2]);
                   cfgNode.kill = codeSeg[3];
			    }
                if(irCode.startsWith("LE") || irCode.startsWith("GE") || irCode.startsWith("NE") || irCode.startsWith("GT")
                   || irCode.startsWith("EQ") || irCode.startsWith("LT")) {
                   cfgNode.gen.add(codeSeg[1]);
				   cfgNode.gen.add(codeSeg[2]);
                }
                /* Check for WRITES */
                if(irCode.startsWith("READ")) {
                   cfgNode.gen.add(codeSeg[1]);
                }
                if(irCode.startsWith("WRITE")) {
                   cfgNode.kill = codeSeg[1];
                }
                /* Check if we have a case for LOAD */
                if(irCode.startsWith("STORE")) {
                    // ($,$), (2, $), ($, a), (a,b) 
                   if(RegisterAllocate.isNumeric(codeSeg[1]))
                   		cfgNode.kill = codeSeg[2];
                   else {
                        cfgNode.gen.add(codeSeg[1]);
                   		cfgNode.kill = codeSeg[2];
                   }
                }
                if(irCode.startsWith("PUSH") || irCode.startsWith("POP")) {
                   if(codeSeg[1] != null)  
                      cfgNode.gen.add(codeSeg[1]);
                }
                if(irCode.startsWith("RET")) {
                   //cfgNode.gen.add(codeSeg[1]);
                }
                /* Check how to handle jsr, jump and label cases */
                if(irCode.startsWith("JSR")) {
                   //cfgNode.gen.add(codeSeg[1]);
                }
                if(irCode.startsWith("JUMP")) {
                   //cfgNode.gen.add(codeSeg[1]);
                }
                if(irCode.startsWith("LABEL")) {
                   //cfgNode.gen.add(codeSeg[1]);
                } 
            }			 
	}
    /*http://books.google.com/books?id=A3yqQuLW5RsC&pg=PA220&lpg=PA220&dq=implementation+of+data+flow+equations+liveness&source=bl&ots=cXHeS3Utpi&sig=s75OuVD-CXuPZnP2kQeFQNMDKKI&hl=zh-CN&sa=X&ei=jNKDVITfDpKOyASrrIL4Dw&ved=0CEwQ6AEwBQ#v=onepage&q=implementation%20of%20data%20flow%20equations%20liveness&f=false*/
    public void calLiveness() {
		boolean anyDiff = true;
        int sameCount = 0;
        IRCFGNode cfgNode = null;
        IRCFGNode succcfgNode = null;
        ArrayList<String> genvar = new ArrayList<String>();
        String killvar = null;
        Set<String> previn = new HashSet<String>();
        Set<String> prevout = new HashSet<String>();
        Set<String> currin = new HashSet<String>();
        Set<String> currout;
        int ind=0;
        while(anyDiff) {
            int i, j;
            ind = ind + 1;
            System.out.println("inside liveness while loop for the "+(ind)+ " time");
            sameCount = 0;
            /* iterate backwards through CFG as it converges faster */
			for (i=CFGList.size()-1; i>=0; i--){ 
			//for (i=0; i<CFGList.size(); i++){ 
                ArrayList<Integer> succrs = new ArrayList<Integer>();    
            	cfgNode = CFGList.get(i);	
            	previn = cfgNode.livein;
            	prevout = cfgNode.liveout;
                genvar = cfgNode.gen;
                killvar = cfgNode.kill;
                /* update livein and liveout using data flow equations */
                /* update liveout */
                
                currout = new HashSet<String>();
                succrs = cfgNode.successors;
                for (j=0; j<succrs.size(); j++) { 
                    succcfgNode = CFGList.get(succrs.get(j));
                	currout.addAll(succcfgNode.livein);
                }

                /* updating livein */
                currin.addAll(currout);
                //currin.addAll(prevout);
                for(j=0; j<cfgNode.gen.size(); j++) {
                	currin.add(cfgNode.gen.get(j));   
                }
                if(((cfgNode.kill) != null) && !(cfgNode.kill).isEmpty()) {
                	currin.remove(cfgNode.kill); 
                }
                /*****************/

                cfgNode.livein = currin;
                cfgNode.liveout = currout;
                if(currin.equals(previn) && currout.equals(prevout)) {
                	sameCount = sameCount + 1;
                }
                if(!currin.equals(previn) || !currin.equals(prevout)) {
                   //String str1 = "index :"+i+" currLivein "+ Arrays.toString(currin.toArray()) + "  --previn--" + Arrays.toString(previn.toArray());
                   //String str2 = "index :"+i+" currLiveout "+ Arrays.toString(currout.toArray()) + "  --prevout--" + Arrays.toString(prevout.toArray());
                   //System.out.println(str1 +"   ::::::::::::::   " +str2); 
                }
        	}
            System.out.println("samecount "+sameCount);
            printCFG();
            if(sameCount == CFGList.size()) anyDiff = false; //Termination condition
        }

    }

	public static boolean isNumeric(String inputData) {
  		return inputData.matches("[-+]?\\d+(\\.\\d+)?");
   }
   
    /* Register allocation - 1) ensure
       2) check for free reg
       3) check for non dirty reg
       4) choose the first dirty (or farthest) reg and spill it. 
       5) remove variables that are not live (may need to spill)
    */

	public void printCFG() {
	    IRCFGNode cfgNode = null;
        try {
			 for (int i=0; i<CFGList.size(); i++){
		         cfgNode = CFGList.get(i);
		         String str1, str2, str3, str4, str5, str6, str7;
		         String str;
		         str1 = "IRCode "+cfgNode.IRCode + "  ";
		         str2 = "Preds "+ Arrays.toString((cfgNode.predecessors).toArray()) + "  ";
		         str3 = "Succs "+ Arrays.toString((cfgNode.successors).toArray()) + "  ";
		         str4 = "Gen " + Arrays.toString((cfgNode.gen).toArray()) + "  ";
				 str5 = "Kill " + cfgNode.kill + "  ";		
		         str6 = "Livein "+ Arrays.toString((cfgNode.livein).toArray()) + "  ";
				 str7 = "Liveout "+ Arrays.toString((cfgNode.liveout).toArray()) + "  ";
		         str = str1 + str2 + str3 + str4 + str5 + str6 + str7;
		         System.out.println(str);
   		    }
       }
       catch(Exception e) {
            System.out.println(e.getMessage());
       }
    }
}
