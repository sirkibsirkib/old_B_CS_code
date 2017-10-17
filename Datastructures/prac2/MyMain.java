package prac2;

public class MyMain {
	void start(){
		int length = 3;
		int[][] m = new int[length][length];
		for(int i = 0; i < m.length; i++){
			for(int j = 0; j < m[0].length; j++){
				m[i][j] = rng(length+2)-((length+2)/3);
			}
		}
			
		
		int result = Tasks.task3(m);
		System.out.println("result " + result);
	}
	
	private static int rng(int range){
		return (int)(Math.random()*range);
	}
	
	public static void main(String[] args){
		new MyMain().start();
	}
}
