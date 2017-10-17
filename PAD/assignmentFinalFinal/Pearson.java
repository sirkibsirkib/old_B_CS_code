package assignmentFinalFinal;

public class Pearson implements DistanceMeasure{
	public double calculateDistance(Unit a, Unit b) {
		double sum = 0;
		double deviationA = standardDeviationOfUnitVars(a),
			deviationB = standardDeviationOfUnitVars(b);
		
		for(int i = 0; i < a.getNumberOfVariables(); i++){
			sum += ((a.getValueAt(i) - a.getAverageOfVariables()) / deviationA) *
					((b.getValueAt(i) - b.getAverageOfVariables()) / deviationB);
		}
		
		return 1-(sum /(a.getNumberOfVariables()-1));
	}
	
	private double standardDeviationOfUnitVars(Unit u){
		double[] values = new double[u.getNumberOfVariables()];
		for(int i = 0; i < u.getNumberOfVariables(); i++){
			values[i] = u.getValueAt(i);
		}
		double mean = mean(values);
		
		for(int i = 0; i < u.getNumberOfVariables(); i++){
			values[i] -= mean;
			values[i] *= values[i];
		}
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
}
