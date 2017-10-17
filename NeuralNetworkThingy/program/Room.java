package program;

import programEntity.Entity;
import programEntity.EntityRow;
import programEntity.Player;
import programEntity.PlayerShadow;
import programEntity.Wall;

public interface Room {
	public Entity getRelativeBlock(int x, int y);
	public boolean playerIsAt(int x, int y);
	public double playerProgress();
	public void refreshRoom();
	public boolean somethingAt(int x, int y);
	public void printAll();
	public void inputsFromFires(String[] listOfFireNames);
	public void moveLeft();
	public void moveRight();
	public void moveUp();
	public void moveDown();
	public int getPlayerX();
	public int getPlayerY();
}
