package stuff;

public class PageJumpListener implements Runnable{
	private Handler handler;
	private int value,
		wait;
	
	PageJumpListener(Handler handler){
		this.handler = handler;
		wait = 0;
		value = 0;
	}
	
	public void run() {
		try {
			listen();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void listen() throws InterruptedException{
		while(true){
			if(wait > 0){
				wait -= 1;
				//System.out.println("wait = " + wait);
			}
			
			Thread.sleep(50);
			if(wait <= 1){
				if(value > 0)
					handler.pageRequested(value);
				value = 0;
			}
		}
	}

	public void numberPressed(int input) throws InterruptedException{
		wait = 10;
		value = (value*10) + input;
		//System.out.println(value);
	}
}
