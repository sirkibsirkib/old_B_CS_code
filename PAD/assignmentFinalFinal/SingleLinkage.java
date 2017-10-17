package assignmentFinalFinal;

public class SingleLinkage implements ClusterMethod{
	private DistanceMeasure dm;
	
	public SingleLinkage(DistanceMeasure dm){
		this.dm = dm;
	}
	
	public double CalculateDistance(Cluster a, Cluster b) {
		double distance = Double.MAX_VALUE;
		int aUnits = a.getNumberOfUnits(),
			bUnits = b.getNumberOfUnits();
		for(int i = 0; i < aUnits; i++){
			for(int j = 0; j < bUnits; j++){
				double tempDistance = dm.calculateDistance(a.getUnit(i), b.getUnit(j));
				if(tempDistance < distance){
					distance = tempDistance;
				}
			}
		}
		return distance;
	}
}
