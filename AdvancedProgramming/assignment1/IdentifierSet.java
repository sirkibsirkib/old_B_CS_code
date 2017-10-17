package assignment1;

public class IdentifierSet implements IdentifierSetInterface{
	private int numberOfIdentifiers;
	private IdentifierInterface[] identifiers;
	
	//CONSTRUCTORS & INITIALIZERS
	
	public IdentifierSet(){
		identifiers = new IdentifierInterface[MAX_ARRAY_SIZE];
		numberOfIdentifiers = 0;
	}
	
	public IdentifierSet(IdentifierSet original){
		identifiers = new IdentifierInterface[MAX_ARRAY_SIZE];
		numberOfIdentifiers = original.numberOfIdentifiers;
		for(int i = 0; i < numberOfIdentifiers; i++){
			identifiers[i] = new Identifier((Identifier)original.identifiers[i]);
		}
	}
	
	public void init() {
		identifiers = new IdentifierInterface[MAX_ARRAY_SIZE];
		numberOfIdentifiers = 0;
	}
	
	//ADD & REMOVE
	
	public void add(Identifier i) {
		identifiers[numberOfIdentifiers] = i;
		numberOfIdentifiers++;
	}
	
	public void tryRemoveIdentifier() {
		if(numberOfIdentifiers == 0)
			return;
		for(int i = 0; i < numberOfIdentifiers-1; i++)
			identifiers[i] = identifiers[i+1];
		numberOfIdentifiers--;
	}
	
	//OPERATIONS
	
	public IdentifierSet union(IdentifierSet other) {
		IdentifierSet b, c;	//b is copy of other	c is empty
		b = new IdentifierSet(other);
		c = new IdentifierSet(this);
		while(b.getSize() > 0){
			Identifier next = b.getIdentifier();
			b.tryRemoveIdentifier();
			if(!c.containsIdentifier(next))
				c.add(next);
		}
		return c;
	}
	
	public IdentifierSet difference(IdentifierSet other) {
		IdentifierSet a, c; //a is copy of this		c is empty
		a = new IdentifierSet(this);
		c = new IdentifierSet();
		while(a.getSize() > 0){
			Identifier next = a.getIdentifier();
			a.tryRemoveIdentifier();
			if(!other.containsIdentifier(next))
				c.add(next);
		}
		return c;
	}
	
	public IdentifierSet intersection(IdentifierSet other) {
		IdentifierSet a, c;	//a is copy of this		c is empty
		a = new IdentifierSet(this);
		c = new IdentifierSet();
		while(a.getSize() > 0){
			Identifier next = a.getIdentifier();
			a.tryRemoveIdentifier();
			if(other.containsIdentifier(next))
				c.add(next);
		}
		return c;
	}
	
	public IdentifierSet symmetricDifference(IdentifierSet other) {
		IdentifierSet a, b, c;	//a is copy of this		b is copy of other		c is empty
		a = new IdentifierSet(this);
		b = new IdentifierSet(other);
		c = new IdentifierSet();
		while(a.getSize() > 0){
			Identifier next = a.getIdentifier();
			a.tryRemoveIdentifier();
			if(!b.containsIdentifier(next))
				c.add(next);
		}
		while(b.getSize() > 0){
			Identifier next = b.getIdentifier();
			b.tryRemoveIdentifier();
			if(!a.containsIdentifier(next))
				c.add(next);
		}
		return c;
	}
	
	//GETTERS & CHECKS
	
	public Identifier getIdentifier() {
		return (Identifier) identifiers[0];
	}
	
	public int getSize() {
		return numberOfIdentifiers;
	}
	
	public boolean containsIdentifier(IdentifierInterface thisIdentifier) {
		for(int i = 0; i < numberOfIdentifiers; i++)
			if(identifiers[i].equals(thisIdentifier))
				return true;
		return false;
	}
}
