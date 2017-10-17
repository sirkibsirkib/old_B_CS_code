package assignment2;

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
		if(!(other instanceof Identifier))
			return false;
		Identifier otherI = (Identifier)other;
		if(getIdLength() != otherI.getIdLength())
			return false;
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
	
	public IdentifierInterface clone(){
		IdentifierInterface copy = new Identifier(getIdCharacter(0));
		for(int i = 1; i < getIdLength(); i++)
			copy.appendId(getIdCharacter(i));
		return copy;
	}
	
	public int compareTo(Object other){		
		if(!(other instanceof IdentifierInterface))
			throw new Error();
		IdentifierInterface otherId = (IdentifierInterface) other;
		int minLength = getIdLength();
		if(otherId.getIdLength() < minLength)
			minLength = otherId.getIdLength();
		for(int i = 0; i < minLength; i++){
			int compareTo = getIdCharacter(i) - otherId.getIdCharacter(i);
			if(compareTo < 0) return -1;
			if(compareTo > 0) return 1;
		}
		if(otherId.getIdLength() - getIdLength() < 0) return 1;
		if(otherId.getIdLength() - getIdLength() > 0) return -1;
		return 0;
	}
}
