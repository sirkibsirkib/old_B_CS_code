package programNode;

public class PointsOutVector {
	String fromName,
		toName;
	double weight;
	
	PointsOutVector(String fromName, String toName, double weight){
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
