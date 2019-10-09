package assemblerTools ;
public class MemoryManager {
    private int NumberOfRegisters , MaxMainMemory ;
    public MemoryManager() {
        NumberOfRegisters = 16 ;
        MaxMainMemory = 1024 ;
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
}
