/**
LotNode.java

Sets up the node object for the BotLot graph structure.

@author Greg Stewart

Started: 10/7/15
Last Edit: 11/9/15

@version 1.0
*/
package BotLot.LotGraph;

/* imports*/
import java.util.HashMap;//for attributes

public class LotNode{
    private String id;//the Id of the node, required before setting edges
    private double metric;//the metric for the node, defaults to 0
    protected HashMap<String,String> attributes;//user defined attributes of the node, defaults to empty
    
    //=========================================================================
    //    Constructors
    //region Constructors
    //=========================================================================
    
	/**
     * Constructor to initialize everything
     * 
     * @param   idIn    		the Id to give the node.
     * @param   metricIn    the metric to give the node.
     * @param   attsIn    	the attributes to give the node.
     * @param   edges	    	the edges to give the node.
     */
    public LotNode(String idIn, long metricIn, HashMap<String,String> attsIn){
        this(idIn);
		this.setMetric(metricIn);
		this.setAtts(attsIn);
    }//LotNode(String idIn)
	
    /**
     * Constructor to initialize the Id.
     * 
     * @param   idIn    the Id to give the node
     */
    public LotNode(String idIn){
        this();
        this.setId(idIn);
    }//LotNode(String idIn)

    /**
     * Empty constructor to initialize the node
     * <p>
     * Will need an id set before adding any edges
     */
    public LotNode(){
        this.attributes = new HashMap<String,String>();
        this.metric = 0;
    }//LotNode()
    
    //endregion
    
    //=========================================================================
    //    Setters
    //region Setters
    //=========================================================================
    
    /**
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
    
    /**
     * Sets the node's metric.
     *
     * @param   metricIn    The new metric of the node
     */
    public void setMetric(long metricIn){
        this.metric = metricIn;
    }//setMetric

    /**
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

    /**
     * Sets a new value of the attribute in. Adds new attribute if not there
	 *
     * @param   attsIn    The attributes to set this Node's attributes to
     */
    public void setAtts(HashMap<String, String> attsIn){
        this.attributes = attsIn;
    }//setAtts
	
	/** 
     * Removes an attribute from the attributes
     *
     * @param   attKeyIn    The attribute to remove
     */
    public void remAtt(String attKeyIn){
        this.attributes.remove(attKeyIn);
    }//setId
	
    //endregion
	
	//=========================================================================
    //    Getters
    //region Getters
    //=========================================================================
	
	/**
     * Returns the id of this node.
     * 
	 * @return	The id of this node.
	 */
	public String getId(){
		return this.id;
	}//getId
	
	/**
     * Returns the metric of this node.
     * 
	 * @return	The metric of this node.
	 */
	public double getMetric(){
		return this.metric;
	}//getMetric
	
	/**
     * Returns the metric of this node.
     * 
	 * @param	attKey	The key of the attribute being retrieved.
	 * @return			The metric of this node.
	 */
	public String getAtt(String attKey){
		return this.attributes.get(attKey);
	}//getAtt
	
	@Override
	public String toString() {
		return "LotNode [id=" + id + ", metric=" + metric + ", attributes=" + attributes + "]";
	}

	//endregion
    
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attributes == null) ? 0 : attributes.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		long temp;
		temp = Double.doubleToLongBits(metric);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

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
		LotNode other = (LotNode) obj;
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
		if (Double.doubleToLongBits(metric) != Double.doubleToLongBits(other.metric))
			return false;
		return true;
	}
    
}//class LotNode
