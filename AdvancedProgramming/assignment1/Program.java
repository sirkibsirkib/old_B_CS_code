package assignment1;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Program {
	private IdentifierSetInterface a, b;
	private Map<String, String> errorMap =  new HashMap<String, String>();
	private PrintStream out;
	
	Program(){
		out = new PrintStream(System.out);
		putErrorMaps();
	}
	
	private void putErrorMaps() {
		errorMap.put("invalidParentheses", "Input needs to be surrounded by curly brackets {}");
		errorMap.put("invalidOpenBracket", "Please preface your set input with a '{'");
		errorMap.put("invalidCloseBracket", "Please end your set input with a '}'");
		errorMap.put("duplicateIdentifier", "Input cannot contain duplicate entries");
		errorMap.put("invalidFirstLetter", "All inputs must begin with a letter");
		errorMap.put("notAlphanumeric", "Input names must be alphanumeric");
		errorMap.put("tooManyElements", "Input no more than 10 identifiers");
	}
	
	private void start(){
		Scanner inputScanner = new Scanner(System.in);
		
		while (true) {
			a = buildSetFrominput(inputScanner, "Give the first set : ");
			b = buildSetFrominput(inputScanner, "Give the second set : ");
			
			printIdentifierRow("difference = ", a.difference((IdentifierSet) b));
			printIdentifierRow("intersection = ", a.intersection((IdentifierSet) b));
			printIdentifierRow("union = ", a.union((IdentifierSet) b));
			printIdentifierRow("sym. diff. = ", a.symmetricDifference((IdentifierSet) b));
		}
	}

	//READ & BUILD SETS
	private IdentifierSetInterface buildSetFrominput(Scanner inputScanner, String prompt) {
		IdentifierSetInterface newSet;
		while(true){
			try{
				out.print(prompt);
				String input="";
				try {input = inputScanner.nextLine();}
				catch(NoSuchElementException e) {System.exit(0);}
				
				newSet = lineToIdentifierSet(input.trim());
				return newSet;
			}
			catch(Exception e){out.println("    ERROR: " + errorMap.get(e.getMessage()));}
		}
	}
	
	private IdentifierSet lineToIdentifierSet(String line) throws Exception{
		IdentifierSetInterface newSet = new IdentifierSet();
		line = checkFormat(line.trim());
		
		int counter = 0;
		boolean newIdentifier = true;
		
		IdentifierInterface x = null;
		for(int i = 0; i < line.length(); i++){
			char c = line.charAt(i);
			
			if(line.charAt(i) == ' '){
				newIdentifier = true;
			}else{
				if(newIdentifier){
					if(counter > 10)			throw new Exception("tooManyElements");
					if(!Character.isLetter(c))	throw new Exception("invalidFirstLetter");
					x = new Identifier(c);
					newSet.add((Identifier)x);
					newIdentifier = false;
					counter++;
				}else{
					if(!Character.isLetterOrDigit(c))	throw new Exception("notAlphanumeric");
					x.appendId(c);
				}
			}
		}
		if(!hasDuplicates(new IdentifierSet((IdentifierSet) newSet)))
			throw new Exception("duplicateIdentifier");
		return (IdentifierSet) newSet;
	}
	
	//VALID INPUT CHECKING
	
	private boolean hasDuplicates(IdentifierSetInterface set) {
		while(set.getSize() > 0){
			IdentifierInterface x = set.getIdentifier();
			set.tryRemoveIdentifier();
			if(set.containsIdentifier(x)){	return false;}
		}
		return true;
	}

	private String checkFormat(String s) throws Exception {
		if(s.length() == 0)
			throw new Exception("invalidParentheses");
		if(s.charAt(0) != '{')
			throw new Exception("invalidOpenBracket");
		if(s.length() < 2 || s.charAt(s.length()-1) != '}')
			throw new Exception("invalidCloseBracket");
		return s.substring(1, s.length()-1);
	}
	
	//PRINT
	
	private void printIdentifierRow(String preamble, IdentifierSetInterface set) {
		String p = "";
		while(set.getSize() > 0){
			Identifier x = (Identifier) set.getIdentifier();
			set.tryRemoveIdentifier();
			p += getEntireId(x);
			if(set.getSize() > 0)	p += " ";
		}
		out.println(preamble + "{" + p + "}");	
	}
	
	private String getEntireId(IdentifierInterface x) {
		String entireId = "";
		for(int i = 0; i < x.getIdLength(); i++)	entireId += x.getIdCharacter(i);
		return entireId;	
	}

	//STATIC VOID MAIN
	public static void main(String[] args){
		new Program().start();
	}
}