package Graph;

import java.util.Scanner;

import Methods.Fun;

public class UndirectedGraph {
	Vertex[] vertices;
	Edge[] edges;
	int numVertices, numEdges;
	int maxVertices = 6;
	int maxEdges = Fun.triangleNumber(maxVertices);
	
	//default constructor
	public UndirectedGraph(){
		vertices = new Vertex[maxVertices];
		edges = new Edge[maxEdges];
	}
	
	public void addVerticesWithParse(String s){
		s.replace(" ", "");
		Scanner read = new Scanner(s);
		read.useDelimiter(",");
		while(read.hasNext()){
			addVertex(read.next());
		}
		read.close();
	}
	
	//add multiple edges <a,b> <c,d> with a single string in format "a,b;c,d"...
	public void addEdgesWithParse(String s){
		s.replace(" ", "");
		Scanner read = new Scanner(s);
		read.useDelimiter(";");
		while(read.hasNext()){
			addEdgeWithParse(read.next());
		}
		read.close();
	}
	
	public void addEdgeWithParse(String s){
		Scanner read = new Scanner(s);
		read.useDelimiter(",");
		
		addEdge(read.next(), read.next());
		read.close();
	}
	
	public void addVertices(String... s){
		for(int i = 0; i < s.length; i++){
			addVertex(s[i]);
		}
	}
	
	public void addVertex(String s){
		if(numVertices-1 < maxVertices){
			doubleMaxVertices();
		}
		vertices[numVertices] = new Vertex(s);
		numVertices++;
	}
	
	public void doubleMaxVertices(){
		maxVertices *= 2;
		maxEdges = Fun.triangleNumber(maxVertices);
		
		Vertex[] moreVertices = new Vertex[maxVertices];
		Edge[] moreEdges = new Edge[maxEdges];
		
		for(int i = 0; i < numVertices; i++){
			moreVertices[i] = vertices[i];
		}
		for(int i = 0; i < numEdges; i++){
			moreEdges[i] = edges[i];
		}
		vertices = moreVertices;
		edges = moreEdges;
	}
	
	public void addEdge(String a, String b, int cost){
		if(!a.equals(b)){
			addEdge(a, b).cost = cost;
		}
	}
	
	public Edge addEdge(String a, String b){
		if(findEdgeConnecting(a, b) != null || a.equals(b)){
			return findEdgeConnecting(a,b);
		}
		if(numEdges-1 > maxEdges){
			doubleMaxVertices();			
		}
		if(!vertexExists(a)){addVertex(a);}
		if(!vertexExists(b)){addVertex(b);}
		
		edges[numEdges] = new Edge(findVertexNamed(a), findVertexNamed(b));
		numEdges++;
		
		return edges[numEdges-1];
	}
	
	public Edge findEdgeConnecting(String a, String b){
		for(int i = 0; i < numEdges; i++){
			if(edges[i].connectA.name.equals(a) && edges[i].connectB.name.equals(b)){
				return edges[i];
			}
			if(edges[i].connectA.name.equals(b) && edges[i].connectB.name.equals(a)){
				return edges[i];
			}
		}
		return null;
	}
	
	public Vertex findVertexNamed(String name){
		for(int i = 0; i < numVertices; i++){
			if(vertices[i].name.equals(name)){
				return vertices[i];
			}
		}
		return null;
	}
	
	public boolean vertexExists(String name){
		for(int i = 0; i < numVertices; i++){
			if(vertices[i].name.equals(name)){
				return true;
			}
		}
		return false;
	}
	
	public String printableList(){
		String s = "";
		
		s += "Vertices:\n";
				
		for(int i = 0; i < numVertices; i++){
			if(vertices[i].cost < Integer.MAX_VALUE){
				s += "(" + vertices[i].name + ") cost:" + vertices[i].cost + "\n";
			} else {
				s += "(" + vertices[i].name + ") cost:inf.\n";
			}
			
		}
		
		s += "\nEdges:\n";
		for(int i = 0; i < numEdges; i++){
			s += "<"  + edges[i].connectA.name + "," + edges[i].connectB.name + "> cost:" +  edges[i].cost + "\n";
		}
		return s;
	}
	
	public String getShortestPath(String start, String end){
		Vertex startVertex = findVertexNamed(start);
		for(int i = 0; i < numVertices; i++){
			vertices[i].cost = Integer.MAX_VALUE;
		}
		startVertex.cost = 0;
		shortestPath(startVertex, startVertex, findVertexNamed(end));
		
		Vertex location = findVertexNamed(end);
		
		String route = "(" + location.name + ")";
		while(location !=  startVertex&& location.previousStep != null){
			location = location.previousStep;
			route = "(" + location.name +")" + " -> " + route;
		}
		return "route: " + route;
	}
	
	private void shortestPath(Vertex start, Vertex location, Vertex end){
		//stop condition
		if(location == end){
			//traceRoute(end);
			return;
		}
		//recurse and write
		for(int i = 0; i < numEdges; i++){
			if(edges[i].connectA == location){//case (location, other)
				if(edges[i].connectB.cost > location.cost + edges[i].cost){
					edges[i].connectB.cost = location.cost + edges[i].cost;
					edges[i].connectB.previousStep = location;
					location.nextStep = edges[i].connectB;
					shortestPath(start, edges[i].connectB, end);
				}
			}
			if(edges[i].connectB == location){//case (other, location)
				if(edges[i].connectA.cost > location.cost + edges[i].cost){
					edges[i].connectA.cost = location.cost + edges[i].cost;
					edges[i].connectA.previousStep = location;
					location.nextStep = edges[i].connectA;
					shortestPath(start, edges[i].connectA, end);
				}
			}
		}
	}
	
	public String mathematicaGraphString(){
		String s = "GraphPlot[{";
		for(int i = 0; i < numEdges; i++){
			s += edges[i].connectA.name + " -> " + edges[i].connectB.name;
			if(i < numEdges - 1){
				s += ", ";
			}
		}
		s += "}, VertexLabeling -> True]";
		return s;
	}
	
	public void addRandomEdges(int numberOfVertices){
		for(int i = 0; i < numberOfVertices; i++){
			addEdge(""+(char)(i+'a'), ""+(char)(Fun.rng(numberOfVertices)+'a'), Fun.rng(30)+1);
			if(Fun.chance(1.5)){
				addEdge(""+(char)(i+'a'), ""+(char)(Fun.rng(numberOfVertices)+'a'), Fun.rng(30)+1);
			}
		}
	}
}
