package assignment4;

import java.io.PrintStream;
import java.util.Locale;
import ui.UIAuxiliaryMethods;
import java.util.Scanner;

public class Clustering {
	private PrintStream out;
	private static final int PRESELECTED_VARIABLES = 50;

	Clustering(){
		Locale.setDefault(Locale.US);
		out = new PrintStream(System.out);
	}
	
	public void start(){
		//Questions
		////1) Cartesian? How
		// drawuserinterface
		
		//2) Pearson not working
		
		
		//3) Pearson 0.00 for Milk.txt?
		//lol
		
		//5) Understand how agglutinative works
		//
		
		UIAuxiliaryMethods.askUserForInput();
		Dataset ds = readDatasetData();
		
		ds.normalizeVariables();
		ds.preselectVariables(PRESELECTED_VARIABLES);
		
		//print preselected variable names
		for(int i = 0; i < ds.getNumberOfVariables(); i++){
			out.print(ds.getVariableName(i));
			if(i < ds.getNumberOfVariables()-1){
				if((i+1)%7 == 0){
					out.print(",\n");
				}else{
					out.print(", ");
				}
			}
		}
		System.out.println();
		System.out.println();
		
		//ASS 5?
//		UIAuxiliaryMethods.askUserForChoice("Please select a distance measure:", "Euclidean", "Manhattan", "Pearson");
//		UIAuxiliaryMethods.askUserForChoice("Please select a cluster method:", "Single Linkage", "Average Linkage", "Complete Linkage");
//		ClusterRow cr = new ClusterRow(ds);
//		DistanceMeasure d = new Euclidean();
//		ClusterMethod c = new SingleLinkage(d);
//		cr.agglutinateTo(ds.getNumberOfClustersWanted(), d, c);
		
		ClusterRow cr = new ClusterRow(ds);
		
		//ASS 4 only
		DistanceMeasure d = new Euclidean();
		ClusterMethod c = new SingleLinkage(d);
		printClusteringExample(d, c, cr);
		
		c = new AverageLinkage(d);
		printClusteringExample(d, c, cr);
		
		c = new CompleteLinkage(d);
		printClusteringExample(d, c, cr);
		
		d = new Manhattan();
		c = new SingleLinkage(d);
		printClusteringExample(d, c, cr);
		
		c = new AverageLinkage(d);
		printClusteringExample(d, c, cr);
		
		c = new CompleteLinkage(d);
		printClusteringExample(d, c, cr);
		
		d = new Pearson();
		c = new SingleLinkage(d);
		printClusteringExample(d, c, cr);
		
		c = new AverageLinkage(d);
		printClusteringExample(d, c, cr);
		
		c = new CompleteLinkage(d);
		printClusteringExample(d, c, cr);		
	}
	
	//ASS4 only
	private void printClusteringExample(DistanceMeasure d, ClusterMethod c, ClusterRow cr){
		System.out.println(d.getName() + " + " + c.name() + ":\t" +
				c.CalculateDistance(cr.getCluster(0), cr.getCluster(1)));
	}
	
	private Dataset readDatasetData(){
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
	
	private StringRow readVariableNames(String input, int numberOfVariables){
		Scanner namesScanner = new Scanner(input);
		namesScanner.useDelimiter("\t");
		StringRow result = new StringRow(numberOfVariables);
		for(int i = 0; i < numberOfVariables; i++){
			result.add(namesScanner.next());
		}
		namesScanner.close();
		return result;
	}
	
	private UnitRow readUnitRow(Scanner input, int numberOfUnits, int numberOfVariables){
		UnitRow result = new UnitRow(numberOfUnits);
		for(int i = 0; i < numberOfUnits; i++){
			result.add(readUnit(input.nextLine(), numberOfVariables));
		}
		return result;
	}
	
	private Unit readUnit(String input, int numberOfVariables){
		Scanner inputScanner = new Scanner(input);
		inputScanner.useDelimiter("\t");
		String name = inputScanner.next();
		NumberRow numbers = readNumberRow(inputScanner, numberOfVariables);
		Unit u = new Unit(name, numbers);
		return u;
	}
	
	private NumberRow readNumberRow(Scanner inputScanner, int numberOfVariables){
		NumberRow numbers = new NumberRow(numberOfVariables);
		for(int i = 0; i < numberOfVariables; i++){
			numbers.add(inputScanner.nextDouble());
		}
		return numbers;
	}
	
	//DEBUG
	private void printSample(Dataset ds, int rows, int columns){
		out.println("---Printing Sample of size " + rows + "x" + columns + "---");
		//row
		for(int i = 0; i < rows; i++){
			//column
			for(int j = 0; j < rows; j++){
				out.print(ds.getValueAt(i, j) + "\t");
			}
			out.println();
		}
		out.println();
	}
	
	public static void main(String[] args) {
		new Clustering().start();
	}
}
