package primativeRows;

public class BooleanRow {
	private boolean[] row;
	private int numberOfElements;
	
	public BooleanRow(){
		row = new boolean[4];
		numberOfElements = 0;
	}
	
	private void doubleSize(){
		boolean[] row2 = new boolean[row.length*2];
		for(int i = 0; i < numberOfElements; i++){
			row2[i] = row[i];
		}
		row = row2;
	}
	
	private void halveSize(){
		boolean[] row2 = new boolean[row.length/2];
		for(int i = 0; i < numberOfElements; i++){
			row2[i] = row[i];
		}
		row = row2;
	}
	
	public void add(boolean value){
		if(numberOfElements >= row.length)
			doubleSize();
		row[numberOfElements] = value;
		numberOfElements++;
	}
	
	public int getNumberOfElements(){
		return numberOfElements;
	}
	
	public void remove(int index){
		if(numberOfElements <= row.length/2 && row.length > 4)
			halveSize();
		for(int i = index; i < numberOfElements-1; i++){
			row[i] = row[i+1];
		}
		numberOfElements--;
	}
	
	public boolean has(boolean value){
		for(int i = 0; i < numberOfElements; i++){
			if(row[i] == value)
				return true;
		}
		return false;
	}
	
	public boolean getElement(int index){
		return row[index];
	}
}
