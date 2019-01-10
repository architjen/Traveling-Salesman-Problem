import java.util.ArrayList;
import java.util.HashMap;

public class AdjNode {
	double[][] M;
	double[][] reducedM;
	int[][] decidedEdges; // 0 = undecided \ 1 = taken \ 2 = not taken
	AdjNode parent;
	int level;
	double lb, leafCost;
	int i, j, n;
	int rightOrLeft; // right = 1 left = 2
	HashMap<Integer, Integer> selectedEdges = new HashMap<Integer, Integer>();

	public AdjNode(AdjNode parent, int rightOrLeft, double[][] M) {
		this.parent = parent;
		this.M = M;
		this.n = M.length;
		this.lb = generateMinCost();
		this.rightOrLeft = rightOrLeft;
		if (rightOrLeft > 0) {
			this.decidedEdges = generateDecidedEdges();
		} else {
			decidedEdges = new int[n][n];
		}
		nextIJ();
	}

	public int[][] generateDecidedEdges() {
		int[][] de = m2mInt(parent.getDecidedEdges());
		if (rightOrLeft == 1) {
			de[parent.getI()][parent.getJ()] = 2;
		}
		if (rightOrLeft == 2) {
			de[parent.getI()][parent.getJ()] = 1;
		}
		return de;
	}

	public void nextIJ() {
		int ii = 0, jj = 0;
		double minValue = Integer.MAX_VALUE;
		for (int k = 0; k < n; k++) {
			for (int p = 0; p < n; p++) {
				if (M[k][p] < minValue && M[k][p] > -1) {
					if (decidedEdges[k][p] == 0) {
						minValue = M[k][p];
						ii = k;
						jj = p;
					}
				}
			}
		}
		this.i = ii;
		this.j = jj;
	}

	public double generateMinCost() {
		double[][] M2 = m2mDouble(M);
		double minCost = 0;
		// row reduction
		for (int k = 0; k < n; k++) {

			double minRow = findRowMin(k, M2);
			for (int p = 0; p < n; p++) {
				if (M2[k][p] > -1) {
					M2[k][p] = M2[k][p] - minRow;
				}
			}
			minCost = minCost + minRow;
		}

		// column reduction
		double[][] M3 = m2mDouble(M2);

		for (int k = 0; k < n; k++) {
			double minCol = findColMin(k, M3);
			for (int p = 0; p < n; p++) {
				if (M3[p][k] > -1) {
					M3[p][k] = M3[p][k] - minCol;
				}
			}
			minCost = minCost + minCol;
		}
		this.reducedM = M3;// *
		return minCost;
	}

	public double findRowMin(int row, double[][] M2) {
		double min = Integer.MAX_VALUE;
		int positiveCounter = 0;
		for (int i = 0; i < n; i++) {
			if (M2[row][i] >= 0) {
				positiveCounter++;
				if (M2[row][i] < min) {
					min = M2[row][i];
				}
			}
		}
		if (positiveCounter < 2) { // at least two positive instance
			min = 0;
		}
		return min;
	}

	public double findColMin(int col, double[][] M3) {
		double min = Integer.MAX_VALUE;
		int positiveCounter = 0;
		for (int i = 0; i < n; i++) {
			if (M3[i][col] >= 0) {
				positiveCounter++;
				if (M3[i][col] < min) {
					min = M3[i][col];
				}
			}
		}
		if (positiveCounter < 2) { // at least two positive instance
			min = 0;
		}
		return min;
	}

	public boolean tourable() { // at least one non -1 entry in each row and column
		int c;
		for (int k = 0; k < n; k++) { // rows
			c = 0;
			for (int p = 0; p < n; p++) {
				if (M[k][p] == -1) {
					c++;
				}
			}
			if (c == n) {
				return false;
			}
		}

		for (int k = 0; k < n; k++) { // columns
			c = 0;
			for (int p = 0; p < n; p++) {
				if (M[p][k] == -1) {
					c++;
				}
			}
			if (c == n) {
				return false;
			}
		}
		return true;
	}

	public boolean isLeaf() {
		int c, d = 0;
		for (int k = 0; k < n; k++) { // rows
			c = 0;
			for (int p = 0; p < n; p++) {
				if (M[k][p] == -1) {
					c++;
				}
			}
			if (c == (n - 1)) {
				d++;
			}
		}
		for (int k = 0; k < n; k++) { // columns
			c = 0;
			for (int p = 0; p < n; p++) {
				if (M[p][k] == -1) {
					c++;
				}
			}
			if (c == (n - 1)) {
				d++;
			}
		}

		if (d == 2 * n) {
			return true;
		} else {
			return false;
		}
	}

	public double leafCost() { // if the node is a leaf
		double cost = 0;
		for (int k = 0; k < n; k++) { // rows
			for (int p = 0; p < n; p++) {
				if (!(M[k][p] == -1)) {
					cost = cost + M[k][p];
				}
			}
		}
		this.leafCost = cost;
		return cost;
	}

	public boolean realTourLeaf() { // feasible
		ArrayList<Integer> visited = findTour();
		if (visited.size() == n) {
			return true;
		}
		return false;
	}

	public ArrayList<Integer> findTour() {
		ArrayList<Integer> visited = new ArrayList<Integer>();
		for (int k = 0; k < n; k++) { // rows
			for (int p = 0; p < n; p++) {
				if (M[k][p] > -1) {
					selectedEdges.put(k, p);
				}
			}
		}

		int from = 0;
		int to;
		while (!visited.contains(0)) { // at least one cycle
			to = selectedEdges.get(from);
			visited.add(to);
			from = to;
		}
		return visited;
	}

	public double[][] makeRightChildM(int i2, int j2) {
		double[][] right = m2mDouble(M);
		right[i2][j2] = -1;
		return right;
	}

	public double[][] makeLeftChildM(int i2, int j2) {
		double[][] left = m2mDouble(M);
		for (int k = 0; k < n; k++) { // rows
			for (int p = 0; p < n; p++) {
				if (k == i2 && !(p == j2)) {
					left[k][p] = -1;
				}
				if (!(k == i2) && p == j2) {
					left[k][p] = -1;
				}
				if (k == j2 && p == i2) {
					left[k][p] = -1;
				}
			}
		}
		return left;
	}

	public String[] bestNodeToTour() {
		String[] sTour = new String[n];
		ArrayList<Integer> tour = findTour();
		for (int k = 0; k < tour.size(); k++) {
			sTour[k] = Integer.toString(tour.get(k));
		}

		return sTour;
	}

	// getters and setters
	public double[][] getM() {
		return M;
	}

	public void setM(double[][] m) {
		M = m;
	}

	public AdjNode getParent() {
		return parent;
	}

	public void setParent(AdjNode parent) {
		this.parent = parent;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getI() {
		return i;
	}

	public int getJ() {
		return j;
	}

	public double getLeafCost() {
		return leafCost;
	}

	public int[][] getDecidedEdges() {
		return decidedEdges;
	}

	public int[] levelToEdge(int l, int n) { // input
		int[] inj = new int[2];
		inj[1] = l % n;
		inj[0] = (l - inj[1]) / n;
		return inj;
	}

	public double[][] m2mDouble(double[][] m1) {
		int n = m1.length;
		double[][] m2 = new double[n][n];
		for (int k = 0; k < n; k++) {
			for (int p = 0; p < n; p++) {
				m2[k][p] = m1[k][p];
			}
		}

		return m2;
	}

	public int[][] m2mInt(int[][] m1) {
		int n = m1.length;
		int[][] m2 = new int[n][n];
		for (int k = 0; k < n; k++) {
			for (int p = 0; p < n; p++) {
				m2[k][p] = m1[k][p];
			}
		}

		return m2;
	}
}
