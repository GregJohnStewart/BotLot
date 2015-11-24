package BotLot;
/**
 * BotLotException.java
 * <p>
 * Exceptions for the BotLot object.
 * <p>
 * Started: 10/7/15
 * 
 * @author Greg Stewart
 * @version	1.0 11/24/15
 */
public class BotLotException extends Exception{
	/** Unsure why exactly we need this.<p>Gave an error without it: "The serializable class LotGraphException does not declare a static final serialVersionUID field of type long" */
	private static final long serialVersionUID = -8102564033843522901L;

	/**
	 * Empty exception (just in case).
	 */
	public BotLotException() {
		super();
	}//BotLotException()
 
	/**
	 * General exception, used for all exceptions made by the BotLot object 
	 * 
	 * @param message	The message of what happened in the BotLot Code
	 */
	 public BotLotException(String message){
		 super(message);
	 }//BotLotException()
}//class BotLotException
