package assignment5;

public class Euclidean implements DistanceMeasure{
	public double calculateDistance(Unit a, Unit b) {
		double dSquared = 0;
		for(int i = 0; i < a.getNumberOfVariables(); i++){
			double difference = a.getValueAt(i)-b.getValueAt(i);
			dSquared += difference*difference;
		}
		return Math.sqrt(dSquared);
	}
	
	public String getName(){
		return "Euclidean";
	}
}
