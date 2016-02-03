package botLot.lotGraph;
/**
 * LotPathException.java
 * <p>
 * Exceptions for the LotPath object.
 * <p>
 * Started: 12/5/15
 * 
 * @author Greg Stewart
 * @version	1.0 12/6/15
 */
public class LotPathException extends Exception {
	/** Unsure why exactly we need this.<p>Gave an error without it: "The serializable class LotPathException does not declare a static final serialVersionUID field of type long" */
	private static final long serialVersionUID = -8102564033843522901L;

	/**
	 * Empty exception (just in case).
	 */
    public LotPathException() {
    	super();
    }//LotGraphException()

    /**
     * General exception, used for all exceptions made by the LotNode object. 
     * 
     * @param message	The message of what happened in the LotNode code.
     */
    public LotPathException(String message){
       super(message);
    }//LotGraphException(String)
}//class LotNodeException