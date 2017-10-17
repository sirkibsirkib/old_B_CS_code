package Assorted;

import java.util.Scanner;

public class BinaryHash {
	private void start(){
		Scanner in = new Scanner(System.in);
		while(true){
			System.out.println(hashify(in.nextLine()));
		}
	}
	
	private boolean hashify(String in) {
		return isEven((int) (Math.pow(countify(in), 1.2 + (in.length() / 5))));
	}

	private int countify(String in) {
		int v = 0;
		for(int i = 0; i < in.length(); i++){
			v += in.charAt(i) - 'a';
		}
		return v;
	}

	private boolean isEven(int x) {
		return x == x/2*2;
	}

	public static void main(String[] args){
		new BinaryHash().start();
	}
}
