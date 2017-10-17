package akinator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import primativeRows.StringRow;

public class Akinator {
	private static final String path = "C:/Users/Christopher/Desktop/akinatorData.txt";
	private StringRow questions;
	private EntryRow entries;
	
	public Akinator(){
		questions = new StringRow();
	}
	
	private void start() throws IOException {
		build();
		//loadBuilt();
		
		guess();
	}
	
	private void loadBuilt() throws IOException{
		QAndEVector qe = new AkinatorSL().loadQAndEFromFile(path);
		questions = qe.getQuestions();
		entries = qe.getEntries();
	}
	
	private void build() throws FileNotFoundException, UnsupportedEncodingException{
		System.out.println("Please type a list of entries (names of entries) in a list\n"
				+ "separated by commas and ending with a new line. ie: cat,dog,fish");
		Scanner entryNameScanner = new Scanner(System.in);
		StringRow entryNames = new StringRow();
		String allEntries = entryNameScanner.nextLine();
		entryNameScanner = new Scanner(allEntries);
		entryNameScanner.useDelimiter(",");
		while(entryNameScanner.hasNext())
			entryNames.add(entryNameScanner.next());
		entries = new EntryRow(entryNames.getNumberOfElements());
		
		for(int i = 0; i < entryNames.getNumberOfElements(); i++){
			entries.add(new Entry(entryNames.getElement(i)));
		}
		while(entries.hasIndistinguishableEntries()){
			EntryVector ev = entries.getTwoIndistinguishableEntries();
			Entry a = ev.getA();
			Entry b = ev.getB();
			System.out.println(a.getName() + " and " + b.getName() + " are currently indistinguishable");
			askToDiscern(a, b);
			ev = entries.getTwoIndistinguishableEntries();
		}
		new AkinatorSL().save(entries, questions, path);
	}
	
	private void guess(){
		System.out.println("Choose one!");
		EntryRow possibilities =  entries.giveCopy();
		int qIndex = 0;
		while(possibilities.getNumberOfEntries() > 1){
			System.out.println(questions.getElement(qIndex));
			Scanner replyScanner = new Scanner(System.in);
			String reply = replyScanner.nextLine();
			if(reply.equals("yes"))
				possibilities.removeThoseWithout(qIndex, true);
			if(reply.equals("no"))
				possibilities.removeThoseWithout(qIndex, false);
			if(possibilities.getNumberOfEntries() > 1)
				qIndex = possibilities.disputedColumn();
		}
		System.out.println("You were thinking of " + possibilities.getEntry(0).getName());
	}
	
	private void askToDiscern(Entry a, Entry b){
		Scanner questionScanner = new Scanner(System.in);
		System.out.println("Please enter a question that will distinguish " + a.getName() + " from " + b.getName());
		questions.add(questionScanner.nextLine());
		
		answerNewQuestion();
	}
	
	private void answerNewQuestion(){
		String thisQuestion = questions.getElement(questions.getNumberOfElements()-1);
		int i = 0;
		while(i < entries.getNumberOfEntries()){
			Entry thisEntry = entries.getEntry(i);
			System.out.println("For \"" + thisEntry.getName() + "\": " + thisQuestion);
			Scanner answerScanner = new Scanner(System.in);
			String answer = answerScanner.nextLine();
			if(answer.equals("yes")){
				thisEntry.giveNewAnswer(true);
				i++;
			}
			if(answer.equals("no")){
				thisEntry.giveNewAnswer(false);
				i++;
			}
		}
	}
		
	public static void main(String[] args) throws IOException{
		new Akinator().start();
	}
}
