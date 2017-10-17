package hangman;

import java.util.Scanner;

public class Hangman {
	CharRow rYes, rNo, rMaybe;
	
	GuessWord word;
	Scanner in = new Scanner(System.in);
	String noose2 = "   ________          \n   |      |          \n   |     _O_         \n   |      |          \n   |     / |";
	String noose = "   ________          \n   |      |          \n   |                 \n   |                 \n   |        ";
	String nooseBackup = "   ________          \n   |      |          \n   |                 \n   |                 \n   |        ";

	int gameState = 1;
	int waitTime = 3;
	
	Hangman(){
		word = new GuessWord();
		setupRows();
	}
	
	void start() throws InterruptedException{
		draw();
		while(true){
			Thread.sleep(1000);
			switch(gameState){
			case(1):{
				if(in.hasNext()){
					System.out.println();
					System.out.println();
					readChar(in.next().charAt(0));
				}
				break;
			}
			case(0):{
				if(waitTime == 0){
					gameState = 1;
					reset();
					waitTime = 3;
				}else{
					System.out.println("Restarting in " + waitTime + "...");
					waitTime--;
				}
				break;
			}
			}
			
		}
	}
	
	void setupRows(){
		rYes = new CharRow(26);
		rNo = new CharRow(26);
		rMaybe = new CharRow(26);
		
		for(int i = 0 ; i < 26; i++){
			rMaybe.add((char)('a' + i));
		}
	}
	
	void readChar(char c){
		if(rMaybe.contains(c)){
			guess(c);
		} else {
			if(c >= 'a' && c <= 'z'){
				System.out.println(c + " has already been guessed.");
			}else{
				System.out.println("Please type only a-z letter");
			}
			
		}
		if(rNo.numElements > 0){
			System.out.println("Already tried: " + rNo.list());
		}
	}
	
	void guess(char c){
		rMaybe.deleteAll(c);
		if(word.containsLetter(c)){
			word.guess(c);
			rYes.add(c);
		}else{
			addBodyPart();
			rNo.add(c);
		}
		draw();
		checkForVictory();
	}
	
	void draw(){
		System.out.println(noose);
		System.out.println("  " + word.visibleText);
		System.out.println();
	}
	
	void addBodyPart(){
		switch(rNo.numElements+1){
		case(1):	addNooseCharAt('O', 54); break;//head
		case(2):	addNooseCharAt('|', 76); break; //body
		case(3):	addNooseCharAt('_', 53); break; //left arm
		case(4):	addNooseCharAt('_', 55); break; //right arm
		case(5):	addNooseCharAt('/', 97); break; //left leg
		case(6):	addNooseCharAt('\\', 99); break; //right leg
		case(7):	endGame();
		}
	}
	
	void addNooseCharAt(char c, int index){
		noose = noose.substring(0, index) + c + noose.substring(index+1, noose.length());
	}
	
	void endGame(){
		System.out.println("You lose!!");
		gameState = 0;
	}
	
	void checkForVictory(){
		if(word.allGuessed()){
			System.out.println("You Win!!");
			gameState = 0;
		}
	}
	
	void reset() throws InterruptedException{
		System.out.println("Starting!");
		System.out.println();
		System.out.println();
		Thread.sleep(1000);
		noose = nooseBackup + "";
		setupRows();
		word = new GuessWord();
		draw();
	}
	
	public static void main(String[] args) throws InterruptedException {
		new Hangman().start();
	}
	
}
