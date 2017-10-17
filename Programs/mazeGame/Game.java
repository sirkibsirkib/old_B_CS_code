package mazeGame;

import java.io.PrintStream;
import java.util.Scanner;

import Methods.Fun;

public class Game {
	static final int ROOM_X = 10,
			ROOM_Y = 20;
	int sleepTime = 300,
			wallNumber = (int)(ROOM_X*.2*ROOM_Y);
	Room m;
	Scanner in = new Scanner(System.in);
	Item man, opponent;
	ItemRow enemies, currentInv;
	String gameState = "overworld";
	PrintStream out = new PrintStream(System.out);
	
	//constructor
	Game(){
		enemies = new ItemRow();
	}
	
	//sets up game. begins play
	void start() throws InterruptedException {
		freshMap();
		m.drawMap();
		play();
	}
	
	//loop handles player input
	void play() throws InterruptedException{
		String s;
		
		while(true){
			s = in.next();
			
			//reads game-state to decide what to do
			switch(gameState){
				case "inventory":	gameInventory(s); break;
				case "overworld":	gameOverworld(s); break;
				case "battle":		gameBattle(s); break;
			}
			if(gameState.equals("overworld")){
				m.drawMap();
			}
			
			Thread.sleep(sleepTime);
		}
	}
	
	//input handled in inventory context
	void gameInventory(String s){
		if(Integer.parseInt(s) == (currentInv.numItems)){
			gameState = "overworld";
			out.println("Closing Inventory");
		}
	}
	
	//input handled in overworld context
	void gameOverworld(String s){
		switch(s){
		case "left":		m.moveThisItem(man, s); break;
		case "right":		m.moveThisItem(man, s); break;
		case "up":			m.moveThisItem(man, s); break;
		case "down":		m.moveThisItem(man, s); break;
		
		case "map":			m.drawMap(); break;
		case "inventory":	printInventoryOf(man); break;
		case "restart":		out.println("Game restarted!");freshMap(); break;
		}
		
		
		
		moveEnemies();
		manChecksForBattle();
	}
	
	//input handled in battle context
	void gameBattle(String s){
		//TODO
	}
	
	//makes each enemy unit take a random step
	void moveEnemies(){
		for(int i = 0; i < enemies.numItems; i++){
			m.moveThisItem(enemies.items[i], Fun.randomDirection());
		}
	}
	
	//resets map
	void freshMap(){
		m = new Room(ROOM_X, ROOM_Y);
		man = m.addSomewhereFree("man");
		man.inv = man.giveInventory();
		man.setStartHealth(6);
		
		man.inv.lHand = new Item("sword");
		man.inv.rHand = new Item("shield");
		man.inv.add(man.inv.lHand);
		
		for(int i = 0; i < 4; i++){
			Item ref = m.addSomewhereFree("enemy");
			enemies.add(ref);
			
			ref.giveInventory();
			ref.setStartHealth(5);
			
			ref.inv.lHand = new Item("shield");
			ref.inv.rHand = new Item("sword");
		}
		
		for(int i = 0; i < wallNumber; i++){
			m.addSomewhereFree("wall");
		}
	}
	
	//checks if an enemy is adjacent to man
	void manChecksForBattle(){
		for(int i = 0; i < enemies.numItems; i++){
			if(m.isAdjacent(man, enemies.items[i])){
				battle(enemies.items[i]);
				i = enemies.numItems;
			}
		}
	}
	
	//begins battle game-state
	void battle(Item enemy){
		out.printf("man engaged %s in battle!\n", enemy.name);
		gameState = "battle";
		opponent = enemy;
		
		out.println();
		out.print("     ");
		m.drawMan(man);
		out.print("     vs.    ");
		m.drawEnemy(enemy);
		out.println();
		
		out.printf(" [%s%s]%s", Fun.chars('#',man.health), Fun.chars(' ',man.maxHealth-man.health), Fun.chars(' ',20-man.maxHealth-enemy.maxHealth));
		out.printf("[%s%s]", Fun.chars(' ',enemy.maxHealth-enemy.health), Fun.chars('#',enemy.health));
		
		//TODO make the battle redraw it's state
	}
	
	//outputs the inventory in a list and starts inventory game-state
	void printInventoryOf(Item x){
		currentInv = x.inv;
		
		
		
		gameState = "inventory";
		out.printf("Inventory of %s:\n", x.name);
		out.println(x.inv.printableNumberedList());
		out.printf("(%d): return\n", currentInv.numItems);
	}
	
	//main
	public static void main(String[] args) throws InterruptedException {
		new Game().start();
	}
}