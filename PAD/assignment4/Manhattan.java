package assignment4;

public class Manhattan implements DistanceMeasure{
	public double calculateDistance(Unit a, Unit b) {
		double d = 0;
		for(int i = 0; i < a.getNumberOfVariables(); i++){
			d += Math.abs(a.getValueAt(i) - b.getValueAt(i));
		}
		return d;
	}
	
	public String getName(){
		return "Manhattan";
	}
}
