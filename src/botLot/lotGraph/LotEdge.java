package botLot.lotGraph;
import java.util.HashMap;//for attributes
/**
 * LotEdge.java
 * <p>
 * Sets up the Edge object for the BotLot graph structure.
 * Started: 10/7/15
 * 
 * @author Greg Stewart
 * @version 1.0 12/6/15
 */
public class LotEdge{
	/** The id of the node, required for normal graph operation. When used with BotLot, this is automatically set. */
    private String id;
    /** The node this edge goes to. */
    private LotNode endNode;
    /** The metric for the node, defaults to 0 */
    private double metric;
    /** User defined attributes of the node, defaults to empty */
    protected HashMap<String,String> attributes;
    
    //=========================================================================
    //    Constructors
    //region Constructors
    //=========================================================================
    
    /**
     * Creates a new LotEdge.
     * 
     * @param edgeIn	The edge to copy off of.
     */
    public LotEdge(LotEdge edgeIn){
    	this(edgeIn.getId(), edgeIn.getEndNode(), edgeIn.getMetric(), edgeIn.getAtts());
    }//LotEdge(LotEdge)
    
    /**
     * Constructor to initialize all the variables.
     * 
     * @param   idIn	The Id to set {@link #id}.
     * @param	nodeIn	The end node for the edge. Sets {@link #endNode}.
     * @param   metricIn	The metric to give {@link #metric}.
     * @param   attsIn	The attributes to give {@link #attributes}.
     */
    public LotEdge(String idIn, LotNode nodeIn, double metricIn, HashMap<String,String> attsIn){
        this(idIn);
        this.setEndNode(nodeIn);
		this.setMetric(metricIn);
		this.setAtts(attsIn);
    }//LotEdge(String, LotNode, double, HashMap<String,String>, LotNode, LotNode)
	
    /**
     * Constructor to initialize {@link #id}.
     * 
     * @param   idIn    The Id to set {@link #id}.
     */
    public LotEdge(String idIn){
        this();
        this.setId(idIn);
    }//LotEdge(idIn)

    /**
     * Empty constructor to initialize the node.
     */
    public LotEdge(){
    	this.endNode = null;
        this.metric = 0;
        this.attributes = new HashMap<String,String>();
    }//LotEdge()
    
    //endregion
    
    //=========================================================================
    //    Setters
    //region Setters
    //=========================================================================
    
    /**
     * Sets the edge's {@link #id}.
     * <p>
     * Trusts that the Id being given is valid, and does not conflict with other Id's.
     *
     * @param   idIn    The new Id to set {@link #id} to.
     */
    public void setId(String idIn){
        this.id = idIn;
    }//setId(String)
    
    /**
     * Sets the Edges' end node ({@link #endNode}). Trusts that the node is valid.
     * 
     * @param nodeIn	The new node.
     */
    public void setEndNode(LotNode nodeIn){
    	this.endNode = nodeIn;
    }//setEndNode(LotNode)
    
    /**
     * Sets the edge's {@link #metric}.
     *
     * @param	metricIn	The new metric of the edge.
     */
    public void setMetric(double metricIn){
        this.metric = metricIn;
    }//setMetric(double)

    /**
     * Sets a new value of the {@link #attributes}. Adds new attribute if not there.
     * <p>
     * Use this to add or edit attributes.
     *
     * @param	attKeyIn	The attribute to add or edit.
     * @param	attValIn	The attribute's value.
     */
    public void setAtt(String attKeyIn, String attValIn){
        this.attributes.put(attKeyIn, attValIn);
    }//setAtt(String, String)

    /**
     * Sets {@link #attributes} to a new set of attributes.
     *
     * @param	attsIn	The attributes to set this Edge's to.
     */
    public void setAtts(HashMap<String,String> attsIn){
        this.attributes = attsIn;
    }//setAtt(HashMap<String,String>)
	
	/**
     * Removes an attribute from {@link #attributes}.
     *
     * @param   attKeyIn    The key of the attribute to remove.
     */
    public void remAtt(String attKeyIn){
        this.attributes.remove(attKeyIn);
    }//remAtt(String)
    
	/**
     * Clears all attributes from this edge.
	 */
	public void clearAtts(){
		this.attributes.clear();
	}//clearAtts()
    
    //endregion
	
	
    //=========================================================================
    //    Getters
    //region getters
    //=========================================================================
    
	/**
	 * Gets this edge's Id ({@link #id}).
	 *
	 * @return	The id of this edge ({@link #id}).
	 */
	public String getId(){
		return this.id;
	}//getId()
	
	/**
	 * Returns {@link #endNode}.
	 * 
	 * @return	{@link #endNode}.
	 */
	public LotNode getEndNode(){
		return this.endNode;
	}
	
	/**
	 * Checks if the edge's end is set.
	 * 
	 * @return	If the edge's end is set.
	 */
	public boolean endSet(){
		if(this.getEndNode() != null){
			return true;
		}
		return false;
	}//endSet()
	
	/**
	 * Gets this edge's metric ({@link #metric}).
	 *
	 * @return	The metric of this edge.
	 */
	public double getMetric(){
		return this.metric;
	}//getMetric()
	
	/**
     * Returns an attribute of this node based on the given key.
     * 
	 * @param	attKey	The key of the attribute being retrieved.
	 * @return	The value at this key.
	 */
	public String getAtt(String attKey){
		return this.attributes.get(attKey);
	}//getAtt(String)
	
	/**
	 * Returns the attributes {@link #attributes} for this Edge.
	 * 
	 * @return {@link #attributes}
	 */
	public HashMap<String, String> getAtts(){
		return this.attributes;
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
			if(this.getAtt(attKey).equals(attVal)){
				return true;
			}
		}
		return false;
	}//hasAtt(String,String)

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
		if (Double.doubleToLongBits(metric) != Double.doubleToLongBits(other.metric))
			return false;
		return true;
	}
}//class LotEdge
