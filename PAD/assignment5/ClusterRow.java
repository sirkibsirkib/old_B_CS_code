package assignment5;

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
	
	//GETS	
	public int getNumberOfClusters(){
		return numberOfElements;
	}
	
	public void merge(int indexA, int indexB){
		Node joint = new Node(getClusterAt(indexA), getClusterAt(indexB));
		row[indexA] = joint;
		removeClusterAt(indexB);
	}
	
	public Cluster getClusterAt(int index){
		return row[index];
	}
	
	private void removeClusterAt(int index){
		for(int i = index; i < numberOfElements-1; i++){
			row[i] = row[i+1];
		}
		numberOfElements--;
	}
	
	public int width(){
		int width = 0;
		for(int i = 0; i < numberOfElements; i++){
			width += row[i].getWidth();
		}
		return width;
	}
	
	public int widthOfClusterAt(int index){
		return row[index].getWidth();
	}
	
	public int depth(){
		int depth = 0;
		for(int i = 0; i < numberOfElements; i++){
			int tempDepth = row[i].getDepth();
			if(tempDepth > depth){
				depth = tempDepth;
			}
		}
		return depth;
	}
}
