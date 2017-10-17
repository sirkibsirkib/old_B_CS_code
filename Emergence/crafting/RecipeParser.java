package crafting;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import primativeRows.Row;


//UIAuxiliaryMethods.askUserForInput();
//Scanner fileScanner = new Scanner(System.in);

public class RecipeParser {
	RecipeBook rb;
	Row<String> recipes, components, types;
	
	RecipeBook parseRecipes() throws IOException{
		rb = new RecipeBook();
		recipes = new Row<>();
		components = new Row<>();
		types = new Row<>();
		
		String path = "C:/Users/Christopher/Dropbox/Christopher-Henry/read.txt";
		
		BufferedReader br = new BufferedReader(new FileReader(path));
		String everything;
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append(System.lineSeparator());
	            line = br.readLine();
	        }
	        everything = sb.toString();
	    } finally {
	        br.close();
	    }
	    
	    Scanner stringScanner = new Scanner(everything);
	    
		while(stringScanner.hasNext()){
			rb.add(parseRecipe(stringScanner));
			if(stringScanner.hasNext())
				stringScanner.nextLine();
		}
		removeKnownRecipes();
		System.out.println("ye");
		printStringRow("Primative types: ", types);
		printStringRow("Fundamental components: ", components);
		return rb;
	}

	private void printStringRow(String preamble, Row<String> row) {
		int num = row.getNumberOfElements();
		System.out.print(preamble);
		for(int i = 0; i < num; i++){
			System.out.print(row.getElement(i));
			if(i < num-1){
				if(i == 8)
					System.out.print(",\n\t");
				else
					System.out.print(", ");
			}
				
		}
		System.out.println();
	}

	private void removeKnownRecipes() {
		for(int i = 0; i < recipes.getNumberOfElements(); i++){
			String recipe = recipes.getElement(i);
			components.remove(recipe);
			types.remove(recipe);
		}
		components.remove("");
		types.remove("");
	}

	private Recipe parseRecipe(Scanner fileScanner) {
		String name = fileScanner.nextLine().trim();
		if(!recipes.contains(name))
			recipes.add(name);
		String[] types = types(fileScanner.nextLine(), name);
		String[] formula = formula(fileScanner);
		//System.out.println();
		return new Recipe(rb, name, formula, types);
	}
	
	private String[] formula(Scanner input) {
		String line = input.nextLine();
		if(!line.contains("[")){
			return null;
			
		}
			
		line = removeChars(line, '[', ']', '_') + ",";
		line += removeChars(input.nextLine(), '[', ']', '_') + ",";
		line += removeChars(input.nextLine(), '[', ']', '_') + ",";
		//System.out.println("formula line:" + line);
		
		String[] result = new String[9];
		
		Scanner gridScanner = new Scanner(line);
		gridScanner.useDelimiter(",");
		for(int i = 0; i < 9; i++){
			String next = gridScanner.next().trim();
			result[i] = next;
			if(!components.contains(next))
				components.add(next);
		}
			
		gridScanner.close();
		
		return result;
	}
	
	private String[] types(String line, String name) {
		//System.out.println("types line:" + line);
		Row<String> strings = new Row<>();
		Scanner lineScanner = new Scanner(line);
		lineScanner.useDelimiter(",");
		while(lineScanner.hasNext()){
			String next = removeChars(lineScanner.next(), ' ');
			if(!types.contains(next))
				types.add(next);
			if(name.equals(next)){
				System.out.println("RECURSIVE TYPE! " + name + " contains its own type");
				lineScanner.close();
				throw new Error();
			}
			
			strings.add(next);
		}
		lineScanner.close();
		String[] result = new String[strings.getNumberOfElements()];
		for(int i = 0; i < strings.getNumberOfElements(); i++){
			result[i] = strings.getElement(i);
		}
		return result;
	}
	
	private String removeChars(String s, char... c){
		for(int i = 0; i < c.length; i++)
			s = s.replace(""+c[i], "");
		return s;
	}

}
