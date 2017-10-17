package assignment4;

public class Pearson implements DistanceMeasure{
	public double calculateDistance(Unit a, Unit b) {
		double sum = 0;
		double deviationA = standardDeviationOfUnitVars(a),
			deviationB = standardDeviationOfUnitVars(b);
		
		//sum += [(a[i] - a*)/stdDevA]   *   [(b[i] - b*)/stdDevB]
		for(int i = 0; i < a.getNumberOfVariables(); i++){
			sum += ((a.getValueAt(i) - a.getAverageOfVariables()) / deviationA) *
					((b.getValueAt(i) - b.getAverageOfVariables()) / deviationB);
		}
		
		//return sum /(n-1))
		return 1-(sum /(a.getNumberOfVariables()-1));
	}
	
	private double standardDeviationOfUnitVars(Unit u){
		double[] values = new double[u.getNumberOfVariables()];
		for(int i = 0; i < u.getNumberOfVariables(); i++){
			values[i] = u.getValueAt(i);
		}
		double mean = mean(values);
		//values contains the unaltered values of variables
		
		for(int i = 0; i < u.getNumberOfVariables(); i++){
			values[i] -= mean;
			values[i] *= values[i];
		}
		//values now contains (n-mean)^2
		double sum = 0;
		for(int i = 0; i < u.getNumberOfVariables(); i++){
			sum += values[i];
		}
		double stdDevSquared = sum/(u.getNumberOfVariables()-1);
		
		return Math.sqrt(stdDevSquared);
	}
	
	private double mean(double[] values){
		double result = 0.0;
		for(int i = 0; i < values.length; i++){
			result += values[i];
		}
		return result/values.length;
	}
	
	public String getName(){
		return "Pearson";
	}
}
