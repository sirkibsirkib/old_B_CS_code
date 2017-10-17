package assignmentFinalFinal;

public class Dataset {
	private UnitRow units;
	private StringRow variableNames;
	private String dataName;
	private int numberOfClustersWanted, numberOfUnits, numberOfVariables;
	
	public Dataset(int numberOfClustersWanted, int numberOfUnits, int numberOfVariables, String dataName, StringRow variableNames, UnitRow units){
		this.numberOfClustersWanted = numberOfClustersWanted;
		this.numberOfUnits = numberOfUnits;
		this.numberOfVariables = numberOfVariables;
		this.variableNames = variableNames;
		this.dataName = dataName;
		this.units = units;
	}
	
	//NORMALIZE
	
	public void normalizeVariables(){
		for(int i = 0; i < numberOfVariables; i++){
			normalizeVariable(i);
		}
	}
	
	private double maxValueOfVariable(int column){
		double result = -Double.MAX_VALUE;
		for(int i = 0; i < numberOfUnits; i++){
			
			if(units.getValueAt(i, column) > result){
				result = units.getValueAt(i, column);
			}
		}
		return result;
	}
	
	private double minValueOfVariable(int column){
		double result = Double.MAX_VALUE;
		for(int i = 0; i < numberOfUnits; i++){
			if(units.getValueAt(i, column) < result){
				result = units.getValueAt(i, column);
			}
		}
		return result;
	}
	
	private void normalizeVariable(int column){
		double min = minValueOfVariable(column),
			max = maxValueOfVariable(column);
		for(int i = 0; i < numberOfUnits; i++){
			
			double notNormalized = units.getValueAt(i, column);
			units.setValueAt(i, column, normalizeNumber(notNormalized, min, max));
		}
	}
	
	private double normalizeNumber(double value, double min, double max){
		if(max-min == 0.0){//avoids division by zero
			return 0.0;
		}
		double result = (value-min)/(max-min);
		return result;
	}
	
	//PRESELECT
	
	public void preselectVariables(int maxVariablesPreselected){
		NumberRow standardDeviations = createStandardDeviationArray();
		
		while(numberOfVariables > maxVariablesPreselected){
			int smallestDeviation = standardDeviations.getIndexOfMinimum();
			
			standardDeviations.remove(smallestDeviation);
			variableNames.remove(smallestDeviation);
			units.removeVariableFromAllUnits(smallestDeviation);
			numberOfVariables--;
			}
	}
	
	private NumberRow createStandardDeviationArray(){
		NumberRow standardDeviations = new NumberRow(numberOfVariables);
		for(int i = 0 ; i < numberOfVariables; i++){
			standardDeviations.add(standardDeviationOfVariable(i));
		}
		return standardDeviations;
	}
	
	private double standardDeviationOfVariable(int variableIndex){
		double[] values = new double[numberOfUnits];
		for(int i = 0; i < numberOfUnits; i++){
			values[i] = units.getValueAt(i, variableIndex);
		}
		double mean = mean(values);
		
		for(int i = 0; i < numberOfUnits; i++){
			values[i] -= mean;
			values[i] *= values[i];
		}
		double sum = 0;
		for(int i = 0; i < numberOfUnits; i++){
			sum += values[i];
		}
		double stdDevSquared = sum/(numberOfUnits-1);
		return Math.sqrt(stdDevSquared);
	}
	
	private double mean(double[] values){
		double result = 0.0;
		for(int i = 0; i < values.length; i++){
			result += values[i];
		}
		return result/values.length;
	}
	
	//GETS
	
	public Unit getUnit(int index){
		return units.getUnit(index);
	}
	
	public int getNumberOfUnits(){
		return numberOfUnits;
	}
	
	public String getVariableName(int index){
		return variableNames.getString(index);
	}
	
	public int getNumberOfVariables(){
		return numberOfVariables;
	}
	
	public int getNumberOfClustersWanted(){
		return numberOfClustersWanted;
	}
	
	public String getDataName(){
		return dataName;
	}
}
