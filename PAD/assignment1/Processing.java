package assignment1;

import java.io.PrintStream;
import java.util.Locale;
import ui.UIAuxiliaryMethods;
import java.util.Scanner;

public class Processing {
	Dataset allData;
	PrintStream out;
	int numberOfClustersWanted, numberOfUnits, numberOfVariables; 
	
	Processing(){
		Locale.setDefault(Locale.US);
		out = new PrintStream(System.out);
	}
	
	void start(){
		UIAuxiliaryMethods.askUserForInput();
		parseInputFile();
		out.println("the maximum value of the variable '"
				+ allData.variableNames[0] + "' is "
				+ allData.maxValueInVariableColumn(0));
	}
	
	void parseInputFile(){
		Scanner in = new Scanner(System.in);
		
		//read first 3 lines
		numberOfClustersWanted = in.nextInt();
		numberOfUnits = in.nextInt();
		numberOfVariables = in.nextInt();
		in.nextLine();
		
		allData = new Dataset(numberOfUnits);

		//read 4th line
		parseNames(in.nextLine());
		
		//read the rest of the file
		parseData(in);
	}
	
	void parseNames(String line){
		Scanner lineScanner = new Scanner(line);
		lineScanner.useDelimiter("\t");
		
		//save the name of the sheet
		allData.dataName = lineScanner.next();
		
		//save the variable names in the units
		allData.variableNames = new String[numberOfVariables];
		for(int i = 0; i < numberOfVariables; i++){
			allData.variableNames[i] = lineScanner.next();
		}
		lineScanner.close();
	}
	
	void parseData(Scanner dataScanner){
		dataScanner.useDelimiter("\t");
		//iterates once per line
		for(int i = 0; i < numberOfUnits; i++){
			allData.units.addUnit(createUnit(dataScanner.nextLine(), i));
		}
	}
	
	Unit createUnit(String dataLine, int entryNumber){		
		Scanner lineScanner = new Scanner(dataLine);
		lineScanner.useDelimiter("\t");
		
		//populates a Unit with it's columns of doubles
		Unit u = new Unit(numberOfVariables);
		u.name = lineScanner.next();
		for(int j = 0; j < numberOfVariables; j++){
			u.numbers.add(lineScanner.nextDouble());
		}
		lineScanner.close();
		return u;
	}
	
	public static void main(String[] args) {
		new Processing().start();
	}
}
