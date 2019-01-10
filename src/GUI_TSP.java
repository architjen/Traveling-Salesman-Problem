import java.awt.Color;
import java.awt.Graphics;
import java.util.*;

import javax.swing.JFrame;

public class GUI_TSP extends JFrame {
	/**
	 * This class helps visualize a graph and some selected edges
	 */
	private static final long serialVersionUID = 1L;

	private CityGraph cityGraph;
	private int MAX_SIZE = 700;
	private int OFF_SET = 50;
	List<Edge> edgesToPrint;

	public GUI_TSP(CityGraph cityGraph) {
		this.cityGraph = cityGraph;
		setTitle("TSP: " + cityGraph.algoName + ", Cost:" + cityGraph.guiCost);
		setSize(MAX_SIZE + 260, MAX_SIZE + 260);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public void paint(Graphics g) {
		paintCircle(g);
		printEdges(g);
	}

	public void paintCircle(Graphics g) {
		for (Edge e : this.cityGraph.getAllEdgesList()) {
			drawCity(g, e.c1);
			drawCity(g, e.c2);
			// drawLine(g, e, Color.black);
		}
		

	}

	//The GUI will need to project the coordinates of the cities based on the size of the window
	public void drawLine(Graphics g, Edge e, Color c) {
		//First find the percentage of the city location with respect to the coordinate of the city
		//divided by the min max value of the graph by the distance between min and max of city graph
		double percentX1 = ((e.c1.getX() - this.cityGraph.getMinX())
				/ Math.abs((this.cityGraph.getMinX() - this.cityGraph.getMaxX())));
		
		//Multiply percent by the max size of window 
		int normalizedX1 = OFF_SET + ((Double) (percentX1 * MAX_SIZE)).intValue();
		double percentY1 = ((e.c1.getY() - this.cityGraph.getMinY())
				/ Math.abs((this.cityGraph.getMinY() - this.cityGraph.getMaxY())));
		int normalizedY1 = OFF_SET + ((Double) (percentY1 * MAX_SIZE)).intValue();

		//Do the same as the X but for the Y
		double percentX2 = ((e.c2.getX() - this.cityGraph.getMinX())
				/ Math.abs((this.cityGraph.getMinX() - this.cityGraph.getMaxX())));
		int normalizedX2 = OFF_SET + ((Double) (percentX2 * MAX_SIZE)).intValue();
		double percentY2 = ((e.c2.getY() - this.cityGraph.getMinY())
				/ Math.abs((this.cityGraph.getMinY() - this.cityGraph.getMaxY())));
		int normalizedY2 = OFF_SET + ((Double) (percentY2 * MAX_SIZE)).intValue();
		g.setColor(c);
		g.drawLine(normalizedX1, normalizedY1, normalizedX2, normalizedY2);
	}
	
	//Draw a circle 
	public void drawCity(Graphics g, City c) {
		int circleSize = 10;
		double percentX = ((c.getX() - this.cityGraph.getMinX())
				/ Math.abs((this.cityGraph.getMinX() - this.cityGraph.getMaxX())));
		int normalizedX = OFF_SET + ((Double) (percentX * MAX_SIZE)).intValue();
		double percentY = ((c.getY() - this.cityGraph.getMinY())
				/ Math.abs((this.cityGraph.getMinY() - this.cityGraph.getMaxY())));
		int normalizedY = OFF_SET + ((Double) (percentY * MAX_SIZE)).intValue();
		g.setColor(Color.BLACK);
		g.fillOval(normalizedX - circleSize / 2, normalizedY - circleSize / 2, circleSize, circleSize);
	}

	public void printEdges(Graphics g) {
		Color c = g.getColor();

		for (Edge e : this.cityGraph.edgesToPrint) {
			drawLine(g, e, Color.RED);
		}
		g.setColor(c);
	}
}
