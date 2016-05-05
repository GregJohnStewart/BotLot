package botLot.pathFinding;
import java.util.ArrayList;//for dealing with the graph structure itself.
import java.util.Collection;

import botLot.BotLot;
import botLot.BotLotException;
import botLot.lotGraph.*;
import botLot.pathFinding.Algorithms.BotLotPFAlgException;
import botLot.pathFinding.Algorithms.BotLotPFAlgorithm;
import botLot.pathFinding.Algorithms.BotLotPFDijkstra;
import botLot.pathFinding.Algorithms.BotLotPFRandom;
/**
 * BotLotPF.java
 * <p>
 * Abstracts out the in between steps to path generation, use these for general use.
 * <p>
 * Started: 11/7/15
 * 
 * @author Greg Stewart
 * @version	1.0 5/4/16
 */
public class BotLotPF {
	/** The string passed to the BotLotPFException when the BotLot object given is not ready. */
	public static final String notReadyString = "The BotLot Object is either not ready, or has no path between current location and destination.";
	/** The minimum ratio value to do random path generation in {@link #getShortestPath(BotLot)} */
	public static float ratioThreshHold = (float) 0.75;
	/** The number of times to do the random generation to attempt a shortest path */
	private static final int numTimesToDoRand = 5;

	/**
	 * Determines which path taking algorithm is probably best to use, and does it.
	 * 
	 * @param graphIn	The graph structure to deal with.
	 * @param curNode	The node we are starting at.
	 * @param destNode	The node we are trying to get to.
	 * @param edgesToAvoid	Edges to never deal with.
	 * @return	A path between the current nod and the destination node.
	 * @throws BotLotPFException	If something is not set right.
	 * @throws BotLotPFAlgException If something went wrong with the path finding.
	 */
	public static LotPath getShortestPath(LotGraph graphIn, LotNode curNode, LotNode destNode, Collection<LotEdge> edgesToAvoid) throws BotLotPFException, BotLotPFAlgException{
		if(!BotLotPFWorkers.readyCheck(graphIn, curNode, destNode, true)){
			throw new BotLotPFException(notReadyString);
		}
		LotPath pathFound = new LotPath();
		pathFound.infSizeFlag = true;
		//determine what algorithm to use.....
		//System.out.println("\tnode/edge ratio: " + graphIn.getConnectedNodeEdgeRatio(curNode, edgesToAvoid) + "\n\tThreshhold: " + ratioThreshHold);
		if(graphIn.getConnectedNodeEdgeRatio(curNode, edgesToAvoid) >= ratioThreshHold){
		//if(true){//testing
			//System.out.println("\tRatio within bounds. Doing random path gen.");
			/*If about the same amount of nodes to edges, get best of a few random path gens
			 * 
			 * Idea being that the paths throughout the data set are fairly linear, therefore this algorithm should run with ~O(# edges between curNode and destNode).
			 */
			LotPath tempPath = new LotPath();
			tempPath.infSizeFlag = true;
			BotLotPFAlgorithm randomPathFinder = new BotLotPFRandom(graphIn, curNode, destNode, edgesToAvoid);
			for(int i = 0; i < numTimesToDoRand; i++){
				tempPath = randomPathFinder.findPath();
				//System.out.println("\t" +  tempPath.getPathMetric() + " < " + pathFound.getPathMetric() + "?");
				if(tempPath.isShorter(pathFound)){
					//System.out.println("\tFound faster path.");
					pathFound = tempPath;
				}
			}
		}else{
			System.out.println("\tToo complicated of a graph. Getting specific shortest path.");
			pathFound = new BotLotPFDijkstra(graphIn, curNode, destNode, edgesToAvoid).findPath();
		}
		System.out.println("Path: " + pathFound.toString());
		return pathFound;
	}//getShortestPath(BotLot)
	
	/**
	 * Determines which path taking algorithm is probably best to use, and does it.
	 * 
	 * @param graphIn	The graph structure to deal with.
	 * @param curNode	The node we are starting at.
	 * @param destNode	The node we are trying to get to.
	 * @return	A path between the current nod and the destination node.
	 * @throws BotLotPFException	If something is not set right.
	 * @throws BotLotPFAlgException If something went wrong with the path finding.
	 */
	public static LotPath getShortestPath(LotGraph graphIn, LotNode curNode, LotNode destNode) throws BotLotPFException, BotLotPFAlgException{
		return getShortestPath(graphIn, curNode, destNode, new ArrayList<LotEdge>());
	}
	
	/**
	 * Determines which path taking algorithm is probably best to use, and does it.
	 * 
	 * @param lotIn	The BotLot structure to deal with.
	 * @param edgesToAvoid	Edges to never deal with.
	 * @return	A path between the current nod and the destination node.
	 * @throws BotLotPFException	If something is not set right.
	 * @throws BotLotPFAlgException If something went wrong with the path finding.
	 */
	public static LotPath getShortestPath(BotLot lotIn, ArrayList<LotEdge> edgesToAvoid) throws BotLotPFException, BotLotPFAlgException{
		return getShortestPath(lotIn.mainGraph, lotIn.getCurNode(), lotIn.getDestNode(), edgesToAvoid);
	}
	
	/**
	 * Determines which path taking algorithm is probably best to use, and does it.
	 * 
	 * @param lotIn	The BotLot structure to deal with.
	 * @return	A path between the current nod and the destination node.
	 * @throws BotLotPFException	If something is not set right.
	 * @throws BotLotPFAlgException If something went wrong with the path finding.
	 */
	public static LotPath getShortestPath(BotLot lotIn) throws BotLotPFException, BotLotPFAlgException{
		return getShortestPath(lotIn.mainGraph, lotIn.getCurNode(), lotIn.getDestNode());
	}
	
	/**
	 * Gets a path leading to the closest node in the set given.
	 * <p>
	 * Use LotPath.getEndNode() to get the actual end node
	 * <p>
	 * TODO:: for the other input standards
	 * 
	 * @param graphIn	The LotGraph object to deal with.
	 * @param curNode	The node we are currently at.
	 * @param destNodesIn	The list of nodes we are determining is closest.
	 * @param edgesToAvoid	A list of edges to avoid in processing.
	 * @return	A path to the closest node, null if no paths found.
	 */
	public static LotPath getPathToClosestNode(LotGraph graphIn, LotNode curNode, Collection<LotNode> destNodesIn, Collection<LotEdge> edgesToAvoid){
		LotPath curPath = null;
		LotPath pathOut = new LotPath();
		pathOut.infSizeFlag = true;
		
		for(LotNode curDestNode : destNodesIn){
			try {
				curPath = getShortestPath(graphIn, curNode, curDestNode, edgesToAvoid);
			} catch (BotLotPFException | BotLotPFAlgException e) {
				continue;
			}
			if(curPath.isShorter(pathOut)){
				pathOut = curPath;
			}
		}
		
		if(pathOut.infSizeFlag){
			return null;
		}
		return pathOut; 
	}// getNodeWithShortestPath(LotGraph, LotNode, Collection<LotNode>, Collection<LotEdge>)
	
	
	/**
	 * Gets the closest not complete node to the current node given.
	 * 
	 * @param graphIn	The graph to operate on.
	 * @param curNode	The node we are currently at.
	 * @param edgesToAvoid	A list of edges to avoid when processing.
	 * @return	The closest incomplete node.
	 */
	public static LotNode getClosestIncompleteNode(LotGraph graphIn, LotNode curNode, Collection<LotEdge> edgesToAvoid){
		ArrayList<LotNode> notCompleteNodes = graphIn.getIncompleteNodes();
		if(notCompleteNodes.size() == 0){
			return null;
		}
		return getPathToClosestNode(graphIn, curNode, notCompleteNodes, edgesToAvoid).getEndNode();
	}//getClosestNotCompleteNode
	
	/**
	 * Figures out which not complete node is closest to the curNode in the BotLot object.
	 * 
	 * @param lotIn	The BotLot object to deal with.
	 * @return	The closest incomplete node. Null if the graph is complete.
	 */
	public static LotNode getClosestIncompleteNode(BotLot lotIn){
		return getClosestIncompleteNode(lotIn.mainGraph, lotIn.getCurNode(), new ArrayList<LotEdge>());
	}//getClosestNotCompleteNode(BotLot)

	/**
	 * Wrapper for {@link #getClosestIncompleteNode(BotLot)} to just set that closest node to the destNode of the BotLot object.
	 * 
	 * @param lotIn	The BotLot object to deal with.
	 */
	public static void setClosestIncompleteNode(BotLot lotIn){
		try {
			lotIn.setDestNode(getClosestIncompleteNode(lotIn));
		} catch (BotLotException e) {
			e.printStackTrace();
			System.out.println("FATAL ERR- setClosestNotCompleteNode(BotLot)- This should not happen. Error: " + e.getMessage());
			System.exit(1);
		}
	}//setClosestNotCompleteNode(BotLot)
	
	/**
	 * Gets the closest not complete node to the current node given.
	 * 
	 * @param lotIn	The BotLot object to take from.
	 * @param edgesToAvoid	Edges to not consider while processing.
	 * @return	The closest not complete node to the current node given.
	 */
	public static LotNode getClosestIncompleteNode(BotLot lotIn, Collection<LotEdge> edgesToAvoid){
		return getClosestIncompleteNode(lotIn.mainGraph, lotIn.getCurNode(), edgesToAvoid);
	}//getClosestNotCompleteNode(BotLot, Collection<LotEdge>)
	
	/**
	 * Sets the destNode in the given BotLot to the closest not complete node to the current node given.
	 * 
	 * @param lotIn	The BotLot object to work off of.
	 * @param edgesToAvoid	Edges to not consider in processing.
	 */
	public static void setClosestIncompleteNode(BotLot lotIn, Collection<LotEdge> edgesToAvoid){
		try {
			lotIn.setDestNode(getClosestIncompleteNode(lotIn, edgesToAvoid));
		} catch (BotLotException e) {
			e.printStackTrace();
			System.out.println("FATAL ERR- setClosestNotCompleteNode(BotLot)- This should not happen. Error: " + e.getMessage());
			System.exit(1);
		}
	}//setClosestNotCompleteNode(BotLot, Collection<LotEdge>)
	
}//class BotLotWorkers