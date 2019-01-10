import java.util.*;

public class TSPBruteForce implements ITSPAlgo {
	private CityGraph graph;
	private String[] bestTourSoFar;
	double minCost = Double.POSITIVE_INFINITY;
	int iterations = 0;

	public TSPBruteForce() {
		
	}

	@Override
	public TSPResult runTSP(CityGraph graph) {
		this.graph = graph;
		long startTime = System.nanoTime();
		runBruteForce();
		long endTime = System.nanoTime();
		long runTimeInMS = (endTime - startTime);  //divide by 1000000 to get millisecond
		
		TSPResult result = new TSPResult("Brute Force", bestTourSoFar, minCost, runTimeInMS);
		return result;
	}

	private void runBruteForce() {
		String[] cities = new String[graph.getCities().size()];
		cities = graph.getCityMap().keySet().toArray(cities);
		int n = cities.length;
		int[] c = new int[n];
		computeCost(cities);
		boolean plus = false;
		for (int i = 0; i < n;) {
			if (c[i] < i) {
				if (i % 2 == 0) {
					swap(cities, 0, i);
				} else {
					swap(cities, c[i], i);
				}
				computeCost(cities);
				plus = !plus;
				c[i]++;
				i = 0;
			} else {
				c[i] = 0;
				i++;
			}
		}
	}
	
	private void computeCost(String[] tour) {
		double tempCost = 0;

		tempCost = graph.computeTourCost(tour);

		if (tempCost < minCost) {
			minCost = tempCost;
			bestTourSoFar = Arrays.copyOf(tour, tour.length);
		}

		iterations++;
	}

	/**
	 * Helper method that swaps 2 cities
	 * @param cities
	 * @param i
	 * @param j
	 */
	private void swap(String[] cities, int i, int j) {
		if (i < 0 || i > cities.length - 1) {
			return;
		}
		String temp = cities[i];
		cities[i] = cities[j];
		cities[j] = temp;
	}

}
