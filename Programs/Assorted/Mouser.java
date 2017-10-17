package Assorted;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Mouser implements MouseListener{
	boolean mousePressed = false;

	public void mouseClicked(MouseEvent e) {

		System.out.println("NIJ");
		mousePressed = true;
	}

	public void mouseEntered(MouseEvent e) {
		//nothing
	}

	public void mouseExited(MouseEvent e) {
		//nothing
	}

	public void mousePressed(MouseEvent e) {
		//nothing
	}

	public void mouseReleased(MouseEvent e) {
		mousePressed = false;
	}

	public boolean mousePressed() {
		return mousePressed;
	}
}
