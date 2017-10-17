package assignment2;

public class NaturalNumber implements NaturalNumberInterface{
	private StringBuilder num;
	
	public NaturalNumber(char c){
		init(c);
	}
	
	public void init(char c){
		num = new StringBuilder();
		num.append(c);
	}
	
	public char getDigitAt(int index){
		return num.charAt(index);
	}
	
	public void append(char c){
		if(numberLength() == 1 && num.charAt(0) == '0')
			init(c);
		else
			num.append(c);
	}
	
	public NaturalNumberInterface clone(){
		NaturalNumberInterface copy = new NaturalNumber(num.charAt(0));
		for(int i = 1; i < numberLength(); i++)
			copy.append(num.charAt(i));
		return copy;
	}
	
	public int numberLength() {
		return num.length();
	}

	public boolean equals(NaturalNumberInterface other){
		NaturalNumber otherNN = (NaturalNumber)other;
		if(numberLength() != otherNN.numberLength())
			return false;
		for(int i = 0; i < numberLength(); i++)
			if(getDigitAt(i) != otherNN.getDigitAt(i))
				return false;
		return true;
	}

	@Override
	public int compareTo(Object other) {
		if(!(other instanceof NaturalNumberInterface))
			throw new Error();
		NaturalNumber otherNN = (NaturalNumber) other;
		if(numberLength() < otherNN.numberLength())		return -1;
		if(numberLength() > otherNN.numberLength())		return 1;
		for(int i = 0; i < numberLength(); i++){
			if(getDigitAt(i) < otherNN.getDigitAt(i))	return -1;
			if(getDigitAt(i) > otherNN.getDigitAt(i))	return 1;
		}
		return 0;
	}
	
	public void incriment() {
		for(int i = num.length()-1; i >= 0; i--){
			char c = num.charAt(i);
			if(c < '9'){
				num.setCharAt(i, (char) (c + 1));
				return;
			}else{
				num.setCharAt(i, '0');
			}
		}
		num.insert(0, '1');
	}
}
