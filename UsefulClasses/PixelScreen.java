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
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class PixelScreen extends Canvas implements MouseListener, Runnable{
	private static final long serialVersionUID = 481358422745315268L;

	public int width = 400, height = 300, scale = 3;
	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	JFrame jf;
	private int[] screenPixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	
	
	PixelScreen(int width, int height, int scale, String jFrameTitle){
		this.width = width;
		this.height = height;
		this.scale = scale;
		start(jFrameTitle);
	}

	private void start(String jFrameTitle) {
		newJFrame(jFrameTitle);
		
		addMouseListener(this);
        setBackground(Color.BLACK);
	}

	private void clear() {
		for(int i = 0; i < width * height; i++){
			screenPixels[i] = 0;
		}
	}

	private void newJFrame(String jFrameTitle) {
		jf = new JFrame(jFrameTitle);
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		jf.addMouseListener(this);
		
		jf.addWindowListener(new WindowAdapter(){
		    public void windowClosing(WindowEvent e){
		    	jf = null;
		    }
		});
		
		setMinimumSize(new Dimension(width*scale, height*scale));
		setMaximumSize(new Dimension(width*scale, height*scale));
		setPreferredSize(new Dimension(width*scale, height*scale));
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

	void render(List<Renderable> renderObjects){
		BufferStrategy bs = null;
		while(bs == null){
			bs = getBufferStrategy();
			createBufferStrategy(3);
			return;
		}
		
		clear();
		
		if(renderObjects != null){
			for(Renderable r : renderObjects){
				r.render();
			}
		}
		
		
		Graphics g = bs.getDrawGraphics();

		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		bs.show();
		
	}
	
	private double dist(int x1, int y1, int x2, int y2) {
		return Math.sqrt(sqr(x2-x1) + sqr(y2-y1));
	}

	private double sqr(double d) {
		return d*d;
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
		int sX = (int) (x(m) * width);
		int sY = (int) (y(m) * height);
		return dist(sX, sY, p.x, p.y) <= m.growthRate();
	}

	private Point getMouse() {
		Point m = MouseInfo.getPointerInfo().getLocation();
		m.x = (m.x - jf.getX()) / scale;
		m.y = (m.y - jf.getY()) / scale;
		
		m.x -= 4;
		m.y -= 12;
		return m;
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		
	}

	@Override
	public void run() {
		new Thread(this);
	}
	
	private void drawNumber(int n, double x, double y){
		int offset = 0;
		int xS = (int) (x*width);
		int yS = (int) (y*height);
				
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
		int index = xS + yS*width;
		if(index < 0 || index >= screenPixels.length)
			return;
		screenPixels[index] = 0xffffff;
	}
	
	//////// INNER /////////
	
	interface Renderable{
		void render();
	}
}
