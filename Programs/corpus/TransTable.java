package corpus;

public class TransTable implements Translator {
	char[] translations;
	
	public TransTable(char... translations){
		this.translations = translations;
	}
	
	public String translate(String x){
		String translated = "";
		for(int i = 0; i < x.length(); i++){
			translated += translateChar(x.charAt(i));
		}
		return translated;
	}

	private char translateChar(char c) {
		if(c < 'a' || 'z' < c)
			return c;
		return translations[c - 'a'];
	}
}
