package BotLot.LotGraph;
import java.util.LinkedList;//for the actual path
/**
 * LotPath.java
 * <p>
 * The class to hold a path for the program to use to traverse the graph.
 * <p>
 * Includes miscellaneous methods/functions for info about the path.
 * <p>
 * Started: 11/18/15
 * 
 * @author Greg Stewart
 * @version	1.0 11/22/15
 */
public class LotPath {
	/** the path of edges held by this object */
	public LinkedList<LotEdge> path;
	
	/**
	 * Constructor that takes in a LotPath.
	 * 
	 * @param pathIn	The LotPath given.
	 */
	public LotPath(LotPath pathIn){
		this();
		this.path = new LinkedList<LotEdge>(pathIn.path);
	}//LotPath(LotPath)
	
	/**
	 * Constructor that takes in a linked list of LotEdges.
	 * 
	 * @param pathEdgesIn
	 */
	public LotPath(LinkedList<LotEdge> pathEdgesIn){
		this();
		this.path = pathEdgesIn;
	}//LotPath(LinkedList<LotEdge>)
	
	/**
	 * Basic constructor. Initializes a new linked list for the edges.
	 */
	public LotPath(){
		this.path = new LinkedList<LotEdge>();
	}//LotPath()
	
	/**
	 * Gets the metric for traveling the whole path
	 * 
	 * @return	The metric of the entire path
	 */
	public double getPathMetric(){
		double metricSum = 0;
		for(int i = 0; i < path.size(); i++){
			metricSum += path.get(i).getMetric();
		}
		return metricSum;
	}//getPathMetric()
}//class LotPath