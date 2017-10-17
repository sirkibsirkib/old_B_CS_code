import java.io.PrintStream;
import java.util.Scanner;

public class Stats {
	PrintStream out = new PrintStream(System.out);
	Scanner in = new Scanner(System.in);
	
	private void start() {
		switch(ask("How many populations are involved?", "one", "two")){
		case 0:	onePop();
		case 1:	twoPops();
		}
	}
	
	private void twoPops() {
		// TODO Auto-generated method stub
		
	}

	private void onePop() {
		switch(ask("What are we looking for?", "mean", "proportion")){
		case 0:	;
		case 1:	;
		}
	}

	private int ask(String q, String... options){
		out.println(q);
		for(int i = 0; i < options.length; i++){
			out.println("   [" + i + "] " + options[i]);
		}
		int get;
		try{
			get = Integer.parseInt(in.nextLine());
		}catch(Exception e){
			return -1;
		}
		if(get <= options.length && get >= 0)
			return get;
		return -1;
	}

	public static void main(String[] args){
		new Stats().start();
	}
}
