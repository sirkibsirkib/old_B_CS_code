package program;

import ui.UIAuxiliaryMethods;

public class Setter {
	private String method,
			toDo,
			requestedTime;
	private int generations,
			iterations,
			depth,
			neuralSightRadius,
			hiddenLayers,
			desiredSeconds;

	private int displayDetail,
			progressIncrements,
			stimNodes;
	
	public Setter(){
		askForDetail();
		askForNetworkRadius();
		askForHiddenLayers();
		askForDesiredSeconds();
		adjustAndOfferSettings();
		
		while(!toDo.equals("BEGIN simulation!")){
			switch(toDo){
			case "Set detail level":				askForDetail(); 		adjustAndOfferSettings();	break;
			case "Set estimated simulation time":	askForDesiredSeconds();	adjustAndOfferSettings();	break;
			case "Set network radius":				askForNetworkRadius();	adjustAndOfferSettings();	break;
			case "Set hidden layers":				askForHiddenLayers();	adjustAndOfferSettings();	break;
			case "Re-generate settings":									adjustAndOfferSettings();	break;
			}
		}
	}
	
	private void askForDesiredSeconds(){
		requestedTime = "5min";
		requestedTime = UIAuxiliaryMethods.askUserForChoice("Select approximate desired simulation time\nto tailor reccomended settings",
				"5min", "2min", "10min", "15min", "20min", "30min", "1hr", "2hr", "4hr", "8hr", "24hr");
		switch(requestedTime){
		case "2min": 	desiredSeconds = 2*60;		break;
		case "5min": 	desiredSeconds = 5*60;		break;
		case "10min": 	desiredSeconds = 10*60;		break;
		case "15min": 	desiredSeconds = 15*60;		break;
		case "20min": 	desiredSeconds = 20*60;		break;
		case "30min": 	desiredSeconds = 30*60;		break;
		case "1hr": 	desiredSeconds = 1*60*60;	break;
		case "2hr": 	desiredSeconds = 2*60*60;	break;
		case "4hr": 	desiredSeconds = 4*60*60;	break;
		case "8hr": 	desiredSeconds = 8*60*60;	break;
		case "24hr": 	desiredSeconds = 24*60*60;	break;
			default:	desiredSeconds = 5*60;		break;
		}
	}
	
	private void offserSettings(){
		toDo = "Re-generate settings";
		toDo = UIAuxiliaryMethods.askUserForChoice("Generations: " + generations
				+ "\nIterations: " + iterations + "\nRecursive Depth: " + depth
				+ "\n\nEstimated Time: " + requestedTime
				+ "\n\nHow would you like to proceed?",
				"Re-generate settings", "Set detail level", "Set estimated simulation time", "Set network radius",
				"Set hidden layers", "BEGIN simulation!");
	}
	
	private void askForDetail(){
		String choice = "Fast";
		choice = UIAuxiliaryMethods.askUserForChoice("Select a display mode",
				"Fast", "Intermediate", "Detailed");
		switch(choice){
		case "Fast":			displayDetail = 0;	progressIncrements = 800;	break;
		case "Intermediate":	displayDetail = 1;	progressIncrements = 400;	break;
		case "Detailed":		displayDetail = 2;	progressIncrements = 40;	break;
		default:				displayDetail = 0;	progressIncrements = 800;	break;
		}
	}
	
	private void askForNetworkRadius(){
		String choice = "(7x7)";
		choice = UIAuxiliaryMethods.askUserForChoice("Select neural stimulus template",
				"(7x7)", "(3x3)", "(5x5)", "(9x9)", "(11x11)", "(15x15)");
		switch(choice){
		case "(3x3)":	neuralSightRadius = 1;	break;
		case "(5x5)":	neuralSightRadius = 2;	break;
		case "(7x7)":	neuralSightRadius = 3;	break;
		case "(9x9)":	neuralSightRadius = 4;	break;
		case "(11x11)":	neuralSightRadius = 5;	break;
		case "(15x15)":	neuralSightRadius = 7;	break;
		default:		neuralSightRadius = 3;	break;
		}
		stimNodes = (neuralSightRadius*2)+1;
		stimNodes *= stimNodes;
	}
	
	private void askForHiddenLayers(){
		String choice = "4";
		choice = UIAuxiliaryMethods.askUserForChoice("Enumerate the number of hidden layers",
				"4", "no layers", "1", "2", "3", "5", "6");
		switch(choice){
		case "no layers":	hiddenLayers = 0;	break;
		case "1":			hiddenLayers = 1;	break;
		case "2":			hiddenLayers = 2;	break;
		case "3":			hiddenLayers = 3;	break;
		case "4":			hiddenLayers = 4;	break;
		case "5":			hiddenLayers = 5;	break;
		case "6":			hiddenLayers = 6;	break;
			default:		hiddenLayers = 4;	break;
		}
	}
	
	private void adjustAndOfferSettings(){
		int simsPerMinute = 1;
		int defaultStimWeight = 49;
		
		switch(displayDetail){
		case 0:	simsPerMinute = 7200/stimNodes*defaultStimWeight;	break;
		case 1:	simsPerMinute = 2000/stimNodes*defaultStimWeight;	break;
		case 2: simsPerMinute = 90/stimNodes*defaultStimWeight;	break;
		}
		int simsWeHaveTimeFor = desiredSeconds/60*simsPerMinute;

		int g = 2,
			i = 3,
			d = 1;
		boolean gMaxed = false,
				iMaxed = false,
				dMaxed = false;
		String lastToIncriment = "none";
		
		while(!gMaxed || !iMaxed || !dMaxed){
			double weight = Math.random();
			if(weight < .45 && !gMaxed){
				if(generations*depth < 250){
					g = (int)(g*1.1 + 1);
					lastToIncriment = "g";
				}else
					gMaxed = true;
			}
			else if(weight < .85 && !iMaxed){
				i = (int)(i*1.1 + 1);
				lastToIncriment = "i";
			}
			else if(!dMaxed){
				d = (int)(d*1.1 + 1);
				lastToIncriment = "d";
			}
			if(Runner.predictSims(g, i, d) > simsWeHaveTimeFor){
				switch(lastToIncriment){
				case "g":	g--;	gMaxed = true;	break;
				case "i":	i--;	iMaxed = true;	break;
				case "d":	d--;	dMaxed = true;	break;
				}
			}
		}
		
		generations = g;
		iterations = i;
		depth = d;
		
		offserSettings();
	}
	
	//GETS
	
	public String getMethod() {
		return method;
	}

	public int getGenerations() {
		return generations;
	}

	public int getIterations() {
		return iterations;
	}

	public int getDepth() {
		return depth;
	}
	
	public int getDisplayDetail() {
		return displayDetail;
	}

	public int getProgressIncrements() {
		return progressIncrements;
	}
	
	public int getNeuralSightRadius() {
		return neuralSightRadius;
	}
	
	public int getHiddenLayers() {
		return hiddenLayers;
	}
}
