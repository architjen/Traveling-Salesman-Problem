import java.util.*;

/*
 * Ant colony simulates a group of ants 
 */
public class AntColonyTSP implements ITSPAlgo {
	private AntColGraph graph;
	private String[] bestTour;
	double minCost = Integer.MAX_VALUE;
	
	//We can twik the number of ants alpha and beta as they are hyper parameters. 
	//This seems to work for graphs of about 100-200 cities

	int alpha = 1;
	int beta = 5;
	int ANTS;
	public AntColonyTSP(int ants) {
		this.ANTS = ants;
	}

	public class AntColGraph {
		private CityGraph graph;
		HashMap<Edge, Double> pheromones;
		HashMap<Edge, Double> prob;

		public AntColGraph(CityGraph graph) {
			this.graph = graph;
			initPheromones();
			// Create edges initialize pheromones etc
		}

		public void initPheromones() {
			this.pheromones = new HashMap<Edge, Double>();
			for (Edge e : this.graph.getAllEdgesList()) {
				//initially, all the pheromones are initialized to 1/distance
				this.pheromones.put(e, 1.0 / e.distance);
			}
		}

	}

	@Override
	public TSPResult runTSP(CityGraph graph) {
		graph.restoreVisited();
		this.graph = new AntColGraph(graph);
		long startTime = System.nanoTime();
		runAntColony();
		long endTime = System.nanoTime();
		long runTimeInNS = (endTime - startTime); // divide by 1000000 to get millisecond
		TSPResult result = new TSPResult("Ant Colony", this.bestTour, minCost, runTimeInNS);
		return result;
	}

	public void runAntColony() {
		List<City> bestTourSoFar = null;

		for (int ant = 0; ant < ANTS; ant++) {

			// select random City
			List<City> unvisitedCities = this.graph.graph.getCities();
			int random = -1;
			Random rand = new Random();
			City lastVisited = null;
			random = rand.nextInt(unvisitedCities.size());
			lastVisited = unvisitedCities.get(random);
			ArrayList<City> tour = new ArrayList<City>();
			double tourCost = 0;

			while (tour.size() < this.graph.graph.getCities().size() - 1) {
				lastVisited.setVisited(true);

				unvisitedCities.remove(lastVisited);
				tour.add(lastVisited);
				ArrayList<Edge> allEdges = lastVisited.getEdges();
				ArrayList<Edge> edges = new ArrayList<Edge>();

				for (Edge e : allEdges) {
					if (e.c1.isVisited() && e.c2.isVisited()) {
						continue;
					}
					edges.add(e);
				}

				double numerator = 0;
				double denom = 0;
				for (Edge e : edges) {
					denom += Math.pow(this.graph.pheromones.get(e), alpha) * Math.pow((1.0 / e.distance), beta);
				}
				int unvisCount = 0;
				for (Edge e : edges) {
					numerator = (Math.pow(this.graph.pheromones.get(e), alpha) * Math.pow((1.0 / e.distance), beta));
					e.prob = numerator / denom;
					unvisCount++;
				}
				double[] rouletteWheel = new double[unvisCount];
				edges.sort(Comparator.comparing(Edge::getProb));

				double acum = 0;
				for (int j = 0; j < edges.size(); j++) {
					rouletteWheel[j] = acum + edges.get(j).prob;
					acum += edges.get(j).prob;
				}
				double random2 = rand.nextDouble();
				int indexOfNextCity = 0;
				while (random2 > rouletteWheel[indexOfNextCity]) {
					indexOfNextCity++;
				}
				Edge nextEdge = edges.get(indexOfNextCity);
				lastVisited = !nextEdge.c1.isVisited() ? nextEdge.c1 : nextEdge.c2;
				tourCost += nextEdge.distance;
			}
			tour.add(lastVisited);
			tourCost += this.graph.graph.getEdge(tour.get(0).getName(), lastVisited.getName()).distance;

			if (tourCost < minCost) {
				minCost = tourCost;
				bestTourSoFar = tour;
			}

			addPheromoneToPath(tour, tourCost);
			this.graph.graph.restoreVisited();
		}

		this.bestTour = new String[bestTourSoFar.size()];
		for (int i = 0; i < this.bestTour.length; i++) {
			this.bestTour[i] = bestTourSoFar.get(i).getName();
		}

	}

	public void addPheromoneToPath(ArrayList<City> tour, double tourCost) {
		for (int i = 0; i < tour.size() - 1; i++) {
			City c1 = tour.get(i);
			City c2 = tour.get(i + 1);
			Edge e = this.graph.graph.getEdge(c1.getName(), c2.getName());
			this.graph.pheromones.put(e, this.graph.pheromones.get(e) + (1 / tourCost));
		}
	}

}
