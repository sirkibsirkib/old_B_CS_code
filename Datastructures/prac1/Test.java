package prac1;

public class Test {
	int printsLeft = 10;
	private void start() {
		for(int i = 0; i < 100; i++)
			System.out.println(testThis());
	}
	
	private long timeTaken(boolean binary, int arraySize){
		long start = System.currentTimeMillis();
		for(int i = 0; i < 100; i++){
			int[] t = randomIntArray(arraySize);
			t = HeapSorter.start(t, binary);
		}
		return System.currentTimeMillis() - start;
		
		
	}

	private boolean testThis() {
		int[] t = randomIntArray(10);
		t = HeapSorter.start(t, false);
		print(t);
		return checkIfSorted(t);
	}
	
	private boolean checkIfSorted(int[] t) {
		if(t.length <= 1)
			return true;
		int max = t[0];
		for(int i = 1; i < t.length; i++){
			if(t[i] < max){
				print(t);
				return false;
			}
			max = t[i];
		}
		return true;
	}

	private int[] randomIntArray(int size) {
		int elements = rng(size);
		int[] nu = new int[elements];
		for(int i = 0; i < elements; i++)
			nu[i] = rng(size);
		return nu;
	}

	private int rng(int max) {
		return (int)(Math.random()*max);
	}

	private void print(int[] a) {
		if(printsLeft == 0)
			return;
		System.out.print("{");
		for(int i = 0; i < a.length; i++){
			System.out.print(a[i]);
			if(i < a.length-1)
				System.out.print(", ");
		}
		System.out.println("}");
		printsLeft--;
	}

	public static void main(String[] args){
		new Test().start();
	}
}
