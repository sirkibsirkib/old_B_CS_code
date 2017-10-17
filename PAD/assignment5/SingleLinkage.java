package assignment5;

public class SingleLinkage implements ClusterMethod{
	private DistanceMeasure d;
	
	SingleLinkage(DistanceMeasure d){
		this.d = d;
	}
	
	public double CalculateDistance(Cluster a, Cluster b) {
		double distance = Double.MAX_VALUE;
		for(int i = 0; i < a.numberOfUnits(); i++){
			for(int j = i; j < b.numberOfUnits(); j++){
				double tempDistance = d.calculateDistance(a.getUnit(i), b.getUnit(j));
				if(tempDistance < distance){
					distance = tempDistance;
				}
			}
		}
		return distance;
	}
	
	public String getName(){
		return "SingleLinkage";
	}
}
