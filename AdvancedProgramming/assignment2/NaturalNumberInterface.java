package assignment2;

/** ADT for the class NaturalNumber.
*
* @author Christopher Esterhuyse & Bertan Konuralp
* @elements
*	Numbers of type char
* @structure 
*	-
* @domain
*	any String with every character being a digit (0-9)
* @constructor
*	NaturalNumber(char c){;
*	    <dl>
*		<dt><b>PRE-condition</b><dd>-
* 		The given String has only characters that are digits (0-9)
*		<dt><b>POST-condition</b><dd>
*		The object has the given String as its numeric value
*	    </dl>
**/

public interface NaturalNumberInterface extends Data{
	
	/** the character of the number value at an index is returned
	 * @precondition
	 *	   	the index is not out of bounds of the stored number value
	 * @postcondition
	 * 		the character at the given index is returned
	 **/
	char getDigitAt(int index);
	
	/** the given character is appended to the existing number
	 * @precondition
	 *	   -
	 * @postcondition
	 * 		the given character is appended to the existing number
	 **/
	void append(char c);
	
	/** the natural number is initialized and contains only the value represented by the given character
	 * @precondition
	 *	   -
	 * @postcondition
	 * 		the natural number is initialized and contains only the value represented by the given character
	 **/
	void init(char c);
	
	/** returns the number of digits comprising the stored number
	 * @precondition
	 *	   -
	 * @postcondition
	 * 		the number of digits comprising the stored number is returned
	 **/
	int numberLength();

	/** compares this NaturalNumber with the given NaturalNumber
	 * @precondition
	 *	   the given object is a NaturalNumber
	 * @postcondition
	 * 		Where A is the numerical value of this NaturalNumber, and B is the numerical value of the given NaturalNumber:
	 *	   	 A = B:		0  is returned
	 * 		 A < B:		-1 is returned
	 * 		 A > B:		1  is returned
	 **/
	int compareTo(Object other);
	
	/** Returns a deep copy of the NaturalNumber
	 * @precondition
	 *	    -
	 * @postcondition
	 *	    A deep copy of the NaturalNumber is returned
	 **/
	NaturalNumberInterface clone();
	
	/** Returns whether the NaturalNumber and the given NaturalNumber have the same numerical values
	 * @precondition
	 *	    -
	 * @postcondition
	 *	    TRUE:	the NaturalNumber and the given NaturalNumber are the same
	 * 		FALSE:	the NaturalNumber and the given NaturalNumber are NOT the same
	 **/
	boolean equals(NaturalNumberInterface other);
	
	/** Increases the numerical value of the Natural Number by 1
	 * @precondition
	 *	    -
	 * @postcondition
	 *	The numerical value of the Natural Number is increased by 1
	 **/
	void incriment();
}
