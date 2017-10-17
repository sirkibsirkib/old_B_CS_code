package assignmentFinalFinal;

import ui.Colour;
import ui.DrawUserInterface;

public class Dendrogram implements View {
	private DrawUserInterface ui;
	private String dataName;
	private int totalDepth, totalWidth, graphPixelSize, borderPixelSize;
	private double ySliceSize;
	private static final int MIN_RATIO_OF_LIMB_TO_BRANCH_SIZE = 3;

	public Dendrogram(String dataName, DrawUserInterface ui, int graphPixelSize){
		this.ui = ui;
		this.dataName = dataName;
		this.graphPixelSize = graphPixelSize;
		borderPixelSize = graphPixelSize/15;
	}
	
	public void draw(ClusterRow cr){
		ui.drawText(graphPixelSize/2, graphPixelSize-(borderPixelSize/2), "Dendrogram of " + dataName, BLACK);
		
		int numberOfClusters = cr.getNumberOfClusters();
		totalDepth = calculateTotalDrawDepth(cr.getDepth(), cr.getWidth());
		
		totalWidth = cr.getWidth();
		ySliceSize = 1.0/totalWidth;
		double yOffset = 0;
			
		for(int i = 0; i < numberOfClusters; i++){
			Cluster c = cr.getClusterAt(i);
			Colour col = colourFromCluster(c);
			plotCluster(c, totalDepth, yOffset, col, true);
			yOffset += c.getWidth()*ySliceSize;
		}
		ui.showChanges();
	}
	
	public void clear() {
		ui.clear();
	}
	
	public int calculateTotalDrawDepth(int crDepth, int crWidth){
		int result = crWidth/MIN_RATIO_OF_LIMB_TO_BRANCH_SIZE;
		if(result < crDepth-1){
			return crDepth-1;
		}
		return result;
	}
	
	//RECURSIVE DRAW
	
	private double plotCluster(Cluster c, int fromDepth, double yOffset, Colour col, boolean isRoot){
		if(c.hasChildren()){
			return plotNode((Node)c, fromDepth, yOffset, col, isRoot);
		}else{
			return plotLeaf((Leaf)c, fromDepth, yOffset, col, isRoot);
		}
	}
	
	private double plotLeaf(Leaf c, int fromDepth, double yOffset, Colour col, boolean isRoot){
		if(isRoot)
			drawRootLimb(0, yOffset, col );
		else
			drawLimb(fromDepth, 0, yOffset, col );
		drawDot(0, yOffset, col );
		drawNameOfUnit(c.getUnit(0), yOffset);
		return yOffset;
	}
	
	private double plotNode(Node c, int fromDepth, double yOffset, Colour col, boolean isRoot){
		int myDepth = c.getDepth()-1;
		double branchOffset = 0;
		
		branchOffset = c.getLeftWidth()*ySliceSize;
		double retLeft = plotCluster(c.getLeftChild(), myDepth, yOffset, col, false);
		double retRight = plotCluster(c.getRightChild(), myDepth, yOffset + branchOffset, col, false);
		double center = (retLeft + retRight)/2;
		
		drawBranch(myDepth, retLeft, retRight, col );
		if(isRoot)
			drawRootLimb(myDepth, center, col );
		else
			drawLimb(fromDepth, myDepth, center, col );
		drawDot(myDepth, center, col );
		return center;
	}
	
	//OTHER DRAWS
	
	private int rationalToPixelX(double x){
		return borderPixelSize + (int)(x*(graphPixelSize-(4*borderPixelSize)));
	}
	
	private int rationalToPixelY(double y){
		return borderPixelSize + (int)((1-y)*(graphPixelSize-(2*borderPixelSize)));
	}
	
	private void drawNameOfUnit(Unit u, double yOffset){
		ui.drawText(rationalToPixelX(1)+9, rationalToPixelY(yOffset)-5 , u.getName(), BLACK);
	}
	
	private void drawLimb(int depthFrom, int depthTo, double yOffset, Colour col){
		ui.drawLine(rationalToPixelX(1-(depthFrom/(1.0*totalDepth+1))), rationalToPixelY(yOffset)-1,
				rationalToPixelX(1-(depthTo/(1.0*totalDepth+1))), rationalToPixelY(yOffset)-1, BLACK);
		ui.drawLine(rationalToPixelX(1-(depthFrom/(1.0*totalDepth+1))), rationalToPixelY(yOffset),
				rationalToPixelX(1-(depthTo/(1.0*totalDepth+1))), rationalToPixelY(yOffset), col);
	}
	
	private void drawRootLimb(int depthFrom, double yOffset, Colour col){
		ui.drawLine(rationalToPixelX(0), rationalToPixelY(yOffset)-1,
				rationalToPixelX(1-(depthFrom/(1.0*totalDepth+1))), rationalToPixelY(yOffset)-1, BLACK);
		ui.drawLine(rationalToPixelX(0), rationalToPixelY(yOffset),
				rationalToPixelX(1-(depthFrom/(1.0*totalDepth+1))), rationalToPixelY(yOffset), col);
	}
	
	private void drawBranch(int depth, double yTop, double yBottom, Colour col){
		ui.drawLine(rationalToPixelX(1-(depth/(1.0*totalDepth+1)))-1, rationalToPixelY(yTop),
				rationalToPixelX(1-(depth/(1.0*totalDepth+1)))-1, rationalToPixelY(yBottom), BLACK);
		ui.drawLine(rationalToPixelX(1-(depth/(1.0*totalDepth+1))), rationalToPixelY(yTop),
				rationalToPixelX(1-(depth/(1.0*totalDepth+1))), rationalToPixelY(yBottom), col);
	}

	private void drawDot(int depth, double yOffset, Colour col){
		ui.drawCircle(rationalToPixelX(1-(depth/(1.0*totalDepth+1))), rationalToPixelY(yOffset), DOT_RADIUS*2+2, DOT_RADIUS*2+2, BLACK, true);
		ui.drawCircle(rationalToPixelX(1-(depth/(1.0*totalDepth+1))), rationalToPixelY(yOffset), DOT_RADIUS*2, DOT_RADIUS*2, col, true);
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
}
