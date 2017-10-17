package assignment5;

public interface Cluster {
	public int getDepth();
	public int getWidth();
	public UnitRow getUnits();
	public boolean hasChildren();
	public int numberOfUnits();
	public Unit getUnit(int index);
	public int getNumberOfVariables();
	public double getMinOfVariable(int variableIndex);
	public double getMaxOfVariable(int variableIndex);
}
