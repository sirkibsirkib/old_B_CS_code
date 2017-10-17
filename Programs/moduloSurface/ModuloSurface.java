package moduloSurface;

public class ModuloSurface {
	Surface surface;
	Feature man;
	static final int VIEW_RADIUS = 7;
	
	ModuloSurface(){
		surface = new Surface(10);
		man = surface.addFeatureAt(0, 0, "man");
	}

	void start(){
		surface.newFeatureSomewhereMult("rock", 20);
		play();
		drawSurface();
	}
	
	void play(){
		
	}
	
	//TODO
	void drawSurface(){
		for(int i = man.x-VIEW_RADIUS; i < man.x+VIEW_RADIUS; i++){
			for(int j = man.y-VIEW_RADIUS; j < man.y+VIEW_RADIUS; j++){
				System.out.print(imageOfFeatureAtMod(surface, i, j));
			}
			System.out.println();
		}
	}
	
	//TODO
	String imageOfFeatureAtMod(Surface su, int x, int y){
		//empty cell
		if(su.featureAtMod(x, y) == null){
			return " - ";
		}
		switch(su.featureAtMod(x, y).name){
			case "man": 	return "man";
			case "rock":	return "{|}";
		}
		//unrecognized name
		return " ? ";
	}
	
	public static void main(String[] args) {
		new ModuloSurface().start();
	}

}
