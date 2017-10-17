package assignment6;

import ui.Colour;
import ui.DrawUserInterface;
import ui.UserInterfaceFactory;

public class Dendrogram implements View {
	private DrawUserInterface drawer;
	private ClusterRow cr;
	private String dataName;
	private int totalDepth, totalWidth;
	private double xSliceSize, ySliceSize;

	Dendrogram(ClusterRow cr, String dataName){
		this.cr = cr;
		this.dataName = dataName;
		drawer = UserInterfaceFactory.getDrawUI(X_WIDTH, Y_WIDTH);
	}
	
	public void draw() {
		drawer.drawText(X_WIDTH/2, Y_WIDTH-(Y_BORDER/2), "Dendrogram of " + dataName, BLACK);
		
		int numberOfClusters = cr.getNumberOfClusters();
		totalDepth = cr.getDepth();
		totalWidth = cr.getWidth();
		ySliceSize = 1.0/totalWidth;
		xSliceSize = 1.0/totalDepth;
		double yOffset = 0;
			
		for(int i = 0; i < numberOfClusters; i++){
			Cluster c = cr.getClusterAt(i);
			Colour col = generateColour(i,numberOfClusters);
			plotCluster(c, totalDepth, yOffset, col, true);
			
			yOffset += c.getWidth()*ySliceSize;
		}
		drawer.showChanges();
	}
	
	private double plotCluster(Cluster c, int fromDepth, double yOffset, Colour col, boolean isRoot){ //return yPosition
		double branchOffset = 0;
		int myDepth = c.getDepth();
		if(c.hasChildren()){ //is node
			branchOffset = c.getLeftWidth()*ySliceSize;
			double retLeft = plotCluster(c.getLeftChild(), myDepth, yOffset, col, false);
			double retRight = plotCluster(c.getRightChild(), myDepth, yOffset + branchOffset, col, false);
			double center = (retLeft + retRight)/2;
			
			drawLimb(fromDepth, myDepth, center, col );
			drawBranch(myDepth, retLeft, retRight, col );
			if(isRoot)
				drawRootLimb(myDepth, center, col );
			drawDot(myDepth, center, col );
			return center;
		} else {
			drawLimb(fromDepth, 0, yOffset, col );
			if(isRoot)
				drawRootLimb(myDepth, yOffset, col );
			drawDot(0, yOffset, col );
			drawNameOfUnit(c.getUnit(0), yOffset
					);
			return yOffset;
		}
	}
	
	private void drawNameOfUnit(Unit u, double yOffset){
		drawer.drawText(coordinateX(1)+9, coordinateY(yOffset)-5 , u.getName(), BLACK);
	}
	
	private void drawLimb(int depthFrom, int depthTo, double yOffset, Colour col){
		drawer.drawLine(coordinateX(1-(depthFrom/(1.0*totalDepth+1))), coordinateY(yOffset)-1,
				coordinateX(1-(depthTo/(1.0*totalDepth+1))), coordinateY(yOffset)-1, BLACK);
		drawer.drawLine(coordinateX(1-(depthFrom/(1.0*totalDepth+1))), coordinateY(yOffset),
				coordinateX(1-(depthTo/(1.0*totalDepth+1))), coordinateY(yOffset), col);
	}
	
	private void drawRootLimb(int depthFrom, double yOffset, Colour col){
		drawer.drawLine(coordinateX(0), coordinateY(yOffset)-1,
				coordinateX(1-(depthFrom/(1.0*totalDepth+1))), coordinateY(yOffset)-1, BLACK
				);
		drawer.drawLine(coordinateX(0), coordinateY(yOffset),
				coordinateX(1-(depthFrom/(1.0*totalDepth+1))), coordinateY(yOffset), col);
	}
	
	private void drawBranch(int depth, double yTop, double yBottom, Colour col){
		drawer.drawLine(coordinateX(1-(depth/(1.0*totalDepth+1)))-1, coordinateY(yTop),
				coordinateX(1-(depth/(1.0*totalDepth+1)))-1, coordinateY(yBottom), BLACK);
		drawer.drawLine(coordinateX(1-(depth/(1.0*totalDepth+1))), coordinateY(yTop),
				coordinateX(1-(depth/(1.0*totalDepth+1))), coordinateY(yBottom), col);
	}

	private void drawDot(int depth, double yOffset, Colour col){
		drawer.drawCircle(coordinateX(1-(depth/(1.0*totalDepth+1))), coordinateY(yOffset), 10, 10, BLACK, true);
		drawer.drawCircle(coordinateX(1-(depth/(1.0*totalDepth+1))), coordinateY(yOffset), 8, 8, col, true);
	}
	
	private int coordinateX(double x){
		return X_BORDER + (int)(x*(X_WIDTH-(3
				*X_BORDER)));
	}
	
	private int coordinateY(double y){
		return Y_BORDER + (int)(y*(Y_WIDTH-(2*Y_BORDER)));
	}
	
	public void clear() {
		drawer.clear();
	}
	
	private Colour generateColour(int index, int max){
		Colour c = new Colour(255*(index%5)/4,
				255*((index+1)%3)/2,
				255*(index%4)/3);
		return c;
	}

	public DrawUserInterface getInputSource() {
		return drawer;
	}

}
