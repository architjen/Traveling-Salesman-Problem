import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Randomized implements ITSPAlgo {

	@Override
	public TSPResult runTSP(CityGraph graph) {
		long startTime = System.nanoTime();

		Random rand = new Random();
		List<City> cities = graph.getCities();
		int numberOfCities = cities.size();
		int random1, random2;
		City lastAddedCity = null;
		ArrayList<String> tour = new ArrayList<String>();
		while (tour.size() < numberOfCities) {
			random1 = rand.nextInt(cities.size());
			if (tour.size() == 0) {
				lastAddedCity = cities.get(random1);
			} else {
				random2 = rand.nextInt(cities.size());
				City randC1 = cities.get(random1);
				City randC2 = cities.get(random2);
				if (graph.getEdge(lastAddedCity.getName(), randC1.getName()).distance < graph
						.getEdge(lastAddedCity.getName(), randC2.getName()).distance) {

					lastAddedCity = randC1;

				} else {
					lastAddedCity = randC2;
				}

			}
			tour.add(lastAddedCity.getName());
			cities.remove(lastAddedCity);
		}
		String[] tourArray = new String[tour.size()];
		tourArray = tour.toArray(tourArray);
		double cost = graph.computeTourCost(tourArray);

		long endTime = System.nanoTime();
		long runTimeInNS = (endTime - startTime);
		TSPResult result = new TSPResult("Randomized", tourArray, cost, runTimeInNS);

		return result;
	}

}
