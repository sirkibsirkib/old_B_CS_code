package assignment5;

public class Node implements Cluster{
	private Cluster left, right;
	
	Node(Cluster left, Cluster right){
		this.left = left;
		this.right = right;
	}

	public boolean hasChildren() {
		return true;
	}

	public int numberOfUnits() {
		return left.numberOfUnits() + right.numberOfUnits();
	}

	public Unit getUnit(int index) {
		int numLeft = left.numberOfUnits();
		if(index <= numLeft){
			return left.getUnit(index);
		}
		return right.getUnit(index - numLeft);
	}
	
	public int getWidth(){
		return left.getWidth() + right.getWidth();
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
		for(int i = 0; i < right.numberOfUnits(); i++){
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
}
