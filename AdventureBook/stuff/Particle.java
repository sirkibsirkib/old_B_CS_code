package stuff;

public class Particle {
	private static final double PERSONAL_SPACE = 0.05;
	private double x, y, x2, y2, closenessToScreen;
	private Page p;
	private static final double MAX_SPEED = .01;
	
	public double getClosenessToScreen() {
		return closenessToScreen;
	}

	public void setClosenessToScreen(double closenessToScreen) {
		this.closenessToScreen = closenessToScreen;
	}

	Particle(double x, double y, Page p){
		this.x = x;
		this.y = y;
		x2 = x;
		y2 = y;
		this.p = p;
	}
	
	public Page getPage(){
		return p;
	}
	
	public void react(Particle pa, double multiplier, int numberOfParticles){
		if(associatedWith(pa))
			attract(pa, numberOfParticles);
		else
			repel(pa, 1 / (numberOfParticles+.1));
	}
	public boolean associatedWith(Particle pa){
		return p.hasOptionTo(pa.getPage()) || pa.getPage().hasOptionTo(p);
	}

	public void takeNextStep(){
		x = x2;
		y = y2;
	}
	
	public double getMomentOfForce(){
		return distanceToWillBe(x, y);
	}
	
	public int getDiameter(){
		return (int)(Math.sqrt(p.getText().length())/5)+9;
	}
	
	public double distanceToWillBe(double otherX, double otherY){
		double xDif = x2 - otherX,
			yDif = y2 - otherY;
		return Math.sqrt((xDif*xDif) + (yDif*yDif));
	}

	public double distanceTo(Particle pa){
		double xDif = x - pa.getX(),
			yDif = y - pa.getY();
		return Math.sqrt((xDif*xDif) + (yDif*yDif));
	}
	
	public double distanceTo(double otherX, double otherY){
		double xDif = x - otherX,
				yDif = y - otherY;
			return Math.sqrt((xDif*xDif) + (yDif*yDif));
	}
	
	public void attract(Particle pa, double multiplier) {
		double distance = distanceToWillBe(pa.getX(), pa.getY()) - PERSONAL_SPACE,
			speed = .3*distance*distance*multiplier,
			angle = angleToward(pa.getX(), pa.getY());
		if(speed > MAX_SPEED)
			speed = MAX_SPEED;
		if(distance < 0)
			speed = -speed;
		alterNextStep(speed, angle);
		pa.alterNextStep(speed, angle + Math.PI);
		//multiplier
	}
	
	public void repel(Particle pa, double multiplier) {
		double distance = distanceToWillBe(pa.getX(), pa.getY()),
			speed = .0005/((distance+.1)) * multiplier,
			angle = angleToward(pa.getX(), pa.getY()) + Math.PI;
		alterNextStep(speed, angle);
		pa.alterNextStep(speed, angle + Math.PI);
	}
	
	public void squishInward(double forceMultiplier){
		double distance = distanceToWillBe(.5, .5),
			speed = .0006*distance*distance*distance*forceMultiplier,
			angle = angleToward(.5, .5);
		alterNextStep(speed, angle);
	}
	
	public boolean willBeWithinBounds(){
		if(x2 > 0 && x2 < 1 && y2 > 0 && y2 < 1)
			return true;
		return false;
	}
	
	public boolean isWithinBounds(){
		if(x > 0 && x < 1 && y > 0 && y < 1)
			return true;
		return false;
	}
	
	public void bound(){
		if(x2 < 0)
			x2 = 0;
		if(x2 > 1)
			x2 = 1;
		if(y2 < 0)
			y2 = 0;
		if(y2 > 1)
			y2 = 1;
	}

	private void alterNextStep(double speed, double angle) {
		x2 += (speed*Math.cos(angle));
		y2 += (speed*Math.sin(angle));
	}
	
	public void capSpeed(){
		if(getMomentOfForce() <= MAX_SPEED)
			return;
		double momentDirection = angleToward(x, y) + Math.PI;
		x2 = x;
		y2 = y;
		alterNextStep(MAX_SPEED, momentDirection);
	}
	
	public double angleToward(double otherX, double otherY){
		double xDif = otherX - x2,
				yDif = otherY - y2;
		if(xDif == 0){
			if(yDif > 0)
				return Math.PI/2;
			if(yDif < 0)
				return Math.PI*3/2;
			return 0;
		}
		if(xDif < 0)
			return Math.atan(yDif/xDif) + Math.PI;
		return Math.atan(yDif/xDif);
	}
	
	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}

	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
}
