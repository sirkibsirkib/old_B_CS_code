package assignment6;

public class AverageLinkage implements ClusterMethod{
	private DistanceMeasure d;
	
	AverageLinkage(DistanceMeasure d){
		this.d = d;
	}
	
	public double CalculateDistance(Cluster a, Cluster b) {
		double sumDistance = 0;
		for(int i = 0; i < a.numberOfUnits(); i++){
			for(int j = i; j < b.numberOfUnits(); j++){
				sumDistance += d.calculateDistance(a.getUnit(i), b.getUnit(j));
			}
		}
		return sumDistance / (a.numberOfUnits()*b.numberOfUnits());
	}
	
	public String getName(){
		return "AverageLinkage";
	}
}
