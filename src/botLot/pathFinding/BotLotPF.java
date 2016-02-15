package botLot.pathFinding;
import java.util.ArrayList;//for dealing with the graph structure itself.

import botLot.BotLot;
import botLot.BotLotException;
import botLot.lotGraph.*;
/**
 * BotLotPF.java
 * <p>
 * Functions needed to find paths in the BotLot class.
 * <p>
 * Started: 11/7/15
 * 
 * @author Greg Stewart
 * @version	1.0 1/20/16
 */
public class BotLotPF {
	/** The string passed to the BotLotPFException when the BotLot object given is not ready. */
	public static final String notReadyString = "The BotLot Object is either not ready, or has no path between current location and destination.";
	/** The minimum ratio value to do random path generation in {@link #getShortestPath(BotLot)} */
	public static final float ratioThreshHold = (float) 0.75;
	/** The number of times to do the random generation to attempt a shortest path */
	private static final int numTimesToDoRand = 5;
	/** The string passed to the BotLotPFException when the gets to the end of a line. */
	public static final String eolString = "End of the line of nodes.";
	
	/**
	 * Determines which path taking algorithm is probably best to use, and does it.
	 * 
	 * @param graphIn	The graph structure to deal with.
	 * @param curNode	The node we are starting at.
	 * @param destNode	The node we are trying to get to.
	 * @param edgesToAvoid	Edges to never deal with.
	 * @return	A path between the current nod and the destination node.
	 * @throws BotLotPFException	If something is not set right.
	 */
	public static LotPath getShortestPath(LotGraph graphIn, LotNode curNode, LotNode destNode, ArrayList<LotEdge> edgesToAvoid) throws BotLotPFException{
		if(!BotLotPFWorkers.readyCheck(graphIn, curNode, destNode, true)){
			throw new BotLotPFException(notReadyString);
		}
		LotPath pathFound = new LotPath();
		pathFound.infSizeFlag = true;
		//determine what algorithm to use.....
		System.out.println("\tnode/edge ratio: " + graphIn.getNodeEdgeRatio() + "\n\tThreshhold: " + ratioThreshHold);
		//if(lotIn.mainGraph.getNodeEdgeRatio() >= ratioThreshHold){
		if(true){//testing
			System.out.println("\tRatio within bounds. Doing random path gen.");
			/*If about the same amount of nodes to edges, get best of a few random path gens
			 * 
			 * Idea being that the paths throughout the data set are fairly linear, therefore this algorithm should run with O(# edges between curNode and destNode).
			 */
			LotPath tempPath = new LotPath();
			tempPath.infSizeFlag = true;
			BotLotPFRandom randomPathFinder = new BotLotPFRandom(graphIn, curNode, destNode);
			for(int i = 0; i < numTimesToDoRand; i++){
				tempPath = randomPathFinder.findPath();
				if(pathFound.isLonger(tempPath)){
					pathFound = tempPath;
				}
			}
			System.out.println("Path: " + tempPath.toString());
		}else{
			System.out.println("\tToo complicated of a graph. Getting specific shortest path.");
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
	 */
	public static LotPath getShortestPath(LotGraph graphIn, LotNode curNode, LotNode destNode) throws BotLotPFException{
		return getShortestPath(graphIn, curNode, destNode, new ArrayList<LotEdge>());
	}
	
	/**
	 * Determines which path taking algorithm is probably best to use, and does it.
	 * 
	 * @param lotIn	The BotLot structure to deal with.
	 * @param edgesToAvoid	Edges to never deal with.
	 * @return	A path between the current nod and the destination node.
	 * @throws BotLotPFException	If something is not set right.
	 */
	public static LotPath getShortestPath(BotLot lotIn, ArrayList<LotEdge> edgesToAvoid) throws BotLotPFException{
		return getShortestPath(lotIn.mainGraph, lotIn.getCurNode(), lotIn.getDestNode(), edgesToAvoid);
	}
	
	/**
	 * Determines which path taking algorithm is probably best to use, and does it.
	 * 
	 * @param lotIn	The BotLot structure to deal with.
	 * @return	A path between the current nod and the destination node.
	 * @throws BotLotPFException	If something is not set right.
	 */
	public static LotPath getShortestPath(BotLot lotIn) throws BotLotPFException{
		return getShortestPath(lotIn.mainGraph, lotIn.getCurNode(), lotIn.getDestNode());
	}
	
	/**
	 * Recursive method to find an exact shortest path.
	 * 
	 * @param lotIn	The BotLot we are dealing with.
	 * @param thisCurNode	The node this iteration is on.
	 * @param lastNode	The node whose iteration called this iteration.
	 * @param hitCounts A hashmap used to determine if we have been places before, and how often.
	 * @param trapEdges	A list of edges to not go down.
	 * @return	A shortest path leading to the end node.
	 * @throws BotLotPFException	If there is no path to the end node.
	 /
	private static LotPath getExactPath(BotLot lotIn, LotNode thisCurNode, LotNode lastNode, HashMap<LotNode,Integer> hitCounts, ArrayList<LotEdge> trapEdges) throws BotLotPFException{
		//System.out.println("Doing the thing!!");
		LotPath pathOut = new LotPath();
		//take care of special cases
		if(thisCurNode.hasEdgeTo(lotIn.getDestNode())){//if we found the destination node
			try {
				pathOut.append(thisCurNode.getEdgeTo(lotIn.getDestNode()));
			} catch (LotPathException e) {
				System.out.println("FATAL ERROR- You should not get this. Could not append last edge. Error: " + e.getMessage());
				System.exit(1);
			}
			return pathOut;
		}else if(thisCurNode.getNumCompEdges() == 0){//nowhere to go
			throw new BotLotPFException(eolString);
		}else if(thisCurNode.getNumCompEdges() == 1){//only way to go is back the way we came
			if(thisCurNode.getEdge(0).getEndNode() == lastNode){
				throw new BotLotPFException(eolString);
			}
		}
		
		ArrayList<LotPath> pathsMade = new ArrayList<LotPath>();
		
		//go down each path TODO:: in a different thread, and get the path..
		LotPath tempPath;
		for(LotEdge curEdge : thisCurNode.getConnectedEdges()){
			//don't try one edge loops, going back the way we came, or go down trap edges
			if(curEdge.getEndNode() == thisCurNode || curEdge.getEndNode() == lastNode || trapEdges.contains(curEdge)){
				continue;
			}
			try {
				if(hitCounts.containsKey(curEdge.getEndNode())){
					//TODO:: verify this works for all cases
					if( hitCounts.get(curEdge.getEndNode()) >= Math.pow(lotIn.mainGraph.getNumEdgesToNode(curEdge.getEndNode()),2) ){
						continue;
					}
				}
			} catch (LotGraphException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("FATAL ERROR- You should not get this. Error: " + e.getMessage());
				System.exit(1);
			}
			
			//add to the hashmap if need to, else increment
			if(!hitCounts.containsKey(curEdge.getEndNode())){
				hitCounts.put(curEdge.getEndNode(), 1);
				//System.out.println("\tNew Node.");
			}else{
				hitCounts.put(curEdge.getEndNode(), (hitCounts.get(curEdge.getEndNode()) + 1));
				//System.out.println("\tBeen here before. Count: " + hitCounts.get(curEdge.getEndNode()));
			}
			
			//add the current edge to the path
			tempPath = new LotPath();
			try {
				tempPath.append(curEdge);
			} catch (LotPathException e) {
				e.printStackTrace();
				System.out.println("FATAL ERROR- You should not get this. Could not append next edge. Error: " + e.getMessage());
				System.exit(1);
			}
			//get the path for the edge
			try {
				tempPath.append(getExactPath(lotIn, curEdge.getEndNode(), thisCurNode, hitCounts, trapEdges));
				tempPath.removeLoops();
				pathsMade.add(tempPath);
			}catch(BotLotPFException e){
				if(!e.getMessage().equals(eolString)){
					e.printStackTrace();
					System.out.println("FATAL ERROR- You should not ghet this. Error: " + e.getMessage());
					System.exit(1);
				}
			} catch (LotPathException e) {
				e.printStackTrace();
				System.out.println("FATAL ERROR- You should not get this. Could not append paths. Error: " + e.getMessage());
				System.exit(1);
			}
		}
		//determine which path is least & return
		pathOut = new LotPath();
		pathOut.infSizeFlag = true;
		for(LotPath curPath : pathsMade){
			if(curPath.isShorter(pathOut)){
				pathOut = curPath;
			}
		}
		//System.out.println("PAth made: " + pathOut.toString());
		return pathOut;
	}//getExactPath(BotLot, LotNode, LotNode)
	
	/**
	 * Gets an exact shortest path to the destination node.
	 * 
	 * @param lotIn	The BotLot object to use.
	 * @return	The shortest path to the destination node.
	 * @throws BotLotPFException	If the object was not ready or 
	 /
	public static LotPath getExactPath(BotLot lotIn) throws BotLotPFException{
		if(!readyPathChecked){
			if(!readyPathCheck(lotIn)){
				throw new BotLotPFException(notReadyString);
			}
		}
		try{
			System.out.println("Trap Edges: " + getTrapEdges(lotIn));
			return getExactPath(lotIn, lotIn.getCurNode(), lotIn.getCurNode(), new HashMap<LotNode,Integer>(), getTrapEdges(lotIn));
		}catch(BotLotPFException e){
			if(e.getMessage().equals(eolString)){
				e.printStackTrace();
				System.out.println("FATAL ERROR- You should not ghet this. Error: " + e.getMessage());
				System.exit(1);
			}
			throw e;
		}
	}//getExactPath(BotLot)
	*/
	
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