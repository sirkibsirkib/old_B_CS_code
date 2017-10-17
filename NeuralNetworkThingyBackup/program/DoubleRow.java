package program;

public class DoubleRow{
	private double[] row;
	private int numberOfElements;
	
	public DoubleRow(){
		numberOfElements = 0;
		row = new double[4];
	}
	
	public void add(double n){
		if(numberOfElements >= row.length){
			expandArray();
		}
		row[numberOfElements] = n;
		numberOfElements++;
	}
	
	public void remove(int index){
		if(row.length > 4 && numberOfElements <= row.length/2){
			collapseArray();
		}
		shuffleArrayInto(index);
		numberOfElements--;
	}
	
	public void replace(double n, int index){
		row[index] = n;
	}
	
	//PRIVATE
	
	private void expandArray(){
		double[] tempRow = new double[row.length*2];
		row = partialTranscription(row, tempRow, numberOfElements);		
	}
	
	private void collapseArray(){
		double[] tempRow = new double[row.length/2];
		row = partialTranscription(row, tempRow, numberOfElements);		
	}
	
	private double[] partialTranscription(double[] from, double[] to, int elementsTranscribed){
		for(int i = 0; i < elementsTranscribed; i++){
			to[i] = from[i];
		}
		return to;
	}
	
	private void shuffleArrayInto(int index){
		for(int i = index; i < numberOfElements-1; i++){
			row[i] = row[i+1];
		}
	}
	
	//GETS
	
	public double getDouble(int index){
		return row[index];
	}
	
	public int getNumberOfElements(){
		return numberOfElements;
	}
}
