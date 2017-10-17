package assignmentFinalFinal;

public class Node implements Cluster{
	private Cluster left, right;
	
	public Node(Cluster left, Cluster right){
		this.left = left;
		this.right = right;
	}

	public boolean hasChildren() {
		return true;
	}
	
	//GETS

	public int getNumberOfUnits() {
		return left.getNumberOfUnits() + right.getNumberOfUnits();
	}

	public Unit getUnit(int index) {
		int numLeft = left.getNumberOfUnits();
		if(index < numLeft){
			return left.getUnit(index);
		}
		return right.getUnit(index - numLeft);
	}
	
	public int getWidth(){
		return left.getWidth() + right.getWidth();
	}
	
	public int getLeftWidth(){
		return left.getWidth();
	}
	
	public int getRightWidth(){
		return right.getWidth();
	}
	
	public Cluster getLeftChild(){
		return left;
	}
	
	public Cluster getRightChild(){
		return right;
	}
	
	public int getDepth(){
		int leftDepth = left.getDepth(),
			rightDepth = right.getDepth();
		if(leftDepth > rightDepth){
			return leftDepth + 1;
		}
		return rightDepth + 1;
	}
	
	public UnitRow getUnits() {
		UnitRow ur = left.getUnits();
		for(int i = 0; i < right.getNumberOfUnits(); i++){
			ur.add(right.getUnit(i));
		}
		return ur;
	}
	
	public int getNumberOfVariables() {
		return left.getNumberOfVariables();
	}
	
	public double getMinOfVariable(int variableIndex){
		if(left.getMinOfVariable(variableIndex) < right.getMinOfVariable(variableIndex)){
			return left.getMinOfVariable(variableIndex);
		}
		return right.getMinOfVariable(variableIndex);
	}
	
	public double getMaxOfVariable(int variableIndex){
		if(left.getMaxOfVariable(variableIndex) > right.getMaxOfVariable(variableIndex)){
			return left.getMaxOfVariable(variableIndex);
		}
		return right.getMaxOfVariable(variableIndex);
	}
	
	public Cluster getCopy(){
		Cluster c = new Node(left.getCopy(), right.getCopy());
		return c;
	}
}
