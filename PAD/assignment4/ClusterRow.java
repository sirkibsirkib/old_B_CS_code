package assignment4;

public class ClusterRow {
	private Cluster[] row;
	private int numberOfElements;
	
	ClusterRow(Dataset ds){
		numberOfElements = 0;
		row = new Cluster[ds.getNumberOfUnits()];
		for(int i = 0; i < ds.getNumberOfUnits(); i++){
			add(createLeaf(ds.getUnit(i), ds.getNumberOfUnits()));
		}
	}
	
	private void add(Cluster c){
		row[numberOfElements] = c;
		numberOfElements++;
	}
	
	private Leaf createLeaf(Unit u, int maxNumberOfLeaves){
		Leaf le = new Leaf(u, maxNumberOfLeaves);
		return le;
	}
	
	public void agglutinateTo(int numberOfDesiredClusters, DistanceMeasure d, ClusterMethod c){
		//TODO this is where the magic happens
	}
	
	//GETS
	public double getMaxValueOfCluster(int clusterIndex){
		return row[clusterIndex].getMaxValueOfVariable();
	}
	
	public int getNumberOfClusters(){
		return numberOfElements;
	}
	
	public Cluster getCluster(int index){
		return row[index];
	}
	
	public int getNumberOfVariables(){
		return row[0].getNumberOfVariables();
	}
}
