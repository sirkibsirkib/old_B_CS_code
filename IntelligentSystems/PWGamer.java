

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import planetWarsAPI.Bot;
import planetWarsAPI.Planet;

public class PWGamer extends planetWarsAPI.PlanetWars{
	private static final String PATH = "C:/Users/Christopher/Desktop/PWGamerLog.txt";
	
	private static final int TIME_LIMIT = 1000 + 4,
			SIM_CHECK_PERIOD = 7,
			SIMS_PER_LINE = 25,
			ROUND_LIMIT = 11,
			MIN_PLANETS = 3,
			MAX_PLANETS = 11;
	

	//0 no debug.
	//1 output text.
	//2 show game playback
	private static int DEBUG =  0;
	
	private int[] DEBUG_VICTORIES = {};
	private List<String> dStrings;

	private byte aHindex;
	private byte bHindex;
	private int wins;
	
	private T ta, tb;
	
	
	private static final String LINE = "------------------------------",
			BAR = "///////////";
	private int turn;
	private PState state;
	private PMove o;
	private boolean botThinking, aTurn, botsInverted;
	private Screen screen;
	private Random rand;
	
	public PWGamer(){
		dStrings = new ArrayList<>();
		rand = new Random();
		
		eSet();
	}
	
	private void eSet(){
		
		Date date = new Date();
		logPrintf("\n%s NEW EXPERIMENT SET %s\n", BAR, BAR);
		logPrintf("%s %s %s\n", BAR, date.toString(), BAR);
		
		
		wins = 1;
		for(int a = 0; a < 3; a++){
			for(int ac = 0; ac < 3; ac++){
				for(int b = 0; b < 3; b++){
					for(int bc = 0; bc < 3; bc++){
						if(b*3 + bc < a*3 + ac)
							continue;
						if(a == 0 && b < 2)
							continue;
						setBots(a, ac, b, bc);
						conduct();
					}
				}
			}
		}
		logPrintf("%s END EXPERIMENT SET %s\n", BAR, BAR);
		logPrintf("%s %s %s\n", BAR, date.toString(), BAR);
	}
	
	private void setBots(int aClass, int aH, int bClass, int bH){
		Bot a = null;
		Bot b = null;
		aHindex = (byte) aH;
		switch(aClass){
		case 0: a = new HCBot().heu(aHindex); break;
		case 1: a = new MMBot().heu(aHindex); break;
		case 2: a = new ABBot().heu(aHindex); break;
		}
		
		bHindex = (byte) bH;
		switch(bClass){
		case 0: b = new HCBot().heu(bHindex); break;
		case 1: b = new MMBot().heu(bHindex); break;
		case 2: b = new ABBot().heu(bHindex); break;
		}
		
		b = new ABBot();
		
		ta = new T(a, this);
		new Thread(ta).start();
		tb = new T(b, this);
		new Thread(tb).start();
	}

	private void conduct() {
		setupExperiment();
		startExperiment();
	}

	private void setupExperiment(){
		botsInverted = false;
		if(DEBUG == 2) screen = new Screen(this);
	}
	
	private void startExperiment() {
		logPrintf("\n%s\nNEW EXPERIMENT : %d wins\n%s(h%d) vs. %s(h%d)\n%s\n", LINE,
				wins, ta.b.getClass().getName(), aHindex, tb.b.getClass().getName(), bHindex, LINE );
		
		int sims = 0;
		while(ta.stat.wins + tb.stat.wins < wins){
			setupGame();
			sims++;
			game();
			if(sims % SIMS_PER_LINE == 0 && sims > 0){
				logPrintf("   %d%%   r: %.4f   %s sims\n", (int) (100 * (1.0 * ta.stat.wins + tb.stat.wins) / wins),  winRatio(), sims);
			}
		}
		if(botsInverted) swapBots();
		
		logPrint("\n\n");
		logPrintf("\n\nEXPERIMENT RESULTS : %d wins\n%s(h%d) vs. %s(h%d)\n\n",
				wins, ta.b.getClass().getName(), aHindex, tb.b.getClass().getName(), bHindex);
		logPrint("\nTOTAL SIMS: " + sims + "\n");
		logPrint("DRAWS: " + (sims - wins) + "\n");
		printResults();
	}
	
	private void printResults() {
		logPrintf("\nRATIO: %.4f\n", winRatio());
		logPrintf("\n [Bot 1]:\nTimeouts: %d\nAvg. time: %d\nT/o ratio: %.4f\n", ta.stat.timeouts, ta.stat.avgTurnTime(), ta.stat.timeoutRatio());
		logPrintf("\n [Bot 2]:\nTimeouts: %d\nAvg. time: %d\nT/o ratio: %.4f\n", tb.stat.timeouts, tb.stat.avgTurnTime(), tb.stat.timeoutRatio());
	}

	private double winRatio() {
		if(ta.stat.wins + tb.stat.wins == 0){
			return Double.NaN;
		}
		if(!botsInverted)
			return 1D * ta.stat.wins / (tb.stat.wins + ta.stat.wins);
		else
			return 1D * tb.stat.wins / (ta.stat.wins + tb.stat.wins);
	}

	private void game(){
		while(!gameOver()){
			round();
			turn++;
		}
		if(DEBUG == 2) screen.addMoment(state, null, null);
		
		int winner = 0;
		switch(gameSituation()){
		case '1':case '>':	winner = 1;	ta.stat.wins++;	break;
		case '2':case '<':	winner = 2;	tb.stat.wins++;	break;
		case '=':case 'x':	winner = 0;					break;
		}
		
		debugPrint(">> WINNER: ");
		int absoluteWinner = winner;
		if(botsInverted){
			switch(winner){
			case PLAYER: absoluteWinner = ENEMY; break;
			case ENEMY: absoluteWinner = PLAYER; break;
			}
		}
			
		logPrint(absoluteWinner + "");
		debugPrintln("\n" + LINE);
		
		if(DEBUG == 1){
			if(in(DEBUG_VICTORIES, winner)){
				printDebugStrings();
			}
			dStrings.clear();
		}else if(DEBUG == 2 && in(DEBUG_VICTORIES, winner)){
			playback();
		}
	}

	private void playback() {
		System.out.println("<== playback trigger");
		sleep(1);
		screen.playback();
		sleep(2000);
		System.out.println("\nPLAYBACK RESUMING");
		sleep(2);
		System.out.println(LINE);
	}

	private void printDebugStrings() {
		System.out.println("<== debug trigger\n\n<<< DEBUG DUMPING>>>");
		for(String s : dStrings){
			System.out.print(s);
		}
		System.out.println("<<< DEBUG DUMP COMPLETE>>>");
		System.out.println("Insert new line to continue:");
		Scanner waitScanner = new Scanner(System.in);
		waitScanner.nextLine();
		System.out.println("<<< CONTINUING in 2 seconds... >>>");
		sleep(2_000);
		System.out.println(LINE);
	}

	private boolean in(int[] arr, int x) {
		for(int y : arr){
			if(y == x)	return true;
		}
		return false;
	}

	private void debugPrint(String s) {
		if(DEBUG == 1) dStrings.add(s);
	}

	private void logPrint(String s) {
		System.out.print(s);
		String path = PATH;
		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(path, true)))) {
		    out.print(s);
		}catch (IOException e) {
		    System.out.println("WRITE ERROR");
		}
	}
	
	private void debugPrintf(String s, Object... o) {
		if(DEBUG == 1) dStrings.add(String.format(s, o));
	}

	private void logPrintf(String s, Object... o) {
		System.out.printf(s, o);
		String path = PATH;
		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(path, true)))) {
		    out.printf(s, o);
		}catch (IOException e) {
		    System.out.println("WRITE ERROR");
		}
	}
	
	private void debugPrintln(String s) {
		debugPrint(s + "\n");
	}

	private boolean gameOver() {
		if(turn >= ROUND_LIMIT-1)	return true;
		char c = gameSituation();
		return c == 'x' || c == '1' || c == '2';
	}
	
	private char gameSituation(){ //'1' p1 wins	'2' p2 wins	'x' both dead	'='	perfect
									//match '<' p2 winning	'>' p1 winning
		int sa = state.totalShipsOfPlayer(PLAYER);
		int sb = state.totalShipsOfPlayer(ENEMY);
		if(sa + sb == 0)	return 'x';
		if(sa == 0)			return '2';
		if(sb == 0)			return '1';
		if(sa > sb)			return '>';
		if(sa < sb)			return '<';
		/* default */		return '=';
	}
	
	private String mapString(){
		String s = "";
		for(PSPlanet x : state.getAllPlanets()){
			s += x.id() + " - " + x.owner() + " " + x.numShips()
					+ " " + x.growthRate() + "+\n";
		}
		return s;
	}

	private void round() {
		//player 1 turn
		aTurn = true;
		turn(ta);
		if(o != null && o.from != null && o.to != null){
			debugPrintln(" <1> " + o.from.id() + " ==> " + o.to.id());
			state.performPMove(o);
		}
		PMove p1o = o;
		
		//player 2 turn
		aTurn = false;
		turn(tb);
		if(o != null && o.from != null && o.to != null){
			debugPrintln(" <2> " + o.from.id() + " ==> " + o.to.id());
			state.performPMove(o);
		}
		

		if(DEBUG == 2) screen.addMoment(state.clone(), p1o, o);
		
		//state tick, resolve, rally
		state.tick();		
		debugPrintln("\n" + mapString());
	}

	private PMove turn(T t) {
		setBotThinking(true);
		o = null;
		t.go = true;
		
		long now;
		long roundStart = now();
		
		do{
			sleep(SIM_CHECK_PERIOD);
			now = now();
		}while(botThinking() && roundStart + TIME_LIMIT > now);

		setBotThinking(false);
		int timeTaken = (int) (now - roundStart);
		if(timeTaken < TIME_LIMIT){
			t.stat.cumulativeTurnTime += timeTaken;
			t.stat.turnsCompleted++;
		}else{
			System.out.print('t');
			t.stat.timeouts++;
		}
		return o;
	}
	
	private void setBotThinking(boolean t){
		botThinking = t;
	}

	private boolean botThinking() {
		return botThinking;
	}

	private void setupGame(){
		if(DEBUG == 2) screen.clearMoments();
		swapBots();
		randomizeMap();
		turn = 0;
	}
	
	private void swapBots() {
		//invert bot a and bot b each turn JUST IN CASE the compiler is doing some optimization that gives a player an unfair advantage
		T c = ta;
		ta = tb;
		tb = c;
		botsInverted = !botsInverted;
	}

	private void randomizeMap() {
		List<PSPlanet> psps = new ArrayList<>();
		
		psps.add(new PSPlanet(new MockPlanet(0, 1, 100, 3)));
		psps.add(new PSPlanet(new MockPlanet(1, 2, 100, 3)));
		int numPlanets = rng(MAX_PLANETS - MIN_PLANETS) + MIN_PLANETS;
		for(int i = 2; i < numPlanets; i++){
			psps.add(new PSPlanet(new MockPlanet(i, 0, rng(40), rng(20))));
		}
		state = new PState(psps);
		debugPrintln(mapString());
	}
	
	private int rng(int x) {
		return (int) (rand.nextDouble() * x);
	}
	
	///////// BOT RESPONSE ////////
	
	public synchronized void issueOrder(Planet f, Planet t){
		if(!botThinking())
			return;
		issueOrder(f.getID(), t.getID());
	}
	public synchronized void issueOrder(int fromID, int toID){
		if(aTurn)	debugPrint(" (o1) "); 
		else		debugPrint(" (o2) ");
		
		if(!botThinking())
			return;
		if(aTurn && state.getPSPlanet(fromID).owner() != PLAYER)
			return;
		if(!aTurn && state.getPSPlanet(fromID).owner() != ENEMY)
			return;
		
		o = new PMove(state.getPSPlanet(fromID), state.getPSPlanet(toID));
		setBotThinking(false);
	}
	
	//////// INNER CLASSES /////////
	
	class T implements Runnable{
		Bot b;
		PWGamer master;
		boolean go;
		Statistics stat;
		
		T(Bot b, PWGamer master){
			this.b = b;	
			this.master = master;
			go = false;
			stat = new Statistics();
		}

		public void run() {
			while(true){
				sleep(4);
				if(go){
					go = false;
					b.doTurn(master);
				}
			}
		}
		
		void sleep(int x){
			try {
				Thread.sleep(x);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public class MockPlanet extends Planet{
		public MockPlanet(PSPlanet x){
			super(x.id(), x.owner(), x.numShips(), x.growthRate(), 0, 0);
		}

		public MockPlanet(int id, int owner, int numShips, int growthRate) {
			super(id, owner, numShips, growthRate, 0, 0);
		}
	}

	private class Statistics{
		int wins, timeouts, cumulativeTurnTime, turnsCompleted;
		
		Statistics(){
			wins = 0;
			timeouts = 0;
			cumulativeTurnTime = 0;
			turnsCompleted = 0;
		}
		
		int avgTurnTime(){
			if(turnsCompleted == 0)
				return -1;
			return cumulativeTurnTime / turnsCompleted;
		}
		
		double timeoutRatio(){
			if(turnsCompleted + timeouts == 0)
				return -1;
			return 1D * timeouts / (timeouts + turnsCompleted);
		}
	}
	
	////////////// OVERRIDES /////////////
	
	
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
		List<PSPlanet> p = state.getAllPlanets();
		int count = 0;
		for(PSPlanet x : p){
			if(x.owner() == PLAYER){
				count += x.numShips();
			}
		}
		return count;
	}
	
	@Override
	public Planet getPlanet(int planetID){
		PSPlanet m = state.getPSPlanet(planetID).clone();
		adjust(m);
		adjust(m);
		return new EPlanet(m);
	}
	
	@Override
	public void log(Object... args){
		//nothing
	}
	
	private List<Planet> buildPlanetList(int... owners){
		if(!aTurn){
			for(int i = 0; i < owners.length; i++){
				if(owners[i] == PLAYER) owners[i] = ENEMY;
				else if(owners[i] == ENEMY) owners[i] = PLAYER;
			}
		}
		List<Planet> result = new ArrayList<>();
		for(PSPlanet y : state.getAllPlanets()){
			PSPlanet x = y.clone();
			
			if(x.owner() == owners[0] ||
					(owners.length >= 2 && x.owner() == owners[1]) ||
					(owners.length >= 3 && x.owner() == owners[2])){
				x = adjust(x);
				result.add(new EPlanet(x));				
			}
		}
		Collections.shuffle(result);
		return result;
	}
	
	private PSPlanet adjust(PSPlanet x) {
		int own;
		if(aTurn){
			own = (x.owner() == PLAYER) ? PLAYER : 
				(x.owner() == ENEMY) ? ENEMY :
					NEUTRAL;
		}else{
			own = (x.owner() == PLAYER) ? ENEMY : 
				(x.owner() == ENEMY) ? PLAYER :
					NEUTRAL;
		}
		return new PSPlanet(x.id(), own, x.growthRate(), x.numShips());
	}

	class EPlanet extends Planet{
		EPlanet(PSPlanet m){
			super(m.id(), m.owner(), m.numShips(), m.growthRate(), 0, 0);
		}

		EPlanet(int id, int owner, int numships, int growthrate, double x, double y){
			super(id, owner, numships, growthrate, x, y);
		}
	}

	private void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private long now() {
		return System.currentTimeMillis();
	}
	
	public static void main(String[] args){
		new PWGamer();
	}
}
