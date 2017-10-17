package assignment2;

public class UnitRow {
	private Unit[] row;
	private int numberOfElements;
	
	public UnitRow(int numberOfUnits){
		row = new Unit[numberOfUnits];
		numberOfElements = 0;
	}
	
	//ALTERATIONS
	public void add(Unit u){
		row[numberOfElements] = u;
		numberOfElements++;
	}
	
	public void remove(int index){
		for(int i = index; i < numberOfElements-1; i++){
			row[i] = row[i+1];
		}
		numberOfElements--;
	}
	
	//GETS
	public String getName(int index){
		return row[index].getName();
	}
	
	public double getValueAt(int unitIndex, int variableIndex){
		return row[unitIndex].getValueAt(variableIndex);
	}
	
	//SETS
	public void removeVariableFromAllUnits(int index){
		for(int i = 0; i < numberOfElements; i++){
			row[i].removeVariable(index);
		}
	}
	
	public void setValueAt(int unitIndex, int variableIndex, double value){
		row[unitIndex].setValueAt(variableIndex, value);
	}
}
