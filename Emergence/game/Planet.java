package game;

import ui.Colour;

public class Planet {
	private double radius;
	

	private Colour col;
	
	Planet(Colour col, double radius){
		this.col = col;
		this.radius = radius;
	}
	
	//GET SET
	
	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public Colour getCol() {
		return col;
	}

	public void setCol(Colour col) {
		this.col = col;
	}
	
	public String printableMap(){
		String map = "";
		for(int i = 1; i < radius; i++){
			int exes = (int)Math.sqrt(i*radius*5);
			map += surfaceLine(30-exes, exes);
			map += "\n";
		}
		return map;
	}
	
	private String surfaceLine(int spaces, int exes){
		String line = "";
		for(int j = 0; j < spaces; j++){line += " ";}
		for(int j = 0; j < exes; j++){line += "x";}
		return line;
	}
}
