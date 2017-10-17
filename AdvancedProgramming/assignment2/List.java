package assignment2;

public class List<E extends Data> implements ListInterface<E>{
	private Node head, current, tail;
	
	public List(){
		init();
	}
	
	public boolean isEmpty() {
		return head == null;
	}

	public List<E> init() {
		head = current = tail = null;
		return this;
	}

	public int size() {
		Node h = head;
		int count = 0;
		while(h != null){
			h = h.next;
			count++;
		}
		return count;
	}

	public List<E> insert(E d) {
		Node b = new Node(d);
		if(head == null){//case: list empty
			head = current = tail = b;
			return this;
		}
		if(d.compareTo(head.data) == -1)//case: new element is smallest 
			return insertInFront(b);
		Node a = findLastNodeWithSmallerData(b, head),
			c = a.next;
		a.next = b;	
		b.next = c;
		b.prior = a;
		if(c != null)
			c.prior = b;
		if(a == tail)
			tail = b;
		return this;
	}
	
	private Node findLastNodeWithSmallerData(Node dataNode, Node after){
		if(after.next == null)
			return after;
		if(dataNode.data.compareTo(after.next.data) == -1)
			return after;
		return findLastNodeWithSmallerData(dataNode, after.next);
	}
	
	private List<E> insertInFront(Node b){
		head.prior = b;
		b.next = head;
		head = b;
		return this;
	}

	public E retrieve() {
		if(current == null)
			return null;
		return (E) current.data;
	}

	public List<E> remove() {
		Node prv, nxt;
		prv = current.prior;
		nxt = current.next;
		if(prv == null && nxt == null){
			head = current = null;
			return this;
		}
		if(prv != null){
			prv.next = nxt;
			current = prv;
		}else{
			head = nxt;
		}
		if(nxt != null){
			nxt.prior = prv;
			current = nxt;
		}
		return this;
	}

	public boolean find(E d) {
		goToFirst();
		if(current == null)
			return false;
		while(d.compareTo(current.data) != 0 && current.next != null)
			goToNext();
		return current.data.compareTo(d) == 0;
	}

	public boolean goToFirst() {
		current = head;
		return head != null;
	}

	public boolean goToLast() {
		current = tail;
		return head != null;
	}

	public boolean goToNext() {
		if(current != null && current.next != null){
			current = current.next;
			return true;
		}
		return false;	
	}

	public boolean goToPrevious() {
		if(current != null && current.prior != null){
			current = current.prior;
			return true;
		}
		return false;
	}

	@Override
	public List<E> clone() {
		List<E> clone = new List<>();
		Node placeHolder = current;
		
		goToFirst();
		Node n = current;
		while(n != null){
			clone.insert((E) n.data.clone());
			n = n.next;
		}
		current = placeHolder;
		
		return clone;
	}
	
	class Node{
		E data;
	    Node prior,
	         next;

	    public Node(E d) {
	        this(d, null, null);
	    }

	    public Node(E data, Node prior, Node next) {
	        this.data = data == null ? null : data;
	        this.prior = prior;
	        this.next = next;
	    }
	}
}
