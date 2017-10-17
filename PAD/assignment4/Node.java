package assignment4;

public class Node implements Cluster{
	private Unit[] row;
	private int numberOfElements,
		maxNumberOfUnits;
	
	Node(Unit u, int maxNumberOfUnits){
		this.maxNumberOfUnits = maxNumberOfUnits;
		row = new Unit[maxNumberOfUnits];
		row[0] = u;
		numberOfElements = 1;
	}
	
	public double getMaxValueOfVariable(){
		double max = -Double.MAX_VALUE;
		for(int i = 0; i < numberOfElements; i++){
			double value = row[i].getMaxValueOfVariable();
			if(value > max){
				max = value;
			}
		}
		return max;
	}
	
	//GETS
	public double valueOfVariableForUnit(int unitIndex, int variableIndex) {
		return row[unitIndex].getValueAt(variableIndex);
	}

	public int numberOfUnits() {
		return numberOfElements;
	}
	
	public Unit getUnit(int index){
		return row[index];
	}
	
	public int getNumberOfVariables() {
		return row[0].getNumberOfVariables();
	}
	
	public boolean hasChildren(){
		return true;
	}
	
	public UnitRow getUnits(){
		UnitRow ur = new UnitRow(maxNumberOfUnits);
		for(int i = 0; i < numberOfElements; i++){
			ur.add(row[i]);
		}
		return ur;
	}
}
