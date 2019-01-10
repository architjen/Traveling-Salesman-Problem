public class AlgoFactory {
	/** This class creates an instance of an algorithm (ITSPAlgo) 
	 * corresponding to an index from 0 to 7
	 * 
	 * @param algoNumber
	 * 0: Brute Force
	 * 1: MST approximation
	 * 2: Dynamic Programming Approach
	 * 3: Greedy Approach
	 * 4: Randomized
	 * 5: Ant Colony
	 * 6: Branch And Bound
	 * 7: Branch And Bound Adding and Removing Edges
	 * @return an instance of the TSP algorithm 
	 */	
	public static int HYPER_PARAM = 10;
//	public static int algoNumber = -1;
	public static ITSPAlgo getImplementation(int algoNumber) {
		ITSPAlgo instance = null;
		switch(algoNumber) {
		case 0:
			instance = new TSPBruteForce();
			break;
		case 1:
			instance = new MSTApprox();
			break;
		case 2:
			instance = new TSPDynamicRecursive();
			break;
		case 3:
			instance = new Greedy();
			break;
		case 4:
			instance = new Randomized();
			break;	
		case 5:
			instance = new AntColonyTSP(HYPER_PARAM);
			break;	
		case 6:
			instance = new TSPBranchAndBound();
			break;	
		case 7:
			instance = new AREdges();
			break;
		default:
			System.out.println("Unknonw Index.");
			return null;
		}
		return instance;
	}
	
	public static ITSPAlgo getImplementation(int algoNumber, int hyperParam) {
		HYPER_PARAM = hyperParam;
		return getImplementation(algoNumber);
	}

}
