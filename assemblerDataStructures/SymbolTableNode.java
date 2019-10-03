package assemblerDataStructures ;
public class SymbolTableNode {
    private final String Name ;
    private int address ;
    SymbolTableNode(String Name) {
        this.Name = Name ;
        address = -1 ;
    }
    public boolean isDefined() {
        return (address> 0) ;
    }
    public void setAddress(int a) {
        address = a ;
    }
}
