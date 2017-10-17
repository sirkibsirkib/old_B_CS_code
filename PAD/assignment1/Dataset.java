package assignment1;

public class Dataset {
	UnitRow units;
	String dataName;
	String[] variableNames;
	
	Dataset(int numberOfUnits){
		units = new UnitRow(numberOfUnits);
	}
	
	//returns a double containing the maximum value in a column across all units.
	double maxValueInVariableColumn(int column){
		double result = 0.0;
		for(int i = 0; i < units.numberOfElements; i++){
			if(units.row[i].numbers.row[column] > result){
				result = units.row[i].numbers.row[column];
			}
		}
		return result;
	}
}
