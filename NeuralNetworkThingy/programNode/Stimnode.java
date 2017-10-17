package programNode;

import program.DoubleRow;
import program.Runner;
import ui.Colour;

public class Stimnode implements Node{
	private NodeRow pointsOut, pointsIn;
	private DoubleRow weightsOut, weightsIn;
	private String name;
	private double stimulation,
		fireCap,
		x,
		y;
	private Colour colOn, colOff;
	private int	sourceX,
		sourceY;
	private boolean fired;
	
	public Stimnode(String name, double x, double y, Colour colOn, Colour colOff){
		this.colOn = colOn;
		this.colOff = colOff;
		this.name = name;
		pointsOut = new NodeRow();
		weightsOut = new DoubleRow();
		pointsIn = new NodeRow();
		weightsIn = new DoubleRow();
		stimulation = 0;
		fireCap = FIRE_DOUBLE;
		this.x = x;
		this.y = y;
		fired = false;
	}
	
	public void removeRandomPointOut(){
		pointsOut.remove(Runner.rng(getNumberOfPointsOut()));
	}
	
	//2 sources --> 3 sources. 1block 2void 3path
	public void giveSource(int sourceX, int sourceY){
		this.sourceX = sourceX;
		this.sourceY = sourceY;
	}
	
	public void pointTo(Node other, double weight){
		pointsOut.add(other);
		weightsOut.add(weight);
		other.youArePointedTo(this, weight);
	}
	
	public void youArePointedTo(Node nodePointing, double weight){
		pointsIn.add(nodePointing);
		weightsIn.add(weight);
		incrimentFireCap(1);
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
		for(int i = 0; i < pointsOut.getNumberOfNodes(); i++){
			double nextWeight = weightsOut.getDouble(i);
			if(nextWeight > 0)
				pointsOut.getNode(i).stimulate(nextWeight);
		}
	}
	
	public void fireNegatives(){
		fired = false;
		for(int i = 0; i < pointsOut.getNumberOfNodes(); i++){
			double nextWeight = weightsOut.getDouble(i);
			if(nextWeight < 0)
				pointsOut.getNode(i).stimulate(-nextWeight);
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
	
	public int getNumberOfPointsOut(){
		return pointsOut.getNumberOfNodes();
	}
	
	public double getXofPointedAtIndex(int index){
		return pointsOut.getNode(index).getX();
	}
	
	public double getYofPointedAtIndex(int index){
		return pointsOut.getNode(index).getY();
	}
	
	public double getWeightOfPointOut(int index){
		return weightsOut.getDouble(index);
	}
	
	public int getSourceX(){
		return sourceX;
	}
	
	public int getSourceY(){
		return sourceY;
	}
	
	public boolean doesPointTo(Node to){
		for(int i = 0; i < pointsOut.getNumberOfNodes(); i++){
			if(pointsOut.getNode(i) == to){
				return true;
			}
		}
		return false;
	}
	
	public boolean didFire(){
		return fired;
	}
	
	public void incrimentFireCap(int multiplicity){
		fireCap += FIRE_DOUBLE*multiplicity;
	}
	
	public void decrementFireCap(int multiplicity){
		fireCap -= FIRE_DOUBLE*multiplicity;
	}

	public Node duplicate() {
		return new Stimnode(name, x, y, colOn, colOff);
	}

	public PointsOutVector[] getPointsOutVectors() {
		int numVectors = pointsOut.getNumberOfNodes();
		PointsOutVector[] vectors = new PointsOutVector[numVectors];
		
		for(int i = 0; i < numVectors; i++){
			vectors[i] = new PointsOutVector(name, pointsOut.getNodeName(i), weightsOut.getDouble(i));
		}
		return vectors;
	}
	
	public SourceVector getSourceVector() {
		return new SourceVector(sourceX, sourceY);
	}
	
	public void removePointsTo(String s){
		int thingsRemoved = pointsOut.removeNodesWithNameReturnNumber(s);
		if(thingsRemoved > 0){
			decrementFireCap(thingsRemoved);
		}
	}
	
	public boolean isRemovable(){
		return false;
	}
	
	public Colour getColOn(){ //keks
		return colOn;
	}
	
	public Colour getColOff(){
		return colOff;
	}
	
	public boolean isWithin(double x, double y, double bufferDistance){
		double xDif = this.x - x;
		xDif *= xDif;
		double yDif = this.y -y;
		yDif *= yDif;
		return Math.sqrt(xDif + yDif) < bufferDistance;
	}

	public boolean isPointedToBy(Node from) {
		for(int i = 0; i < pointsIn.getNumberOfNodes(); i++){
			if(pointsIn.getNode(i) == from){
				return true;
			}
		}
		return false;
	}
	
	public double getWeightOfPointIn(int index) {
		return weightsIn.getDouble(index);
	}
	
	public int getNumberOfPointsIn() {
		return pointsIn.getNumberOfNodes();
	}
	
	public Node getPointOut(int index){
		return pointsOut.getNode(index);
	}
}
