import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class represents an adjecenty table in a higher level
 * It also has some useful methods that different algorithms of TSP might need
 * @author Daniel Ozuna
 *
 */
public class CityGraph {
	private HashMap<String, City> cities;
	private HashMap<String, Edge> edges;
	private String graphName = "";
	private double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE, maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE;
	public String algoName = ""; // name shown in the GUI window
	public String guiCost = ""; // Cost shown in the GUI window
	public ArrayList<Edge> edgesToPrint; // Edges to print in the GUI

	public CityGraph() {
		this.cities = new HashMap<>();
		this.edges = new HashMap<>();
	}

	public HashMap<String, City> getCityMap() {
		return this.cities;
	}

	public List<City> getCities() {
		List<City> list = new ArrayList<City>(this.cities.values());
		return list;
	}

	public void addCity(City c) {
		if (this.cities.containsKey(c.getName())) {
			return;
		}

		updateMinMax(c);
		// Add edges to both cities
		for (String key : this.cities.keySet()) {
			City temp = this.cities.get(key);
			Edge e = new Edge(c, temp);
			temp.addEdge(e);
			c.addEdge(e);
			this.edges.put(c.getName() + ":" + key, e);
		}
		this.cities.put(c.getName(), c);
	}

	public void setEdgesFromTSPResult(TSPResult result) {
		this.edgesToPrint = new ArrayList<Edge>();
		for (int i = 0; i < result.tour.length - 1; i++) {
			this.edgesToPrint.add(this.getEdge(result.tour[i], result.tour[i + 1]));
		}
		this.edgesToPrint.add(this.getEdge(result.tour[0], result.tour[result.tour.length - 1]));
		this.algoName = result.algoName;
		this.guiCost = Double.toString(Math.round(result.cost));
	}

	public Edge[] getAllEdges() {
		Edge[] edges = new Edge[this.edges.size()];
		this.edges.values().toArray(edges);
		return edges;
	}

	public List<Edge> getAllEdgesList() {
		List<Edge> edges = new ArrayList<Edge>(this.edges.values());
		return edges;
	}

	public double[][] getAdjMatrix() {
		int numOfCities = this.cities.size();
		City[] arrayCities = new City[numOfCities];
		arrayCities = this.cities.values().toArray(arrayCities);
		double[][] adjMatrix = new double[numOfCities][numOfCities];
		Edge temp;
		for (int i = 0; i < numOfCities; i++) {
			for (int j = 0; j < numOfCities; j++) {
				if (i == j) {
					adjMatrix[i][j] = Double.POSITIVE_INFINITY;
				} else {

					temp = getEdge(arrayCities[i].getName(), arrayCities[j].getName());
					if (temp == null) {
						adjMatrix[i][j] = Double.POSITIVE_INFINITY;
					} else {
						adjMatrix[i][j] = temp.distance;
					}

				}

			}
		}

		return adjMatrix;
	}

	public void updateMinMax(City c) {
		double x = c.getX();
		double y = c.getY();

		if (x < this.minX)
			this.minX = x;
		if (y < this.minY)
			this.minY = y;
		if (x > this.maxX)
			this.maxX = x;
		if (y > this.maxY)
			this.maxY = y;

	}

	public Edge getEdge(int c1, int c2) {
		return getEdge(Integer.toString(c1), Integer.toString(c2));
	}

	public Edge getEdge(String c1, String c2) {
		if (!this.edges.containsKey(c1 + ":" + c2)) {
			if (!this.edges.containsKey(c2 + ":" + c1)) {
				return null;
			} else {
				return this.edges.get(c2 + ":" + c1);
			}
		} else {
			return this.edges.get(c1 + ":" + c2);
		}
	}

	public Edge removeEdge(int c1, int c2) {
		return removeEdge(Integer.toString(c1), Integer.toString(c2));
	}

	public double computeTourCost(String[] tour) {
		double cost = 0;
		int i;
		for (i = 0; i < tour.length - 1; i++) {
			cost += getEdge(tour[i], tour[i + 1]).distance;
		}
		// first and last
		cost += getEdge(tour[i], tour[0]).distance;
		return cost;
	}

	public void restoreVisited() {
		for (City c : getCities()) {
			c.setVisited(false);
		}
	}

	public Edge removeEdge(String c1, String c2) {
		Edge e = null;
		if (!this.edges.containsKey(c1 + ":" + c2)) {
			if (!this.edges.containsKey(c2 + ":" + c1)) {
				return null;
			} else {

				e = this.edges.remove(c2 + ":" + c1);
				e.c1.removeEdge(e);
				e.c2.removeEdge(e);
				return e;
			}
		} else {
			e = this.edges.remove(c1 + ":" + c2);
			e.c1.removeEdge(e);
			e.c2.removeEdge(e);
			return e;
		}
	}

	public Edge getMinimumUnvisitedEdge() {
		Edge minEdge = null;
		double minDistance = Double.POSITIVE_INFINITY;
		for (Edge e : this.edges.values()) {
			if (e.distance < minDistance && !e.isVisited && (!e.c1.isVisited() && !e.c2.isVisited())) {
				minEdge = e;
				minDistance = e.distance;
				// System.out.println(e);
			}
		}

		return minEdge;
	}

	public void addCity(double x, double y, String cityName) {
		City c = new City(x, y, cityName);
		addCity(c);
	}

	public void removeCity(City c) {
		this.cities.remove(c.getName());
	}

	public void removeCityByName(String cityName) {
		this.cities.remove(cityName);
	}

	public City getCity(String cityName) {
		return this.cities.get(cityName);
	}

	public City getCity(int cityName) {
		return this.getCity(Integer.toString(cityName));
	}

	public City getClosestCityFrom(String cityName) {
		return null;
	}

	public boolean isVisited(String cityName) {
		City c = this.getCity(cityName);
		if (c != null) {
			return c.isVisited();
		} else {
			return false;
		}
	}

	public boolean isVisited(int cityNumber) {
		return isVisited(String.valueOf(cityNumber));
	}

	public String getGraphName() {
		return graphName;
	}

	public void setGraphName(String graphName) {
		this.graphName = graphName;
	}

	public double getMinX() {
		return minX;
	}

	public void setMinX(double minX) {
		this.minX = minX;
	}

	public double getMinY() {
		return minY;
	}

	public void setMinY(double minY) {
		this.minY = minY;
	}

	public double getMaxX() {
		return maxX;
	}

	public void setMaxX(double maxX) {
		this.maxX = maxX;
	}

	public double getMaxY() {
		return maxY;
	}

	public void setMaxY(double maxY) {
		this.maxY = maxY;
	}

}
