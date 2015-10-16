/**
LotNode.java

Sets up the Node object for the BotLot graph structure.

@author Greg Stewart
Other Contributor(s):

Started: 10/7/15
Last Edit: 10/15/15

@version 1.0

**/

/* imports*/
import java.util.Arrays;//for the edges
import java.util.HashMap;//for attributes

public class LotNode{
    private String id;//the ID of the node, required before setting edges
    private long metric;//the metric for the node, defaults to 0
    private HashMap<String,String> attributes;//user defined attributes of the node, defaults to empty
    private ArrayList<lotEdge> edges//edges attached to this node, default empty
    
    //=========================================================================
    //    Constructors
    //region Constructors
    //=========================================================================
    
    /** LotNode(String idIn)
     *
     * Constructor to initialize the ID.
     * 
     * @param   idIn    the ID to give the node
     */
    public void LotNode(String idIn){
        this.LotNode();
        this.setID(idIn);
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
    
    /** setID(String idIn)
     * 
     * Sets the node's id.
     * <p>
     * Trusts that the ID being given is valid, and does not conflict with other ID's
     * TODO: error check for this by going through all nodes currently in contact with?
     *
     * @param   idIn    The new ID to set to.
     */
    public void setID(String idIn){
        this.id = idIn;
    }//setID
    
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
    }//setID
	
	/** remAtt(String attKeyIn, String attValIn)
     * 
     * Removes an attribute from the attributes
     *
     * @param   attKeyIn    The attribute to remove
     */
    public void remAtt(String attKeyIn){
        this.attributes.remove(attKeyIn);
    }//setID
	
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
     * Sets a new value of the attribute in. Adds new attribute if not there
     * <p>
     * Use this to add or edit attributes
     *
     * @param   newEdge    Adds the edge to this node
	 */
	public void addEdge(LotEdge newEdge){
		this.edges.add(newEdge);
	}//addEdge
	
	/** remEdge(LotEdge edgeToRem)
     * 
     * Removes the specified edge
     *
	 */
	public void remEdge(LotEdge edgeToRem){
		this.edges.remove(edgeToRem);
	}//remEdge
	
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
	 * @return			The edge with the specified ID. Returns null if no edge exists.
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
	 * TODO:: review for correctness
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
	
	//endregion
    
    
}//class LotNode
