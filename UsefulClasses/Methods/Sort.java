package Methods;

public class Sort {
	
	
	/**recursively merge-sorts an array of strings into ASCII order
	 * 
	 * @param s input string array
	 * @return returns the parameter string with elements rearranged
	 */
	public static String[] mergeSort(String[] s){
		//stop condition
		if(s.length <= 1)
			return s;
		
		//recurse inward
		String[] a = new String[(int)(s.length / 2)];
		String[] b = new String[s.length - a.length];
		
		for(int i = 0; i < a.length; i++) {a[i] = s[i];}
		for(int i = 0; i < b.length; i++) {b[i] = s[i + a.length];}
		a = mergeSort(a);
		b = mergeSort(b);
		
		//recurse outward sort step
		int selectA = 0;
		int selectB = 0;
		
		for(int i = 0; i < s.length; i++){
			if(selectB == b.length){ //a by default
				s[i] = a[selectA];
				selectA++;
			} else if(selectA == a.length){	//b by default
				s[i] = b[selectB];
				selectB++;
			} else if(ascending(a[selectA], b[selectB])){ // a < b
				s[i] = a[selectA];
				selectA++;
			}else{ // a > b
				s[i] = b[selectB];
				selectB++;
			}
		}
		return s;
	}
	
	
	
	/**returns a recursively quick-sorted string array in ascending ascii order
	 * @param s input string array to be sorted
	 * @return input array with elements rearranged
	 */
	public static String[] quickSort(String[] s){
		int left, right, pivot;
		
		//stop condition
		if(s.length <= 1){
			return s;
		}
		
		//sort step
		pivot = (int)((s.length-1)/2);
		left = 0;
		right = s.length-1;
		
		while(left < right){
			while(ascending(s[left], s[pivot]) && left < pivot){
				left++;
			}
			while(ascending(s[pivot], s[right]) && pivot < right){
				right--;
			}
			if(left < pivot && pivot < right){ //simple swap
				String temp = s[left];
				s[left] = s[right];
				s[right] = temp;
			} else if (left < pivot){ // right ended
				s = moveElement(s, left, pivot);
				pivot--;
				right--;
			} else if (pivot < right){ // left ended
				s = moveElement(s, right, pivot);
				pivot++;
				left++;
			}
		}
		
		//recurse in and out
		if(pivot > 1){
			String[] partA = new String[pivot];
			for(int i = 0; i < partA.length; i++){
				partA[i] = s[i];
			}
			partA = quickSort(partA);
			for(int i = 0; i < partA.length; i++){
				s[i] = partA[i];
			}
		}
		if(s.length-pivot-1 > 1){
			String[] partB = new String[s.length-pivot-1];
			for(int i = 0; i < partB.length; i++){
				partB[i] = s[i+pivot+1];
			}
			partB = quickSort(partB);
			for(int i = 0; i < partB.length; i++){
				s[i + pivot+1] = partB[i];
			}
		}
		return s;
	}
	/**moves an element to a new index position and re-arranges other elements appropriately
	 * @param s input string array
	 * @param fromIndex index position of element to be moved
	 * @param toIndex target index position of element
	 * @return returns input string array with rearranged elements
	 */
	private static String[] moveElement(String[] s, int fromIndex, int toIndex){
		String hold = s[fromIndex];
		if(fromIndex < toIndex){
			for(int i = fromIndex; i < toIndex; i++){
				s[i] = s[i+1];
			}
		}else{
			for(int i = fromIndex; i > toIndex; i--){
				s[i] = s[i-1];
			}
		}
		s[toIndex] = hold;
		return s;
	}
	
	
	
	/**returns true if two strings are in ascending ASCII order, otherwise returns false
	 * @param a first string
	 * @param b second string
	 */
	public static boolean ascending(String a, String b){
		a = a.toLowerCase();
		b = b.toLowerCase();
		
		int minLength = a.length();
		if(b.length() < minLength){minLength = b.length();}
		
		int n = 0;
		while(n < minLength){
			if(a.charAt(n) > b.charAt(n)){return false;}
			if(a.charAt(n) < b.charAt(n)){return true;}
			n++;
		}
		return true;
	}
	

	
	/**returns true if input array is already sorted in ascending ascii order, and false if any elements are out of order 
	 * @param input array to check
	 */
	public static boolean isSorted(String [] s){
		for(int i = 0; i < s.length-1; i++){
			if(!ascending(s[i], s[i+1])){
				return false;
			}
		}
		return true;
	}
	
	
	/**reverses the order of elements of an input string array
	 * @param s the input string array to be reversed
	 * @return the input string array with rearranged elements
	 */
	public static String[] reverse(String[] s){
		String[] reversed = new String[s.length];
		for(int i = 0; i < s.length; i++){
			reversed[s.length-i] = s[i];
		}
		return reversed;
	}
	
	/**Iteratively bogus-sorts a string array in ascending ascii order
	 * WARNING: Bogus search is incredibly inefficient and may lead to a deadlock
	 * @param s input string array to be sorted
	 * @return input string with rearranged elements
	 */
	public static String[] bogusSort(String[] s){
		int a, b;
		boolean sorted = isSorted(s);
		String temp;
		while(!sorted){
			a = Fun.rng(s.length);
			do{
				b = Fun.rng(s.length);
			}while(a == b);
			temp = s[a];
			s[a] = s[b];
			s[b] = temp;
			sorted = isSorted(s);
		}
		return s;
	}
	
	/**returns an iteratively bubble-sorted string array in ascending ascii order
	 * @param s input string array to be sorted
	 * @return input array with elements rearranged
	 */
	public static String[] bubbleSort(String[] s){
		for(int i = 0; i < s.length; i++){
			for(int j = 0; j < s.length-1; j++){
				if(s[j] == null && s[j+1] != null){
					s[j] = s[j+1];
					s[j+1] = null;
				} else if(s[j] != null && s[j+1] != null && !ascending(s[j], s[j+1])){
					String temp = s[j];
					s[j] = s[j+1];
					s[j+1] = temp;
				}
			}
		}
		return s;
	}
	
	/**returns an iteratively bubble-sorted int array in ascending ascii order
	 * @param s input int array to be sorted
	 * @return input array with elements rearranged
	 */
	public static int[] bubbleSort(int[] s){
		for(int i = 0; i < s.length; i++){
			for(int j = 0; j < s.length-1; j++){
				if(s[j] > s[j+1]){
					int temp = s[j];
					s[j] = s[j+1];
					s[j+1] = temp;
				}
			}
		}
		return s;
	}
}
