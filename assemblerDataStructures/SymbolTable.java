package assemblerDataStructures ;
import assemblerExceptions.* ;
import java.util.* ;
public class SymbolTable {
    private ArrayList<SymbolTableNode> table ;
    public SymbolTable() {
        table = new ArrayList<SymbolTableNode>() ;
    }
    public void addSymbol(String Name , String type) throws SymbolNodeFormatException{
        if(!contains(Name)) table.add(new SymbolTableNode(Name , type , -1)) ;
    }
    public void addSymbol(String Name , String type , int address ) throws SymbolNodeFormatException{
        table.add(new SymbolTableNode(Name , type , address)) ;
    }
    public boolean contains(String Name) {
        for(int i = 0 ; i < table.size() ; i++)
            if(Name.equals(table.get(i).Name))  return true ;
        return false ;
    }
    public int indexOf(String Name) {
        for(int i = 0 ; i < table.size() ; i++)
            if(Name.equals(table.get(i).Name))  return i ;
        return -1 ;
    }
    public boolean isDefined(String Name) {
        int i = indexOf(Name) ;
        if(i == -1) return false ;
        return table.get(i).isDefined() ;
    }
    public void printTable() {
        for(int i = 0 ; i < table.size() ; i++)
            System.out.println(table.get(i));
    }
    public void setAddress(String Name , int address) {
        table.get(indexOf(Name)).setAddress(address) ;
    }
    public int getAddress(String Name) {
        return table.get(indexOf(Name)).getAddress() ;
    }
    public void containsUndefinedSymbol() throws SymbolNotDefinedException {
        SymbolNotDefinedException Exp = new SymbolNotDefinedException() ;
        for (int i = 0 ; i < table.size() ; i++ )
            if(!table.get(i).isDefined())  Exp.addSymbolName(table.get(i).Name+" ("+table.get(i).type+")") ;
        if(Exp.throwValid())  throw Exp ;
    }
}
