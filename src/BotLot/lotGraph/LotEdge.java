/**




**/

/* imports*/
import java.util.HashMap;//for attributes


public class LotEdge{
    public String id;//the ID of the node, required
    private HashMap<String,String> attributes;//user defined attributes of the node, defaults to empty
    public long metric;//the metric for the node, defaults to 0
    public LotNode nodeOne;//one of the nodes this is attatched to
    public LotNode nodeTwo;//the other node this is attatched to    
    
    
}//class LotEdge
