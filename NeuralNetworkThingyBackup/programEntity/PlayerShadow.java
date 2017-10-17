package programEntity;

public class PlayerShadow implements Entity{
	private int x, y;
	
	public PlayerShadow(int x, int y){
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
		return false;
	}
}
