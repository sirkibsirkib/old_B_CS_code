import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import planetWarsAPI.Planet;
import planetWarsAPI.PlanetWars;

public class Experimenter extends PlanetWars{
	private static final String LINE = "-----------------";
	Model p1State;
	int turn;
	boolean army1;
	MPlanet hFrom;
	Order o;
	boolean botDone = false;
	BotData p1, p2;
	
	public static void main(String[] args){
		new Experimenter().start();
	}

	private void generateRandomMap() {
		List<MPlanet> planets = new ArrayList<>();
		
		planets.add(new MPlanet(0, 1, 3, 100,
				rdg(0.8) + 0.1, rdg(0.8) + 0.1));
		
		planets.add(new MPlanet(1, 2, 3, 100,
				rdg(0.8) + 0.1, rdg(0.8) + 0.1));
		
		int numPlanets = rng(7) + 3;
		for(int i = 2; i < numPlanets; i++){
			planets.add(new MPlanet(i, 0, rng(10), rng(30) + 1,
					rdg(0.8) + 0.1, rdg(0.8) + 0.1));
		}

		p1State = new Model(planets);
	}

	private int rng(int i) {
		return (int) rdg(i);
	}

	private double rdg(double i) {
		return Math.random() * i;
	}
	
	private void writeLog(String... strings){
		for(String s : strings){
			System.out.println(s);
			String path = "C:/Users/Christopher/Desktop/experimentLog.txt";
			try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(path, true)))) {
			    out.println(s);
			}catch (IOException e) {
			    System.out.println("WRITE ERROR");
			}
		}
		
	}
	
	private void writef(String string, Object... o){
		System.out.printf(string, o);
		String path = "C:/Users/Christopher/Desktop/experimentLog.txt";
		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(path, true)))) {
		    out.printf(string, o);
		}catch (IOException e) {
		    System.out.println("WRITE ERROR");
		}
	}

	private void start() {
		
		/*
		 * INPUTS HERE:
		 */
		
		int games = 200;
		int p1Depth = 3;
		int p2Depth = 3;
		p1 = new BotData(new David().setDepth(p1Depth));
		p2 = new BotData(new CleanBot().setDepth(p2Depth));

		writeLog(LINE, "NEW EXPERIMENT : " + games + " sims.");
		writeLog(p1.b.getClass().getName() + "(" + p1Depth + ") vs. " + 
				p2.b.getClass().getName() + "(" + p2Depth + ")", LINE);
		
		for(int i = 0; i < games; i++){
			army1 =  i%2 == 0;
			//army1 = false;
			test();
			if( i%25 == 0 && i > 0){
				writef(">> %.3f Done\n", (1D * i / games));
				results(i);
			}	
		}	
		
		results(games);
		writeLog(LINE);
		writeLog("DONE");
	}

	private void results(int games) {
		writef("\nP1:\tatt[%5d]\tto[%5d]\n", p1.averageTimeTaken(), p1.timeouts);
		writef("P2:\tatt[%5d]\tto[%5d]\n", p2.averageTimeTaken(), p2.timeouts);
		int draws = (games - p1.wins - p2.wins);
		double winProp = (1.0 * p1.wins / (games-draws));
		writef("WINS: 1[" + p1.wins + "] 2[" + p2.wins + "] d[" + draws + "]\n");
		writef("Win Ratio: %.3f\n", winProp);
	}

	private void test() {
		generateRandomMap();
		turn = 0;
		
		while(!gameOver()){
			if(army1)	playTurn(p1);
			else		playTurn(p2);
			p1State.rally();
			
			army1 = !army1;
		}
	}

	private void playTurn(BotData bd) {
		synchronized (this) {
			botDone = false;
		}
		o = null;
		long turnStart = timeNow();
		bd.b.doTurn(this);
		long now;
		do{
			sleep(11);
			now = timeNow();
		}while(timeNow() <= turnStart + 1000 && !isBotDone());

		long timeTaken = now - turnStart;
		
		if(timeTaken < 1000)	bd.timeTaken += timeTaken;
		else					bd.timeouts ++;
		bd.turnsTaken++;
		
		if(o != null){
			executeOrder(o);
		}
		turn++;
	}

	private boolean isBotDone() {
		synchronized (this) {
			return botDone;
		}
	}

	private long timeNow() {
		return System.currentTimeMillis();
	}

	private void executeOrder(Order o) {
		//todo checks go here
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
			writef("0");
			return true;
		}
		if(p1State.planetsOfPlayer(PLAYER).size() == 0){
			writef("2");
			p2.wins++;
			return true;
		}
		if(p1State.planetsOfPlayer(ENEMY).size() == 0){
			writef("1");
			p1.wins++;
			return true;
		}
		return false;
	}
	
	
	////////// OVERRIDES ////////////
	
	@Override
	public void issueOrder(Planet f, Planet t){
		issueOrder(f.getID(), t.getID());
	}
	
	@Override
	public void issueOrder(int fromID, int toID){
		synchronized (this) {
			botDone = true;
		}
		o = new Order(p1State.get(fromID), p1State.get(toID));
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
    		writeLog("Model doesn't contain planet numbered " + qId + " among it's " + mPlanets.size() + " planets");
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
