package botLot.lotGraph;
/**
 * LotGraphException.java
 * <p>
 * Exceptions for the LotGraph object.
 * <p>
 * Started: 10/7/15
 * 
 * @author Greg Stewart
 * @version	1.0 11/22/15
 */
public class LotGraphException extends Exception {
	/** Unsure why exactly we need this.<p>Gave an error without it: "The serializable class LotGraphException does not declare a static final serialVersionUID field of type long" */
	private static final long serialVersionUID = -8102564033843522901L;

	/**
	 * Empty exception (just in case).
	 */
    public LotGraphException() {
    	super();
    }//LotGraphException()

    /**
     * General exception, used for all exceptions made by the LotGraph object. 
     * 
     * @param message	The message of what happened in the LotGraph code.
     */
    public LotGraphException(String message){
       super(message);
    }//LotGraphException(String)
}//class LotGraphException
