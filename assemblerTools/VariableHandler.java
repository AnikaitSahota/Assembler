package assemblerTools ;
import assemblerDataStructures.* ;
import java.util.* ;
public class VariableHandler {
    private ArrayList<String> Variables ;
    public VariableHandler() {
        Variables = new ArrayList<String>() ;
    }
    public boolean isValidVarName(String Name) {
        if(new MemoryManager().isValidAddress(Name))	return false ;
//		if(Name.charAt(0))
        return true ;
    }
    public void addVar(String Name) {
        //TODO: use set for storing variables
        Variables.add(Name) ;
    }
    public void allocateAddress(SymbolTable ST , int LC) {
        for(int i = 0 ; i < Variables.size() ; i++)
            ST.setAddress(Variables.get(i),LC+(i*12)) ;
    }
}
