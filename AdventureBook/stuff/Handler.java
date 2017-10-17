package stuff;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import ui.DrawUserInterface;
import ui.Event;

public class Handler implements Runnable {
	private Adventure adv;
	private View v;
	private PageJumpListener pjl;
	
	public Handler(Adventure adv, View v){
		this.adv = adv;
		this.v = v;
		pjl = new PageJumpListener(this);
		new Thread(pjl).start();
	}
	
	public void run(){
		DrawUserInterface drawer = v.getUi();
		drawer.enableEventProcessing(true);
		
		while(true){
			Event ev = drawer.getEvent();
			hotkeys(ev.name, ev.data);
			//System.out.println(ev.name + " " + ev.data);
			if(ev.name.equals("click") && ev.data.equals("pageButton"))
				adv.pageButtonPressed();
			if(ev.name.equals("click") && ev.data.equals("mapButton"))
				adv.toggleMap();
			if(ev.name.equals("click") && ev.data.equals("masterButton"))
				try {
					adv.masterButtonPressed();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if(ev.name.equals("click") && ev.data.startsWith("out")){
				Scanner inputScanner = new Scanner(ev.data.substring(4, ev.data.length()));
				adv.outButtonPressed(inputScanner.nextInt());
				inputScanner.close();
			}
			if(ev.name.equals("click") && ev.data.startsWith("edi")){
				Scanner inputScanner = new Scanner(ev.data.substring(4, ev.data.length()));
				adv.outEditButtonPressed(inputScanner.nextInt());
				inputScanner.close();
			}
			if(ev.name.equals("click") && ev.data.startsWith("in")){
				Scanner inputScanner = new Scanner(ev.data.substring(3, ev.data.length()));
				adv.inButtonPressed(inputScanner.nextInt());
				inputScanner.close();
			}
			if(ev.name.equals("click") && ev.data.startsWith("addOut")){
				adv.addOut();
			}
			if(ev.name.equals("mouseover") && ev.data.startsWith("dot")){
				Scanner inputScanner = new Scanner(ev.data.substring(4, ev.data.length()));
				adv.setToolTipFromPageIndex(inputScanner.nextInt());
				inputScanner.close();
			}
			if(ev.name.equals("click") && ev.data.startsWith("dot")){
				Scanner inputScanner = new Scanner(ev.data.substring(4, ev.data.length()));
				adv.jumpToPage(inputScanner.nextInt());
				inputScanner.close();
			}
			if(ev.name.equals("mouseexit") && ev.data.startsWith("dot")){
				adv.removeTooltip();
			}
			if(ev.name.equals("number")){
				Scanner inputScanner = new Scanner(ev.data);
				try {
					pjl.numberPressed(inputScanner.nextInt());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				inputScanner.close();
			}
		}
	}

	private void hotkeys(String name, String data) {
		if(name.equals("other_key") && data.equals("N"))
			adv.newPageHotkey();
//		if(name.equals("other_key") && data.equals("J"))
//			adv.newLinkHotkey();
		if(name.equals("other_key") && data.equals("M"))
			adv.toggleAnimationStyle();
		if(name.equals("letter") && data.equals("d"))
			if(adv.getDrawingMap())
				v.toggleDetailed();
		if(name.equals("other_key") && data.equals("Escape"))
			adv.askForExit();
		if(name.equals("letter") && data.equals("m"))
			adv.toggleMap();
		if(name.equals("other_key") && data.equals("L")){
			try {
				adv.load();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if(name.equals("other_key") && data.equals("S")){
			try {
				adv.save();
			} catch (FileNotFoundException | UnsupportedEncodingException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		}
	}

	public void pageRequested(int value) {
		adv.tryToGoToPageIndex(value - 1);
	}
}
