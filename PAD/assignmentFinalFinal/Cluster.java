package assignmentFinalFinal;

public interface Cluster {
	public boolean hasChildren();
	
	public int getDepth();
	public int getWidth();
	
	public Unit getUnit(int index);
	public int getNumberOfUnits();
	public UnitRow getUnits();
	
	public int getNumberOfVariables();
	public double getMinOfVariable(int variableIndex);
	public double getMaxOfVariable(int variableIndex);
	
	public Cluster getCopy();
}
