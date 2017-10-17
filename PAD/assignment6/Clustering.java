package assignment6;

import java.io.PrintStream;
import java.util.Locale;
import ui.DrawUserInterface;
import ui.Event;
import ui.UIAuxiliaryMethods;

import java.util.Scanner;

public class Clustering{
	private PrintStream out;
	private static final int PRESELECTED_VARIABLES = 50;

	Clustering(){
		Locale.setDefault(Locale.US);
		out = new PrintStream(System.out);
	}
	
	public void start() throws InterruptedException{
		UIAuxiliaryMethods.askUserForInput();
		
		Dataset ds = readDatasetData();
		ds.normalizeVariables();
		ds.preselectVariables(PRESELECTED_VARIABLES);
//		
//		//DEBUG
//		printPreselectedVariables(ds);
		
		ClusterRow cr = new ClusterRow(ds);
		
		DistanceMeasure d = askUserForDistanceMeasure();
		ClusterMethod c = askUserForClusterMethod(d);
		Clusterer clusterMaker = new Clusterer(ds, cr);
		
		View v = setupView(ds, cr);
		v.draw();
		
		handleEvents(v.getInputSource(), v, ds, cr, clusterMaker, c);
	}
	
	private DistanceMeasure askUserForDistanceMeasure(){
		String choice = UIAuxiliaryMethods.askUserForChoice("Please select a distance measure",
				"Euclidean", "Manhattan", "Pearson");
		
		DistanceMeasure chosenOption = new Euclidean();
		switch(choice){
		case("Manhattan"):	chosenOption = new Manhattan();	break;
		case("Pearson"):	chosenOption = new Pearson();	break;
		}
		return chosenOption;
	}
	
	private ClusterMethod askUserForClusterMethod(DistanceMeasure d){
		String choice = UIAuxiliaryMethods.askUserForChoice("Please select a cluster method",
				"Single Linkage", "Average Linkage", "Complete Linkage");
		
		ClusterMethod chosenOption = new SingleLinkage(d);
		switch(choice){
		case("Average Linkage"):	chosenOption = new AverageLinkage(d);	break;
		case("Complete Linkage"):	chosenOption = new CompleteLinkage(d);	break;
		}
		return chosenOption;
	}
	
	private View setupView(Dataset ds, ClusterRow cr){
		View v;
		if(ds.getNumberOfVariables() == 2){
			v = new Cartesian(cr, ds.getDataName(), ds.getVariableName(0), ds.getVariableName(1));
		} else {
			//REPLACE THIS WITH DENDROGRAM
			v = new Dendrogram(cr, ds.getDataName());
		}	
		return v;
	}
	
	//DEBUG
//	private void printPreselectedVariables(Dataset ds){
//		for(int i = 0; i < ds.getNumberOfVariables(); i++){
//			out.print(ds.getVariableName(i));
//			if(i < ds.getNumberOfVariables()-1){
//				if((i+1)%7 == 0){
//					out.print(",\n");
//				}else{
//					out.print(", ");
//					
//				}
//			}
//		}
//		System.out.println();
//		System.out.println();
//	}
	
	private void handleEvents(DrawUserInterface inputSource, View v, Dataset ds, ClusterRow cr, Clusterer clusterMaker, ClusterMethod c) throws InterruptedException{
		inputSource.enableEventProcessing(true);
		Event userInput;

		inputSource.printf("Press 'A' to proceed through clusters\n");
		int numberOfClustersWanted = ds.getNumberOfClustersWanted(),
			numberOfLeaves = ds.getNumberOfUnits();
		while(true){
			userInput = inputSource.getEvent();
			switch(userInput.data){
				case("a"):{ 
					int numberOfClusters = cr.getNumberOfClusters();
					if(numberOfClusters > numberOfClustersWanted){
						clusterMaker.agglutinateOneStep(c);
						v.clear();
						v.draw();
						numberOfClusters--;
						
						inputSource.printf("Step %d/%d with %d clusters\n", numberOfLeaves-numberOfClusters,
								numberOfLeaves-numberOfClustersWanted, numberOfClusters);
					}break;
				}
			}
			Thread.sleep(100);
		}
	}
	
//	//ASS4 only
//	private void printClusteringExample(DistanceMeasure d, ClusterMethod c, ClusterRow cr){
//		System.out.println(d.getName() + " + " + c.getName() + ":\t" +
//				c.CalculateDistance(cr.getClusterAt(0), cr.getClusterAt(1)));
//	}
	
	//READ INPUT
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
//	
//	//DEBUG
//	private void printSample(Dataset ds, int rows, int columns){
//		out.println("---Printing Sample of size " + rows + "x" + columns + "---");
//		//row
//		for(int i = 0; i < rows; i++){
//			//column
//			for(int j = 0; j < rows; j++){
//				out.print(ds.getValueAt(i, j) + "\t");
//			}
//			out.println();
//		}
//		out.println();
//	}
	
	public static void main(String[] args) throws InterruptedException {
		new Clustering().start();
	}
}
