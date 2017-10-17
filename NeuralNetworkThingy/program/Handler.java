package program;

import java.util.Scanner;

import ui.DrawUserInterface;
import ui.Event;

public class Handler implements Runnable {
	private Runner runner;
	private View v;
	private boolean simPaused;
	
	public Handler(Runner runner, View v){
		this.runner = runner;
		this.v = v;
		simPaused = false;
	}
	
	public void run(){
		DrawUserInterface drawer = v.getUi();
		drawer.enableEventProcessing(true);
		
		while(true){
			Event ev = drawer.getEvent();
			//System.out.println(ev.name + " " + ev.data);
			
			if(ev.name.equals("arrow") && ev.data.equals("L"))
				runner.incrimentDetail();
			if(ev.name.equals("arrow") && ev.data.equals("R"))
				runner.decrementDetail();
			if(ev.name.equals("arrow") && ev.data.equals("U"))
				runner.decrementZoom();
			if(ev.name.equals("arrow") && ev.data.equals("D"))
				runner.incrimentZoom();
		}
	}
}
