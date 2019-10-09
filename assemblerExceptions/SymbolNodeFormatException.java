package assemblerExceptions ;
public class SymbolNodeFormatException extends Exception {
    private String message ;
    public SymbolNodeFormatException(String message) {
        this.message = message ;
    }
    public String toString() {
        return message ;
    }
}
