/**
 * This class helps as the User interface through a command prompt window
 * 
 */
public class TSPInterface {
	public static int MAX_ALGO_INDEX = 8;
	public static int MAX_PARAM_NUM = 4;
	public static void main(String[] args) {
		String help = "Please enter the location of the graph file, then the filename and followed by the number of algorithm to run\n"+""
				+ " TSP <file location> <file name> <Algorithm Number> +\n" +
				"	 Algorithm Numbers:\n "+ 
				"	 * 0: Brute Force\r\n" + 
				"	 * 1: MST Aprox\r\n" + 
				"	 * 2: Dynamic Programming Approach\r\n" + 
				"	 * 3: Greedy Approach\r\n" + 
				"	 * 4: Randomized\r\n" + 
				"	 * 5: Ant Colony\r\n" +
				"	 * 6: Branch And Bound\r\n" + 
				"	 * 7: Branch And Bound Adding and Removing Edges\r\n";
		if (args == null || args.length < MAX_PARAM_NUM - 1) {
			System.out.println(help);	
			return;
		}
		
		String loc = args[0];
		String fileName = args[1];
		String algoNum = args[2];
		String hyperPar = null;
		
		int hyperParam = 0;
		if (args.length == 4)
		{
			hyperPar = args[3];
			hyperParam = Integer.parseInt(hyperPar);
		}
		int algoNumber = 0;
		
		try {
			CityGraph graph = TSPReader.readFile(loc + "\\" + fileName);
			if (algoNumber < 0 || algoNumber > MAX_ALGO_INDEX) {
				System.out.println(help);	
				return;
			}
			
			algoNumber = Integer.parseInt(algoNum);
			ITSPAlgo algorithm = null;
			
			//Ant colony uses an optional hyper parameter, so allow that optional parameter
			if (args.length == 4) {
				algorithm= AlgoFactory.getImplementation(algoNumber, hyperParam);
			} else {
				algorithm = AlgoFactory.getImplementation(algoNumber);
			}
			
			TSPResult result = algorithm.runTSP(graph);
			System.out.println(result);
			graph.setEdgesFromTSPResult(result);
			new GUI_TSP(graph);	//this line just creates a visualization that the user can close
			
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(help);
			return;
		}	
	}
}
