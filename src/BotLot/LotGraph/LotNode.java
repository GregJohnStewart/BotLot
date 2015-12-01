package BotLot.LotGraph;
import java.util.HashMap;//for attributes
/**
 * LotNode.java
 * <p>
 * Sets up the node object for the BotLot graph structure.
 * <p>
 * Started: 10/7/15
 * 
 * @author Greg Stewart 
 * @version 1.0 11/28/15
 */
public class LotNode{
	/** The Id of the node, required before setting edges */
    private String id;
    /** The number of edges this node is actually supposed to actually have.<p>Used to determine if graph is complete or not. (-1 if we do not know(default)) */
    private int numEdges;
    /** User defined attributes of the node, defaults to empty */
	protected HashMap<String,String> attributes;
    
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
    	this(nodeIn.getId(), nodeIn.attributes, nodeIn.getNumEdges());
    }//LotNode(LotNode)
    
	/**
     * Constructor to initialize everything.
     * 
     * @param   idIn	The Id to give {@link #id}.
     * @param   metricIn	The metric to give {@link #metric}.
     * @param   attsIn	The attributes to give {@link #attributes}.
     */
    public LotNode(String idIn, HashMap<String,String> attsIn, int numEdgesIn){
        this(idIn);
		this.setAtts(attsIn);
		this.setNumEdges(numEdgesIn);
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
     *  {@link #numEdges} defaults to '-1'.
     * <p>
     * 
     * Will need an id set before adding any edges.
     */
    public LotNode(){
        this.attributes = new HashMap<String,String>();
        this.numEdges = -1;
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
     */
    public void setId(String idIn){
        this.id = idIn;
    }//setId

    /**
     * Sets a new value of the {@link #attributes}. Adds new attribute if not there.
     * <p>
     * Use this to add or edit attributes.
     *
     * @param   attKeyIn    The attribute to add or edit.
     * @param   attValIn    The attribute's value.
     */
    public void setAtt(String attKeyIn, String attValIn){
        this.attributes.put(attKeyIn, attValIn);
    }//setAtt

    /**
     * Sets {@link #attributes} to a new set of attributes.
	 *
     * @param   attsIn    The attributes to set {@link #attributes}.
     */
    public void setAtts(HashMap<String, String> attsIn){
        this.attributes = attsIn;
    }//setAtts
	
	/** 
     * Removes an attribute from {@link #attributes}.
     *
     * @param   attKeyIn    The key of the attribute to remove.
     */
    public void remAtt(String attKeyIn){
        this.attributes.remove(attKeyIn);
    }//setId
	
    /**
     * Sets {@link #numEdges}.
     * 
     * @param numEdgesIn	The new number of edges.
     */
    public void setNumEdges(int numEdgesIn){
    	this.numEdges = numEdgesIn;
    }//setNumEdges(int)
    
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
	}//getId
	
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
	 * Returns the attribute HashMap<String, String> {@link #attributes} for this Node.
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
			if(this.getAtt(attKey).equals(attVal)){
				return true;
			}
		}
		return false;
	}//hasAtt(String,String)
	
	/**
	 * Gets {@link #numEdges}.
	 * 
	 * @return	The actual number of edges this node is supposed to have.
	 */
	public int getNumEdges(){
		return this.numEdges;
	}//getNumEdges()

	@Override
	public String toString() {
		return "LotNode [id=" + id + ", attributes=" + attributes + ", numEdges=" + numEdges
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attributes == null) ? 0 : attributes.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + numEdges;
		return result;
	}

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
		if (numEdges != other.numEdges)
			return false;
		return true;
	}
	
	//endregion
    
}//class LotNode
