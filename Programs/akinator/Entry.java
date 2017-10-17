package akinator;

import primativeRows.BooleanRow;

public class Entry {
	BooleanRow answers;
	private String name;
	
	public Entry(String name){
		this.name = name;
		answers = new BooleanRow();
	}
	
	public String getName(){
		return name;
	}
	
	public void giveNewAnswer(boolean answer){
		answers.add(answer);
	}
	
	public boolean getAnswerAt(int index){
		return answers.getElement(index);
	}
	
	public int getNumberOfAnswers(){
		return answers.getNumberOfElements();
	}
}
