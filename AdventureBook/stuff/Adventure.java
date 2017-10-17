package stuff;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.InputMismatchException;
import java.util.Scanner;

import ui.UIAuxiliaryMethods;

public class Adventure {
	private PageRow pr;
	private View v;
	private Page thisPage;
	private ParticleRow par;
	private boolean drawingMap, hasDrawn;
	private String savePath, graphMode;
	
	Adventure(){
		calcSavePath();
		//for ease of debug
		
		v = new View(this);
		setup();
	}
	
	private void calcSavePath() {
		savePath = Adventure.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		int i = savePath.length()-1;
		while(savePath.charAt(i) != '\\' && savePath.charAt(i) != '/'){
			i--;
		}
		savePath = savePath.substring(0, i+1);
		savePath = savePath.replaceAll("%20", " ");
		
		if(savePath.equals("/C:/Users/Christopher/Dropbox/EclipseWorkspace/Home/bin/")){
			savePath = "C:/Users/Christopher/Desktop/";
			System.out.println("welcome to debug :)");
		}
	}

	private void setup(){
		pr = new PageRow();
		pr.addPage(new Page(0));
		thisPage = pr.getPageAt(0);
		par = new ParticleRow();
		par.addParticle(new Particle(.5, .5, thisPage));
		drawingMap = false;
		graphMode = "Spring";
		hasDrawn = false;
	}
	
	private void reconstructParticles(){
		par = new ParticleRow();
		int numberOfParticles = pr.getNumberOfPages();
		int gridWidth = (int)(Math.sqrt(numberOfParticles) + .5),
			gridWidth2 = numberOfParticles / gridWidth;
		if(numberOfParticles > gridWidth*gridWidth2)
			gridWidth++;
		int nextParticle = 0;
		for(int i = 0; i < gridWidth; i++){
			for(int j = 0; j < gridWidth2; j++){
				if(nextParticle < numberOfParticles){
					par.addParticle(new Particle(.5/(gridWidth+1)*(i+1)+.25, .5/(gridWidth2+1)*(j+1)+.25, pr.getPageAt(nextParticle)));
					nextParticle++;
				}
			}
		}
	}
	
	private void start() throws InterruptedException{
		Handler handler = new Handler(this, v);
		new Thread(handler).start();
		drawLoop();
	}
	
	private void drawLoop() throws InterruptedException{
		while(true){
			if(drawingMap){
				switch(graphMode){
				case "Spring":	springLoop();	break;
				case "Flag":	flagLoop();		break;
				case "Radial":	radialLoop();	break;
				}
			}
			else{
				v.draw(thisPage);
				Thread.sleep(300);
			}
		}
	}
	
	private void springLoop() throws InterruptedException {
		par.springEmbed();
		while(graphMode.equals("Spring") && drawingMap && par.getGraphFlux() > par.getnumberOfParticles()/20+1.5){
			Thread.sleep(40);
			v.draw(par);
			int animationSpeed = par.getnumberOfParticles()/2 + 1;
			for(int i = 0; i < animationSpeed; i++){
				par.springEmbed();
			}
		}
		Thread.sleep(40);
		v.draw(par);
	}
	
	private void flagLoop() throws InterruptedException {
		if(!hasDrawn){
			par.wavyGridLayout();
			par.swapMinimizePositons();
			hasDrawn = true;
		}
		v.draw(par);
		Thread.sleep(300);
	}

	private void radialLoop() throws InterruptedException {
		if(!hasDrawn){
			par.radialEmbed(6, thisPage.getGlobalIndex());
			hasDrawn = true;
		}
		v.draw(par);
		Thread.sleep(300);
	}

	private String askUserForText(String prompt) {
		String s = "";
		s = UIAuxiliaryMethods.askUserForString(prompt);
		if(s == null)
			s = "";
		s.trim();
		return s;
	}
	
	private String askUserForText() {
		return askUserForText("Please input text") ;
	}
	
	public void pageButtonPressed(){
		String choice;
		if(thisPage.getText().equals(""))
			choice = "Rewrite text";
		else choice = askForChoice("Select an action for this page",
				"Cancel",
				"Append to text",
				"Rewrite text",
				"Change Page Number",
				"Delete page");
		switch(choice){
		case "Append to text":	thisPage.setText(thisPage.getText() + " " + askUserForText());	break;
		case "Rewrite text":	thisPage.setText(askUserForText());								break;
		case "Delete page":	{
			if(askUserBeforeDeleting()){
				if(pr.getNumberOfPages() > 1){
					par.removeParticleWithPage(thisPage);
					pr.removePageAt(thisPage.getGlobalIndex());
					thisPage = pr.getPageAt(0);
					show("Page deleted");
				}else{
					thisPage.reset();
					show("Page deleted");
				}
			}
			break;
		}
		case "Change Page Number":{
			int pageChoice = -1;
			if(pr.getNumberOfPages() > 1){
				while(pageChoice < 0 || pageChoice >= pr.getNumberOfPages()){
					String input = "";
					try{
						input = UIAuxiliaryMethods.askUserForString("Please input a valid page number:\n" + "(1 - " + pr.getNumberOfPages() + ")");
						Scanner inputScanner = new Scanner(input);
						pageChoice = inputScanner.nextInt() -1;
						inputScanner.close();
					}
					catch(InputMismatchException e){
						pageChoice = -1;
					}
					catch(NullPointerException e){
						pageChoice = -1;
					}
				}
				pr.movePageIntoIndex(thisPage, pageChoice);
			}else
				show("There is only one page");
			break;
		}
		}
	}
	
	private boolean askUserBeforeDeleting() {
		 String choice = askForChoice("Are you certain you wish to delete this page?\nThis action can't be undone",
				"No",
				"Yes");
		if(choice.equals("Yes")){
			return true;
		}
		return false;
	}

	public void masterButtonPressed() throws IOException{
		 String choice = askForChoice("Select an action for this book",
				"Cancel",
				"Save",
				"Load",
				"New Project",
				"Shuffle Pages",
				"Write book to file");
		switch(choice){
		case "Shuffle Pages":{
			pr.shufflePages();
			break;
		}
		case "Write book to file":{
			int looseEndCount = pr.countLooseEnds();
			Page looseEnd = pr.getALoosePage();
			
			if(looseEnd == null)
				new Bookwriter(savePath, getWritePath(), pr);
			else{
				show("WARNING:\n(" + looseEndCount + ") links without pages have been found.\n"
						+ "Proceeding with write will leave out these links!");
				String reply = askForChoice("Proceed with write?", "No", "Yes");
				if(reply.equals("Yes"))
					new Bookwriter(savePath, getWritePath(), pr);
				else
					thisPage = looseEnd;
			}
			break;
		}
		case "Save":{
			save();
			break;
		} 
		case "Load":{
			load();
			break;
		}
		case "New Project":{
			String reply = askForChoice("Close current project and open a blank one?\n"
					+ "unsaved progress will be lost.", "No", "Yes");
			if(reply.equals("Yes")){
				setup();
				show("New project successfully created");
			}
			break;
		}
		}
	}
	
	
	private String getWritePath() {
		String wp = "";
		do{
			wp = UIAuxiliaryMethods.askUserForString("Input a valid file name for book.\n"
					+ "The finished txt will be given this name.\n"
					+ "Be careful not to overwrite something important!");
		}while(wp.length() < 1);
		return wp + ".txt";
		
	}

	public void show(String message){
		UIAuxiliaryMethods.showMessage(message);
	}

	public void load() throws IOException {
		String fileName = "";
		fileName = UIAuxiliaryMethods.askUserForString("Please input the file name of the\nof the save file. No extension\n"
				+ "is required. Eg \"filename.txt\"");
		if(fileName == null || fileName.length() < 1){
			show("Load aborted");
			return;
			}
		PageRow loaded = new SLBook().pathLoad(savePath, fileName + ".txt");
		if(loaded == null)
			show("Failed to find \"" + fileName + ".txt\"\nBe sure the desired file is in\nthe same directory as the executable.");
		else{
			pr = loaded;
			thisPage = pr.getPageAt(0);
			reconstructParticles();
			drawingMap = false;
			graphMode = "Spring";
			hasDrawn = false;
		}
	}
	
	public String askForChoice(String question, String... choices){
		String choice =  choices[0];
		choice = UIAuxiliaryMethods.askUserForChoice(question, choices);
		return choice;
	}

	public void outButtonPressed(int outIndex) {
		Option opt = thisPage.getOptionsOut().getOption(outIndex);
		if(opt.getLinksTo() != null){
			thisPage = opt.getLinksTo();
		}
	}

	private Page askUserWhichPage() {
		String choice = "No input";
		choice = UIAuxiliaryMethods.askUserForChoice("Select the desired page with text...", pageList());
		
		int separatorIndex = 0;
		if(choice.contains(")")){
			while(choice.charAt(separatorIndex) != ')')
				separatorIndex++;
			choice = choice.substring(0, separatorIndex);
			return pr.getPageAt(Integer.parseInt(choice)-1);
		}
		return thisPage;
	}

	public void addOut() {
		 String choice = askForChoice("Select an action to perform",
				"Cancel",
				"Create a new outgoing option");
		if(choice.equals("Create a new outgoing option")){
			Option newOut = new Option(askUserForText("Input link text\nAny # characters will be\nreplaced with the page number.\nLeave blank for \"(Turn to #)\""), thisPage);
			thisPage.addOptionOut(newOut);
		}
	}

	public void inButtonPressed(int inIndex) {
		Option opt = thisPage.getOptionsIn().getOption(inIndex);
		if(opt.getLinksTo() != null){
			thisPage = opt.getLinksFrom();
		}
	}
	
	private String[] pageList(){
		String[] pageNames = new String[pr.getNumberOfPages()];
		for(int i = 0; i < pageNames.length; i++){
			String pageName = pr.getPageAt(i).getText();
			if(pageName.length() < 40)
				pageNames[i] = (i+1) + ") " + pr.getPageAt(i).getText();
			else if(pageName.length() == 0)
				pageNames[i] = (i+1) + ") <No Page Description>";
			else
				pageNames[i] = (i+1) + ") " + pr.getPageAt(i).getText().substring(0, 40) + "...";
		}
		return pageNames;
	}

	public void outEditButtonPressed(int outIndex) {
		Option opt = thisPage.getOptionsOut().getOption(outIndex);
		String choice;
		if(opt.getLinksTo() != null){
			choice = askForChoice("Select an action for this option",
					"Cancel",
					"Remove page link",
					"Append to text",
					"Rewrite text");
		}else{
			choice = askForChoice("Select an action for this option",
					"Cancel",
					"Create and link new page",
					"Link to existing page",
					"Append to text",
					"Delete option",
					"Rewrite text");
		}
		switch(choice){
		case "Navigate to Page":	thisPage = opt.getLinksTo();								break;
		case "Create and link new page":{
			Page p = new Page(pr.getNumberOfPages()); pr.addPage(p);
			addParticle(thisPage, p);
			opt.setLinksTo(p); p.addOptionIn(opt);
			thisPage = p;
			
			break;
		}
		case "Link to existing page":{
			Page p = askUserWhichPage();
			opt.setLinksTo(p); p.addOptionIn(opt); thisPage = p;								break;
		}
		case "Remove page link":{
			Page targetPage = opt.getLinksTo();
			opt.setLinksTo(null);
			targetPage.pruneDeadIncoming(); 													break;
		}
		case "Append to text":	opt.setText((opt.getText() + " " + askUserForText()).trim());	break;
		case "Delete option":{
			thisPage.findAndDelete(opt);
			break;
		}
		case "Rewrite text":	opt.setText(askUserForText());									break;
		}
	}
	
	private void addParticle(Page previousPage, Page nextPage){
		Particle previousParticle = par.getParticleWithPage(previousPage);
		double prevX = previousParticle.getX(),
			prevY = previousParticle.getY(),
			nextX,
			nextY;
		Particle newParticle;
		do{
			nextX = prevX + Fun.rrg(.3) - .15;
			nextY = prevY + Fun.rrg(.3) - .15;
			newParticle = new Particle(nextX, nextY, nextPage);
		}while(nextX < .1 || nextX > .9 || nextY < .1 || nextY > .9
				|| newParticle.distanceToWillBe(.5, .5) > .49);
		par.addParticle(newParticle);
	}

	public void toggleMap() {
		if(drawingMap)
			drawingMap = false;
		else{
			hasDrawn = false;
			if(pr.getNumberOfPages() > 39 && graphMode.equals("Spring")){
				String choice =  askForChoice("WARNING!\nWith 40+ nodes, this form of\nembedding"
						+ " has an extremely high\ncomputational complexity and time.\n"
						+ "Radial mode is advised.\nProceed with \"Spring\"", "No", "Yes");
				if(choice.equals("No"))
					toggleAnimationStyle();
			} else if(pr.getNumberOfPages() > 119 && graphMode.equals("Flag")){
				String choice =  askForChoice("WARNING!\nWith 120+ nodes, this form of\nembedding"
						+ " has an extremely high\ncomputational complexity and time.\n"
						+ "Radial mode is advised.\nProceed with \"Spring\"", "No", "Yes");
				if(choice.equals("No"))
					toggleAnimationStyle();
			}
				
			drawingMap = true;
		}
	}

	public Page getThisPage() {
		return thisPage;
	}

	public void setToolTipFromPageIndex(int pageIndex) {
		String fullText = pr.getPageAt(pageIndex).getText();
		if(fullText.length() > 80)
			v.setTooltip("Page " + (pageIndex+1) + ": " + fullText.substring(0, 80) + "...");
		else if(fullText.length() == 0)
			v.setTooltip("Page " + (pageIndex+1) + ": <No Page Description>");
		else
			v.setTooltip("Page " + (pageIndex+1) + ": " + fullText.substring(0, fullText.length()));
	}
	
	public void removeTooltip() {
		v.setTooltip("");
	}
	
	public void jumpToPage(int pageIndex) {
		v.setTooltip("");
		if(graphMode.equals("Radial")){
			hasDrawn = false;
			if(thisPage.getGlobalIndex() == pageIndex)
				drawingMap = false;
		}
			else
			drawingMap = false;
		thisPage = pr.getPageAt(pageIndex);
	}

	public void askForExit() {
		 String choice = askForChoice("Are you certain you want to exit?",
				"No",
				"Yes");
		if(choice.equals("Yes")){
			System.exit(1);
		}
	}

	public void save() throws FileNotFoundException, UnsupportedEncodingException {
		String fileName = "fileName";
		fileName = UIAuxiliaryMethods.askUserForString("Please input the file name of the\nof the save file. No extension\n"
				+ "is required. Eg \"" + fileName + "\"");
		if(fileName != null && fileName.length() > 0)
			new SLBook().save(pr, savePath, fileName + ".txt");
		else
			show("Save aborted");
	}

	public void tryToGoToPageIndex(int index) {
		if(index >= 0 && index < pr.getNumberOfPages()){
			thisPage = pr.getPageAt(index);
			if(graphMode.equals("Radial"))
				hasDrawn = false;
				
		}	
	}
	
	public void newPageHotkey(){
		if(drawingMap)
			return;
		Option opt = new Option("", thisPage);
		thisPage.addOptionOut(opt);
		
		Page p = new Page(pr.getNumberOfPages()); pr.addPage(p);
		addParticle(thisPage, p);
		opt.setLinksTo(p); p.addOptionIn(opt);
		thisPage = p;
	}
	
	
	//DEBUG DEBUG DEBUG DEBUG DEBUG DEBUG DEBUG DEBUG
//	public void newLinkHotkey(){
//		if(pr.getNumberOfPages() < 2)
//			return;
//		if(drawingMap)
//			par.springEmbed();
//		Page to;
//		do{
//			to = pr.getPageAt(Fun.rng(pr.getNumberOfPages()));
//		}while(thisPage == to);
//		
//		Option opt = new Option("", thisPage);
//		thisPage.addOptionOut(opt);
//		opt.setLinksTo(to); to.addOptionIn(opt);
//	}
	
	public void toggleAnimationStyle(){
		graphMode = UIAuxiliaryMethods.askUserForChoice("Select a graph display mode", "Spring", "Flag", "Radial");
		hasDrawn = false;
		switch(graphMode){
		case "Spring":	chooseSpring();		break;
		case "Flag":	chooseFlag();		break;
		case "Radial":	chooseRadial();		break;
		}
	}
	
	private void chooseSpring() {
		boundParticles();
		if(par.getnumberOfParticles() < 40)
			show("Page graph will now be an animated spring embedding.");
		else{
			String choice =  askForChoice("WARNING!\nWith 40+ nodes, this form of\nembedding"
					+ " has an extremely high\ncomputational complexity and time.\n"
					+ "Radial mode is advised.\nProceed with \"Spring\"", "No", "Yes");
			if(choice.equals("No"))
				toggleAnimationStyle();
		}
	}

	private void boundParticles() {
		int numberOfParticles = pr.getNumberOfPages();
		int gridWidth = (int)(Math.sqrt(numberOfParticles) + .5),
			gridWidth2 = numberOfParticles / gridWidth;
		if(numberOfParticles > gridWidth*gridWidth2)
			gridWidth++;
		int nextParticle = 0;
		for(int i = 0; i < gridWidth; i++){
			for(int j = 0; j < gridWidth2; j++){
				if(nextParticle < numberOfParticles){
					Particle pa = par.getParticleAt(nextParticle);
					if(!pa.isWithinBounds()){
						pa.setX(.5/(gridWidth+1)*(i+1)+.25);
						pa.setY(.5/(gridWidth2+1)*(j+1)+.25);
					}
					nextParticle++;
				}
			}
		}
	}

	private void chooseFlag() {
		if(par.getnumberOfParticles() < 120)
			show("Page graph will now be a static flag-shaped grid embedding.");
		else{
			String choice =  askForChoice("WARNING!\nWith 120+ nodes, this form of\nembedding"
					+ " has an extremely high\ncomputational complexity and time.\n"
					+ "Radial mode is advised.\nProceed with \"Spring\"", "No", "Yes");
			if(choice.equals("No"))
				toggleAnimationStyle();
		}
	}
	
	private void chooseRadial() {
		show("Page graph will now be a radial emedding.\nNodes more than 4 page links away are hidden.");
	}
	
	public boolean getDrawingMap(){
		return drawingMap;
	}
	
	public static void main(String[] args) throws InterruptedException {
		new Adventure().start();
	}

	public boolean isRadial() {
		return graphMode.equals("Radial");
	}
}
