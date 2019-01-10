
import java.util.ArrayList;

public class Route {
	
	CityGraph graph ;
	ArrayList<City> path = new ArrayList<City>();
	double totalWeight = 0 ;

	public Route(CityGraph graph) {
		this.graph = graph;
	}
	
	public ArrayList<City> getPath() {
		return path;
	}
	
	public void addCityToPath(City nextCity) {
		path.add(nextCity);
	}
	
	public void addWeight(double edgeWeight) {
		totalWeight = totalWeight + edgeWeight;
	}
	
	public String toString() {
		String st ="";
		for(int i=0; i<this.path.size() ;i++)  {
			st = st + (i+1) + " :  "+this.path.get(i).getName()+"\n";
		}
		st = st+"\n"+"Total distance: "+this.totalWeight;
		return st;
	} 

}
