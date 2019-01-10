import java.util.ArrayList;

public class City {
		private double x, y;
		private String name;
		private boolean visited = false;  //default value
		private ArrayList<Edge> edges;

		public City(double x, double y, String name) {
			this.x = x;
			this.y = y;
			this.setName(name);
			this.edges = new ArrayList<Edge>();
		}

		public boolean isVisited() {
			return this.visited;
		}

		public void setVisited(boolean visited) {
			this.visited = visited;
		}

		public String getName() {
			return name;
		}
		
		public double getX() {
			return this.x;
		}
		
		public double getY() {
			return this.y;
		}
		

		public void setName(String name) {
			this.name = name;
		}
		
		public void addEdge(Edge e) {
			this.edges.add(e);
		}
		
		public void removeEdge(Edge e) {
			this.edges.remove(e);
		}
		
		public ArrayList<Edge> getEdges() {
			return this.edges;
		}
		
		public String toString() {
			return String.format("%s: (%.2f, %.2f)", this.name,this.x, this.y);
		} 

	}