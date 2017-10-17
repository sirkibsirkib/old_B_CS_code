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
//			if(ev.name.equals("mouseover")){
//				boolean hotSpotFire = false;
//				if(ev.data.charAt(0) == 't')
//					hotSpotFire = true;
//				String intData = ev.data.substring(2);
//				System.out.println(intData);
//				Scanner hotspotScanner = new Scanner(intData);
//				hotspotScanner.useDelimiter(",");
//				int screenX = hotspotScanner.nextInt(),
//					screenY = hotspotScanner.nextInt();
//				hotspotScanner.close();
//				v.drawStimHotspotSquare(hotSpotFire, ev.data.charAt(1), screenX, screenY);
//				
//			}
//			if(ev.name.equals("mouseexit")){
//				v.clear();
//			}
		}
	}
}
