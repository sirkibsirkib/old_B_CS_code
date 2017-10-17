package space;

public class God {
	private PlanetRow planets;
	private Planet mao;
	private static final int MAX_NUMBER_OF_PLANETS = 10;
	
	God(){
		planets = new PlanetRow(MAX_NUMBER_OF_PLANETS);
	}
	
	private void start(){
		
		planets.add(new Planet("mao", 2));
		planets.addNewPlanet("boob", 2);
		
		mao = planets.getPlanetAt(0);
		
		planets.makeAllPlanetsBigger();
		
	}
	
	public static void main(String[] args) {
		new God().start();
	}
}
