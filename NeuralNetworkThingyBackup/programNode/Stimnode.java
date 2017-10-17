package programNode;

import program.DoubleRow;
import program.Runner;
import ui.Colour;

public class Stimnode implements Node{
	private NodeRow points;
	private DoubleRow weights;
	private String name;
	private double stimulation,
		fireCap,
		x,
		y;
	private Colour col;
	private int	sourceX,
		sourceY;
	private boolean fired,
		positive;
	
	public Stimnode(String name, double x, double y, Colour col){
		this.col = col;
		this.name = name;
		points = new NodeRow();
		weights = new DoubleRow();
		stimulation = 0;
		fireCap = FIRE_DOUBLE;
		this.x = x;
		this.y = y;
		fired = false;
		positive = true;
	}
	
	public void removeRandomPoint(){
		points.remove(Runner.rng(getNumberOfPoints()));
	}
	
	//2 sources --> 3 sources. 1block 2void 3path
	public void giveSource(int sourceX, int sourceY, boolean positive){
		this.sourceX = sourceX;
		this.sourceY = sourceY;
		this.positive = positive;
	}
	
	public void pointTo(Node other, double weight){
		points.add(other);
		weights.add(weight);
		other.incrimentFireCap(1);
	}
	
	public void stimulate(double stimulation){
		if(stimulation >= 1) return; //only fires once
		this.stimulation += stimulation;
		if(this.stimulation >= fireCap){
			fire();
		}
	}
	
	public void fire(){
		fired = true;
		for(int i = 0; i < points.getNumberOfNodes(); i++){
			if
			points.getNode(i).stimulate(weights.getDouble(i));
		}
	}
	
	public void resetFire(){
		fired = false;
		stimulation = 0;
	}
	
	//GETS
	
	public String getName(){
		return name;
	}
	
	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
	
	public int getNumberOfPoints(){
		return points.getNumberOfNodes();
	}
	
	public double getXofPointedAtIndex(int index){
		return points.getNode(index).getX();
	}
	
	public double getYofPointedAtIndex(int index){
		return points.getNode(index).getY();
	}
	
	public double getWeightOfPoint(int index){
		return weights.getDouble(index);
	}
	
	public int getSourceX(){
		return sourceX;
	}
	
	public int getSourceY(){
		return sourceY;
	}
	
	public boolean doesPointTo(Node to){
		for(int i = 0; i < points.getNumberOfNodes(); i++){
			if(points.getNode(i) == to){
				return true;
			}
		}
		return false;
	}
	
	public boolean didFire(){
		return fired;
	}
	
	public boolean isPositive(){
		return positive;
	}
	
	public void incrimentFireCap(int multiplicity){
		fireCap += FIRE_DOUBLE*multiplicity;
	}
	
	public void decrementFireCap(int multiplicity){
		fireCap -= FIRE_DOUBLE*multiplicity;
	}

	public Node duplicate() {
		return new Stimnode(name, x, y, col);
	}

	public PointVector[] getPointVectors() {
		int numVectors = points.getNumberOfNodes();
		PointVector[] vectors = new PointVector[numVectors];
		
		for(int i = 0; i < numVectors; i++){
			vectors[i] = new PointVector(name, points.getNodeName(i), weights.getDouble(i));
		}
		return vectors;
	}
	
	public SourceVector getSourceVector() {
		return new SourceVector(sourceX, sourceY, positive);
	}
	
	public void removePointsTo(String s){
		int thingsRemoved = points.removeNodesWithNameReturnNumber(s);
		if(thingsRemoved > 0){
			decrementFireCap(thingsRemoved);
		}
	}
	
	public boolean isRemovable(){
		return false;
	}
	
	public Colour getCol(){
		return col;
	}
}
