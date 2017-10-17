package assignmentFinalFinal;

public class CompleteLinkage implements ClusterMethod{
	private DistanceMeasure dm;
	
	public CompleteLinkage(DistanceMeasure dm){
		this.dm = dm;
	}
	
	public double CalculateDistance(Cluster a, Cluster b) {
		double distance = 0;
		int aUnits = a.getNumberOfUnits(),
			bUnits = b.getNumberOfUnits();
		for(int i = 0; i < aUnits; i++){
			for(int j = 0; j < bUnits; j++){
				double tempDistance = dm.calculateDistance(a.getUnit(i), b.getUnit(j));
				if(tempDistance > distance){
					distance = tempDistance;
				}
			}
		}
		return distance;
	}
}
