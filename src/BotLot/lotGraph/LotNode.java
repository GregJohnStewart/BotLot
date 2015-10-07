/**




**/

/* imports*/
import java.util.Arrays;//for the edges
import java.util.HashMap;//for attributes


public class LotNode{
    public String id;//the ID of the node, required before setting edges
    private HashMap<String,String> attributes;//user defined attributes of the node, defaults to empty
    public long metric;//the metric for the node, defaults to 0
    private ArrayList<lotEdge> edges//edges attatched to this node, default empty
    
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

    /** LotNode(String idIn)
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
    public void setAtt(String attKeyIn, String attValn){
        this.attributes
    }//setID
    
    //endregion
    
    
}//class LotNode
