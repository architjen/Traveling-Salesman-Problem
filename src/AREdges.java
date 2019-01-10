import java.text.DecimalFormat;
import java.util.Stack;

public class AREdges implements ITSPAlgo {
	CityGraph graph;
	double[][] sM;
	int n;
	AdjNode globalBestNode;
	double ub = Integer.MAX_VALUE;
	Stack<AdjNode> stackbnb = new Stack<AdjNode>();

	@Override
	public TSPResult runTSP(CityGraph graph) {
		this.graph = graph;
		long startTime = System.nanoTime();
		String[] tour = runARE();
		long endTime = System.nanoTime();
		long runTimeInMS = (endTime - startTime); // divide by 1000000 to get millisecond

		TSPResult result = new TSPResult("Branch and Bound with Edges Approach", tour, globalBestNode.getLeafCost(),
				runTimeInMS);
		return result;
	}

	public String[] runARE() {
		this.sM = graph.getAdjMatrix();
		this.n = sM.length;

		for (int i = 0; i < n; i++) {
			sM[i][i] = -1;
		}
		AdjNode start = new AdjNode(null, 0, sM);

		stackbnb.push(start);
		while (!stackbnb.isEmpty()) {
			AdjNode node = stackbnb.pop();

			if (node.isLeaf()) { // if it is a leaf
				node.leafCost();
				if (node.realTourLeaf()) {
					if (node.getLeafCost() < ub) {
						globalBestNode = node;
						ub = node.getLeafCost();
					}
				}
			} else { // if it is not a leaf

				// #right child
				double[][] rightM = node.makeRightChildM(node.getI(), node.getJ());
				AdjNode right = new AdjNode(node, 1, rightM);

				if (right.tourable() && right.lb < ub) {
					stackbnb.push(right);
				}

				// #left child
				double[][] leftM = node.makeLeftChildM(node.getI(), node.getJ());
				AdjNode left = new AdjNode(node, 2, leftM);
				
				if (left.tourable() && left.lb < ub) {
					stackbnb.push(left);
				}
			}
		}

		String[] tour = globalBestNode.bestNodeToTour();
		return tour;
	}

	public void printM(double[][] matrix) {
		System.out.println("===========");
		DecimalFormat df2 = new DecimalFormat(".##");
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				if (matrix[i][j] == -1) {
					System.out.print("-     ");
				} else {
					System.out.print(df2.format(matrix[i][j]) + "     ");
				}
			}
			System.out.println();
		}
		System.out.println("============");
	}

}
