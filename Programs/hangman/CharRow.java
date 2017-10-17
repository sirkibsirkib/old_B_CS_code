package hangman;

public class CharRow {
	char[] row;
	int numElements = 0;
	
	CharRow(int size){
		row = new char[size];
	}
	
	boolean contains(char c){
		for(int i = 0 ; i < numElements; i++){
			if(row[i] == c){
				return true;
			}
		}
		return false;
	}
	
	void add(char c){
		row[numElements] = c;
		numElements++;
	}
	
	void deleteAll(char c){
		for(int i = 0; i < numElements; i++){
			if(row[i] == c){
				deleteAtIndex(i);
			}
		}
	}
	
	void deleteAtIndex(int index){
		for(int i = index; i < numElements-1; i++){
			row[i] = row[i+1];
		}
		numElements--;
	}
	
	String list(){
		String list = "";
		for(int i = 0; i < numElements; i++){
			list += row[i];
			if(i < numElements-1){
				list += ", ";
			}
		}
		return list;
	}
}
