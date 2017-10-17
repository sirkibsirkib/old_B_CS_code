package assignmentFinalFinal;

import ui.Colour;
import ui.DrawUserInterface;

public class Cartesian implements View{
	private DrawUserInterface ui;
	private String dataName, var0, var1;
	private int graphPixelSize, borderPixelSize;
	
	public Cartesian(String dataName, String var0, String var1, DrawUserInterface ui, int graphPixelSize){
		this.ui = ui;
		this.dataName = dataName;
		this.var0 = var0;
		this.var1 = var1;
		this.graphPixelSize = graphPixelSize;
		borderPixelSize = graphPixelSize/15;
	}
	
	public void draw(ClusterRow cr){
		int numClusters = cr.getNumberOfClusters();
		drawAxes();
		for(int i = 0; i < numClusters; i++){
			Cluster c = cr.getClusterAt(i);
			Colour col = colourFromCluster(c);
			drawCluster(c, col);
		}
		ui.showChanges();
	}
	
	public void clear(){
		ui.clear();
	}
	
	public void hotspotLabel(String label){
		ui.drawText(graphPixelSize/2, borderPixelSize/2, "Unit name: " + label, BLACK);
		ui.showChanges();
	}
	
	private void drawCluster(Cluster c, Colour col){
		for(int i = 0; i < c.getNumberOfUnits(); i++){
			Unit u = c.getUnit(i);
			drawDot(u.getValueAt(0), u.getValueAt(1), col);
			createHotspot(u.getValueAt(0), u.getValueAt(1), u.getName());
		}
		if(c.hasChildren()){
			encircleCluster(c, col);
		}
	}
	
	private int rationalToPixel(double rational){
		return borderPixelSize + (int)(rational*(graphPixelSize-(2*borderPixelSize)));
	}
	
	private double pixelToRational(int pixelValue){
		return pixelValue/(2*borderPixelSize + graphPixelSize);
	}
	
	private Colour colourFromCluster(Cluster c){
		return colourFromString(c.getUnit(0).getName());
	}
	
	private Colour colourFromString(String s){
		int value1 = 1,
			value2 = 1,
			value3 = 1;
		for(int i = 0; i < s.length(); i++){
			value1 = (value1 * s.charAt(i) * 2)%245 + 1;
			value2 = (value2 * s.charAt(i) * 3)%245 + 1;
			value3 = (value3 * s.charAt(i) * 5)%245 + 1;
		}
		return new Colour(value1+9, value2+9, value3+9);
	}
	
	private void drawAxes(){
		ui.drawText(graphPixelSize/2, graphPixelSize-(borderPixelSize/2), "Cartesian Plot of " + dataName, BLACK);
		ui.drawLine(rationalToPixel(0), rationalToPixel(0), rationalToPixel(0), rationalToPixel(1), BLACK);
		ui.drawLine(rationalToPixel(0), rationalToPixel(0), rationalToPixel(1), rationalToPixel(0), BLACK);
		ui.drawText(borderPixelSize-20, borderPixelSize-20, "0", BLACK);
		ui.drawText(graphPixelSize-(borderPixelSize*2), borderPixelSize-30, var1, BLACK);
		ui.drawText(borderPixelSize-20, graphPixelSize-(borderPixelSize/2), var0, BLACK);
	}
	
	private void drawDot(double x, double y, Colour col){
		ui.drawCircle(rationalToPixel(x), rationalToPixel(y), DOT_RADIUS*2+2, DOT_RADIUS*2+2, BLACK, true);
		ui.drawCircle(rationalToPixel(x), rationalToPixel(y), DOT_RADIUS*2, DOT_RADIUS*2, col, true);
		
	}
	
	private void createHotspot(double x, double y, String unitName){
		ui.setCircleHotspot(rationalToPixel(x), rationalToPixel(y), 10, 10, unitName);
	}
	
	private void encircleCluster(Cluster c, Colour col){
		double circleX = (c.getMinOfVariable(0)+c.getMaxOfVariable(0))/2,
			circleY = (c.getMinOfVariable(1)+c.getMaxOfVariable(1))/2,
			circleRadius = .02;
		while(!clusterEncircled(circleX, circleY, circleRadius, c)){
			circleRadius += .005;
		}
		drawCircle(circleX, circleY, circleRadius, col);
	}
	
	private void drawCircle(double x, double y, double size, Colour col){
		ui.drawCircle(rationalToPixel(x), rationalToPixel(y),
				(int)(graphPixelSize*size), (int)(graphPixelSize*size), col, false);
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
		return Math.sqrt(xComponent+yComponent) < size/2 - pixelToRational(DOT_RADIUS);
	}
}
