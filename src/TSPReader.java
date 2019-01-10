import java.io.*;
import java.util.*;
import java.util.regex.*;

public class TSPReader {

	public TSPReader() {
	}

	public static CityGraph readFile(String path) throws IOException {
		File file = new File(path);

		BufferedReader br = new BufferedReader(new FileReader(file));

		String st;
		StringBuilder fullFileAsString = new StringBuilder();
		while ((st = br.readLine()) != null) {
			fullFileAsString.append(st + "\n");
		}

		String myRegex = "(?m)^.*NAME.*:\\s*(.*)\\s*$";
		Matcher m = Pattern.compile(myRegex).matcher(fullFileAsString);
		String name = "";
		while (m.find()) {
			name = m.group(1);
		}
		myRegex = "(?m)^\\s*(\\d+)\\s*(-?\\d+[\\.\\d+]*[e\\+\\d*]*)\\s*(-?\\d+[\\.\\d+]*[e\\+\\d*]*)\\s*$";

		m = Pattern.compile(myRegex).matcher(fullFileAsString);

		List<String> nodeNumber = new ArrayList<String>();
		List<Double> coordsX = new ArrayList<Double>();
		List<Double> coordsY = new ArrayList<Double>();

		CityGraph graph = new CityGraph();
		graph.setGraphName(name);

		while (m.find()) {
			String cityName = m.group(1);
			nodeNumber.add(cityName);
			double x = Double.parseDouble(m.group(2));
			coordsX.add(x);
			double y = Double.parseDouble(m.group(3));
			coordsY.add(y);
			graph.addCity(x, y, cityName);
		}

		try {
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return graph;

	}

}
