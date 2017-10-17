package Methods;

public class AttributeRow {
	StringRow attributes = new StringRow();
	IntRow value = new IntRow();
	public static int PRINTABLE_BUFFER_SPACES = 4;
	
	public AttributeRow(){
	}
	
	//adds new empty attribute if it isnt already listed
	public void add(String attribute){
		if(!attributes.contains(attribute)){
			attributes.add(attribute);
			value.add(0);
		}
	}
	public void add(String attribute, int amount){
		if(attributes.indexOf(attribute) > -1){
			value.row[attributes.indexOf(attribute)] += amount;
		} else {
			attributes.add(attribute);
			value.add(amount);			
		}
	}
	
	public void addMany(String... attArr){
		for(int i = 0; i < attArr.length; i++){
			add(attArr[i]);
		}
	}
	
	public String printableList(){
		String printable = "";
		int entryBuffer;
		
		for(int i = 0; i < attributes.numStrings; i++){
			entryBuffer = attributes.longestEntryLength() - attributes.row[i].length()
				+ PRINTABLE_BUFFER_SPACES;
			printable += attributes.row[i] + Fun.chars(' ', entryBuffer) + value.row[i] + "\n";
		}
		return printable;
	}
}
