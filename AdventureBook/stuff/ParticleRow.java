package stuff;

public class ParticleRow {
	private Particle[] particles;
	private int numberOfParticles;
	private double graphFlux;
	
	ParticleRow(){
		particles = new Particle[4];
		numberOfParticles = 0;
		graphFlux = 1000;
	}
	
	public void addParticle(Particle p){
		if(particles.length <= numberOfParticles){
			doubleArraySize();
		}
		particles[numberOfParticles] = p;
		numberOfParticles++;
	}
	
	public void removeParticleAt(int index){
		for(int i = index; i < numberOfParticles-1; i++){
			particles[i] = particles[i+1];
		}
		numberOfParticles--;
		if(numberOfParticles <= particles.length/2 && particles.length > 4){
			halveArraySize();
		}
	}
	
	private void doubleArraySize(){
		Particle[] particles2 = new Particle[particles.length*2];
		for(int i = 0; i < numberOfParticles; i++){
			particles2[i] = particles[i];
		}
		particles = particles2;
	}
	
	private void halveArraySize(){
		Particle[] particles2 = new Particle[particles.length/2];
		for(int i = 0; i < numberOfParticles; i++){
			particles2[i] = particles[i];
		}
		particles = particles2;
	}
	
	public Particle getParticleAt(int index){
		return particles[index];
	}
	
	public int getnumberOfParticles(){
		return numberOfParticles;
	}
	
	public double getGraphFlux(){
		return graphFlux;
	}
	
	public void wavyGridLayout(){
		int gridWidth = (int)(Math.sqrt(numberOfParticles) + .5),
			gridWidth2 = numberOfParticles / gridWidth;
		if(numberOfParticles > gridWidth*gridWidth2)
			gridWidth++;
		int nextParticle = 0;
		
		for(int i = 0; i < gridWidth; i++){
			for(int j = 0; j < gridWidth2; j++){
				if(nextParticle < numberOfParticles){
					particles[nextParticle].setX(.8/(gridWidth+1)*(i+1)+.1 +  Math.cos(1.0*3.1*j/gridWidth2)*j/gridWidth2/gridWidth2*.8);
					particles[nextParticle].setY(.8/(gridWidth2+1)*(j+1)+.1 +  Math.sin(1.0*7.1*i/gridWidth)*i/gridWidth/gridWidth*.8);
					nextParticle++;
				}
			}
		}
	}

	public void swapMinimizePositons(){
		for(int i = 0; i < numberOfParticles; i++){
			Particle a = particles[i];
			if(a.isWithinBounds()){
				for(int j = i+1; j < numberOfParticles; j++){
					Particle b = particles[j];
					if(b.isWithinBounds()){
						double initial = connectedWeight();
						swapParticlePositions(i, j);
						double resulting = connectedWeight();
						if(resulting > initial)
							swapParticlePositions(i, j);
					}
				}
			}
		}
	}
	
	private double connectedWeight(){
		double connectedWeight = 0;
		for(int i = 0; i < numberOfParticles; i++){
			Particle a = particles[i];
			if(a.isWithinBounds()){
				for(int j = i+1; j < numberOfParticles; j++){
					Particle b = particles[j];
					if(a.associatedWith(b) && b.isWithinBounds()){
						connectedWeight += a.distanceTo(b);
					}
				}
			}
			
		}
		return connectedWeight;
	}
	
	public void radialEmbed(int stepsRemaining, int centerIndex){
		//move all particles out
		for(int i = 0; i < numberOfParticles; i++){
			particles[i].setX(-1);
			particles[i].setY(-1);
		}
		recursiveRadialEmbed(stepsRemaining, centerIndex, .5, .5, .3);
	}
	
	private void recursiveRadialEmbed(int stepsRemaining, int centerIndex, double x, double y, double radius){
		Particle newCenter = particles[centerIndex];
		newCenter.setX(x);
		newCenter.setY(y);
		newCenter.setClosenessToScreen(Math.pow(stepsRemaining, 1.8)/8+.7);
		
		if(stepsRemaining < 1)
			return;
		int numberOfNodesPossible = newCenter.getPage().getOptionsIn().getNumberOfOptions()
				+ newCenter.getPage().getOptionsOut().getNumberOfOptions();
		int[] nodeIndices = new int[numberOfNodesPossible];
		int numberOfNodes = 0;
		for(int i = 0; i < numberOfParticles; i++){
			Particle other = particles[i];
			if(newCenter.associatedWith(other) && !other.isWithinBounds()){
				nodeIndices[numberOfNodes] = i;
				numberOfNodes++;
			}
		}
		double nextX, nextY, direction;
		for(int i = 0; i < numberOfNodes; i++){
			Particle pa = particles[nodeIndices[i]];
			direction = Math.PI*2/numberOfNodes*i + centerIndex;
			nextX = x+Math.cos(direction)*radius;
			nextY = y+Math.sin(direction)*radius;
			if(newCenter.distanceTo(nextX, nextY) < newCenter.distanceTo(pa) || !pa.isWithinBounds())
				recursiveRadialEmbed(stepsRemaining-1, nodeIndices[i], nextX, nextY, radius*.6);
		}
	}
	
	

	private void swapParticlePositions(int indexA, int indexB){
		double aX = particles[indexA].getX(),
			aY = particles[indexA].getY();
		particles[indexA].setX(particles[indexB].getX());
		particles[indexA].setY(particles[indexB].getY());
		particles[indexB].setX(aX);
		particles[indexB].setY(aY);
	}
	
	public void springEmbed() {
		double eccentricity = calcEccentricity();
		if(eccentricity > .48)
			eccentricity = .48;
		double expanse = 1/(.55-eccentricity+.00001);
		for(int i = 0; i < numberOfParticles; i++){
			for(int j = i+1; j < numberOfParticles; j++){
				particles[i].react(particles[j], expanse, numberOfParticles);
			}
		}
		double sumMomentsOfForce = 0;
		for(int i = 0; i < numberOfParticles; i++){
			particles[i].bound();
			particles[i].squishInward(expanse);
			sumMomentsOfForce += particles[i].getMomentOfForce()*100000;
			particles[i].capSpeed();
			particles[i].takeNextStep();
		}
		if(numberOfParticles > 0)
			graphFlux = sumMomentsOfForce / numberOfParticles;
		else 
			graphFlux = 0;
	}
	
	public double calcEccentricity(){
		double max = 0;
		for(int i = 0; i < numberOfParticles; i++){
			double thisDistance = particles[i].distanceToWillBe(.5, .5);
			if(thisDistance > max)
				max = thisDistance;
		}
		return max;
	}

	public void removeParticleWithPage(Page p) {
		for(int i = 0; i < numberOfParticles; i++)
			if(particles[i].getPage() == p){
				removeParticleAt(i);
				i--;
			}
	}

	public Particle getParticleWithPage(Page p) {
		for(int i = 0; i < numberOfParticles; i++)
			if(particles[i].getPage() == p){
				return particles[i];
		}
		return null;
	}
}
