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
 * TODO:: deal with edges to avoid.
 * 
 * @author Greg Stewart
 * @version	1.0 3/12/16
 */
public class LotPath {
	/** the path of edges held by this object */
	public LinkedList<LotEdge> path;
	/** Flag to denote this should be treated as a path with infinite size. For comparison purposes. */
	public boolean infSizeFlag = false;
	
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
	 * Creates a lot path out of a list of nodes.
	 * 
	 * @param nodeListIn	The list of nodes we are getting.
	 * @param edgesToAvoid	Edges we do not want to go down. Simply give this param as an empty subclass of Collection to negate this. (this param is required to overload the other constructor with a collection)
	 * @throws LotPathException	If there is no actual path described by this list.
	 */
	public LotPath(Collection<LotNode> nodeListIn, Collection<LotEdge> edgesToAvoid) throws LotPathException{
		this();
		this.setPathWithNodes(nodeListIn, edgesToAvoid);
	}
	
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
     * @return	This path.
	 * @throws LotPathException If the given set of edges is not continuous.
	 */
	public LotPath setPath(Collection<LotEdge> collectionIn) throws LotPathException{
		this.path = new LinkedList<LotEdge>(collectionIn);
		if(!this.pathIsContinuous()){
			throw new LotPathException("Given set of edges is not continuous.");
		}
		return this;
	}//setPath(Collection<LotEdge)
	
	/**
	 * Sets the current path to one given via a collection of nodes.
	 * 
	 * @param nodeListIn	The list of nodes to turn into the path.
	 * @return	This path.
	 * @throws LotPathException	If something went wrong.
	 */
	public LotPath setPathWithNodes(Collection<LotNode> nodeListIn) throws LotPathException{
		return setPathWithNodes(nodeListIn, new LinkedList<LotEdge>());
	}//setPathWithNodes(Collection<LotNode>)
	
	/**
	 * Sets the current path to one given via a collection of nodes.
	 * 
	 * @param nodeListIn	The list of nodes in.
	 * @param edgesToAvoidIn	Edges we do not want to go down.
	 * @return	This LotPath.
	 * @throws LotPathException	If something goes wrong.
	 */
	public LotPath setPathWithNodes(Collection<LotNode> nodeListIn, Collection<LotEdge> edgesToAvoidIn) throws LotPathException{
		LinkedList<LotNode> nodeList = new LinkedList<LotNode>(nodeListIn);
		while(true){
			if(nodeList.size() == 1){
				return this;
			}
			try {
				this.append(nodeList.get(0).getShortestEdgeTo(nodeList.get(1), edgesToAvoidIn));
			} catch (LotPathException e) {
				throw new LotPathException("Node list given is not continuous. Inner Error: " + e.getMessage());
			}
			nodeList.removeFirst();
		}
	}

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
	 * Gets the node at the end of the path.
	 * 
	 * @return The node at the end of the path.
	 */
	public LotNode getEndNode(){
		return this.path.getLast().getEndNode();
	}//getEndNode();
	
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
	 * Determines if this path contains any loops.
	 * 
	 * @return	If this path contains any loops.
	 */
	public boolean hasLoops(){
		for(int i = 0;i < this.path.size(); i++){
			//System.out.println("\tAt edge " + i + " of " + this.path.size());
			if(this.path.indexOf(this.path.get(i)) != this.path.lastIndexOf(this.path.get(i))){
				return true;
			}
		}
		return false;
	}//hasLoops()
	
	/**
	 * Determines if the path given is valid.
	 * 
	 * @param pathIn The path to check.
	 * @return	If the path given is valid.
	 */
	public static boolean pathIsContinuous(LotPath pathIn){
		LotEdge lastEdge = null;
		for(LotEdge curEdge : pathIn.path){
			if(lastEdge == null){
				lastEdge = curEdge;
			}else{
				if(lastEdge.getEndNode().getConnectedEdges().contains(curEdge)){
					lastEdge = curEdge;
				}else{
					return false;
				}
			}
		}
		return true;
	}//pathIsContinuous(LotPath)
	
	/**
	 * Determines if the path held by this object is valid.
	 * 
	 * @return	If the path given is valid.
	 */
	public boolean pathIsContinuous(){
		return pathIsContinuous(this);
	}//pathIsContinuous()
	
	/**
	 * Appends a collection of LotEdges to the current list.
	 * 
	 * @param edgeListIn	The edge list to append.
     * @return	This path.
	 * @throws LotPathException	If the given list breaks the path.
	 */
	public LotPath append(Collection<LotEdge> edgeListIn) throws LotPathException{
		if(edgeListIn.isEmpty()){
			return this;
		}
		LinkedList<LotEdge> listToAppend = (LinkedList<LotEdge>)edgeListIn;
		if(this.path.getLast().getEndNode().hasEdge(listToAppend.get(0))){
			path.addAll(edgeListIn);
			if(!this.pathIsContinuous()){
				throw new LotPathException("The path we ended up with was not continuous.");
			}
		}else{
			throw new LotPathException("The given set would not make a continuous path if appended.");
		}
		return this;
	}//append(Collection<LotEdge>)
	
	/**
	 * Appends a LotPath to this  LotPath.
	 * 
	 * @param pathIn	The path to append.
     * @return	This path.
	 * @throws LotPathException	If the given path breaks the path.
	 */
	public LotPath append(LotPath pathIn) throws LotPathException{
		this.append(pathIn.path);
		return this;
	}//append(LotPath)
	
	/**
	 * Appends an edge to the path.
	 * 
	 * @param edgeIn	The edge to add.
     * @return	This path.
	 * @throws LotPathException	If the edge given would not make a continuous path if appended.
	 */
	public LotPath append(LotEdge edgeIn) throws LotPathException{
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
		return this;
	}//append(LotEdge)
	
	/**
	 * Appends an edge from the last node pointed to in the set to the given node
	 * 
	 * @param nodeIn	The node to add an edge to.
	 * @return	This path.
	 * @throws LotPathException	If something went wrong.
	 */
	public LotPath append(LotNode nodeIn) throws LotPathException{
		if(this.path.size() == 0){
			throw new LotPathException("There are no nodes in path.");
		}else if(this.path.getLast().getEndNode().hasEdgeTo(nodeIn)){
			this.append(this.path.getLast().getEndNode().getShortestEdgeTo(nodeIn));
		}else{
			throw new LotPathException("Node given has no paths going to it from the last node in path.");
		}
		return this;
	}//append(LotNode)
	
	/**
	 * Determines if this path is shorter than the path given.
	 * 
	 * @param pathIn	The path we are testing against.
	 * @return	If this path is shorter than the path given.
	 */
	public boolean isShorter(LotPath pathIn){
		if(this.infSizeFlag){
			return false;
		}
		if(pathIn.infSizeFlag){
			return true;
		}
		if(this.getPathMetric() < pathIn.getPathMetric()){
			return true;
		}else if(this.getPathMetric() == pathIn.getPathMetric()){
			if(this.size() < pathIn.size()){
				return true;
			}
		}
		return false;
	}//isShorter(LotPath)
	
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
	}//isLonger(LotPath)
	
	/**
	 * Determines if the path hits the node given during its travel.
	 * 
	 * @param nodeIn	The node to check for.
	 * @return	If we hit the node or not.
	 */
	public boolean hasWaypoint(LotNode nodeIn){
		return this.hasWaypoint(nodeIn, true);
	}//hasWaypoint(LotNode)
	
	/**
	 * Determines if the path hits the node given during its travel.
	 * 
	 * @param nodeIn	The node to check for.
	 * @param countLast	If you want to account for the last node.
	 * @return	If we hit the node or not.
	 */
	public boolean hasWaypoint(LotNode nodeIn, boolean countLast){
		//loop through path, checking end nodes
		for(LotEdge curEdge : this.path){
			if(curEdge.getEndNode() == nodeIn){
				if(!countLast & curEdge == this.path.getLast()){
					return false;
				}
				return true;
			}
		}
		return false;
	}//hasWaypoint(LotNode)
	
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
		return "LotPath [metric="+this.getPathMetric()+", numEdges="+this.path.size()+", edge/metric ratio="+this.getEdgesMetricRatio()+", path=" + path + " ]";
	}
}//class LotPath