package assemblerTools ;
public class FileTextManipulation {
    public String removeComments(String inputStr) {	// a function to seprate the code and comment for furthur processing
        int index = inputStr.indexOf("##") ;
        if(index == -1) return inputStr.trim() ;
        return inputStr.substring(0,index).trim() ;
    }
}
