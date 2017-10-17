package prac2;

public class Tasks {

  /* DATASTRUCTURES AND ALGORITHMS 2015-2016
  * Programming assignment 2
  * Deadline: Thursday October 8, 2015, before or at 23:59
  * 
  * Name(s) :	Christopher Esterhuyse
  * VUid(s) :	cee600
  */

	
	
	
	
	
  // --------------------------------------------------------
  //   METHODS FOR TASK 1 BELOW THIS LINE
  // --------------------------------------------------------
	
	
	
  /* For ease of use, the user is spared of having to work
   * with the length of the array. This helper method bridges the gap
   * between what the user supplies and what the recursive function needs.
   */
  public static int task1 (int[] a) {
	  return rodCutRecursive(a, a.length);
  }
  
  private static int rodCutRecursive(int[] a, int n){
	  //recursive stop case
	  int max = a[0];
	  
	  for(int i = 1; i < n; i++){
		  
		  /* The recursive call here can potentially have a depth of n,
		   * revealing the down-side of this approach
		   */
		  int thisCut = rodCutRecursive(a, i) + a[n-i];
		  if(thisCut > max)
			  max = thisCut;
	  }
	  return max;
  }
  
  
  
  // --------------------------------------------------------
  //   METHODS FOR TASK 2 BELOW THIS LINE
  // --------------------------------------------------------
  
  
  
  /* The dynamic programming solution to rod cutting involves an iterative and
   * a component that resembles the old recursive method. Here they are split in two.
   */
  public static int task2 (int[] a) {
	  
	  //An array is initialized to hold all the values for "best results with rod of length n"
	  int[] maxes = new int[a.length];
	  
	  maxes[0] = a[0];
	  
	  /* We walk from the shortest to longest possible given rod, and determine
	   * the maximum value of such a length of rod, from the bottom up, iteratively.
	   */
	  for(int i = 1; i < a.length; i++){
		  
		  //The second function is called to use existing data to create new data.
		  int thisMax = maxRodCutAt(maxes, a, i);
		  
		  //The new data is saved for the next loop iteration to use.
		  maxes[i] = thisMax;
	  }
	  
	  //The maximum value of the longest given length is our ultimate result.
	  return maxes[a.length-1];
  }
  
  /* This method looks like the old recursive function, except for an extra parameter
   * for accessing "remembered" values of shorter pipes. This is the optimization gained
   * from the dynamic programming approach.
   */
  private static int maxRodCutAt(int[] maxes, int[]a, int n){
	  
	  //Base case is given as the value of the rod with no cut performed.
	  int max = a[n];
	  
	  //each smaller rod is considered
	  for(int i = 1; i < n; i++){
		  int thisCut = maxes[n-i] + a[i];
		  
		  //in the event such a cut is fruitful, the "max value" is updated
		  if(thisCut > max)
			  max = thisCut;
	  }
	  return max;
  }
  
  
  
  // --------------------------------------------------------
  //   METHODS FOR TASK 3 BELOW THIS LINE
  // --------------------------------------------------------
  
  /* maxH, maxV, maxD and maxC all each consist of two nested for loops.
   * Ignoring some messy details with the diagonals, we can say that they are each
   * in complexity domain O(x^2) where x is the width of the input array.
   * There are a constant number of such steps (4, to be exact) and so we say
   * the ultimate time complexity of task3 is in O(x^2) also.
   * 
   * The main method here does no work beyond delegating the sub-problems to other methods.
   * Please see individual methods for more details
   */
   public static int task3 (int[][] m) { // m has size n x n (i.e. it is square)
	  int h, v, d, c;
	  
	  h = maxH(m);
	  v = maxV(m);
	  d = maxD(m);
	  c = maxC(m);
	  
	  return h + v + d + c;
  }
  
   
   
  /* Many of the following methods have much in common, but different in key aspects.
   * Each new feature is annotated in the first method in which it is present.
   * This results in the first methods being more heavily annotated than the later ones.
   */
  
  
  
  //HORIZONTAL
  
  
  
  /* The maxH method is bounded by O(m^2) where m is the width of the input array, since each
   * element is "walked over" in maxHH
   * maxH method determines the largest ROW value. by comparing outputs of maxHH calls.
   */
   
  private static int maxH(int[][] m){
	  
	  //if the array has no elements, the result is zero.
	  int totalMax = 0;
	  
	  //this loop is executed once per row (as many times as the width of the input array)
	  for(int i = 0; i < m.length; i++){
		  
		  /* a function call to maxHH does the heavy lifting, and is called
		   * x times, where x is the width of the given array
		   */
		  int rowMax = maxHH(m, i);
		  
		  //each row max result is given the opportunity to be the largest
		  if(i == 0)
			  totalMax = rowMax;
		  else
			  totalMax = max(totalMax, rowMax);
	  }
	  return totalMax;
  }
  
  /* maxHH is relatively simple. It could be simpler still by passing m[row]
   * arrays, but the entire value of m is passed to keep the similarity with
   * the other methods intact and clear
   * 
   * The method is called x times, where x is the width of the input array, thus each element
   * in the array is considered once overall.
   */
  private static int maxHH(int[][] m, int row){
	  
	  /* previousMax will be the value that serves as our "maxSoFar" array.
	   * by only using a single integer and overwriting its value, we can avoid
	   * the creation of a helper array, though that method is also possible.
	   */
	  
	  int previousMax = 0;
	  
	  //a single integer holds the overall result of the max sub array
	  int maximum = 0;
	  
	  /* the loop runs once per column in the array. thus the contents of this loop
	   * are executed (overall) once per element in the entire matrix
	   */
	  for(int i = 0; i < m[row].length; i++){
		  
		  /* the sub-array might BEGIN with the given index, thus throwing
		   * away the sum so far. this is represented in code by choosing between
		   * the max of zero, and the sum so far PLUS the next element
		   */
		  
		  previousMax = max(0, previousMax + m[row][i]);
		  
		  //maximum of the whole row is updated if necessary
		  maximum = max(maximum, previousMax);
	  }
	  
	  //the maximum of the row is returned, to be used by the maxH method
	  return maximum;
  }
  
  
  
  //VERTICAL
  
  
  
  /* The version of the two previous methods is almost identical for the
   * vertical max sub array. The maxV method, in contrast to maxH is
   * distinguished  by considering columns instead of rows
   * 
   * Complexity-wise, maxV is identical to maxH. The same number of computations are performed.
   * One might point out that with larger arrays, the fact that we are accessing different arrays more
   * frequently might lead to less efficient caching, and thus more time needed, but for the
   * sake of sanity, we will call them identical :)
   */
  private static int maxV(int[][] m){
	  int totalMax = 0;
	  for(int i = 0; i < m.length; i++){
		  
		  //the maxVV method does the heavy lifting here
		  int columnMax = maxVV(m, i);
		  
		  if(i == 0)
			  totalMax = columnMax;
		  else
			  totalMax = max(totalMax, columnMax);
	  }
	  return totalMax;
  }
  
  /* This is the first time we understand the need to pass the entire matrix
   * It is indeed possible to use 5 methods instead of 8 methods by using maxV to BUILD
   * and ordinary "row" for one common maxSubArray method to work with, but this would
   * result in a lot of needless computation.
   * Instead we opt for a more personalized approach for each case, and make do with the
   * array itself as an input
   * 
   * The column parameter is used to tell this auxiliary method which array values it
   * should consider and which it should ignore for "this" column subArray result.
   */
  private static int maxVV(int[][] m, int column){
	  int maximum = 0;
	  int previousMax = 0;
	  
	  for(int i = 0; i < m.length; i++){
		  
		  /* The biggest difference from maxHH lies here. Different a arrays are
		   * accessed on each loop iteration, instead of different array entries
		   * in a[row] as it was with maxVV.
		   */
		  previousMax = max(0, previousMax + m[i][column]);
		  maximum = max(maximum, previousMax);
	  }
	  return maximum;
  }
  
  
  
  //DIAGONAL ONE
  
  
  
  /* whether this "diagonal" is from top-left or bottom-left etc. depends
   * on how the array is represented graphically. Thus, to avoid confusion, 
   * here we refer only to "Diagonal one" and "Diagonal two".
   */
  private static int maxD(int[][] m){
	  int totalMax = 0;
	  
	  /* The number of "rows" that are used is actually not equal to the width
	   * of the array this time. Here a small auxiliary method does the calculation.
	   * More detail can be found at the method itself.
	   */
	  for(int i = 0; i < diagonalRowCount(m.length); i++){
		  int dRowMax = maxDD(m, i);
		  
		  if(i == 0)
			  totalMax = dRowMax;
		  else
			  totalMax = max(totalMax, dRowMax);
	  }
	  return totalMax;
  }
  
  /* maxD is indeed called MORE times than maxV or maxH with the same input matrix,
   * as there are more diagonal rows than horizontal or vertical rows.
   * But the number of computations for each row shrinks at the exact same rate, and
   * ultimately, there are roughly as many computations performed per element.
   */
  private static int maxDD(int[][] m, int dRow){	  
	  /* x and y are variables that exist to guide the for loop over the correct
	   * matrix values for consideration. Instead of cluttering the internals of
	   * the loop up with complex if statements, the most difficult part 
	   * (the index of the FIRST matrix value) is calculated, and all following values
	   * are simply counted steps away from that one.
	   * 
	   * The ternary operator helps us find the correct value for x and y without
	   * too much unsightly code to make the process difficult to follow.
	   */
	  int x = dRow < m.length ? 0 : (dRow - (m.length-1));
	  int y = dRow < m.length ? ((m.length-1) - dRow) : 0;
	  
	  int previousMax = 0;
	  int maximum = 0;
	  
	  /* The value of i is still used the same was as before to build maxSoFar,
	   * but the length of the "row" we want to check depends on which row we are working on.
	   * An additional function takes care of this for us.
	   */
	  for(int i = 0; i < lengthOfDRow(dRow, m.length); i++){
		  previousMax = max(0, previousMax + m[x][y]);
		  
		  //as we walk along the diagonal entries, x and y change in tandem.
		  x++;
		  y++;
		  maximum = max(maximum, previousMax);
	  }
	  return maximum;
  }
  
  
  
  //DIAGONAL TWO
  
  
  
  private static int maxC(int[][] m){
	  int totalMax = 0;
	  for(int i = 0; i < diagonalRowCount(m.length); i++){
		  int dRowMax = maxCC(m, i);
		  
		  if(i == 0)
			  totalMax = dRowMax;
		  else
			  totalMax = max(totalMax, dRowMax);
	  }
	  return totalMax;
  }
  
  private static int maxCC(int[][] m, int dRow){
	  
	  //The values of x and y are largely different, but the idea is the same
	  int x = dRow < m.length ? dRow : m.length-1;
	  int y = dRow < m.length ? 0 : (dRow - (m.length-1));
	  
	  int previousMax = 0;
	  int maximum = 0;
	  
	  for(int i = 0; i < lengthOfDRow(dRow, m.length); i++){
		  previousMax = max(0, previousMax + m[x][y]);
		  
		  //this time, as y increases, x decreases
		  x--;
		  y++;
		  maximum = max(maximum, previousMax);
	  }
	  return maximum;
  }
  
  
  //AUXILIARY
  
  
  /* This simple method takes at least one input integer, and returns
   * the maximum of all the inputs. It is analogous to Math.max()
   */
  private static int max(int x0, int... x){
	  int max = x0;
	  for(int i = 0; i < x.length; i++){
		  if(x[i] > max)
			  max = x[i];
	  }
	  return max;
  }
  
  
  
  /* This method calculates how many "rows" must be considered for a
   * matrix of given width.
   */
  private static int diagonalRowCount(int width){
	  return width + (width-1);
  }
  
  
  
  /* the length of a given row in a matrix is extracted into this method,
   * to keep the rest of the code as tidy as possible
   */
  private static int lengthOfDRow(int dRow, int width){
	  return width - Math.abs(dRow + 1 - width);
  }
}
