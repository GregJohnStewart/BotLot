package botLot.lotGraph;
import java.util.Collection;
import java.util.LinkedList;//for the actual path
import java.util.NoSuchElementException;
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
 * @version	1.0 1/20/16
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
	 * @throws LotPathException If the given set of edges is not continuous.
	 */
	public LotPath(Collection<LotEdge> collectionIn) throws LotPathException{
		this();
		this.setPath(collectionIn);
	}//LotPath(Collection<LotEdge>)
	
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
	 * @throws LotPathException If the given set of edges is not continuous.
	 */
	public void setPath(Collection<LotEdge> collectionIn) throws LotPathException{
		this.path = new LinkedList<LotEdge>(collectionIn);
		if(!this.pathIsValid()){
			throw new LotPathException("Given set of edges is not continuous.");
		}
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
	 * Gets the ratio of the number of individual edges to the total path metric.
	 * <p>
	 * returns: (# of edges) / (total path metric) 
	 * 
	 * @return	The 
	 */
	public double getEdgesMetricRatio(){
		return this.size() / this.getPathMetric();
	}
	
	/**
	 * Gets the metric for traveling the whole path.
	 * <p>
	 * TODO:: do I want to do this this way? Figure that out.
	 * 
	 * @return	The metric of the entire path.
	 */
	public double getNormalizedPathMetric(){
		double metricSum = this.getPathMetric();
		double ratio = this.getEdgesMetricRatio();
		double endMetric = metricSum / ratio;
		return endMetric;
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
	
	/**
	 * Determines if the path given is valid.
	 * 
	 * @param pathIn The path to check.
	 * @return	If the path given is valid.
	 */
	public static boolean pathIsValid(LotPath pathIn){
		for(int i = 0; i < pathIn.size(); i++){
			if((i + 1) == pathIn.size()){
				return true;
			}else if(!pathIn.path.get(i).getEndNode().hasEdge(pathIn.path.get(i + 1))){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Determines if the path held by this object is valid.
	 * 
	 * @return	If the path given is valid.
	 */
	public boolean pathIsValid(){
		return pathIsValid(this);
	}
	
	/**
	 * Appends a collection of LotEdges to the current list.
	 * 
	 * @param edgeListIn	The edge list to append.
	 * @throws LotPathException	If the given list breaks the path.
	 */
	public void append(Collection<LotEdge> edgeListIn) throws LotPathException{
		if(edgeListIn.isEmpty()){
			return;
		}
		LinkedList<LotEdge> listToAppend = (LinkedList<LotEdge>)edgeListIn;
		if(this.path.getLast().getEndNode().hasEdge(listToAppend.get(0))){
			path.addAll(edgeListIn);
			if(!this.pathIsValid()){
				throw new LotPathException("The path we ended up with was not continuous.");
			}
		}else{
			throw new LotPathException("The given set would not make a continuous path if appended.");
		}
	}
	
	/**
	 * Appends a LotPath to this  LotPath.
	 * 
	 * @param pathIn	The path to append.
	 * @throws LotPathException	If the given path breaks the path.
	 */
	public void append(LotPath pathIn) throws LotPathException{
		this.append(pathIn.path);
	}
	
	/**
	 * Appends an edge to the path.
	 * 
	 * @param edgeIn	The edge to add.
	 * @throws LotPathException	If the edge given would not make a continuous path if appended.
	 */
	public void append(LotEdge edgeIn) throws LotPathException{
		boolean hasPathToEdge;
		try{
			hasPathToEdge = this.path.getLast().getEndNode().hasEdge(edgeIn);
		}catch(NoSuchElementException e){
			hasPathToEdge = false;
		}
		if(hasPathToEdge || this.path.isEmpty()){
			path.add(edgeIn);
		}else{
			throw new LotPathException("The given edge would not make a continuous path if appended.");
		}
	}
	
	/**
	 * Determines if this path is shorter than the path given.
	 * 
	 * @param pathIn	The path we are testing against.
	 * @return	If this path is shorter than the path given.
	 */
	public boolean isShorter(LotPath pathIn){
		if(this.getPathMetric() < pathIn.getPathMetric()){
			return true;
		}
		return false;
	}
	
	/**
	 * Determines if this path is longer than the path given.
	 * <p>
	 * Wrapper for {@link #isShorter(LotPath)}
	 * 
	 * @param pathIn	The path we are testing against.
	 * @return	If this path is longer than the path given.
	 */
	public boolean isLonger(LotPath pathIn){
		return !this.isShorter(pathIn);
	}
	
	public boolean hasWaypoint(LotNode nodeIn){
		//TODO:: do this
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LotPath other = (LotPath) obj;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LotPath [metric="+this.getPathMetric()+", numEdges="+this.path.size()+" edge/metric ratio="+this.getEdgesMetricRatio()+", path=" + path + " ]";
	}
}//class LotPath