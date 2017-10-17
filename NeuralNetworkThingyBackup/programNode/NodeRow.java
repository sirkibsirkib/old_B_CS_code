package programNode;

public class NodeRow {
	private Node[] row;
	private int numberOfElements;
	
	public NodeRow(){
		numberOfElements = 0;
		row = new Node[4];
	}
	
	public void add(Node n){
		if(numberOfElements >= row.length){
			expandArray();
		}
		row[numberOfElements] = n;
		numberOfElements++;
	}
	
	public void remove(int index){
		for(int i = 0; i < numberOfElements; i++){
			row[i].removePointsTo(row[index].getName());
		}
		if(row.length > 4 && numberOfElements <= row.length/2){
			collapseArray();
		}
		shuffleArrayInto(index);
		numberOfElements--;
	}
	
	public int getNumberOfPoints(){
		int total = 0;
		for(int i = 0; i < numberOfElements; i++){
			total += row[i].getNumberOfPoints();
		}
		return total;
	}
	
	public void replace(Node n, int index){
		row[index] = n;
	}
	
	public int removeNodesWithNameReturnNumber(String name){
		int removed = 0;
		for(int i = 0; i < numberOfElements; i++){
			if(findNodeIndexWithName(name) < Integer.MAX_VALUE){
				remove(findNodeIndexWithName(name));
				removed++;
			}
		}
		return removed;
	}
	
	public NodeRow duplicate(){
		NodeRow nodeRow2 = new NodeRow();
		for(int i = 0; i < numberOfElements; i++){	//create unconnected points
			nodeRow2.add(row[i].duplicate());
			//they are blank
		}
		for(int i = 0; i < numberOfElements; i++){
			Node next = nodeRow2.getNode(i);
			PointVector[] vecs = row[i].getPointVectors();
			for(int j = 0; j < vecs.length; j++){
				next.pointTo(nodeRow2.findNodeWithName(vecs[j].getToName()), vecs[j].getWeight());
			}
			
			if(row[i] instanceof Stimnode){
				Stimnode nextStim = (Stimnode)next;
				Stimnode source = (Stimnode)row[i];
				SourceVector vec = source.getSourceVector();
				nextStim.giveSource(vec.getSourceX(),vec.getSourceY(), vec.getPositive());
			}
		}
		return nodeRow2;
	}
	
	//PRIVATE
	
	private Node findNodeWithName(String queryName){
		for(int i = 0; i < numberOfElements; i++){
			if(row[i].getName().equals(queryName)){
				return row[i];
			}
		}
		return null;
	}
	
	public boolean hasNodeWithname(String queryName){
		for(int i = 0; i < numberOfElements; i++){
			if(row[i].getName().equals(queryName)){
				return true;
			}
		}
		return false;
	}
	
	private int findNodeIndexWithName(String queryName){
		for(int i = 0; i < numberOfElements; i++){
			if(row[i].getName().equals(queryName)){
				return i;
			}
		}
		return Integer.MAX_VALUE;
	}
	
	private void expandArray(){
		Node[] tempRow = new Node[row.length*2];
		row = partialTranscription(row, tempRow, numberOfElements);		
	}
	
	private void collapseArray(){
		Node[] tempRow = new Node[row.length/2];
		row = partialTranscription(row, tempRow, numberOfElements);		
	}
	
	private Node[] partialTranscription(Node[] from, Node[] to, int elementsTranscribed){
		for(int i = 0; i < elementsTranscribed; i++){
			to[i] = from[i];
		}
		return to;
	}
	
	private void shuffleArrayInto(int index){
		for(int i = index; i < numberOfElements-1; i++){
			row[i] = row[i+1];
		}
	}
	
	//GETS
	
	public Node getNode(int index){
		return row[index];
	}
	
	public int getNumberOfNodes(){
		return numberOfElements;
	}
	
	public String getNodeName(int index){
		return row[index].getName();
	}
	
	public double getXof(int index){
		return row[index].getX();
	}
	
	public double getYof(int index){
		return row[index].getY();
	}
}
