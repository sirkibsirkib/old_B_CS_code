package programEntity;

public class Player implements Entity{
	private int x, y;
	
	public Player(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
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
