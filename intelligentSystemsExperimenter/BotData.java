import planetWarsAPI.Bot;

public class BotData {
	Bot b;
	int timeTaken = 0;
	int turnsTaken = 0;
	int timeouts = 0;
	int wins = 0;
	
	BotData(Bot bot){
		this.b = bot;
	}
	
	public int averageTimeTaken(){
		if(timeouts == turnsTaken) return 999999;
		return timeTaken / (turnsTaken-timeouts);
	}
}
