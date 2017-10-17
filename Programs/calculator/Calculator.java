package calculator;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import Methods.Fun;


public class Calculator {
	int volume = 0,
			currentUp = 0,
			x,
			y;
	Robot bot;
	int killMeCountDown = 4;
	
	Calculator(){
		
	}
	
	void start(){
		try{
			bot = new Robot();
			showKey();
			getInput();
		}
		catch(AWTException e){
			e.printStackTrace();
		}
	}
	
	void showKey(){
		System.out.println("IIII------------NNNNN");
		System.out.println("IIII                |");
		System.out.println("|                   |");
		System.out.println("|                   |");
		System.out.println("|                   |");
		System.out.println(" ------------------- ");
		System.out.println();
		System.out.println("I: Incriment volume");
		System.out.println("N: Next video & reset volume");
		System.out.println("4 sunsecutive N's will end the process");
	}
	
	void getInput(){
		while(true){
			bot.delay(80);
			switch(getStatus()){
				case("topLeft"):	incVolume(); break;
				case("allRight"):	zeroVolume(); bot.keyPress(KeyEvent.VK_N); break;
			}
			randoms();
		}
	}
	
	//read mouse position
	String getStatus(){
		x = MouseInfo.getPointerInfo().getLocation().x;
		y = MouseInfo.getPointerInfo().getLocation().y;
		
		if(x<30 && y<30){
			return "topLeft";
		}
		if(x>1000 & y == 0){
			return "allRight";
		}
		return "unknown";
	}
	
	//Increment volume NOTCH
	void incVolume(){
		currentUp++;
		if(currentUp >= volume + 5){
			killMeCountDown = 4;
			currentUp = 0;
			bot.mouseWheel(-1);
			volume += 1;
		}
	}
	
	//scroll volume down to zero
	void zeroVolume(){
		currentUp = 0;
		while(volume > 0){
			bot.delay(10);
			volume -= 1;
			bot.mouseWheel(1);
		}
		bot.delay(2000);
		killMeCountDown--;
		if(killMeCountDown < 0){
			System.out.println("killing self!");;
		}
		if(killMeCountDown < 0){
			endRun();
		}
	}

	void randoms(){
		if(Fun.chance((volume+20)*10)){
			bot.keyPress(KeyEvent.VK_Y);
			bot.delay(1000);
		}
		/*if(Fun.chance(50)){
			bot.mouseMove(x+Fun.rng(40)-20, y+Fun.rng(40)-20);
		}*/
	}
	
	void endRun(){
		bot.keyPress(KeyEvent.VK_SPACE);
		bot.keyPress(KeyEvent.VK_M);
		System.exit(1);
	}
	
	public static void main(String[] args) {
		new Calculator().start();
	}
}
