package assemblerExceptions ;
public class OpCodeNotValidException extends Exception {
    private String message ;
    public OpCodeNotValidException(String message) {
        this.message = message ;
    }
    public String toString() {
        return message ;
    }
}
