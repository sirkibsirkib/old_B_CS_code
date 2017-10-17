package mazeGame;

import java.io.PrintStream;

import Methods.Fun;

public class Room {
	int xSize, ySize;
	//Item[] items;
	ItemRow field;
	PrintStream out = new PrintStream(System.out);
	
	//constructor
	Room(int xSize, int ySize){
		field = new ItemRow(xSize*ySize);
		this.xSize = xSize;
		this.ySize = ySize;
		outerWall();
	}
	
	//returns the name of an item at (x,y) and "nothing" if no item is found
	String nameAt(int x, int y){
		for(int i = 0; i < field.numItems; i++){
			if(field.items[i].isAt(x, y)){
				return field.items[i].name;
			}
		}
		return "nothing";
	}
	
	//returns the item index of an item at (x,y) and -1 if no item is found
	int itemAt(int x, int y){
		for(int i = 0; i < field.numItems; i++){
			if(field.items[i].isAt(x, y)){
				return i;
			}
		}
		return -1;
	}
	
	//returns the reference of an item at (x,y)
	Item itemRefAt(int x, int y){
		for(int i = 0; i < field.numItems; i++){
			if(field.items[i].isAt(x, y)){
				return field.items[i];
			}
		}
		return null;
	}
	
	//adds an item with name in a free space, prints a warning if there
	//is no free space, and returns an Item pointer to the new Item
	Item addSomewhereFree(String name){
		if(field.numItems == xSize*ySize){
			out.printf("Grid Full! Cannot add %s\n", name);
			return null;
		}
		
		int x, y;
		do{
			x = Fun.rng(xSize);
			y = Fun.rng(ySize);
		}while(itemAt(x,y) != -1);
		
		field.add(x,y,name);
		
		return field.items[field.numItems-1];
	}
	
	//removes any Item at (x,y)
	void destroyAt(int x, int y){
		for(int i = 0; i < field.numItems; i++){
			if(field.items[i].isAt(x, y)){
				field.removeItem(i);
			}
		}
	}
	
	//adds or replaces an Item at (x,y) and returns a pointer to it
	void makeAt(int x, int y, String name){
		
		int id = itemAt(x,y);
		if(id != -1){
			field.items[id].name = name;
			//return field.items[id];
		}else{
			field.add(x,y,name);
			//return field.items[numItems-1];
		}
	}
	
	//returns a pointer of first item with name if it exists
	Item findNamed(String name){
		for(int i = 0; i < field.numItems; i++){
			if(field.items[i].name == name){
				return field.items[i];
			}
		}
		return null;
	}
	
	//moves first item with "name" in "direction"
	void moveItemNamed(String name, String direction){
		Item ref = findNamed(name);
		moveThisItem(ref, direction);
	}
	
	//moves first item with reference in "direction"
	void moveThisItem(Item ref,String direction){
		switch(direction){
		case "left":	tryMoveTo(ref, ref.x-1, ref.y); break;
		case "right":	tryMoveTo(ref, ref.x+1, ref.y); break;
		case "up":		tryMoveTo(ref, ref.x, ref.y-1); break;
		case "down":	tryMoveTo(ref, ref.x, ref.y+1); break;
		}
	}
	
	//map gets a solid outer wall
	void outerWall(){
		for(int i = 0; i < xSize; i++){
			makeAt(i, 0,"wall");
			makeAt(i, ySize-1,"wall");
		}
		for(int i = 1; i < ySize-1; i++){
			makeAt(0, i, "wall");
			makeAt(xSize-1, i, "wall");
		}
	}
	
	//tries to move referenced item into position free, non-solid (x,y).
	//prints result of attempt to console
	void tryMoveTo(Item ref, int x, int y){
		if(freeOrSolid(x,y)){
			ref.x = x;
			ref.y = y;
			if(ref.name.equals("man")){
				out.printf("%s successfully moved!\n", ref.name);
			}
		}else if (ref.name.equals("man")){
			out.printf("cannot go there, %s in the way!\n", nameAt(x,y));
		}
	}
	
	//returns whether or not (x,y) is either free or solid
	boolean freeOrSolid(int x, int y){
		return itemAt(x,y) == -1 || !field.items[itemAt(x,y)].isSolid();
	}
	
	//prints map of current setup to console
	void drawMap(){
		String s = "";
		for(int i = 0; i < ySize; i++){
			for(int j = 0; j < xSize; j++){
				switch(nameAt(j,i)){
				case "nothing":	out.print(" - - "); break;
				case "rock":	out.print("<<|>>"); break;
				case "wall":	out.print("[___]"); break;
				case "gravel":	out.print("{:::}"); break;
				case "enemy":	drawEnemy(itemRefAt(j,i)); break;
				case "man":		drawMan(itemRefAt(j,i)); break;
				}
			}
			out.println();
		}
	}
	
	//returns true if two items are adjacent in the room
	boolean isAdjacent(Item a, Item b){
		if(Math.abs(a.x-b.x) == 1 && Math.abs(a.y-b.y) == 0)return true; //horizontally adjacent
		if(Math.abs(a.x-b.x) == 0 && Math.abs(a.y-b.y) == 1)return true; //vertically adjacent
		return false;
	}
	
	//custom draw method when item drawn is "man"
	void drawMan(Item manRef){
		out.print(manRef.inv.lHand.leftLook);
		out.print(manRef.healthFace());
		out.print(manRef.inv.rHand.rightLook);
	}
	
	void drawEnemy(Item manRef){
		out.print(manRef.inv.lHand.leftLook);
		out.print("(Y)");
		out.print(manRef.inv.rHand.rightLook);
	}
}