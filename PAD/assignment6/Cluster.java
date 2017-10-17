package assignment6;

public interface Cluster {
	public int getDepth();
	public int getWidth();
	public int getLeftWidth();
	public int getRightWidth();
	public Cluster getLeftChild();
	public Cluster getRightChild();
	public UnitRow getUnits();
	public boolean hasChildren();
	public int numberOfUnits();
	public Unit getUnit(int index);
	public int getNumberOfVariables();
	public double getMinOfVariable(int variableIndex);
	public double getMaxOfVariable(int variableIndex);
}
