package assemblerExceptions ;
public class MultipleLabelDefinationException extends Exception {
    private String LabelName ;
    public MultipleLabelDefinationException(String LabelName) {
        this.LabelName = LabelName ;
    }
    public String toString() {
        return ("Label "+LabelName+" is defined multiple times");
    }
}
