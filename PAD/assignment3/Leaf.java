package assignment3;

public class Leaf implements Cluster{
	private Unit u;
	
	Leaf(Unit u){
		this.u = u;
	}
	
	public double getMaxValueOfVariable(){
		return u.getMaxValueOfVariable();
	}
}
