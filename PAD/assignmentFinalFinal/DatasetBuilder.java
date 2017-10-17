package assignmentFinalFinal;

import java.util.Scanner;
import ui.UIAuxiliaryMethods;

public class DatasetBuilder {
	public static Dataset readDatasetData(){
		UIAuxiliaryMethods.askUserForInput();
		Scanner fileScanner = new Scanner(System.in);
		
		int numberOfClustersWanted = fileScanner.nextInt(), 
			numberOfUnits = fileScanner.nextInt(),
			numberOfVariables = fileScanner.nextInt();
		fileScanner.nextLine();
		
		fileScanner.useDelimiter("\t");
		String dataName = fileScanner.next();
		
		StringRow variableNames = readVariableNames(fileScanner.nextLine(), numberOfVariables);
		
		UnitRow units = readUnitRow(fileScanner, numberOfUnits, numberOfVariables);
		
		Dataset ds = new Dataset(numberOfClustersWanted, numberOfUnits, numberOfVariables,
				dataName, variableNames, units);
		return ds;
	}
	
	private static StringRow readVariableNames(String input, int numberOfVariables){
		Scanner namesScanner = new Scanner(input);
		namesScanner.useDelimiter("\t");
		StringRow result = new StringRow(numberOfVariables);
		for(int i = 0; i < numberOfVariables; i++){
			result.add(namesScanner.next());
		}
		namesScanner.close();
		return result;
	}
	
	private static UnitRow readUnitRow(Scanner input, int numberOfUnits, int numberOfVariables){
		UnitRow result = new UnitRow(numberOfUnits);
		for(int i = 0; i < numberOfUnits; i++){
			result.add(readUnit(input.nextLine(), numberOfVariables));
		}
		return result;
	}
	
	private static Unit readUnit(String input, int numberOfVariables){
		Scanner inputScanner = new Scanner(input);
		inputScanner.useDelimiter("\t");
		String name = inputScanner.next();
		NumberRow numbers = readNumberRow(inputScanner, numberOfVariables);
		Unit u = new Unit(name, numbers);
		return u;
	}
	
	private static NumberRow readNumberRow(Scanner inputScanner, int numberOfVariables){
		NumberRow numbers = new NumberRow(numberOfVariables);
		for(int i = 0; i < numberOfVariables; i++){
			numbers.add(inputScanner.nextDouble());
		}
		return numbers;
	}
}
