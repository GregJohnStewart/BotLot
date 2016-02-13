package botLot.pathFinding;
/**
 * BotLotPFException.java
 * <p>
 * Exceptions for the BotLotPF static class.
 * <p>
 * Started: 12/12/15
 * 
 * @author Greg Stewart
 * @version	1.0 12/12/15
 */
public class BotLotPFException extends Exception{
	/** Unsure why exactly we need this.<p>Gave an error without it: "The serializable class LotGraphException does not declare a static final serialVersionUID field of type long" */
	private static final long serialVersionUID = -8102564033843522901L;

	/**
	 * Empty exception (just in case).
	 */
	public BotLotPFException() {
		super();
	}//BotLotException()
 
	/**
	 * General exception, used for all exceptions made by the BotLotPF static object 
	 * 
	 * @param message	The message of what happened in the BotLotPF Code
	 */
	 public BotLotPFException(String message){
		 super(message);
	 }//BotLotException()
}//class BotLotException
