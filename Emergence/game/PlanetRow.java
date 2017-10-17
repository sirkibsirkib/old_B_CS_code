package game;

public class PlanetRow {
	private Planet[] planets;
	private int numberOfPlanets;
	
	PlanetRow(){
		planets = new Planet[4];
		numberOfPlanets = 0;
	}
	
	public void addPlanet(Planet p){
		if(numberOfPlanets - 1 >= planets.length)
			doubleArraySize();
		planets[numberOfPlanets] = p;
		numberOfPlanets++;
	}
	
	public void removePlanet(int index){
		if(numberOfPlanets < planets.length/2 && planets.length > 4)
			halveArraySize();
		for(int i = 0; i < numberOfPlanets-1; i++){
			planets[i] = planets[i+1];
		}
		numberOfPlanets--;
	}
	
	private void doubleArraySize(){
		Planet[] planets2 = new Planet[planets.length];
		for(int i = 0; i < numberOfPlanets; i++){
			planets2[i] = planets[i];
		}
		planets = planets2;
	}
	
	private void halveArraySize(){
		Planet[] planets2 = new Planet[planets.length/2];
		for(int i = 0; i < numberOfPlanets; i++){
			planets2[i] = planets[i];
		}
		planets = planets2;
	}
	
	public Planet getPlanet(int index){
		return planets[index];
	}
	
	public int getNumberOfPlanets(){
		return numberOfPlanets;
	}
}
