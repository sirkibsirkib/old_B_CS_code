package assignment1;

public class Identifier implements IdentifierInterface{
	private StringBuilder id;
	
	//CONSTRUCTORS & INITIALIZATION
	
	public Identifier(char c){
		id = new StringBuilder();
		id.append(c);
	}
	
	public Identifier(Identifier original){
		id = new StringBuilder();
		for(int i = 0; i < original.getIdLength(); i++)
			id.append(original.getIdCharacter(i));
	}
	
	public void init(char c) {
		id = new StringBuilder();
		id.append(c);
	}
	
	//GETTERS & CHECKS
	
	public char getIdCharacter(int index) {
		return id.charAt(index);
	}
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof Identifier)) return false;
		Identifier otherI = (Identifier)other;
		if(getIdLength() != otherI.getIdLength()) return false;
		for(int i = 0; i < getIdLength(); i++)
			if(getIdCharacter(i) != otherI.getIdCharacter(i))
				return false;
		return true;
	}
	
	public int getIdLength(){
		return id.length();
	}
	
	public void appendId(char c) {
		id.append(c);
	}
}
