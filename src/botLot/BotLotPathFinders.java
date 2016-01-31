package botLot;
import java.util.ArrayList;//for dealing with the graph structure itself.
import java.util.Random;//for the random path gen.

import botLot.lotGraph.*;
/**
 * BotLotPathFinders.java
 * <p>
 * Functions needed to find paths in the BotLot class.
 * <p>
 * Started: 11/7/15
 * 
 * @author Greg Stewart
 * @version	1.0 1/20/16
 */
public class BotLotPathFinders {
	/** Used to see if we already checked the BotLot object for correctness */
	private static boolean readyPathChecked = false;
	/** The string passed to the BotLotPathFindingException when the BotLot object given is not ready. */
	private static final String notReadyString = "The BotLot Object is either not ready, or has no path between current location and destination.";
	/** The minimum ratio value to do random path generation in {@link #getShortestPath(BotLot)} */
	private static final float ratioThreshHold = (float) 0.75;
	/** The number of times to do the random generation to attempt a shortest path */
	private static final int numTimesToDoRand = 5;
	/**
	 * Determines which path taking algorithm is probably best to use.
	 * 
	 * @param lotIn	The graph structure to deal with.
	 * @return	A new path from the current node in lotIn to the destination node in that structure.
	 * @throws BotLotPathFindingException	If the LotGraph given is not ready for path finding.
	 */
	public static LotPath getShortestPath(BotLot lotIn) throws BotLotPathFindingException{
		if(!readyPathCheck(lotIn)){
			throw new BotLotPathFindingException(notReadyString);
		}
		readyPathChecked = true;
		LotPath pathFound = null;
		
		//determine what algorithm to use.....
		//System.out.println("node/edge ratio: " + lotIn.mainGraph.getNodeEdgeRatio());
		if(lotIn.mainGraph.getNodeEdgeRatio() >= ratioThreshHold){
			//System.out.println("Ratio within bounds. Doing random path gen.");
			/*If about the same amount of nodes to edges, get best of a few random path gens
			 * 
			 * Idea being that the paths throughout the data set are fairly linear, therefore this algorithm should run with O(# edges between curNode and destNode).
			 */
			double curBest = Double.POSITIVE_INFINITY;
			LotPath tempPath;
			for(int i = 0; i < numTimesToDoRand; i++){
				tempPath = findRandomPath(lotIn);
				if(tempPath.getPathMetric() < curBest){
					pathFound = tempPath;
					curBest = tempPath.getPathMetric();
				}
			}
		}else{
			//System.out.println("Too complicated of a graph. Getting specific shortest path.");
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
	 * @return A new shortest path from the current node in lotIn to the destination node in that structure.
	 * @throws BotLotPathFindingException If the LotGraph object isn't ready for path generation.
	 */
	public static LotPath doDijkstra(BotLot lotIn) throws BotLotPathFindingException{
		if(!readyPathChecked){
			if(!readyPathCheck(lotIn)){
				throw new BotLotPathFindingException(notReadyString);
			}
		}
		//TODO:: use Dijkstra's algorithm to find a shortest path
		
		return new LotPath();
	}//doDijkstra(BotLot)
	
	/**
	 * Finds a path by randomly selecting edges on each node.
	 * <p>
	 * Obviously not a great way to do this. Done for testing simple things. Or directing something for the scenic route.
	 * 
	 * @param lotIn The LotGraph structure to deal with.
	 * @return	A new random path from the current node in lotIn to the destination node in that structure.
	 * @throws BotLotPathFindingException If the BotLot object given is not ready for path generation.
	 */
	public static LotPath findRandomPath(BotLot lotIn) throws BotLotPathFindingException{
		//System.out.println("entered findRandomPath(BotLot)");
		if(!readyPathChecked){
			if(!readyPathCheck(lotIn)){
				throw new BotLotPathFindingException(notReadyString);
			}
		}
		LotPath tempPath = new LotPath();
		LotNode tempNode = lotIn.getCurNode();
		LotNode lastNode = null;
		Random rand = new Random();
		//System.out.println("Calculating new path...");
		try{
			//System.out.println("start loop...");
			int edgeIndexInTempListToGoDown = 0;
			lastNode = tempNode;
			while(tempNode != lotIn.getDestNode() && tempNode.getNumEdges() > 0){
				//System.out.println("\tEdges: " + tempEdgeList.toString() + " Size: "+ tempEdgeList.size() +"\n\ttempNode: " + tempNode.toString());
				//System.out.println("\tGetting random edge...");
				if(tempNode.getNumEdges() == 1){//if there is only one path
					edgeIndexInTempListToGoDown = 0;
				}else{
					edgeIndexInTempListToGoDown = (rand.nextInt((tempNode.getNumEdges() - 1)));
					//don't go down edge we came from
					if(tempNode.hasEdgeTo(lastNode)){
						while(edgeIndexInTempListToGoDown == tempNode.getEdgeIndex(tempNode.getEdgeTo(lastNode))){
							edgeIndexInTempListToGoDown = (rand.nextInt((tempNode.getNumEdges() - 1)));
						}
					}
				}
				//if end node chosen has an end node, else go choose again (after error checking) 
				if(tempNode.getEdge(edgeIndexInTempListToGoDown).getEndNode() != null){
					lastNode = tempNode;
					//System.out.println("\t\tEdge going down: " + edgeIndexInTempListToGoDown);
					//System.out.println("\tAdding to new path...");
					tempPath.path.add(tempNode.getEdge(edgeIndexInTempListToGoDown));
					//System.out.println("\tUpdating temp node...");
					tempNode = lotIn.mainGraph.getOtherNode(tempNode, tempNode.getEdge(edgeIndexInTempListToGoDown));
				}else{//if the node we got was null, check to see if we are in a dead end, and account for it.
					if(tempNode.getNumEdges() <= 1){//if the node we are at only has one edge (the one we chose)
						if(lastNode.getNumEdges() <= 1){//if the last node only has one edge (the one we went down)
							LotNode innerTempNode = lastNode;
							tempPath.path.removeLast();
							while(innerTempNode.getNumEdges() <= 1){//go backwards until we are no longer in a dead end
								if(lotIn.mainGraph.hasNode(tempPath.path.getLast())){
									try{
										innerTempNode = lotIn.mainGraph.getNode(tempPath.path.getLast());
									}catch(LotGraphException err){
										System.out.println("FATAL ERROR- findRandomPath(BotLot)- You should not get this. Could not back out of a dead end.");
										System.exit(1);
									}
									tempPath.path.removeLast();
								}else{
									System.out.println("FATAL ERROR- findRandomPath(BotLot)- You should not get this. Could not back out of a dead end.");
									System.exit(1);
								}
							}
							tempNode = innerTempNode;
							lastNode = lotIn.mainGraph.getNode(tempPath.path.getLast());
						}else{
							tempNode = lastNode;
							tempPath.path.removeLast();
						}
					}
				}
			}
		}catch(LotGraphException err){
			throw new BotLotPathFindingException("There was an error when trying to generate a random path. Error: " + err.getMessage());
			//System.exit(1);
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
			//check if curNode is directly connected to destNode (avoid much processing)
			if(lotIn.getCurNode().getConnectedNodes().contains(lotIn.getDestNode())){
				return true;
			}
			//else have to go find it, following edges
			ArrayList<LotNode> nodesConnected = lotIn.getCurNode().getConnectedNodes();//nodes that are connected to the curNode (and nodes subsequently attatched to them, and so on...)
			ArrayList<LotNode> tempList = new ArrayList<LotNode>();
			ArrayList<LotNode> nodesFinished = new ArrayList<LotNode>();//nodes we have hit in the past, to not go in circles
			
			nodesFinished.add(lotIn.getCurNode());
			nodesConnected.removeAll(nodesFinished);//ensure none of the edges pointed back to curNode
			while(true){
				//for each in nodes connected, get nodes connected to them (if not one already searched)
				//	essentially does this by emptying the list as it goes, saving memory and an index to keep track of
				while(nodesConnected.size() != 0){
					//check if dest node is in this node's connected set
					if(nodesConnected.get(0).getConnectedNodes().contains(lotIn.getDestNode())){
						return true;
					}
					//put nodes held by node into tempList
					for(LotNode curNode : nodesConnected.get(0).getConnectedNodes()){
						if(!nodesFinished.contains(curNode) && curNode != null){
							tempList.add(curNode);//could get possible duplicates in finished without checking
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
			}//running loop (runs indefinitely, until the algorithm finishes)
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