package assemblerExceptions ;
public class OpCodeFileFormatException extends Exception {
    public OpCodeFileFormatException(String message) {
        System.out.println(message+" is missing in OpCodes.data file");
    }
}
