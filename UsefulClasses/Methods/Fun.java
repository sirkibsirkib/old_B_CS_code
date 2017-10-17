package Methods;

//Fun is a class of useful functions to apply globally
public abstract class Fun {
	
	//returns a random integer from 0 to n, not including n
	public static int rng(int n){
		return (int)(Math.random()*n);
	}
	
	//has a 1 in n chance of returning true
	public static boolean chance(double outOf){
		return 0 == (int)(Math.random()*outOf);
	}
	
	//returns "left", "right", "up" or "down" randomly
	public static String randomDirection(){
		switch(rng(4)){
		case 0:	return "left";
		case 1:	return "right";
		case 2:	return "up";
		}
		return "down";
	}
	
	//returns a string of input char "c", "iterations" long
	public static String chars(char c, int iterations){
		String s = "";
		for(int i = 0; i < iterations; i++){
			s += c;
		}
		return s;
	}
	
	//returns input string , lowercase, with characters a-z only
	//useful for passing of strings with some leeway
	public static String textMinimize(String input){
		input = input.toLowerCase();
		String result = "";
		char c;
		for(int i = 0; i < input.length(); i++){
			c = input.charAt(i);
			if('a' <= c && c <= 'z'){
				result += c;
			}
		}
		return result;
	}
	
	public static int triangleNumber(int n){
		return (n*(n+1)/2);
	}
	
	//returns positive-bound modulo of input, using custom bound
	public static int positiveMod(int input, int bound){
		if(input < 0){ //custom modulo
			while(input < 0){
				input += bound;
			}
			return input;
		}
		//standard modulo
		return input % bound;
	}
}
