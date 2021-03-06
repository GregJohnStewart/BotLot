package botLot.lotGraph;
import java.util.ArrayList;//for edges
import java.util.HashMap;//for attributes
import java.util.Collection;//for toString
import java.util.Iterator;//for toString
/**
 * LotNode.java
 * <p>
 * Sets up the node object for the BotLot graph structure.
 * <p>
 * Started: 10/7/15
 * 
 * @author Greg Stewart
 * @version 1.0 12/9/15
 */
public class LotNode{
	/** The Id of the node, required before setting edges */
    private String id;
    /** The edges this node has. */
    private ArrayList<LotEdge> edges;
    /** The number of edges this node is actually supposed to actually have.<p>Used to determine if graph is complete or not. (Defaults to {@link #UNDETERMINED_NUM_EDGES} ('-1')) */
    private int actNumEdges;
    /** User defined attributes of the node, defaults to empty */
	private HashMap<String,String> attributes;
	/** The default value given to {@link #actNumEdges}. Value always '-1'. */
	public static final int UNDETERMINED_NUM_EDGES = -1;
    
    //=========================================================================
    //    Constructors
    //region Constructors
    //=========================================================================

	/**
     * Constructor for taking in a LotNode.
     * 
     * @param nodeIn	The LotNode given.
     */
    public LotNode(LotNode nodeIn){
    	this(nodeIn.getId(), nodeIn.getEdges(), nodeIn.getActNumEdges(), nodeIn.attributes);
    }//LotNode(LotNode)
    
    /**
     * Constructor to initialize everything.
     * 
     * @param idIn	The Id to give {@link #id}.
     * @param edgesIn	The edges to give this node ({@link #edges}).
     * @param numEdgesIn	The number of edges to give {@link #actNumEdges}.
     * @param attsIn	The attributes to give {@link #attributes}.
     */
    public LotNode(String idIn, ArrayList<LotEdge> edgesIn, int numEdgesIn, HashMap<String,String> attsIn){
        this(idIn);
		this.setAtts(attsIn);
		this.setActNumEdges(numEdgesIn);
    }//LotNode(String idIn)
	
    /**
     * Constructor to initialize  {@link #id}.
     * 
     * @param   idIn    the Id to give {@link #id}.
     */
    public LotNode(String idIn){
        this();
        this.setId(idIn);
    }//LotNode(String idIn)

    /**
     * Empty constructor to initialize the node.
     * <p>
     *  {@link #actNumEdges} defaults to '-1'.
     * <p>
     * 
     * Will need an id set before adding any edges.
     */
    public LotNode(){
    	this.edges = new ArrayList<LotEdge>();
        this.attributes = new HashMap<String,String>();
        this.actNumEdges = -1;
    }//LotNode()
    
    //endregion
    
    //=========================================================================
    //    Setters
    //region Setters
    //=========================================================================
    
    /**
     * Sets {@link #id}.
     * <p>
     * Trusts that the Id being given is valid, and does not conflict with other Id's.
     *
     * @param   idIn    The new Id to set to.
     * @return	This node.
     */
    public LotNode setId(String idIn){
        this.id = idIn;
        return this;
    }//setId

    /**
     * Adds a list of edges. Breaks if the list is larger than {@link #actNumEdges}.
     * 
     * @param edgesIn	The edges to give this node.
     * @throws LotNodeException	If Edge list given larger than the actual number of edges.
     * @return	This node.
     */
    public LotNode setEdges(ArrayList<LotEdge> edgesIn) throws LotNodeException{
		if(edgesIn.size() > this.getActNumEdges() && this.getActNumEdges() > UNDETERMINED_NUM_EDGES){
			throw new LotNodeException("Edge list given larger than the actual number of edges.");
		}else{
			this.edges = edgesIn;
		}
		return this;
	}//setEdges(ArrayList<LotEdges>
    
    /**
     * Ads the set of edges to {@link #edges}. Breaks if the resulting list is larger than {@link #actNumEdges}.
     * 
     * @param edgesIn	The edges to give this node.
     * @throws LotNodeException	If the resulting list is larger than the actual number of edges.
     * @return	This node.
     */
    public LotNode addEdges(ArrayList<LotEdge> edgesIn) throws LotNodeException{
		if(this.getActNumEdges() > UNDETERMINED_NUM_EDGES & (edgesIn.size() + this.getNumEdges()) > this.getActNumEdges()){
			throw new LotNodeException("Edge list given larger than the actual number of edges.");
		}else{
			for(int i = 0; i < edgesIn.size(); i++){
				if(this.hasEdge(edgesIn.get(i))){
					throw new LotNodeException("At least one edge in the list is duplicate to one already present.");
				}else{
					this.addEdge(edgesIn.get(i));
				}
			}
		}
		return this;
	}//addEdges(ArrayList<LotEdges>
    
    /**
     * Adds an edge to the node. Breaks if the node list is already full.
     * 
     * @param edgeIn	The edge to add.
     * @throws LotNodeException	If the node list is already full.
     * @return	This node.
     */
	public LotNode addEdge(LotEdge edgeIn) throws LotNodeException{
		if(this.isFull()){
			throw new LotNodeException("Node is already full.");
		}else if(this.hasEdge(edgeIn)){
			throw new LotNodeException("Node already has given edge.");
		}else{
			this.edges.add(edgeIn);
		}
		return this;
	}//addEdges(LotEdge)
	
	/**
	 * Clears the edge list.
     * @return	This node.
	 */
	public LotNode clearEdges(){
		this.edges.clear();
		return this;
	}//clearEdges()
	
	/**
	 * Removes a specific edge from {@link #edges}.
	 * 
	 * @param edgeToRemove	The edge to remove.
     * @return	This node.
	 * @throws LotNodeException If the node given was not found in {@link #edges}.
	 */
	public LotNode remEdge(LotEdge edgeToRemove) throws LotNodeException{
		if(this.hasEdge(edgeToRemove)){
			this.edges.remove(edgeToRemove);
		}else{
			throw new LotNodeException("This edge is not in the set.");
		}
		return this;
	}//remEdge(LotEdge)
	
	/**
	 * Removes a specific edge from {@link #edges}.
	 * 
	 * @param edgeIdToRemove	The ID of the edge to remove.
     * @return	This node.
	 * @throws LotNodeException	If no edge with the given ID found in {@link #edges}.
	 */
	public LotNode remEdge(String edgeIdToRemove) throws LotNodeException{
		if(this.hasEdge(edgeIdToRemove)){
			this.edges.remove(this.getEdge(edgeIdToRemove));
		}else{
			throw new LotNodeException("No edge with the given ID found.");
		}
		return this;
	}//remEdge(String)
	
	/**
	 * Removes an edge based on the index given.
	 * 
	 * @param edgeIndexToRemove	The index of the node to remove.
     * @return	This node.
	 * @throws LotNodeException	If there is no edge on that index.
	 */
	public LotNode remEdge(int edgeIndexToRemove) throws LotNodeException{
		if(this.hasEdge(edgeIndexToRemove)){
			this.edges.remove(this.getEdge(edgeIndexToRemove));
		}else{
			throw new LotNodeException("No edge with the given ID found.");
		}
		return this;
	}//remEdge(int)
	
	/**
	 * Removes an edge that goes to the given node.
	 * 
	 * @param nodeIn	The ending node of the edge to remove.
     * @return	This node.
	 * @throws LotNodeException	If the node given is not present.
	 */
	public LotNode remEdgeTo(LotNode nodeIn) throws LotNodeException{
		if(this.hasEdgeTo(nodeIn)){
			try {
				this.remEdge(this.getEdgeTo(nodeIn));
			} catch (LotNodeException e) {
				System.out.println("FATAL ERROR- remEdgeTo(LotNode)- This should not happen. Error: " + e.getMessage());
				System.exit(1);
			}
		}else{
			throw new LotNodeException("No edges in data set point to given node.");
		}
		return this;
	}//hasEdgeTo(LotNode)
	
	/**
	 * Removes an edge that goes to the node with the given ID.
	 * 
	 * @param nodeIdIn	The ID of the ending node of the edge to remove.
     * @return	This node.
	 * @throws LotNodeException	If the specified node isn't an end node of any edge.
	 */
	public LotNode remEdgeTo(String nodeIdIn) throws LotNodeException{
		if(this.hasEdgeTo(nodeIdIn)){
			try {
				this.remEdge(this.getEdgeTo(nodeIdIn));
			} catch (LotNodeException e) {
				System.out.println("FATAL ERROR- remEdgeTo(String)- This should not happen. Error: " + e.getMessage());
				System.exit(1);
			}
		}else{
			throw new LotNodeException("No edges in data set point to given node.");
		}
		return this;
	}//remEdgeTo(String)

	/**
	 * Sets {@link #actNumEdges}.
	 * 
	 * @param numEdgesIn	The new number of edges.
     * @return	This node.
	 */
	public LotNode setActNumEdges(int numEdgesIn){
		this.actNumEdges = numEdgesIn;
		return this;
	}//setNumEdges(int)
	
	/**
     * Sets a new value of the {@link #attributes}. Adds new attribute if not there.
     * <p>
     * Use this to add or edit attributes.
     *
     * @param   attKeyIn    The attribute to add or edit.
     * @param   attValIn    The attribute's value.
     * @return	This node.
     */
    public LotNode setAtt(String attKeyIn, String attValIn){
        this.attributes.put(attKeyIn, attValIn);
        return this;
    }//setAtt(String, String)
    
    /**
     * Sets {@link #attributes} to a new set of attributes.
	 *
     * @param   attsIn    The attributes to set {@link #attributes}.
     * @return	This node.
     */
    public LotNode setAtts(HashMap<String, String> attsIn){
        this.attributes = attsIn;
        return this;
    }//setAtts(HashMap<String, String>)
	
	/** 
     * Removes an attribute from {@link #attributes}.
     *
     * @param   attKeyIn    The key of the attribute to remove.
     * @return	This node.
     */
    public LotNode remAtt(String attKeyIn){
        this.attributes.remove(attKeyIn);
        return this;
    }//setId(String)
    
    //endregion
	
	//=========================================================================
    //    Getters
    //region Getters
    //=========================================================================
	
	/**
     * Returns {@link #id}.
     * 
	 * @return	The id of this node.
	 */
	public String getId(){
		return this.id;
	}//getId()
	
	/**
	 * Gets the set of edges {@link #edges}.
	 * 
	 * @return	The set of edges {@link #edges}.
	 */
	public ArrayList<LotEdge> getEdges(){
		return this.edges;
	}//getEdges()
	
	/**
	 * Gets a list of completed edges in the set {@link #edges}.
	 * @return	A list of completed edges in the set {@link #edges}.
	 */
	public ArrayList<LotEdge> getCompEdges(){
		ArrayList<LotEdge> outputList = new ArrayList<LotEdge>();
		for(LotEdge curEdge : this.getEdges()){
			if(curEdge.endSet()){
				outputList.add(curEdge);
			}
		}
		return outputList;
	}//getCompEdges()

	/**
	 * Gets the number of edges in the set {@link #edges}.
	 * 
	 * @return	The number of edges in the set {@link #edges}.
	 */
	public int getNumEdges(){
		return this.getEdges().size();
	}//getNumEdges();
	
	/**
	 * Gets the number of completed edges in set {@link #edges}.
	 * 
	 * @return	The number of completed edges in the set {@link #edges}.
	 */
	public int getNumCompEdges(){
		int compCount = 0;
		for(LotEdge curEdge : this.getEdges()){
			if(curEdge.endSet()){
				compCount++;
			}
		}
		return compCount;
	}//getNumCompEdges()
	
	/**
	 * Determines if the node is full. Will never be full if {@link #actNumEdges} = {@link #UNDETERMINED_NUM_EDGES}
	 * <p>
	 * Does not check if those edges are actually connected to other nodes. See {@link #isFullAndConnected()} for that.
	 * 
	 * @return	If the node is full or not.
	 */
	public boolean isFull(){
		if(this.getActNumEdges() > UNDETERMINED_NUM_EDGES & this.getActNumEdges() == this.getNumEdges()){
			return true;
		}else{
			return false;
		}
	}//isFull()
	
	/**
	 * Determines if the node is full, and edges all have nodes. Will never be full if {@link #actNumEdges} = {@link #UNDETERMINED_NUM_EDGES}
	 * 
	 * @return	If the node is full or not, and all edges have an end node.
	 */
	public boolean isFullAndConnected(){
		if(this.isFull()){
			for(int i = 0; i < this.getNumEdges(); i++){
				if(!this.getEdges().get(i).endSet()){
					return false;
				}
			}
			return true;
		}else{
			return false;
		}
	}//isFullAndConnected()
	
	/**
	 * Gets the edge from {@link #edges}. Returns null if not found.
	 * 
	 * @param edgeToGet	The edge to return.
	 * @return	The edge given. Null if not found.
	 */
	public LotEdge getEdge(LotEdge edgeToGet){
		for(int i = 0; i < this.getNumEdges(); i++){
			if(this.edges.get(i) == edgeToGet){
				return this.edges.get(i);
			}
		}
		return null;
	}//getEdge(LotEdge)
	
	/**
	 * Gets an edge from {@link #edges} based on its ID.
	 * 
	 * @param edgeIdToGet	The id of the edge to get.
	 * @return	The edge with the given ID. Null if not found.
	 */
	public LotEdge getEdge(String edgeIdToGet){
		for(int i = 0; i < this.getNumEdges(); i++){
			if(this.edges.get(i).getId().equals(edgeIdToGet)){
				return this.edges.get(i);
			}
		}
		return null;
	}//getEdge(String)

	/**
	 * Gets the edge at the given index.
	 * 
	 * @param edgeIndexToGet	The index of the node to get.
	 * @return	The node at the given index.
	 * @throws IndexOutOfBoundsException	If the index does not point to a node.
	 */
	public LotEdge getEdge(int edgeIndexToGet){
		if(this.getActNumEdges() > UNDETERMINED_NUM_EDGES & this.getActNumEdges() > edgeIndexToGet && this.getNumEdges() > edgeIndexToGet){
			return this.getEdges().get(edgeIndexToGet);
		}else{
			throw new IndexOutOfBoundsException("Index given is out of range. \n\tIndex given: " + edgeIndexToGet + "\n\t# edges: " + this.getNumEdges() + "\n\tActual # edges: " + this.getActNumEdges());
		}
	}//getEdge(int)

	/**
	 * Determines if the given edge is already present in {@link #edges}.
	 * 
	 * @param edgeIn	The edge to find.
	 * @return	If the given edge is already present in {@link #edges}.
	 */
	public boolean hasEdge(LotEdge edgeIn){
		if(this.getEdge(edgeIn) != null){
			return true;
		}
		return false;
	}//hasEdge(LotEdge)

	/**
	 * Determines if there is already an edge with the given id in {@link #edges}.
	 * 
	 * @param edgeIdIn	The id to search off of.
	 * @return	If there is already an edge with the given id in {@link #edges}.
	 */
	public boolean hasEdge(String edgeIdIn){
		if(this.getEdge(edgeIdIn) != null){
			return true;
		}
		return false;
	}//hasEdge(String)
	
	/**
	 * Determines if there is already an edge at the given index in {@link #edges}.
	 * 
	 * @param edgeIndexIn	The index given.
	 * @return	If there is already an edge at the given index in {@link #edges}.
	 */
	public boolean hasEdge(int edgeIndexIn){
		try{
			LotEdge tempEdge = this.getEdge(edgeIndexIn);
			if(tempEdge != null){
				return true;
			}
		}catch(IndexOutOfBoundsException err){
			
		}
		return false;
	}//hasEdge(int)
	
	/**
	 * Gets the edge to a given node. Null if not found.
	 * 
	 * @param nodeIn	The end node we are trying to get to.
	 * @return	The edge going to the given node. Null if not found.
	 */
	public LotEdge getEdgeTo(LotNode nodeIn){
		for(LotEdge curEdge : this.getEdges()){
			if(curEdge.getEndNode() == nodeIn){
				return curEdge;
			}
		}
		return null;
	}//getEdgeTo(LotNode)
	
	/**
	 * Gets the edge to a node with the given ID. Null if not found.
	 * 
	 * @param nodeIdIn	The ID of the node we are trying to get to.
	 * @return	The edge going to the specified node. Null if not found.
	 */
	public LotEdge getEdgeTo(String nodeIdIn){
		for(LotEdge curEdge : this.getEdges()){
			if(curEdge.getEndNode().getId() == nodeIdIn){
				return curEdge;
			}
		}
		return null;
	}//getEdgeTo(String)
	
	/**
	 * Determined if there is an edge to the given node.
	 * 
	 * @param nodeIn	The node we are trying to get to.
	 * @return	If there is an edge to the given node or not.
	 */
	public boolean hasEdgeTo(LotNode nodeIn){
		if(this.getEdgeTo(nodeIn) != null){
			return true;
		}else{
			return false;
		}
	}//hasEdgeTo(LotNode)
	
	/**
	 * Determines if one of the edges' end node's ID is the id given.
	 * 
	 * @param nodeIdIn	The id to search for.
	 * @return	If the node has an edge that points to the node with the given ID.
	 */
	public boolean hasEdgeTo(String nodeIdIn){
		if(this.getEdgeTo(nodeIdIn) != null){
			return true;
		}else{
			return false;
		}
	}//hasEdgeTo(String)
	
	/**
	 * Gets a list of edges going to a specified node.
	 * 
	 * @param toNode	The node to get edges to.
	 * @return	A list of edges going to the specified node.
	 */
	public ArrayList<LotEdge> getEdgesTo(LotNode toNode){
		if(this.hasEdgeTo(toNode)){
			ArrayList<LotEdge> edgesToNode = new ArrayList<LotEdge>();
			for(LotEdge curEdge : this.getConnectedEdges()){
				if(curEdge.getEndNode() == toNode){
					edgesToNode.add(curEdge);
				}
			}//main loop
			return edgesToNode;
		}else{
			return new ArrayList<LotEdge>();
		}
	}
	
	/**
	 * Gets a list of edges going to a specified node.
	 * 
	 * @param toNodeId	The node to get edges to.
	 * @return	A list of edges going to the specified node.
	 */
	public ArrayList<LotEdge> getEdgesTo(String toNodeId){
		if(this.hasEdgeTo(toNodeId)){
			ArrayList<LotEdge> edgesToNode = new ArrayList<LotEdge>();
			for(LotEdge curEdge : this.getConnectedEdges()){
				if(curEdge.getEndNode().getId().equals(toNodeId)){
					edgesToNode.add(curEdge);
				}
			}//main loop
			return edgesToNode;
		}else{
			return new ArrayList<LotEdge>();
		}
	}
	
	/**
	 * Gets the number of edges that go to a specified node from this node.
	 * 
	 * @param toNode	The node to get the number of edges to.
	 * @return	The number of edges that go to a specified node from this node.
	 */
	public int getNumEdgesTo(LotNode toNode){
		return this.getEdgesTo(toNode).size();
	}
	
	/**
	 * Gets the number of edges that go to a specified node from this node.
	 * 
	 * @param toNodeId	The node to get the number of edges to.
	 * @return	The number of edges to the given node.
	 */
	public int getNumEdgesTo(String toNodeId){
		return this.getEdgesTo(toNodeId).size();
	}
	
	/**
	 * Gets the edge with the shortest metric to a specified node.
	 * 
	 * @param toNode	The node to get the shortest edge to.
	 * @return	The shortest edge to the given node.
	 * @throws LotPathException If there are no edges.
	 */
	public LotEdge getShortestEdgeTo(LotNode toNode) throws LotPathException{
		if(!this.hasEdgeTo(toNode)){
			throw new LotPathException("This node has no edge to the given node.");
		}
		LotEdge infSizeEdge = new LotEdge("",null,Double.POSITIVE_INFINITY, null);
		infSizeEdge.infSizeFlag = true;
		LotEdge shortestEdge = new LotEdge(infSizeEdge);
		for(LotEdge curEdge : this.getEdgesTo(toNode)){
			if(shortestEdge.isLonger(curEdge)){
				shortestEdge = curEdge;
			}
		}
		return shortestEdge;
	}
	
	/**
	 * Gets the edge with the shortest metric to a specified node.
	 * 
	 * @param toNodeId	The id of the node to get the shortest edge to.
	 * @return	The shortest edge to the given node.
	 * @throws LotPathException If there are no edges.
	 */
	public LotEdge getShortestEdgeTo(String toNodeId) throws LotPathException{
		if(!this.hasEdgeTo(toNodeId)){
			throw new LotPathException("This node has no edge to the given node.");
		}
		LotEdge infSizeEdge = new LotEdge("",null,Double.POSITIVE_INFINITY, null);
		infSizeEdge.infSizeFlag = true;
		LotEdge shortestEdge = new LotEdge(infSizeEdge);
		for(LotEdge curEdge : this.getEdgesTo(toNodeId)){
			if(shortestEdge.isLonger(curEdge)){
				shortestEdge = curEdge;
			}
		}
		return shortestEdge;
	}//getShortestEdgeTo(String)
	
	/**
	 * Gets the edge with the shortest metric to a specified node, excluding edges in the given set.
	 * 
	 * @param toNode	The destination node
	 * @param edgesToAvoid	Edges not to include.
	 * @return	The shortest edge to the given node.
	 * @throws LotPathException If there are no edges.
	 */
	public LotEdge getShortestEdgeTo(LotNode toNode, Collection<LotEdge> edgesToAvoid) throws LotPathException{
		if(!this.hasEdgeTo(toNode)){
			throw new LotPathException("This node has no edge to the given node.");
		}
		LotEdge infSizeEdge = new LotEdge("",null,Double.POSITIVE_INFINITY, null);
		infSizeEdge.infSizeFlag = true;
		LotEdge shortestEdge = new LotEdge(infSizeEdge);
		for(LotEdge curEdge : this.getEdgesTo(toNode)){
			if(shortestEdge.isLonger(curEdge) && !edgesToAvoid.contains(curEdge)){
				shortestEdge = curEdge;
			}
		}
		if(shortestEdge == infSizeEdge){
			throw new LotPathException("No edges to the given node that aren't in the given set to ignore.");
		}
		return shortestEdge;
	}
	
	/**
	 * Gets the edge with the shortest metric to a specified node, excluding edges in the given set.
	 * 
	 * @param toNodeId	The destination node
	 * @param edgesToAvoid	Edges not to include.
	 * @return	The shortest edge to the given node.
	 * @throws LotPathException If there are no edges.
	 */
	public LotEdge getShortestEdgeTo(String toNodeId, Collection<LotEdge> edgesToAvoid) throws LotPathException{
		if(!this.hasEdgeTo(toNodeId)){
			throw new LotPathException("This node has no edge to the given node.");
		}
		LotEdge infSizeEdge = new LotEdge("",null,Double.POSITIVE_INFINITY, null);
		infSizeEdge.infSizeFlag = true;
		LotEdge shortestEdge = new LotEdge(infSizeEdge);
		for(LotEdge curEdge : this.getEdgesTo(toNodeId)){
			if(shortestEdge.isLonger(curEdge) && !edgesToAvoid.contains(curEdge)){
				shortestEdge = curEdge;
			}
		}
		if(shortestEdge == infSizeEdge){
			throw new LotPathException("No edges to the given node that aren't in the given set to ignore.");
		}
		return shortestEdge;
	}
	
	/**
	 * Gets a list of the nodes this node is connected to.
	 * 
	 * @return	A list of the nodes this node is connected to.
	 */
	public ArrayList<LotNode> getConnectedNodes(){
		ArrayList<LotNode> connectedNodes = new ArrayList<LotNode>();
		for(LotEdge curEdge : this.getEdges()){
			if(curEdge.endSet()){
				connectedNodes.add(curEdge.getEndNode());
			}
		}
		return connectedNodes;
	}//getConnectedNodes()
	
	/**
	 * Gets a list of edges that are connected to other nodes.
	 * 
	 * @return	A list of edges that are connected to other nodes.
	 */
	public ArrayList<LotEdge> getConnectedEdges(){
		ArrayList<LotEdge> connectedEdges = new ArrayList<LotEdge>();
		for(LotEdge curEdge : this.getEdges()){
			if(curEdge.endSet()){
				connectedEdges.add(curEdge);
			}
		}
		return connectedEdges;
	}
	
	/**
	 * Gets a list of edges that are connected to other nodes, excluding those in the given list.
	 * 
	 * @param edgesToAvoid	The list of edges to not include in the returned list.
	 * @return	A list of edges this node holds that are connected and not in the given list.
	 */
	public ArrayList<LotEdge> getConnectedEdges(Collection<LotEdge> edgesToAvoid){
		ArrayList<LotEdge> tempList = this.getConnectedEdges();
		tempList.removeAll(edgesToAvoid);
		return tempList;
	}
	
	/**
	 * Gets the index of the given node. Returns '-1' if not present.
	 * 
	 * @param edgeIn	The node to get the index of.
	 * @return	The index of the given node. '-1' if not present.
	 */
	public int getEdgeIndex(LotEdge edgeIn){
		if(this.hasEdge(edgeIn)){
			return this.getEdges().indexOf(edgeIn);
		}
		return -1;
	}//getEdgeIndex(LotEdge)
	
	/**
	 * Gets the index of the node with the given ID. Returns '-1' if not present.
	 * 
	 * @param edgeIdIn	The ID of the node to get the index of.
	 * @return	The index of the node with the given ID. '-1' if not present.
	 */
	public int getEdgeIndex(String edgeIdIn){
		if(this.hasEdge(edgeIdIn)){
			return this.getEdges().indexOf(edgeIdIn);
		}
		return -1;
	}//getEdgeIndex(String)

	/**
	 * Gets {@link #actNumEdges}.
	 * 
	 * @return	The actual number of edges this node is supposed to have.
	 */
	public int getActNumEdges(){
		return this.actNumEdges;
	}//getNumEdges()

	/**
     * Returns the value of a key-value pair in {@link #attributes}.
     * 
	 * @param	attKey	The key of the attribute being retrieved.
	 * @return	The value at this key.
	 */
	public String getAtt(String attKey){
		return this.attributes.get(attKey);
	}//getAtt
	
	/**
	 * Returns the attributes ({@link #attributes}) for this Node.
	 * 
	 * @return {@link #attributes}
	 */
	public HashMap<String, String> getAtts(){
		return attributes;
	}//getAtts()
	
	/**
	 * Tests if there is a key in {@link #attributes}.
	 * 
	 * @param	attKey	The key of the value to see exists.
	 * @return	If the key-value pair exists.
	 */
	public boolean hasAtt(String attKey){
		return this.attributes.containsKey(attKey);
	}//hasAtt(String)
	
	/**
	 * Tests if there is a specific key-value pair in {@link #attributes}.
	 * 
	 * @param attKey	The key we are looking for.
	 * @param attVal	The value we are looking for.
	 * @return	If that key-value pair is present.
	 */
	public boolean hasAtt(String attKey, String attVal){
		if(this.hasAtt(attKey)){
			if(attVal == null){
				return true;
			}else if(this.getAtt(attKey).equals(attVal)){
				return true;
			}
		}
		return false;
	}//hasAtt(String,String)
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final int maxLen = 10;
		return "LotNode [id=" + id + ", actNumEdges=" + actNumEdges + ", attributes=" + (attributes != null ? toString(attributes.entrySet(), maxLen) : null)
				+ ", edges=" + (edges != null ? toString(edges, maxLen) : null) + "]";
	}//toString

	private String toString(Collection<?> collection, int maxLen) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		int i = 0;
		for (Iterator<?> iterator = collection.iterator(); iterator.hasNext() && i < maxLen; i++) {
			if (i > 0)
				builder.append(", ");
			builder.append(iterator.next());
		}
		builder.append("]");
		return builder.toString();
	}//toString(Collection<?>, int)

}//class LotNode
