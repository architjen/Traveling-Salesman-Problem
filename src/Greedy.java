import java.util.Random;

public class Greedy implements ITSPAlgo {

	private CityGraph graph;
	int n;
	private String[] tour;
	double cost;

	@Override
	public TSPResult runTSP(CityGraph graph) {
		this.graph = graph;
		long startTime = System.nanoTime();
		runGreedy(graph);
		long endTime = System.nanoTime();
		long runTimeInMS = (endTime - startTime); // divide by 1000000 to get millisecond

		TSPResult result = new TSPResult("Greedy Approach", this.tour, cost, runTimeInMS);
		return result;
	}

	public void runGreedy(CityGraph inGraph) {
		graph = inGraph;

		n = graph.getCities().size();
		Random r = new Random();
		int startCityNum = r.nextInt(n) + 1;
		City startCity = graph.getCity(startCityNum);

		Route greedyRoute = new Route(graph);

		City currentCity = startCity;
		greedyRoute.addCityToPath(currentCity);
		startCity.setVisited(true);
		City nextCity;

		for (int i = 2; i < n + 1; i++) {
			nextCity = closestInUnvisited(currentCity);
			Edge edge = graph.getEdge(currentCity.getName(), nextCity.getName());
			greedyRoute.addWeight(edge.getDistance());
			currentCity = nextCity;
			currentCity.setVisited(true);
			greedyRoute.addCityToPath(currentCity);
		}

		greedyRoute.addCityToPath(startCity);
		greedyRoute.addWeight(graph.getEdge(startCity.getName(), currentCity.getName()).getDistance());

		City[] greedyTourArray = new City[greedyRoute.path.size()];
		greedyTourArray = greedyRoute.path.toArray(greedyTourArray);
		this.tour = new String[greedyTourArray.length - 1];
		for (int i = 0; i < greedyTourArray.length - 1; i++) {
			tour[i] = greedyTourArray[i].getName();
		}
		this.cost = greedyRoute.totalWeight;

	}

	public City closestInUnvisited(City from) {
		double minDis = Integer.MAX_VALUE;
		City nearest = null;

		for (int i = 1; i < n + 1; i++) {
			if ((!(Integer.valueOf(from.getName()) == i)) && graph.getCity(i).isVisited() == false) {
				double dis = graph.getEdge(Integer.valueOf(from.getName()), i).getDistance();
				if (dis < minDis) {
					minDis = dis;
					nearest = graph.getCity(i);
				}
			}
		}
		return nearest;
	}
}
