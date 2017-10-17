package assignment6;

public class Clusterer {
	private Dataset ds;
	private ClusterRow cr;
	
	Clusterer(Dataset ds, ClusterRow cr){
		this.ds = ds;
		this.cr = cr;
	}
	
	public void agglutinateOneStep(ClusterMethod cm){
		int branchA = 0,
			branchB = 1,
			clusterPool = cr.getNumberOfClusters();
		double distance = Double.MAX_VALUE;
		
		for(int i = 0; i < clusterPool; i++){
			for(int j = i+1; j < clusterPool; j++){
				double tempDistance = cm.CalculateDistance(cr.getClusterAt(i), cr.getClusterAt(j));
				if(tempDistance < distance){
					distance = tempDistance;
					branchA = i;
					branchB = j;
				}
			}
		}
		cr.merge(branchA, branchB);
	}
}
