import java.util.* ;
import java.io.File ;
import java.io.FileNotFoundException ;
class main {
	public static void main(String[] args) {
		assembler a = new assembler() ;
	}
}
class assembler {
	File SourceCode ;		// This File will contain the assembly language code during whole assembly process
	Hashtable<String,OpCodeTableDetails> OpCode_Table ;		// this is OpCode table
	class OpCodeTableDetails {
		final int MachineOpcode ;
		final String Description , parameterInfo ;
		public OpCodeTableDetails(int BinaryCode , String Details , String parameterInfo)
		{
			MachineOpcode = BinaryCode ;
			Description = Details ;
			this.parameterInfo = parameterInfo ;
		}
	}
	public assembler() {	// initiator
		Scanner scan = new Scanner(System.in) ;
		boolean errorFound = loadOpCodeTable() ;
		if(!errorFound)
			errorFound = loadSourceCode(scan.nextLine()) ;
		if(!errorFound)
			passOne() ;
	}
	private boolean loadOpCodeTable()	{
		/* This is a fuction to read the file OpCodes.data because this file contains the all opcode details.
		and make a hashtable with key as assembly language opcode and value as all other important details.	*/
		OpCode_Table = new Hashtable<String,OpCodeTableDetails>() ; // initilaizing the hashtable (OpCode_Table)
		String str , str1 , str2 , str3 , str4 ;
		try{
			File Opcodefile = new File("OpCodes.data") ;
			if(!Opcodefile.exists())	throw new FileNotFoundException() ;
			Scanner file = new Scanner(Opcodefile) ;
			file.nextLine() ; // ignoring the first line showing opcode storage formate
			while(file.hasNextLine())
				addOpCode(file.nextLine()) ;
			return false ; 	// error not found
		}
		catch(FileNotFoundException NoFileExc) {
			System.out.println("The Opcode File is not present\nAssembler has been damaged");
		}
		catch(NumberFormatException String2IntExc) {
			System.out.println("Can't extract Binary OpCode(Machine language opcode)\nAssembler has been damaged");
		}
		return true ;
	}
	private void addOpCode(String s) throws NumberFormatException {
		int index = s.indexOf(" : ") , tmp ;
		String str = s.substring(0,index) ;
		int MachineOpcode = Integer.parseInt(str) ; // extracted the machine code (binary code)
		// IDEA: to print the error line and opcode having details
		for(int i = 0 ; i < str.length() ; i++)
			if(str.charAt(i) != '0' && str.charAt(i) != '1')	throw new NumberFormatException() ;

		tmp = index + 3 ;
		index = s.indexOf(" : ",index+1) ;
		String Discription = s.substring(tmp , index) ; // extracted the discription of the opcode

		tmp = index + 3 ;
		index = s.indexOf(" : ",index+1) ;
		String AssemblyOpCode = s.substring(tmp , index) ; // extracted the Assembly opcode

		// FIXME: store the parameter info such that there is no need tp traverse or manipulate a string
		String parameterInfo = "none" ;

		OpCode_Table.put(AssemblyOpCode,new OpCodeTableDetails(MachineOpcode,Discription,parameterInfo)) ;
	}
	private boolean loadSourceCode(String fileName) {
		System.out.println("--"+fileName+"--");
		try
		{
			SourceCode = new File(fileName);
			if(!SourceCode.exists())	throw new FileNotFoundException() ;
		}
		catch(FileNotFoundException exp)
		{
			System.out.println(fileName+" is not a valid file name\nNo file exists with this name");
			return true ;
		}
		return false ;
	}
	private void passOne() {
		System.out.println("Entering passOne");
		// TODO: complete pass passOne
		passTwo() ;
	}
	private void passTwo() {
		System.out.println("Entering passTwo");
		// TODO: complete passTwo
	}
}
