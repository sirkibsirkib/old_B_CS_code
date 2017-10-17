package programEntity;

public interface Entity {
	public int getX();
	public int getY();
	public void moveBy(int x, int y);
	public void moveTo(int x, int y);
	public boolean isObstacle();
}
