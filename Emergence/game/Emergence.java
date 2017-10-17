package game;

import ui.Colour;

public class Emergence {
Colour red = new Colour (255,0,0);
	
	private void start() {
		Planet mars = new Planet(red, 10);
		System.out.println(mars.printableMap());
	}
	
	public static void main(String[] args) {
		new Emergence().start();
	}
}
