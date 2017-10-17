package assignment2;

/** ADT for the class Identifier.
*
* @author Christopher Esterhuyse & Bertan Konuralp
* @elements
*	Characters of type Char
* @structure 
*	linear
* @domain
*	Any nonempty sequence of alphanumeric Characters, with the first character being a letter
* @constructor
*	Identifier(char c){;
*	    <dl>
*		<dt><b>PRE-condition</b><dd>-
*		<dt><b>POST-condition</b><dd>The new
*		Identifier-object has a new id matching the given character
*	    </dl>
*	<br>
*	Identifier(Identifier original);
*	    <dl>
*		<dt><b>PRE-condition</b><dd>-
*		<dt><b>POST-condition</b><dd>The new
*		Identifier-object has the same ID as the original Identifier
*	    </dl>
**/
public interface IdentifierInterface {
	
	/** Initializes the Identifier object to have the given id.
	 * @precondition
	 *	    -
	 * @postcondition
	 *	    The Identifier has the given Character as id
	 **/
	void init(char id);
	
	/** returns whether or not the given Object is an Identifier with the same id as this one
	 * @precondition
	 *	    -
	 * @postcondition
	 *		TRUE:	The given Object is an Identifier with the same id as this one
	 *		FALSE:	The given Object is NOT an Identifier with the same id as this one
	 **/
	boolean equals(Object other);

	/** returns the id character of this Identifier at the given index position
	 * @precondition
	 *	    the id value has a length at least as great as index+1
	 * @postcondition
	 *		The id character of this Identifier at the given index position is returned
	 **/
	char getIdCharacter(int index);
	
	/** returns the length of the Identifier's id as an integer
	 * @precondition
	 *	    -
	 * @postcondition
	 *		The the length of the Identifier's id is returned as an integer
	 **/
	int getIdLength();

	/** appends the given character to the end of the Identifier's existing id
	 * @precondition
	 *	    -
	 * @postcondition
	 *		the given character is appended to the end of the Identifier's existing id
	 **/
	void appendId(char charAt);
	
	/** compares the identifier to a given identifier
	 * @precondition
	 *	    -
	 * @postcondition
	 *	    returns an integer value determined by the ordering of the ids and
	 *		this Identifier and the given Identifier
	 * 		 this = given:	returns 0
	 * 		 this < given:	returns -1
	 * 		 this > given:	returns 1
	 **/
	public int compareTo(Object other);

	/** Returns a deep copy of the identifier
	 * @precondition
	 *	    -
	 * @postcondition
	 *	    A deep copy of the identifier is returned
	 **/
	IdentifierInterface clone();
}
