package assignmentFinalFinal;

public class StringRow {
	private String[] row;
	private int numberOfElements;
	
	public StringRow(int maxNumberOfElements){
		row = new String[maxNumberOfElements];
		numberOfElements = 0;
	}
	
	public void add(String s){
		row[numberOfElements] = s;
		numberOfElements++;
	}
	
	public void remove(int column){
		for(int i = column; i < numberOfElements-1; i++){
			row[i] = row[i+1];
		}
		numberOfElements--;
	}
	
	//GETS
	
	public String getString(int index){
		return row[index];
	}
	
	public int getNumberOfElements(){
		return numberOfElements;
	}
}
