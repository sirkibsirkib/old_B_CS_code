package programNode;

public class SourceVector {
	private int	sourceX,
		sourceY;
	private boolean positive;
	
	public SourceVector(int	sourceX, int sourceY, boolean positive){
		this.sourceX = sourceX;
		this.sourceY = sourceY;
		this.positive = positive;
	}
	
	public int getSourceX(){
		return sourceX;
	}
	
	public int getSourceY(){
		return sourceY;
	}
	
	public boolean getPositive(){
		return positive;
	}
}
