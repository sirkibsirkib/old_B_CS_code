package game;

import ui.Colour;
import ui.DrawUserInterface;
import ui.Event;
import ui.UserInterfaceFactory;

public class ShipTest {
double coordinate, destination, time, speed,
	accel = 1;
DrawUserInterface ui;
	
	private void start() throws InterruptedException {
		ui = UserInterfaceFactory.getDrawUI(500, 500);
		setup();
		listen();
		
	}
	
	private void listen() throws InterruptedException {
		while(true){
			Event userInput = ui.getEvent();
			System.out.println(userInput.data + " " + userInput.name);
			coordinate += speed;
			time++;
			handle(userInput.name, userInput.data);
			ui.clear();
			ui.drawText(30, 30, "Speed:\t" + (int)(speed), new Colour(0,0,0));
			ui.drawText(30, 70, "Distance:\t" + (int)(coordinate), new Colour(0,0,0));
			ui.drawText(30, 90, "Destination:\t" + (int)(destination), new Colour(0,0,0));
			ui.drawText(30, 110, "Time:\t" + getTime((int) time), new Colour(0,0,0));
			double predSpd = speed + (3*accel);
			if(speed > 0)
				ui.drawText(30, 130, "ETA:\t" + getTime((int) ((destination-coordinate)/speed)), new Colour(0,0,0));
			else
				ui.drawText(30, 130, "TripTime:\t" + getTime((int) ((destination-coordinate))), new Colour(0,0,0));
			if(speed*speed/accel/2 > destination-coordinate)
				ui.drawText(30, 150, "OVERSHOOT", new Colour(0,0,0));
			else if(predSpd*predSpd/accel/2 > (destination-coordinate)*.75)
				ui.drawText(30, 150, "SLOW DOWN", new Colour(0,0,0));
			else if(predSpd*predSpd/accel/2 < (destination-coordinate)*.25)
				ui.drawText(30, 150, "ACCELERATE", new Colour(0,0,0));
			ui.showChanges();
			
			Thread.sleep(100);
		}
	}

	private void handle(String name, String data) {
		if(name.equals("arrow"))
			handleArrow(data);
		if(name.equals("letter"))
			handleLetter(data);
	}

	private void handleLetter(String data) {
		switch(data){
		case "r":{
			setup();
			destination = getDest();
			return;
		}
		}
	}

	private void setup() {
		coordinate = 0;
		destination = getDest();
		time = 0;
		speed = 0;
	}

	private void handleArrow(String data) {
		switch(data){
		case "L":{
			if(speed > 0)
				speed -= accel;
			return;
		}
		case "R":{
			speed += accel;
			return;
		}
		}
	}

	private String getTime(int sec) {
		if(sec < 60)
			return sec + "s";
		int min = (int) (sec/60);
		sec = sec%60;
		if(min < 60)
			return min + "m " + sec + "s";
		int hr = min/60;
		min = min%60;
		if(hr < 24)
			return hr + "h " + min + "m " + sec + "s";
		int day = hr/24;
		min = day%24;
		return day + "d " + hr + "h " + min + "m " + sec + "s";
	}
	
	private double getDest(){
		double rand = Math.random()*15 + 1;
		return rand*rand*rand*rand*10;
	}

	public static void main(String[] args) throws InterruptedException {
		new ShipTest().start();
	}
}
