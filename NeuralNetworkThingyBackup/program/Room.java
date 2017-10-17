package program;

import programEntity.Entity;
import programEntity.EntityRow;
import programEntity.Player;
import programEntity.PlayerShadow;
import programEntity.Wall;

public class Room {
	private EntityRow things;
	static final int ROOM_WIDTH = 155,
			ROOM_HEIGHT = 137,
			MAN_START_X = ROOM_WIDTH/2,
			MAN_START_Y = ROOM_HEIGHT/2;
	private Entity man;
	
	public Room(){
		things = new EntityRow(ROOM_WIDTH*ROOM_HEIGHT);
		man = new Player(MAN_START_X, MAN_START_Y);
		things.add(man);
		
		createBoundaries();
		clutterRoom();
		
		printAll();
	}
	
	public Entity getRelativeBlock(int x, int y){
		return things.getEntityAt(man.getX() + x, man.getY() + y);
	}
	
	private void createBoundaries(){
		for(int i = 0; i < ROOM_WIDTH; i++){
			things.add(new Wall(i, 0));
			things.add(new Wall(i, ROOM_HEIGHT-1));
		}
		
		for(int i = 1; i < ROOM_HEIGHT-1; i++){
			things.add(new Wall(0, i));
			things.add(new Wall(ROOM_WIDTH-1, i));
		}
	}
	
	public double playerProgress(){
		int dX = man.getX()-MAN_START_X,
			dY = man.getY()-MAN_START_Y;
		return Math.sqrt(dX*dX + dY*dY);
	}
	
	public void refreshRoom(){
		things.removeAllPlayerShadow();
		man.moveTo(MAN_START_X, MAN_START_Y);
	}
	
	private void clutterRoom(){
		int num0 = 0, num1 = 0, num2 = 0, num3 = 0;
		for(int i = 0; i < ROOM_WIDTH; i++){
			for(int j = 0; j < ROOM_HEIGHT; j++){
				if(!somethingAt(i, j) && (num3%11==0 || num0%2==0 && num1%3==0 )||( num2%5==0 && (num1-num0)%2==0)){
					things.add(new Wall(i,j));
				}
				num0 += 1;
				num1 += num0;
				num3 += num2 - num1;
				num0 += num3;
			}
			num0 -= 3;
			num1 += num0;
			num2 += 1;
			num3 = num1;
		}
	}
	
	public boolean somethingAt(int x, int y){
		for(int i = 0; i < things.getNumberOfElements(); i++){
			Entity temp = things.getElement(i);
			if(temp.isObstacle() && temp.getX() == x && temp.getY() == y){
				return true;
			}
		}
		return false;
	}
	
	public void printAll(){
		for(int i = 0; i < ROOM_HEIGHT; i++){
			for(int j = 0; j < ROOM_WIDTH; j++){
				if(somethingAt(j,i)){
					System.out.print("x");
				}else{
					System.out.print(" ");
				}
			}
			System.out.println();
		}
	}
	
	public void inputsFromFires(String[] listOfFireNames){
		System.out.println(things.getNumberOfElements());
		for(int i = 0; i < listOfFireNames.length; i++){
			switch(listOfFireNames[i]){
			case "left": 	moveLeft(); 	break;
			case "right": 	moveRight(); 	break;
			case "up": 		moveUp();		break;
			case "down": 	moveDown();		break;
			}
		}
	}
	
	public void moveLeft(){
		if(!somethingAt(man.getX()-1, man.getY())){
			things.add(new PlayerShadow(man.getX(), man.getY()));
			man.moveBy(-1, 0);
		}
	}
	
	public void moveRight(){
		if(!somethingAt(man.getX()+1, man.getY())){
			things.add(new PlayerShadow(man.getX(), man.getY()));
			man.moveBy(1, 0);
		}
	}
	
	public void moveUp(){
		if(!somethingAt(man.getX(), man.getY()-1)){
			things.add(new PlayerShadow(man.getX(), man.getY()));
			man.moveBy(0, -1);
		}
	}
	
	public void moveDown(){
		if(!somethingAt(man.getX(), man.getY()+1)){
			things.add(new PlayerShadow(man.getX(), man.getY()));
			man.moveBy(0, 1);
		}
	}
	
	public int getPlayerX(){
		return man.getX();
	}
	
	public int getPlayerY(){
		return man.getX();
	}
}
