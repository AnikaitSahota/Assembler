import java.util.* ;
import assemblerExceptions.* ;	import assemblerDataStructures.* ;	import assemblerTools.* ;
import java.io.* ;
class main {
	public static void main(String[] args) {
		assembler a = new assembler() ;	// starting the assembler
	}
}

class assembler {
	private File SourceCode ;		// This File will contain the assembly language code during whole assembly process
	private Hashtable<String,OpCodeTableDetails> OpCode_Table ;		// this is OpCode table
	private HashMap<Integer , List<String>> ItrStrg ; // Intermediate Storage to store LC , instruction
	private SymbolTable Symbol_Table ;		// symbol table to help pass one
	private MemoryManager MM ;	// Memory Manager to manage Memory blocks
	private VariableHandler VarHdl ;	// variable handler to stop repetetion of variable
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
				addOpCode(new FileTextManipulation().removeOpCodeFileComments(line)) ;	// sending line after removing comments
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
		return true ;
	}
	private void addOpCode(String s) throws NumberFormatException , OpCodeFileFormatException {
		if(s.length() == 0)	return ;	// if there is no instruction

		int index = s.indexOf(" : ") , tmp ;
		if(index == -1)	throw new OpCodeFileFormatException("Machine level Opcode") ;
		String str = s.substring(0,index) ;
		Integer.parseInt(str) ;
		String MachineOpcode = str ; // extracted the machine code (binary code)
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
	private boolean checkOpCode(List<String> instruction) throws OpCodeNotValidException {	// funtion to handle errors and SymbolAddition flag
		if(!OpCode_Table.containsKey(instruction.get(0)))	throw new OpCodeNotValidException(instruction.get(0)+" is not a valid OpCode.") ;
		else if(OpCode_Table.get(instruction.get(0)).parameterInfo.equals("none") && instruction.size()!=1)	throw new OpCodeNotValidException(instruction.get(0)+" needs no parameter.") ;
		else if(OpCode_Table.get(instruction.get(0)).parameterInfo.equals("address")) {
			if(instruction.size()!=2)	throw new OpCodeNotValidException(instruction.get(0)+"needs a address as parameter.") ;
			if(instruction.get(0).equals("INP") && VarHdl.isValidVarName(instruction.get(1)))	return true ;
			if(! MM.isValidAddress(instruction.get(1)))	throw new OpCodeNotValidException(instruction.get(1)+" is not a valid address.") ;
		}
		else if(OpCode_Table.get(instruction.get(0)).parameterInfo.equals("label")) {
			if(instruction.size()!=2)	throw new OpCodeNotValidException(instruction.get(0)+" needs a label as parameter.") ;
			if(!Symbol_Table.contains(instruction.get(1)))	return true ;
		}
		return false ;
		// TODO: Check for label's parameter check
	}
	private boolean passOne() {
		System.out.println("Entering passOne");
		boolean errorFound = false , STARTfound = false , ENDfound = false ;
		String scope = "" ;	int LC = -1 ;
		// TODO: create symbol table , litral table , instruction table ( Pool table )
		try {
			Scanner file = new Scanner(SourceCode) ;
			Symbol_Table = new SymbolTable() ;	ItrStrg = new HashMap<Integer,List<String> >() ;
			FileTextManipulation help = new FileTextManipulation() ;
			MM = new MemoryManager() ;	VarHdl = new VariableHandler() ;
			int lineNum = 0 ; String Line = "" ;
			while( file.hasNextLine() && !ENDfound )	{
				Line = help.removeSourceFileComments(file.nextLine()) ;	lineNum++ ;
				List<String> inst_List = help.standardSplit(Line) ;	// line to split the instruction in liist of string
				try {
					if(inst_List.size() > 0 && (inst_List.get(0).equals("END") || inst_List.get(0).equals("START")) && scope.length() == 0)
					{ // block to handle START , END
						if(inst_List.get(0).equals("END"))
						{
							if(!STARTfound)	throw new AssemblerDirectivesException("END used before START") ;
							else ENDfound = true ;
						}
						if(inst_List.get(0).equals("START"))
						{
							if(STARTfound)	throw new AssemblerDirectivesException("Multiple START statements") ;
							if(inst_List.size() > 2 )	throw new AssemblerDirectivesException("Too much parameters for START") ;
							if(inst_List.size() == 2 )
							{
								try {
									LC = Integer.parseInt(inst_List.get(1))-1 ;
									if(LC < 0)	throw new AssemblerDirectivesException("Location counter can't be negative") ;
								}
								catch (NumberFormatException exp) {throw new AssemblerDirectivesException("Invalid parameters for START") ;}
							}
							STARTfound = true ;
						}
					}
					else if(inst_List.size() > 0 && OpCode_Table.containsKey(inst_List.get(0)))
					{// block to handle opcode in global scope
						boolean SymbolAddition = checkOpCode(inst_List) ;
						if(STARTfound)	ItrStrg.put(++LC , inst_List) ;
						if(SymbolAddition)
						{
							if(inst_List.get(0).equals("INP"))	{	Symbol_Table.addSymbol(inst_List.get(1) , "variable" ) ;	VarHdl.addVar(inst_List.get(1)) ;}
							else if(OpCode_Table.get(inst_List.get(0)).parameterInfo.equals("label"))	Symbol_Table.addSymbol(inst_List.get(1) , "Label" ) ;
							else System.out.println("Some thing is worong while selecting symbol to add line number "+lineNum);
						}
					}
					else if(inst_List.size() > 1 && inst_List.get(1).equals(":"))
					{// block for handling label defination
						checkOpCode(inst_List.subList(2,inst_List.size())) ;
						if(STARTfound)	ItrStrg.put(++LC , inst_List.subList(2,inst_List.size())) ;
						if(Symbol_Table.isDefined(inst_List.get(0)))	throw new MultipleLabelDefinationException(inst_List.get(0)) ;
						else if(!Symbol_Table.contains(inst_List.get(0)))	Symbol_Table.addSymbol(inst_List.get(0), "Label" , LC ) ;	// added the symbol(label) in symbol table.
						else Symbol_Table.setAddress(inst_List.get(0), LC ) ;
					}
					else if(inst_List.size() > 1 && inst_List.get(1).equals("MACRO"))
					{// underDevlopment							// scope += "$Macro" ;		SymbolTable.add(new SymbolTableNode(inst_List.get(0),scope,lineNum)) ;
						throw new SymbolNodeFormatException("Macro are under development\nKindle code without it.") ;
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
				catch (AssemblerDirectivesException AssDirExp) {
					System.out.println("\n"+AssDirExp+"\nError at line number : "+lineNum);System.out.println(Line);
					errorFound = true ;
				}
				catch (MultipleLabelDefinationException MultiDefineLabelExp) {
					System.out.println("\n"+MultiDefineLabelExp);	errorFound = true ;
				}
			}
			if(!STARTfound)	throw new AssemblerDirectivesException("START not present in file") ;
			if(!ENDfound)	throw new AssemblerDirectivesException("END not present in file") ;
			if(scope.length() != 0)	throw new ScopeNotResolvedException("MACRO") ;
			VarHdl.allocateAddress(Symbol_Table,++LC) ;
//			Symbol_Table.printTable() ;
			Symbol_Table.containsUndefinedSymbol() ;
		}
		catch(FileNotFoundException FileExp) {
			System.out.println("\nFile doesn't exist");
			return true ;
		}
		catch (ScopeNotResolvedException ScopeExp) {//NOTE: THis exception is uselaess after removal of MACRO from project1
			System.out.println(ScopeExp+" is not ending using programs scope are weird");
			return true ;
		}
		catch (AssemblerDirectivesException AssDirExp) {
			System.out.println("\n"+AssDirExp);
			return true ;
		}
		catch (SymbolNotDefinedException e) {	System.out.println(e);	return true ;	}
		return errorFound ;
	}
	private void passTwo() {
		System.out.println("Entering passTwo");
		BufferedWriter file = null ;
		try {
			file = new BufferedWriter(new FileWriter("output.obj")) ;
			for(Integer LocCtr : ItrStrg.keySet() ) {
				try {
//					file.write("Key : "+ LocCtr+" value : "+ItrStrg.get(LocCtr)+"\n") ;
					String ins_Content = OpCode_Table.get(ItrStrg.get(LocCtr).get(0)).MachineOpcode ;
					if(ItrStrg.get(LocCtr).size() > 1) {
						ins_Content += " " ;
						if(OpCode_Table.get(ItrStrg.get(LocCtr).get(0)).parameterInfo.equals("label")) {
							ins_Content += new FileTextManipulation().dec2bin(Symbol_Table.getAddress(ItrStrg.get(LocCtr).get(1))) ;
						}
						if(OpCode_Table.get(ItrStrg.get(LocCtr).get(0)).parameterInfo.equals("address")) {
							if(Symbol_Table.contains(ItrStrg.get(LocCtr).get(1)))
								ins_Content += new FileTextManipulation().dec2bin(Symbol_Table.getAddress(ItrStrg.get(LocCtr).get(1))) ;
							else
								ins_Content += new FileTextManipulation().dec2bin(Integer.parseInt(ItrStrg.get(LocCtr).get(1))) ;
						}
					}
//					System.out.println(ins_Content);
					file.write(ins_Content+"\n") ;
				}
				catch(IOException IOExp) {
					System.out.println("An IO Exception occure while wriiting the file\nKindly try again");
				}
			}
			file.close() ;
		}
		catch (Exception e) {System.out.println("Exception caught in pass two "+e);}
	}
}
