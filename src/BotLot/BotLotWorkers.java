/**
BotLotWorkers.java

Handles the heavy lifting, easier to separate out the code for cleanliness and such.

@author Greg Stewart

Started: 10/7/15
Last Edit: 10/18/15

@version	1.0
 */

package BotLot;

import java.util.LinkedList;

import BotLot.LotGraph.*;

public class BotLotWorkers {
	
	public static LinkedList<LotEdge> getShortestPath(BotLot lotIn, LotNode destNode){
		
		//determine what algorithm to use.....
		
		return doDijkstra(lotIn, destNode);
	}
	
	public static LinkedList<LotEdge> doDijkstra(BotLot lotIn, LotNode ddestNode){
		return new LinkedList<LotEdge>();
	}
	
	
	
}
