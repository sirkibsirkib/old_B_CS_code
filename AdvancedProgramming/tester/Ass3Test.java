package tester;

import assignment3.Program;

public class Ass3Test {
	private void start() {
		Program.start("sort -i /desktop/wang.txt | uniq");
	}
	
	public static void main(String[] args){
		new Ass3Test().start();
	}
}
