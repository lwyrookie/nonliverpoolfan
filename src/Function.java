import java.util.ArrayList;
import java.util.HashMap;

public class Function {

	 public String funcName;
	 public int locNum;
	 public int parNum;
     public int tempNum;
	 
	 public HashMap<String, String> functioninfo = new HashMap<String,String>(); //keeps all the memory assignment information of this function
	 
	 
	 public Function(String name){
		 this.funcName =name;
	 }
	 
	 
	 public Function(String name,int locNum, int parNum){
		 this.funcName =name;
		 this.locNum =locNum;
		 this.parNum =parNum;
	 }
	 
	 
	 public void setLoc(int locNum){
		 this.locNum = locNum;
	 }
	 
	 
	 public void setPar(int parNum){
		 this.parNum = parNum;
	 }
	 
	 
	 public void setLocalParReturn(int loc, int par){
		 int i,memid;
         this.locNum =loc;
         this.parNum =par;
		 for (i=1; i<=par; i++){
			 this.functioninfo.put("$P"+Integer.toString(i), "$"+Integer.toString(i)); //setting parameter positions
		 }
		 
		 this.functioninfo.put("$R","$"+Integer.toString(i));  //setting return position
		 
		 for(i=1; i<loc; i++){
			 memid = -1*i;
			 this.functioninfo.put("$L"+Integer.toString(i), "$"+Integer.toString(memid));
		 }


/*for test*/
/*
        for (HashMap.Entry<String, String> entry : functioninfo.entrySet())
        {
          System.out.println(entry.getKey() + "/" + entry.getValue());
        }
			 
*/			 
	 }
	 
	 
	 
	 public void pushTempList(ArrayList<String> tempList){
         this.tempNum = tempList.size();
		 int i=-1*locNum;
          System.out.println("locNum in "+funcName+ "is :"+locNum);
		 //int j = tempList.size();
		 for (String element : tempList) {
			 i--;
			  this.functioninfo.put(element, "$"+Integer.toString(i)); //assuming arraylist is increasing order of $T#
			}
		// i--;
		// j++;
		// this.functioninfo.put("$T"+Integer.toString(j), "$"+Integer.toString(i)); //leaving an extra slot, for further use.
		 for (HashMap.Entry<String, String> entry : functioninfo.entrySet())
        {
          if (entry.getKey().contains("$T"))
          System.out.println(entry.getKey() + "/" + entry.getValue());
        }
		 
		 
	 }	
}
