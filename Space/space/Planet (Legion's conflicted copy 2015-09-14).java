package space;

public class Planet {
	private String name;
	private double size;
	
	Planet(String name, double size){
		this.name = name;
		this.size = size;
	}
	
	public void makeBigger(){
		size++;
	}
	
	public double getSize(){
		return size;
	}
}
