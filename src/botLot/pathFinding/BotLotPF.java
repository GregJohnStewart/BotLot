package botLot.pathFinding;
import java.util.ArrayList;//for dealing with the graph structure itself.

import botLot.BotLot;
import botLot.BotLotException;
import botLot.lotGraph.*;
import botLot.pathFinding.Algorithm.BotLotPFAlgException;
import botLot.pathFinding.Algorithm.BotLotPFAlgorithm;
import botLot.pathFinding.Algorithm.BotLotPFExact;
import botLot.pathFinding.Algorithm.BotLotPFRandom;
/**
 * BotLotPF.java
 * <p>
 * Abstracts out the in between steps to path generation, use these for general use.
 * <p>
 * Started: 11/7/15
 * 
 * @author Greg Stewart
 * @version	1.0 3/7/16
 */
public class BotLotPF {
	/** The string passed to the BotLotPFException when the BotLot object given is not ready. */
	public static final String notReadyString = "The BotLot Object is either not ready, or has no path between current location and destination.";
	/** The minimum ratio value to do random path generation in {@link #getShortestPath(BotLot)} */
	public static final float ratioThreshHold = (float) 0.75;
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
	 * @throws BotLotPFAlgException 
	 */
	public static LotPath getShortestPath(LotGraph graphIn, LotNode curNode, LotNode destNode, ArrayList<LotEdge> edgesToAvoid) throws BotLotPFException, BotLotPFAlgException{
		if(!BotLotPFWorkers.readyCheck(graphIn, curNode, destNode, true)){
			throw new BotLotPFException(notReadyString);
		}
		LotPath pathFound = new LotPath();
		pathFound.infSizeFlag = true;
		//determine what algorithm to use.....
		//System.out.println("\tnode/edge ratio: " + graphIn.getNodeEdgeRatio() + "\n\tThreshhold: " + ratioThreshHold);
		//if(graphIn.getNodeEdgeRatio() >= ratioThreshHold){
		if(true){//testing
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
			//System.out.println("Path: " + pathFound.toString());
		}else{
			System.out.println("\tToo complicated of a graph. Getting specific shortest path.");
			pathFound = new BotLotPFExact(graphIn, curNode, destNode, edgesToAvoid).findPath();
			//pathFound = getExactPath(lotIn);
		}
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
	 * @throws BotLotPFAlgException 
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
	 * @throws BotLotPFAlgException 
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
	 * @throws BotLotPFAlgException 
	 */
	public static LotPath getShortestPath(BotLot lotIn) throws BotLotPFException, BotLotPFAlgException{
		return getShortestPath(lotIn.mainGraph, lotIn.getCurNode(), lotIn.getDestNode());
	}
	
	/**
	 * Figures out which not complete node is closest to the curNode in the BotLot object. 
	 * <p>
	 * TODO:: review this
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
			//TODO:: figure out how to multithread this
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
				} catch (BotLotPFException e) {
					//ignore if no path exists
				} catch (BotLotPFAlgException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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