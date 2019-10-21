package assemblerExceptions ;
import java.util.ArrayList ;
public class SymbolNotDefinedException extends Exception {
    private ArrayList<String> UndefinedSymbols ;
    public SymbolNotDefinedException() {
        UndefinedSymbols = new ArrayList<String>() ;
    }
    public void addSymbolName(String name) {
        UndefinedSymbols.add(name) ;
    }
    public String toString() {
        String msg = "" ;
        for(int i = 0 ; i < UndefinedSymbols.size() ; i++)
            msg += "\n"+UndefinedSymbols.get(i)+" is not defined" ;
        return msg ;
    }
    public boolean throwValid() {
        return UndefinedSymbols.size() != 0 ;
    }
}
