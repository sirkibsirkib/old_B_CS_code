package assignment2;

	public class KVPair<E extends Clonable<E>> implements KVPairInterface<E>{
	private IdentifierInterface key;
	private E value;
	
	public KVPair(IdentifierInterface key){
		this(key, null);
	}
	
	public KVPair(IdentifierInterface key, E value){
		this.key = key;
		this.value = value;
	}
	
	@Override
	public KVPairInterface<E> clone() {
		KVPair<E> kvNew = new KVPair<E>(key.clone(), (E) value.clone());
		return kvNew;
	}
	
	public IdentifierInterface getIdentifier(){
		return key;
	}
	
	public E getValue(){
		return value;
	}
	
	@Override
	public int compareTo(Object other) {
		if(!(other instanceof KVPair))
			throw new Error();
		KVPair<E> otherKVPair = (KVPair<E>) other;
		return key.compareTo(otherKVPair.key);
	}
}
