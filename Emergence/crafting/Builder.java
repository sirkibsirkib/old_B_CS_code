package crafting;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

import primativeRows.Row;

public class Builder {
	RecipeBook rb;
	String[] reagents;
	
	void start() throws IOException{
		rb = new RecipeParser().parseRecipes();
		System.out.println("type eg: palladium 0 1)");
		listen();
	}
	
	private void listen() {
		reagents = new String[9];
		for(int i = 0; i < reagents.length; i++)
			reagents[i] = "";
		Scanner in = new Scanner(System.in);
		while(true){
			System.out.println();
			readLine(in);
			printReagents();
		}
	}

	private void printReagents() {
		PrintStream out = new PrintStream(System.out);
		
		out.printf("[%s%s,%s%s,%s%s]\n", reagents[0], blank(reagents[0]), reagents[1], blank(reagents[1]), reagents[2], blank(reagents[2]));
		out.printf("[%s%s,%s%s,%s%s]\n", reagents[3], blank(reagents[3]), reagents[4], blank(reagents[4]), reagents[5], blank(reagents[5]));
		out.printf("[%s%s,%s%s,%s%s]\n", reagents[6], blank(reagents[6]), reagents[7], blank(reagents[7]), reagents[8], blank(reagents[8]));
		
	}

	private String blank(String string) {
		String blankSpaces = "";
		for(int i = 20-string.length(); i > 0; i--){
			blankSpaces += " ";
		}
		return blankSpaces;
	}

	private void readLine(Scanner in) {
		String what = "";
		while(!nextCharIsDigit(in)){
			what += in.next() + " ";
			switch(what.trim()){
			case "go":{
				String buildName = rb.build(reagents);
				if(buildName == null)
					System.out.println("no items made");
				else
					System.out.println("you made a " + buildName);
				return;
			}	
			case "scavenge":{
				Recipe get = rb.get(in.next());
				if(get == null){
					System.out.println("NO SUCH RECIPE");
					return;
				}
				Row<String> parts = get.scavenge();
				System.out.print("GOT: ");
				for(int i = 0; i < parts.getNumberOfElements(); i++){
					System.out.print(parts.getElement(i) + ", ");
				}
				System.out.println();
				return;
			}	
			}
		}
		what = what.trim();
		int x = in.nextInt(),
			y = in.nextInt();
		reagents[y*3 + x] = what;
	}
	
	boolean nextCharIsDigit (Scanner in) {
		return in.hasNext("[0-9]");
	}

	public static void main(String... args) throws IOException{
		new Builder().start();
	}
}
