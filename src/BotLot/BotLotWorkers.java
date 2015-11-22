/**
BotLotWorkers.java

Handles the heavy lifting, easier to separate out the code for cleanliness and such.

@author Greg Stewart

Started: 10/7/15
Last Edit: 10/18/15

@version	1.0
 */

package BotLot;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import BotLot.LotGraph.*;

public class BotLotWorkers {
	
	public static LinkedList<LotEdge> getShortestPath(BotLot lotIn, LotNode destNode){
		
		//determine what algorithm to use.....
		
		//return doDijkstra(lotIn, destNode);
		return findRandomPath(lotIn, destNode);
	}
	
	public static LinkedList<LotEdge> doDijkstra(BotLot lotIn, LotNode destNode){
		return new LinkedList<LotEdge>();
	}
	
	/**
	 * Finds a path by randomly selecting edges on each node.
	 * <p>
	 * Obviously not a great way to do this.
	 * 
	 * @param lotIn
	 * @param destNode
	 * @return
	 */
	public static LinkedList<LotEdge> findRandomPath(BotLot lotIn, LotNode destNode){
		LinkedList<LotEdge> tempPath = new LinkedList<LotEdge>();
		LotNode tempNode = lotIn.getCurNode();
		ArrayList<LotEdge> tempEdgeList = new ArrayList<LotEdge>();
		Random rand = new Random();
		//System.out.println("Calculating new path...");
		try{
			tempEdgeList = lotIn.mainGraph.getEdgeListWithoutNulls(tempNode);
			//System.out.println("start loop...");
			int edgeIndexInTempListToGoDown = 0;
			while(tempNode != destNode && tempEdgeList.size() > 0){
				//System.out.println("\tEdges: " + tempEdgeList.toString() + " Size: "+ tempEdgeList.size() +"\n\ttempNode: " + tempNode.toString());
				//System.out.println("\tGetting random edge...");
				if(tempEdgeList.size() == 1){
					edgeIndexInTempListToGoDown = 0;
				}else{
					edgeIndexInTempListToGoDown = (rand.nextInt((tempEdgeList.size() - 1)));
				}
				//System.out.println("\t\tEdge going down: " + edgeIndexInTempListToGoDown);
				//System.out.println("\tAdding to new path...");
				tempPath.add(tempEdgeList.get(edgeIndexInTempListToGoDown));
				//System.out.println("\tUpdating temp node...");
				tempNode = lotIn.mainGraph.getOtherNode(tempNode, tempEdgeList.get(edgeIndexInTempListToGoDown));
				//System.out.println("\tUpdating temp edge list...");
				tempEdgeList = lotIn.mainGraph.getEdgeListWithoutNulls(tempNode);
			}
		}catch(LotGraphException err){
			System.out.println("FATAL ERROR- findRandomPath(). You should not get this. Error: " + err.getMessage());
			System.exit(1);
		}
		
		return tempPath;
	}//findRandomPath(BotLot, destNode)
	
	
	
}
