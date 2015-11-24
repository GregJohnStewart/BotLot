package BotLot;
import BotLot.LotGraph.*;//for dealing with everything LotGraph.
import java.util.ArrayList;//for dealing with the graph structure itself.
import java.util.LinkedList;//for building paths of edges.
import java.util.Random;//for the random path gen.
/**
 * BotLotWorkers.java
 * <p>
 * Handles the heavy lifting, easier to separate out the code for cleanliness and such.
 * 
 * @author Greg Stewart
 * @version	1.0 10/24/15
 */
public class BotLotWorkers {
	
	/**
	 * Determines which path taking algorithm is probably best to use.
	 * 
	 * @param lotIn	The graph structure to deal with.
	 * @return	A new path from the current node in {@link lotIn} to the destination node in that structure.
	 */
	public static LinkedList<LotEdge> getShortestPath(BotLot lotIn){
		//determine what algorithm to use.....
		
		//return doDijkstra(lotIn, destNode);
		return findRandomPath(lotIn);
	}//getShortestPath(BotLot)
	
	/**
	 * Performs Dijkstra's algorithm to obtain a shortest path to the destination node.
	 * <p>
	 * Still needs to be implemented.
	 * 
	 * @param lotIn	The LotGraph structure to deal with.
	 * @return A new shortest path from the current node in {@link lotIn} to the destination node in that structure.
	 */
	public static LinkedList<LotEdge> doDijkstra(BotLot lotIn){
		return new LinkedList<LotEdge>();
	}//doDijkstra(BotLot)
	
	/**
	 * Finds a path by randomly selecting edges on each node.
	 * <p>
	 * Obviously not a great way to do this. Done for testing simple things. Or directing something for the scenic route.
	 * 
	 * TODO:: stop finding paths if it is obvious that one cannot be found
	 * @param lotIn The LotGraph structure to deal with.
	 * @return	A new random path from the current node in {@link lotIn} to the destination node in that structure.
	 */
	public static LinkedList<LotEdge> findRandomPath(BotLot lotIn){
		LinkedList<LotEdge> tempPath = new LinkedList<LotEdge>();
		LotNode tempNode = lotIn.getCurNode();
		ArrayList<LotEdge> tempEdgeList = new ArrayList<LotEdge>();
		Random rand = new Random();
		//System.out.println("Calculating new path...");
		try{
			tempEdgeList = lotIn.mainGraph.getEdgeListWithoutNulls(tempNode);
			//System.out.println("start loop...");
			int edgeIndexInTempListToGoDown = 0;
			while(tempNode != lotIn.getDestNode() && tempEdgeList.size() > 0){
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
	}//findRandomPath(BotLot)
	
	/**
	 * Determines if there is a path from the curNode to the destNode. 
	 * 
	 * @param lotIn	The BotLot that we are dealing with.
	 * @return	If there is a path from the curNode to the destNode of the BotLot given.
	 */
	public static boolean hasPath(BotLot lotIn){
		//TODO:: finish this bitch
		return true;
	}
	
}//class BotLotWorkers