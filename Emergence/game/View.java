package game;

import ui.DrawUserInterface;
import ui.UserInterfaceFactory;

public class View {
	private DrawUserInterface ui;
	
	View(){
		ui = UserInterfaceFactory.getDrawUI(700,500);
	}
	
	public void draw(SolarSystem ss){
		for(int i = 0 ; i < ss.getNumberOfPlanets(); i++){
			drawPlanet(ss.getPlanet(i));
		}
	}
	
	private void drawPlanet(Planet p){
		ui.drawCircle(arg0, arg1, arg2, arg3, arg4, arg5);
	}
	
	private int 
}
