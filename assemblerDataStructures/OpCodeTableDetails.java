package assemblerDataStructures ;
public class OpCodeTableDetails {
    /*	A class to store all the data associated with a assembly language opcode or symbol.
        MachineOpcode is a int data member to store the binary opcode value corresponding to assembly level opcode.
        Description and parameterInfo is just a data member to store documentation of opcode for future need.
        parameterInfo contains the details of the parameters corresponding to OpCode.	*/
    public final String Description , parameterInfo , MachineOpcode ;
    public OpCodeTableDetails(String BinaryCode , String Details , String parameterInfo)
    {	// parameterized constructor
        MachineOpcode = BinaryCode ;
        Description = Details ;
        this.parameterInfo = parameterInfo ;
    }
}
