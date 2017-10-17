package assignment3;

public class ClusterRow {
	private Cluster[] row;
	private int numberOfElements;
	
	ClusterRow(Dataset ds){
		numberOfElements = 0;
		row = new Cluster[ds.getNumberOfUnits()];
		for(int i = 0; i < ds.getNumberOfUnits(); i++){
			add(createNode(ds.getUnit(i), ds.getNumberOfUnits()));
		}
	}
	
	private void add(Node c){
		row[numberOfElements] = c;
		numberOfElements++;
	}
	
	private Node createNode(Unit u, int maxNumberOfLeaves){
		Node no = new Node(u, maxNumberOfLeaves);
		return no;
	}
	
	//GETS
	public double getMaxValueOfCluster(int clusterIndex){
		return row[clusterIndex].getMaxValueOfVariable();
	}
	
	public int getNumberOfClusters(){
		return numberOfElements;
	}
}
