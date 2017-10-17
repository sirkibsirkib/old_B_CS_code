import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

public class VLCRunner extends Canvas implements Runnable{
	private static final String NAME	= "VLCRobot";
	private static final long serialVersionUID = 7283472734987L;
	public static boolean running = false;
	
	private static int WIDTH = 300, HEIGHT = 200;
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private int[] screenPixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	
	long nextTick;
	
	private JFrame frame;
	private static final int FPS = 30,
			GREEN = 0x00ff22,
			BLACK = 0x000000;
	Robot robot;

	
	public static final int LONG_SEC = 10, 			LONG_KEY = KeyEvent.VK_O,
							MED_SEC = 33, 			MED_KEY = KeyEvent.VK_I,
							SHORT_SEC = 100,		SHORT_KEY = KeyEvent.VK_U,
							SLEEP_TIME = 20_000,	NEXT_KEY = KeyEvent.VK_N;
	
	public VLCRunner() {
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		setMaximumSize(new Dimension(WIDTH, HEIGHT));
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		
		paint(BLACK);
		prepareJFrame();
		setupRobot();
		
	}
	
	private void setupRobot() {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	private void prepareJFrame() {
		frame = new JFrame(NAME);
		setFocusTraversalKeysEnabled(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(this, BorderLayout.CENTER);
		frame.pack();
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public void run() {
		new Listener(this);
		long lastRender = now();
		double millisPerRender = 1_000/FPS;
		while(true){
			sleep(30);
			long now = now();
			
			if(running && now >= nextTick){
				tick();
				nextTick = now() + tickTime();
			}
			
			if(now - lastRender > millisPerRender){
				render();
				lastRender = now; 
			}
		}
	}
	
	private long tickTime() {
		long result = wrng(SLEEP_TIME) + 1_000;
		System.out.println(result);
		return result;
	}

	private long now(){
		return System.currentTimeMillis();
	}
	
	private void tick(){
		if(chance(2))
			robot.mouseWheel(-1);
		if(chance(4)){
			next();
			sleep(150);
		}
		jump(wrng(2*60));
	}
	
	public void render(){
		BufferStrategy bs = getBufferStrategy();
		if(bs == null){
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		bs.show();
	}

	private void paint(int colour) {
		for(int i = 0; i < WIDTH; i++){
			for(int j = 0; j < HEIGHT; j++){
				screenPixels[i + WIDTH*j] = colour;
			}
		}
	}
	
	public static void sleep(int millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void escape() {
		System.exit(0);
	}

	public void space() {
		if(running){
			running = false;
			paint(BLACK);
		}else{
			running = true;
			nextTick = now() + tickTime();
			paint(GREEN);
		}
	}
	
	private void next() {
		robot.keyPress(NEXT_KEY);
	}

	private boolean chance(int sides) {
		return rng(sides) == 0;
	}

	private int rng(int range) {
		return (int)(Math.random()*range);
	}

	private int wrng(int range) {
		double pow = Math.random()*Math.random();
		return (int)(pow*range);
	}

	public void jump(int seconds){
		while(seconds > LONG_SEC){
			robot.keyPress(LONG_KEY);
			seconds -= LONG_SEC;
		}
		while(seconds > MED_SEC){
			robot.keyPress(MED_KEY);
			seconds -= MED_SEC;
		}
		while(seconds > SHORT_SEC){
			robot.keyPress(SHORT_KEY);
			seconds -= SHORT_SEC;
		}
		System.out.println("JUMP!");
	}

	public void halt() {
		if(running){
			running = false;
			paint(BLACK);
		}
	}

	public synchronized void go() {
		new Thread(this).start();
	}
	
	public synchronized void stop() {
		running = false;
	}
	
	public static void main(String[] args) {
		new VLCRunner().go();
	}
}
