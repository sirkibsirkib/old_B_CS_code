package dijkstra;

import java.io.PrintStream;

import Methods.Fun;
import Methods.StringRow;

public class Dijkstra {
	StringRow vertices;
	Edge[] edges;
	int numEdges = 0;
	int[] vertexCosts;
	String[] path;
	String startVertex, endVertex;
	PrintStream out = new PrintStream(System.out);
	static final int NUMBER_OF_VERTICES =26; //max 26
	static final double RATIO_VERTICES_TO_EDGES = 1.4;
	
	Dijkstra(){
		vertices = new StringRow();
		vertexCosts = new int[NUMBER_OF_VERTICES];
		path = new String[NUMBER_OF_VERTICES];
		edges = new Edge[(int)(NUMBER_OF_VERTICES*RATIO_VERTICES_TO_EDGES)];
	}
	
	void start(){
		vertices.populateForDebug(1, NUMBER_OF_VERTICES);
		out.println("Vertices:");
		out.println(vertices.printableList());
		
		do{
			createRandomEdge();
			removeComplexEdges();
		}while(numEdges < edges.length-1);
		
		giveEdgeCosts();
		
		startVertex = vertices.pickRandom();
		endVertex = vertices.pickRandom();
		out.println();
		
		out.println("Find Shortest Path:");
		out.println("from:\t  " + startVertex);
		out.println("to:\t  " + endVertex);
		out.println();
		
		out.println("Edges:");
		printEdges();
		
		initializeVertexCosts();
		shortestPath(startVertex);
		out.println();
		
		out.println("Vertex Costs:");
		printShortestPath();
		
		if(vertexCosts[vertices.indexOf(endVertex)] < 99999){
			out.println("Goal reachable!");
		} else {
			out.println("Goal NOT reachable!");
		}
		
		//showRoute();
	}
	
	void printEdges(){
		for(int i = 0; i < numEdges; i++){
			out.println(edges[i].a + "-" + edges[i].b + "\tcost:" + edges[i].cost);
		}
	}
	
	void createRandomEdge(){
		edges[numEdges] = new Edge();
		edges[numEdges].a = vertices.row[Fun.rng(vertices.numStrings)];
		do{
			edges[numEdges].b = vertices.row[Fun.rng(vertices.numStrings)];
		}while(edges[numEdges].a.equals(edges[numEdges].b));
		numEdges++;
	}
	
	void removeComplexEdges(){
		for(int i = 0; i < numEdges; i++){
			for(int j = 0; j < numEdges; j++){
				if(edgeIsTheSame(i,j) && i != j){
					removeEdge(j);
				}
			}
		}
	}
	
	boolean edgeIsTheSame(int indexA, int indexB){
		if(edges[indexA].a.equals(edges[indexB].a) &&
				edges[indexA].b.equals(edges[indexB].b)){ //case (1,2) (1,2)
			return true;
		}
		if(edges[indexA].a.equals(edges[indexB].b) &&
				edges[indexA].b.equals(edges[indexB].a)){ //case (1,2) (2,1)
			return true;
		}
		return false;
	}
	
	void removeEdge(int index){
		for(int i = index; i < numEdges-1; i++){
			edges[i] = edges[i+1];
		}
		numEdges--;
	}
	
	void giveEdgeCosts(){
		for(int i = 0; i < numEdges; i++){
			edges[i].cost = Fun.rng(20)+2;
		}
	}

	public static void main(String[] args) {
		new Dijkstra().start();
	}

	void initializeVertexCosts(){
		for(int i = 0; i < vertices.numStrings; i++){
			vertexCosts[i] = 99999;
			path[i] = "n/a";
		}
		vertexCosts[vertices.indexOf(startVertex)] = 0;
	}
	
	void shortestPath(String locus){
		if(locus.equals(endVertex)){
			return;
		}
		for(int i = 0; i < numEdges; i++){
			if(edges[i].a.equals(locus)){ //case: (this, another)
				if(vertexCosts[vertices.indexOf(edges[i].b)] > vertexCosts[vertices.indexOf(edges[i].a)] + edges[i].cost){
					vertexCosts[vertices.indexOf(edges[i].b)] = vertexCosts[vertices.indexOf(edges[i].a)] + edges[i].cost;
					path[vertices.indexOf(edges[i].b)] = locus;
					shortestPath(edges[i].b);
				}
			} else if(edges[i].b.equals(locus)){ //case: (another, this)
				if(vertexCosts[vertices.indexOf(edges[i].a)] > vertexCosts[vertices.indexOf(edges[i].b)] + edges[i].cost){
					vertexCosts[vertices.indexOf(edges[i].a)] = vertexCosts[vertices.indexOf(edges[i].b)] + edges[i].cost;
					path[vertices.indexOf(edges[i].a)] = locus;
					shortestPath(edges[i].a);
				}
			}
		}
	}
	
	void printShortestPath(){
		for(int i = 0; i < vertices.numStrings; i++){
			if(vertexCosts[i] == 99999){
				out.println(vertices.row[i] + "\tcost:  inf." + "\tfrom:  " + path[i]);
			}else{
				out.println(vertices.row[i] + "\tcost:  " + vertexCosts[i] + "\tfrom:  " + path[i]);
			}
		}
	}
	
	void showRoute(){
		String whereNow = endVertex;
		int timeout = NUMBER_OF_VERTICES;
		while(!whereNow.equals(startVertex) && timeout > 0){
			timeout--;
			//TODO
		}
		
	}
}
