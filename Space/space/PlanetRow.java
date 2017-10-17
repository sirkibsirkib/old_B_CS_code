package space;

public class PlanetRow {
	private Planet[] row;
	private int numberOfPlanets;
	static final double MAX_PLANET_SIZE = 5;
	
	PlanetRow(int maxNumberOfPlanets){
		row = new Planet[maxNumberOfPlanets];
		numberOfPlanets = 0;
	}
	
	public void add(Planet newPlanet){
		row[numberOfPlanets] = newPlanet;
		numberOfPlanets++;
	}
	
	public void addNewPlanet(String planetName, double size){
		Planet newPlanet = new Planet(planetName, size);
		add(newPlanet);
	}
	
	public void makeAllPlanetsBigger(){
		for(int i = 0; i < numberOfPlanets; i++){
			if(row[i].getSize() < MAX_PLANET_SIZE)
				row[i].makeBigger();
		}
	}
	
	//GETS
	
	public Planet getPlanetAt(int index){
		return row[index];
	}
	
}
