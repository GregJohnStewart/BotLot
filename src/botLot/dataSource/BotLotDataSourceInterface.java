package botLot.dataSource;

import botLot.BotLot;
import botLot.lotGraph.LotGraph;

/**
 * Interface to define required functionality in data source objects.
 * <p>
 * Started: 5/4/16
 * 
 * @author Greg
 * @version	1.0 5/4/16
 */

interface BotLotDataSourceInterface {
	
	/**
	 * Method to check if the data source is ready to operate or not.
	 * @return	If the data source is ready to operate or not.
	 */
	public boolean ready();
	
	/**
	 * Retrieves the graph data saved where the object is configured to get it from.
	 * @return The graph data saved where the object is configured to get it from.
	 * @throws BotLotDSException If the graph could not be retrieved.
	 */
	public LotGraph getGraph() throws BotLotDSException;
	
	/**
	 * Retrieves the BotLot data saved where the object is configured to get it from.
	 * @return The BotLot data saved where the object is configured to get it from.
	 * @throws BotLotDSException If the data could not be retrieved.
	 */
	public BotLot getBotLot() throws BotLotDSException;
	
	/**
	 * Saves the graph data given to the configured data location.
	 * @param graphIn	The data to save to the source.
	 * @throws BotLotDSException If something went wrong with saving the data.
	 */
	public void saveGraph(LotGraph graphIn) throws BotLotDSException;
	
	/**
	 * Saves the BotLot data given to the configured data location.
	 * @param lotIn The data to save to the source.
	 * @throws BotLotDSException If something went wrong with saving the data.
	 */
	public void saveBotLot(BotLot lotIn) throws BotLotDSException;
	
	/**
	 * Checks if the data source has (valid) graph data inside it.
	 * @return	If the data source has (valid) graph data inside it or not.
	 */
	public boolean sourceHasData();
	
}//interface BotLotDataSourceInterface
