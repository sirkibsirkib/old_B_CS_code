import java.util.ArrayList;
import java.util.List;

import planetWarsAPI.Bot;
import planetWarsAPI.Planet;
import planetWarsAPI.PlanetWars;

public class Simmer extends PlanetWars{
	Model p1State;
	int turn;
	boolean humanInputting;
	boolean army1 = true;
	long turnStart;
	MPlanet hFrom;
	Order o;
	Screen screen;
	
	public static void main(String[] args){
//		if(args.length != 3){
//			System.err.println("arg[0] name of the first bot eg: \'BullyBot\'");
//			System.err.println("arg[1] name of the second bot eg: \'BullyBot\'");
//			System.err.println("arg[2] name of the path to the map from maps folder eg: '5planets\\map1.txt'");
//			throw new Error();
//		}
		new Simmer().start();
	}
	
	Simmer(){
		turn = 0;
		humanInputting = false;
	}

	private void generateRandomMap() {
		List<MPlanet> planets = new ArrayList<>();
		for(int i = 0; i < 5; i++){
			planets.add(new MPlanet(i, 0, rng(7), rng(44) + 1,
					rdg(0.8) + 0.1, rdg(0.8) + 0.1));
		}
		
		planets.add(new MPlanet(5, 1, 3, 300,
				rdg(0.8) + 0.1, rdg(0.8) + 0.1));
		
		planets.add(new MPlanet(6, 2, 3, 300,
				rdg(0.8) + 0.1, rdg(0.8) + 0.1));

		p1State = new Model(planets);
	}

	private int rng(int i) {
		return (int) rdg(i);
	}

	private double rdg(double i) {
		return Math.random() * i;
	}

	private void start() {
		screen = new Screen(this);
		screen.run();
		
		Bot p1 = getBot(screen.getString("Input the name of the bot controlling Player 1\n"
                + "for example 'BullyBot'\n"
        		+ "submit blank for human controller",
                "Bot Name Select", "BullyBot"));
		Bot p2 = getBot(screen.getString("Input the name of the bot controlling Player 2\n"
                + "for example 'BullyBot'\n"
        		+ "submit blank for human controller",
                "Bot Name Select", "BullyBot"));
		
		getMap(screen.getString("Input the filename of the map for this match\n"
                + "for example '3planets/map1'\n"
        		+ "submit blank for a randomly-generated map",
                "Map select", ""));
		
		army1 = true;

		for(int i = 0; i < 3; i++)
			screen.render();
		sleep(3);
		
		while(!gameOver()){
			say("turn");
			screen.render();
			if(army1)	playTurn(p1);
			else		playTurn(p2);
			army1 = !army1;
			p1State.rally();
			screen.render();
		}
		screen.render();
	}

	private void getMap(String string) {
		generateRandomMap();
	}

	private void playTurn(Bot b) {
		if(army1) 	say("P1 turn!");
		else 		say("P2 turn!");
		
		o = null;
		turnStart = timeNow();
		if(b == null)	humanPlay();
		else			botPlay(b);
	}

	private void say(String string) {
		System.out.println(string);
	}

	private void botPlay(Bot b) {
		b.doTurn(this);
		while(timeNow() <= turnStart + 1000 || o == null){
			sleep(47);
		}
		
		Order ready = null;
		synchronized (this) {
			ready = o;
		}
		
		sleep(1000);
		
		if(o != null){
			executeOrder(ready);
		}
	}

	private long timeNow() {
		return System.currentTimeMillis();
	}

	private void humanPlay() {
		humanInputting = true;
		while(o == null){
			sleep(200);
		}
		executeOrder(o);
	}

	private void executeOrder(Order o) {
		//todo checks go here
		say("ORDER: " + o.from.id + " ==> " + o.to.id);
		p1State.performOrder(o.from.id, o.to.id);
	}
	
	private void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private boolean gameOver() {
		if(turn == 200){
			System.out.println("TURNS OVER");
			return true;
		}
		if(p1State.planetsOfPlayer(PLAYER).size() == 0){
			System.out.println("P2 WINS");
			return true;
		}
		if(p1State.planetsOfPlayer(ENEMY).size() == 0){
			System.out.println("P1 WINS");
			return true;
		}
		return false;
	}

	private Bot getBot(String string) {
		Bot b = null;
		@SuppressWarnings("rawtypes")
		Class theClass;
		try {
			theClass = Class.forName(string);
			b = (Bot)theClass.newInstance();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			System.out.println("BOT CALLED <" + string + "> NOT FOUND. USING HUMAN PLAYER.");
		}
		return b;
	}

	public void click(MPlanet x, boolean lClick) {
		if(humanInputting){
			if(lClick){
				hFrom = x;
			}else{
				if(hFrom != null){
					o = new Order(hFrom, x);
				}
			}
		}
	}
	
	public void release(MPlanet x) {
		
	}
	
	
	////////// OVERRIDES ////////////
	
	@Override
	public void issueOrder(Planet f, Planet t){
		issueOrder(f.getID(), t.getID());
	}
	
	@Override
	public void issueOrder(int fromID, int toID){
		synchronized (this) {
			o = new Order(p1State.get(fromID), p1State.get(toID));
		}
	}
	
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
		System.out.println(args);
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

	private MPlanet flip(MPlanet x) {
		if(!army1){
			if(x.owner == PLAYER)		x.owner = ENEMY;
			else if(x.owner == ENEMY)	x.owner = PLAYER; 
		}
		return x;
	}

	////////// SIMULATION ///////////
	
	
	
	class Model{
    	List<MPlanet> mPlanets;
    	
    	Model(List<MPlanet> mPlanets){
    		this.mPlanets = mPlanets;
    	}

		public List<MPlanet> flip(List<MPlanet> planets) {
			List<MPlanet> p = new ArrayList<>();
			for(MPlanet x : planets){
				MPlanet next = x.floop();
				switch(next.owner){
				case PLAYER: next.owner = ENEMY;	break;
				case ENEMY: next.owner = PLAYER;	break;
				}
				p.add(next);
			}
			return p;
		}

		//add appropriate reinforcements to all non-neutral planet models
    	public Model rally() {
			for(MPlanet x : mPlanets){
				if(x.owner != PlanetWars.NEUTRAL){
					x.numShips += x.growthRate;
				}
			}
			return this;
		}

		//alters the current model to reflect the state after the given order is conducted
    	Model performOrder(int a, int b){
    		MPlanet from = get(a);
    		MPlanet to = get(b);
    		
    		int aggressor = from.owner;
    		int shipsEnRoute = from.numShips/2;
    		from.numShips -= shipsEnRoute;
    		
    		if(to.owner == aggressor){ //reinforcement
    			to.numShips += shipsEnRoute;
    		}else{//attack	
    			if(to.numShips >= shipsEnRoute){//failed conquest
    				to.numShips -= shipsEnRoute;
    			}else{//successful conquest
    				to.owner = aggressor;
    				to.numShips = shipsEnRoute - to.numShips;
    			}
    		}
    		return this;
    	}
    	
    	public MPlanet get(int qId){
    		for(MPlanet x : mPlanets){
    			if(x.id == qId){
    				return x;
    			}
    		}
    		System.out.println("Model doesn't contain planet numbered " + qId + " among it's " + mPlanets.size() + " planets");
    		return null;
    	}
    	
    	//return heuristic
    	public double heuristic() {
    		double me = totalShipsOfPlayer(PlanetWars.PLAYER) + planetWorth(PlanetWars.PLAYER);
    		double them = totalShipsOfPlayer(PlanetWars.ENEMY) + planetWorth(PlanetWars.ENEMY);
    		
    		double h = 1D * me / (me + them);
    		return h;
		}
    	
    	private double planetWorth(int player) {
    		double worth = 0;
			for(MPlanet x : planetsOfPlayer(player)){
				worth += 0.000_0001 * x.growthRate;
			}
			return worth;
		}

		int largestArmyOfPlayer(int playerId){
    		int result = 0;
    		for(MPlanet x : planetsOfPlayer(playerId)){
    			int shipsThere = x.numShips / 2;
    			if(shipsThere > result){
    				result = shipsThere;
    			}
    		}
    		return result;
    	}
    	
    	//returns a list of all the model planets owned by the given player
    	List<MPlanet> planetsOfPlayer(int playerId){
    		List<MPlanet> result = new ArrayList<>();
    		for(MPlanet x : mPlanets){
    			if(x.owner == playerId){
    				result.add(x);
    			}
    		}
    		return result;
    	}
    	
    	List<MPlanet> planetsNotOfPlayer(int playerId){
    		List<MPlanet> result = new ArrayList<>();
    		for(MPlanet x : mPlanets){
    			if(x.owner != playerId){
    				result.add(x);
    			}
    		}
    		return result;
    	}
    	
		//return a list of all model planets
    	List<MPlanet> getAllPlanets(){
    		List<MPlanet> result = new ArrayList<>();
    		for(MPlanet x : mPlanets){
    			result.add(x);
    		}
    		return result;
    	}
    	
    	//return deep clone of this game state model
    	public Model floop(){
    		List<MPlanet> list = new ArrayList<>();
    		for(MPlanet mp : mPlanets){
    			list.add(mp.floop());
    		}
    		return new Model(list);
    	}
    	
    	public int totalShipsOfPlayer(int playerID){
    		int result = 0;
    		for(MPlanet x : mPlanets){
    			if(x.owner == playerID){
    				result += x.numShips;
    			}
    		}
    		return result;
    	}
    }
	

	

	
	class EPlanet extends Planet{
		EPlanet(MPlanet m){
			super(m.id, m.owner, m.numShips, m.growthRate, 5, 5);
		}
		
		EPlanet(int id, int owner, int numships, int growthrate, double x, double y){
			super(id, owner, numships, growthrate, x, y);
		}
	}
    
    //model of a planet
    class MPlanet{
    	int id, owner, growthRate, numShips, radius;
    	double x, y;
    	
    	public MPlanet(int id, int owner, int growthRate, int numShips, double x, double y){
    		this.id = id;
    		this.owner = owner;
    		this.growthRate = growthRate;
    		this.numShips = numShips;
    		this.x = x;
    		this.y = y;
    		radius = 4 + growthRate;
    	}
    	
    	//return a clone of the planet model
    	public MPlanet floop(){
    		return new MPlanet(id, owner, growthRate, numShips, x, y);
    	}
    }
    
    class Order{
    	MPlanet from, to;
    	
    	Order(MPlanet from, MPlanet to){
    		this.from = from;
    		this.to = to;
    	}
    }
}
