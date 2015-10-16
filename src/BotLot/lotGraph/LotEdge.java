/**
LotEdge.java

Sets up the Edge object for the BotLot graph structure.

@author Greg Stewart

Started: 10/7/15
Last Edit: 10/15/15

@version 0.1

**/

/* imports*/
import java.util.HashMap;//for attributes

public class LotEdge{
    public String id;//the ID of the node, required
    public long metric;//the metric for the node, defaults to 0
    private HashMap<String,String> attributes;//user defined attributes of the node, defaults to empty
    public LotNode nodeOne;//one of the nodes this is attatched to
    public LotNode nodeTwo;//the other node this is attatched to    
    
    //=========================================================================
    //    Constructors
    //region Constructors
    //=========================================================================
    
    /** LotEdge(String idIn)
     *
     * Constructor to initialize the ID.
     * 
     * @param   idIn    the ID to give the edge
     */
    public void LotEdge(String idIn){
        this.LotEdge();
        this.setID(idIn);
    }//LotEdge(String idIn)

    /** LotEdge(String idIn)
     *
     * empty constructor to initialize the node
     */
    public void LotEdge(){
        this.attributes = new HashMap<String,String>;
        this.metric = 0;
        this.nodeOne = null;
        this.nodeTwo = null;
    }//LotEdge()
    
    //endregion
    
    //=========================================================================
    //    Setters
    //region Setters
    //=========================================================================
    
    /** setID(String idIn)
     * 
     * Sets the edge's id.
     * <p>
     * Trusts that the ID being given is valid, and does not conflict with other ID's
     * TODO: error check for this by going through all edges currently in contact with?
     *
     * @param   idIn    The new ID to set to.
     */
    public void setID(String idIn){
        this.id = idIn;
    }//setID
    
    /** setMetric(long metricIn)
     * 
     * Sets the edge's metric.
     *
     * @param   metricIn    The new metric of the edge
     */
    public void setMetric(long metricIn){
        this.metric = metricIn;
    }//setMetric

    /** setAtt(String attKeyIn, String attValIn)
     * 
     * Sets a new value of the attribute. Adds new attribute if not there
     * <p>
     * Use this to add or edit attributes
     *
     * @param   attKeyIn    The attribute to add or edit
     * @param   attValIn    The attribute's value
     */
    public void setAtt(String attKeyIn, String attValIn){
        this.attributes.put(attKeyIn, attValIn);
    }//setAtt
	
	/** remAtt(String attKeyIn, String attValIn)
     * 
     * Removes an attribute from the attributes
     *
     * @param   attKeyIn    The attribute to remove
     */
    public void remAtt(String attKeyIn){
        this.attributes.remove(attKeyIn);
    }//remAtt
    
	/** clearAtts()
     * 
     * Clears all attributes from this edge
     * 
	 */
	public void clearAtts(){
		this.attributes = new HashMap<String,String>;
	}//clearAtts
    
    /** setNodeOne
     * 
     * Sets nodeOne to the input node
     *
     * @param   nodeIn  The node to set nodeOne to
     */
    public void setNodeOne(LotNode nodeIn){
        this.nodeOne = nodeIn
    }//setNodeOne
    
    /** clearNodeOne
     * 
     * clears nodeOne
     */
    public void clearNodeOne(){
        this.nodeOne = null;
    }//setNodeOne
    
    /** setNodeTwo
     * 
     * Sets nodeTwo to the input node
	 * 
	 * @param	nodeIn	The node to set nodeTwo to
     */
    public void setNodeTwo(LotNode nodeIn){
        this.nodeTwo = nodeIn
    }//setNodeTwo
    
    /** clearNodeTwo
     * 
     * clears nodeTwo
     */
    public void clearNodeTwo(){
        this.nodeTwo = null;
    }//setNodeTwo
    
    //endregion
	
	
    //=========================================================================
    //    Getters
    //region getters
    //=========================================================================
    
	/**
	 * gets this edge's ID
	 *
	 * @return		The id of this edge
	 */
	public String getId(){
		return this.id;
	}//getId
	
	/**
	 * gets this edge's metric
	 *
	 * @return		The metric of this edge
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
	
	/**
	 * gets this edge's nodeOne
	 *
	 * @return		The nodeOne of this edge
	 */
	public LotNode getNodeOne(){
		return this.nodeOne;
	}//getNodeOne
	
	/**
	 * gets this edge's nodeTwo
	 *
	 * @return		The nodeTwo of this edge
	 */
	public LotNode getNodeTwo(){
		return this.nodeTwo;
	}//getNodeTwo
	
	/**
	 *gets the node on the other side of the edge
	 *
	 * @param	nodeAt	The node we are currently at
	 * @return		The node on the other side of the edge
	 */
	 public LotNode getOtherNode(LotNode nodeAt){
		 if(this.nodeOne == nodeAt){
			 return this.getNodeTwo;
		 }else if(this.nodeTwo == nodeAt){
			 return this.getNodeOne;
		 }else{
			 return null;
		 }
	 }//getOtherNode
	
    //endregion
	
}//class LotEdge
