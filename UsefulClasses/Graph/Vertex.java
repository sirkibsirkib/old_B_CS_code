package Graph;

public class Vertex {
	String name;
	Vertex previousStep, nextStep;
	int cost;
	
	Vertex(){
		
	}
	
	Vertex(String name){
		this.name = name;
	}
}
