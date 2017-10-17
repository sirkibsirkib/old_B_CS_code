package prac1;
	
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

public class Main {
	
	final static String BINARY = "b",
					  TERNARY = "t";
	
	static String input, output;
	static boolean binaryHeap;
	
	static void printToFile (int[] sorted) {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(output, "UTF-8");
		} catch (FileNotFoundException e) {
			System.out.println("Error while writing to file (FileNotFoundException).");
			System.out.println("Please contact Femke.");
			System.exit(1);
		} catch (UnsupportedEncodingException e) {
			System.out.println("Error while writing to file (UnspportedEncodingException).");
			System.out.println("Please contact Femke.");
			System.exit(1);
		}
		
		int limit = sorted.length - 1;
		for (int i = 0; i < limit; i++) {
			writer.print(sorted[i] + " ");
		}
		writer.println(sorted[limit] + "");
		writer.close();
	}
	
	static int[] parseInput(Scanner counter, Scanner reader) {
		int elements = 0;
		
		while (counter.hasNext()) {
			counter.nextInt();
			elements += 1;
		}
		
		int[] result = new int[elements];
		
		for (int i = 0; i < elements; i++) {
			result[i] = reader.nextInt();
		}
		
		return result;
	}
	
	
	static int[] getArray () {
		Scanner counter, reader;
		counter = reader = null;
		
		try {
			counter = new Scanner(new File(input));
			reader = new Scanner(new File(input));
		} catch (FileNotFoundException e) {
			System.out.println("Unable to find file '" + input + "'.");
			System.exit(1);
		}
		
		return parseInput(counter, reader);
	}
	
	static void parseArguments (String[] args) {
		if (args.length != 3) {
			System.out.println("Please provide exactly three arguments:");
			System.out.println("    <input file> <output file> <heap type>");
			System.out.println("where <heap type> is 'b' iff the heap should be binary");
			System.out.println("or 't' iff the heap should be ternary.");
			System.exit(1);
		}
		
		input = args[0];
		output = args[1];
		
		if (args[2].toLowerCase().equals(BINARY)) {
			binaryHeap = true;
		} else if (args[2].toLowerCase().equals(TERNARY)) {
			binaryHeap = false;
		} else {
			System.out.println(args[2]);
			System.out.println("The third argument must be either 'b' for binary heap");
			System.out.println("or 't' for ternary heap.");
			System.exit(1);
		}
	}

	public static void main (String[] args) {
		parseArguments(args);
		int[] toSort = getArray();
		int[] sorted = HeapSorter.start(toSort, binaryHeap);
		printToFile(sorted);
	}
}
