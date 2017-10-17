import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Screen extends Canvas implements MouseListener, Runnable{
	private static final long serialVersionUID = 481358422745315268L;

	public static final int WIDTH = 400, HEIGHT = 300, SCALE = 3;
	PWGamer master;
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	JFrame jf;
	private int[] screenPixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	private List<Moment> moments;
	private boolean screening = false;
	
	
	Screen(PWGamer master){
		this.master = master;
		moments = new ArrayList<>();
	}
	
	void playback(){
		start();
		int numMoments = moments.size();
		while(screening){
			for(int i = 0; i < numMoments; i++){
				if(screening)
					render(moments.get(i), i+1, numMoments);
			}
			if(screening)
				sleep(1000);
		}
	}

	private void start() {
		screening = true;
		newJFrame();
		
		addMouseListener(this);
        setBackground(Color.BLACK);
		if(moments.size() > 0)
			render(moments.get(0), 0, 0);
	}

	private void clear() {
		for(int i = 0; i < WIDTH * HEIGHT; i++){
			screenPixels[i] = 0;
		}
	}

	private void newJFrame() {
		jf = new JFrame("Simmer");
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		jf.addMouseListener(this);
		//jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		//jf.setD
		
		jf.addWindowListener(new WindowAdapter(){
		    public void windowClosing(WindowEvent e){
		    	screening = false;
		    	jf = null;
		    }
		});
		
		setMinimumSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		setMaximumSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		panel.setLayout(new FlowLayout());
		jf.add(this, BorderLayout.CENTER);
		jf.pack();
		jf.setResizable(false);
		jf.setLocationRelativeTo(null);
		jf.setVisible(true);
	}
	
	public String getString(String message, String bar, String suggestion){
		String s = "";
		s = (String)JOptionPane.showInputDialog(
				jf, message, bar, JOptionPane.PLAIN_MESSAGE, null, null, suggestion);
		return s;
	}
	
	private void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	void render(Moment m, int step, int of){
		BufferStrategy bs = getBufferStrategy();
		if(bs == null){
			createBufferStrategy(3);
			return;
		}
		List<PSPlanet> planets = m.s.getAllPlanets();
		
		clear();
		drawPlanets(planets);
		if(of != 0)
			drawStepNumber(step, of);
		show(bs);
		sleep(100);
		show(bs);
		sleep(100);
		
		if(m.p1o != null || m.p2o != null){
			for(int c = 1; c < 16; c++){
				clear();
				drawPlanets(planets);
				if(m.p1o != null)
					drawShips(m.p1o, 0.06 * c, 0x00dd00);
				if(m.p2o != null)
					drawShips(m.p2o, 0.06 * c, 0xff0000);
				if(of != 0)
					drawStepNumber(step, of);
				show(bs);
				sleep(100);
			}
		}
	}

	private void drawStepNumber(int step, int of) {
		drawNumber(step, 0.95, 0.06);
		drawNumber(of, 0.95, 0.09);
	}

	private void show(BufferStrategy bs) {
		Graphics g = bs.getDrawGraphics();

		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		bs.show();
	}

	private void drawPlanets(List<PSPlanet> planets) {
		for(PSPlanet next : planets){
			double x = x(next);
			double y = y(next);
			drawPlanet(x, y, next.growthRate()+5, next.owner());
			drawPlanetText(x, y, next.numShips(), next.growthRate());
		}
	}
	
	private void drawShips(PMove p1o, double c, int col) {
		double x1 = x(p1o.from);
		double y1 = y(p1o.from);
		double x2 = x(p1o.to);
		double y2 = y(p1o.to);
		
		double x = x1*(1-c) + x2*c;
		double y = y1*(1-c) + y2*c;
		
		int sX = (int) (x*WIDTH);
		int sY = (int) (y*HEIGHT);
		ship(sX, sY, col);
	}

	private void ship(int xS, int yS, int col) {
		for(int i = -1; i < 2; i++){
			for(int j = -1; j < 2; j++){
				int index = (xS+i) + (yS+j) * WIDTH;
				screenPixels[index] = col;
			}
		}
	}

	private double x(PSPlanet p){
		return (p.id()%4 + 0.5)*.25;
	}
	
	private double y(PSPlanet p){
		double d = (p.id()/4 + 0.5)*.25;
		return d + sqr(0.15 * (p.id()%4));
	}

	private void drawPlanetText(double x, double y, int numShips, int growthRate) {
		drawNumber(numShips, x, y);
		drawNumber(growthRate, x, y + 0.03);
	}

	private void drawPlanet(double x, double y, int radius, int owner) {
		int sX = (int) (x*WIDTH);
		int sY = (int) (y*HEIGHT);
		
		int col;
		switch(owner){
		case Simmer.PLAYER: col = 0x00_99_11;	break;
		case Simmer.ENEMY:	col = 0xdd_00_00;	break;
		default:			col = 0x77_77_77;	break;
		}
		
		for(int i = max(0, sX-radius); i < min(WIDTH, sX + radius); i++){
			for(int j = max(0, sY-radius); j < min(HEIGHT, sY + radius); j++){
				if(dist(i, j, sX, sY) < radius){
					screenPixels[i + WIDTH*j] = col;
				}
			}
		}
	}
	
	private double dist(int x1, int y1, int x2, int y2) {
		return Math.sqrt(sqr(x2-x1) + sqr(y2-y1));
	}

	private double sqr(double d) {
		return d*d;
	}

	private int min(int a, int b){
		if(a < b) return a;
		return b;
	}
	
	private int max(int a, int b){
		if(a > b) return a;
		return b;
	}

	public void mouseClicked(MouseEvent arg0) {}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}

	public void mousePressed(MouseEvent arg0) {
//		Point p = getMouse();
//		for(PSPlanet x : master.state.getAllPlanets()){
//			if(within(x, p)){
//				master.click(x, true);
//			}
//		}
	}
	
	private boolean within(PSPlanet m, Point p){
		int sX = (int) (x(m) * WIDTH);
		int sY = (int) (y(m) * HEIGHT);
		return dist(sX, sY, p.x, p.y) <= m.growthRate();
	}

	private Point getMouse() {
		Point m = MouseInfo.getPointerInfo().getLocation();
		m.x = (m.x - jf.getX()) / SCALE;
		m.y = (m.y - jf.getY()) / SCALE;
		
		m.x -= 4;
		m.y -= 12;
		return m;
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
//		Point p = getMouse();
//		for(PSPlanet x : master.state.getAllPlanets()){
//			if(within(x, p)){
//				master.click(x, false);
//			}
//		}
	}

	@Override
	public void run() {
		new Thread(this);
	}
	
	private void drawNumber(int n, double x, double y){
		int offset = 0;
		int xS = (int) (x*WIDTH);
		int yS = (int) (y*HEIGHT);
				
		do{
			drawDigit(n%10, xS - offset*4, yS - 2);
			n /= 10;
			offset++;
		}while(n > 0);
	}

	private void drawDigit(int n, int xS, int yS) {
		//row1
		if(isOf(n, 5, 7, 9))		pixel(xS+0, yS+0);
		if(n != 4)					pixel(xS+1, yS+0);
		if(n >= 4)					pixel(xS+2, yS+0);
		
		//row2
		if(!isOf(n, 4, 7))			pixel(xS+0, yS+1);
		if(isOf(n, 1, 4))			pixel(xS+1, yS+1);
		if(isOf(n, 0, 2, 3, 7, 8, 9))	pixel(xS+2, yS+1);
		
		//row3
		if(isOf(n, 0, 4, 5, 6, 9))		pixel(xS+0, yS+2);
		if(isOf(n, 1, 5, 8, 9))		pixel(xS+1, yS+2);
		if(isOf(n, 0, 2, 3, 5, 7, 9))	pixel(xS+2, yS+2);
		
		//row4
		if(isOf(n, 0, 4, 6, 8))		pixel(xS+0, yS+3);
		if(!isOf(n, 0, 4, 5, 8, 9))	pixel(xS+1, yS+3);
		if(!isOf(n, 1, 2, 3, 7))	pixel(xS+2, yS+3);
		
		//row5
		if(!isOf(n, 1, 3, 7, 9))	pixel(xS+0, yS+4);
		if(isOf(n, 1, 4, 7))		pixel(xS+1, yS+4);
		if(!isOf(n, 1, 2, 7))		pixel(xS+2, yS+4);
		
		//row7
		if(isOf(n, 1, 2, 3))		pixel(xS+0, yS+5);
		if(n != 4)					pixel(xS+1, yS+5);
		if(!isOf(n, 0, 5, 6, 7, 8))	pixel(xS+2, yS+5);
	}
	
	private boolean isOf(int query, int... sequence) {
		for(int i = 0; i < sequence.length; i++){
			if(query == sequence[i]){
				return true;
			}
		}
		return false;
	}

	private void pixel(int xS, int yS){
		int index = xS + yS*WIDTH;
		if(index < 0 || index >= screenPixels.length)
			return;
		screenPixels[index] = 0xffffff;
	}
	
	public void addMoment(PState s, PMove p1o, PMove p2o){
		moments.add(new Moment(s, p1o, p2o));
	}
	
	public void clearMoments(){
		moments.clear();
	}
	
	//////// INNER CLASSES /////////
	
	private class Moment{
		PState s;
		PMove p1o, p2o;
		
		Moment(PState s, PMove p1o, PMove p2o){
			this.s = s;
			this.p1o = p1o;
			this.p2o = p2o;
		}
	}
}
