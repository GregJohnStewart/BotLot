package BotLot;
import BotLot.LotGraph.*;//for dealing with all the LotGraph stuff
import java.util.ArrayList;//for dealing with the ArrayLists in the graph.
import java.util.LinkedList;//for dealing with the LinkedList of the path.
/**
 * BotLot.java
 * <p>
 * Holds the graph object and handles operations directly related with handling where it is in the graph, and moving around that graph.
 * <p>
 * Started: 10/7/15
 * 
 * @author Greg Stewart
 * @version	1.0 11/24/15
 */
public class BotLot{
	/** The main graph. Where all data is held. */
	public LotGraph mainGraph;
	/** the node we are currently at in the graph. Required to be set for any path finding operations. */
	private LotNode curNode;
	/** The node we are currently trying to get to. Required to be set for any path finding operations. */
	private LotNode destNode;
	/** The current path to the node we are trying to get to.<p>TODO:: change current system of deleting as we go along to simply keeping an index placeholder?*/
	private LotPath curPath;
	
	
    //=========================================================================
    //    Constructors
    //region Constructors
    //=========================================================================
    
	/**
	 * Constructor that takes in another BotLot object.
	 * 
	 * @param lotIn	The other BotLot object.
	 */
	public BotLot(BotLot lotIn){
		this();
		this.mainGraph = new LotGraph(lotIn.mainGraph);
		//just set the node, we know that it is valid from the nature of the object
		this.curNode = new LotNode(lotIn.getCurNode());
		this.curPath = new LotPath(lotIn.getCurPath());
	}//BotLot(BotLot)
	
	/**
	 * Constructor that takes in a graph and a node.
	 * 
	 * @param graphIn	The graph to set this node to.
	 * @param nodeIn	The node we are setting this to.
	 * @throws BotLotException	If we are not able to set the current node with the node given.
	 */
	public BotLot(LotGraph graphIn, LotNode nodeIn) throws BotLotException{
		this(graphIn);
		try{
			this.setCurNode(nodeIn);
		}catch(BotLotException err){
			throw new BotLotException("Unable to set the current node. Error: " + err.getMessage());
		}
	}//BotLot(LotGraph, nodeIn)
	
	/**
	 * Constructor to set the graph.
	 * 
	 * @param graphIn	The graph to set this to.
	 */
	public BotLot(LotGraph graphIn){
		this();
		this.setGraph(graphIn);
	}//BotLot(LotGraph)
	
	/**
	 * Basic constructor.
	 * @throws BotLotException Shouldn't do this.
	 */
	public BotLot(){
		this.setGraph(new LotGraph());
		this.clearCurPath();
		try{
			this.setCurNode((LotNode)null);
		}catch(BotLotException err){
			System.out.println("FATAL ERROR- BotLot()- Unable to do empty constructor. You should not get this. Error: " + err.getMessage());
			System.exit(1);
		}
	}//BotLot()
	
	
	//endregion
	

    //=========================================================================
    //    Setters
    //region Setters
    //=========================================================================
	
	/**
	 * Sets the graph data to the new graph data.
	 * <p>
	 * Also checks if the {@link #curNode}, {@link #destNode}, {@link #curPath} are present in the new data. If not, it clears them.
	 * 
	 * @param graphIn	The new graph data.
	 */
	public void setGraph(LotGraph graphIn){
		this.mainGraph = graphIn;
		if(!this.mainGraph.hasNode(this.getCurNode())){
			this.clearCurNode();
		}
		if(!this.mainGraph.hasNode(this.getDestNode())){
			this.clearDestNode();
		}
		if(!this.curPathIsValid()){
			this.clearCurPath();
		}
	}//setGraph(LotGraph)
	
	/**
	 * Sets {@link #curNode} to the node given.
	 * 
	 * @param nodeIn	The node to set the curNode to.
	 * @throws BotLotException	If the node is not within the data.
	 */
	public void setCurNode(LotNode nodeIn) throws BotLotException{
		if(nodeIn != null && this.mainGraph.hasNode(nodeIn)){
			this.curNode = nodeIn;
		}else{
			if(nodeIn == null){
				this.curNode = null;
			}else{
				throw new BotLotException("Node not found withing ");
			}
		}
	}//setCurNode(LotNode)
	
	/**
	 * Sets {@link #curNode} to the node with the specified id.
	 * 
	 * @param nodeIdIn	The is of the node to add.
	 * @throws BotLotException	If the node is not in the data.
	 */
	public void setCurNode(String nodeIdIn) throws BotLotException{
		if(nodeIdIn != null && this.mainGraph.hasNode(nodeIdIn)){
			this.curNode = this.mainGraph.getNode(nodeIdIn);
		}else{
			if(nodeIdIn == null){
				this.curNode = null;
			}else{
				throw new BotLotException("Node not found withing ");
			}
		}
	}//setCurNode(String)

	/**
	 * Sets {@link #curNode} based on the index given.
	 * 
	 * @param nodeIndexIn	The index of the node in the node list.
	 * @throws BotLotException	If the index does not point to a node.
	 */
	public void setCurNode(int nodeIndexIn) throws BotLotException{
		if(this.mainGraph.hasNode(nodeIndexIn)){
			try{
				this.curNode = this.mainGraph.getNode(nodeIndexIn);
			}catch(LotGraphException err){
				System.out.println("FATAL ERROR- setCurNode(). This should not happen.");
				System.exit(1);
			}
		}else{
			throw new BotLotException("Node not found withing ");
		}
	}//setCurNode(int)
	
	/**
	 * Clears the current node.
	 */
	public void clearCurNode(){
		try{
			this.setCurNode((LotNode)null);
		}catch(BotLotException err){
			System.out.println("FATAL ERROR- clearCurNode()- Unable to do clear current node. You should not get this. Error: " + err.getMessage());
			System.exit(1);
		}
	}//clearCurNode()
	
	/**
	 * Sets {@link #destNode}. Checks if it is there or not.
	 * @param nodeIn
	 * @throws BotLotException
	 */
	public void setDestNode(LotNode nodeIn) throws BotLotException{
		if(nodeIn != null && this.mainGraph.hasNode(nodeIn)){
			this.destNode = nodeIn;
		}else{
			if(nodeIn == null){
				this.destNode = null;
			}else{
				throw new BotLotException("Node not found withing ");
			}
		}
	}//setDestNode(LotNode)
	
	/**
	 * Sets {@link #destNode}, and checks to ensure it is in the data.
	 * 
	 * @param nodeIdIn	The id of the node.
	 * @throws BotLotException	If the node is not present in the graph.
	 */
	public void setDestNode(String nodeIdIn) throws BotLotException{
		if(nodeIdIn != null && this.mainGraph.hasNode(nodeIdIn)){
			this.destNode = this.mainGraph.getNode(nodeIdIn);
		}else{
			if(nodeIdIn == null){
				this.destNode = null;
			}else{
				throw new BotLotException("Node not found withing ");
			}
		}
	}//setDestNode(String)

	/**
	 * Sets {@link #destNode}, and checks to ensure it is in the data.
	 * 
	 * @param nodeIndexIn	The index of the destination node in the node list.
	 * @throws BotLotException	If the node index given is invalid.
	 */
	public void setDestNode(int nodeIndexIn) throws BotLotException{
		if(this.mainGraph.hasNode(nodeIndexIn)){
			try{
				this.destNode = this.mainGraph.getNode(nodeIndexIn);
			}catch(LotGraphException err){
				System.out.println("FATAL ERROR- setCurNode(). This should not happen.");
				System.exit(1);
			}
		}else{
			throw new BotLotException("setDestNode(int)- Node not found within graph data.");
		}
	}//setDestNode(int)
	
	/**
	 * Clears {@link #destNode}.
	 */
	public void clearDestNode(){
		try{
			this.setDestNode((LotNode)null);
		}catch(BotLotException err){
			System.out.println("FATAL ERROR- clearCurNode()- Unable to do clear current node. You should not get this. Error: " + err.getMessage());
			System.exit(1);
		}
	}//clearDestNode()
	
	/**
	 * Adds an edge between {@link #curNode} and the node given.
	 * 
	 * @param nodeToIn	The the node the edge is going to.
	 * @param edgeIn	The edge to add.
	 * @throws BotLotException	If either the current node or the node given cannot be found
	 */
	public void addEdge(LotNode nodeToIn, LotEdge edgeIn) throws BotLotException{
		if(this.graphHasCurNode()){
			try{
				this.mainGraph.setEdge(edgeIn, this.getCurNode(), nodeToIn);
			}catch(LotGraphException err){
				throw new BotLotException("Unable to add edge. Inner error: " + err.getMessage());
			}
		}else{
			throw new BotLotException("The current node is no longer in the data set.");
		}
	}//addEdge()
	
	/**
	 * Adds an edge between {@link #curNode} and the node given.
	 * 
	 * @param nodeToIdIn	The id of the node the edge is going to.
	 * @param edgeIn	The edge to add.
	 * @throws BotLotException	If either the current node or the node given cannot be found.
	 */
	public void addEdge(String nodeToIdIn, LotEdge edgeIn) throws BotLotException{
		if(this.graphHasCurNode()){
			try{
				this.mainGraph.setEdge(edgeIn, this.getCurNode().getId(), nodeToIdIn);
			}catch(LotGraphException err){
				throw new BotLotException("Unable to add edge. Inner error: " + err.getMessage());
			}
		}else{
			throw new BotLotException("The current node is no longer in the data set.");
		}
	}//addEdge()
	
	/**
	 * Adds an edge between {@link #curNode} and the node given.
	 * 
	 * @param nodeToIndexIn	The index of the node the edge is going to.
	 * @param edgeIn	The edge to add.
	 * @throws BotLotException	If either the current node or the node given cannot be found.
	 */
	public void addEdge(int nodeToIndexIn, LotEdge edgeIn) throws BotLotException{
		if(this.graphHasCurNode()){
			try{
				this.mainGraph.setEdge(edgeIn, this.mainGraph.getNodeIndex(this.getCurNode()), nodeToIndexIn);
			}catch(LotGraphException err){
				throw new BotLotException("Unable to add edge. Inner error: " + err.getMessage());
			}
		}else{
			throw new BotLotException("The current node is no longer in the data set.");
		}
	}//addEdge()
	
	/**
	 * Removes an edge from {@link #curNode}.
	 * 
	 * @param nodeToIn	The the node we are removing the edge to.
	 * @throws BotLotException	If either the current edge or the edge to remove the edge to is not in the graph.
	 */
	public void remEdge(LotNode nodeToIn) throws BotLotException{
		if(this.graphHasCurNode()){
			try{
				this.mainGraph.removeEdge(this.getCurNode(), nodeToIn);
			}catch(LotGraphException err){
				throw new BotLotException("Unable to remove edge. Inner error: " + err.getMessage());
			}
		}else{
			throw new BotLotException("The current node is no longer in the data set.");
		}
	}//remEdge(LotNode)
	
	/**
	 * Removes an edge from {@link #curNode}.
	 * 
	 * @param nodeToIdIn	The id of the node we are removing the edge to.
	 * @throws BotLotException	If either the current edge or the edge to remove the edge to is not in the graph.
	 */
	public void remEdge(String nodeToIdIn) throws BotLotException{
		if(this.graphHasCurNode()){
			try{
				this.mainGraph.removeEdge(this.getCurNode().getId(), nodeToIdIn);
			}catch(LotGraphException err){
				throw new BotLotException("Unable to remove edge. Inner error: " + err.getMessage());
			}
		}else{
			throw new BotLotException("The current node is no longer in the data set.");
		}
	}//remEdge(String)
	
	/**
	 * Removes an edge from {@link #curNode}.
	 * 
	 * @param nodeToIndexIn	The index of the node we are removing the edge to.
	 * @throws BotLotException	If either the current edge or the edge to remove the edge to is not in the graph.
	 */
	public void remEdge(int nodeToIndexIn) throws BotLotException{
		if(this.graphHasCurNode()){
			try{
				this.mainGraph.removeEdge(this.mainGraph.getNodeIndex(this.getCurNode()), nodeToIndexIn);
			}catch(LotGraphException err){
				throw new BotLotException("Unable to remove edge. Inner error: " + err.getMessage());
			}
		}else{
			throw new BotLotException("The current node is no longer in the data set.");
		}
	}//remEdge(int)
	
	/**
	 * Sets the {@link #curPath} we have for the one using this object to go through. 
	 * <p>
	 * TODO:: performance is going to be an issue here. Also need to check for node continuity.
	 * 
	 * @param pathIn	The path we are trying to set to.
	 * @throws LotGraphException	If something went wrong with edge checking.
	 */
	public void setCurPath(LinkedList<LotEdge> pathIn) throws BotLotException{
		this.curPath.path = pathIn;
		if(!this.curPathIsValid()){
			this.clearCurPath();
			throw new BotLotException("Path given is not valid; is not continuous for this data.");
		}
	}//setCurPath(LinkedList<LotEdge>)
	
	/**
	 * Sets {@link #curPath}. Also checks that path to ensure it is valid.
	 * 
	 * @param pathIn	The path to set to.
	 * @throws BotLotException	If the path given is not valid.
	 */
	public void setCurPath(LotPath pathIn) throws BotLotException{
		this.curPath = pathIn;
		if(!this.curPathIsValid()){
			this.clearCurPath();
			throw new BotLotException("Path given is not valid; is not continuous for this data.");
		}
	}//setCurPath(LotPath)
	
	/**
	 * Adds an edge to {@link #curPath}.
	 * 
	 * @param edgeIn	The edge we are adding.
	 * @throws BotLotException If thew edge is not valid for the path, or not within the data.
	 */
	public void addToPath(LotEdge edgeIn) throws BotLotException{
		if(this.mainGraph.hasEdge(edgeIn)){
			this.curPath.path.addLast(edgeIn);
			if(!curPathIsValid()){
				this.curPath.path.removeLast();
				throw new BotLotException("Edge given does not make for a contiuous path.");
			}
		}else{
			throw new BotLotException("Edge given does not exist in data.");
		}
	}//addToPath(edgeIn)
	
	/**
	 * Moves {@link #curNode} through one edge.
	 * 
	 * @throws BotLotException	If the path is empty.
	 */
	public void movedThroughPath() throws BotLotException{
		if(!this.getCurPath().path.isEmpty()){
			try{
				this.setCurNode(this.mainGraph.getOtherNode(this.getCurNode(), this.getCurPath().path.removeFirst()));
			}catch(LotGraphException err){
				System.out.println("FATAL ERR- movedThroughPath(). This should not happen. Error: " + err.getMessage());
				System.exit(1);
			}
		}else{
			throw new BotLotException("Current path is empty or reached end of current path.");
		}
	}//movedThroughPath()
	
	/**
	 * Moves {@link #curNode} through the number of edges specified.
	 * 
	 * @param numSteps	The number of edges you want to go through.
	 * @throws BotLotException	If something went wrong.
	 */
	public void movedThroughPath(int numSteps) throws BotLotException{
		if(!this.getCurPath().path.isEmpty()){
			for(int i = 1; i <= numSteps; i++){
				try{
					this.movedThroughPath();
				}catch(BotLotException err){
					throw new BotLotException("Reached end of path early.");
				}
			}
		}else{
			throw new BotLotException("Current path is empty.");
		}
	}//movedThroughPath(int)
	
	/**
	 * Moves {@link #curNode} all the way through the path, eventually setting it to {@link #destNode}. Then clears {@link #destNode}.
	 * <p>
	 * Clears the current path as it goes through.
	 * @throws BotLotException If something went wrong. I.E., if the current path is empty. 
	 */
	public void movedToEndOfPath() throws BotLotException{
		try{
			this.movedThroughPath(this.getCurPath().path.size());
			this.clearDestNode();
		}catch(BotLotException err){
			if(err.getMessage().equals("Reached end of path early.")){
				throw err;
			}
			System.out.println("FATAL ERR- movedToEndOfPath(). This should not happen. Error: " + err.getMessage());
			System.exit(1);
		}
	}//movedToEndOfPath()
	
	/**
	 * Clears {@link #curPath}.
	 */
	public void clearCurPath(){
		this.curPath = new LotPath();
	}//clearCurPath()
	
	//endregion

    //=========================================================================
    //    Getters
    //region getters
    //=========================================================================
	
	
	/**
	 * Gets the node we are currently at, {@link #curNode}.
	 * 
	 * @return	The node we are currently at in the Graph ({@link #curNode}).
	 */
	public LotNode getCurNode(){
		return this.curNode;
	}//getCurNode();
	
	/**
	 * Gets the edges that {@link #curNode} has going out from it.
	 * 
	 * @return	An ArrayList of the edges that are going out from this node.
	 * @throws BotLotException If the current node is no longer in the graph.
	 */
	public ArrayList<LotEdge> getCurNodeEdges() throws BotLotException{
		if(this.graphHasCurNode()){
			ArrayList<LotEdge> tempEdgeList = new ArrayList<LotEdge>();
			int curNodeIndex = this.mainGraph.getNodeIndex(this.getCurNode());
			for(int i = 0; i < this.mainGraph.getNodeListSize(); i++){
				try{
					if(this.mainGraph.hasEdgeFromTo(curNodeIndex, i)){
						tempEdgeList.add(this.mainGraph.getEdgeFromTo(curNodeIndex, i));
					}
				}catch(LotGraphException err){
					System.out.println("FATAL ERROR- getCurNodeEdges()- Unable to do so. This should not happen. Message: "+ err.getMessage());
					System.exit(1);
				}
			}
			return tempEdgeList;
		}else{
			throw new BotLotException("The current node is no longer in the data set.");
		}
	}//getCurNodeEdges()
	
	/**
	 * Determines if BotLot knows where we are (If {@link #curNode} is set or not).
	 * 
	 * @return	If {@link #curNode} is set or not.
	 */
	public boolean hasCurNode(){
		if(this.getCurNode() != null){
			return true;
		}
		return false;
	}//hasCurNode()
	
	/**
	 * Determines if the graph data has the node we currently are at ({@link #curNode}).
	 * 
	 * @return	If the graph has {@link #curNode}.
	 */
	public boolean graphHasCurNode(){
		return this.mainGraph.hasNode(this.getCurNode());
	}//graphHasCurNode()
	
	/**
	 * Determines if {@link #curNode} has any edges.
	 * 
	 * @return	If {@link #curNode} has any edges or not.
	 * @throws BotLotException If {@link #curNode} is not in the Graph.
	 */
	public boolean curNodeHasEdges() throws BotLotException{
		try{
			if(this.getCurNodeEdges().size() > 0){
				return true;
			}
			return false;
		}catch(BotLotException err){
			throw err;
		}
	}//curNodeHasEdges()
	
	/**
	 * Gets {@link #destNode}.
	 * 
	 * @return	{@link #destNode}.
	 */
	public LotNode getDestNode(){
		return this.destNode;
	}//getDestNode()
	
	/**
	 * Determines if {@link #destNode} is set.
	 * 
	 * @return	If {@link #destNode} is set.
	 */
	public boolean hasDestNode(){
		if(this.getDestNode() != null){
			return true;
		}
		return false;
	}//hasDestNode()
	
	/**
	 * Tests to see if the graph has {@link #destNode}.
	 * 
	 * @return	If the graph has {@link #destNode}.
	 */
	public boolean graphHasDestNode(){
		return this.mainGraph.hasNode(this.getDestNode());
	}//graphHasDestNode()
	
	/**
	 * Gets {@link #curPath}.
	 * 
	 * @return	{@link #curPath}.
	 */
	public LotPath getCurPath(){
		return this.curPath;
	}//getCurPath()
	
	/**
	 * Checks the continuity of {@link #curPath}.
	 * <p>
	 * I.E., if all the edges lead through nodes that lead to the other nodes correctly.
	 * 
	 * @return	If {@link #curPath} is valid or not.
	 */
	public boolean curPathIsValid(){
		LotNode tempNode = this.getCurNode();
		for(LotEdge tempEdge : this.getCurPath().path){
			if(this.mainGraph.hasOtherNode(tempNode, tempEdge)){
				try{
					tempNode = this.mainGraph.getOtherNode(tempNode, tempEdge);
				}catch(LotGraphException err){
					System.out.println("FATAL ERR- curPathIsValid(). You should not get this. Error: " + err.getMessage());
				}
			}else{
				return false;//if the path is not continuous
			}
		}//for each
		return true;//if the path is continuous
	}//curPathIsValid()
	
	/**
	 * Calculates a new path based on {@link #curNode} and {@link #destNode}.
	 * 
	 * @throws BotLotException	If something went wrong.
	 */
	public void calcNewPath() throws BotLotException{
		if(this.hasCurNode() && this.hasDestNode() && this.curNodeHasEdges()){
			try{
				LinkedList<LotEdge> newPath = BotLotWorkers.getShortestPath(this);
				this.setCurPath(newPath);
				if(!this.curPathIsValid()){
					throw new BotLotException("Path generated is not valid.");
				}
			}catch(BotLotException err){
				System.out.println("FATAL ERR- calcNewPath(). This should not happen. Error: " + err.getMessage());
				System.exit(1);
			}
		}else{
			if(!this.hasCurNode() && !this.hasDestNode() && !this.curNodeHasEdges()){
				throw new BotLotException("You got nothing to work with, son. Come back after you have set more variables. (curNode & destNode not set, and curNode doesn't have any edges.)");
			}
			String exceptionOutput = "Something(s) isn't set- ";
			if(!this.hasCurNode()){
				exceptionOutput += " curNode";
			}
			if(!this.hasDestNode()){
				exceptionOutput += " destNode";
			}
			if(!this.curNodeHasEdges()){
				exceptionOutput += " curNode's edges";
			}
			throw new BotLotException(exceptionOutput);
		}
	}//calcNewPath()
	
	/**
	 * Calculates a new path to the given node using the functions in the BotLotWorkers class.
	 * <p>
	 * Automatically sets {@link #destNode}.
	 * 
	 * @param destNode	A new destination node to set.
	 * @throws BotLotException	If something went wrong with the generation.
	 */
	public void calcNewPath(LotNode destNode) throws BotLotException{
		try{
			this.setDestNode(destNode);
			this.calcNewPath();
		}catch(BotLotException err){
			throw new BotLotException("calcNewPath(LotNode)- " + err.getMessage());
		}
	}//calcNewPath(LotNode)

	/**
	 * Calculates a new path to the given node using the functions in the BotLotWorkers class.
	 * <p>
	 * Automatically sets {@link #destNode}.
	 * 
	 * @param destNodeId	The id of the node we are going to.
	 * @throws BotLotException	If something went wrong with the generation.
	 */
	public void calcNewPath(String destNodeId) throws BotLotException{
		try{
			this.setDestNode(destNodeId);
			this.calcNewPath();
		}catch(BotLotException err){
			throw new BotLotException("calcNewPath(String)- " + err.getMessage());
		}
	}//calcNewPath(String)
	
	/**
	 * Calculates a new path to the given node using the functions in the BotLotWorkers class.
	 * <p>
	 * Automatically sets {@link #destNode}.
	 * 
	 * @param destNodeIndex	The index of the destination node in the node list.
	 * @throws BotLotException	If something goes wrong with the generation.
	 */
	public void calcNewPath(int destNodeIndex) throws BotLotException{
		try{
			this.setDestNode(destNodeIndex);
			this.calcNewPath();
		}catch(BotLotException err){
			throw new BotLotException("calcNewPath(int)- " + err.getMessage());
		}
	}//calcNewPath(int)

	@Override
	public String toString() {
		return "BotLot [mainGraph=" + mainGraph + ", curNode=" + curNode + ", curPath=" + curPath + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((curNode == null) ? 0 : curNode.hashCode());
		result = prime * result + ((curPath == null) ? 0 : curPath.hashCode());
		result = prime * result + ((mainGraph == null) ? 0 : mainGraph.hashCode());
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
		BotLot other = (BotLot) obj;
		if (curNode == null) {
			if (other.curNode != null)
				return false;
		} else if (!curNode.equals(other.curNode))
			return false;
		if (curPath == null) {
			if (other.curPath != null)
				return false;
		} else if (!curPath.equals(other.curPath))
			return false;
		if (mainGraph == null) {
			if (other.mainGraph != null)
				return false;
		} else if (!mainGraph.equals(other.mainGraph))
			return false;
		return true;
	}
	
	//endregion
	
	
	
	
	
	
	
	
}//class BotLot
