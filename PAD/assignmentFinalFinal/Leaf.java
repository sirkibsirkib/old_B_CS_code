package assignmentFinalFinal;

public class Leaf implements Cluster{
	private Unit u;
	private int maxNumberOfUnits;
	
	public Leaf(Unit u, int maxNumberOfUnits){
		this.maxNumberOfUnits = maxNumberOfUnits;
		this.u = u;
	}
	
	//GETS
	
	public int getNumberOfUnits() {
		return 1;
	}
	
	public Unit getUnit(int index){
		return u;
	}
	
	public boolean hasChildren(){
		return false;
	}
	
	public UnitRow getUnits(){
		UnitRow ur = new UnitRow(maxNumberOfUnits);
		ur.add(u);
		return ur;
	}
	
	public int getWidth(){
		return 1;
	}
	
	public int getDepth(){
		return 1;
	}

	public int getNumberOfVariables() {
		return u.getNumberOfVariables();
	}
	
	public double getMinOfVariable(int variableIndex){
		return u.getValueAt(variableIndex);
	}
	
	public double getMaxOfVariable(int variableIndex){
		return u.getValueAt(variableIndex);
	}
	
	public Cluster getCopy(){
		return this;
	}
}
