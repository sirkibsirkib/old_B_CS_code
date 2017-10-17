package game;

public class SolarSystem {
	private PlanetRow system;
	
	public void addPlanet(Planet p){
		system.addPlanet(p);
	}
	
	public Planet getPlanet(int index){
		return system.getPlanet(index);
	}
	
	public int getNumberOfPlanets(){
		return system.getNumberOfPlanets();
	}
}
