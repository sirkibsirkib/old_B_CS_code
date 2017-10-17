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


public class KeyManipulator extends Canvas implements Runnable{
	private static final long serialVersionUID = 6274022486809918337L;
	private static final int WIDTH = 300,
							HEIGHT = 200;
	private JFrame frame;
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private int[] screenPixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	Robot robot;
	
	private static final int LONG_SEC = 10, 		LONG_KEY = KeyEvent.VK_O,
							MED_SEC = 33, 			MED_KEY = KeyEvent.VK_I,
							SHORT_SEC = 100,		SHORT_KEY = KeyEvent.VK_U,
							SLEEP_MILLI = 20_000,	NEXT_KEY = KeyEvent.VK_N;
	
	public KeyManipulator() {
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		setMaximumSize(new Dimension(WIDTH, HEIGHT));
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		//SetupRobot();
		
	}
	
	public void go(){
		new Thread(this).start();
	}

	private void SetupRobot() {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	private void SetupJFrame() {
		frame = new JFrame("DIQ");
		setFocusTraversalKeysEnabled(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(this, BorderLayout.CENTER);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	private void render(){
		System.out.println("render");
		BufferStrategy bs = getBufferStrategy();
		if(bs == null){
			createBufferStrategy(3);
			return;
		}
		for(int i = 0; i < WIDTH; i++){
			for(int j = 0; j < HEIGHT; j++){
				int index = i + (i*j);
				screenPixels[index] = 0x000000;
			}
		}
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		bs.show();
	}
	
	public void run(){
		render();
		sleep(5_000);
		SetupJFrame();
		
		
		while(true){
			render();
			sleep(1_000);
			if(chance(4))
				next();
			else
				jump(wrng(2*60));
			sleep(wrng(SLEEP_MILLI/2) + 1);
			if(chance(2))
				robot.mouseWheel(-1);
			sleep(wrng(SLEEP_MILLI/2) + 1);
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
	
	public static void sleep(int millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		new KeyManipulator().go();
	}
}
