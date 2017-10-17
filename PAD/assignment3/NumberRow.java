package assignment3;

public class NumberRow {
	private double[] row;
	private int numberOfElements;
	
	NumberRow(int maxNumberOfElements){
		row = new double[maxNumberOfElements];
		numberOfElements = 0;
	}
	
	//ALTERATIONS
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
	
	public int indexOfMinimum(){
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
	
	//GETS
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
