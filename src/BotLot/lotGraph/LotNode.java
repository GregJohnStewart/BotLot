/**
LotNode.java

Sets up the Node object for the BotLot graph structure.

@author Greg Stewart

Started: 10/7/15
Last Edit: 10/17/15

@version 1.0

**/

/* imports*/
import java.util.Arrays;//for the edges
import java.util.HashMap;//for attributes

public class LotNode{
    private String id;//the Id of the node, required before setting edges
    private long metric;//the metric for the node, defaults to 0
    private HashMap<String,String> attributes;//user defined attributes of the node, defaults to empty
    private ArrayList<lotEdge> edges//edges attached to this node, default empty
    
    //=========================================================================
    //    Constructors
    //region Constructors
    //=========================================================================
    
	/** LotNode(String idIn)
     *
     * Constructor to initialize the Id.
     * 
     * @param   idIn    		the Id to give the node
     * @param   metricIn    the metric to give the node
     * @param   attsIn    	the attributes to give the node
     * @param   edges	    	the edges to give the node
     */
    public void LotNode(String idIn, long metricIn, HashMap<String,String> attsIn, ArrayList<LotEdge> edgesIn){
        this.LotNode(idIn);
		this.setMetric(metricIn);
		this.setAtts(attsIn);
		this.setEdges(edgesIn);
    }//LotNode(String idIn)
	
    /** LotNode(String idIn)
     *
     * Constructor to initialize the Id.
     * 
     * @param   idIn    the Id to give the node
     */
    public void LotNode(String idIn){
        this.LotNode();
        this.setId(idIn);
    }//LotNode(String idIn)

    /** LotNode()
     *
     * empty constructor to initialize the node
     * <p>
     * Will need an id set before adding any edges
     */
    public void LotNode(){
        this.attributes = new HashMap<String,String>;
        this.metric = 0;
        this.edges = new ArrayList<lotEdge>;
    }//LotNode()
    
    //endregion
    
    //=========================================================================
    //    Setters
    //region Setters
    //=========================================================================
    
    /** setId(String idIn)
     * 
     * Sets the node's id.
     * <p>
     * Trusts that the Id being given is valid, and does not conflict with other Id's
     * TODO: error check for this by going through all nodes currently in contact with?
     *
     * @param   idIn    The new Id to set to.
     */
    public void setId(String idIn){
        this.id = idIn;
    }//setId
    
    /** setMetric(long metricIn)
     * 
     * Sets the node's metric.
     *
     * @param   metricIn    The new metric of the node
     */
    public void setMetric(long metricIn){
        this.metric = metricIn;
    }//setMetric

    /** setAtt(String attKeyIn, String attValIn)
     * 
     * Sets a new value of the attribute in. Adds new attribute if not there
     * <p>
     * Use this to add or edit attributes
     *
     * @param   attKeyIn    The attribute to add or edit
     * @param   attValIn    The attribute's value
     */
    public void setAtt(String attKeyIn, String attValIn){
        this.attributes.put(attKeyIn, attValIn);
    }//setAtt

    /** setAtts(HashMap<String, String> attsIn)
     * 
     * Sets a new value of the attribute in. Adds new attribute if not there
	 *
     * @param   attsIn    The attributes to set this Node's attributes to
     */
    public void setAtts(HashMap<String, String> attsIn){
        this.attributes = attsIn;
    }//setAtts
	
	/** remAtt(String attKeyIn, String attValIn)
     * 
     * Removes an attribute from the attributes
     *
     * @param   attKeyIn    The attribute to remove
     */
    public void remAtt(String attKeyIn){
        this.attributes.remove(attKeyIn);
    }//setId
	
	/** clearAtts()
     * 
     * Clears all attributes from this node
     * 
	 */
	public void clearAtts(){
		this.attributes = new HashMap<String,String>;
	}//clearEdges
	
	/** addEdge(LotEdge newEdge)
     * 
     * Sets a new edge.
     *
     * @param   newEdge    Adds the edge to this node
	 */
	public void addEdge(LotEdge newEdge){
		this.edges.add(newEdge);
	}//addEdge
	
	/** repEdge(LotEdge oldEdge, LotEdge newEdge)
     * 
     * Replaces an edge
	 * <p>
	 * If it does not have the old edge, adds it
     *
     * @param   oldEdge    the edge to replace
     * @param   newEdge    Adds the edge to this node
	 */
	public void repEdge(LotEdge oldEdge, LotEdge newEdge){
		if(!this.hasEdge(oldEdge)){
			this.addEdge(newEdge);
		}else{
			this.remEdge(oldEdge);
			this.addEdge(newEdge);
		}
	}//repEdge
	
	/** repEdge(String oldEdge, LotEdge newEdge)
     * 
     * Replaces an edge based in an Id
	 * <p>
	 * If it does not have the old edge, adds it
     *
     * @param   oldEdgeId    the edge to replace
     * @param   newEdge    Adds the edge to this node
	 */
	public void repEdge(String oldEdgeId, LotEdge newEdge){
		if(!this.hasEdge(oldEdgeId)){
			this.addEdge(newEdge);
		}else{
			this.remEdge(oldEdgeId);
			this.addEdge(newEdge);
		}
	}//repEdge
	
	/** remEdge(LotEdge edgeToRem)
     * 
     * Removes the specified edge
     *
	 * @param	edgeToRem		The the edge to remove
	 */
	public void remEdge(LotEdge edgeToRem){
		this.edges.remove(edgeToRem);
	}//remEdge
	
	/** remEdge(String edgeIdToRem)
     * 
     * Removes the specified edge
	 * <p>
	 *	TODO: check for nonexistent edge?
     *
	 * @param	edgeIdToRem		The id of the edge to remove
	 */
	public void remEdge(String edgeIdToRem){
		this.remEdge(this.getEdge(edgeIdToRem));
	}//remEdge
	
	/** setEdges(LotEdge newEdge)
     * 
     * Sets a new set of edges
     *
     * @param   edgesIn    The edges to set this node to
	 */
	public void setEdges(ArrayList<LotEdge> edgesIn){
		this.edges = edgesIn;
	}//addEdge
	
	/** clearEdges()
     * 
     * Clears all edges from this node
     * 
	 */
	public void clearEdges(){
		this.edges.clear();
	}//clearEdges
    
    //endregion
	
	//=========================================================================
    //    Getters
    //region Getters
    //=========================================================================
	
	/** getId()
     * 
     * Returns the id of this node
     * 
	 * @return			The id of this node.
	 */
	public String getId(){
		return this.id;
	}//getId
	
	/** getMetric()
     * 
     * Returns the metric of this node
     * 
	 * @return			The metric of this node.
	 */
	public long getMetric(){
		return this.metric;
	}//getMetric
	
	/** getAtt(String)
     * 
     * Returns the metric of this node
     * 
	 * @param	attKey	The key of the attribute being retrieved.
	 * @return			The metric of this node.
	 */
	public String getAtt(String attKey){
		return this.attributes.get(attKey);
	}//getAtt
	
	/** getEdges()
     * 
     * Returns the edges of this node in the form of an array list
     * 
	 * @return			The edges.
	 */
	public ArrayList<LotEdge> getEdges(){
		return this.edges;
	}//getEdges
	
	/** getEdge(String)
     * 
     * Returns an edge of this node based on its id.
     * 
	 * @return			The edge with the specified Id. Returns null if no edge exists.
	 */
	public LotEdge getEdge(String edgeId){
		for(int i = 0;i < this.edges.size();i++){
			if(this.edges.get(i).getId() == edgeId){
				return this.edges.get(i);
			}
		}
		return null;
	}//getEdge
	
	/** getEdgeByAtt(String,String,bool)
     * 
     * Returns an edge of this node based on a key/value pair
     * 
	 * @return			The edge with the specified key/value pair. Returns null if no edge exists.
	 */
	public LotEdge getEdgeByAtt(String attKey, String attVal, bool hasValue){
		for(int i = 0;i < this.edges.size();i++){
			if(this.edges.get(i).containsKey(attKey)){
				if(!hasValue || this.edges.get(i).getAtt(attVal) == attVal){
					return this.edges.get(i);
				}
			}
		}
		return null;
	}//getEdgeByAtt
	
	/** getNumEdges()
     * 
     * Returns the number edges of this node
     * 
	 * @return			The number of edges on this node
	 */
	public int getNumEdges(){
		return this.edges.size();
	}//getNumEdges
	
	/** hasEdge(LotEdge)
     * 
     * Returns if the node has the given edge
     * 
	 * @param	edgeIn	The edge to test on
	 * @return		If this node has this edge
	 */
	public boolean hasEdge(LotEdge edgeIn){
		return this.edges.contains(edgeIn);
	}//hasEdge
	
	/** hasEdge(String)
     * 
     * Returns if the node has the given edge
     * 
	 * @param	edgeIdIn	The edgeId to test on
	 * @return		If this node has this edge
	 */
	public boolean hasEdge(String edgeIdIn){
		for(int i = 0;i < edges.size();i++){
			if(this.edges.get(i).getId() == edgeIdIn){
				return true;
			}
		}
		return false;
	}//hasEdge
	
	//endregion
    
    
}//class LotNode
