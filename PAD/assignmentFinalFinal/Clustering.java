package assignmentFinalFinal;

import java.util.Locale;
import ui.DrawUserInterface;
import ui.Event;
import ui.UIAuxiliaryMethods;
import ui.UserInterfaceFactory;

public class Clustering{
	 //Christopher Esterhuyse
	 //Project Application Development
	 //VU Amsterdam 2015
	
	private static final int PRESELECTED_VARIABLES = 50,
		GRAPH_PIXEL_SIZE = 900;

	public Clustering(){
		Locale.setDefault(Locale.US);
	}
	
	public void start() throws InterruptedException{
		Dataset ds = DatasetBuilder.readDatasetData();
		ds.normalizeVariables();
		ds.preselectVariables(PRESELECTED_VARIABLES);
		
		ClusterRow cr = new ClusterRow(ds);
		
		DistanceMeasure dm = askUserForDistanceMeasure();
		ClusterMethod cm = askUserForClusterMethod(dm);
		String displayChoice = getDisplayChoice(ds.getNumberOfVariables());
		
		DrawUserInterface ui = UserInterfaceFactory.getDrawUI(GRAPH_PIXEL_SIZE, GRAPH_PIXEL_SIZE);
		View v = setupView(ds, cr, ui, displayChoice);
		v.draw(cr);
		
		handleEvents(ui, v, ds, cr, cm);
	}
	
	private void handleEvents(DrawUserInterface ui, View v, Dataset ds, ClusterRow cr, ClusterMethod cm) throws InterruptedException{
		ui.enableEventProcessing(true);
		printMessages(ui);
		
		int stepsWanted = cr.getNumberOfClusters() - ds.getNumberOfClustersWanted(),
			stepsTaken = 0;
		boolean enableHotspots = v instanceof Cartesian;
		Clusterer crr = new Clusterer(cr, cm);
		
		while(true){
			Event userInput = ui.getEvent();
			if(userInput.name.equals("arrow") && userInput.data.equals("R")){
				if(stepsTaken < stepsWanted){
					stepsTaken++;
					ui.printf("Step %d/%d\n", stepsTaken, stepsWanted);
					v.clear();
					v.draw(crr.getClusterRow(stepsTaken));
				}
			}
			if(userInput.name.equals("arrow") && userInput.data.equals("L")){
				if(stepsTaken > 0){
					stepsTaken--;
					ui.printf("Step %d/%d\n", stepsTaken, stepsWanted);
					v.clear();
					v.draw(crr.getClusterRow(stepsTaken));
				}
			}
			if(userInput.name.equals("mouseover") && enableHotspots){
				Cartesian cart = (Cartesian)v;
				cart.hotspotLabel(userInput.data);
			}
			if(userInput.name.equals("mouseexit") && enableHotspots){ 
				v.clear();
				v.draw(crr.getClusterRow(stepsTaken));
			}
			if(userInput.name.equals("other_key") && userInput.data.equals("Backspace")){
				v.clear();
				printMessages(ui);
				crr = getNewSettings(ds, ui);
				stepsTaken = 0;
				v.draw(crr.getClusterRow(stepsTaken));
			}
			if(userInput.name.equals("other_key") && userInput.data.equals("Escape")){
				queryExit();
			}
			Thread.sleep(30);
		}
	}
	
	private void printMessages(DrawUserInterface ui){
		ui.clearStatusBar();
		ui.printf("Press [Backspace] to change selected options. Press [Escape] to exit the program.\n");
		ui.printf("Press [left] and [right] keys to change the number of clusters.\n");
	}
	
	private Clusterer getNewSettings(Dataset ds, DrawUserInterface ui){
		ClusterRow cr = new ClusterRow(ds);
		
		DistanceMeasure d = askUserForDistanceMeasure();
		ClusterMethod c = askUserForClusterMethod(d);
		
		Clusterer crr = new Clusterer(cr, c);
		return crr;
	}
	
	private DistanceMeasure askUserForDistanceMeasure(){
		String choice = UIAuxiliaryMethods.askUserForChoice("Please select a distance measure", "Euclidean", "Manhattan", "Pearson");
		switch(choice){
			case("Euclidean"):	return new Euclidean();
			case("Manhattan"):	return new Manhattan();
			case("Pearson"):	return new Pearson();
			default:			return new Pearson();
		}
	}
	
	private ClusterMethod askUserForClusterMethod(DistanceMeasure d){
		String choice = UIAuxiliaryMethods.askUserForChoice("Please select a cluster method",
				"Single Linkage", "Average Linkage", "Complete Linkage");
		switch(choice){
			case("Single Linkage"):		return new SingleLinkage(d);
			case("Average Linkage"):	return new AverageLinkage(d);
			case("Complete Linkage"):	return new CompleteLinkage(d);
			default:					return new AverageLinkage(d);
		}
	}
	
	private String getDisplayChoice(int numberOfVariables){
		if(numberOfVariables == 2){
			return UIAuxiliaryMethods.askUserForChoice("Please select a graph type", "Cartesian Plot", "Dendrogram");
		}else{
			return "Dendrogram";
		}
	}
	
	private View setupView(Dataset ds, ClusterRow cr, DrawUserInterface ui, String displayChoice){
		if(displayChoice.equals("Cartesian Plot")){
			return new Cartesian(ds.getDataName(), ds.getVariableName(0), ds.getVariableName(1), ui, GRAPH_PIXEL_SIZE);
		}else{
			return new Dendrogram(ds.getDataName(), ui, GRAPH_PIXEL_SIZE);
		}
	}
	
	private void queryExit(){
		String choice = UIAuxiliaryMethods.askUserForChoice("Exit the program?", "Yes", "No");
		if(choice.equals("Yes")){
			System.exit(1);
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		new Clustering().start();
	}
}
