package assignment5;

import ui.Colour;
import ui.DrawUserInterface;

public interface View {
	static final int X_WIDTH = 600,
		Y_WIDTH = 600,
		X_BORDER = 50,
		Y_BORDER = 50;
	static final Colour BLACK = new Colour(0,0,0);
	
	public void draw();
	public void clear();
	public DrawUserInterface getInputSource();
}
