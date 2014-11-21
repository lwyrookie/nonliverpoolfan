public class Symbol {
  // may not need scope field
	protected String name;
	protected String type;                    //type also viewed as a String
	protected String value;                   //specifically for String variable
	
	public Symbol(String name, String type, String value)
	{
		this.name = name;
		this.type =type;
		if(type == "STRING")
			this.value = value;
	}
	
    public String getType(){
       return type;
    }
   

    public String getValue(){
       return value;
    }


}
