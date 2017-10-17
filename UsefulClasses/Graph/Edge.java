package Graph;

public class Edge {
	Vertex connectA, connectB;
	int cost;
	
	Edge(){
		cost = 0;
	}
	
	Edge(Vertex a, Vertex b){
		connectA = a;
		connectB = b;
		cost = 0;
	}
	
	Edge(Vertex a, Vertex b, int cost){
		connectA = a;
		connectB = b;
		this.cost = cost;
	}
}
