package paperDistributor;

import java.io.PrintStream;
import java.util.Scanner;

import Methods.Fun;
import Methods.StringRow;

class PaperDistributor {
	Scanner in = new Scanner(System.in);
	PrintStream out = new PrintStream(System.out);
	StringRow students, papers, results;
	int longestName = 0;
	
	PaperDistributor(){
		students = new StringRow();
		papers = new StringRow();
		results = new StringRow();
	}
	
	void start(){
		students.addMany("Brad Cooper", "Henry", "Brad Arity", "Seth Rogan", "Hunkleburg", "Steve", "Achim");
		students.addMany("Donovan", "Leroy", "Jeff", "Art", "Jen", "Hank", "Finn", "Gerard", "Addie", "Sarah", "Alice");
		
		papers.addMany("Science", "Religion", "Farming", "Agriculture", "Fishing", "Smoking", "reading", "Bottany");
		papers.addMany("Italian", "Pottery", "Anime", "Pilchards", "Liberal Arts", "Fine Arts", "French", "Driving", "Hunting");
		
		calcLongestName();
		offerChoiceLoop();
		printResults();
	}
	
	void calcLongestName(){
		for(int i = 0; i < students.numStrings; i++){
			if(students.row[i].length() > longestName){
				longestName = students.row[i].length();
			}
		}
		longestName += 4;
	}
	
	void offerChoiceLoop(){
		while(!students.isEmpty() && !papers.isEmpty()){
			String stud = students.pickRandom();
			String pap = papers.pickRandom();
			
			out.printf("Would %s accept %s? (y/n)\n", stud, pap);
			
			String input = in.next();
			
			if(input.equals("y")){
				out.println("pairing accepted");
				out.println();
				
				students.remove(stud);
				papers.remove(pap);
				
				String gap = "";
				for(int i = 0; i < longestName-stud.length(); i++){
					gap += ' ';
				}
				
				results.add(stud + gap + "will read  " + pap);
			} else if(input.equals("n")){
				out.println("pairing rejected");
				out.println();
			} else {
				out.println("unknown response. Please reply \"y\" or \"n\".");
				out.println();
			}
			
		}
	}
	
	void printResults(){
		results.asciiSort("bubbleSort");
		out.println("All pairings sufficiently completed");
		out.println();
		
		for(int i = 0; i < results.numStrings; i++){
			out.println(results.row[i]);
		}
	}
	
	public static void main(String[] args){
		new PaperDistributor().start();
	}
}
