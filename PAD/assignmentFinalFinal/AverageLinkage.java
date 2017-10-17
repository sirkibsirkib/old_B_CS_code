package assignmentFinalFinal;

public class AverageLinkage implements ClusterMethod{
	private DistanceMeasure dm;
	
	public AverageLinkage(DistanceMeasure dm){
		this.dm = dm;
	}
	
	public double CalculateDistance(Cluster a, Cluster b) {
		double sumDistance = 0;
		int aUnits = a.getNumberOfUnits(),
			bUnits = b.getNumberOfUnits();
		for(int i = 0; i < aUnits; i++){
			for(int j = 0; j < bUnits; j++){
				sumDistance += dm.calculateDistance(a.getUnit(i), b.getUnit(j));
			}
		}
		return sumDistance / (aUnits*bUnits);
	}
}
