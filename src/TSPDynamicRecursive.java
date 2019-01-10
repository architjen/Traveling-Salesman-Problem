import java.util.*;

public class TSPDynamicRecursive implements ITSPAlgo{

	private int m_VisitedAllCitiesState;
	private int m_CitiesCount;
	private double[][] m_DistanceMatrix;
	private String[] bestTour;
	double minCost = 999999999;
	private CityGraph graph;
	private double[][] m_DPTableOfAllMaskValues;
	
	private List<Integer> m_BestTourIndexes = new ArrayList<>();
	
	
	@Override
	public TSPResult runTSP(CityGraph graph) {
		this.graph = graph;
		long startTime = System.nanoTime();
		runDynamicProgrammingAlgo();
		long endTime = System.nanoTime();
		long runTimeInMS = (endTime - startTime);  //divide by 1000000 to get millisecond
		TSPResult result = new TSPResult("Dynamic Programming", bestTour, minCost, runTimeInMS);
		return result;	   
	}
	
	private void runDynamicProgrammingAlgo() {
		m_DistanceMatrix = graph.getAdjMatrix();
		m_CitiesCount = graph.getCities().size();
		m_VisitedAllCitiesState = (int)Math.pow(2, m_CitiesCount) - 1;
		
		// Initialize the m_DPTableOfAllMaskValues array
		m_DPTableOfAllMaskValues = new double[(int)Math.pow(2, m_CitiesCount)][m_CitiesCount];
	
	    for(int i = 0; i < ((int)Math.pow(2, m_CitiesCount)); i++)
	    {
	        for(int j = 0; j < m_CitiesCount;j++)
	        {
	        		m_DPTableOfAllMaskValues[i][j] = -1;
	        }
	    }
	
	    m_BestTourIndexes.add(0); 
	    
	    minCost = CalculateTSPForRemainingCities(1, 0);
	    m_BestTourIndexes.add(0);
	    bestTour = new String[m_BestTourIndexes.size()];
	    
	    for(int i = 0; i < bestTour.length;i++ )
	    {	    
	    	bestTour[i] = Integer.toString(m_BestTourIndexes.get(i));
	    }
	}
	
	
	private double CalculateTSPForRemainingCities(int _cityCorrespondingBitMask, int _cityIndex)
	{
		if(_cityCorrespondingBitMask == m_VisitedAllCitiesState)
		{
			return m_DistanceMatrix[_cityIndex][0];
		}
		
		if(m_DPTableOfAllMaskValues[_cityCorrespondingBitMask][_cityIndex] != -1) // Has calculated cost allready
		{
			   return m_DPTableOfAllMaskValues[_cityCorrespondingBitMask][_cityIndex];
		}
		
		//Now from current node, we will try to go to every other node and take the min ans
		double m_MinCost = Double.POSITIVE_INFINITY;

		//Visit all the unvisited cities and take the best route
		for(int r = 0; r < m_CitiesCount;r++)
		{
			if((_cityCorrespondingBitMask & (1 << r)) == 0)
			{
				int m_NextCityCorrespondingBitMask = _cityCorrespondingBitMask | (1 << r);
				double m_RemainingCost = CalculateTSPForRemainingCities(m_NextCityCorrespondingBitMask, r);
				double m_NewCost = m_DistanceMatrix[_cityIndex][r] + m_RemainingCost;
				if (m_NewCost < m_MinCost) 
				{
					m_MinCost = m_NewCost;
					
					if(m_BestTourIndexes.contains(r) == false)
					{
						m_BestTourIndexes.add(r);
					}
			     }
			}

		}
		m_DPTableOfAllMaskValues[_cityCorrespondingBitMask][_cityIndex] = m_MinCost;
		return m_MinCost;
	}


}
