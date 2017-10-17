package assignment2;

import java.io.PrintStream;

//parser attempts to perform a command specified by a string
public class Interpreter {
	private static final char[] VALID_EXPRESSION_CHARS = {'.', '=', '<', '>', '%', '+', '-', '|', '*', '{', '}', '(', ')', ','};
	private Table t = new Table();
	PrintStream out;
	
	public Interpreter(PrintStream out) {
		this.out = out;
	}
	
	//INPUT PARSING
	
	public void interpret(String s) throws APException{
		s = s.trim();
		if(s.length() == 0)	throw new APException("EmptyCommandLineGiven");
		if(s.charAt(0) == '/')						return;
		if(s.charAt(0) == '?'){	parsePrint(s);		return;}
		if(find(s, '=') != -1){	parseAssignment(s);	return;}
		throw new APException("UndefinedCommand");
	}

	private void parseAssignment(String s) throws APException {
		int equalsIndex = find(s, '=');
		SetInterface<NaturalNumberInterface> v = evaluate(preparseExpression(s.substring(equalsIndex+1, s.length())));
		IdentifierInterface i = newIdentiferFromString(s.substring(0, equalsIndex));
		t.assign(i, v);
	}

	private String preparseExpression(String s) throws APException {
		String output = "";
		boolean insideASet = false;
		
		for(int i = 0; i < s.length(); i++){
			char c = s.charAt(i);
			if(!validCharacter(c))
				throw new APException("UnexpectedCharacter \"" + c + "\" in expression");
			if(c == '{')	insideASet = true;
			if(c == '}')	insideASet = false;
			if(!Character.isWhitespace(c) || insideASet)	output += c;
		}
		return output;
	}

	private void parsePrint(String s) throws APException {
		s = s.substring(1, s.length());
		SetInterface<NaturalNumberInterface> printSet = evaluate(preparseExpression(s));
		print(printSet);
	}

	//EVALUATION
	
	public SetInterface<NaturalNumberInterface> evaluate(String exp) throws APException {
		if(exp.length() == 0)
			throw new APException("NoValueGiven");
		if(countMatches(exp, '(') != countMatches(exp, ')'))
			throw new APException("ImbalancedParentheses");
		while(needsOuterBracketsTrimmed(exp))
			exp = trimOuterBrackets(exp);
		
		int index = findIndexOfLastOperation(exp);
		char op = (index >= 0) ? exp.charAt(index) : ' ';
		
		if(exp.contains("%if"))	return evaluateIf(exp);
		if(op != ' ')			return operateAtIndex(exp, index, op);
		if(setFormat(exp))		return buildNewSet(exp);
		
		
		SetInterface<NaturalNumberInterface> found =  t.tryRetrieve(newIdentiferFromString(exp));
		if(found == null)
			throw new APException("UnknownIdentifier \"" + exp + "\"");
		return found;
	}

	private boolean setFormat(String exp){
		return exp.length() >= 2 && exp.charAt(0) == '{' && exp.charAt(exp.length()-1) == '}';
	}
	
	private SetInterface<NaturalNumberInterface> operateAtIndex(String exp, int index, char op) throws APException{
		SetInterface<NaturalNumberInterface> left, right;
		left = evaluate(exp.substring(0, index));
		right = evaluate(exp.substring(index+1, exp.length()));
		
		switch(op){
		case '*':	return left.intersection(right);
		case '+':	return left.union(right);
		case '-':	return left.complement(right);
		default :	return left.symmetricDifference(right);
		}
	}
	
	private int findIndexOfLastOperation(String exp){
		char op = ' ';
		int opHeight = Integer.MAX_VALUE,
			height = 0,
			index = -1;
		for(int i = 0; i < exp.length(); i++){
			char c = exp.charAt(i);
			if(c == '(')	height++;
			if(c == ')')	height--;
			
			if(!(c == '+' || c == '-' || c == '|' || c == '*' ))
				continue;
			
			if(height < opHeight){
				op = c;
				index = i;
				opHeight = height;
			} else if(opHeight == height && !(c == '*' && (op == '+' || op == '-' || op == '|'))){
				op = c;
				index = i;
				opHeight = height;
			}
		}
		return index;
	}

	private SetInterface<NaturalNumberInterface> evaluateIf(String s) throws APException {
		if(!s.startsWith("%if"))
			throw new APException("InvalidIf");
		int indexOfThen = findAttachedToThisIf(s, "then", 1);
		int indexOfElse = findAttachedToThisIf(s, "else", 1);
		if(indexOfThen == -1)	throw new APException("IfbutNoThen");
		if(indexOfElse == -1)	throw new APException("IfbutNoElse");
		boolean ifIsTrue = evaluateBoolean(s.substring(3, indexOfThen));
		if(ifIsTrue)
			return evaluate(preparseExpression(s.substring(indexOfThen+4, indexOfElse)));
		return evaluate(preparseExpression(s.substring(indexOfElse+4)));
	}
	
	private int findAttachedToThisIf(String s, String query, int startIndex){
		if(startIndex >= s.length())
			return -1;
		int nextIf = findIndexOfSubString(s, "%if", startIndex);
		int nextQuery = findIndexOfSubString(s, query, startIndex);
		if(nextIf != -1 && nextQuery != -1 && nextIf < nextQuery)
			return findAttachedToThisIf(s, query, nextQuery + 1);
		if(nextQuery != -1)
			return nextQuery;
		return -1;
	}
	
	private boolean evaluateBoolean(String exp) throws APException {
		SetInterface<NaturalNumberInterface> left, right;
		int equalsIndex = find(exp, '=');
		if(equalsIndex != -1){
			left = evaluate(exp.substring(0, equalsIndex));
			right = evaluate(exp.substring(equalsIndex+1));
			return left.equals(right);
		}
		int lessThanIndex = find(exp, '<');
		if(lessThanIndex != -1){
			left = evaluate(exp.substring(0, lessThanIndex));
			right = evaluate(exp.substring(lessThanIndex+1));
			return left.union(right).equals(right);
		}
		int greaterThanIndex = find(exp, '>');
		if(greaterThanIndex != -1){
			left = evaluate(exp.substring(0, greaterThanIndex));
			right = evaluate(exp.substring(greaterThanIndex+1));
			return left.union(right).equals(left);
		}
		throw new APException("UnknownBooleanOperator");
	}
	
	//OBJECT BUILDING FROM STRING

	private SetInterface<NaturalNumberInterface> buildNewSet(String input) throws APException{
		input = input.substring(1, input.length()-1);
		int dotsIndex = findIndexOfSubString(input, "..", 0);
		if(dotsIndex != -1)
			return buildNonTerminalSet(input, dotsIndex);
		return buildTerminalSet(input);
	}

	private SetInterface<NaturalNumberInterface> buildTerminalSet(String contents) throws APException {
		SetInterface<NaturalNumberInterface> newSet = new Set<>();
		NaturalNumberInterface nextNum = null;
		boolean leadingZero = false;
		for(int i = 0; i < contents.length(); i++){
			char c = contents.charAt(i);
			
			if(Character.isWhitespace(c) || c == ','){
				if(nextNum != null && !newSet.contains(nextNum))
					newSet.add(nextNum);
				leadingZero = false;
				nextNum = null;
			}else if(Character.isDigit(c)){
				if(nextNum == null){
					if(c == '0')
						leadingZero = true;
					nextNum = new NaturalNumber(c);
				}else{
					if(leadingZero)
						throw new APException("noLeadingZeroes");
					nextNum.append(c);
				}
			}else
				throw new APException("unexpectedCharacter \"" + c + "\" in expression");
		}
		if(nextNum != null && !newSet.contains(nextNum))
			newSet.add(nextNum);
		return newSet;
	}
	
	private SetInterface<NaturalNumberInterface> buildNonTerminalSet(String contents, int dotsIndex) throws APException{
		SetInterface<NaturalNumberInterface> newSet = new Set<>();
		NaturalNumberInterface firstNumber = parseNN(contents.substring(0, dotsIndex).trim());
		NaturalNumberInterface lastNumber = parseNN(contents.substring(dotsIndex+2, contents.length()).trim());
		if(firstNumber.compareTo(lastNumber) > 0){
			NaturalNumberInterface hold = firstNumber;
			firstNumber = lastNumber;
			lastNumber = hold;
		}
		NaturalNumberInterface next = firstNumber;
		while(!next.equals(lastNumber)){
			newSet.add(next.clone());
			next.incriment();
		}
		newSet.add(next.clone());
		return newSet;
	}

	private NaturalNumberInterface parseNN(String s) throws APException{
		if(s.length() < 1)
			throw new APException("missingNumber");
		char c = s.charAt(0);
		if(!Character.isDigit(c))
			throw new APException("NonDigitInNumberPlace");
		NaturalNumberInterface x = new NaturalNumber(c);
		if(c == '0' && s.length() != 1)
			throw new APException("noLeadingZeroes");
		for(int i = 1; i < s.length(); i++){
			c = s.charAt(i);
			if(!Character.isDigit(c))
				throw new APException("NonDigitInNumberPlace");
			x.append(c);
		}
		return x;
	}

	public IdentifierInterface newIdentiferFromString(String s) throws APException {
		IdentifierInterface x = null;
		boolean done = false;
		if(Character.isDigit(s.charAt(0)))
			throw new APException("NumberInPlaceOfIdentifier");
		
		for(int i = 0; i < s.length(); i++){
			char c = s.charAt(i);
			
			if(Character.isWhitespace(c)){
				if(x != null)
					done = true;
			}else if(done)
				throw new APException("IllegalIdentifierCharacter");
			if(Character.isLetter(c) && x == null)
				x = new Identifier(c);
			else if(Character.isLetterOrDigit(c) && x != null)
				x.appendId(c);
			if(!Character.isLetterOrDigit(c) && !Character.isWhitespace(c))
				throw new APException("UnexpectedCharacter<" + c + ">");
		}
		if(x == null || x.getIdLength() == 0)
			throw new APException("EmptyIdentifierName");
		return x;
	}
	
	//STRING & CHARACTER FUNCTIONS
	
	private boolean validCharacter(char c) {
		if(Character.isWhitespace(c) || Character.isLetterOrDigit(c))
			return true;
		for(int i = 0; i < VALID_EXPRESSION_CHARS.length; i++)
			if(c == VALID_EXPRESSION_CHARS[i])
				return true;
		return false;
	}


	private int findIndexOfSubString(String s, String query, int startIndex) {
		int qLength = query.length();
		outer:
		for(int i = startIndex; i < s.length(); i++){
			for(int j = 0; j < qLength; j++){
				if(s.charAt(i + j) != query.charAt(j))
					continue outer;
			}
			return i;
		}
		return -1;
	}
	
	private int find(String s, char c){
		for(int i = 0; i < s.length(); i++)
			if(s.charAt(i) == c)
				return i;
		return -1;
	}
	
	private void print(SetInterface<NaturalNumberInterface> inputSet) {
		SetInterface<NaturalNumberInterface> copy = inputSet.clone();
		String o = "";
		
		while(copy.getSize() > 0){
			NaturalNumberInterface n = copy.getElement();
			String nextNumberString = "";
			for(int i = 0; i < n.numberLength(); i++)
				nextNumberString += n.getDigitAt(i);
			o = o + nextNumberString;
			copy.removeElement(n);
			if(copy.getSize() > 0)
				o += " ";
		}
		out.println(o);
	}
	
	private boolean needsOuterBracketsTrimmed(String exp) {
		if(exp.length() <= 0)
			return false;
		exp = exp.trim();
		if(exp.charAt(0) != '(' || exp.charAt(exp.length()-1) != ')')
			return false;
		int height = 1;
		for(int i = 1; i < exp.length()-1; i++){
			char c = exp.charAt(i);
			if(c == '(')	height++;
			if(c == ')')	height--;
			
			if(height == 0)
				return false;
		}
		return true;
	}

	private String trimOuterBrackets(String exp) {
		exp = exp.trim();
		if(needsOuterBracketsTrimmed(exp))
			return exp.substring(1, exp.length()-1).trim();
		return exp;
	}
	
	private int countMatches(String exp, char c) {
		int count = 0;
		for(int i = 0; i < exp.length(); i++)
			if(exp.charAt(i) == c)
				count++;
		return count;
	}
}
