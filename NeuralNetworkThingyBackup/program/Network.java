package program;

import programEntity.Wall;
import programNode.Memory;
import programNode.Node;
import programNode.NodeRow;
import programNode.Nodule;
import programNode.Stimnode;

public class Network {
	static final double MEMORY_RATIO = .25,
			LAYER_EDGE_BUFFER = .05;
	private NodeRow stimuli,
			buttons,
			allNodes,
			memoryNodes;
	private int	hiddenLayers;
	private double neuronReach;
	
	public Network(int povRadius, int HIDDEN_LAYERS){
		this.hiddenLayers = HIDDEN_LAYERS;
		if(hiddenLayers > 0)
			this.neuronReach = Math.sqrt(1.0*1/HIDDEN_LAYERS);
		else
			this.neuronReach = .7;
		stimuli = new NodeRow();
		buttons = new NodeRow();
		allNodes = new NodeRow();
		memoryNodes = new NodeRow();
		
		setupStimuliNodes(povRadius);
		setupButtonNodes();
	}
	
	//for duplication
	private Network(NodeRow allNodes, int HIDDEN_LAYERS){
		this.allNodes = allNodes;
		this.hiddenLayers = HIDDEN_LAYERS;
		if(hiddenLayers > 0)
			this.neuronReach = Math.sqrt(1.0*1/HIDDEN_LAYERS);
		else
			this.neuronReach = .7;
		
		stimuli = new NodeRow();
		for(int i = 0; i < allNodes.getNumberOfNodes(); i++){
			Node n = allNodes.getNode(i);
			if(n instanceof Stimnode){
				stimuli.add(n);
			}
		}
		
		buttons = new NodeRow();
		for(int i = 0; i < allNodes.getNumberOfNodes(); i++){
			Node n = allNodes.getNode(i);
			if(n.getY() == 0){
				buttons.add(n);
			}
		}
		
		memoryNodes = new NodeRow();
		for(int i = 0; i < allNodes.getNumberOfNodes(); i++){
			Node n = allNodes.getNode(i);
			if(n instanceof Memory){
				memoryNodes.add(n);
			}
		}
	}	
	
	private boolean aNodePointsTo(Node n){
		for(int i = 0; i < allNodes.getNumberOfNodes(); i++){
			if(allNodes.getNode(i).doesPointTo(n)){
				return true;
			}
		}
		return false;
	}
	
	public double getComplexity(){
		double clutter = allNodes.getNumberOfPoints() + allNodes.getNumberOfNodes() + 1;
		return Math.pow(clutter, .2) - 1;
	}
	
	public Network duplicate(){
		return new Network(allNodes.duplicate(), hiddenLayers);
	}
	
	public void mutateClean(){
		for(int i = 0; i < allNodes.getNumberOfNodes(); i++){
			Node n = allNodes.getNode(i);
			if(n.isRemovable() && (n.getNumberOfPoints() == 0 || !aNodePointsTo(n))){
				allNodes.remove(i);
				i = 0;
			}
		}
	}
	
	public void mutateAIMD(){
		int stims = stimuli.getNumberOfNodes(),
			hiddenNodes = allNodes.getNumberOfNodes() - buttons.getNumberOfNodes() - stims,
			numberOfPoints = allNodes.getNumberOfPoints();
		for(int i = 0; i < hiddenNodes/8; i++)
			removeOneRandomNode();
		for(int i = 0; i < numberOfPoints/8; i++)
			removeOneRandomPoint();
		for(int i = 0; i < Math.random()/neuronReach/neuronReach; i++)
			createRandomNode();
		for(int i = 0; i < Runner.rng(stims/4)/neuronReach/neuronReach + 3; i++)
			createOneRandomPoint();
	}
	
	public void mutateGrow(){
		int stims = stimuli.getNumberOfNodes(),
			hiddenNodes = allNodes.getNumberOfNodes() - buttons.getNumberOfNodes() - stims,
			numberOfPoints = allNodes.getNumberOfPoints();
		for(int i = 0; i < hiddenNodes/8/(neuronReach/neuronReach) + 3; i++)
			createRandomNode();
		for(int i = 0; i < stims/3/neuronReach/neuronReach + 6; i++)
			createOneRandomPoint();
	}
	
	public void mutateTrim(){
		int hiddenNodes = allNodes.getNumberOfNodes() - buttons.getNumberOfNodes() - stimuli.getNumberOfNodes(),
			numberOfPoints = allNodes.getNumberOfPoints();
		for(int i = 0; i < Runner.rng(hiddenNodes/5); i++){
			removeOneRandomNode();
		}
		for(int i = 0; i < Runner.rng(numberOfPoints/3); i++)
			removeOneRandomPoint();
	}
	
	public void mutateJumble(){ //reassigns half points and nodes randomly
		int hiddenNodes = allNodes.getNumberOfNodes() - buttons.getNumberOfNodes() - stimuli.getNumberOfNodes(),
			numberOfPoints = allNodes.getNumberOfPoints();
		for(int i = 0; i < hiddenNodes/2; i++)
			removeOneRandomNode();
		for(int i = 0; i < numberOfPoints/2; i++)
			removeOneRandomPoint();
		for(int i = 0; i < hiddenNodes/2; i++)
			createRandomNode();
		for(int i = 0; i < numberOfPoints/2; i++)
			createOneRandomPoint();
	}
	
	public void removeOneRandomNode(){
		if(hasRemovableNode()){
			int index,
				timeout = 50;
			do{
				index = Runner.rng(allNodes.getNumberOfNodes());
				if(timeout == 0)
					return;
				timeout--;
			}while(!allNodes.getNode(index).isRemovable());
			allNodes.remove(index);
		}
	}
	
	private boolean hasRemovableNode(){
		for(int i = 0; i < allNodes.getNumberOfNodes(); i++){
			if(allNodes.getNode(i).isRemovable()){
				return true;
			}			
		}
		return false;
	}
	
	public void removeOneRandomPoint(){
		int numPoints,
			numElements = allNodes.getNumberOfNodes(),
			timeout = numElements,
			nodeIndex = Runner.rng(numElements);
		do{
			nodeIndex++;
			if(nodeIndex == numElements){
				nodeIndex = 0;
			}
			numPoints = allNodes.getNode(nodeIndex).getNumberOfPoints();
			if(timeout == 0)
				return;
			timeout--;
		}while(numPoints == 0);
		allNodes.getNode(nodeIndex).removeRandomPoint();
	}
	
	private void setupStimuliNodes(int povRadius){
		int offset = 0,
			area = (2*povRadius+1)*(2*povRadius+1);
		area = area*2 - 1;
		for(int i = -povRadius; i <= povRadius; i++){
			for(int j = -povRadius; j <= povRadius; j++){
				Stimnode n = new Stimnode("" + (j+povRadius) + "," + (i+povRadius), 1.0*(offset)/(area), 1, Runner.BLACK);
				n.giveSource(j+povRadius, i+povRadius, true);
				stimuli.add(n);
				allNodes.add(n);
				offset++;
			}
		}
		for(int i = -povRadius; i <= povRadius; i++){
			for(int j = -povRadius; j <= povRadius; j++){
				Stimnode n = new Stimnode("" + (j+povRadius) + "," + (i+povRadius), 1.0*(offset)/(area), 1, Runner.WHITE);
				n.giveSource(j+povRadius, i+povRadius, false);
				stimuli.add(n);
				allNodes.add(n);
				offset++;
			}
		}
	}
	
	private void setupButtonNodes(){
		Node n = new Nodule("left", 0.0, 0);
		buttons.add(n);
		allNodes.add(n);
		
		n = new Nodule("right", 0.333, 0);
		buttons.add(n);
		allNodes.add(n);
		
		n = new Nodule("up", 0.667, 0);
		buttons.add(n);
		allNodes.add(n);
		
		n = new Nodule("down", 1.0, 0);
		buttons.add(n);
		allNodes.add(n);
	}
	
	public void createOneRandomPoint(){
		Node from, to;
		int timeout = 50;
		do{
			from = allNodes.getNode(Runner.rng(allNodes.getNumberOfNodes()));
			to = allNodes.getNode(Runner.rng(allNodes.getNumberOfNodes()));
			if(timeout == 0)
				return;
			timeout--;
		}while(from.getY() - to.getY() > neuronReach || from == to ||
				from.getY() <= to.getY() || from.doesPointTo(to));
		if(from instanceof Stimnode && Math.random() > .5) //stimnodes have negative weight connections ie. negations
			from.pointTo(to, -Math.random());
		else
			from.pointTo(to, Math.random());
	}
	
	public void createRandomNode(){
		if(Math.random() < MEMORY_RATIO){
			Memory mem = new Memory(nextAvailableNodeName("m"), Math.random(), getALayerY());
			memoryNodes.add(mem);
			allNodes.add(mem);
		}else{
			allNodes.add(new Nodule(nextAvailableNodeName("n"), Math.random(), getALayerY()));
		}
	}
	
	private double getALayerY(){
		if(hiddenLayers == 0)
			return Math.random()*(1-(2*LAYER_EDGE_BUFFER)) + LAYER_EDGE_BUFFER;
		int layerIndex = (int)(Math.random()*hiddenLayers);
		double y = (1.0*(1 + layerIndex)/(hiddenLayers+1));
		return y;
	}
	
	private String nextAvailableNodeName(String prefix){
		int numerator = 0;
		while(true){
			if(!allNodes.hasNodeWithname(prefix + numerator)){
				return prefix + numerator;
			}
			numerator++;
		}
	}
	
	public void resetFires(){
		for(int i = 0; i < allNodes.getNumberOfNodes(); i++){
			allNodes.getNode(i).resetFire();
		}
	}
	
	public void memoryFire(){
		for(int i = 0; i < memoryNodes.getNumberOfNodes(); i++){
			Memory next = (Memory)memoryNodes.getNode(i);
			if(next.remembers())
				next.memoryFire();
		}
	}
	
	public void fireStimuli(Room r, int povRadius){
		for(int i = 0; i < stimuli.getNumberOfNodes(); i++){
			Stimnode n = (Stimnode)stimuli.getNode(i);
			boolean blockThere = r.getRelativeBlock(n.getSourceX()-povRadius, n.getSourceY()-povRadius) instanceof Wall;
			if((blockThere && n.isPositive()) ||
					(!blockThere && !n.isPositive())){
				n.fire();
			}
		}
	}
	
	public double getXof(int index){
		return stimuli.getXof(index);
	}
	
	public double getYof(int index){
		return stimuli.getYof(index);
	}
	
	public int getNumberOfNodes(){
		return allNodes.getNumberOfNodes();
	}
	
	public Node getNode(int index){
		return allNodes.getNode(index);
	}
	
	public String[] getButtonFireNames(){
		String[] firedButtons = new String[buttons.getNumberOfNodes()];
		int firedElements = 0;
		for(int i = 0; i < buttons.getNumberOfNodes(); i++){
			if(buttons.getNode(i).didFire()){
				firedButtons[firedElements] = buttons.getNode(i).getName();
			}else{
				firedButtons[firedElements] = "no fire";
			}
			firedElements++;
		}
		return firedButtons;
	}
}
