package program;

import ui.Colour;
import ui.DrawUserInterface;
import ui.Event;
import ui.UIAuxiliaryMethods;

public class Runner {
	static final Colour BLACK = new Colour(0,0,0),
			RED = new Colour(200, 0, 0),
			GREEN = new Colour(0, 255, 0),
			BLUE = new Colour(3, 135, 250),
			GRAY = new Colour(180, 180, 180),
			PINK = new Colour(255, 200, 200),
			WHITE = new Colour(255, 255, 255);
	long previousTime = System.currentTimeMillis(),
			timeTaken = 0,
			expectedSims = 1,
			doneSims = 0;
	static final int SIMULATION_TIMEOUT = 100,
			STUCK_TIMEOUT = 2;
	String alterationInfo = "";
	private int hiddenLayers,
			neuralSightRadius = 3,
			viewSightRadius = 5;
	private int generations = 4,
			networkIterations = 7,
			recursiveDepth = 4;
	private int progressOutput = 0,
			progressIncrements = 200;
	private int displayDetail = 1; //0 low 1 med 2 high
	
	public void start() throws InterruptedException{
		Room r = new Room();
		View v = new View();
		
		Thread.sleep(300);
		
		Setter setr = new Setter();
		networkIterations = setr.getIterations();
		recursiveDepth = setr.getDepth();
		generations = setr.getGenerations();
		displayDetail = setr.getDisplayDetail();
		progressIncrements = setr.getProgressIncrements();
		neuralSightRadius = setr.getNeuralSightRadius();
		hiddenLayers = setr.getHiddenLayers();
		
		generateSimulations(r, v);
		
		//TODO add event handler for key inputs
	}
	
	
	private void displayFinished(DrawUserInterface drawer, Network net, Room r, View v) throws InterruptedException{
		v.getUi().printf("100%% done%n");
		while(true){
			double complexity = net.getComplexity(),
				success = play(net, r, v) - complexity + (Math.random()*complexity/networkIterations);
			drawer.printf("Neural network achieves a score of (%f)%n", success);
		}
	}
	
	public static long predictSims(int gens, int its, int recursDepth){
		long expectedRecursiveSims = -1;
		for(int i = 0; i <= recursDepth; i++)
			expectedRecursiveSims += gens*Math.pow(its, i);
		return expectedRecursiveSims;
	}
	
	private void generateSimulations(Room r, View v) throws InterruptedException{
		expectedSims = predictSims(generations, networkIterations, recursiveDepth);
		
		DrawUserInterface drawer = v.getUi();
		drawer.printf("Expected simulations: %d%n", expectedSims);
		
		Network original = new Network(neuralSightRadius, hiddenLayers);
		
		original.resetFires();
		original.fireStimuli(r, neuralSightRadius);
		v.draw(original, r, viewSightRadius, neuralSightRadius, 0);
		
		for(int i = 0; i < generations; i++){
			String simInfo = "Generation (" + (i+1) + "/" + generations + ")";
			if(displayDetail == 0){
				v.clear();
				v.draw(original, r, viewSightRadius, neuralSightRadius, 0);
				v.drawSimContext("Sim #" + doneSims + "/" + expectedSims + ":", simInfo);
			}
			
			original = improveNetwork(original, r, v, recursiveDepth, simInfo);
			
			double complexity = original.getComplexity(),
				success = simulatePlay(original, r, v, simInfo) - complexity + (Math.random()*complexity/networkIterations);
			drawer.printf("Generation {%d/%d} completed with a score of (%f)%n", i+1, generations, success);
			if(displayDetail >= 1)
				Thread.sleep(400);
		}
		Thread.sleep(1000);
		System.out.println();
		System.out.println();
		
		displayFinished(drawer, original, r, v);
	}
	
	//RECURSE CALL
	private Network improveNetwork(Network original, Room r, View v, int depth, String simInfo) throws InterruptedException{
		//recurse inwards
		Network[] networks = new Network[networkIterations];
		double[] successes = new double[networkIterations];
		
		for(int j = 0; j < networks.length; j++){
			r.refreshRoom();
			double ratioNetworksDone =1.0*j / networks.length;
			
			if(j == 0)
				networks[j] = original;
			else
				networks[j] = original.duplicate();
			
			String simInfo2 = "";
			boolean assigned = false;
			if(j == 0){
				simInfo2 = simInfo + " original";
				assigned = true;
			}
			if(!assigned && ratioNetworksDone <= .1){
				simInfo2 = simInfo + " +" + (j+1) + "clean";
				networks[j].mutateGrow();
				assigned = true;
			}
			if(!assigned && ratioNetworksDone <= .2){
				simInfo2 = simInfo + " +" + (j+1) + "grow";
				networks[j].mutateGrow();
				assigned = true;
			}
			if(!assigned && ratioNetworksDone <= .7){
				simInfo2 = simInfo + " +" + (j+1) + "AIMD";
				networks[j].mutateAIMD();
			}
				
			if(!assigned && ratioNetworksDone <= .85){
				simInfo2 = simInfo + " +" + (j+1) + "trim";
				networks[j].mutateTrim();
				assigned = true;
			}
				
			if(!assigned && ratioNetworksDone <= 1){
				simInfo2 = simInfo + " +" + (j+1) + "jumble";
				networks[j].mutateJumble();
				assigned = true;
			}
			
			if(depth > 1)
				networks[j] = improveNetwork(networks[j], r, v, depth-1, simInfo2);
			
			double complexity = networks[j].getComplexity();
			successes[j] = simulatePlay(networks[j], r, v, simInfo2) - complexity + (Math.random()*complexity/networkIterations);
		}
		if(doneSims >= progressOutput + progressIncrements){
			updateProgress(v);
		}
		//recurse outwards
		//TODO v.drawBranchyDiagram(simInfo2)
		return networks[indexOfLargest(successes)];
	}
	
	private void updateProgress(View v){
		progressOutput += progressIncrements;
		long currentTime = System.currentTimeMillis();
		timeTaken += currentTime - previousTime;
		previousTime = currentTime;
		
		v.getUi().printf("%d%% done.%s%n", (int)(100.0*doneSims/expectedSims), timeRemaining());
	}
	
	private int indexOfLargest(double... values){
		int indexSoFar = 0;
		double valueSoFar = -Double.MAX_VALUE;
		for(int i = 0; i < values.length; i++){
			if(values[i] > valueSoFar){
				valueSoFar = values[i];
				indexSoFar = i;
			}
		}
		return indexSoFar;
	}
	
	private double simulatePlay(Network net, Room r, View v, String simInfo) throws InterruptedException{
		r.refreshRoom();
		int stuckFor = 0,
			manX = r.getPlayerX(),
			manY = r.getPlayerY();
		
		for(int i = 0; i < SIMULATION_TIMEOUT; i++){
			if(displayDetail == 2){
				v.clear();
				v.drawSimContext("Sim #" + doneSims + " / " + expectedSims + ":", simInfo);
			}	
			
			net.resetFires();			
			net.memoryFire();
			net.fireStimuli(r, neuralSightRadius);
			r.inputsFromFires(net.getButtonFireNames());
			
			if(displayDetail == 2){
				v.draw(net, r, viewSightRadius, neuralSightRadius, 1.0*i/SIMULATION_TIMEOUT);
				Thread.sleep(200);
			}
			
			int manX2 = r.getPlayerX(),
				manY2 = r.getPlayerY();
			if(manX == manX2 && manY == manY2){
				if(stuckFor >= STUCK_TIMEOUT){
					i = SIMULATION_TIMEOUT;
				}else{
					stuckFor++;
				}
			}else{
				manX = manX2;
				manY = manY2;
				stuckFor = 0;
			}
		}
		if(displayDetail >= 1){
			v.clear();
			v.draw(net, r, viewSightRadius, neuralSightRadius, 1.0);
			if(displayDetail == 1)
				v.drawSimContext("Sim #" + doneSims + " / " + expectedSims + ":", simInfo);
			Thread.sleep(5);
		}
		if(displayDetail >= 1)
			Thread.sleep(40);

		doneSims++;
		return r.playerProgress();
	}
	
	private String timeRemaining(){
		String remaining = " Estimated time remaining:";
		if(timeTaken == 0){
			return "";
		}
		long totalMilliseconds = timeTaken/doneSims*expectedSims - timeTaken;
		
		long totalSeconds = totalMilliseconds / 1000;
		long seconds = totalSeconds % 60;
		long totalMinutes = totalSeconds / 60;
		long minutes = totalMinutes % 60;
		long totalHours = totalMinutes / 60;
		long hours = totalHours % 24;
		long totalDays = totalHours / 24;
		long days = totalDays % 365;
		long totalYears = totalDays / 365;
		long years = totalYears % 1000;
		long totalMillennia = totalYears / 1000;
		long millennia = totalMillennia % 1000;
		long age = totalMillennia / 1000;
		
		if(age > 0)
			remaining += " " + age + " Ages";
		if(millennia > 0)
			remaining += " " + millennia + " Millennia";
		if(years > 0 )
			remaining += " " + years + " Years";
		if(days > 0 && age == 0)
			remaining += " " + days + " Days";
		if(hours > 0 && millennia == 0 && age == 0)
			remaining += " " + hours + " Hours";
		if(minutes > 0 && years == 0 && millennia == 0 && age == 0)
			remaining += " " + minutes + " Minutes";
		if(seconds > 0 && days == 0 && years == 0 && millennia == 0 && age == 0)
			remaining += " " + seconds + " Seconds";
		return remaining;
		
	}
	
	private double play(Network net, Room r, View v) throws InterruptedException{
		//DrawUserInterface drawer = v.getUi();
		r.refreshRoom();
		int stuckFor = 0,
				manX = r.getPlayerX(),
				manY = r.getPlayerY();
		
		Thread.sleep(500);
		for(int i = 0; i < SIMULATION_TIMEOUT; i++){
			net.resetFires();
			v.clear();
			
			net.memoryFire();
			net.fireStimuli(r, neuralSightRadius);
			r.inputsFromFires(net.getButtonFireNames());
			
			v.draw(net, r, viewSightRadius, neuralSightRadius, 1.0*i/SIMULATION_TIMEOUT);
			Thread.sleep(250);
			
			int manX2 = r.getPlayerX(),
				manY2 = r.getPlayerY();
			if(manX == manX2 && manY == manY2){
				if(stuckFor >= STUCK_TIMEOUT){
					i = SIMULATION_TIMEOUT;
				}else{
					stuckFor++;
				}
			}else{
				manX = manX2;
				manY = manY2;
				stuckFor = 0;
			}
		}
		v.clear();
		v.draw(net, r, viewSightRadius, neuralSightRadius, 1.0);
		
		Thread.sleep(50);
		return r.playerProgress();
	}
	
	public static void main(String[] args) throws InterruptedException {
		new Runner().start();
	}
	
	public static int rng(int range){ //returns a random integer from 0 to range-1
		return (int)(Math.random()*range);
	}
	
	public static int expRng(int range){
		return (int)(Math.sqrt(Math.random()*range*range));
	}
	
	public static boolean coinToss(){
		return (int)(Math.random()*2) == 0;
	}
	
	public static boolean dieToss(int dieSides){
		return (int)(Math.random()*dieSides) == 0;
	}
}
