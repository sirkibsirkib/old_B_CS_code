package assignment4;

public interface ClusterMethod {
	/**
	 * Calculates the distance between two clusters based on a
	 * cluster method and a distance measure.
	 * @param cluster1 The first cluster from which the distance is calculated.
	 * @param cluster2 The second cluster to which the distance is calculated.
	 * @return The distance between cluster1 and cluster2.
	 */
	public double CalculateDistance(Cluster a, Cluster b);
	public String name();
}
