package assignment3;

/* 	ASK TIM:
 * 
 * ordering lowercase and uppercase:
 * 		both use compareTo of Identifier. sorting requires them to be not case sensitive
 * stacktrace? y/n
 * throw IOException? or catch it and throw APException (file read fail)
 * private & public
 * functions are not too long
 * logical and accurate function names NOT TOO LONG
 * 
 * NO STACKTRACE
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Scanner;

public class Program {
	private BinarySearchTreeInterface<IdentifierInterface> bst;
	private boolean caseSensitive;
	private boolean ascending;
	private PrintStream out;
	
	//PARSING
	
	public Program(){
		out = new PrintStream(System.out);
		bst = new BinarySearchTree<>();
		caseSensitive = true;
		ascending = true;
	}
	
	private void parse(String[] args){
		parseAllOptions(args);
		parseAllFiles(args);
		printBSTContents(bst);
	}
	
	private void parseAllOptions(String[] args) {
		for(int i = 0; i < args.length; i++){
			if(isOption(args[i])){
				try {parseOption(args[i]);}
				catch (APException e) {out.println(e.getMessage() + ": " + args[i]);}
			}
		}
	}
	
	private void parseOption(String optString) throws APException {
		switch(optString.charAt(1)){
		case 'i': caseSensitive = false;	return;
		case 'd': ascending = false;		return;
		}
		throw new APException("unknownOption");
	}
	
	private void parseAllFiles(String[] args) {
		for(int i = 0; i < args.length; i++){
			if(!isOption(args[i])){
				try {appendToBST(bst, loadFromPath(args[i]));}
				catch (IOException e){out.println("ERROR: failed to find file at path:" + args[i]);}
			}
		}
	}
	
	private boolean isOption(String string) {
		if(string.length() < 0)	return false;
		return string.charAt(0) == '-';
	}

	//BUILDING BST FROM FILE CONTENTS
	
	private String loadFromPath(String path) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(path));
		String everything;
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();
	        while (line != null) {
	            sb.append(line);
	            sb.append(System.lineSeparator());
	            line = br.readLine();
	        }
	        everything = sb.toString();
	    } finally {
	        br.close();
	    }
	    return everything;
	}

	private void appendToBST(BinarySearchTreeInterface<IdentifierInterface> bst, String s){
		Scanner stringScanner = new Scanner(s);
		stringScanner.useDelimiter("");
		IdentifierInterface x = null;
		while(stringScanner.hasNext()){
			char c =  stringScanner.next().charAt(0);
			c = caseSensitive ? c : Character.toLowerCase(c);
			if(!Character.isLetterOrDigit(c)){ //delimiter
				if(x != null){
					insertIfUnique(bst, x);
					x = null;
				}
			}else{//build this
				if(x == null){
					if(Character.isLetter(c))
						x = new Identifier(c);
					else{//skip to next delim;
						while(stringScanner.hasNext() && Character.isLetterOrDigit(c)){
							c =  stringScanner.next().charAt(0);
							c = caseSensitive ? c : Character.toLowerCase(c);
						}
					}
				}else if(Character.isLetterOrDigit(c)) //append this
					x.appendId(c);
				else
					;//do nothing
			}
		}
		stringScanner.close();
		if(x != null) insertIfUnique(bst, x);
	}
	
	private void insertIfUnique(BinarySearchTreeInterface<IdentifierInterface> bst, IdentifierInterface x){
		if(bst.contains(x))	bst.remove(x);
		else				bst.insert(x);
	}
	
	//OUTPUT
	
	private void printBSTContents(BinarySearchTreeInterface<IdentifierInterface> bst) {
		Iterator<IdentifierInterface> iterator;
		if(ascending)	iterator = bst.ascendingIterator();	 
		else			iterator = bst.descendingIterator();
		while(iterator.hasNext())
			out.println(identifierName(iterator.next()));
	}

	private String identifierName(IdentifierInterface x) {
		String s = "";
		for(int i = 0; i < x.getIdLength(); i++)
			s += x.getIdCharacter(i);
		return s;
	}

	//MAIN
	public static void main(String[] args){
		new Program().parse(args);
	}
}
