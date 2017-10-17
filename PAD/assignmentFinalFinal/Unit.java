package assignmentFinalFinal;

public class Unit {
	private String name;
	private NumberRow numbers;
	
	public Unit(String name, NumberRow numbers){
		this.name = name;
		this.numbers = numbers;
	}
	
	public void removeVariable(int index){
		numbers.remove(index);
	}
	
	//GETS
	
	public double getAverageOfVariables(){
		return numbers.getAverageOfElements();
	}
	
	public int getNumberOfVariables(){
		return numbers.getNumberOfElements();
	}
	
	public double getMaxValueOfVariable(){
		return numbers.getMaxValue();
	}
	
	public String getName(){
		return name;
	}
	
	public double getValueAt(int index){
		return numbers.getValueAt(index);
	}

	//SETS
	
	public void setValueAt(int index, double value){
		numbers.setValueAt(index, value);
	}
}
