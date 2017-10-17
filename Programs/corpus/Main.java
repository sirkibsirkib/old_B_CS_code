package corpus;

import java.io.PrintStream;
import java.util.Scanner;

public class Main {
	Translator corpus;

	private void start() {
		corpus = new TransTable('a', 't', 'y', 'p', 'e',
							 't', 'j', 'k', 'i', 't',
							 'k', 'p', 's', 't', 'o',
							 'k', 'r', 't', 'y', 'p',
							 'u', 't', 'j', 'k', 'y',
							 'z');
		run();
	}

	private void run(){
		Scanner in = null;
		try{
			in = new Scanner(System.in);
			PrintStream out = new PrintStream(System.out);
			
			while(true){
				String text = preparse(in.nextLine());
				out.println(corpus.translate(text) + '\n');
				sleep(1000);
			}
		}finally{
			in.close();
		}
	}

	private String preparse(String s) {
		return s.toLowerCase();
	}

	private void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {}
	}

	public static void main(String[] args) {
		new Main().start();
	}
}
