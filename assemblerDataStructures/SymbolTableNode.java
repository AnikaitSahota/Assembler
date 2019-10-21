package assemblerDataStructures ;
import assemblerExceptions.* ;
//import java.util.* ;
class SymbolTableNode {
    final String Name , type ;
    private int address ;
    public SymbolTableNode(String Name,String type , int address) throws SymbolNodeFormatException {
        this.Name = Name ;
        this.type = type ;
        checkSymbolType() ;
        this.address = address ; //TODO: Do something to handle error here
    }
    // private void checkSymbolScope() throws SymbolNodeFormatException {
    //     if(type.equals("Undefined") && address < 0) return ;
    //     if(type.charAt(0) != '$')   throw new SymbolNodeFormatException(type+" is not a valid Symbol type") ;
    //     List<String> scopes = Arrays.asList(type.split("$")) ;
    //     for(int i = 0 ; i < scopes.size() ; i++)
    //         if(!scopes.get(i).equals("$Label") && !scopes.get(i).equals("$Macro"))
    //             throw new SymbolNodeFormatException(type+" is not a valid Symbol type") ;
    // }
    private void checkSymbolType() throws SymbolNodeFormatException {
        if (!type.equalsIgnoreCase("Label") && !type.equalsIgnoreCase("Variable")) throw new SymbolNodeFormatException(type+" is not a valid type for a Symbol") ;
    }
    public boolean isDefined() {
        return (address> 0) ;
    }
    public void setAddress(int a) {
        address = a ;
    }
    public String toString() {
        return Name+" : "+type+" : "+address ;
    }
    public int getAddress() {   return address ; }
}
