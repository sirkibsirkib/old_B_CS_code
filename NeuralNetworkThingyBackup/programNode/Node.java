package programNode;

public interface Node {
	static final double FIRE_DOUBLE = .25;
	
	public void pointTo(Node other, double weight);
	public void incrimentFireCap(int multiplicity);
	public void decrementFireCap(int multiplicity);
	public void stimulate(double stimulation);
	public void fire();
	public void resetFire();
	public boolean doesPointTo(Node to);
	public boolean didFire();
	public void removeRandomPoint();
	
	public Node duplicate();
	public PointVector[] getPointVectors();
	public void removePointsTo(String name);
	
	//GETS

	public String getName();
	public double getX();
	public double getY();
	public double getXofPointedAtIndex(int index);
	public double getYofPointedAtIndex(int index);
	public double getWeightOfPoint(int index);
	public int getNumberOfPoints();
	public boolean isRemovable();
}
