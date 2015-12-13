package botLot;
import java.util.ArrayList;//for dealing with the graph structure itself.
import java.util.Random;//for the random path gen.

import botLot.lotGraph.*;
/**
 * BotLotWorkers.java
 * <p>
 * Handles the heavy lifting, easier to separate out the code for cleanliness and such.
 * <p>
 * Started: 11/7/15
 * 
 * @author Greg Stewart
 * @version	1.0 12/12/15
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
	 * @throws BotLotPathFindingException
	 */
	public static LotPath getShortestPath(BotLot lotIn) throws BotLotPathFindingException{
		if(!readyPathCheck(lotIn)){
			throw new BotLotPathFindingException(notReadyString);
		}
		readyPathChecked = true;
		LotPath pathFound = null;
		
		//determine what algorithm to use.....
		if(lotIn.mainGraph.getNodeEdgeRatio() >= ratioThreshHold){
			/*If about the same amount of nodes to edges, get best of 5 random path gens
			 * 
			 * Idea being that the paths throughout the data set are fairly linear, therefore this algorithm should run with O(# edges between curNode and destNode).
			 */
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
	 * @throws BotLotPathFindingException 
	 */
	public static LotPath doDijkstra(BotLot lotIn) throws BotLotPathFindingException{
		if(!readyPathChecked){
			if(!readyPathCheck(lotIn)){
				throw new BotLotPathFindingException(notReadyString);
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
	 * @throws BotLotPathFindingException 
	 */
	public static LotPath findRandomPath(BotLot lotIn) throws BotLotPathFindingException{
		if(!readyPathChecked){
			if(!readyPathCheck(lotIn)){
				throw new BotLotPathFindingException(notReadyString);
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
	 * @throws BotLotPathFindingException If the BotLot object given isn't ready for path generation.
	 */
	public static boolean hasPath(BotLot lotIn) throws BotLotPathFindingException{
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
		throw new BotLotPathFindingException("LotGraph not ready to determine path.");
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
		} catch (BotLotPathFindingException e) {
			return false;
		}
		return true;
	}//readyPathCheck(BotLot)
	
	/**
	 * Figures out which not complete node is closest to the curNode in the BotLot object. 
	 * 
	 * @param lotIn	The BotLot object to deal with.
	 * @return	The closest incomplete node. Null if the graph is complete.
	 */
	public static LotNode getClosestNotCompleteNode(BotLot lotIn){
		LotNode destNodeHold = lotIn.getDestNode();
		LotNode closestNotCompleteNode = null;
		if(!lotIn.mainGraph.graphIsComplete()){
			ArrayList<LotNode> nodesNotComplete = lotIn.mainGraph.getIncompleteNodes();
			double curBestMetric = Double.POSITIVE_INFINITY;
			LotPath tempPath = null; 
			for(LotNode curNode : nodesNotComplete){
				try {
					lotIn.setDestNode(curNode);
					tempPath = getShortestPath(lotIn);
					if(tempPath.getPathMetric() < curBestMetric){
						closestNotCompleteNode = curNode;
						curBestMetric = tempPath.getPathMetric();
					}
				} catch (BotLotException e) {
					System.out.println("FATAL ERR- getClosestNotCompleteNode(BotLot)- This should not happen. Error: " + e.getMessage());
					System.exit(1);
				} catch (BotLotPathFindingException e) {
					//ignore if no path exists
				}
			}
		}
		try {
			lotIn.setDestNode(destNodeHold);
		} catch (BotLotException e) {
			System.out.println("FATAL ERR- setClosestNotCompleteNode(BotLot)- This should not happen. Error: " + e.getMessage());
			System.exit(1);
		}
		return  closestNotCompleteNode;
	}//getClosestNotCompleteNode(BotLot)

	/**
	 * Wrapper for {@link #getClosestNotCompleteNode(BotLot)} to just set that closest node to the destNode of the BotLot object.
	 * 
	 * @param lotIn	The BotLot object to deal with.
	 */
	public static void setClosestNotCompleteNode(BotLot lotIn){
		try {
			lotIn.setDestNode(getClosestNotCompleteNode(lotIn));
		} catch (BotLotException e) {
			System.out.println("FATAL ERR- setClosestNotCompleteNode(BotLot)- This should not happen. Error: " + e.getMessage());
			System.exit(1);
		}
	}//setClosestNotCompleteNode(BotLot)
	
	
}//class BotLotWorkers