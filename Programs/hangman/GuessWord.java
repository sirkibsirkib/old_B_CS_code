package hangman;

import Methods.Fun;

public class GuessWord {
	String[] possibleWords = {"lemonade", "general", "cowboy", "fishpaste", "hairbrush", "symbol",
			"pirate", "sailor", "particle", "sediment", "flourish","mountain", "membrane", "variable",
			"trebuchet", "alarming", "vibration", "articulate", "selection", "ministry",
			"collaboration", "punishment", "homogenous", "growling", "history", "venture", "blight",
			"archive", "recess", "journeys", "artichoke", "gherkin", "ministry", "diamond", "reflex",
			"lustre", "display", "sickly", "homogenous", "distribute", "organize", "flourish", "surmount",
			"plastic", "telltale", "ventilate", "attachment", "artificial", "pomegranate", "bracelet",
			"robotics", "daffodil", "melancholy", "numeral", "contrived", "unification", "bereave",
			"dissipate", "hologram", "television", "octopus", "rascal", "gentrify", "levitate", "heroic"};
	String text, visibleText;
	
	GuessWord(){
		text = possibleWords[Fun.rng(possibleWords.length)];
		visibleText = "";
		for(int i = 0; i < text.length(); i++){
			visibleText += "_ ";
		}
	}
	
	boolean containsLetter(char c){
		return text.contains(c + "");
	}
	
	void guess(char c){
		for(int i = 0; i < text.length(); i++){
			if(text.charAt(i) == c){
				visibleText = visibleText.substring(0, 2*i) + c + visibleText.substring(2*(i)+1, visibleText.length());
			}
		}
	}
	
	boolean allGuessed(){
		return !visibleText.contains('_' + "");
	}
}
