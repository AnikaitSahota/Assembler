package assemblerTools ;
import java.util.* ;
public class FileTextManipulation {
    public String removeComments(String inputStr) {	// a function to seprate the code and comment for furthur processing
        int index = inputStr.indexOf("##") ;
        if(index == -1) return inputStr.trim() ;
        return inputStr.substring(0,index).trim() ;
    }
    public List<String> standardSplit(String message) {
        String str = "" ;
        for(int i = 0 ; i < message.length() ; i++) {
            if(message.charAt(i) == ' ' && (message.charAt(i-1) == ' ' || message.charAt(i-1) == ',' || message.charAt(i+1) == ','))    continue ;
            else if(message.charAt(i) == ':' && message.charAt(i-1) != ' ')    str += " "+message.charAt(i) ;
            else if(message.charAt(i) == ',' && str.charAt(str.length()-1) == ' ')    str = str.substring(0,str.length()-1)+"," ;
            else str += message.charAt(i) ;
        }
        return Arrays.asList(str.split(" ")) ;
    }
}
