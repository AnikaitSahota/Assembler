package assemblerExceptions ;
public class AssemblerDirectivesException extends Exception {
    private String message ;
    public AssemblerDirectivesException(String message) {
        this.message = message ;
    }
    public String toString() {
        return (message+"\tInvalid use of Assembler Directives");
    }
}
