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
import java.util.ArrayList;

public class LotGraph{	
    private ArrayList <LotNode> nodes;//the nodes we are currently have
    private ArrayList<ArrayList<LotEdge>> nodeEdges;//the edges for the nodes
    private Random rand;//random number generator for making new id's
    
    
    //stuff for semantics
	private final String edgeIdPred = "BOTLOTEDGE";//the predicate of new edge id's
	private final String nodeIdPred = "BOTLOTNODE";//the predicate of new node id's
	private final int idSaltRange = Integer.MAX_VALUE;//the max value of new ids' salts
	private boolean checkEdgeListSizeOnAddNodes = true;//flag to care edge array sizes 
	
    //=========================================================================
    //    Constructors
    //region Constructors
    //=========================================================================

	/** LotGraph(LotNode)
     * 
     * Basic constructor, sets the initial node.
     * 
     * @param	nodesIn	The nodes to set the initial nodes to.
     * @param	nodeEdgesIn	The edges to initially set.
     */
    public LotGraph(ArrayList<LotNode> nodesIn, ArrayList<ArrayList<LotEdge>> nodeEdgesIn){
    	this();
    	if(nodesIn.size() == nodeEdgesIn.size() && this.nodeEdgesAreSquare(nodeEdgesIn)){
    		this.checkEdgeListSizeOnAddNodes = false;
    		this.setNodes(nodesIn);
    		this.setNodeEdges(nodeEdgesIn);
    	}
    }//LotGraph(LotNode)
    
	/** LotGraph()
     * 
     * Basic constructor
     */
    public LotGraph(){
    	this.nodes = new ArrayList<LotNode>();
    	this.nodeEdges = new ArrayList<ArrayList<LotEdge>>();
    	this.rand = new Random();
    }//LotGraph()
    
    //endregion
    

    //=========================================================================
    //    Setters
    //region Setters
    //=========================================================================
    
    private void setNodes(ArrayList<LotNode> nodesIn){
    	if(checkEdgeListSizeOnAddNodes){
    		this.nodes = nodesIn;
    		this.setupEdges();
    	}
    }//setNodes
    
    /**
     * Sets the node edges to the edges given
     * 
     * @param nodeEdgesIn	The edges given
     */
    private void setNodeEdges(ArrayList<ArrayList<LotEdge>> nodeEdgesIn){
    	//loop through each node
    	for(int i = 0; i < this.getNodeEdges().size(); i++){
    		//loop through all connections that node has, setting the edges as it goes
    		for(int j = 0; j < this.getNodeEdges().get(i).size(); j++){
    			try{
    				this.setEdge(nodeEdgesIn.get(i).get(j), i, j);
    			}catch(IndexOutOfBoundsException err){
    				try{
    					this.setEdge( null, i, j);
    				}catch(LotGraphException err2){
    					System.out.println("FATAL-ERR- tried to access outside the size of the held info. This should not happen. Error:\n\t" + err.getMessage());
        				System.exit(1);
    				}
    			}catch(LotGraphException err){
    				System.out.println("FATAL-ERR- tried to access outside the size of the held info. This should not happen. Error:\n\t" + err.getMessage());
    				System.exit(1);
    			}
    		}//inner for loop
    	}//outer for loop
    }//setNodeEdges(ArrayList<ArrayList<LotEdge>> nodeEdgesIn)

    /**
     * Sets a node's edge based on the given variables
     * 
     * @param edgeIn	The edge to set to
     * @param fromNode	The node we are dealing with
     * @param toNode	The node to attach the thing to
     */
    public void setEdge(LotEdge edgeIn, LotNode fromNode, LotNode toNode) throws LotGraphException{
    	if(this.hasNode(fromNode) && this.hasNode(toNode)){
    		this.getNodeEdges().get(this.getNodeIndex(fromNode)).set(this.getNodeIndex(toNode), edgeIn);
    	}else{
    		if(!this.hasNode(fromNode) && !this.hasNode(toNode)){
    			throw new LotGraphException("Neither nodes entered are inside the stored data.");
    		}else if(!this.hasNode(fromNode)){
    			throw new LotGraphException("Node One is not inside the stored data.");
    		}else if(!this.hasNode(toNode)){
    			throw new LotGraphException("Node Two is not inside the stored data.");
    		}
    	}
    }//setEdge(LotEdge edgeIn, int nodeOne, int nodeTwo)
    
    public void setEdge(LotEdge edgeIn, String fromNodeId, String toNodeId) throws LotGraphException{
    	if(this.hasNode(fromNodeId) && this.hasNode(toNodeId)){
    		this.getNodeEdges().get(this.getNodeIndex(fromNodeId)).set(this.getNodeIndex(toNodeId), edgeIn);
    	}else{
    		if(!this.hasNode(fromNodeId) && !this.hasNode(toNodeId)){
    			throw new LotGraphException("Neither nodes entered are inside the stored data.");
    		}else if(!this.hasNode(fromNodeId)){
    			throw new LotGraphException("Node One is not inside the stored data.");
    		}else if(!this.hasNode(toNodeId)){
    			throw new LotGraphException("Node Two is not inside the stored data.");
    		}
    	}
    }
    
    /**
     * Sets a node's edge based on the given variables
     * 
     * @param edgeIn	The edge to set to
     * @param fromNodeIndex	The node we are dealing with
     * @param toNodeIndex	The node to attach the thing to
     */
    public void setEdge(LotEdge edgeIn, int fromNodeIndex, int toNodeIndex) throws LotGraphException{
    	if(this.getNodeListSize() <=  fromNodeIndex || this.getNodeListSize() <=  toNodeIndex){
    		this.getNodeEdges().get(fromNodeIndex).set(toNodeIndex, edgeIn); 
    	}else{
    		if(!(this.getNodeListSize() <=  fromNodeIndex) && !(this.getNodeListSize() <=  toNodeIndex)){
    			throw new LotGraphException("Neither nodeOne("+fromNodeIndex+")'s nor nodeTwo("+toNodeIndex+")'s indexes entered point inside the stored data (max: "+this.getNodeListSize()+").");
    		}else if(!(this.getNodeListSize() <= fromNodeIndex)){
    			throw new LotGraphException("nodeOne("+fromNodeIndex+") does not point inside the stored data (max: "+this.getNodeListSize()+").");
    		}else if(!(this.getNodeListSize() <= toNodeIndex)){
    			throw new LotGraphException("nodeTwo("+toNodeIndex+") does not point inside the stored data (max: "+this.getNodeListSize()+").");
    		}
    	}
    }//setEdge(LotEdge edgeIn, int nodeOne, int nodeTwo)
    
    public LotEdge createEdge(LotNode fromNode, LotNode toNode){
    	LotEdge newEdge = new LotEdge();
    	newEdge.setId(this.getNewUniqueId('e'));
    	try{
    		this.setEdge(newEdge, fromNode, toNode);
    	}catch(LotGraphException err){
    		System.out.println("FATAL-ERROR- createNode(). You should not get this error. Message: " + err.getMessage());
    		System.exit(1);
    	}
    	return newEdge;
    }
    
    public String createEdgeGiveString(LotNode fromNode, LotNode toNode){
    	return this.createEdge(fromNode, toNode).getId();
    }
    
    public LotEdge createEdge(String fromNodeId, String toNodeId){
    	LotEdge newEdge = new LotEdge();
    	newEdge.setId(this.getNewUniqueId('e'));
    	try{
    		this.setEdge(newEdge, fromNodeId, toNodeId);
    	}catch(LotGraphException err){
    		System.out.println("FATAL-ERROR- createNode(). You should not get this error. Message: " + err.getMessage());
    		System.exit(1);
    	}
    	return newEdge;
    }
    
    public String createEdgeGiveString(String fromNodeId, String toNodeId){
    	return this.createEdge(fromNodeId, toNodeId).getId();
    }
    
    public LotEdge createEdge(int fromNodeIndex, int toNodeIndex){
    	LotEdge newEdge = new LotEdge();
    	newEdge.setId(this.getNewUniqueId('e'));
    	try{
    		this.setEdge(newEdge, fromNodeIndex, toNodeIndex);
    	}catch(LotGraphException err){
    		System.out.println("FATAL-ERROR- createNode(). You should not get this error. Message: " + err.getMessage());
    		System.exit(1);
    	}
    	return newEdge;
    }
    
    public String createEdgeGiveString(int fromNodeIndex, int toNodeIndex){
    	return this.createEdge(fromNodeIndex, toNodeIndex).getId();
    }
    
    public void removeEdge(LotNode fromNode, LotNode toNode) throws LotGraphException{
    	if(this.hasNode(fromNode) && this.hasNode(toNode)){
    		this.getNodeEdges().get(this.getNodeIndex(fromNode)).set(this.getNodeIndex(toNode), null);
    	}else{
    		if(!this.hasNode(fromNode) && !this.hasNode(toNode)){
    			throw new LotGraphException("Neither fromNode nor toNode is within the stored data.");
    		}else if(!this.hasNode(fromNode)){
    			throw new LotGraphException("fromNode is not within the stored data.");
    		}else if(!this.hasNode(toNode)){
    			throw new LotGraphException("toNode is not within the stored data.");
    		}
    	}
    }
    public void removeEdge(String fromNodeId, String toNodeId) throws LotGraphException{
    	if(this.hasNode(fromNodeId) && this.hasNode(toNodeId)){
    		this.getNodeEdges().get(this.getNodeIndex(fromNodeId)).set(this.getNodeIndex(toNodeId), null);
    	}else{
    		if(!this.hasNode(fromNodeId) && !this.hasNode(toNodeId)){
    			throw new LotGraphException("Neither fromNodeId nor toNodeId is within the stored data.");
    		}else if(!this.hasNode(fromNodeId)){
    			throw new LotGraphException("fromNodeId is not within the stored data.");
    		}else if(!this.hasNode(toNodeId)){
    			throw new LotGraphException("toNodeId is not within the stored data.");
    		}
    	}
    }
    public void removeEdge(int fromNodeIndex, int toNodeIndex) throws LotGraphException{
    	if(this.hasNode(fromNodeIndex) && this.hasNode(toNodeIndex)){
    		this.getNodeEdges().get(fromNodeIndex).set(toNodeIndex, null);
    	}else{
    		if(!this.hasNode(fromNodeIndex) && !this.hasNode(toNodeIndex)){
    			throw new LotGraphException("Neither fromNodeIndex nor toNodeIndex is within the stored data.");
    		}else if(!this.hasNode(fromNodeIndex)){
    			throw new LotGraphException("fromNodeIndex is not within the stored data.");
    		}else if(!this.hasNode(toNodeIndex)){
    			throw new LotGraphException("toNodeIndex is not within the stored data.");
    		}
    	}
    }
    
    
    /**
     * Adds a new node to the set, dealing with the sizing of the edges and keeping things square
     * 
     * @param newNode	The node to add
     * @throws LotGraphException	If the new node has the same ID of a node already present
     */
    public void addNode(LotNode newNode) throws LotGraphException{
    	if(this.idIsUnique('n', newNode.getId())){
    		this.getNodes().add(newNode);    		
    		this.getNodeEdges().add(new ArrayList<LotEdge>());//add to the list
    		int nodeEdgeSize = this.getNodeListSize();
    		//increase the graph size
    		for(int i = 0; i < nodeEdgeSize; i++){
    			this.getNodeEdges().get(nodeEdgeSize - 1).add(null);//null out the new row
    			this.getNodeEdges().get(i).add(null);//add a null edge to the rest of the rows
    		}
    	}else{
    		throw new LotGraphException("Node entered has duplicate ID("+newNode.getId()+").");
    	}
    }//addNode(LotNode newNode)
    
    
    public LotNode createNode(){
    	LotNode newNode = new LotNode();
    	newNode.setId(this.getNewUniqueId('n'));
    	try{
    		this.addNode(newNode);
    	}catch(LotGraphException err){
    		System.out.println("FATAL-ERROR- createNode(). You should not get this error. Message: " + err.getMessage());
    		System.exit(1);
    	}
    	return newNode;
    }
    
    public String createNodeGiveString(){
    	return this.createNode().getId();
    }
    
    public void removeNode(LotNode nodeToRemove) throws LotGraphException{
    	if(this.hasNode(nodeToRemove)){
    		int nodeIndex = this.getNodeIndex(nodeToRemove);
    		this.getNodes().remove(nodeIndex);
    		this.getNodeEdges().remove(nodeIndex);
    		for(int i = 0; i < this.getNodeListSize(); i++){
    			this.getNodeEdges().get(i).remove(nodeIndex);
    		}
    	}else{
    		throw new LotGraphException("The node given is not within sotred data.");
    	}
    }
    public void removeNode(String nodeToRemoveId) throws LotGraphException{
    	if(this.hasNode(nodeToRemoveId)){
    		int nodeIndex = this.getNodeIndex(nodeToRemoveId);
    		this.getNodes().remove(nodeIndex);
    		this.getNodeEdges().remove(nodeIndex);
    		for(int i = 0; i < this.getNodeListSize(); i++){
    			this.getNodeEdges().get(i).remove(nodeIndex);
    		}
    	}else{
    		throw new LotGraphException("The node given is not within sotred data.");
    	}
    }
    public void removeNode(int nodeToRemoveId) throws LotGraphException{
    	if(this.hasNode(nodeToRemoveId)){
    		this.getNodes().remove(nodeToRemoveId);
    		this.getNodeEdges().remove(nodeToRemoveId);
    		for(int i = 0; i < this.getNodeListSize(); i++){
    			this.getNodeEdges().get(i).remove(nodeToRemoveId);
    		}
    	}else{
    		throw new LotGraphException("The node given is not within sotred data.");
    	}
    }
    
    //=========================================================================
    //    Getters
    //region Getters
    //=========================================================================
    
    /**
     * Gets the node list.
     * @return	The node list.
     */
    public ArrayList<LotNode> getNodes(){
    	return this.nodes;
    }//getNodes()
    
    /**
     * Gets the current size of the list of nodes.
     * 
     * @return	The size of the node list.
     */
    public int getNodeListSize(){
    	return this.getNodeListSize();
    }//getNodeListSize()
    
    /**
     * Gets the specified node.
     * 
     * @param nodeIn	The node we are getting.
     * @return	The node in, if found. Null if not found.
     */
    public LotNode getNode(LotNode nodeIn){
    	LotNode foundNode = null;
    	
    	if(this.getNodes().contains(nodeIn)){
    		return nodeIn;
    	}
    	
    	return foundNode;
    }//getNode(LotNode)
    
    /**
     * Gets the specified node.
     * 
     * @param nodeIdIn	The ID of the node we are getting.
     * @return	The node found. Null if nothing found.
     */
    public LotNode getNode(String nodeIdIn){
    	for(int i = 0; i < this.getNodeListSize(); i++){
    		if(this.getNodes().get(i).getId().equals(nodeIdIn)){
    			return this.getNodes().get(i);
    		}
    	}
    	return null;
    }//getNode(String)
    
    /**
     * Gets the node at the specified index.
     * 
     * @param nodeIndexIn	The index of the node we are trying to get.
     * @return	The node found. Throws exception if the index is out of bounds.
     * @throws  LotGraphException	If the index is out of bounds.
     */
    public LotNode getNode(int nodeIndexIn) throws LotGraphException{
    	if(this.getNodeListSize() > nodeIndexIn){
    		return this.getNodes().get(nodeIndexIn);
    	}else{
    		throw new LotGraphException("The given index is out of bounds of the stored data.");
    	}
    }//getNode(int)
    
    /**
     * Gets the index of the specified node in the list
     * 
     * @param nodeIn	The node to find
     * @return	The index of the node, if found. -1 if not found.
     */
    public int getNodeIndex(LotNode nodeIn){
    	return this.getNodes().indexOf(nodeIn);
    }//getNodeIndex(LotNode)
    
    /**
     * Gets the index of the specified node in the list
     * 
     * @param nodeIdIn	The Id of the node to find.
     * @return	The index of the node, if found. -1 if not found.
     */
    public int getNodeIndex(String nodeIdIn){
    	if(this.hasNode(nodeIdIn)){
    		for(int i = 0; i < this.getNodeListSize(); i++){
    			if(this.getNodes().get(i).equals(nodeIdIn)){
    				return i;
    			}
    		}
    	}
    	return -1;
    }//getNodeIndex(String)
    
    /**
     * Determines if the node given is present.
     * 
     * @param nodeIn	The node we are dealing with.
     * @return	If the node is present or not.
     */
    public boolean hasNode(LotNode nodeIn){
    	if(this.getNode(nodeIn) != null){
    		return true;
    	}
    	return false;
    }//hasNode(LotNode)
    
    /**
     * Determines if there is a node present with the given ID.
     * 
     * @param nodeIdIn	The id of the node to deal with.
     * @return	If the node is present or not.
     */
    public boolean hasNode(String nodeIdIn){
    	if(this.getNode(nodeIdIn) != null){
    		return true;
    	}
    	return false;
    }//hasNode(String)
    
    /**
     * Determines if there is a node present at the given index.
     * 
     * @param nodeIndexIn	The index of the node.
     * @return	If the node is present or not.
     */
    public boolean hasNode(int nodeIndexIn){
    	try{
    		this.getNode(nodeIndexIn);
    	}catch(LotGraphException err){
    		return false;
    	}
    	return true;
    }//hasNode(int)
    
    /**
     * Gets the two dimensional array of edges.
     * 
     * @return	The two dimensional array of edges.
     */
    public ArrayList<ArrayList<LotEdge>> getNodeEdges(){
    	return this.nodeEdges;
    }//getNodeEdges()
    
    /**
     * Gets an arrayList of initialized edges in the matrix
     * 
     * @return	The ArrayList of initialized edges.
     */
    public ArrayList<LotEdge> getEdgeList(){
    	ArrayList<LotEdge> edgeList = new ArrayList<LotEdge>();
    	for(int i = 0; i < this.getNodeListSize(); i++){
    		for(int j = 0; j < this.getNodeListSize(); j++){
    			try{
    				if(this.hasEdgeFromTo(i, j)){
    					edgeList.add(this.getEdgeFromTo(i, j));
    				}
    			}catch(LotGraphException err){
    				System.out.println("FATAL-ERROR- getEdgeList(). You should not get this error. Message: " + err.getMessage());
    			}
    		}
    	}
    	return edgeList;
    }//getEdgeList()
    
    /**
     * Gets the number of edges currently set
     * 
     * @return	The number of edges currently set
     */
    public long getNumEdges(){
    	return this.getEdgeList().size();
    }//getNumEdges()

    /**
     * Gets the specified edge.
     * 
     * @param edgeIn	The edge in question.
     * @return	The edge, null if not found
     */
    public LotEdge getEdge(LotEdge edgeIn){
    	ArrayList<LotEdge> tempEdgeList = this.getEdgeList();
    	for(int i = 0; i < tempEdgeList.size(); i++){
    		if(tempEdgeList.get(i) == edgeIn){
    			return tempEdgeList.get(i);
    		}
    	}
    	return null;
    }//getEdge(LotEdge)
    
    /**
     * Gets the edge with the specified ID.
     * 
     * @param edgeIdIn	The ID of the node to find
     * @return	The edge, null if not found
     */
    public LotEdge getEdge(String edgeIdIn){
    	ArrayList<LotEdge> tempEdgeList = this.getEdgeList();
    	for(int i = 0; i < tempEdgeList.size(); i++){
    		if(tempEdgeList.get(i).getId().equals(edgeIdIn)){
    			return tempEdgeList.get(i);
    		}
    	}
    	return null;
    }//getEdge(String)
    
    /**
     * Gets the edge at the specified index of the generated Edge list
     * 
     * @param edgeIndex	The index of the edge to get.
     * @return	The edge at the index.
     * @throws LotGraphException	If the index is out of bounds.
     */
    public LotEdge getEdge(int edgeIndex) throws LotGraphException{
    	ArrayList<LotEdge> tempEdges = this.getEdgeList();
    	if(tempEdges.size() > edgeIndex){
    		return tempEdges.get(edgeIndex);
    	}else{
    		throw new LotGraphException("edgeIndex("+edgeIndex+") is out of bounds (max: "+tempEdges.size()+"");
    	}
    }//getEdge(int)
    
    /**
     * Gets the index of a specified edge.
     * 
     * @param edgeIn	The edge to find.
     * @return	The index of the edge, if found. -1 if not found.
     */
    public int getEdgeIndex(LotEdge edgeIn){
    	ArrayList<LotEdge> tempEdgeList = this.getEdgeList();
    	for(int i = 0; i < tempEdgeList.size(); i++){
    		if(tempEdgeList.get(i) == edgeIn){
    			return i;
    		}
    	}
    	return -1;
    }//getEdgeIndex(LotEdge)
    
    /**
     * Gets the index of a specified edge.
     * 
     * @param edgeIdIn	The Id of the edge to find.
     * @return	The index of the edge, if found. -1 if not found.
     */
    public int getEdgeIndex(String edgeIdIn){
    	ArrayList<LotEdge> tempEdgeList = this.getEdgeList();
    	for(int i = 0; i < tempEdgeList.size(); i++){
    		if(tempEdgeList.get(i).getId().equals(edgeIdIn)){
    			return i;
    		}
    	}
    	return -1;
    }//getEdgeIndex(string)
    
    /**
     * Tests if the edge given is present in the edges.
     * 
     * @param edgeIn	The edge to look for
     * @return	If the edge was found or not
     */
    public boolean hasEdge(LotEdge edgeIn){
    	if(this.getEdge(edgeIn) != null){
    		return true;
    	}
    	return false;
    }
    
    /**
     * Tests if there is an edge between the specified nodes.
     * 
     * @param edgeIdIn
     * @return
     */
    public boolean hasEdge(String edgeIdIn){
    	if(this.getEdge(edgeIdIn) != null){
    		return true;
    	}
    	return false;
    }//hasEdge(String)
    
    
    /**
     * Tests if there is an edge between the specified nodes.
     * 
     * @param fromNodeIndex	The index of the edge (the node where it begins)
     * @param toNodeIndex	The index of the edge (the node where it ends)
     * @return	If there is a node there or not
     * @throws LotGraphException	If either node cannot be found.
     */
    public boolean hasEdge(int edgeIndex) throws LotGraphException{
    	try{
    		if(this.getEdge(edgeIndex) != null){
    			return true;
    		}
    	}catch(LotGraphException err){
    		throw err;
    	}
		return false;
    }//hasEdge(int,int)
    
    /**
     * Gets the edge that goes from one node to another.
     * 
     * @param nodeOne	The first node.
     * @param nodeTwo	The second node.
     * @return	The edge that goes from the first node to the second node.
     * @throws LotGraphException	If either node cannot be found.
     */
    public LotEdge getEdgeFromTo(LotNode nodeOne, LotNode nodeTwo) throws LotGraphException{
    	if(this.hasNode(nodeOne) && this.hasNode(nodeTwo)){
    		try{
    			return this.getEdgeFromTo(this.getNodeIndex(nodeOne),this.getNodeIndex(nodeTwo));
    		}catch(LotGraphException err){
    			System.out.println("FATAL-ERROR- getEdgeFromTo(LotNode,LotNode)- you should not get this. Error: " + err.getMessage());
    			System.exit(1);
    		}
    	}else{
    		if(!this.hasNode(nodeOne) && !this.hasNode(nodeTwo)){
    			throw new LotGraphException("Neither nodeOne nor nodeTwo found in stored data.");
    		}else if(!this.hasNode(nodeOne)){
    			throw new LotGraphException("nodeOne not found in stored data.");
    		}else if(!this.hasNode(nodeTwo)){
    			throw new LotGraphException("nodeTwo not found in stored data.");
    		}else{
    			throw new LotGraphException("Unspecified error in getEdgeFromTo(LotNode,LotNode). You should not get this.");
    		}
    	}
    	return null;
    }//getEdgeFromTo(LotNode,LotNode)
    
    /**
     * Gets the edge that goes from one node to another.  
     * 
     * @param nodeOneId	The id of the first node.
     * @param nodeTwoId	The id of the second node.
     * @return	The edge that goes from nodeOne to nodeTwo.
     * @throws LotGraphException	If either node cannot be found.
     */
    public LotEdge getEdgeFromTo(String nodeOneId, String nodeTwoId) throws LotGraphException{
    	if(this.hasNode(nodeOneId) && this.hasNode(nodeTwoId)){
    		try{
    			return this.getEdgeFromTo(this.getNodeIndex(nodeOneId),this.getNodeIndex(nodeTwoId));
    		}catch(LotGraphException err){
    			System.out.println("FATAL-ERROR- getEdgeFromTo(LotNode,LotNode)- you should not get this. Error: " + err.getMessage());
    			System.exit(1);
    		}
    	}else{
    		if(!this.hasNode(nodeOneId) && !this.hasNode(nodeTwoId)){
    			throw new LotGraphException("Neither nodeOne nor nodeTwo found in stored data.");
    		}else if(!this.hasNode(nodeOneId)){
    			throw new LotGraphException("nodeOne not found in stored data.");
    		}else if(!this.hasNode(nodeTwoId)){
    			throw new LotGraphException("nodeTwo not found in stored data.");
    		}else{
    			throw new LotGraphException("Unspecified error in getEdgeFromTo(LotNode,LotNode). You should not get this.");
    		}
    	}
    	return null;
    }//getEdgeFromTo(String,String)
    
    /**
     * Gets the edge from one index to another. Wrapper for 'getEdge(int,int)', for method consistency.
     * Probably best to use 'getEdge(int,int)' for simplicity's sake.
     * 
     * @param fromNodeIndex	The index of the first node in the node list.
     * @param toNodeIndex	The index of the second node in the node list.
     * @return	The edge between the nodes.
     * @throws LotGraphException	If either node cannot be found.
     */
    public LotEdge getEdgeFromTo(int fromNodeIndex, int toNodeIndex) throws LotGraphException{
    	if(this.getNodeListSize() <= fromNodeIndex || this.getNodeListSize() <= fromNodeIndex){
    		return this.getNodeEdges().get(fromNodeIndex).get(toNodeIndex);
    	}else{
    		if(!(this.getNodeListSize() <=  fromNodeIndex) && !(this.getNodeListSize() <=  toNodeIndex)){
    			throw new LotGraphException("Neither fromNodeIndex("+fromNodeIndex+")'s nor toNodeIndex("+toNodeIndex+")'s indexes entered point inside the stored data (max: "+this.getNodeListSize()+").");
    		}else if(!(this.getNodeListSize() <= fromNodeIndex)){
    			throw new LotGraphException("fromNodeIndex("+fromNodeIndex+") does not point inside the stored data (max: "+this.getNodeListSize()+").");
    		}else if(!(this.getNodeListSize() <= toNodeIndex)){
    			throw new LotGraphException("toNodeIndex("+toNodeIndex+") does not point inside the stored data (max: "+this.getNodeListSize()+").");
    		}else{
    			throw new LotGraphException("WARNING-- Unexpected error occured in getEdge(). You should not get this error.");
    		}
    	}
    }//getEdgeFromTo(int,int)
    
    /**
     * Tests if there are edges going from one node to another
     * 
     * @param fromNodeIndex	The index of the first node in the node list
     * @param toNodeIndex	The index of the second node in the node list
     * @return	If there is an edge going from the first to the second node.
     * @throws LotGraphException	If either node cannot be found.
     */
    public boolean hasEdgeFromTo(int fromNodeIndex, int toNodeIndex) throws LotGraphException{
    	try{
    		if((this.getEdgeFromTo(fromNodeIndex, toNodeIndex) != null)){
    			return true;
    		}
    	}catch(LotGraphException err){
    		throw err;
    	}
		return false;
    }//hasEdge(int,int)
    
    /**
     * Determines if there is an edge from the first node to the second node.
     * 
     * @param fromNode	The first node.
     * @param toNode	The second node.
     * @return	If there is an edge going from the first to the second node.
     * @throws LotGraphException	If either node cannot be found.
     */
    public boolean hasEdgeFromTo(LotNode fromNode, LotNode toNode) throws LotGraphException{
    	try{
    		if(this.getEdgeFromTo(fromNode, toNode) != null){
    			return true;
    		}
    	}catch(LotGraphException err){
    		throw err;
    	}
    	return false;
    }//hasEdgeFromTo(LotNode,LotNode)
    
    /**
     * Determines if there is an edge from the first node to the second node.
     * 
     * @param fromNodeId	The ID of the first node.
     * @param toNodeId	The ID of the second node.
     * @return	If there is an edge going from the first to the second node.
     * @throws LotGraphException	If either node cannot be found.
     */
    public boolean hasEdgeFromTo(String fromNodeId, String toNodeId) throws LotGraphException{
    	try{
    		if(this.getEdgeFromTo(fromNodeId, toNodeId) != null){
    			return true;
    		}
    	}catch(LotGraphException err){
    		throw err;
    	}
    	return false;
    }//hasEdgeFromTo(String, String)
    
    /**
     * Checks if the two nodes have edges both ways.
     * 
     * @param nodeOneIndex	The index of the first node in the node list.
     * @param nodeTwoIndex	The index of the second node in the node list.
     * @return	If the nodes have an edge to each other or not.
     * @throws LotGraphException	If either of the nodes can't be found.
     */
    public boolean hasEdgeBothWays(int nodeOneIndex, int nodeTwoIndex) throws LotGraphException{
    	try{
    		if(this.hasEdgeFromTo(nodeOneIndex, nodeTwoIndex) && this.hasEdgeFromTo(nodeTwoIndex, nodeOneIndex)){
    			return true;
    		}
    	}catch(LotGraphException err){
    		throw err;
    	}
    	return false;
    }//hasEdgeBothWays(int, int)
    
    /**
     * Checks if the two nodes have edges both ways.
     * 
     * @param nodeOne	The first node.
     * @param nodeTwo	The second node.
     * @return	If the nodes have an edge to each other or not.
     * @throws LotGraphException	If either of the nodes can't be found.
     */
    public boolean hasEdgeBothWays(LotNode nodeOne, LotNode nodeTwo) throws LotGraphException{
    	try{
    		if(this.hasEdgeFromTo(nodeOne, nodeTwo) && this.hasEdgeFromTo(nodeTwo, nodeOne)){
    			return true;
    		}
    	}catch(LotGraphException err){
    		throw err;
    	}
    	return false;
    }//hasEdgeBothWays(LotNode, LotNode)
    
    /**
     * Checks if the two nodes have edges both ways.
     * 
     * @param nodeOneId	The first node's ID.
     * @param nodeTwoId	The second node's ID.
     * @return	If the nodes have an edge to each other or not.
     * @throws LotGraphException	If either of the nodes can't be found.
     */
    public boolean hasEdgeBothWays(String nodeOneId, String nodeTwoId) throws LotGraphException{
    	try{
    		if(this.hasEdgeFromTo(nodeOneId, nodeTwoId) && this.hasEdgeFromTo(nodeTwoId, nodeOneId)){
    			return true;
    		}
    	}catch(LotGraphException err){
    		throw err;
    	}
    	return false;
    }//hasEdgeBothWays(String, String)
    
    
    /**
     * checks to see if we have a current node
     * 
     */
    public boolean isEmpty(){
    	if(this.getNodeListSize() != 0){
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
    
    
    //endregion
    

      //=========================================================================
      //    Workers
      //region Workers
      //=========================================================================
    
    /**
     * Checks if the given nodeEdgesIn is square or not
     * 
     * @param nodeEdgesIn	The nodeEdges arrayList
     * @return	If the nodeEdges arrayList is square
     */
    private boolean nodeEdgesAreSquare(ArrayList<ArrayList<LotEdge>> nodeEdgesIn){
    	int sideOne = nodeEdgesIn.size();
    	
    	for(int i = 0;i < sideOne;i++){
    		if(nodeEdgesIn.get(i).size() != sideOne){
    			return false;
    		}
    	}
    	
    	return true;
    }//nodeEdgesAreSquare
    
    /**
     * Checks if the nodeEdgesIn is square or not
     * 
     * @return	If the nodeEdges arrayList is square
     */
    public boolean nodeEdgesAreSquare(){
    	return nodeEdgesAreSquare(this.getNodeEdges());
    }//nodeEdgesAreSquare
    
    /**
     * Sets up a square, empty nodeEdges based on the size of the node array
     */
    private void setupEdges(){
    	int nodeNum = this.getNodeListSize();
    	
    	this.nodeEdges = new ArrayList<ArrayList<LotEdge>>();
    	this.nodeEdges.ensureCapacity(nodeNum);
    	
    	//null all nodes
    	for(int i = 0; i < nodeNum; i++){
    		for(int j = 0; j < nodeNum; i++){
    			this.nodeEdges.get(i).add(null);
    		}
    	}
    }//setupEdges
    

    /**
     * Generates a new ID for new nodes or edges.
     * 
     * Does this by combining two random integers (ideally never getting the same one twice).
     * 
     * @param idType	The type of id to generate (node='n', edge='e').
     * @return	The new ID.
     */
    private String getNewId(char idType){
    	String output = "";
    	switch(idType){
    	case 'n':
    		output = nodeIdPred;
    		break;
    	case 'e':
    		output = edgeIdPred;
    		break;
    		default:
    			return null;
    	}
    	output += (new Integer(rand.nextInt(idSaltRange)).toString()) + (new Integer(rand.nextInt(idSaltRange)).toString());
    	return output;
    }//getNewId()
    
    private String getNewUniqueId(char idType){
    	String newId = getNewId(idType);
    	while(!this.idIsUnique(idType, newId)){
    		newId = getNewId(idType);
    	}
    	return newId;
    }
    
    public boolean idIsUnique(char idType, String idIn){
    	switch(idType){
    	case 'n':
    		for(int i = 0; i < this.getNodeListSize(); i++){
    			try{
    				if(this.getNode(i).getId().equals(idIn)){
    					return true;
    				}
    			}catch(LotGraphException err){
    				System.out.println("FATAL-ERROR- idIsUnique(). You should not get this error. Message: " + err.getMessage());
    				System.exit(1);
    			}
    		}
    		break;
    	case 'e':
    		ArrayList<LotEdge> tempEdgeList = this.getEdgeList();
    		for(int i = 0; i < tempEdgeList.size(); i++){
    			if(tempEdgeList.get(i).getId().equals(idIn)){
    				return true;
    			}
    		}
    		break;
    	}
    	return false;
    }
}//class LotGraph