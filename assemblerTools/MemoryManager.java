package assemblerTools ;
import java.util.* ;
public class MemoryManager {
    private int NumberOfRegisters , MaxMainMemory ;
    private ArrayList<Integer> MainStorage ;
    public MemoryManager() {
        NumberOfRegisters = 16 ;
        MaxMainMemory = 1024 ;
        MainStorage = new ArrayList<Integer>() ;
    }
    public boolean isValidAddress(String address) {
        // TODO: complete defination of this function
        try {
            if(address.charAt(0)=='R' && (Integer.parseInt(address.substring(1)) < 0 || Integer.parseInt(address.substring(1)) >= NumberOfRegisters)) return false ;
            if(Integer.parseInt(address) < 0 || Integer.parseInt(address) >= MaxMainMemory) return false ;
        }
        catch(NumberFormatException NumExp) {
            return false ;
        }
        return true ;
    }
    public boolean alocateAddress(String address) {
        // TODO: add alocate memory for registers
        int adr = Integer.parseInt(address) ;
        if(MainStorage.indexOf(adr) != -1)  return false ;
        MainStorage.add(adr) ;
        return true ;
    }
}
