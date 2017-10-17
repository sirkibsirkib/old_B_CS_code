package game;

import ui.Colour;

public class EmergenceGame {
	Colour red = new Colour (255,0,0);
	
	private void start() {
		Planet mars = new Planet(red, 5);
	}

	public static void main(String[] args) {
		new EmergenceGame().start();
	}
}
