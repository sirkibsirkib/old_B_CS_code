package prac2;
	
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import java.io.PrintStream;
import java.io.PrintWriter;

public class Main {
	
	static String input, output;
	static int task;
	
  static void writeToFile (int result) {
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
		
		writer.println(result + "");

		writer.close();
	}
	
	static int[] parseArray (Scanner counter, Scanner reader) {
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
		
		return parseArray(counter, reader);
	}
	
  static int[][] parseMatrix (Scanner counter, Scanner reader) { // n x n
    int n = 0;
    
    while (counter.hasNextLine()) {
      counter.nextLine();
      n += 1;
    }
     
    int[][] result = new int[n][n];

    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        try {
          result[i][j] = reader.nextInt();
        } catch (Exception e) {
          System.out.println("Error while parsing matrix. Make sure it is a" +
            "square matrix (i.e. a matrix with an equal number of rows and" +
            "columns");
          System.exit(1);
        }
      }
      reader.nextLine();
    }
    
    return result;
  }
	
	static int[][] getMatrix () {
	  Scanner counter, reader;
		counter = reader = null;
		
		try {
			counter = new Scanner(new File(input));
			reader = new Scanner(new File(input));
		} catch (FileNotFoundException e) {
			System.out.println("Unable to find file '" + input + "'.");
			System.exit(1);
		}
		
		return parseMatrix(counter, reader);
	}
	
	static void parseArguments (String[] args) {
		if (args.length != 3) {
			System.out.println("Please provide exactly three arguments:");
			System.out.println("    <input file> <output> <task>");
			System.out.println("where <task> is either 1, 2 or 3");
			System.exit(1);
		}
		
		input = args[0];
		output = args[1];
		
		if (args[2].equals("1")) {
			task = 1;
		} else if (args[2].equals("2")) {
			task = 2;
		} else if (args[2].equals("3")) {
		  task = 3;
		} else {
			System.out.println("Argument <task> must be either 1, 2 or 3.");
			System.exit(1);
		}
	}
	
	public static void main (String[] args) {
		parseArguments(args);
		
		int result;
		
		if (task == 1) {
		  result = Tasks.task1(getArray());
		} else if (task == 2) { 
		  result = Tasks.task2(getArray());
		} else { // task == 3
		  result = Tasks.task3(getMatrix());
		}
		
		writeToFile(result);
	}
}
