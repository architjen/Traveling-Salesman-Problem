import java.util.Arrays;

/**
 * This class is just a container for storing the results of each of the algorithms
 * 
 *
 */
public class TSPResult {
	public String algoName;
	public String[] tour;
	public double cost;
	public long runTimeInMS;
	
	public TSPResult(String algoName, String[] tour, double cost, long runTimeInML) {
		this.algoName = algoName;
		this.tour = tour;
		this.cost = cost;
		this.runTimeInMS = runTimeInML;
	}
	
	public String testingFriendlytoString() {
		String returnValue = this.algoName + ";" +	this.runTimeInMS + ";" + this.cost;
		return returnValue;
	}
	
	@Override
	public String toString() {
		String returnValue = "Name: " + this.algoName + "\n" + "Tour :" + Arrays.toString(tour) + "\n" + 
				"cost: " +  String.format( "%.2f", this.cost )  + " \n" + "Runtime: " + this.runTimeInMS + "\n";
		return returnValue;
	}
	
}
