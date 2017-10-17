package assignment3;

public class Node implements Cluster{
	private Unit[] row;
	private int numberOfElements;
	
	Node(Unit u, int maxNumberOfUnits){
		row = new Unit[maxNumberOfUnits];
		row[0] = u;
		numberOfElements = 1;
	}
	
	public double getMaxValueOfVariable(){
		double max = -Double.MAX_VALUE;
		for(int i = 0; i < numberOfElements; i++){
			double value = row[i].getMaxValueOfVariable();
			if(value > max){
				max = value;
			}
		}
		return max;
	}
}
