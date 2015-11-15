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
	
	public static LinkedList<LotEdge> findRandomPath(BotLot lotIn, LotNode destNode){
		LinkedList<LotEdge> tempPath = new LinkedList<LotEdge>();
		LotNode tempNode = lotIn.getCurNode();
		ArrayList<LotEdge> tempEdgeList = new ArrayList<LotEdge>();
		Random rand = new Random();
		System.out.println("Calculating new path...");
		try{
			tempEdgeList = lotIn.mainGraph.getEdgeListWithoutNulls(tempNode);
			System.out.println("start loop...");
			while(tempNode != destNode && tempEdgeList.size() > 0){
				System.out.println("\tGetting random edge...");
				int pathToGoOn = rand.nextInt(tempEdgeList.size());
				System.out.println("\tAdding to new path...");
				tempPath.add(tempEdgeList.get(pathToGoOn));
				System.out.println("\tUpdating temp node...");
				tempNode = lotIn.mainGraph.getOtherNode(tempNode, tempEdgeList.get(pathToGoOn));
				System.out.println("\tUpdating temp edge list...");
				tempEdgeList = lotIn.mainGraph.getEdgeListWithoutNulls(tempNode);
			}
		}catch(LotGraphException err){
			System.out.println("FATAL ERROR- findRandomPath(). You should not get this. Error: " + err.getMessage());
			System.exit(1);
		}
		
		return tempPath;
	}
	
	
	
}
