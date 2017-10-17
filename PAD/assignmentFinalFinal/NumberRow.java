package assignmentFinalFinal;

public class NumberRow {
	private double[] row;
	private int numberOfElements;
	
	public NumberRow(int maxNumberOfElements){
		row = new double[maxNumberOfElements];
		numberOfElements = 0;
	}
	
	public void add(double number){
		row[numberOfElements] = number;
		numberOfElements++;
	}
	
	public void remove(int index){
		for(int i = index; i < numberOfElements-1; i++){
			row[i] = row[i+1];
		}
		numberOfElements--;
	}

	//GETS
	
	public int getIndexOfMinimum(){
		int result = 0;
		double min = row[0];
		for(int i = 0; i < numberOfElements; i++){
			if(row[i] < min){
				min = row[i];
				result = i;
			}
		}
		return result;
	}
	
	public double getAverageOfElements(){
		double sum = 0;
		for(int i = 0 ; i < numberOfElements; i++){
			sum += row[i];
		}
		return sum/numberOfElements;
	}
	
	public int getNumberOfElements(){
		return numberOfElements;
	}
	
	public double getMaxValue(){
		double max = -Double.MAX_VALUE;
		for(int i = 0; i < numberOfElements; i++){
			if(row[i] > max){
				max = row[i];
			}
		}
		return max;
	}
	
	public double getValueAt(int index){
		return row[index];
	}
	
	//SETS
	
	public void setValueAt(int index, double value){
		row[index] = value;
	}
}
