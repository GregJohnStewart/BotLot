/**
 * BotLot.java
 * 
 * Holds the graph object and handles big operations like shortest path algorithms
 * 
 * 
 */
package BotLot;

import java.util.ArrayList;
import java.util.LinkedList;
import BotLot.LotGraph.*;

public class BotLot{
	public LotGraph mainGraph;//the main graph
	private LotNode curNode;//the node we are currently at in the graph
	private LinkedList<LotEdge> curPath;//TODO:: change current system of deleting as we go along to simply keeping an index placeholder?
	

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
		this.mainGraph = lotIn.mainGraph;
		//just set the node, we know that it is valid from the nature of the object
		this.curNode = lotIn.getCurNode();
		this.curPath = lotIn.getCurPath();
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
	 * Empty constructor. Nulls the 
	 * @throws BotLotException Shouldn't do this.
	 */
	public BotLot(){
		this.setGraph(new LotGraph());
		this.clearCurPath();
		try{
			this.setCurNode(null);
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
	
	public void setGraph(LotGraph graphIn){
		this.mainGraph = graphIn;
		if(!this.mainGraph.hasNode(this.getCurNode())){
			this.clearCurNode();
		}
	}
	
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
	}
	
	public void clearCurNode(){
		try{
			this.setCurNode(null);
		}catch(BotLotException err){
			System.out.println("FATAL ERROR- clearCurNode()- Unable to do clear current node. You should not get this. Error: " + err.getMessage());
			System.exit(1);
		}
	}
	
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
	 * Adds an edge between the current node and the node given.
	 * 
	 * @param nodeToIndexIn	The index of the node the edge is going to.
	 * @param edgeIn	The edge to add.
	 * @throws BotLotException	If either the current node or the node given cannot be found
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
	 * Removes an edge from the current node
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
	 * Removes an edge from the current node
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
	 * Removes an edge from the current node.
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
	 * Sets the current path we have for the one using this object to go through. 
	 * <p>
	 * TODO:: performance is going to be an issue here. Also need to check for node continuity.
	 * 
	 * @param pathIn	The path we are trying to set to.
	 * @throws LotGraphException	If something went wrong with edge checking.
	 */
	public void setCurPath(LinkedList<LotEdge> pathIn) throws BotLotException{
		this.curPath = pathIn;
		if(!this.curPathIsValid()){
			this.clearCurPath();
			throw new BotLotException("Path given is not valid; is not continuous for this data.");
		}
	}//setCurPath(LinkedList<LotEdge>)
	
	/**
	 * Adds an edge to the edge path.
	 * 
	 * @param edgeIn	The edge we are adding.
	 * @throws BotLotException 
	 */
	public void addToPath(LotEdge edgeIn) throws BotLotException{
		if(this.mainGraph.hasEdge(edgeIn)){
			this.curPath.addLast(edgeIn);
			if(!curPathIsValid()){
				this.curPath.removeLast();
				throw new BotLotException("Edge given does not make for a contiuous path.");
			}
		}else{
			throw new BotLotException("Edge given does not exist in data.");
		}
	}//addToPath(edgeIn)
	
	public void movedThroughPath() throws BotLotException{
		if(!this.getCurPath().isEmpty()){
			try{
				this.setCurNode(this.mainGraph.getOtherNode(this.getCurNode(), this.getCurPath().removeFirst()));
			}catch(LotGraphException err){
				System.out.println("FATAL ERR- movedThroughPath(). This should not happen. Error: " + err.getMessage());
				System.exit(1);
			}
		}else{
			throw new BotLotException("Current path is empty or reached end of current path.");
		}
	}
	
	public void movedThroughPath(int numSteps) throws BotLotException{
		if(!this.getCurPath().isEmpty()){
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
	}
	
	public void movedToEndOfPath() throws BotLotException{
		try{
			this.movedThroughPath(this.getCurPath().size());
		}catch(BotLotException err){
			System.out.println("FATAL ERR- movedToEndOfPath(). This should not happen. Error: " + err.getMessage());
			System.exit(1);
		}
	}
	
	public void clearCurPath(){
		this.curPath = new LinkedList<LotEdge>();
	}
	
	//endregion

    //=========================================================================
    //    Getters
    //region getters
    //=========================================================================
	
	
	/**
	 * Gets the node we are currently at
	 * 
	 * @return	The node we are currently at in the Graph
	 */
	public LotNode getCurNode(){
		return this.curNode;
	}//getCurNode();
	
	/**
	 * Gets the edges that this node has going out from it.
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
	 * Determines if BotLot knows where we are
	 * 
	 * @return	If BotLot has a currentNode	
	 */
	public boolean hasCurNode(){
		if(this.getCurNode() != null){
			return true;
		}
		return false;
	}//hasCurNode()
	
	/**
	 * Determines if the graph data has the node we currently have.
	 * 
	 * @return	If the graph has the current node
	 */
	public boolean graphHasCurNode(){
		return this.mainGraph.hasNode(this.getCurNode());
	}//graphHasCurNode()
	
	/**
	 * Determines if the current node has any edges.
	 * 
	 * @return	If the current node has any edges or not.
	 * @throws BotLotException If the current node is not in the Graph
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
	 * Gets the complete list of steps list in the path left.
	 * 
	 * @return	The path currently set.
	 */
	public LinkedList<LotEdge> getCurPath(){
		return this.curPath;
	}//getCurPath()
	
	/**
	 * Checks the continuity of the path list.
	 * <p>
	 * I.E., if all the edges lead through nodes that lead to the other nodes correctly.
	 * 
	 * @return	If the path is valid or not.
	 */
	public boolean curPathIsValid(){
		LotNode tempNode = this.getCurNode();
		for(LotEdge tempEdge : this.getCurPath()){
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
	}
	
	public void calcNewPath(LotNode destNode){
		try{
			this.setCurPath(BotLotWorkers.getShortestPath(this,destNode));
		}catch(BotLotException err){
			System.out.println("FATAL ERR- calcNewPath(). This should not happen. Error: " + err.getMessage());
			System.exit(1);
		}
	}
	
	//endregion
	
	
	
	
	
	
	
	
}//class BotLot
