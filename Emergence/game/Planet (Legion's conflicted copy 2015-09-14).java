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
}
