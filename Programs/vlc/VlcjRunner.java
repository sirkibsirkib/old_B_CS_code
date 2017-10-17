package vlc;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.windows.Win32FullScreenStrategy;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

public class VlcjRunner {
	JFrame f = new JFrame();
	Canvas c = new Canvas();
	JPanel p = new JPanel();
	MediaPlayerFactory mpf;
	EmbeddedMediaPlayer emp;

	private void start() {
		initJFrame();
		
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "C:/Program Files (x86)/VideoLAN/VLC");
		System.out.println(RuntimeUtil.getLibVlcLibraryName());
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
		System.out.println("Load Library OK");
		
		initMediaPlayer();
		
		String file = "C:/Program Files (x86)/VideoLAN/Holde/locker/EPORNER.COM - "
				+ "[529017] Busty Sex Teacher Sarah Jessie Gets Fucked (720p)";
		emp.prepareMedia(file);
		emp.play();
		
	}

	private void initMediaPlayer() {
		mpf = new MediaPlayerFactory();
		emp = mpf.newEmbeddedMediaPlayer(new Win32FullScreenStrategy(f));
		emp.setVideoSurface(mpf.newVideoSurface(c));
		emp.toggleFullScreen();
		emp.setEnableMouseInputHandling(false);
		emp.setEnableKeyInputHandling(false);
		System.out.println("Media Player OK");
	}

	private void initJFrame() {
		f.setLocation(100, 100);
		f.setSize(1000, 800);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		c.setBackground(Color.BLACK);
		p.setLayout(new BorderLayout());
		p.add(c);
		f.add(p);
		System.out.println("JFrame OK");
	}
	
	public static void main(String[] args){
		new VlcjRunner().start();
	}
}
