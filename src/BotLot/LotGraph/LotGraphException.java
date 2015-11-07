package BotLot.LotGraph;


public class LotGraphException extends Exception {
	//unsure why exactly we need this
	private static final long serialVersionUID = -8102564033843522901L;

	/**
	 * Empty exception (just in case).
	 */
    public LotGraphException() {}

    /**
     * General exception, used for all exceptions made by the LotGraph object 
     * 
     * @param message	The message of what happened in the LotGraph Code
     */
    public LotGraphException(String message){
       super(message);
    }
}//class LotGraphException
