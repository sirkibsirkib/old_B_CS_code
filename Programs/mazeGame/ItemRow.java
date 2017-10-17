package mazeGame;

public class ItemRow {
	int maxItems = 4;
	int numItems = 0;
	Item[] items;
	Item lHand, rHand;
	
	ItemRow(){
		items = new Item[maxItems];
	}
	
	ItemRow(int size){
		maxItems = size;
		items = new Item[size];
	}
	
	//adds a new item
	Item add(Item w){
		if(numItems == maxItems -1){
			doubleMaxSize();
		}
		items[numItems] = w;
		numItems++;
		return items[numItems];
	}
	
	Item add(int x, int y, String s){
		Item w = new Item(x, y, s);
		w = add(w);
		return w;
	}
	
	void doubleMaxSize(){
		int newMax = maxItems*2;
		
		Item[] moreItems = new Item[newMax];
		for(int i = 0; i < numItems; i++){
			moreItems[i] = items[i];
		}
		maxItems = newMax;
		items = moreItems;
	}
	
	void halveMaxSize(){
		int newMax = (int)(maxItems/2);
				
		Item[] lessItems = new Item[newMax];
		for(int i = 0; i < numItems; i++){
			lessItems[i] = items[i];
		}
		maxItems = newMax;
		items = lessItems;
	}
	
	//removes an entry from the item list and re-organizes list
	void removeItem(int n){
		while(n < numItems - 1){
			items[n] = items[n+1];
			n ++;
		}
		items[n] = null;
		numItems--;
		
		if(numItems < (int)(maxItems/2) && maxItems > 4){
			halveMaxSize();
		}
	}
	
	String printableNumberedList(){
		String s = "";
		for(int i = 0; i < numItems; i++){
			s += "(" + i + ")\t" + items[i].name;
			if(i < numItems - 1){s += "\n";}
		}
		return s;
	}
}
