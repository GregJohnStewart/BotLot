package botLot.lotGraph;
import java.util.HashMap;//for attributes
/**
 * LotEdge.java
 * <p>
 * Sets up the Edge object for the BotLot graph structure.
 * Started: 10/7/15
 * 
 * @author Greg Stewart
 * @version 1.0 3/7/16
 */
public class LotEdge{
	/** The id of the edge, required for normal graph operation. When used with BotLot, this is automatically set. */
    private String id;
    /** The node this edge goes to. */
    private LotNode endNode;
    /** The metric for the edge, defaults to 0 */
    private double metric;
    /** User defined attributes of the edge, defaults to empty */
    protected HashMap<String,String> attributes;
	/** Flag to denote this should be treated as a path with infinite size. For comparison purposes. */
	public boolean infSizeFlag = false;
    
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
    	this.infSizeFlag = edgeIn.infSizeFlag;
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
     * @return	This edge.
     */
    public LotEdge setId(String idIn){
        this.id = idIn;
        return this;
    }//setId(String)
    
    /**
     * Sets the Edges' end node ({@link #endNode}). Trusts that the node is valid.
     * 
     * @param nodeIn	The new end node.
     * @return	This edge.
     */
    public LotEdge setEndNode(LotNode nodeIn){
    	this.endNode = nodeIn;
    	return this;
    }//setEndNode(LotNode)
    
    /**
     * Sets the edge's {@link #metric}.
     *
     * @param	metricIn	The new metric of the edge.
     * @return	This edge.
     */
    public LotEdge setMetric(double metricIn){
        this.metric = metricIn;
        return this;
    }//setMetric(double)

    /**
     * Sets a new value of the {@link #attributes}. Adds new attribute if not there.
     * <p>
     * Use this to add or edit attributes.
     *
     * @param	attKeyIn	The attribute to add or edit.
     * @param	attValIn	The attribute's value.
     * @return	This edge.
     */
    public LotEdge setAtt(String attKeyIn, String attValIn){
        this.attributes.put(attKeyIn, attValIn);
        return this;
    }//setAtt(String, String)

    /**
     * Sets {@link #attributes} to a new set of attributes.
     *
     * @param	attsIn	The attributes to set this Edge's to.
     * @return	This edge.
     */
    public LotEdge setAtts(HashMap<String,String> attsIn){
        this.attributes = attsIn;
        return this;
    }//setAtt(HashMap<String,String>)
	
	/**
     * Removes an attribute from {@link #attributes}.
     *
     * @param   attKeyIn    The key of the attribute to remove.
     * @return	This edge.
     */
    public LotEdge remAtt(String attKeyIn){
        this.attributes.remove(attKeyIn);
        return this;
    }//remAtt(String)
    
	/**
     * Clears all attributes from this edge.
     * 
     * @return	This edge.
	 */
	public LotEdge clearAtts(){
		this.attributes.clear();
		return this;
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
	}//getEndNode()
	
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
	
	/**
	 * Determines if this edge is shorter than the edge given.
	 * 
	 * @param edgeIn	The edge we are testing against.
	 * @return	If this edge is shorter than the edge given.
	 */
	public boolean isShorter(LotEdge edgeIn){
		if(this.getMetric() < edgeIn.getMetric() || edgeIn.infSizeFlag){
			return true;
		}
		return false;
	}
	
	/**
	 * Determines if this edge is longer than the edge given.
	 * 
	 * @param edgeIn	The edge we are testing against.
	 * @return	If this edge is longer than the edge given.
	 */
	public boolean isLonger(LotEdge edgeIn){
		return !this.isShorter(edgeIn);
	}
	
	@Override
	public String toString() {
		return "LotEdge [id=" + id + ", endNode=" + endNode.getId() + ", metric=" + metric + ", attributes=" + attributes + "]";
	}

}//class LotEdge
