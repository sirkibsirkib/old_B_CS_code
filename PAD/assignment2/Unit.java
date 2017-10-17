package assignment2;

public class Unit {
	private String name;
	private NumberRow numbers;
	
	Unit(String name, NumberRow numbers){
		this.name = name;
		this.numbers = numbers;
	}
	
	//GETS
	public String getName(){
		return name;
	}
	
	public double getValueAt(int index){
		return numbers.getValueAt(index);
	}

	//SETS
	public void removeVariable(int index){
		numbers.remove(index);
	}
	
	public void setValueAt(int index, double value){
		numbers.setValueAt(index, value);
	}
}
