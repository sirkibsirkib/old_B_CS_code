
public class Puppeteer extends PlanetWars{
	//////////BOT REPLY ////////////
		
	@Override
	public synchronized void issueOrder(Planet f, Planet t){
		issueOrder(f.getID(), t.getID());
	}
	
	@Override
	public synchronized void issueOrder(int fromID, int toID){
		if(getPlanet(fromID).getOwner() == PLAYER){ //assume that caller == P1
			o1 = new Order(p1State.get(fromID), p1State.get(toID));
			bot1Done = true;
		}else if(getPlanet(fromID).getOwner() == ENEMY){
			o2 = new Order(p1State.get(fromID), p1State.get(toID));
			bot2Done = false;
		}		
	}
	
	
	////////// OVERRIDES ///////////
	
	@Override
	public void finishTurn(){
		//LEL i dont care
	}
	
	@Override
	public List<Planet> getAllPlanets(){
		return buildPlanetList(PLAYER, NEUTRAL, ENEMY);
	}
	
	@Override
	public List<Planet> getEnemyPlanets(){
		return buildPlanetList(ENEMY);
	}
	
	@Override
	public List<Planet> getMyPlanets(){
		return buildPlanetList(PLAYER);
	}
	
	@Override
	public List<Planet> getNeutralPlanets(){
		return buildPlanetList(NEUTRAL);
	}
	
	@Override
	public List<Planet> getNotMyPlanets(){
		return buildPlanetList(ENEMY, NEUTRAL);
	}
	
	@Override
	public int getNumShips(int PLAYER){
		List<MPlanet> p = p1State.getAllPlanets();
		int count = 0;
		for(MPlanet x : p){
			if(x.owner == PLAYER){
				count += x.numShips;
			}
		}
		return count;
	}
	
	@Override
	public Planet getPlanet(int planetID){
		MPlanet m = p1State.get(planetID).floop();
		flip(m);
		return new EPlanet(m);
	}
	
	@Override
	public void log(Object... args){
		//nothing
	}
	
	
	
	////////// AUX OVERRIDES ////////
	
	
	
	private List<Planet> buildPlanetList(int... owners){
		if(!army1){
			for(int i = 0; i < owners.length; i++){
				if(owners[i] == PLAYER) owners[i] = ENEMY;
				else if(owners[i] == ENEMY) owners[i] = PLAYER;
			}
		}
		List<Planet> result = new ArrayList<>();
		for(MPlanet y : p1State.getAllPlanets()){
			MPlanet x = y.floop();
			
			
			if(x.owner == owners[0]){
				result.add(new EPlanet(flip(x)));
				
			}else if(owners.length >= 2 && x.owner == owners[1]){
				result.add(new EPlanet(flip(x)));
				
			}else if(owners.length >= 3 && x.owner == owners[2]){
				result.add(new EPlanet(flip(x)));
			}
		}
		return result;
	}
}
