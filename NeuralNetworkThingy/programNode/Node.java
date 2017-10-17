package programNode;

public interface Node {
	static final double FIRE_DOUBLE = .25;
	
	public void pointTo(Node other, double weight);
	public void youArePointedTo(Node nodePointing, double weight);
	public void incrimentFireCap(int multiplicity);
	public void decrementFireCap(int multiplicity);
	public void stimulate(double stimulation);
	public void fire();
	public void resetFire();
	public boolean doesPointTo(Node to);
	public boolean isPointedToBy(Node to);
	public boolean didFire();
	public void removeRandomPointOut();
	
	public Node duplicate();
	public PointsOutVector[] getPointsOutVectors();
	public void removePointsTo(String name);
	
	//GETS

	public String getName();
	public double getX();
	public double getY();
	public double getXofPointedAtIndex(int index);
	public double getYofPointedAtIndex(int index);
	public double getWeightOfPointOut(int index);
	public double getWeightOfPointIn(int index);
	public int getNumberOfPointsOut();
	public int getNumberOfPointsIn();
	public boolean isRemovable();
	public boolean isWithin(double x, double y, double bufferDistance);
	
	public Node getPointOut(int index);
	
	//public boolean isPointedToBy(Node to);
	//public int getNumberOfPointsIn();
}
