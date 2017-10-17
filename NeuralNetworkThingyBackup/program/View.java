package program;

import programEntity.Entity;
import programEntity.Player;
import programEntity.PlayerShadow;
import programEntity.Wall;
import programNode.Memory;
import programNode.Node;
import programNode.Nodule;
import programNode.Stimnode;
import ui.Colour;
import ui.DrawUserInterface;
import ui.UserInterfaceFactory;

public class View {
	static final int BORDER = 50,
			SPACE_DIAMETER = 500,
			ROOM_HEIGHT = BORDER + SPACE_DIAMETER + BORDER,
			ROOM_WIDTH = BORDER + SPACE_DIAMETER + BORDER + SPACE_DIAMETER + BORDER,
			DOT_DIAMETER = 8,
			LINE_SPACING = 20;
	
	private DrawUserInterface ui;
	
	public View(){
		ui = UserInterfaceFactory.getDrawUI(BORDER+SPACE_DIAMETER+BORDER+SPACE_DIAMETER+BORDER, BORDER+SPACE_DIAMETER+BORDER);
	}
	
	public void clear(){
		ui.clear();
		ui.showChanges();
	}
	
	public void draw(Network net, Room r, int drawRadius, int povRadius, double simProgress){
		drawSquares();
		drawNetwork(net, povRadius);
		drawRoom(r, drawRadius, povRadius);
		
		drawSimProgress(simProgress);
		ui.showChanges();
	}
	
	public void drawSimContext(String firstLine, String secondLine){
		ui.drawText(vtcXRight(0), vtcY(1)-(BORDER/2), firstLine, Runner.BLACK);
		ui.drawText(vtcXRight(0), vtcY(1)-(BORDER/2)-LINE_SPACING, secondLine, Runner.BLACK);
	}
	
	private void drawSimProgress(double simProgress){
		ui.drawCircle(vtcXLeft(simProgress), vtcY(0), 10, 10, Runner.BLACK, true);
	}
	
	public DrawUserInterface getUi(){
		return ui;
	}
	
	private void drawNetwork(Network net, int povRadius){
		for(int i = 0; i < net.getNumberOfNodes(); i++){
			drawNode(net.getNode(i), povRadius);
		}
	}
	
	private void drawRoom(Room r, int drawRadius, int povRadius){
		drawRadius += 2;
		int drawDiameter = drawRadius*2+1;
		for(int i = -drawRadius; i <= drawRadius; i++){
			for(int j = -drawRadius; j <= drawRadius; j++){
				Entity temp = r.getRelativeBlock(j, i);
				if(temp instanceof Wall){
					drawSquare((1.0*j+drawRadius)/drawDiameter, (1.0*i+drawRadius)/drawDiameter, 1.0*1/drawDiameter, Runner.BLACK, true);
				}
				if(temp instanceof PlayerShadow){
					drawSquare((1.0*j+drawRadius)/drawDiameter, (1.0*i+drawRadius)/drawDiameter, 1.0*1/drawDiameter, Runner.PINK, true);
				}
			}
		}
		drawPlayer(drawRadius, povRadius);
	}
	
	private void drawPlayer(int drawRadius, int povRadius){
		int drawDiameter = drawRadius*2+1;
		int povDiameter = povRadius*2+1;
		drawSquare((1.0*drawRadius)/drawDiameter, (1.0*drawRadius)/drawDiameter, 1.0*1/drawDiameter, Runner.RED, true);
		drawSquare((1.0*-povRadius+drawRadius)/drawDiameter, (1.0*-povRadius+drawRadius)/drawDiameter, 1.0*1/drawDiameter*povDiameter, Runner.PINK, false);
	}
	
	private void drawSquare(double startX, double startY, double diameter, Colour col, boolean fill){
		int pixelDiameter = vtcXLeft(startX+diameter) - vtcXLeft(startX) + 1;
		ui.drawSquare(vtcXLeft(startX), vtcY(startY), pixelDiameter, pixelDiameter, col, fill);
	}
	
	private void drawNode(Node n, int povRadius){
		ui.drawCircle(vtcXRight(n.getX()), vtcY(n.getY()), DOT_DIAMETER+2, DOT_DIAMETER+2, Runner.BLACK, true);
		if(n instanceof Nodule)
			drawNodule((Nodule)n);
		if(n instanceof Stimnode)
			drawStimnode((Stimnode)n);
		if(n instanceof Memory)
			drawMemory((Memory)n);
		if(!(n instanceof Stimnode) || povRadius < 3)
			ui.drawText(vtcXRight(n.getX()), vtcY(n.getY()), n.getName(), Runner.BLACK);
		
		for(int i = 0; i < n.getNumberOfPoints(); i++){
			ui.drawLine(vtcXRight(n.getX()), vtcY(n.getY()),
					vtcXRight(n.getXofPointedAtIndex(i)), vtcY(n.getYofPointedAtIndex(i)),
					colourFromDouble(n.getWeightOfPoint(i)));
		}
	}
	
	private void drawNodule(Nodule n){
		Colour col = Runner.RED;
		if(n.didFire())
			col = Runner.GREEN;
		ui.drawCircle(vtcXRight(n.getX()), vtcY(n.getY()), DOT_DIAMETER, DOT_DIAMETER, col, true);
	}
	
	private void drawStimnode(Stimnode n){
		Colour col = Runner.GRAY;
		if(n.didFire())
			col = n.getCol();
		ui.drawCircle(vtcXRight(n.getX()), vtcY(n.getY()), DOT_DIAMETER, DOT_DIAMETER, col, true);
	}
	
	private void drawMemory(Memory n){
		Colour col = Runner.RED;
		if(n.remembers())
			col = Runner.BLUE;
		ui.drawCircle(vtcXRight(n.getX()), vtcY(n.getY()), DOT_DIAMETER+1, DOT_DIAMETER-2, col, true);
	}
	
	private void drawSquares(){
		ui.drawSquare(vtcXLeft(0), vtcY(0), SPACE_DIAMETER, SPACE_DIAMETER, Runner.BLACK, false);
		ui.drawSquare(vtcXRight(0), vtcY(0), SPACE_DIAMETER, SPACE_DIAMETER, Runner.BLACK, false);
	}
	
	private int vtcXLeft(double value){ //value to coordinate
		return (int)(BORDER + value*SPACE_DIAMETER);
	}
	
	private int vtcXRight(double value){
		return (int)(BORDER + SPACE_DIAMETER + BORDER + value*SPACE_DIAMETER);
	}
	
	private int vtcY(double value){
		return ROOM_HEIGHT- (int)(BORDER + value*SPACE_DIAMETER);
	}
	
	private Colour colourFromDouble(double d){
		return new Colour(150,255-(int)(255*d),255-(int)(255*d));
	}
	
}
