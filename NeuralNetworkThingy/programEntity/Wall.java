package programEntity;

public class Wall implements Entity {
	private int x, y;
	
	public Wall(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void moveBy(int x, int y){
		this.x += x;
		this.y += y;
	}
	
	public void moveTo(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public boolean isObstacle(){
		return true;
	}
}
