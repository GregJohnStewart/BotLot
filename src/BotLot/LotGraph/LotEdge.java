package BotLot.LotGraph;
/**
LotEdge.java

Sets up the Edge object for the BotLot graph structure.

@author Greg Stewart

Started: 10/7/15
Last Edit: 10/18/15

@version 1.0

**/

/* imports*/
import java.util.HashMap;//for attributes

public class LotEdge{
    private String id;//the Id of the node, required
    private long metric;//the metric for the node, defaults to 0
    protected HashMap<String,String> attributes;//user defined attributes of the node, defaults to empty  
    
    //=========================================================================
    //    Constructors
    //region Constructors
    //=========================================================================
    
    /** LotEdge(String idIn, long metricIn, HashMap<String,String> attsIn, LotNode nodeOneIn, LotNode nodeTwoIn)
     *
     * Constructor to initialize all the variables.
     * 
     * @param   idIn    the Id to give the edge
     * @param   metricIn    the metric to give the node
     * @param   attsIn    the attributes to give the node
     */
    public LotEdge(String idIn, long metricIn, HashMap<String,String> attsIn){
        this(idIn);
		this.setMetric(metricIn);
		this.setAtts(attsIn);
    }//LotEdge(String idIn, long metricIn, HashMap<String,String> attsIn, LotNode nodeOneIn, LotNode nodeTwoIn)
	
    /** LotEdge(String idIn)
     *
     * Constructor to initialize the Id.
     * 
     * @param   idIn    the Id to give the edge
     */
    public LotEdge(String idIn){
        this();
        this.setId(idIn);
    }//LotEdge(String idIn)

    /** LotEdge(String idIn)
     *
     * empty constructor to initialize the node
     */
    public LotEdge(){
        this.attributes = new HashMap<String,String>();
        this.metric = 0;
    }//LotEdge()
    
    //endregion
    
    //=========================================================================
    //    Setters
    //region Setters
    //=========================================================================
    
    /** setId(String idIn)
     * 
     * Sets the edge's id.
     * <p>
     * Trusts that the Id being given is valid, and does not conflict with other Id's
     * TODO: error check for this by going through all edges currently in contact with?
     *
     * @param   idIn    The new Id to set to.
     */
    public void setId(String idIn){
        this.id = idIn;
    }//setId
    
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

    /** setAtts(HashMap<String,String> attsIn)
     * 
     * Sets a new set attributes.
     *
     * @param   attsIn    The attributes to set this Edge's to
     */
    public void setAtts(HashMap<String,String> attsIn){
        this.attributes = attsIn;
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
		this.attributes = new HashMap<String,String>();
	}//clearAtts
    
    //endregion
	
	
    //=========================================================================
    //    Getters
    //region getters
    //=========================================================================
    
	/**
	 * gets this edge's Id
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

	@Override
	public String toString() {
		return "LotEdge [id=" + id + ", metric=" + metric + ", attributes=" + attributes + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attributes == null) ? 0 : attributes.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + (int) (metric ^ (metric >>> 32));
		return result;
	}
    //endregion

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LotEdge other = (LotEdge) obj;
		if (attributes == null) {
			if (other.attributes != null)
				return false;
		} else if (!attributes.equals(other.attributes))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (metric != other.metric)
			return false;
		return true;
	}
}//class LotEdge
