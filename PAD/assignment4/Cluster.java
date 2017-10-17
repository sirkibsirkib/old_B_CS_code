package assignment4;

public interface Cluster {
//	/**
//	 * Returns the depth of the cluster.
//	 * @return the depth of the cluster.
//	 */
//	public int getDepth();
//	
//	/**
//	 * Returns the width of the cluster.
//	 * @return the width of the cluster.
//	 */
//	public int getWidth();
//	
	/**
	 * Returns all units contained in this cluster.
	 * @return all units contained in this cluster.
	 */
	public UnitRow getUnits();
	
	/**
	 * Returns whether this cluster has children. 
	 * I.e. is this a node or a leaf?
	 * @return 	true: if this cluster has children. <br>
	 * 			false: if this cluster has no children.
	 */
	public boolean hasChildren();
	
	public double getMaxValueOfVariable();
	public double valueOfVariableForUnit(int unitIndex, int variableIndex);
	public int numberOfUnits();
	public Unit getUnit(int index);
	public int getNumberOfVariables();
}
