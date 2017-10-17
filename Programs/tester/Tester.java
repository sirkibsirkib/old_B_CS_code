package tester;

import java.io.PrintStream;

import Graph.UndirectedGraph;

public class Tester {
	PrintStream out = new PrintStream(System.out);
	long[] timeSteps = new long[20];
	long startTime;
	
	UndirectedGraph graph = new UndirectedGraph();
	
	Tester(){
		startTime = System.nanoTime();
	}
	
	void start(){
		graph.addRandomEdges(12);
		
		System.out.println(graph.getShortestPath("d", "e"));
		
		System.out.println();
		out.println(graph.printableList());
		
		out.println(graph.mathematicaGraphString());
	}
	
	void measureTime(String label){
		long time = (System.nanoTime() - startTime)/10000;
		startTime = System.nanoTime();
		out.printf(">Time at [" + label.toUpperCase() + "]: " + parseTime(time) + ".00 ns.\n");
	}
	
	String parseTime(long l){
		String ls = l + "";
		String s = "";
		
		for(int i = 0; i < ls.length(); i++){
			s += ls.charAt(i);
			if((ls.length()-i) % 3 == 1 && i < ls.length()-1){
				s += ',';
			}
		}
		return s;
	}
	
	public static void main(String[] args) {
		new Tester().start();
	}
}