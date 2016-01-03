package botLot;
import java.util.ArrayList;//for dealing with the ArrayLists in the graph.
import java.util.LinkedList;//for dealing with the LinkedList of the path.

import botLot.BotLotException;
import botLot.lotGraph.*;
/**
 * BotLot.java
 * <p>
 * Holds the graph object and handles operations directly related with handling where it is in the graph, and moving around that graph.
 * <p>
 * Started: 10/7/15
 * 
 * @author Greg Stewart
 * @version	1.0 12/12/15
 */
public class BotLot{
	/** The main graph. Where all data is held. */
	public LotGraph mainGraph;
	/** the node we are currently at in the graph. Required to be set for any path finding operations. */
	private LotNode curNode;
	/** The node we are currently trying to get to. Required to be set for any path finding operations. */
	private LotNode destNode;
	/** The current path to the node we are trying to get to.*/
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
	 * @param edgeIn	The edge to add.
	 * @throws BotLotException	If either the current node or the node given cannot be found
	 */
	public void addEdge(LotEdge edgeIn) throws BotLotException{
		if(this.graphHasCurNode()){
			try{
				this.mainGraph.setEdge(edgeIn, this.getCurNode());
			}catch(LotGraphException err){
				throw new BotLotException("Unable to add edge. Inner error: " + err.getMessage());
			}
		}else{
			throw new BotLotException("The current node is no longer in the data set.");
		}
	}//addEdge()
	
	/**
	 * Creates an edge off of the current node.
	 * 
	 * @return	The edge created.
	 * @throws BotLotException	If the current node isn't set.
	 */
	public LotEdge createEdge() throws BotLotException{
		if(this.graphHasCurNode()){
			try {
				return this.mainGraph.createEdge(this.getCurNode());
			} catch (LotGraphException e) {
				System.out.println("FATAL ERR- createEdge()- This should not happen. Error: " + e.getMessage());
				System.exit(1);
			}
		}
		throw new BotLotException("Cur node not set.");
	}//createEdge()
	/**
	 * Creates an edge off of the current node, and returns it's id.
	 * 
	 * @return	The id of the new edge.
	 * @throws BotLotException	If the current node isn't set.
	 */
	public String createEdgeGiveId() throws BotLotException{
		return this.createEdge().getId();
	}//createEdgeGiveId()
	
	/**
	 * Creates an edge to the given node.
	 * 
	 * @param toNodeIndex	The given node.
	 * @return	The edge created.
	 * @throws BotLotException	If the node given is not set.
	 */
	public LotEdge createEdgeTo(LotNode toNode) throws BotLotException{
		if(this.graphHasCurNode() && this.mainGraph.hasNode(toNode)){
			try {
				return this.mainGraph.createEdge(this.getCurNode(), toNode);
			} catch (LotGraphException e) {
				System.out.println("FATAL ERR- createEdgeTo(LotNode)- This should not happen. Error: " + e.getMessage());
				System.exit(1);
			}
		}
		if(!this.graphHasCurNode()){
			throw new BotLotException("Cur node not set.");
		}
		throw new BotLotException("toNode was not found in data.");
	}//createEdgeTo(LotNode)
	
	/**
	 * Creates an edge to the given node and returns it's ID.
	 * <p>
	 * Wrapper for {@link #createEdgeTo(LotNode)}
	 * 
	 * @param toNodeIndex	The given node.
	 * @return	The edge created.
	 * @throws BotLotException	If the node given is not set.
	 */
	public String createEdgeToGiveId(LotNode toNode) throws BotLotException{
		return this.createEdgeTo(toNode).getId();
	}//createEdgeToGiveId(LotNode)

	/**
	 * Creates an edge to the given node.
	 * 
	 * @param toNodeIndex	The id of the given node.
	 * @return	The edge created.
	 * @throws BotLotException	If the node given is not set.
	 */
	public LotEdge createEdgeTo(String toNodeId) throws BotLotException{
		if(this.graphHasCurNode() && this.mainGraph.hasNode(toNodeId)){
			try {
				return this.mainGraph.createEdge(this.getCurNode().getId(), toNodeId);
			} catch (LotGraphException e) {
				System.out.println("FATAL ERR- createEdgeTo(String)- This should not happen. Error: " + e.getMessage());
				System.exit(1);
			}
		}
		if(!this.graphHasCurNode()){
			throw new BotLotException("Cur node not set.");
		}
		throw new BotLotException("toNode was not found in data.");
	}//createEdgeTo(String)
	
	/**
	 * Creates an edge to the given node, and returns it's id.
	 * <p>
	 * Wrapper for {@link #createEdgeTo(String)}
	 * 
	 * @param toNodeIndex	The id of the given node.
	 * @return	The id of the edge created.
	 * @throws BotLotException	If the node given is not set.
	 */
	public String createEdgeToGiveId(String toNodeId) throws BotLotException{
		return this.createEdgeTo(toNodeId).getId();
	}//createEdgeToGiveId(String)
	
	/**
	 * Creates an edge to the given node.
	 * 
	 * @param toNodeIndex	The index of the given node.
	 * @return	The edge created.
	 * @throws BotLotException	If the node given is not set.
	 */
	public LotEdge createEdgeTo(int toNodeIndex) throws BotLotException{
		if(this.graphHasCurNode() && this.mainGraph.hasNode(toNodeIndex)){
			try {
				return this.mainGraph.createEdge(this.mainGraph.getNodeIndex(this.getCurNode()), toNodeIndex);
			} catch (LotGraphException e) {
				System.out.println("FATAL ERR- createEdgeTo(int)- This should not happen. Error: " + e.getMessage());
				System.exit(1);
			}
		}
		if(!this.graphHasCurNode()){
			throw new BotLotException("Cur node not set.");
		}
		throw new BotLotException("toNode was not found in data.");
	}//createEdgeTo(int)
	/**
	 * Creates an edge to the given node and returns its ID.
	 * <p>
	 * Wrapper for {@link #createEdgeTo(int)}
	 * 
	 * @param toNodeIndex	The index of the given node.
	 * @return
	 * @throws BotLotException
	 */
	public String createEdgeToGiveId(int toNodeIndex) throws BotLotException{
		return this.createEdgeTo(toNodeIndex).getId();
	}//createEdgeToGiveId(int)
	
	/**
	 * Removes an edge from {@link #curNode}.
	 * 
	 * @param toNode	The the node we are removing the edge to.
	 * @throws BotLotException	If either the current edge or the edge to remove the edge to is not in the graph.
	 */
	public void remEdge(LotNode toNode) throws BotLotException{
		if(this.graphHasCurNode() && this.mainGraph.hasEdgeFromTo(this.getCurNode(), toNode)){
			try{
				this.mainGraph.removeEdgeFromTo(this.getCurNode(), toNode);
			}catch(LotGraphException e){
				System.out.println("FATAL ERR- remEdge(LotNode)- This should not happen. Error: " + e.getMessage());
				System.exit(1);
			}
		}
		if(!this.graphHasCurNode()){
			throw new BotLotException("Cur node not set.");
		}
		throw new BotLotException("toNode was not found in data.");
	}//remEdge(LotNode)
	
	/**
	 * Removes an edge from {@link #curNode}.
	 * 
	 * @param toNodeId	The id of the node we are removing the edge to.
	 * @throws BotLotException	If either the current edge or the edge to remove the edge to is not in the graph.
	 */
	public void remEdge(String toNodeId) throws BotLotException{
		if(this.graphHasCurNode() && this.mainGraph.hasEdgeFromTo(this.getCurNode().getId(), toNodeId)){
			try{
				this.mainGraph.removeEdgeFromTo(this.getCurNode().getId(), toNodeId);
			}catch(LotGraphException e){
				System.out.println("FATAL ERR- remEdge(String)- This should not happen. Error: " + e.getMessage());
				System.exit(1);
			}
		}
		if(!this.graphHasCurNode()){
			throw new BotLotException("Cur node not set.");
		}
		throw new BotLotException("toNode was not found in data.");
	}//remEdge(String)
	
	/**
	 * Removes an edge from {@link #curNode}.
	 * 
	 * @param toNodeIndex	The index of the node we are removing the edge to.
	 * @throws BotLotException	If either the current edge or the edge to remove the edge to is not in the graph.
	 */
	public void remEdge(int toNodeIndex) throws BotLotException{
		if(this.graphHasCurNode() && this.mainGraph.hasEdgeFromTo(this.mainGraph.getNodeIndex(this.getCurNode()), toNodeIndex)){
			try{
				this.mainGraph.removeEdgeFromTo(this.mainGraph.getNodeIndex(this.getCurNode()), toNodeIndex);
			}catch(LotGraphException e){
				System.out.println("FATAL ERR- remEdge(LotNode)- This should not happen. Error: " + e.getMessage());
				System.exit(1);
			}
		}
		if(!this.graphHasCurNode()){
			throw new BotLotException("Cur node not set.");
		}
		throw new BotLotException("toNodeIndex does not point to a node in data.");
	}//remEdge(int)
	
	/**
	 * Sets the {@link #curPath} we have for the one using this object to go through. 
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
	 * Acknowledges that we have moved down a specified edge.
	 * <p>
	 * CLEARS THE CURRENT PATH because the path would no longer be valid.
	 * 
	 * @param edgeMovedDown	The edge moved down,
	 * @throws BotLotException If the current node does not have the given edge.
	 */
	public void movedDownEdge(LotEdge edgeMovedDown) throws BotLotException{
		if(this.curNodeHasEdge(edgeMovedDown)){
			try {
				this.setCurNode(this.mainGraph.getOtherNode(this.getCurNode(), edgeMovedDown));
			} catch (LotGraphException e) {
				System.out.println("FATAL ERR- movedDownEdge(). This should not happen. Error: " + e.getMessage());
				System.exit(1);
			}
			this.clearCurPath();
		}else{
			throw new BotLotException("Current node does not have the given edge.");
		}
	}//movedDownEdge(LotEdge)
	
	/**
	 * Acknowledges that we have moved down a specified edge.
	 * <p>
	 * CLEARS THE CURRENT PATH because the path would no longer be valid.
	 * 
	 * @param edgeMovedDownId	The edge moved down,
	 * @throws BotLotException If the current node does not have the given edge.
	 */
	public void movedDownEdge(String edgeMovedDownId) throws BotLotException{
		if(this.curNodeHasEdge(edgeMovedDownId)){
			try {
				this.setCurNode(this.mainGraph.getOtherNode(this.getCurNode().getId(), edgeMovedDownId));
			} catch (LotGraphException e) {
				System.out.println("FATAL ERR- movedDownEdge(). This should not happen. Error: " + e.getMessage());
				System.exit(1);
			}
			this.clearCurPath();
		}else{
			throw new BotLotException("Current node does not have the given edge.");
		}
	}//movedDownEdge(String)
	
	/**
	 * Acknowledges that we have moved down a specified edge.
	 * <p>
	 * CLEARS THE CURRENT PATH because the path would no longer be valid.
	 * 
	 * @param edgeMovedDownIndex	The edge moved down,
	 * @throws BotLotException If the current node does not have the given edge.
	 */
	public void movedDownEdge(int edgeMovedDownIndex) throws BotLotException{
		if(this.curNodeHasEdge(edgeMovedDownIndex)){
			try {
				this.setCurNode(this.mainGraph.getOtherNode(this.mainGraph.getNodeIndex(this.getCurNode()), edgeMovedDownIndex));
			} catch (LotGraphException e) {
				System.out.println("FATAL ERR- movedDownEdge(). This should not happen. Error: " + e.getMessage());
				System.exit(1);
			}
			this.clearCurPath();
		}else{
			throw new BotLotException("Current node does not have the given edge.");
		}
	}//movedDownEdge(String)
	
	/**
	 * Moves {@link #curNode} through one edge.
	 * 
	 * @throws BotLotException	If the path is empty.
	 */
	public void movedThroughPath() throws BotLotException{
		if(!this.getCurPath().path.isEmpty()){
			try{
				//hold the current path, to have it for use after we move
				LotPath tempPath = new LotPath(this.getCurPath());
				this.movedDownEdge(tempPath.path.removeFirst());
				//System.out.println("Moved down edge fine");
				this.curPath = tempPath;
			}catch(BotLotException e){
				System.out.println("FATAL ERR- movedThroughPath(). This should not happen. Error: " + e.getMessage());
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
				//System.out.println("Moving down path, " + i);
				try{
					this.movedThroughPath();
				}catch(BotLotException e){
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
		if(this.hasPath()){
			try{
				this.movedThroughPath(this.getCurPath().size());
				this.clearDestNode();
			}catch(BotLotException e){
				if(e.getMessage().equals("Reached end of path early.")){
					throw e;
				}
				System.out.println("FATAL ERR- movedToEndOfPath(). This should not happen. Error: " + e.getMessage());
				System.exit(1);
			}
		}else{
			throw new BotLotException("The object does not have a path set.");
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
			return this.getCurNode().getEdges();
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
	 */
	public boolean curNodeHasEdges(){
		try{
			if(this.getCurNodeEdges().size() > 0){
				return true;
			}
		}catch(BotLotException e){
			//throw e;
		}
		return false;
	}//curNodeHasEdges()

	/**
	 * Determines if the current node has a specified edge.
	 * 
	 * @param edgeIn	The specified edge.
	 * @return	If the current node has a specified edge.
	 */
	public boolean curNodeHasEdge(LotEdge edgeIn){
		if(this.hasCurNode() && this.getCurNode().hasEdge(edgeIn)){
			return true;
		}
		return false;
	}//curNodeHasEdge(LotEdge)
	
	/**
	 * Determines if the current node has an edge with a specified Id.
	 * 
	 * @param edgeIdIn	The specified edge Id.
	 * @return	If the current node has an edge with a specified Id.
	 */
	public boolean curNodeHasEdge(String edgeIdIn){
		if(this.hasCurNode() && this.getCurNode().hasEdge(edgeIdIn)){
			return true;
		}
		return false;
	}//curNodeHasEdge(String)
	
	/**
	 * Determines if the current node has an edge with a specified index.
	 * 
	 * @param edgeIdIn	The specified edge index.
	 * @return	If the current node has an edge with a specified index.
	 */
	public boolean curNodeHasEdge(int edgeIdIn){
		if(this.hasCurNode() && this.getCurNode().hasEdge(edgeIdIn)){
			return true;
		}
		return false;
	}//curNodeHasEdge(int)
	
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
	 * Lets anyone check if their path is valid.
	 * 
	 * @param pathIn	The path to check.
	 * @return	If the path given is valid.
	 */
	public boolean pathIsValid(LotPath pathIn){
		if(this.curPath != null && this.curPath.path != null && this.graphHasCurNode() && this.graphHasDestNode()){
			LotNode tempNode = this.getCurNode();
			for(LotEdge tempEdge : pathIn.path){
				if(this.mainGraph.hasOtherNode(tempNode, tempEdge)){
					try{
						tempNode = this.mainGraph.getOtherNode(tempNode, tempEdge);
					}catch(LotGraphException e){
						System.out.println("FATAL ERR- curPathIsValid(). You should not get this. Error: " + e.getMessage());
						System.exit(1);
					}
				}else{
					return false;//if the path is not continuous
				}
			}//for each
			return true;//if the path is continuous
		}
		return false;
	}//pathIsValid(LotPath)
	
	/**
	 * Checks the continuity of {@link #curPath}.
	 * <p>
	 * I.E., if all the edges lead through nodes that lead to the other nodes correctly.
	 * <p>
	 * Wrapper for {@link #pathIsValid(LotPath)}
	 * 
	 * @return	If {@link #curPath} is valid or not.
	 */
	public boolean curPathIsValid(){
		return this.pathIsValid(this.getCurPath());
	}//curPathIsValid()
	
	/**
	 * Determines if we have a path or not.
	 * <p>
	 * We assume that any path that we have is valid (checks happen on the setters).
	 * 
	 * @return	If we have a path.
	 */
	public boolean hasPath(){
		if(!(this.getCurPath() == new LotPath()) && !(this.getCurPath() != null)){
			return false;
		}
		return true;
	}//hasPath()
	
	public LotPath calcNewPathGivePath() throws BotLotException{
		if(this.hasCurNode() && this.hasDestNode() && this.curNodeHasEdges()){
			try{
				LotPath newPath = BotLotPathFinders.getShortestPath(this);
				
				if(!this.pathIsValid(newPath)){
					throw new BotLotException("Path generated is not valid.");
				}
				return newPath;
			}catch(BotLotPathFindingException e){
				System.out.println("FATAL ERR- calcNewPath(). This should not happen. Error: " + e.getMessage());
				System.exit(1);
			}
		}
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
	
	/**
	 * Calculates a new path based on {@link #curNode} and {@link #destNode}.
	 * 
	 * @throws BotLotException	If something went wrong.
	 */
	public void calcNewPath() throws BotLotException{
		this.setCurPath(this.calcNewPathGivePath());
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
		this.setDestNode(destNode);
		this.setCurPath(this.calcNewPathGivePath());
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
		this.setDestNode(destNodeId);
		this.setCurPath(this.calcNewPathGivePath());
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
		this.setDestNode(destNodeIndex);
		this.setCurPath(this.calcNewPathGivePath());
	}//calcNewPath(int)
	
	/**
	 * Determines if the graph is 'ready', meaning that it is populated enough to work.
	 * <p>
	 * Essentially, if the object is ready to do things.
	 * 
	 * @param checkPath	If you want to check if we have a path.
	 * @return	If the object is set up.
	 */
	public boolean ready(boolean checkPath){
		if(!this.mainGraph.graphIsComplete()){
			return false;
		}
		if(!this.hasCurNode()){
			return false;
		}
		if(!this.hasDestNode()){
			return false;
		}
		if(checkPath & !this.hasPath()){
			return false;
		}
		return true;
	}//ready(boolean)
	
	/**
	 * Returns the closest node that is not complete. Null if it is complete.
	 * @return The closest node that is not complete. Null if it is complete.
	 */
	public LotNode getClosestNotCompleteNode(){
		if(!this.mainGraph.graphIsComplete()){
			try{
				LotNode destNodeHolder = this.getDestNode();
				ArrayList<LotNode> incompleteNodes = this.mainGraph.getIncompleteNodes();
			
				LotNode curClosestNode = null;
				LotPath tempPath = null;
				double curLowestMetric = Double.POSITIVE_INFINITY;
				for(LotNode curNode : incompleteNodes){
					this.setDestNode(curNode);
					tempPath = this.calcNewPathGivePath();
					if(tempPath.getPathMetric() < curLowestMetric){
						curClosestNode = curNode;
						curLowestMetric = tempPath.getPathMetric();
					}
				}
				this.setDestNode(destNodeHolder);
				return curClosestNode;
			}catch(BotLotException e){
				System.out.println("FATAL ERR- getClosestNotCompleteNode(). You should not get this. Error: " + e.getMessage());
				System.exit(1);
			}
		}
		return null;//to make errors go away
	}//getClosestNotCompleteNode()
	
	/**
	 * Sets the destination node to the closest incomplete node. Returns false if no incomplete node found, else true;
	 * 
	 * @return Returns false if no incomplete node found, else true.
	 */
	public boolean setClosestNotCompleteNode(){
		try{
			LotNode tempNode = this.getClosestNotCompleteNode();
			if(tempNode != null){
				this.setDestNode(tempNode);
				return true;
			}else{
				return false;
			}
		}catch(BotLotException e){
			System.out.println("FATAL ERR- setClosestNotCompleteNode(). You should not get this. Error: " + e.getMessage());
			System.exit(1);
		}
		return false;//to make errors go away
	}//setClosestNotCompleteNode()
	
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