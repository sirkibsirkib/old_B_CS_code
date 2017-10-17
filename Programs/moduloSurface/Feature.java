package moduloSurface;

public class Feature {
	int x, y;
	String name;
	
	Feature(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	Feature(int x, int y, String name){
		this.x = x;
		this.y = y;
		this.name = name;
	}
	
	boolean isAt(int testX, int testY){
		if(testX == x && testY == y){
			return true;
		}
		return false;
	}
}
