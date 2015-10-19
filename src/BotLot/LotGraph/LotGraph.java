/**
Class LotGraph

@author Greg Stewart

Started: 10/7/15
Last Edit: 10/18/15

@version	1.0

**/
package BotLot.LotGraph;

/* imports*/
import java.util.Random;

public class LotGraph{	
    private LotNode curNode;//the node we are currently at
    private Random rand;//random number generator for making new id's
	private final String edgeIdPred = "BOTLOTEDGE";//the predicate of new edge id's
	private final String nodeIdPred = "BOTLOTNODE";//the predicate of new node id's
	private final int idSaltRange = Integer.MAX_VALUE;//the max value of new ids' salts
	
    //=========================================================================
    //    Constructors
    //region Constructors
    //=========================================================================

	/** LotGraph(LotNode)
     * 
     * Basic constructor, sets the initial node.
     * 
     * @param	nodeIn	The node to set the initial node to.
     */
    public LotGraph(LotNode nodeIn){
    	this();
    	this.setInitNode(nodeIn);
    }//LotGraph(LotNode)
    
	/** LotGraph()
     * 
     * Basic constructor
     */
    public LotGraph(){
    	this.curNode = null;
    	this.rand = new Random();
    }//LotGraph()
    
    //endregion
    

    //=========================================================================
    //    Setters
    //region Setters
    //=========================================================================
    
    /**
     * Sets the initial node, doesn't if already set
     * 
     * @param	nodeIn	The node we are setting the initial node to
     */
    public void setInitNode(LotNode nodeIn){
    	if(this.isEmpty()){
    		this.curNode = nodeIn;
    	}
    }//setInitNode
    
    /**
     * Creates an initial node
     */
    public void createInitNode(){
    	if(this.isEmpty()){
    		String newId =  this.getNewId("node");
    		LotNode newNode = new LotNode(newId);
    		this.setInitNode(newNode);
    	}
    }//createInitNode
    
    /**
     * Adds an edge to the current node
     * 
     * @param edgeIn	The edge to add to this node
     */
    public void addEdge(LotEdge edgeIn){
    	if(this.isNotEmpty()){
    		this.getCurNode().addEdge(edgeIn);
    	}
    }//addEdge
    
    /**
     * Removes this edge based on an edge object.
     * 
     * @param edgeIn	The edge to remove.
     */
    public void remEdge(LotEdge edgeIn){
    	if(this.isNotEmpty()){
    		edgeIn.clearNode(this.getCurNode());
    		this.getCurNode().remEdge(edgeIn);
    	}
    }//remEdge
    
    /**
     * Removes this edge based on the ID of an edge
     * 
     * @param edgeIn	The ID of the edge to remove.
     */
    public void remEdge(String edgeIdIn){
    	if(this.isNotEmpty()){
    		this.getCurNode().getEdge(edgeIdIn).clearNode(this.getCurNode());
    		this.getCurNode().remEdge(edgeIdIn);
    	}
    }//remEdge
    
    /**
     * Adds a new edge 
     * @return	The new edge object
     */
    public LotEdge newEdge(){
    	if(this.isNotEmpty()){
    		String newId =  this.getNewId("edge");
    		LotEdge newEdge = new LotEdge(newId);
    		newEdge.setNodeOne(this.getCurNode());
    		this.addEdge(newEdge);
    		return newEdge();
    	}else{
    		return null;
    	}
    }//newEdge
    
    /**
     * Adds a new edge and returns the 
     * @return The ID of the new edge
     */
    public String newEdgeString(){
    	LotEdge newEdge = this.newEdge();
    	if(newEdge != null){
    		return newEdge.getId();
    	}else{
    		return null;
    	}
    }//newEdge
    
    /**
     * Puts a new node at the other end of the edge provided
     * 
     * @param edgeToAddTo	The edge we are putting a new node on
     * @return The Node this created
     */
    public LotNode newNode(LotEdge edgeToAddTo){
    	if(this.isNotEmpty()){
    		String newId =  this.getNewId("node");
    		LotNode newNode = new LotNode(newId);
    		LotEdge edge = this.getEdge(edgeToAddTo);
    		if(edge != null){
    			this.addNode(edge, newNode);
    			return newNode;
    		}
    	}
    	return null;
    }//newNode(LotEdge)
    
    /**
     * Adds a new node to the end of an edge and returns the id 
     * @return The ID of the new edge
     */
    public String newNodeString(LotEdge edgeToAddTo){
    	LotNode newNode = this.newNode(edgeToAddTo);
    	if(newNode != null){
    		return newNode.getId();
    	}else{
    		return null;
    	}
    }//newNodeString
    
    /**
     * Sets a new node to the end of an edge
     * 
     * @param edgeIdToAddTo	The edge we are adding this to
     * @return	The new node
     */
    public LotNode newNode(String edgeIdToAddTo){
    	if(this.isNotEmpty()){
    		String newId = this.getNewId("node");
    		LotNode newNode = new LotNode(newId);
    		LotEdge edge = this.getEdge(edgeIdToAddTo);
    		if(edge != null){
    			this.addNode(edge, newNode);
    			return newNode;
    		}
    	}
    	return null;
    }//newNode(String)
    
    /**
     * Adds a new node to the end of an edge and returns the id 
     * @return The ID of the new edge
     */
    public String newNodeString(String edgeIdToAddTo){
    	LotNode newNode = this.newNode(edgeIdToAddTo);
    	if(newNode != null){
    		return newNode.getId();
    	}else{
    		return null;
    	}
    }//newNodeString
    
    /**
     * Adds a node to the end of the edge
     * @param edge	The edge that we are setting this node to
     * @param nodeToAdd	The mode we are adding
     */
    public void addNode(LotEdge edge, LotNode nodeToAdd){
    	if(edge == this.getCurNode().getEdge(edge)){
    		edge.setOtherNode(this.getCurNode(), nodeToAdd);
			nodeToAdd.addEdge(edge);
    	}
    }//addNode(LotEdge,LotNode)
    
    /**
     * Adds a node to the end of the edge
     * @param edgeId	The Id of the edge that we are setting this node to
     * @param nodeToAdd	The mode we are adding
     */
    public void addNode(String edgeId, LotNode nodeToAdd){
    	if(this.getCurNode().getEdge(edgeId) != null){
    		this.getCurNode().getEdge(edgeId).setOtherNode(this.getCurNode(), nodeToAdd);
			nodeToAdd.addEdge(this.getCurNode().getEdge(edgeId));
    	}
    }//addNode(String,LotNode)
    
    /**
     * Removes this node from the graph. Does this by removing all links to it.
     * 
     * @param nodeToRemove
     */
    public void remNode(LotNode nodeToRemove){
    	if(this.isNotEmpty()){
    		if(this.getCurNode() == nodeToRemove){
    			return;
    		}else{
    			//go through all other nodes to find this one, and remove it
    			//TODO: go through each edge in the node, null this node from them
    			for(int i = 0;i < nodeToRemove.getNumEdges();i++){
    				nodeToRemove.getEdges().get(i).clearNode(nodeToRemove);
    			}
    			nodeToRemove.clearEdges();
    			//TODO: deconstructor?
    		}
    	}
    	return;
    }
    
    
  //=========================================================================
    //    Getters
    //region Getters
    //=========================================================================
    
    private String getNewId(String idType){
    	String output = "";
    	switch(idType){
    	case "node":
    		output = nodeIdPred;
    		break;
    	case "edge":
    		output = edgeIdPred;
    		break;
    		default:
    			return null;
    	}
    	output += (new Integer(rand.nextInt(idSaltRange)).toString()) + (new Integer(rand.nextInt(idSaltRange)).toString());
    	return output;
    }
    /**
     * getCurNode()
     * 
     * Gets the current node
     * 
     * @return	The node we are currently at
     * 
     */
    public LotNode getCurNode(){
    	return this.curNode;
    }//getCurNode
    
    /**
     * checks to see if we have a current node
     * 
     */
    public boolean isEmpty(){
    	if(this.curNode == null){
    		return true;
    	}else{
    		return false;
    	}
    }//isEmpty
    
    /**
     * checks to see if we don't have a current node
     * 
     */
    public boolean isNotEmpty(){
    	return !this.isEmpty();
    }//isEmpty
    
    /**
     * Gets an edge from the current node.
     * 
     * @param edgeToGet	The edge that we are trying to get
     * @return
     */
    public LotEdge getEdge(LotEdge edgeToGet){
    	return this.getCurNode().getEdge(edgeToGet);
    }
    
    /**
     * Gets an edge from the current node.
     * 
     * @param edgeIdToGet The Id of the edge that we are trying to get
     * @return	
     */
    public LotEdge getEdge(String edgeIdToGet){
    	return this.getCurNode().getEdge(edgeIdToGet);
    }
    
    /**
     * Tests if this node has a specific edge
     * 
     * @param edgeIn	The edge we are testing for
     * @return	Whether of not the node has the edge
     */
    public boolean hasEdge(LotEdge edgeIn){
    	if(this.isNotEmpty()){
    		return this.getCurNode().hasEdge(edgeIn);
    	}
    	return false;
    }//hasEdge
    
    /**
     * Tests if this node has a specific edge
     * 
     * @param edgeIn	The Id of the edge we are testing for
     * @return	Whether of not the node has the edge
     */
    public boolean hasEdge(String edgeIdIn){
    	if(this.isNotEmpty()){
    		return this.getCurNode().hasEdge(edgeIdIn);
    	}
    	return false;
    }//hasEdge
    
    //endregion
    
}//class LotGraph
