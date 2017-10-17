package Methods;

public class IntRow {
	//globals
	public static int PRINTABLE_BUFFER_SPACES = 4;
	int[] row;
	int numInts = 0,
		maxSize = 4;
	
	//default constructor
	public IntRow(){
		row = new int[maxSize];
	}
	
	//constructor with given initial max size
	public IntRow(int maxSize){
		this.maxSize = maxSize;
		row = new int[maxSize];
	}
	
	//adds an entry
	public void add(int x){
		if(numInts >= maxSize - 1){
			doubleMax();
		}
		row[numInts] = x;
		numInts++;
	}
	
	//adds many entries at once
	public void addMany(int... x){
		for(int i = 0; i < x.length; i++){
			add(x[i]);
		}
	}
	
	//grows array to accomodate a new entry
	private void doubleMax(){
		maxSize = maxSize*2;
		int[] biggerRow = new int[maxSize];
		
		for(int i = 0; i < numInts; i++){
			biggerRow[i] = row[i];
		}
		row = biggerRow;
	}
	
	//removes an element at an index
	public void removeElementAtIndex(int index){
		for(int i = 0; i < index-1; i++){
			row[i] = row[i+1];
		}
		numInts--;
		if(numInts < (int)(maxSize/4) && maxSize > 8){
			halveMax();
		}
	}
	
	//shrinks array when 3/4 of the space isn't used
	private void halveMax(){
		maxSize = (int)(maxSize/2);
		int[] smallerRow = new int[maxSize];
		
		for(int i = 0; i < numInts; i++){
			smallerRow[i] = row[i];
		}
		row = smallerRow;
	}
	
	//rearranges entries in accordance to a chosen sorting algorithm
	public void asciiSort(String algorithm){
		trimArrayUnknowns();
		algorithm = Fun.textMinimize(algorithm);
		switch(algorithm){
		case "bubblesort":	row = Sort.bubbleSort(row); break;
		}
	}
	
	//resizes array to exactly the currently used size
	private void trimArrayUnknowns(){
		int[] trimmed = new int[numInts];
		
		for(int i = 0; i < numInts; i++){
			trimmed[i] = row[i];
		}
		maxSize = numInts;
		row = trimmed;
	}
	
	//returns a single string of the entire list for debug purposes
	public String printableList(){
		String printable = "";
		for(int i = 0; i < numInts; i++){
			printable += row[i] + "\n";
		}
		return printable;
	}
}
