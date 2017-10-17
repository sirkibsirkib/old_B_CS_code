package assignment1;

public class UnitRow {
	Unit[] row;
	int numberOfElements;
	
	UnitRow(int numberOfUnits){
		row = new Unit[numberOfUnits];
		numberOfElements = 0;
	}
	
	void addUnit(Unit u){
		row[numberOfElements] = u;
		numberOfElements++;
	}
}
