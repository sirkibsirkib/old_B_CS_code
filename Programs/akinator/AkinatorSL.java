package akinator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import primativeRows.StringRow;

public class AkinatorSL {
	public void save(EntryRow entries, StringRow questions, String path) throws FileNotFoundException, UnsupportedEncodingException{
		int numEntries = entries.getNumberOfEntries();
		int numQuestion = questions.getNumberOfElements();
		
		File file = new File(path);
		if(file.isDirectory()){
			System.out.println("not saved!");
			return; //TODO not optimal solution
		}
			
		file.getParentFile().mkdirs();
		PrintWriter writer = new PrintWriter(file, "UTF-8");
		
		writer.println(numQuestion);
		writer.println(numEntries);
		
		for(int i = 0; i < numQuestion; i++)
			writer.print(questions.getElement(i) + "%%");
		writer.println();
		for(int i = 0; i < numEntries; i++){
			writer.println(entryData(entries.getEntry(i)));
		}
		System.out.println("saved!");
		writer.close();
	}
	
	private String entryData(Entry ent){
		String data = ent.getName() + "%%";
		for(int i = 0; i < ent.getNumberOfAnswers(); i++){
			if(ent.getAnswerAt(i) == true)
				data += '1';
			else
				data += '0';
		}
		return data;
	}
	
	public QAndEVector loadQAndEFromFile(String path) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(path));
		String everything;
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append(System.lineSeparator());
	            line = br.readLine();
	        }
	        everything = sb.toString();
	    } finally {
	        br.close();
	    } 
	    return loadQAndEFromString(everything);
	}
	
	private QAndEVector loadQAndEFromString(String data){
		Scanner dataScanner = new Scanner(data);
		int numQuestions = dataScanner.nextInt(),
			numEntries = dataScanner.nextInt();
		dataScanner.nextLine();
		
		StringRow questions = makeQuestionsFromData(dataScanner.nextLine());
		
		EntryRow entries = new EntryRow(numEntries);
		for(int i = 0; i < numEntries; i++)
			entries.add(makeEntryFromData(dataScanner.nextLine()));
		
		dataScanner.close();
		return new QAndEVector(questions, entries);
	}
	
	private StringRow makeQuestionsFromData(String questionLine){
		StringRow questions = new StringRow();
		Scanner lineScanner = new Scanner(questionLine);
		lineScanner.useDelimiter("%%");
		while(lineScanner.hasNext())
			questions.add(lineScanner.next());
		lineScanner.close();
		return questions;
	}
	
	private Entry makeEntryFromData(String entryLine){
		Scanner lineScanner = new Scanner(entryLine);
		lineScanner.useDelimiter("%%");
		
		Entry ent = new Entry(lineScanner.next());
		String answerData = lineScanner.next();
		lineScanner.close();
		for(int i = 0; i < answerData.length(); i++){
			if(answerData.charAt(i) == '1')
				ent.giveNewAnswer(true);
			if(answerData.charAt(i) == '0')
				ent.giveNewAnswer(false);
		}
		return ent;
	}
}
