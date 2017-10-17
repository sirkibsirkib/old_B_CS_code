package assignment3;

import java.util.Iterator;

/** ADT for the class BinarySearchTree.
*
* @author Christopher Esterhuyse & Bertan Konuralp
* @elements
*	Nodes with data of type E
* @structure 
*	tree
* @domain
*	Any valid E elements
* @constructor
*	BinarySearchTree(){;
*	    <dl>
*		<dt><b>PRE-condition</b><dd>-
*		<dt><b>POST-condition</b><dd>
*			There is a new Binary Search tree with no elements
*	    </dl>
*	<br>
**/
public interface BinarySearchTreeInterface<E extends Comparable>{
	
	/** Returns whether or not the tree contains an element with the same value as the given value
	 * @precondition
	 *	    -
	 * @postcondition
	 *		TRUE:	The tree contains an element with equal value as the given element
	 *		FALSE:	The tree does not contain an element with equal value as the given element
	 **/
	boolean contains(E e);
	
	/** Inserts the given element into the tree
	 * @precondition
	 *	    -
	 * @postcondition
	 *		The tree contains the given element
	 **/
	void insert(E e);
	
	/** Removes an element with equivalent value to the given element, from the tree
	 * @precondition
	 *	    The tree contains the given element
	 * @postcondition
	 *		The tree has one less element with the given value
	 **/
	void remove(E e);
	
	/** Returns the number of data nodes in the tree
	 * @precondition
	 *	    -
	 * @postcondition
	 *		The number of data nodes in the tree is returned
	 **/
	int numberOfNodes();
	
	/** Returns the height of the tree
	 * @precondition
	 *	    -
	 * @postcondition
	 *		The height of the tree is returned
	 **/
	int height();
	
	/** Returns the element in the tree with the smallest value
	 * @precondition
	 *	    -
	 * @postcondition
	 *		The element in the tree with the smallest value is returned
	 **/
	E smallest();
	
	/** Returns the element in the tree with the largest value
	 * @precondition
	 *	    -
	 * @postcondition
	 *		The element in the tree with the largest value is returned
	 **/
	E largest();
	
	/** Returns a copy of the tree
	 * @precondition
	 *	    -
	 * @postcondition
	 *		A structural copy of the tree is returned
	 **/
	BinarySearchTreeInterface<E> copy();
	
	/** Returns an iterator of type E
	 * @precondition
	 *	    -
	 * @postcondition
	 * The data stored in the binary search tree was iterated in
	 * monotonically non-decreasing order and was added in this
	 * order to an object of the type Iterator<E>.
	 * This object of the type Iterator<E> was subsequently
	 * returned.
	**/
	Iterator<E> ascendingIterator ();
	
	/** Returns an iterator of type E
	 * @precondition
	 *	    -
	 * @postcondition
	 * The data stored in the binary search tree was iterated in
	 * monotonically non-increasing order and was added in this
	 * order to an object of the type Iterator<E>.
	 * This object of the type Iterator<E> was subsequently
	 * returned.
	**/
	Iterator<E> descendingIterator ();
}
