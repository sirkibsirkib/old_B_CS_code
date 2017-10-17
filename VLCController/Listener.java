import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Listener implements KeyListener{
	private VLCRunner master;
	
	Listener(VLCRunner master){
		master.addKeyListener(this);
		this.master = master;
	}
	
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()){
		case KeyEvent.VK_ESCAPE:	master.escape();	return;
		case KeyEvent.VK_SPACE:		master.space();		return;
		
		case VLCRunner.LONG_KEY:
		case VLCRunner.MED_KEY:
		case VLCRunner.SHORT_KEY:
		case VLCRunner.NEXT_KEY:		master.halt();		return;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
