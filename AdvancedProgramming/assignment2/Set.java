package assignment2;

public class Set<E extends Data> implements SetInterface<E>{
	private List<E> ls;
	
	public Set(){
		ls = new List<>();
	}

	public void add(E x){
		ls.insert(x);
	}
	
	public E getElement(){
		return ls.retrieve();
	}
	
	public boolean contains(E x){
		return ls.find(x);
	}
	
	public void removeElement(E e){
		ls.find(e);
		ls.remove();
	}
	
	public int getSize(){
		return ls.size();
	}
	
	//OPERATIONS

	public SetInterface<E> union(SetInterface<E> other) {//+
		SetInterface<E> b, c;
		b = other.clone();
		c = clone();
		while(b.getSize() > 0){
			E next = (E) b.getElement();
			b.removeElement(next);
			if(!c.contains(next))
				c.add(next);
		}
		return c;
	}
	
	public SetInterface<E> complement(SetInterface<E> other) {//-
		SetInterface<E> a, c;
		a = clone();
		c = new Set<>();
		while(a.getSize() > 0){
			E next = a.getElement();
			a.removeElement(next);
			if(!other.contains(next))
				c.add(next);
		}
		return c;
	}
	
	public SetInterface<E> intersection(SetInterface<E> other) {// *
		SetInterface<E> a, c;
		a = clone();
		c = new Set<>();
		while(a.getSize() > 0){
			E next = a.getElement();
			a.removeElement(next);
			if(other.contains(next))
				c.add(next);
		}
		return c;
	}
	
	public SetInterface<E> symmetricDifference(SetInterface<E> other) {//|
		SetInterface<E> a, b, c;
		a = clone();
		b = other.clone();
		c = new Set<>();
		while(a.getSize() > 0){
			E next = a.getElement();
			a.removeElement(next);
			if(!other.contains(next))
				c.add(next);
		}
		while(b.getSize() > 0){
			E next = b.getElement();
			b.removeElement(next);
			if(!contains(next))
				c.add(next);
		}
		return c;
	}

	@Override
	public SetInterface<E> clone() {
		Set<E> copy = new Set<>();
		copy.ls = ls.clone();
		return copy;
	}

	@Override
	public boolean equals(SetInterface<E> other) {
		return symmetricDifference(other).getSize() == 0;
	}
}
