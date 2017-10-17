package stuff;

import ui.Colour;
import ui.DrawUserInterface;
import ui.UserInterfaceFactory;

public class View {
	private static int ROOM_WIDTH = 900,
		ROOM_HEIGHT = 700,
		BUFFER = 6,
		LINE_HEIGHT = 15,
		BUTTON_DIAMETER = 50,
		OPTION_LIMIT = 8;
	private Colour black, red, green, darkBlue, lightBlue, gray;
	private Adventure adventure;
	private String tooltip;
	private DrawUserInterface ui;
	private boolean detailed;
	
	View(Adventure sourceAdventure){
		black = new Colour(0,0,0);
		red = new Colour(255,0,0);
		green = new Colour(50,190,50);
		darkBlue = new Colour(0,0,190);
		lightBlue = new Colour(30,144,255);
		gray = new Colour(180,180,180);
		ui = UserInterfaceFactory.getDrawUI(ROOM_WIDTH, ROOM_HEIGHT);
		adventure = sourceAdventure;
		tooltip = "";
		detailed = true;
	}
	
	public void draw(Page thisPage){
		ui.clear();
		drawLeftBlock(thisPage.getOptionsIn());
		drawCenterBlock(thisPage);
		drawRightBlock(thisPage.getOptionsOut());
		
		ui.showChanges();
	}

	private void drawCenterBlock(Page thisPage) {
		ui.drawSquare(ROOM_WIDTH/4+BUFFER, ROOM_HEIGHT-BUFFER, ROOM_WIDTH/2-(2*BUFFER), ROOM_HEIGHT-(2*BUFFER), black, false);
		ui.setSquareHotspot(ROOM_WIDTH/4+BUFFER, ROOM_HEIGHT-BUFFER, ROOM_WIDTH/2-(2*BUFFER)-BUTTON_DIAMETER, ROOM_HEIGHT-(2*BUFFER), "pageButton");
		String pageText = thisPage.getText();
		drawStringAt(ROOM_WIDTH/4+BUFFER, ROOM_HEIGHT-BUFFER, "Page " + (thisPage.getGlobalIndex()+1) + ":", black, 40);
		
		if(pageText.equals(""))
			drawStringAt(ROOM_WIDTH/4+BUFFER, ROOM_HEIGHT-30-BUFFER, "<No Page Description>", black, 50);
		else
			drawStringAt(ROOM_WIDTH/4+BUFFER, ROOM_HEIGHT-30-BUFFER, pageText, black, 40);
		makeHotspotCalled(ROOM_WIDTH*3/4-BUFFER-BUTTON_DIAMETER/2, ROOM_HEIGHT-BUFFER-BUTTON_DIAMETER/2, BUTTON_DIAMETER, black, "mapButton", "MAP");
		makeHotspotCalled(ROOM_WIDTH*3/4-BUFFER-BUTTON_DIAMETER/2, BUFFER+BUTTON_DIAMETER/2, BUTTON_DIAMETER, black, "masterButton", "OPT");
	}

	public void draw(ParticleRow par){
		ui.clear();
		
		for(int i = 0; i < par.getnumberOfParticles(); i++){
			for(int j = 0; j < par.getnumberOfParticles(); j++){
				if(i != j && par.getParticleAt(i).getPage().hasOptionTo(par.getParticleAt(j).getPage()))
					drawParticleStripe(par.getParticleAt(i), par.getParticleAt(j));
			}
		}
		for(int i = 0; i < par.getnumberOfParticles(); i++){
			drawParticle(par.getParticleAt(i));
		}
		makeHotspotCalled(ROOM_WIDTH*3/4-BUFFER-BUTTON_DIAMETER/2, ROOM_HEIGHT-BUFFER-BUTTON_DIAMETER/2, BUTTON_DIAMETER, black, "mapButton", "MAP");
		ui.drawText(BUFFER, BUFFER, tooltip, black);
		ui.showChanges();
	}

	private void drawParticleStripe(Particle from, Particle to) {
		Colour col = green;
		if(adventure.getThisPage() == from.getPage())
			col = lightBlue;
		if(adventure.getThisPage() == to.getPage())
			col = darkBlue;
		
		if(from.isWithinBounds() && to.isWithinBounds()){
			connectingStripe(from, to, col);
		}
		
	}

	private void connectingStripe(Particle from, Particle to, Colour col) {
		int x1 = roomX(from),
			y1 = roomY(from),
			x2 = roomX(to),
			y2 = roomY(to);
		ui.drawLine(x1, y1+1, (x1+x2)/2, (y1+y2)/2, black);
		ui.drawLine(x1+1, y1, (x1+x2)/2, (y1+y2)/2, black);
		ui.drawLine(x1, y1+1, x2, y2, col);
	}

	private int roomX(Particle pa){
		return (int)(ROOM_WIDTH*pa.getX());
	}
	
	private int roomY(Particle pa){
		return (int)(ROOM_HEIGHT*pa.getY());
	}

	private void drawParticle(Particle pa) {
		Page p = pa.getPage();
		int x = (int)(ROOM_WIDTH*pa.getX()),
			y = (int)(ROOM_HEIGHT*pa.getY());
		Colour fillCol = green;
		int outlineThickness = 2,
				diameter;
		if(adventure.isRadial())
			diameter = (int)(pa.getDiameter()*pa.getClosenessToScreen());
		else
			diameter = pa.getDiameter();
		if(pa.getPage().getText().length() == 0)
			fillCol = red;
		if(pa.getPage().getGlobalIndex() == 0)
			fillCol = gray;
		if(pa.getPage() == adventure.getThisPage()){
			fillCol = darkBlue;
		}
		Colour outlineCol = black;
		if(pa.getPage().getOptionsOut().getNumberOfOptions() == 0){
			outlineCol = gray;
			outlineThickness = 5;
		}
		if(pa.getPage().getNumberOfLooseEnds() > 0){
			outlineCol = red;
			outlineThickness = 7;
		}
		ui.drawCircle(x, y, diameter+outlineThickness, diameter+outlineThickness, outlineCol, true);	
		ui.drawCircle(x, y, diameter, diameter, fillCol, true);
		ui.setCircleHotspot(x, y, diameter+outlineThickness, diameter+outlineThickness, "dot$" + pa.getPage().getGlobalIndex());
		if(detailed)
			ui.drawText(x-3, y+8, ((p.getGlobalIndex()+1) + ""), black);
	}

	private void drawLeftBlock(OptionRow optionsIn){
		int numBlocks = optionsIn.getNumberOfOptions();
		if(numBlocks == 0)
			return;
		int leftPixel = BUFFER,
			topPixel = ROOM_HEIGHT,
			boxWidth = ROOM_WIDTH/4-(2*BUFFER),
			boxHeight = ROOM_HEIGHT/numBlocks-(2*BUFFER);
		for(int i = 0; i < numBlocks; i++){
			ui.setSquareHotspot(leftPixel, (int)(topPixel-topPixel*(1.0*i/numBlocks))-BUFFER, boxWidth, boxHeight, "in$" + i);
			ui.drawSquare(leftPixel, (int)(topPixel-topPixel*(1.0*i/numBlocks))-BUFFER, boxWidth, boxHeight, black, false);
			String text = optionsIn.getTextAt(i);
			if(text.equals(""))
				text = "<(Turn to #)>";
			drawStringAt(leftPixel, (int)(topPixel-topPixel*(1.0*i/numBlocks))-BUFFER, text, green, 25);
		}
	}
	
	private void drawRightBlock(OptionRow optionsOut){
		int numBlocks = optionsOut.getNumberOfOptions();
		boolean moreOptionsEnabled;
		if(optionsOut.getNumberOfOptions() < OPTION_LIMIT){
			numBlocks++;
			moreOptionsEnabled = true;
		}else{
			moreOptionsEnabled = false;;
		}
		
		int leftPixel = ROOM_WIDTH*3/4+BUFFER,
			topPixel = ROOM_HEIGHT,
			boxWidth = ROOM_WIDTH/4-(2*BUFFER),
			boxHeight = ROOM_HEIGHT/numBlocks-(2*BUFFER);
		for(int i = 0; i < numBlocks; i++){
			if(i == numBlocks-1 && moreOptionsEnabled){
				ui.drawSquare(leftPixel, (int)(topPixel-topPixel*(1.0*i/numBlocks))-BUFFER, boxWidth, boxHeight, black, true);
				ui.setSquareHotspot(leftPixel, (int)(topPixel-topPixel*(1.0*i/numBlocks))-BUFFER, boxWidth, boxHeight, "addOut");
			}
			else{
				Colour col = red;
				if(optionsOut.getLinksToAt(i) != null)
					col = green;
				ui.setSquareHotspot(leftPixel, (int)(topPixel-topPixel*(1.0*i/numBlocks))-BUFFER, boxWidth-BUTTON_DIAMETER, boxHeight, "out$" + i);
				makeHotspotCalled(leftPixel+boxWidth-BUTTON_DIAMETER/2, (int)(topPixel-topPixel*(1.0*i/numBlocks))-BUFFER-boxHeight+BUTTON_DIAMETER/2, BUTTON_DIAMETER, black, "edi$" + i, "OPT");
				
				ui.drawSquare(leftPixel, (int)(topPixel-topPixel*(1.0*i/numBlocks))-BUFFER, boxWidth, boxHeight, black, false);
				String text = optionsOut.getTextAt(i);
				if(text.equals(""))
					text = "<(Turn to #)>";
				drawStringAt(leftPixel, (int)(topPixel-topPixel*(1.0*i/numBlocks))-BUFFER, text, col, 25);
			}
		}
	}
	
	private void drawStringAt(int scrnX, int scrnY, String text, Colour col, int charLimit){
		while(text.length() > 0){
			String thisLine = text;
			if(text.length() > charLimit){
				int splitIndex = findSplitInString(text, charLimit);
				thisLine = text.substring(0, splitIndex).trim();
				text = text.substring(splitIndex);
			}else
				text = "";
			ui.drawText(scrnX, scrnY-LINE_HEIGHT, thisLine, col);
			scrnY -= LINE_HEIGHT;
		}
	}
	
	private int findSplitInString(String s, int suggestedSplit){
		int i = suggestedSplit;
		while(s.charAt(i) != ' '){
			i--;
			if(i < 1)
				return suggestedSplit;
		}
		return i;
	}
	
	private void makeHotspotCalled(int x, int y, int diameter, Colour col, String buttonName, String threeLetterDisplay){
		ui.setCircleHotspot(x, y, diameter, diameter, buttonName);
		ui.drawCircle(x, y, diameter, diameter, col, false);
		ui.drawText(x-13, y-6, threeLetterDisplay, black);
	}

	public DrawUserInterface getUi() {
		return ui;
	}

	public void setTooltip(String text) {
		tooltip = text;
	}
	
	public void toggleDetailed(){
		if(detailed)
			detailed = false;
		else
			detailed = true;
	}
}
