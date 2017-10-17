package mazeGame;

import Methods.Fun;

public class Item {
	String name;
	int x, y, health, maxHealth;
	ItemRow inv;
	char leftLook = ' ',
		rightLook = ' ';
	static final String[] SOLIDS = {"man","wall", "rock", "gravel", "enemy"};
	
	//constructor
	Item(String name){
		x = y = 0;
		this.name = name;
		setup();
	}
	//overloaded constructor given (x,y)
	Item(int x, int y, String name){
		this.x = x;
		this.y = y;
		this.name = name;
		setup();
	}
	
	//constructor continued...
	void setup(){
		if(name.equals("wall") && Fun.chance(7)){this.name = "rock";}
		if(name.equals("wall") && Fun.chance(7)){this.name = "gravel";}
		freshLook();
	}
	
	//checks if this is an item that needs it's own look
	void freshLook(){
		switch(name){
		case "sword":	freshSword();	break;
		case "shield":	freshShield();	break;
		}
	}
	
	//assigns an arbitrary sword look
	void freshSword(){
		switch(Fun.rng(3)){
		case 0:	leftLook = '\\';rightLook = '/';break;
		case 1:	leftLook = '!';	rightLook = '!';break;
		case 2:	leftLook = '|';	rightLook = '|';break;
		}	
	}
	
	//assigns an arbitrary shield look
	void freshShield(){
		switch(Fun.rng(4)){
		case 0:	leftLook = 'o';rightLook = 'o';break;
		case 1:	leftLook = 'O';	rightLook = 'O';break;
		case 2:	leftLook = '$';	rightLook = '$';break;
		case 3:	leftLook = '0';	rightLook = '0';break;
		}	
	}
	
	//returns true if Item is at (x,y)
	boolean isAt(int x, int y){
		if(x != this.x){
			return false;
		}
		if(y != this.y){
			return false;
		}
		return true;
	}
	
	//returns true if this object is solid
	boolean isSolid(){
		for(int i = 0; i < SOLIDS.length; i++){
			if(SOLIDS[i].equals(name)){
				return true;
			}
		}
		return false;
	}
	
	//returns whether or not given item is same as this item
	boolean sameAs(Item x){
		return name.equals(x.name);
	}
	
	//gives a new inventory of standard size
	ItemRow giveInventory(){
		inv = new ItemRow();
		return inv;
	}
	
	void setStartHealth(int setHealth){
		maxHealth = health = setHealth;
	}
	
	char healthFace(){
		if(health > maxHealth * .5){
			return 'ü';
		}
		return 'ö';
	}
}