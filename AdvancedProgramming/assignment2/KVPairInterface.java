package assignment2;

/**	@elements : Values of type E	
 * 				Keys of type Identifier
 *	@structure : none
 *	@domain : 	Any clonable object can be a value
 *				Any valid Identifier can be a key
 *	@constructor - KVPair(IdentifierInterface key);
 *	<dl>
 *		<dt><b>PRE-conditie</b><dd>		-
 *		<dt><b>POST-conditie</b><dd> 	The key value pair has the given identifier with a NULL value
 * </dl>
 * 
 * @constructor - KVPair(IdentifierInterface key, E value)
 *	<dl>
 *		<dt><b>PRE-conditie</b><dd>		-
 *		<dt><b>POST-conditie</b><dd> 	The key value pair has the given identifier with the given value
 * </dl>
 **/
public interface KVPairInterface<E extends Clonable<E>> extends Data {

	
	/** A deep copy of the key value pair is returned
	 *	@precondition -
	 *  @postcondition - A deep copy of the key value pair is returned
	 **/
	public KVPairInterface<E> clone();
	
	/** return this key value pair's identifier
	 *	@precondition -
	 *  @postcondition - the identifier for this key value pair is returned
	 **/
	public IdentifierInterface getIdentifier();
	
	/** return this key value pair's value
	 *	@precondition -
	 *  @postcondition - the value for this key value pair is returned
	 **/
	public E getValue();
	
	/** compares the Identifier of this key value pair to the given key value pair's identifier
	 *	@precondition -
	 *  @postcondition
	 *	    returns an integer value determined by the ordering of this key value pair's
	 *		Identifier and the Identifier of the given key value pair
	 * 		 this = given:	returns 0
	 * 		 this < given:	returns -1
	 * 		 this > given:	returns 1
	 **/
	public int compareTo(Object other);
}
