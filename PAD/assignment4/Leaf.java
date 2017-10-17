package assignment4;

public class Leaf implements Cluster{
	private Unit u;
	private int maxNumberOfUnits;
	
	Leaf(Unit u, int maxNumberOfUnits){
		this.maxNumberOfUnits = maxNumberOfUnits;
		this.u = u;
	}
	
	
	//GETS
	public double getMaxValueOfVariable(){
		return u.getMaxValueOfVariable();
	}
	
	public double valueOfVariableForUnit(int unitIndex, int variableIndex) {
		return u.getValueAt(variableIndex);
	}

	public int numberOfUnits() {
		return 1;
	}
	
	public Unit getUnit(int index){
		return u;
	}
	
	public int getNumberOfVariables(){
		return u.getNumberOfVariables();
	}
	
	public boolean hasChildren(){
		return false;
	}
	
	public UnitRow getUnits(){
		UnitRow ur = new UnitRow(maxNumberOfUnits);
		ur.add(u);
		return ur;
	}
}
