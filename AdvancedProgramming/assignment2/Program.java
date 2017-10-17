package assignment2;

import java.io.PrintStream;
import java.util.Scanner;

public class Program {
	private Interpreter interpreter;
	PrintStream out;
	
	public Program(){
		out = new PrintStream(System.out);
		interpreter = new Interpreter(out);
	}
	
	private void start(){
		Scanner lineScanner = new Scanner(System.in);
		
		while(lineScanner.hasNext()){
			try {interpreter.interpret(lineScanner.nextLine());}
			catch (APException e) {out.println(e.getMessage());}
		}
		lineScanner.close();
		out.close();
	}

	public static void main(String[] args) {
		new Program().start();
	}
}
