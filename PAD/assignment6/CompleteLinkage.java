package assignment6;

public class CompleteLinkage implements ClusterMethod{
	private DistanceMeasure d;
	
	CompleteLinkage(DistanceMeasure d){
		this.d = d;
	}
	
	public double CalculateDistance(Cluster a, Cluster b) {
		double distance = 0;
		for(int i = 0; i < a.numberOfUnits(); i++){
			for(int j = i; j < b.numberOfUnits(); j++){
				double tempDistance = d.calculateDistance(a.getUnit(i), b.getUnit(j));
				if(tempDistance > distance){
					distance = tempDistance;
				}
			}
		}
		return distance;
	}
	
	public String getName(){
		return "CompleteLinkage";
	}
}
