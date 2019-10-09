package assemblerExceptions ;
public class ScopeNotResolvedException extends Exception {
    private String message ;
    public ScopeNotResolvedException(String message) {
        this.message = message ;
    }
    public String toString() {
        return message ;
    }
}
