package botLot.dataSource;
/**
 * BotLotDataSourceException.java
 * <p>
 * Exceptions for the BotLot Data Source object.
 * <p>
 * Started: 10/26/15
 * 
 * @author Greg Stewart
 * @version	1.0 11/26/15
 */
public class BotLotDSException extends Exception{
	/** Unsure why exactly we need this.<p>Gave an error without it: "The serializable class LotGraphException does not declare a static final serialVersionUID field of type long" */
	private static final long serialVersionUID = -8102564033843522901L;

	/**
	 * Empty exception (just in case).
	 */
	public BotLotDSException() {
		super();
	}//BotLotDataSourceException()
 
	/**
	 * General exception, used for all exceptions made by the BotLotDataSource object 
	 * 
	 * @param message	The message of what happened in the BotLotDataSource Code
	 */
	 public BotLotDSException(String message){
		 super(message);
	 }//BotLotDataSourceException()
}//class BotLotDataSourceException

