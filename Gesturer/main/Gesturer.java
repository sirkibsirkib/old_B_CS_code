package main;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Gesturer implements MouseListener{
	private boolean mousePressed = false;
	private JFrame frame;
	private JLabel chatLabel;
	private static int WIDTH = 900;
	
	private void start() {
		frame = newJFrame();
		List<Point> trail;
		List<Point> model = makeModel();
		while(true){
			if(mousePressed){
				trail = createTrail();
				print((int)(100 * 100 /(compareTrails(trail, model) + 20)) + " damage!");
			}
			sleep(100);
		}
	}
	
	private List<Point> makeModel() {
		List<Point> m = null;
		int NEEDED = 10;
		print("Please make " + NEEDED + " unique swipe motions");
		int i = 0;
		int get = rng(NEEDED);
		while(i < NEEDED){
			if(mousePressed){
				List<Point> n = createTrail();
				if(i == get)
					m = n;
				print((NEEDED-i-1) + " more times");
				i++;
			}
			sleep(100);
		}
		print("Now try to match the swipe");
		return m;
	}

	private void print(String s) {
		chatLabel.setText(s);
	}

	private int rng(int i) {
		return (int) (Math.random()*i);
	}

	private JFrame newJFrame() {
		JFrame frame = new JFrame("JFrame Example");
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		JLabel label = new JLabel("This is a label!");
		label.setFont(new Font("Serif", Font.PLAIN, 24));
		chatLabel = label;
		panel.add(label);
		frame.add(panel);
		frame.setSize(WIDTH, WIDTH);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.addMouseListener(this);
		return frame;
	}

	private void sleep(int i) {
		try {Thread.sleep(i);} catch (InterruptedException e) {}
	}
	
	private double compareTrails(List<Point> a, List<Point> b){
		standardizeTrail(a);
		standardizeTrail(b);
		double diff = 0;
		if(a.size() > b.size()){
			List<Point> c = a;
			a = b;
			b = c;
		} // a <= b
		double sizeProp = 1.0 * a.size() / b.size();
		for(int i = 0; i < a.size(); i++){
			double thisDiff = distance(a.get((int)(i*sizeProp)), b.get(i));
			if(thisDiff < 30)
				thisDiff *= thisDiff / 30;
			diff += thisDiff;
		}
			
		System.out.println();
		diff += (b.size()-a.size()) * WIDTH;
		return diff / b.size();
	}

	private List<Point> createTrail(){
		List<Point> pts = new ArrayList<>();
		while(mousePressed){
			Point next = getMouse();
			next.x -= frame.getX();
			next.y -= frame.getY();
			pts.add(next);
			sleep(5);
		}
		return pts;
	}
	
	private void standardizeTrail(List<Point> trail){
		if(trail.size() == 0)
			return;
		double xOffset = trail.get(0).getX();
		double yOffset = trail.get(0).getY();
		for(Point p : trail){
			p.x -= xOffset;
			p.y -= yOffset;
		}
	}

	private double distance(Point a, Point b){
		return Math.sqrt(sqr(a.getX()-b.getX())+ sqr(a.getY() - b.getY()));
	}
	
	private double sqr(double d) {
		return d*d;
	}

	private Point getMouse(){
		return MouseInfo.getPointerInfo().getLocation();
	}
	
	public static void main(String[] args){
		new Gesturer().start();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		//do nothing
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		//do nothing
	}

	@Override
	public void mouseExited(MouseEvent e) {
		//do nothing
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mousePressed = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mousePressed = false;
	}
}
