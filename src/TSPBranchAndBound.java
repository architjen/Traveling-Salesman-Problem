import java.util.*;

public class TSPBranchAndBound implements ITSPAlgo{

	private double[][] m_DistanceMatrix;
	private int m_CitiesCount;
	private LinkedHashMap<String, Double> m_BestRouteWithCost = new LinkedHashMap<String, Double> ();

	private double m_RouteCost = 0;
	private String[] bestTour;
	private double minCost = Double.POSITIVE_INFINITY;
	private double m_PositiveInfinity = Double.POSITIVE_INFINITY;
	
	
	@Override
	public TSPResult runTSP(CityGraph graph) {
		long startTime = System.nanoTime();
		runBranchAndBound(graph);
		long endTime = System.nanoTime();
		long runTimeInMS = (endTime - startTime);  //divide by 1000000 to get millisecond
		
		TSPResult result = new TSPResult("Branch And Bound", bestTour, minCost, runTimeInMS);
		return result;
	}
	
	public void runBranchAndBound(CityGraph graph){
		m_CitiesCount = graph.getCities().size();
		m_DistanceMatrix = graph.getAdjMatrix();
				
		double m_ReduceCost;
		double m_TotalReduceCost = 0;

		for(int i = 0; i < m_CitiesCount; i++)
		{
			m_ReduceCost = m_DistanceMatrix [i][0];
			for(int j = 1; j < m_CitiesCount; j++)
			{
				if (m_DistanceMatrix [i][j] < m_ReduceCost) {
					m_ReduceCost = m_DistanceMatrix [i][j];
				}
			}

			if (m_ReduceCost == m_PositiveInfinity)
				m_ReduceCost = 0;

			for(int j = 0; j < m_CitiesCount; j++)
			{
				m_DistanceMatrix [i][j] = m_DistanceMatrix [i][j] - m_ReduceCost;
			}

			m_TotalReduceCost = m_TotalReduceCost + m_ReduceCost;
		}
		

		for(int j = 0; j < m_CitiesCount; j++)
		{
			m_ReduceCost = m_DistanceMatrix [0][j];
			for(int i = 1; i < m_CitiesCount; i++)
			{
				if (m_DistanceMatrix [i][j] < m_ReduceCost) {
					m_ReduceCost = m_DistanceMatrix [i][j];
				}
			}

			if (m_ReduceCost == m_PositiveInfinity)
				m_ReduceCost = 0;

			for(int i = 0; i < m_CitiesCount; i++)
			{
				m_DistanceMatrix [i][j] = m_DistanceMatrix [i][j] - m_ReduceCost;
			}

			m_TotalReduceCost = m_TotalReduceCost + m_ReduceCost;
		}
		
		m_BestRouteWithCost.put ("0", m_TotalReduceCost);
	
		
		while (m_BestRouteWithCost.size() < m_CitiesCount) {
			CalculateNextNode ();
		}
		
		
		
		for(String key: m_BestRouteWithCost.keySet()) {
			m_RouteCost = m_BestRouteWithCost.get(key+"");
		}


		String m_RouteIS = "";
		bestTour = new String[m_CitiesCount];
		
		for(String key: m_BestRouteWithCost.keySet()) {
			m_RouteIS = m_RouteIS + (Integer.parseInt(key) + 1) + " - ";
			bestTour[Integer.parseInt(key)] = Integer.toString(Integer.parseInt(key) + 1);
		}
		
		minCost = m_RouteCost;
		m_RouteIS = m_RouteIS +" 1";
//		System.out.println ("Route is : " + m_RouteIS);
//		System.out.println ("Route Cost is : " + m_RouteCost);
	}
	
	

	private void CalculateNextNode()
	{
		double m_MinimumReduceCost = Double.POSITIVE_INFINITY;
		int m_MinimumCityIndex = 0;
		double m_ReduceCost;
		double[][] m_CurrentMatrix= new double[m_CitiesCount][m_CitiesCount];
		double[][] m_MinimumMatrix = new double[m_CitiesCount][m_CitiesCount];
		double m_TotalReduceCost = 0;

		for (int c = 0; c < m_CitiesCount; c++) {
			if (m_BestRouteWithCost.containsKey (c+"")) {
				continue;
			}
				
			for (int i = 0; i < m_CitiesCount; i++) {
				for (int j = 0; j < m_CitiesCount; j++) {
					m_CurrentMatrix [i][j] = m_DistanceMatrix [i][j];
				}
			}
			
			m_TotalReduceCost = 0;

			int m_FirstCity = 0;
			int m_LastCity = 0;
			int m_Cnt = 0;
			
			for(String key: m_BestRouteWithCost.keySet()) {
				if (m_Cnt == 0) {
					m_FirstCity = Integer.parseInt(key);
					m_LastCity = Integer.parseInt(key);
				} else {
					m_LastCity = Integer.parseInt(key);
				}
				m_Cnt = m_Cnt + 1;
			}

			for (int i = 0; i < m_CitiesCount; i++) {
				if (i == m_LastCity) {
					for (int j = 0; j < m_CitiesCount; j++) {
						m_CurrentMatrix [i][j] = m_PositiveInfinity;
					}
					break;
				}
			}

			for (int j = 0; j < m_CitiesCount; j++) {
				if (j == c) {
					for (int i = 0; i < m_CitiesCount; i++) {
						m_CurrentMatrix [i][j] = m_PositiveInfinity;
					}
					break;
				}
			}
			
			m_CurrentMatrix [c][m_LastCity] = m_PositiveInfinity;
			m_CurrentMatrix [c][m_FirstCity] = m_PositiveInfinity;
		
			// Matrix Row reducing
			for (int i = 0; i < m_CitiesCount; i++) {
				m_ReduceCost = m_CurrentMatrix [i][0];

				for (int j = 1; j < m_CitiesCount; j++) { // Find minimum value in Row
					if (m_CurrentMatrix [i][j] < m_ReduceCost) {
						m_ReduceCost = m_CurrentMatrix [i][j];
					}
				}

				if (m_ReduceCost == m_PositiveInfinity)
					m_ReduceCost = 0;

				for (int j = 0; j < m_CitiesCount; j++) {
					m_CurrentMatrix [i][j] = m_CurrentMatrix [i][j] - m_ReduceCost; // reduce the row of the matrix from that found minimum value
				}

				m_TotalReduceCost = m_TotalReduceCost + m_ReduceCost;
			}
		

			// Matrix Coloumn reducing
			for (int j = 0; j < m_CitiesCount; j++) {
				m_ReduceCost = m_CurrentMatrix [0][j];
				for (int i = 1; i < m_CitiesCount; i++) { // Find minimum value in Coloumn
					if (m_CurrentMatrix [i][j] < m_ReduceCost) {
						m_ReduceCost = m_CurrentMatrix [i][j];
					}
				}

				if (m_ReduceCost == m_PositiveInfinity)
					m_ReduceCost = 0;

				for (int i = 0; i < m_CitiesCount; i++) {
					m_CurrentMatrix [i][j] = m_CurrentMatrix [i][j] - m_ReduceCost; // reduce the coloumn of the matrix from that found minimum value
				}

				m_TotalReduceCost = m_TotalReduceCost + m_ReduceCost;

			}
			
			m_TotalReduceCost = m_DistanceMatrix [m_LastCity][c] + m_BestRouteWithCost.get(m_LastCity+"")  + m_TotalReduceCost;
			if (m_TotalReduceCost < m_MinimumReduceCost) {
				m_MinimumReduceCost = m_TotalReduceCost;
				m_MinimumCityIndex = c;
				for (int i = 0; i < m_CitiesCount; i++) {
					for (int j = 0; j < m_CitiesCount; j++) {
						m_MinimumMatrix [i][j] = m_CurrentMatrix [i][j];
					}
				}
			}
		}

		for (int i = 0; i < m_CitiesCount; i++) {
			for (int j = 0; j < m_CitiesCount; j++) {
				m_DistanceMatrix [i][j] = m_MinimumMatrix [i][j];
			}
		}
		m_BestRouteWithCost.put (m_MinimumCityIndex+"", m_MinimumReduceCost);
	}


	
	
	
	
}
