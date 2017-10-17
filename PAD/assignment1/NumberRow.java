package assignment1;

public class NumberRow {
	double[] row;
	int numberOfElements;
	
	NumberRow(int maxNumberOfElements){
		row = new double[maxNumberOfElements];
		numberOfElements = 0;
	}
	
	void add(double number){
		row[numberOfElements] = number;
		numberOfElements++;
	}
}
