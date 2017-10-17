package test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import ui.UIAuxiliaryMethods;

public class Test {
	Test(){
		UIAuxiliaryMethods.askUserForInput();
	}
	//text file is just "1"
	
	void start() throws FileNotFoundException{
		
		Scanner input2 = new Scanner(new FileInputStream("C:/Users/Christopher/Documents/aaatest.txt"));
		Scanner input  = new Scanner(System.in);
		int data = input.nextInt();
		System.out.println(data);
	}
	public static void main(String[] args) throws FileNotFoundException {
		new Test().start();
	}
}
