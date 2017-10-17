package assignmentFinalFinal;

import ui.Colour;

public interface View {
	static final Colour BLACK = new Colour(0,0,0);
	static final int DOT_RADIUS = 4;
	
	public void draw(ClusterRow cr);
	public void clear();
}
