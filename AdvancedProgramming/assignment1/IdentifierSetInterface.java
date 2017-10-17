package assignment1;

/** ADT for the class IdentifierSet.
*
* @author Christopher Esterhuyse & Bertan Konuralp
* @elements
*    identifiers of the type Identifier
* @structure 
*	none
* @domain
*	min 0 to MAX_ARRAY_SIZE identifiers
* @constructor
*	IdentifierSet();
*	    <dl>
*		<dt><b>PRE-condition</b><dd>-
*		<dt><b>POST-condition</b><dd>The new
*		IdentifierRow-object contains an empty set of Identifiers
*	    </dl>
*	<br>
*	IdentifierSet(IdentifierSet original);
*	    <dl>
*		<dt><b>PRE-condition</b><dd>-
*		<dt><b>POST-condition</b><dd>The new
*		IdentifierSet-object contains copies of the Identifiers in "original"
*	    </dl>
**/
public interface IdentifierSetInterface {
public static int MAX_ARRAY_SIZE = 20;
	
	/** Initializes the IdentifierSet object to the empty set of Identifiers.
	 * @precondition
	 *	    -
	 * @postcondition
	 *	    There are no Identifier objects in this set.
	 **/
	public void init();
	
	/** Returns the number of Identifiers in this set.
     * @precondition
     *	    -
     * @postcondition
     *	    The number of Identifiers is returned
     **/
	public int getSize();
	
	/** Returns the Identifier most recently added to the set.
     * @precondition
     *	    There is at least one identifier in the set
     * @postcondition
     *	    The Identifier most recently added to the set is returned
     **/
	public Identifier getIdentifier();
	
	/** Removes an Identifier most recently added to the set from the set
     * @precondition
     *	    -
     * @postcondition
     *	   If there is an Identifier most recently added to the set, it is removed from the set.
     **/
	public void tryRemoveIdentifier();
	
	/** Adds a given Identifier to the end of the set
     * @precondition
     *	    -
     * @postcondition
     *	    The row contains the given Identifier in the end position
     *		and the set is 1 element longer
     **/
	public void add(Identifier newIi);
	
	/** Returns a new IdentifierSet that is the Union of this and the given IdentifierSet
     * @precondition
     *	    -
     * @postcondition
     *	    A new IdentifierSet is returned, containing the Identifiers of this
     *		IdentifierSet, as well as Identifiers of the given IdentifierSet.
     **/
	public IdentifierSet union(IdentifierSet b);
	
	/** Returns a new IdentifierSet that is the Difference of this and the given IdentifierSet
     * @precondition
     *	    -
     * @postcondition
     *	    A new IdentifierSet is returned, containing the Identifiers of this
     *		IdentifierSet, but without any Identifiers found in the given IdentifierSet
     **/
	public IdentifierSet difference(IdentifierSet b);
	
	/** Returns a new IdentifierSet that is the Intersection of this and the given IdentifierSet
     * @precondition
     *	    -
     * @postcondition
     *	    A new IdentifierSet is returned, containing any Identifiers found in
     *		both this and the given IdentifierSets.
     **/
	public IdentifierSet intersection(IdentifierSet b);
	
	/** Returns a new IdentifierSet that is the Symmetric Difference of this and the
	 * given IdentifierSet
     * @precondition
     *	    -
     * @postcondition
     *	    A new IdentifierSet is returned, containing the Identifiers in either this or the
     *		given IdentifierSets, but with Identifiers they have in common omitted.
     **/
	public IdentifierSet symmetricDifference(IdentifierSet b);
	
	/** Checks if the given Identifier has the same ID as the given Identifier
     * @precondition
     *	    -
     * @postcondition
     *	    TRUE:	the row contains an Identifier with the same ID as the given Identifier
     *		FALSE:	the row does not contain any Identifiers with the same ID as the given Identifier
     **/
	public boolean containsIdentifier(IdentifierInterface thisIdentifier);
}
