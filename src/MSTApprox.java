import java.util.*;

public class MSTApprox implements ITSPAlgo {
	private CityGraph graph;
	private String[] bestTour;
	double cost;

	public MSTApprox() {
	}

	@Override
	public TSPResult runTSP(CityGraph graph) {
		this.graph = graph;
		long startTime = System.nanoTime();
		runMSTApprox();
		long endTime = System.nanoTime();
		long runTimeInMS = (endTime - startTime); 

		TSPResult result = new TSPResult("MST Approximation", this.bestTour, cost, runTimeInMS);
		return result;
	}

	public void runMSTApprox() {
		HashMap<String, List<String>> parents = new HashMap<String, List<String>>();
		List<Edge> allEdges = graph.getAllEdgesList();
		if (allEdges.isEmpty()) {
			return;
		}
		allEdges.sort(Comparator.comparing(Edge::getDistance));
		List<Edge> mstEdges = new ArrayList<Edge>();

		while (mstEdges.size() < graph.getCities().size() - 1) {
			// boolean hasTwoEdges = false;
			boolean createsCycle = false;

			Edge e = allEdges.remove(0);
			if (!parents.containsKey(e.c1.getName())) {

				parents.put(e.c1.getName(), new ArrayList<String>());

			}
			if (!parents.containsKey(e.c2.getName())) {

				parents.put(e.c2.getName(), new ArrayList<String>());

			}
			if (hasParent(null, e.c1.getName(), e.c2.getName(), parents)
					|| hasParent(null, e.c2.getName(), e.c1.getName(), parents)) {
				createsCycle = true;
			}

			if (!createsCycle) {
				parents.get(e.c1.getName()).add(e.c2.getName());
				parents.get(e.c2.getName()).add(e.c1.getName());
				mstEdges.add(e);
			}

		}
		String firstCity = mstEdges.get(0).c1.getName();
		List<String> tour = new ArrayList<String>();
		generateTour(null, firstCity, parents, tour);
		this.bestTour = new String[tour.size()];
		this.bestTour = tour.toArray(bestTour);
		cost = graph.computeTourCost(this.bestTour);
	}

	
	private void generateTour(String fromCity, String cityName, HashMap<String, List<String>> parents,
			List<String> tour) {
		if (parents == null || !parents.containsKey(cityName)) {
			return;
		}
		if (tour == null) {
			tour = new ArrayList<String>();
		}

		tour.add(cityName);
		List<String> parentList = parents.get(cityName);

		for (String parent : parentList) {
			if (parent.equals(fromCity)) {
				continue;
			} else {
				generateTour(cityName, parent, parents, tour);
			}
		}

	}
	
	//This method helps get the Minimum Spanning Tree by keeping track of the related nodes in a hashmap
	private boolean hasParent(String fromCity, String cityName, String target, HashMap<String, List<String>> parents) {
		if (parents == null || !parents.containsKey(cityName)) {
			return false;
		}
		List<String> parentList = parents.get(cityName);

		for (String parent : parentList) {
			if (parent.equals(fromCity)) {
				continue;
			}
			if (parent.equals(target)) {
				return true;
			} else {
				if (hasParent(cityName, parent, target, parents)) {
					return true;
				}
			}
		}
		return false;
	}

}
