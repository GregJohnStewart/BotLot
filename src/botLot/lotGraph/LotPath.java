package botLot.lotGraph;
import java.util.Collection;
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
 * @version	1.0 12/9/15
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
	 * Constructor that takes in a collection of edges.
	 * 
	 * @param collectionIn	The collection of edges.
	 */
	public LotPath(Collection<LotEdge> collectionIn){
		this();
		this.setPath(collectionIn);
	}//LotPath(Collection<LotEdge>)
	
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
	 * Sets the {@link #path} to a collection of edges.
	 * 
	 * @param collectionIn	The collection of edges.
	 */
	public void setPath(Collection<LotEdge> collectionIn){
		this.path = new LinkedList<LotEdge>(collectionIn);
	}//setPath(Collection<LotEdge)
	
	/**
	 * Gets the metric for traveling the whole path.
	 * 
	 * @return	The metric of the entire path.
	 */
	public double getPathMetric(){
		double metricSum = 0;
		for(int i = 0; i < this.path.size(); i++){
			metricSum += this.path.get(i).getMetric();
		}
		return metricSum;
	}//getPathMetric()
	
	/**
	 * Gets the number of 'hops' in the path.
	 * <p>
	 * Essentially a wrapper for path.size()
	 * 
	 * @return	The size of the path, aka how many hops the path has.
	 */
	public int size(){
		return this.path.size();
	}//size()
	
	/**
	 * Removes all loops from the path.
	 */
	public void removeLoops(){
		//System.out.println("Removing loops:");
		for(int i = 0;i < this.path.size(); i++){
			//System.out.println("\tAt edge " + i + " of " + this.path.size());
			while(this.path.indexOf(this.path.get(i)) != this.path.lastIndexOf(this.path.get(i))){
				//System.out.println("\t\t" + this.path.indexOf(this.path.get(0)) + " and " + this.path.lastIndexOf(this.path.get(i)));
				this.path.remove(i + 1);
			}
		}
	}//removeLoops
	
}//class LotPath