package akinator;

public class EntryRow {
	private Entry[] row;
	private int numberOfEntries;
	
	public EntryRow(int numMaxEntries){
		row = new Entry[numMaxEntries];
		numberOfEntries = 0;
	}
	
	public void add(Entry ent){
		row[numberOfEntries] = ent;
		numberOfEntries++;
	}
	
	public void remove(int index){
		for(int i = index; i < numberOfEntries-1; i++){
			row[i] = row[i+1];
		}
		numberOfEntries--;
	}
	
	public Entry getEntry(int index){
		return row[index];
	}
	
	public int getNumberOfEntries(){
		return numberOfEntries;
	}
	
	public boolean areUnique(Entry a, Entry b){
		int numAnswers = a.getNumberOfAnswers();
		for(int i = 0; i < numAnswers; i++){
			if(a.getAnswerAt(i) != b.getAnswerAt(i))
				return true;
		}
		return false;
	}
	
	public int getNumMaxEntries(){
		return row.length;
	}
	
	public void giveEntryThisNewBool(int indexOfEntry, boolean value){
		row[indexOfEntry].giveNewAnswer(value);
	}
	
	public boolean hasIndistinguishableEntries(){
		EntryVector ev = null;
		for(int i = 0; i < numberOfEntries-1; i++){
			for(int j = i+1; j < numberOfEntries; j++){
				if(!areUnique(row[i], row[j])){
					return true;
				}
			}
		}
		return false;
	}
	
	public EntryVector getTwoIndistinguishableEntries(){
		EntryVector ev = null;
		for(int i = 0; i < numberOfEntries-1; i++){
			for(int j = i+1; j < numberOfEntries; j++){
				if(!areUnique(row[i], row[j])){
					ev = new EntryVector(row[i], row[j]);
				}
			}
		}
		return ev;
	}
	
	public EntryRow giveCopy(){
		EntryRow copy = new EntryRow(row.length);
		for(int i = 0; i < numberOfEntries; i++)
			copy.add(row[i]);
		return copy;
	}
	
	public void removeThoseWithout(int index, boolean value){
		for(int i = 0; i < numberOfEntries; i++){
			if(row[i].getAnswerAt(index) != value){
				remove(i);
				i--;
			}
		}
	}
	
	public int disputedColumn(){
		int numAnswers = row[0].getNumberOfAnswers(),
			index = (int) (Math.random()*(1.0*numAnswers)),
			timeout = numAnswers+1;
		do{
			
			if(!allAgreeOnColumn(index))
				return index;
			index++;
			if(index >= numAnswers)
				index = 0;
			timeout--;
		}while(timeout > 0);
		return Integer.MIN_VALUE;
	}
	
	private boolean allAgreeOnColumn(int column){
		if(numberOfEntries < 1)
			return true;
		boolean value = row[0].getAnswerAt(column);
		for(int i = 1; i < numberOfEntries; i++){
			if(row[i].getAnswerAt(column) != value)
				return false;
		}
		return true;
	}
}
