
import javax.swing.*;
import java.awt.*;

public class Panel {
	JFrame frame;
	JPanel pan;
	JButton but;
	JLabel lab;
	
	Panel(){
		frame = new JFrame("title");
		frame.setVisible(true);
		frame.setSize(600, 400);
		
		pan = new JPanel();
		but = new JButton("button");
		lab =  new JLabel("labelz");
		
		pan.add(but);
		pan.add(lab);
		frame.add(pan);
	}
	
	private void start() {
		System.out.println("ye");
	}

	public static void main(String[] args){
		new Panel().start();
	}
}
