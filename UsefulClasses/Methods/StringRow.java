package Methods;

public class StringRow {
	//globals
	public static int PRINTABLE_BUFFER_SPACES = 4;
	public int maxSize = 4,
		numStrings = 0;
	public String[] row;
	
	//default constructor
	public StringRow(){
		row = new String[maxSize];
	}
	
	//constructor with given max size
	public StringRow(int maxSize){
		this.maxSize = maxSize;
		row = new String[maxSize];
	}
	
	//returns true if array has a string matching input string
	public boolean contains(String query){
		for(int i = 0; i < numStrings; i++){
			if(row[i].equals(query)){
				return true;
			}
		}
		return false;
	}
	
	//adds an entry
	public void add(String s){
		if(numStrings >= maxSize - 1){
			doubleMax();
		}
		row[numStrings] = s;
		numStrings++;
	}
	
	//adds several entries
	public void addMany(String... list){
		for(int i = 0; i < list.length; i++){
			add(list[i]);
		}
	}
	
	//removes all strings that match input string 
	public void remove(String s){
		for(int i = 0; i < numStrings; i++){
			if(row[i].equals(s)){
				numStrings--;
				for(int j = i; j < numStrings; j++){
					row[j] = row[j+1];
				}
			}
		}
		if(numStrings < (int)(maxSize/2) && maxSize > 8){
			halveMax();
		}
	}
	
	//removes the entry at index position
	public void remove(int index){
		for(int i = index; i < numStrings-1; i++){
			row[i] = row[i+1];
		}
		numStrings--;
		if(numStrings < (int)(maxSize/4) && maxSize > 8){
			halveMax();
		}
	}
	
	//doubles max size to accommodate a new entry
	private void doubleMax(){
		maxSize = maxSize*2;
		String[] biggerRow = new String[maxSize];
		
		for(int i = 0; i < numStrings; i++){
			biggerRow[i] = row[i];
		}
		row = biggerRow;
	}
	
	//halves max entry size in response to 3/4 spaces going to waste
	private void halveMax(){
		maxSize = (int)(maxSize/2);
		String[] smallerRow = new String[maxSize];
		
		for(int i = 0; i < numStrings; i++){
			smallerRow[i] = row[i];
		}
		row = smallerRow;
	}
	
	//returns true if this string row has no content
	public boolean isEmpty(){
		return numStrings == 0;
	}
	
	//return the string value of a random entry
	public String pickRandom(){
		return row[Fun.rng(numStrings)];
	}
	
	//resizes the array and eliminates all other values from memory
	private void trimArrayUnknowns(){
		String[] trimmed = new String[numStrings];
		
		for(int i = 0; i < numStrings; i++){
			trimmed[i] = row[i];
		}
		maxSize = numStrings;
		row = trimmed;
	}
	
	//adds all elements of another string row to this one
	public void addStringRow(StringRow s){
		for(int i = 0; i < s.numStrings; i++){
			add(s.row[i]);
		}
	}
	
	//adds elements of another stringrow, but only partially
	public void addStringRowPartition(StringRow s, int firstIndex, int lastIndex){
		for(int i = firstIndex; i <= lastIndex; i++){
			add(s.row[i]);
		}
	}
	
	//move a value at in index position to a new position
	public void moveValue(int fromIndex, int toIndex){
		String hold = row[fromIndex];
		if(fromIndex < toIndex){
			for(int i = fromIndex; i < toIndex; i++){
				row[i] = row[i+1];
			}
		}else{
			for(int i = fromIndex; i > toIndex; i--){
				row[i] = row[i-1];
			}
		}
		row[toIndex] = hold;
	}
	
	//shuffles the order of entries
	public void shuffle(){
		int copyNumStrings = numStrings;
		String[] copy = new String[copyNumStrings];
		int removeThis;
		
		for(int i = 0; i < copyNumStrings; i++){
			removeThis = Fun.rng(numStrings);
			copy[i] = row[removeThis];
			remove(removeThis);
		}
		row = copy;	
		numStrings = copyNumStrings;
	}
	
	//sorts all entries using a specified sorting algorithm
	public void asciiSort(String algorithm){
		trimArrayUnknowns();
		algorithm = Fun.textMinimize(algorithm).replace("sort", "");
		switch(algorithm){
		case "merge":	row = Sort.mergeSort(row); break;
		case "bubble":	row = Sort.bubbleSort(row); break;
		case "quick":	row = Sort.quickSort(row); break;
		case "bogus":	row = Sort.bogusSort(row); break;
		}
	}
	
	//returns a single string of all entries on their own line for debug
	public String printableList(){
		String result = "";
		for(int i = 0; i < numStrings; i++){
			result += row[i] + "\n";
		}
		return result;
	}
	
	//returns the length of the longest entry
	public int longestEntryLength(){
		int result = 0;
		for(int i = 0; i < numStrings; i++){
			if(row[i].length() > result){
				result = row[i].length();
			}
		}
		return result;
	}
	
	//returns an int of the first index position containing input
	//string, and -1 if none is found
	public int indexOf(String s){
		for(int i = 0; i < numStrings; i++){
			if(row[i].equals(s)){
				return i;
			}
		}
		return -1;
	}
	
	//empties all elements and resets all values.
	public void clear(){
		maxSize = 4;
		String[] empty = new String[maxSize];
		row = empty;
		numStrings = 0;
	}
	
	//fills the row with values for debug. entryLength and optionsPerChar'
	//allow the user to further customize the initial contents
	public void populateForDebug(int entryLength, int optionsPerChar){
		clear();
		switch(entryLength){
			case 1:{
				for(int i = 0; i < optionsPerChar; i++){
					add("" + (char)('a'+i));
				}
				break;
			}
			case 2:{
				for(int i = 0; i < optionsPerChar; i++){
					for(int j = 0; j < optionsPerChar; j++){
						add("" + (char)('a'+i) + (char)('a'+j));
					}
				}
				break;
			}
			case 3:{
				for(int i = 0; i < optionsPerChar; i++){
					for(int j = 0; j < optionsPerChar; j++){
						for(int k = 0; k < optionsPerChar; k++){
							add("" + (char)('a'+i) + (char)('a'+j) + (char)('a'+k));
						}
					}
				}
				break;
			}
		}
	}
}
