package primativeRows;


public class Row<E extends Comparable> {
	private E[] elements;
	private int numberOfElements;
	
	@SuppressWarnings("unchecked")
	public Row(){
		elements = (E[]) new Comparable[4];
		numberOfElements = 0;
	}
	
	public void add(E e){
		if(numberOfElements >= elements.length)
			doubleArraySize();
		elements[numberOfElements] = e;
		numberOfElements++;
	}
	
	public E getElement(int index){
		return elements[index];
	}
	
	public int getNumberOfElements(){
		return numberOfElements;
	}

	private void doubleArraySize() {
		@SuppressWarnings("unchecked")
		E[] elements2 = (E[]) new Comparable[elements.length*2];
		for(int i = 0; i < numberOfElements; i++)
			elements2[i] = elements[i];
		elements = elements2;
	}
	
	@SuppressWarnings("unchecked")
	public E findSameAs(E e){
		for(int i = 0; i < numberOfElements; i++)
			if(elements[i].compareTo(e) == 0)
				return elements[i];
		return null;
	}

	public void remove(E query) {
		for(int i = 0; i < numberOfElements; i++){
			if(query.compareTo(elements[i]) == 0)
				removeAt(i);
		}
	}

	public void removeAt(int index) {
		if(numberOfElements < elements.length/2 && numberOfElements > 4)
			halveArraySize();
		for(int i = index; i < numberOfElements-1; i++)
			elements[i] = elements[i+1];
		numberOfElements--;
	}

	private void halveArraySize() {
		@SuppressWarnings("unchecked")
		E[] elements2 = (E[]) new Comparable[elements.length/2];
		for(int i = 0; i < numberOfElements; i++)
			elements2[i] = elements[i];
		elements = elements2;
	}

	public boolean contains(E query) {
		for(int i = 0; i < numberOfElements; i++){
			if(query.compareTo(elements[i]) == 0)
				return true;
		}
		return false;
	}
}
