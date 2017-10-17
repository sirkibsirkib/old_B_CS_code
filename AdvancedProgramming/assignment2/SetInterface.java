package assignment2;

/** ADT for the class Set.
*
* @author Christopher Esterhuyse & Bertan Konuralp
* @elements
*	elements of type E
* @structure 
*	-
* @domain
*	Any set of unique elements
* @constructor
*	Set(){;
*	    <dl>
*		<dt><b>PRE-condition</b><dd>-
*		<dt><b>POST-condition</b><dd>
*		The object is an empty set of elements
*	    </dl>
**/

public interface SetInterface<E extends Data> extends Clonable<SetInterface<E>>{
	
	/** Adds the given element to this set if it's not already in the set
	 * @precondition
	 *	    -
	 * @postcondition
	 *	    if set already contains given element
	 * 		 TRUE:	does nothing
	 * 	   	 FALSE:	adds the given element to the set
	 **/
	void add(E x);
	
	/** Returns one of the set elements
	 * @precondition
	 *	    -
	 * @postcondition
	 *	    if set is empty
	 * 		 TRUE:	returns NULL
	 * 		 FALSE:	returns an element from the set
	 **/
	E getElement();
	
	/** Checks if the given element is in the set
	 * @precondition
	 *	    -
	 * @postcondition
	 *	    TRUE:	element is in the set
	 * 		FALSE:	element is not in the set
	 **/
	boolean contains(E x);
	
	/** Removes one of the set elements
	 * @precondition
	 *	    -
	 * @postcondition
	 * 	   if set is empty
	 *	    TRUE:	does nothing
	 * 		FALSE:	removes an element
	 **/
	void removeElement(E e);
	
	/** Returns the size of the set
	 * @precondition
	 *	    -
	 * @postcondition
	 *	    The size of the set is returned
	 **/
	int getSize();

	/** Returns the union of this set and the given set
	 * @precondition
	 *	    -
	 * @postcondition
	 *	    The union of this and the given set is returned
	 * 		the union of A and B: all elements in either A or B
	 **/
	SetInterface<E> union(SetInterface<E> other);
	
	/** Returns the complement of this set and the given set
	 * @precondition
	 *	    -
	 * @postcondition
	 *	    The complement of this and the given set is returned
	 * 		the complement of A and B: all elements in A but not in B
	 **/
	SetInterface<E> complement(SetInterface<E> other);
	
	/** Returns the intersection of this set and the given set
	 * @precondition
	 *	    -
	 * @postcondition
	 *	    The intersection of this and the given set is returned
	 * 		the intersection of A and B: all elements that are each in both A and B
	 **/
	SetInterface<E> intersection(SetInterface<E> right);
	
	/** Returns the symmetric difference of this set and the given set
	 * @precondition
	 *	    -
	 * @postcondition
	 *	    The symmetric difference of this and the given set is returned
	 * 		the symmetric difference of A and B: all elements in A or B, but not in both A and B
	 **/
	SetInterface<E> symmetricDifference(SetInterface<E> other);

	/** Returns a deep copy of the set
	 * @precondition
	 *	    -
	 * @postcondition
	 *	    A deep copy of the set is returned
	 **/
	SetInterface<E> clone();
	
	/** Returns whether or not this and the given set are equal
	 * @precondition
	 *	    -
	 * @postcondition
	 *	    TRUE:	This and the given set have have exactly the equivalent elements
	 *		FALSE:	This and the given set do not have exactly the equivalent elements
	 **/
	boolean equals(SetInterface<E> other);
}
