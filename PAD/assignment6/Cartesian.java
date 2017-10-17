package assignment6;

import ui.Colour;
import ui.DrawUserInterface;
import ui.UserInterfaceFactory;

public class Cartesian implements View{
	private DrawUserInterface drawer;
	private ClusterRow cr;
	private String dataName, var0, var1;
	
	Cartesian(ClusterRow cr, String dataName, String var0, String var1){
		this.cr = cr;
		this.dataName = dataName;
		this.var0 = var0;
		this.var1 = var1;
		drawer = UserInterfaceFactory.getDrawUI(X_WIDTH, Y_WIDTH);
	}
	
	public void draw(){
		int numClusters = cr.getNumberOfClusters();
		drawAxes();
		for(int i = 0; i < numClusters; i++){
			Colour c = generateColour(i, numClusters);
			drawCluster(cr.getClusterAt(i), c);
		}
		drawer.showChanges();
	}
	
	public void clear(){
		drawer.clear();
	}
	
	public DrawUserInterface getInputSource(){
		return drawer;
	}
	
	private Colour generateColour(int index, int max){
		Colour c = new Colour(255*(index%5)/5,
				255*((index+1)%3)/2,
				255*(index%4)/3);
		return c;
	}
	
	private void drawAxes(){
		drawer.drawText(X_WIDTH/2, Y_WIDTH-(Y_BORDER/2), "Cartesian Plot of " + dataName, BLACK);
		drawer.drawLine(coordinateX(0), coordinateY(0), coordinateX(0), coordinateY(1), BLACK);
		drawer.drawLine(coordinateX(0), coordinateY(0), coordinateX(1), coordinateY(0), BLACK);
		drawer.drawText(X_BORDER-20, Y_BORDER-20, "0", BLACK);
		drawer.drawText(X_WIDTH-(X_BORDER*2), Y_BORDER-20, var1, BLACK);
		drawer.drawText(X_BORDER-20, Y_WIDTH-(Y_BORDER/2), var0, BLACK);
	}
	
	private int coordinateX(double x){
		return X_BORDER + (int)(x*(X_WIDTH-(2*X_BORDER)));
	}
	
	private int coordinateY(double y){
		return Y_BORDER + (int)(y*(Y_WIDTH-(2*Y_BORDER)));
	}
	
	private void drawCluster(Cluster c, Colour col){
		UnitRow ur = c.getUnits();
		for(int i = 0; i < ur.getNumberOfUnits(); i++){
			Unit u = ur.getUnit(i);
			drawDot(u.getValueAt(0), u.getValueAt(1), col);
		}
		if(c.hasChildren()){
			encircleCluster(c, col);
		}
	}
	
	private void drawDot(double x, double y, Colour col){
		drawer.drawCircle(coordinateX(x), coordinateY(y), 10, 10, BLACK, true);
		drawer.drawCircle(coordinateX(x), coordinateY(y), 8, 8, col, true);
	}
	
	private void encircleCluster(Cluster c, Colour col){
		double circleX = (c.getMinOfVariable(0)+c.getMaxOfVariable(0))/2,
			circleY = (c.getMinOfVariable(1)+c.getMaxOfVariable(1))/2,
			circleRadius = .02;
		while(!clusterEncircled(circleX, circleY, circleRadius, c)){
			circleRadius += .01;
		}
		drawCircle(circleX, circleY, circleRadius, col);
	}
	
	private void drawCircle(double x, double y, double size, Colour col){
		drawer.drawCircle(coordinateX(x), coordinateY(y),
				(int)(X_WIDTH*size), (int)(Y_WIDTH*size), col, false);
	}
	
	private boolean clusterEncircled(double x, double y, double size, Cluster c){
		UnitRow ur = c.getUnits();
		for(int i = 0; i < ur.getNumberOfUnits(); i++){
			if(!unitWithinCircle(x, y, size, ur.getUnit(i))){
				return false;
			}
		}
		return true;
	}
	
	private boolean unitWithinCircle(double x, double y, double size, Unit u){
		double xComponent = u.getValueAt(0)-x;
		xComponent *= xComponent;
		double yComponent = u.getValueAt(1)-y;
		yComponent *= yComponent;
		return Math.sqrt(xComponent+yComponent) < size/2;
	}
}
