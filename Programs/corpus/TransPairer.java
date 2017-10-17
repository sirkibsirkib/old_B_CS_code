package corpus;

import java.util.ArrayList;
import java.util.List;

public class TransPairer implements Translator{
	List<Pairing> ls;
	
	public TransPairer(){
		ls = new ArrayList<>();
	}
	
	public TransPairer(char... alphabet){
		this();
		alphabet(alphabet);
	}
	
	public void alphabet(char... t) {
		if(t.length > 26)
			throw new Error();
		for(int i = 0; i < t.length; i++){
			if(t[i] != (i + 'a'))
				addPair((char)(i + 'a'), t[i]);
		}
	}
	
	public String translate(String x){
		String translated = "";
		for(int i = 0; i < x.length(); i++){
			translated += translateChar(x.charAt(i));
		}
		return translated;
	}

	public void addPair(char a, char b) {
		ls.add(new Pairing(a, b));
	}

	private char translateChar(char c) {
		for(int i = 0; i < ls.size(); i++){
			Pairing next = ls.get(i);
			if(next.appliesTo(c))
				return next.translate();
		}
		return c;
	}
	
	public class Pairing {
		private char before, after;
		
		Pairing(char before, char after){
			this.before = before;
			this.after = after;
		}
		
		public boolean appliesTo(char x){
			return before == x;
		}
		
		public char translate(){
			return after;
		}
	}
}
