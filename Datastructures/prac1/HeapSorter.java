package prac1;

class HeapSorter {
	/* DATASTRUCTURES AND ALGORITHMS 2015-2016
	 * Practical assignment 1
	 * Deadline: Thursday September 17, 2015
	 * 
	 * Name(s) :  
         * VUid(s) : cee600
	 */
	
	/* NOTE: binary and ternary methods have been segregated.
     * originally the two sets of methods were one, using a parameter to distinguish the base of the heap,
     * but they were separated to make more detailed annotations possible.
     * This results in a great deal of duplicate code between the two sets of methods.
     * 
     * Due to the overlap, the bulk of the annotations are given in the code for binary heap sort.
     * The annotations in the code for ternary heap-sort largely just distinguish its differences
     * from that of binary heap-sort.
	 */
	
	
	
	//BINARY
	
	
	
	/* The binary heapSort algorithm has complexity of n*log(2)n.
	 * log(2)n represents the time taken to place one element in the correct position.
	 * Since this must be done n times, a complexity of n*log(2)n is logical.
	 */
	public static int[] binaryHeapSort (int[] a) {
		//Building the heap has time complexity of order n. For more details, see the method itself
		binaryBuildMaxHeap(a);
		
		/* "firstOutput" is the variable indicating the first element of the sorted output sequence.
		 * The execution of the contents of a single loop has a complexity in log(2)n.
		 * The loop iterates (n) times, contributing a complexity of at worst n*log(2)n.
		 * This, being the main loop of the algorithm, gives the sorting its complexity.
		 * 
		 * We iterate from the last to the first element in the array, moving the max out of the heap,
		 * moving a leaf into it's place, and then restoring the heap property.
		 */
		for(int firstOutput = a.length-1; firstOutput > 0; firstOutput--){
			
			//The max is moved to the next (sequentially, first or leftmost) space of the output sequence.
			swap(a, 0, firstOutput);
			
			/* The top of the heap now might violate the heap property.
			 * So, a bubbleDown is initiated at the top of the heap to correct this.
			 * Each bubble down has a potential recursive depth of log(2)n
			 */
			binaryBubbleDownMax(a, 0, firstOutput);
		}
		/* As soon as the loop terminates, there are n elements in the
		 * output sequence, and 0 heap elements, completing the sorting process.
		 */
		
		//The sorted array is returned once
		return a;
	}
	
	
	
	//This method accepts any integer array and returns it with the elements rearranged to form a valid max heap.
	static void binaryBuildMaxHeap (int[] a){
		
		/* The last half of the array is guaranteed to be leaf nodes, as they are "exposed" to the bottom.
		 * The higher the base of the heap, the more the heap tends to be a linear shape (maximum "exposure").
		 * Being a leaf is the stop condition of the bubble-down process, and so initiating the process there is fruitless.
		 * The number of loops here is thus n/2.
		 * This entire loop has a worst possible complexity in the order of n*log(2)n.
		 * It is generally smaller, as most bubble down calls don't travel the entire height of the tree.
		 * 
		 * We iterate from the first non-leaf node, back up towards the top of the tree, initiating bubbleDown there each time.
		 */
		for(int i = a.length/2; i >= 0; i--){
			
			//One such bubble down call takes, worst case, log(2)n
			binaryBubbleDownMax(a, i, a.length);
		}
	}
	
	
	
	/* BubbleDown is a recursive method that makes sure that a parent node is not smaller than any of it's children.
	 * The method searches for the biggest child, and if that child is larger than the parent,
	 * it swaps that child with the parent. "parent" is here the index at a[top].
	 * If such a swap occurs, a new bubbleDown is initiated at the former position of the largest child.
	 */
	private static void binaryBubbleDownMax (int[] a, int top, int length) {
		
		//By default, the parent is the largest.
		int largest = top;
		
		//This loop iterates once per child and tests them individually.
		for(int i = 1; i <= 2; i++){
			
			//Regardless of base, the index of the leftmost child is parent*base + 1.
			int childIndex = top*2 + i;
			
			//A child index might be out of bounds of the heap / array, thus the bounds are tested first.
			if(childIndex < length && a[childIndex] > a[largest])
				
				//If the label of the child is larger than the current largest, the child becomes the largest.
				largest = childIndex;
		}
		//At the end of the loop, the index of the largest node is stored in "largest".
		
		/* This is the recursive stop condition. The bubble down terminates when there
		 * are no larger children found, which could mean the children are all not larger,
		 * or the children do not exist (ie. the top node is a leaf).
		 */
		if(top == largest){
			
			//The recursion stops. Any nodes below this one are deemed to be correctly structured.
			return;
		}
		//If the stop condition is not met and this line is reached, the bubble down recurses further.
		
		//The largest child and the parent swap their "values" or "labels".
		swap(a, largest, top);

		//The method recurses inwards with a new top node indicated(bubbling down).
		binaryBubbleDownMax(a, largest, length);
	}
	
	
	
	//TERNARY
	
	
	
	/* Since much is the same between the two, the bulk of the annotations are given in the
     * code for binary heap sort.
     * The annotations for ternary are largely just distinguishing its differences from binary.
	 */
	

	/* The ternary heapSort algorithm has complexity of n*log(3)n.
	 * The reduced depth of a ternary heap might be tempting in leu of a binary tree, but if one
	 * considers the original appeal of a heap, the benefits of binary become clearer.
	 * The whole point of a heap is that it prevents you from having to compare every element to
	 * every other element in the array, as some assumptions can be made as long as one takes care to
	 * maintain the heap property.
	 * In a "wider" tree the larger bases would provide, it becomes necessary to compare more values
	 * to one another as each parent node has more children to compare itself to.
	 * The advantage of a ternary tree instead of a binary tree is a shallower recursive depth per bubble down.
	 */
	public static int[] ternaryHeapSort (int[] a) {
		ternaryBuildMaxHeap(a);
		
		/* The execution of the contents of a single loop has a complexity in log(3)n.
		 * The loop iterates (n) times, contributing a complexity of at worst n*log(3)n.
		 */
		for(int firstOutput = a.length-1; firstOutput > 0; firstOutput--){
			
			//the max is moved to the next (sequentially, first or leftmost) space of the output sequence.
			swap(a, 0, firstOutput);
			
			/* the top of the heap now might violate the heap property.
			 * so, a bubbleDown is initiated at the top of the heap to correct this.
			 * each bubble down has a potential recursive depth of log(2)n
			 */
			ternaryBubbleDownMax(a, 0, firstOutput);
		}
		return a;
	}
	
	
	
	//this method accepts any int array and returns it with the elements rearranged to form a valid max heap.
	static void ternaryBuildMaxHeap (int[] a){
		
		/* the last two thirds of the array are guaranteed to be leaf nodes, he number of loops here is thus n/3.
		 * this entire loop has a worst possible complexity in the order of n*log(3)n.
		 * 
		 * We iterate from the first non-leaf node, back up towards the top of the tree, initiating bubbleDown there each time.
		 */
		for(int i = a.length/3; i >= 0; i--){
			
			//One such bubble down call takes, worst case, log(3)n
			ternaryBubbleDownMax(a, i, a.length);
		}
	}
	
	
	
	/* the down-side of a low base is the increased depth of the tree for a bubble down, meaning more calls.
	 * the upside of a high base is a smaller constant factor from having one less child to check.
	 * The value of one over the other depends on how willing one is to suffer the trade-off
	 */
	private static void ternaryBubbleDownMax (int[] a, int top, int length) {
		int largest = top;
		
		//now with three children, three comparisons must be made.
		for(int i = 1; i <= 3; i++){
			int childIndex = top*3 + i;
			if(childIndex < length && a[childIndex] > a[largest])
				largest = childIndex;
		}
		//at the end of the loop, the index of the largest node is stored in "largest".
		
		// this is the recursive stop condition.
		if(top == largest){
			return;
		}
		swap(a, largest, top);

		//The method recurses inwards with a new top node indicated.
		ternaryBubbleDownMax(a, largest, length);
	}
	
	
	//COMMON	
	
	
	/* this method takes a copy of an integer array with two index positions, and returns
	 * the array with the values in those positions swapped.
	 * This method is shared by both heap-sorts because the base of the heap is irrelevant.
	 */
	private static void swap(int[] a, int indexA, int indexB){
		
		//using one extra integer space, the two values at indexA and indexB are exchanged
		int c = a[indexA];
		a[indexA] = a[indexB];
		a[indexB] = c;
	}
	
	static int[] start (int[] toSort, boolean binaryHeap) {
		int[] sorted;
		if (binaryHeap)	//binary heap
			sorted = binaryHeapSort(toSort);
		else			//ternary heap
			sorted = ternaryHeapSort(toSort);		
		
		return sorted;
	}
}