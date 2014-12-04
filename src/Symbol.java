public class Symbol {
  // may not need scope field
	protected String name;
	protected String type;                    //type also viewed as a String
	protected String value;                   //specifically for String variable
	protected String nodePrefix;
	
	public Symbol(String name, String type, String value)
	{
		this.name = name;
		this.type =type;
		if(type == "STRING")
			this.value = value;
		this.nodePrefix = null;
	}
	
	public Symbol(String name, String type, String value, String nodePrefix)
	{
		this.name = name;
		this.type =type;
		if(type == "STRING")
			this.value = value;
		this.nodePrefix = nodePrefix;
		if(this.nodePrefix.contains("@")) {
            //System.out.println("node prefix before " +this.nodePrefix);
            this.nodePrefix = "$" + this.nodePrefix.trim().substring(1);
            //System.out.println("node prefix after " +this.nodePrefix);
		}
	}
    public String getType(){
       return type;
    }
       
	public String getPrefix(){
       return nodePrefix;
    }

    public String getValue(){
       return value;
    }


}
