package program;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import programNode.Memory;
import programNode.Node;
import programNode.NodeRow;
import programNode.Nodule;
import programNode.Stimnode;

public class NetworkSL {
	Runner runner;
	private NodeRow allNodes,
		buttons,
		stimuli,
		memoryNodes;
	
	public NetworkSL(Runner runner){
		this.runner = runner;
	}
	
	//SAVE
	public void save(Network net, String path) throws FileNotFoundException, UnsupportedEncodingException{
		int numNodes = net.getNumberOfNodes();
		
		File file = new File(path);
		if(!file.exists() || file.isDirectory())
			return; //TODO not optimal solution
		
		file.getParentFile().mkdirs();
		PrintWriter writer = new PrintWriter(file, "UTF-8");
		
		writer.println(numNodes);
		writer.println(net.getHiddenLayers());
		writer.println(net.getPovRadius());
		
		for(int i = 0; i < numNodes; i++)
			writer.println(saveNodeString(net.getNode(i)));
		for(int i = 0; i < numNodes; i++)
			writer.println(savePointsString(net.getNode(i)));
		writer.close();
	}
	
	private String saveNodeString(Node n){//n0,0.10123123,0.082
		String s = "";
		s += n.getName() + "\t" + n.getX() + "\t" + n.getY();
		if(n instanceof Stimnode){
			Stimnode sn = (Stimnode) n;
			s+= "\t" + sn.getSourceX() + "\t" + sn.getSourceY();
		}
		return s;
	}
	
	private String savePointsString(Node n){//n0$n1,0,0#n2,0,0
		String s = "";
		s += n.getName();
		for(int i = 0; i < n.getNumberOfPointsOut(); i++){
			s +=  "\t" + n.getPointOut(i).getName() + "\t" + n.getWeightOfPointOut(i);
		}
		return s;
	}
	//LOAD
	
	public Network loadNetworkFromPath(String path) throws IOException{
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
	    return loadNetworkFromString(everything);
	}
	
	private Network loadNetworkFromString(String data) throws FileNotFoundException{
		Scanner dataScanner = new Scanner(data);
		int numberOfNodes = dataScanner.nextInt();
		dataScanner.nextLine();
		int hiddenLayers = dataScanner.nextInt();
		dataScanner.nextLine();
		runner.setNeuralSightRadius(dataScanner.nextInt());
		dataScanner.nextLine();
		
		allNodes = new NodeRow();
		buttons = new NodeRow(); 
		stimuli = new NodeRow(); 
		memoryNodes = new NodeRow(); 
		
		Network net = new Network(hiddenLayers, allNodes, buttons, stimuli, memoryNodes);
		
		for(int i = 0; i < numberOfNodes; i++)
			createNodeFromSave(dataScanner.nextLine());
		for(int i = 0; i < numberOfNodes; i++)
			createPointsFromSave(dataScanner.nextLine());
		
		dataScanner.close();
		return net;
	}
	
	//LOAD NODE
	private void createNodeFromSave(String lineData){
		Scanner scan = new Scanner(lineData);
		String nodeName = scan.next();
		double nodeX = scan.nextDouble(),
			nodeY = scan.nextDouble();
		Node n = null;
		
		if(nodeName.equals("left") || nodeName.equals("right") || nodeName.equals("up") || nodeName.equals("down"))
		{
			n = new Nodule(nodeName, nodeX, nodeY);
			buttons.add(n);
		}
		else
			switch(nodeName.charAt(0)){
				case 'n': {
					n = new Nodule(nodeName, nodeX, nodeY);
					break;
				}
				case 'm':{
					n = new Memory(nodeName, nodeX, nodeY);
					break;
				}
				case 'w': {
					n = new Stimnode(nodeName, nodeX, nodeY, Runner.BLACK, Runner.WHITE);//n0,0.10123123,0.082,1,1
					stimuli.add(n);
					Stimnode sn = (Stimnode)n;
					int sourceX = scan.nextInt(),
						sourceY = scan.nextInt();
					sn.giveSource(sourceX, sourceY);
					break;
				}
				case 't': {
					n = new Stimnode(nodeName, nodeX, nodeY, Runner.PINK, Runner.GRAY);
					stimuli.add(n);
					Stimnode sn = (Stimnode)n;
					sn.giveSource(scan.nextInt(), scan.nextInt());
					break;
				}
			}
		allNodes.add(n);
		scan.close();
	}
	
	//LOAD POINTS
	private void createPointsFromSave(String lineData){
		Scanner lineScanner = new Scanner(lineData);
		String nodeName = lineScanner.next();
		Node n = allNodes.getNodeWithName(nodeName);
		while(lineScanner.hasNext()){
			n.pointTo(allNodes.getNodeWithName(lineScanner.next()), lineScanner.nextDouble());
		}
		lineScanner.close();
	}
}
