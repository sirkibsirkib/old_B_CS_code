package game;

import java.util.Random;

public class SeedTest {
	long coordX = 1000,
		coordY = 1029,
		seed = 193849;
	
	SeedTest(){
		
	}
	
	private void start() {
		int soiz = 20;
		Random ran = new Random(4);
		for(int i = 0; i < soiz; i++){
			coordX++;
			for(int j = 0; j < soiz; j++){
				coordY++;
				boolean isPlanet = (ran.nextInt(20) == 0);
				if(isPlanet)
					System.out.print("(X)");
				else
					System.out.print("  ");
			}
			System.out.println();
		}
	}

	public static void main(String[] args){
		new SeedTest().start();
	}
}
