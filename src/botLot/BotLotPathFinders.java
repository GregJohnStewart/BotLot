package botLot;
import java.util.ArrayList;//for dealing with the graph structure itself.
import java.util.Random;//for the random path gen.

import botLot.lotGraph.*;
/**
 * BotLotWorkers.java
 * <p>
 * Handles the heavy lifting, easier to separate out the code for cleanliness and such.
 * 
 * @author Greg Stewart
 * @version	1.0 12/10/15
 */
public class BotLotPathFinders {
	private static boolean readyPathChecked = false;
	private static final String notReadyString = "The BotLot Object is either not ready, or has no path between current location and destination.";
	private static final double ratioThreshHold = 0.75;
	/**
	 * Determines which path taking algorithm is probably best to use.
	 * 
	 * @param lotIn	The graph structure to deal with.
	 * @return	A new path from the current node in {@link lotIn} to the destination node in that structure.
	 * @throws BotLotException 
	 */
	public static LotPath getShortestPath(BotLot lotIn) throws BotLotException{
		if(!readyPathCheck(lotIn)){
			throw new BotLotException(notReadyString);
		}
		readyPathChecked = true;
		LotPath pathFound = null;
		
		//determine what algorithm to use.....
		if(lotIn.mainGraph.getNodeEdgeRatio() >= ratioThreshHold){
			//if about the same amount of nodes to edges, get best of 5 random path gen's
			double curBest = Double.POSITIVE_INFINITY;
			LotPath tempPath;
			for(int i = 0; i < 5; i++){
				tempPath = findRandomPath(lotIn);
				if(tempPath.getPathMetric() < curBest){
					pathFound = tempPath;
					curBest = tempPath.getPathMetric();
				}
			}
		}else{
			//TODO:: implement something better
			pathFound = findRandomPath(lotIn);
		}
		
		readyPathChecked = false;
		return pathFound;
	}//getShortestPath(BotLot)
	
	/**
	 * Performs Dijkstra's algorithm to obtain a shortest path to the destination node.
	 * <p>
	 * Still needs to be implemented.
	 * 
	 * @param lotIn	The LotGraph structure to deal with.
	 * @return A new shortest path from the current node in {@link lotIn} to the destination node in that structure.
	 * @throws BotLotException 
	 */
	public static LotPath doDijkstra(BotLot lotIn) throws BotLotException{
		if(!readyPathChecked){
			if(!readyPathCheck(lotIn)){
				throw new BotLotException(notReadyString);
			}
		}
			
		
		return new LotPath();
	}//doDijkstra(BotLot)
	
	/**
	 * Finds a path by randomly selecting edges on each node.
	 * <p>
	 * Obviously not a great way to do this. Done for testing simple things. Or directing something for the scenic route.
	 * 
	 * TODO:: stop finding paths if it is obvious that one cannot be found
	 * @param lotIn The LotGraph structure to deal with.
	 * @return	A new random path from the current node in {@link lotIn} to the destination node in that structure.
	 * @throws BotLotException 
	 */
	public static LotPath findRandomPath(BotLot lotIn) throws BotLotException{
		if(!readyPathChecked){
			if(!readyPathCheck(lotIn)){
				throw new BotLotException(notReadyString);
			}
		}
		LotPath tempPath = new LotPath();
		LotNode tempNode = lotIn.getCurNode();
		ArrayList<LotEdge> tempEdgeList = new ArrayList<LotEdge>();
		Random rand = new Random();
		//System.out.println("Calculating new path...");
		try{
			tempEdgeList = lotIn.mainGraph.getNode(tempNode).getEdges();
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
				tempPath.path.add(tempEdgeList.get(edgeIndexInTempListToGoDown));
				//System.out.println("\tUpdating temp node...");
				tempNode = lotIn.mainGraph.getOtherNode(tempNode, tempEdgeList.get(edgeIndexInTempListToGoDown));
				//System.out.println("\tUpdating temp edge list...");
				tempEdgeList = lotIn.mainGraph.getNode(tempNode).getEdges();
			}
		}catch(LotGraphException err){
			System.out.println("FATAL ERROR- findRandomPath(). You should not get this. Error: " + err.getMessage());
			System.exit(1);
		}
		//remove loops; for every node, if there are duplicates and everything in between.
		tempPath.removeLoops();
		return tempPath;
	}//findRandomPath(BotLot)
	
	/**
	 * Determines if there is a path from the curNode to the destNode. 
	 * 
	 * @param lotIn	The BotLot that we are dealing with.
	 * @return	If there is a path from the curNode to the destNode of the BotLot given.
	 * @throws BotLotException If the BotLot object given isn't ready for path generation.
	 */
	public static boolean hasPath(BotLot lotIn) throws BotLotException{
		if(lotIn.ready(false)){
			//check if curNode is directly connected to destNode
			if(lotIn.getCurNode().getConnectedNodes().contains(lotIn.getDestNode())){
				return true;
			}
			//else have to go find it, following edges
			ArrayList<LotNode> nodesConnected = lotIn.getCurNode().getConnectedNodes();
			ArrayList<LotNode> tempList = new ArrayList<LotNode>();
			ArrayList<LotNode> nodesFinished = new ArrayList<LotNode>();
			nodesFinished.add(lotIn.getCurNode());
			nodesConnected.removeAll(nodesFinished);//ensure none of the edges pointed back to curNode
			while(true){
				//for each in nodes connected, get nodes connected to them (if not one already searched)
				while(nodesConnected.size() != 0){
					//check if dest node is in this node's connected set
					if(nodesConnected.get(0).getConnectedNodes().contains(lotIn.getDestNode())){
						return true;
					}
					//put nodes held by node into tempList
					for(LotNode curNode : nodesConnected.get(0).getConnectedNodes()){
						if(!nodesFinished.contains(curNode)){
							tempList.add(curNode);
						}
					}
					//put node in nodesFinished and remove it from nodesConnected
					nodesFinished.add(nodesConnected.remove(0));
				}
				//if we got this far and no nodes are in the temp list, there is nothing else we can do, so there is no path
				if(tempList.size() == 0){
					return false;
				}else{
					nodesConnected = new ArrayList<LotNode>(tempList);
					tempList = new ArrayList<LotNode>();
				}
			}//running loop
		}//if got valid stuff
		throw new BotLotException("LotGraph not ready to determine path.");
	}//hasPath(BotLot)
	
	/**
	 * Checks if the BotLot object given is ready, and has a path between curNode and destNode.
	 * 
	 * @param lotIn	The BotLot object to test.
	 * @return	If the BotLot object given is ready, and has a path between curNode and destNode.
	 */
	private static boolean readyPathCheck(BotLot lotIn){
		if(!lotIn.ready(false)){
			return false;
		}
		try {
			if(!hasPath(lotIn)){
				return false;
			}
		} catch (BotLotException e) {
			return false;
		}
		return true;
	}//readyPathCheck(BotLot)
	
	
}//class BotLotWorkers