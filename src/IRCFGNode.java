import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class IRCFGNode
{
  //private boolean isLeader;
  public String IRCode;
  public ArrayList<Integer> predecessors;
  public ArrayList<Integer> successors;
  public ArrayList<String> gen;
  public String kill;
  public Set<String> livein;
  public Set<String> liveout;
  public String func;
  //public int jumpNode;
  
  public IRCFGNode(String IRCode)
  {
    this.IRCode = IRCode;
    predecessors = new ArrayList<Integer>();
	successors = new ArrayList<Integer>();
    gen = new ArrayList<String>();
	kill = null;
    livein = new HashSet<String>();
    liveout = new HashSet<String>();
    func = null;
  }
 
  
  public String toString()
  {
    String s = "IRNode: hashCode details = , successors are:\n";
    return null;
  }
  
}
