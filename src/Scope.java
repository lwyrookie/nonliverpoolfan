import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Scope {
	public String Scopetype; // global, function, / block series number
	public Scope enclosingscope; // record the enclosing scope of current scope
	protected Map<String, Symbol> symbolMap = new LinkedHashMap<String, Symbol>(); // Symbol
																					// :flexibility
																					// for
																					// String
																					// variables

	// declaration combined with initialization

	// protected Map<String, String> symbolMap = new LinkedHashMap<S, Symbol>;

	public Scope(String Scopetype, Scope enclosingscope) // constructor
	{
		this.Scopetype = Scopetype;
		this.enclosingscope = enclosingscope;

	}

	public void addsymbol(String name, String type, String value)// Symbol
																	// should
																	// contain
	// private field of name
	{
		// tell if same variable exists in current Scope
		if (symbolMap.containsKey(name)) {
			throw new IllegalArgumentException("DECLARATION ERROR " + name);
		}

		// not in current scope, add the new symbol to the Scope
		Symbol symbol = new Symbol(name, type, value);
		symbolMap.put(name, symbol);

	}

	public void addsymbol(String name, String type, String value, String nodePrefix)// Symbol
																	// should
																	// contain
	// private field of name
	{
		// tell if same variable exists in current Scope
		if (symbolMap.containsKey(name)) {
			throw new IllegalArgumentException("DECLARATION ERROR " + name);
		}

		// not in current scope, add the new symbol to the Scope
		Symbol symbol = new Symbol(name, type, value, nodePrefix);
		symbolMap.put(name, symbol);

	}
	// need to modify

	public void addsymbols(String name, String type, String value)// Symbol
																	// should
																	// contain
	// private field of name
	{
		int i;
		String[] nameArray = name.split(",");
		for (i = 0; i < nameArray.length; i++) {
			if (symbolMap.containsKey(nameArray[i].trim())) {
				throw new IllegalArgumentException("DECLARATION ERROR " + nameArray[i].trim());
			} else {
				Symbol symbol = new Symbol(nameArray[i].trim(), type, value);
				symbolMap.put(nameArray[i].trim(), symbol);
			}
		}
	}

	public void addsymbols(String name, String type, String value, String nodePrefix)// Symbol
																	// should
																	// contain
	// private field of name
	{
		int i;
		String[] nameArray = name.split(",");
		for (i = 0; i < nameArray.length; i++) {
			if (symbolMap.containsKey(nameArray[i].trim())) {
				throw new IllegalArgumentException("DECLARATION ERROR " + nameArray[i].trim());
			} else {
				Symbol symbol = new Symbol(nameArray[i].trim(), type, value, nodePrefix);
				symbolMap.put(nameArray[i].trim(), symbol);
			}
		}
	}
	
	public Symbol resolve(String name) {
		Symbol symbol = symbolMap.get(name);
		if (symbol != null)
			return symbol;
		if (enclosingscope != null)
			return enclosingscope.resolve(name);
		return null;
	}

	public Scope enclosingScope() {
		return enclosingscope;
	}

    public void setenclosingscope(Scope down){
         enclosingscope = down;         
    
    }

	public String toString() {
           StringBuilder sb = new StringBuilder();    // build output string from this
           java.util.Iterator<Entry<String, Symbol>> it = symbolMap.entrySet().iterator();
            while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            //System.out.println(pairs.getKey() + " = " + pairs.getValue());
            //it.remove(); // avoids a ConcurrentModificationException
            if(((Symbol) pairs.getValue()).getType() == "STRING")
                //sb.append("name "+pairs.getKey()+" type STRING value "+((Symbol) pairs.getValue()).getValue()+"\n");
                sb.append("name "+pairs.getKey()+" type STRING value "+((Symbol) pairs.getValue()).getValue()+ "nodePrefix"+((Symbol) pairs.getValue()).getPrefix()+ "\n");
            else
                sb.append("name "+pairs.getKey()+" type "+((Symbol) pairs.getValue()).getType()+"nodePrefix"+((Symbol) pairs.getValue()).getPrefix()+"\n");
                
        }  
            String temp = sb.toString().replace("[", "").replace("]", "");
            String temp2 = temp.replaceAll(", ", "");
            return temp2;
            //return symbolMap.keySet().toString();
        }


}
