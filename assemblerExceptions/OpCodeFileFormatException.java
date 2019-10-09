package assemblerExceptions ;
public class OpCodeFileFormatException extends Exception {
    private String message ;
    public OpCodeFileFormatException(String message) {
        this.message = message ;
    }
    public String toString() {
        return (message+" is missing in OpCodes.data file");
    }
}
