import java.util.* ;
import assemblerExceptions.* ;	import assemblerDataStructures.* ;	import assemblerTools.* ;
import java.io.* ;
class main {
	public static void main(String[] args) {
		assembler a = new assembler() ;
	}
}

class assembler {
	private File SourceCode ;		// This File will contain the assembly language code during whole assembly process
	private Hashtable<String,OpCodeTableDetails> OpCode_Table ;		// this is OpCode table
	private ArrayList<SymbolTableNode> SymbolTable ;
	public assembler() {	// initiator
		boolean errorFound = loadOpCodeTable() ;
		if(!errorFound)
			errorFound = loadSourceCode(new Scanner(System.in).nextLine()) ;
		if(!errorFound)
			errorFound = passOne() ;
		if(!errorFound)
			passTwo() ;
	}
	private boolean loadOpCodeTable()	{
		/* This is a fuction to read the file OpCodes.data because this file contains the all opcode details.
		and make a hashtable with key as assembly language opcode and value as all other important details.	*/
		OpCode_Table = new Hashtable<String,OpCodeTableDetails>() ; // initilaizing the hashtable (OpCode_Table)
		int lineNum = 0 ;	String line = "" ;
		try{
			File Opcodefile = new File("OpCodes.data") ;
			if(!Opcodefile.exists())	throw new FileNotFoundException() ;		// Exception if file not found
			Scanner file = new Scanner(Opcodefile) ;	// scanner object to read Opcodefile
			while(file.hasNextLine())
			{
				line = file.nextLine() ;	// reading the line from file
				lineNum++ ;		// incrementing counter for number of line
				addOpCode(new FileTextManipulation().removeComments(line)) ;	// sending line after removing comments
			}
			return false ; 	// error not found
		}
		catch(FileNotFoundException NoFileExc) {
			System.out.println("\nThe Opcode File is not present\nAssembler has been damaged");
		}
		catch(NumberFormatException String2IntExc) {
			System.out.println("\nCan't extract Binary OpCode(Machine language opcode)\nAssembler has been damaged");
			System.out.println("Error in OpCodes.data at line number : "+lineNum);System.out.println(line);
		}
		catch(OpCodeFileFormatException FileNotFormated) {
			System.out.println("\nThe opcode file is not formated as documented in the file\n"+FileNotFormated);
			System.out.println("Error in OpCodes.data at line number : "+lineNum);System.out.println(line);
		}
		// IDEA: try to do something such that, all the errors are shown in same time
		return true ;
	}
	private void addOpCode(String s) throws NumberFormatException , OpCodeFileFormatException {
		if(s.length() == 0)	return ;

		int index = s.indexOf(" : ") , tmp ;
		if(index == -1)	throw new OpCodeFileFormatException("Machine level Opcode") ;
		String str = s.substring(0,index) ;
		int MachineOpcode = Integer.parseInt(str) ; // extracted the machine code (binary code)
		for(int i = 0 ; i < str.length() ; i++)
			if(str.charAt(i) != '0' && str.charAt(i) != '1')	throw new NumberFormatException() ;

		tmp = index + 3 ;
		index = s.indexOf(" : ",index+1) ;
		if(index == -1)	throw new OpCodeFileFormatException("Description") ;
		String Discription = s.substring(tmp , index) ; // extracted the discription of the opcode

		tmp = index + 3 ;
		index = s.indexOf(" :",index+1) ;
		if(index == -1)	throw new OpCodeFileFormatException("Assembly Opcode") ;
		String AssemblyOpCode = s.substring(tmp , index) ; // extracted the Assembly opcode

		if(s.length() <= index+3 )	throw new OpCodeFileFormatException("parameter Info") ;
		String parameterInfo = s.substring(index+3) ;
		// TODO: add condtions for parameter info.

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
			if(fileName.length() == 0)	System.out.println("Enter a file name. with the command. :)");
			else System.out.println(fileName+" is not a valid file name\nNo file exists with this name");
			return true ;
		}
		return false ;
	}
	private void checkOpCode(List<String> instruction) throws OpCodeNotValidException {
		if(!OpCode_Table.containsKey(instruction.get(0)))	throw new OpCodeNotValidException(instruction.get(0)+" is not a valid OpCode.") ;
		else if(OpCode_Table.get(instruction.get(0)).parameterInfo.equals("none") && instruction.size()!=1)	throw new OpCodeNotValidException(instruction.get(0)+" needs no parameter.") ;
		else if(OpCode_Table.get(instruction.get(0)).parameterInfo.equals("address")) {
			if(instruction.size()!=2)	throw new OpCodeNotValidException(instruction.get(0)+"needs a address as parameter.") ;
			if(!new MemoryManager().isValidAddress(instruction.get(1)))	throw new OpCodeNotValidException(instruction.get(1)+" is not a valid address.") ;
		}
		else if(OpCode_Table.get(instruction.get(0)).parameterInfo.equals("label") && instruction.size()!=2) {
			if(instruction.size()!=2)	throw new OpCodeNotValidException(instruction.get(0)+"needs a address as parameter.") ;
		}
		// TODO: Check for label's parameter check
	}
	private boolean passOne() {
		System.out.println("Entering passOne");
		boolean errorFound = false ;
		String scope = "" ;
		// TODO: create symbol table , litral table , instruction table ( Pool table )
		try {
			Scanner file = new Scanner(SourceCode) ;
			SymbolTable = new ArrayList<SymbolTableNode>() ;
			FileTextManipulation help = new FileTextManipulation() ;
			int lineNum = 0 ; String Line = "" ;
			while(file.hasNextLine())	{
				Line = help.removeComments(file.nextLine()) ;	lineNum++ ;
				List<String> inst_List = help.standardSplit(Line) ;	// line to split the instruction in liist of string
				try {
						if(inst_List.size() > 0 && inst_List.get(0).equals("MEND") && scope.length() > 5 && scope.substring(scope.length()-6).equals("$Macro"))
						{ // block to handle MEND
							scope = scope.substring(0,scope.length()-6) ;	// moving it out of MACROs scope
						}
						//  FIXME: handle STP in pass one
						else if(inst_List.size() > 0 && OpCode_Table.containsKey(inst_List.get(0)))
						{
							checkOpCode(inst_List) ;
						}
						else if(inst_List.size() > 1 && inst_List.get(1).equals(":"))
						{// block for handling label defination
							checkOpCode(inst_List.subList(2,inst_List.size())) ;
							SymbolTable.add(new SymbolTableNode(inst_List.get(0),scope+"$Label",lineNum)) ;	// added the symbol(label) in symbol table.
						}
						else if(inst_List.size() > 1 && inst_List.get(1).equals("MACRO"))
						{
							scope += "$Macro" ;
							SymbolTable.add(new SymbolTableNode(inst_List.get(0),scope,lineNum)) ;
						}
				}
				catch (OpCodeNotValidException OpCodeExp) {
					System.out.println("\n"+OpCodeExp+"\nError at line number : "+lineNum);System.out.println(Line);
					errorFound = true ;
				}
				catch (SymbolNodeFormatException SymbolExp) {
					System.out.println("\n"+SymbolExp+"\nError at line number : "+lineNum);System.out.println(Line);
					errorFound = true ;
				}
			}
			if(scope.length() != 0)	throw new ScopeNotResolvedException("MACRO") ;
		}
		catch(FileNotFoundException FileExp) {
			System.out.println("\nFile doesn't exist");
			return true ;
		}
		catch (ScopeNotResolvedException ScopeExp) {
			System.out.println(ScopeExp+" is not ending using MEND programs scope are weird");
			return true ;
		}
		return errorFound ;
	}
	private void passTwo() {
		System.out.println("Entering passTwo");
		// TODO: complete passTwo
	}
}
