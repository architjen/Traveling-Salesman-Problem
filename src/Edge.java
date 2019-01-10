/**
 * This class represents an Edge vetween two Cities
 * 
 */
public class Edge {
	City c1;
	City c2;
	double distance;
	public boolean isVisited;
	public double prob; // used by ant colony

	public Edge(City c1, City c2) {
		this.distance = dist(c1.getX(), c1.getY(), c2.getX(), c2.getY());

		double distFromOrigin1 = dist(c1.getX(), c1.getY(), 0, 0);
		double distFromOrigin2 = dist(c2.getX(), c2.getY(), 0, 0);

		if (distFromOrigin1 < distFromOrigin2) {
			this.c1 = c1;
			this.c2 = c2;
		} else {
			this.c2 = c1;
			this.c1 = c2;
		}
	}

	public double getProb() {
		return this.prob;
	}

	public double getDistance() {
		return this.distance;
	}

	public double dist(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
	}

	@Override
	public String toString() {
		return "Edge {" + c1.toString() + " TO " + c2.toString() + "} Distance: " + this.distance;
	}
}
