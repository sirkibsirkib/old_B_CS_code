package programNode;

import program.DoubleRow;
import program.Runner;

public class Memory implements Node{

	private NodeRow pointsOut, pointsIn;
	private DoubleRow weightsOut, weightsIn;
	private String name;
	private double stimulation,
		fireCap,
		x,
		y;
	private boolean fired, memory;
	
	public Memory(String name, double x, double y){
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
		memory = false;
	}
	
	public void removeRandomPointOut(){
		pointsOut.remove(Runner.rng(getNumberOfPointsOut()));
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
			memory = true;
		}
	}
	
	public void fire(){
		fired = true;
		for(int i = 0; i < pointsOut.getNumberOfNodes(); i++){
			pointsOut.getNode(i).stimulate(weightsOut.getDouble(i));
		}
	}
	
	public void resetFire(){
		fired = false;
		stimulation = 0;
	}
	
	public void memoryFire(){
		memory = false;
		fire();
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
	
	public boolean remembers(){
		return memory;
	}
	
	public void incrimentFireCap(int multiplicity){
		fireCap += FIRE_DOUBLE*multiplicity;
	}
	
	public void decrementFireCap(int multiplicity){
		fireCap -= FIRE_DOUBLE*multiplicity;
	}
	
	public Node duplicate() {
		return new Memory(name, x, y);
	}
	
	public PointsOutVector[] getPointsOutVectors() {
		int numVectors = pointsOut.getNumberOfNodes();
		PointsOutVector[] vectors = new PointsOutVector[numVectors];
		
		for(int i = 0; i < numVectors; i++){
			vectors[i] = new PointsOutVector(name, pointsOut.getNodeName(i), weightsOut.getDouble(i));
		}
		return vectors;
	}
	
	public void removePointsTo(String s){
		int thingsRemoved = pointsOut.removeNodesWithNameReturnNumber(s);
		if(thingsRemoved > 0){
			decrementFireCap(thingsRemoved);
		}
	}
	
	public boolean isRemovable(){
		return 0 < y && y < 1;
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
