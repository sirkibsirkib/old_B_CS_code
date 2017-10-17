package programNode;

public class PointVector {
	String fromName,
		toName;
	double weight;
	
	PointVector(String fromName, String toName, double weight){
		this.fromName = fromName;
		this.toName = toName;
		this.weight = weight;
	}
	
	public String getFromName(){
		return fromName;
	}
	
	public String getToName(){
		return toName;
	}
	
	public double getWeight(){
		return weight;
	}
}
