package assignmentFinalFinal;

public class Clusterer {
	private int numberOfElements;
	private ClusterRow[] row;
	private ClusterMethod cm;
	
	public Clusterer(ClusterRow initialCr, ClusterMethod cm){
		row = new ClusterRow[initialCr.getWidth()];
		this.cm = cm;
		row[0] = initialCr;
		numberOfElements = 1;
	}
	
	public ClusterRow getClusterRow(int index){
		while(index > numberOfElements-1){
			generateNextClusterRow();
		}
		return row[index];
	}
	
	public int getNumberOfClusterRows(){
		return numberOfElements;
	}
	
	private void generateNextClusterRow(){
		row[numberOfElements] = row[numberOfElements-1].getCopy();
		agglutinateOneStep(row[numberOfElements], cm);
		numberOfElements++;
	}
	
	public void agglutinateOneStep(ClusterRow cr, ClusterMethod cm){
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