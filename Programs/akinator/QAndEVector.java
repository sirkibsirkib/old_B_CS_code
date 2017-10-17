package akinator;

import primativeRows.StringRow;

public class QAndEVector {
	private StringRow questions;
	private EntryRow entries;
	
	public QAndEVector(StringRow questions, EntryRow entries){
		this.entries = entries;
		this.questions = questions;
	}
	
	public StringRow getQuestions() {
		return questions;
	}

	public EntryRow getEntries() {
		return entries;
	}	
}
