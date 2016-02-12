package botLot;
import java.util.ArrayList;//for dealing with the graph structure itself.
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import botLot.lotGraph.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
/**
 * BotLotPF.java
 * <p>
 * Functions needed to find paths in the BotLot class.
 * <p>
 * Started: 11/7/15
 * <p>
 * TODO::
 * 	Implement conditional searches
 * 
 * @author Greg Stewart
 * @version	1.0 1/20/16
 */
public class BotLotPF {
	/** Used to see if we already checked the BotLot object for correctness */
	private static boolean readyPathChecked = false;
	/** The string passed to the BotLotPFException when the BotLot object given is not ready. */
	public static final String notReadyString = "The BotLot Object is either not ready, or has no path between current location and destination.";
	/** The minimum ratio value to do random path generation in {@link #getShortestPath(BotLot)} */
	public static final float ratioThreshHold = (float) 0.75;
	/** The number of times to do the random generation to attempt a shortest path */
	private static final int numTimesToDoRand = 5;
	/** The string passed to the BotLotPFException when the gets to the end of a line. */
	public static final String eolString = "End of the line of nodes.";
	
	/**
	 * Determines which path taking algorithm is probably best to use.
	 * 
	 * @param lotIn	The graph structure to deal with.
	 * @return	A new path from the current node in lotIn to the destination node in that structure.
	 * @throws BotLotPFException	If the LotGraph given is not ready for path finding.
	 */
	public static LotPath getShortestPath(BotLot lotIn) throws BotLotPFException{
		if(!readyPathCheck(lotIn)){
			throw new BotLotPFException(notReadyString);
		}
		readyPathChecked = true;
		LotPath pathFound = null;
		
		//determine what algorithm to use.....
		System.out.println("\tnode/edge ratio: " + lotIn.mainGraph.getNodeEdgeRatio() + "\n\tThreshhold: " + ratioThreshHold);
		if(lotIn.mainGraph.getNodeEdgeRatio() >= ratioThreshHold){
		//if(true){//testing
			System.out.println("\tRatio within bounds. Doing random path gen.");
			/*If about the same amount of nodes to edges, get best of a few random path gens
			 * 
			 * Idea being that the paths throughout the data set are fairly linear, therefore this algorithm should run with O(# edges between curNode and destNode).
			 */
			double curBest = Double.POSITIVE_INFINITY;
			LotPath tempPath = null;
			for(int i = 0; i < numTimesToDoRand; i++){
				tempPath = findRandomPath(lotIn);
				if(tempPath.getPathMetric() < curBest){
					pathFound = tempPath;
					curBest = tempPath.getPathMetric();
				}
			}
			System.out.println("Path: " + tempPath.toString());
		}else{
			System.out.println("\tToo complicated of a graph. Getting specific shortest path.");
			pathFound = getExactPath(lotIn);
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
	 * @throws BotLotPFException If the LotGraph object isn't ready for path generation.
	 */
	public static LotPath doDijkstra(BotLot lotIn) throws BotLotPFException{
		if(!readyPathChecked){
			if(!readyPathCheck(lotIn)){
				throw new BotLotPFException(notReadyString);
			}
		}
		//TODO:: use Dijkstra's algorithm to find a shortest path
		throw new NotImplementedException();
	}//doDijkstra(BotLot)
	
	/**
	 * Normalized random num gen. Exclusive of max, because used with indexes.
	 * 
	 * @param min	The minimum value you want.
	 * @param max	The top cap you want. Will never be greater than (max - 1). 
	 * @return	A random value between these two.
	 */
	private static int getRandNum(int min, int max){
		return ThreadLocalRandom.current().nextInt(min, max);
	}
	
	/**
	 * Finds a path by randomly selecting edges on each node.
	 * <p>
	 * Obviously not a great way to do this. Done for testing simple things. Or directing something for the scenic route.
	 * <p>
	 * TODO:: fix for edges that are not completed
	 * 
	 * @param lotIn The LotGraph structure to deal with.
	 * @return	A new random path from the current node in lotIn to the destination node in that structure.
	 * @throws BotLotPFException If the BotLot object given is not ready for path generation.
	 */
	public static LotPath findRandomPath(BotLot lotIn) throws BotLotPFException{
		//System.out.println("entered findRandomPath(BotLot)");
		if(!readyPathChecked){
			if(!readyPathCheck(lotIn)){
				throw new BotLotPFException(notReadyString);
			}
		}
		LotPath tempPath = new LotPath();
		LotNode tempNode = lotIn.getCurNode();
		LotNode lastNode = null;
		//int loopCount = 0;//# times caught in loop
		//System.out.println("Calculating new path...");
		try{
			//System.out.println("start loop...");
			int edgeIndexInTempListToGoDown = 0;
			lastNode = tempNode;
			
			while(tempNode != lotIn.getDestNode()){
				//TODO:: have hashmap to tell it not to go down trapping edge again
				if(tempPath.hasWaypoint(tempNode, false) && tempPath.size() > 1){
					//System.out.println("In loop. Retrying...");
					//loopCount++;
					tempPath = new LotPath();
					tempNode = lotIn.getCurNode();
					lastNode = tempNode;
					edgeIndexInTempListToGoDown = 0;
				}

				//System.out.println("\tGetting random edge...");
				//System.out.println("\t\t# edges: " + tempNode.getNumEdges());
				if(tempNode.getNumEdges() == 0){
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
				}else if(tempNode.getNumEdges() == 1){//if there is only one path
					edgeIndexInTempListToGoDown = 0;
					//System.out.println("\t\tOnly one path.");
				}else{
					//System.out.println("\t\tMultiple paths.");
					edgeIndexInTempListToGoDown = getRandNum(0, tempNode.getNumEdges());
					//don't go down edge we came from
					if(tempNode.hasEdgeTo(lastNode)){
						while(edgeIndexInTempListToGoDown == tempNode.getEdgeIndex(tempNode.getEdgeTo(lastNode))){
							edgeIndexInTempListToGoDown = getRandNum(0, tempNode.getNumEdges());
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
					
				}
				//System.out.println("\tEOI. Edges: " + tempPath.toString() + " tempNode: " + tempNode.toString());
			}//running loop
		}catch(LotGraphException err){
			throw new BotLotPFException("There was an error when trying to generate a random path. Error: " + err.getMessage());
			//System.exit(1);
		}
		//System.out.println("# times caught in loop: " + loopCount);
		return tempPath;
	}//findRandomPath(BotLot)
	
	/**
	 * Gets a list of edges that would put a searching algorithm into a trapped state. Trapped meaning they can no longer reach the destination node.
	 * 
	 * @param lotIn	The BotLot object we are dealing with.
	 * @return	A list of edges that will trap a navigational driver from getting to the destination node.
	 * @throws BotLotPFException If the object given is not ready for path generation.
	 * 
	 * TODO:: rewrite to make it work well
	 */
	public static ArrayList<LotEdge> getTrapEdges(BotLot lotIn) throws BotLotPFException{
		if(lotIn.ready(false)){
			//else have to go find it, following edges
			ArrayList<LotNode> nodesConnected = lotIn.getCurNode().getConnectedNodes();//nodes that are connected to the curNode (and nodes subsequently attatched to them, and so on...)
			ArrayList<LotNode> tempList = new ArrayList<LotNode>();//temporary list of connected nodes
			ArrayList<LotNode> nodesFinished = new ArrayList<LotNode>();//nodes we have hit in the past, to not go in circles
			
			ArrayList<LotEdge> trapEdgeList = new ArrayList<LotEdge>();
			
			nodesFinished.add(lotIn.getCurNode());//add the current node to list of nodes we have hit
			nodesConnected.removeAll(nodesFinished);//ensure none of the edges pointed back to curNode
			while(true){
				//for each in nodes connected, get nodes connected to them (if not one already searched)
				//	essentially does this by emptying the list as it goes, saving memory and an index to keep track of
				while(nodesConnected.size() != 0){
					//TODO:: this sometimes throws a null pointer exception (nodesConnected.get(0) == null). figure out why.
					LotNode curNode = null;
					curNode = nodesConnected.get(0);
					//System.out.println("On: " + curNode);
					
					if(nodesFinished.contains(curNode)){//in case we get to a node already finished
						nodesConnected.remove(0);
						continue;
					}
					//so we don't go back on this one
					nodesFinished.add(curNode);
					
					//add to completed what edges don't trap things. Add to list those that do.
					for(LotNode tempNode : curNode.getConnectedNodes()){
						try {
							//TODO:: should this work?
							//if(!hasPath(lotIn, curNode, tempNode) && tempNode != lotIn.getDestNode()){
							//System.out.println("Testing if there is a path from " + tempNode.toString() + " to destNode....");
							if(!hasPath(lotIn, tempNode, lotIn.getDestNode()) && tempNode != lotIn.getDestNode()){
									//System.out.println("\tThere isnt!");
								trapEdgeList.addAll(lotIn.mainGraph.getEdgesFromTo(curNode, tempNode));
							}else{
								//System.out.println("\tThere is!");
								if(!nodesFinished.contains(tempNode) && tempNode != null){
									tempList.add(tempNode);
								}
							}
						} catch (BotLotPFException e) {
							e.printStackTrace();
							System.exit(1);
						} catch (LotGraphException e) {
							e.printStackTrace();
							System.exit(1);
						}
					}
					nodesConnected.remove(0);
				}
				//System.out.println("done with this set.");
				
				//if we got this far and no nodes are in the temp list, there is nothing else we can do, so there is no path
				if(tempList.size() == 0){
					return trapEdgeList;
				}else{//else we move nodes we have connected from tempList to nodesConnected
					nodesConnected = new ArrayList<LotNode>(tempList);
					tempList = new ArrayList<LotNode>();
				}
			}//running loop (runs indefinitely, until the algorithm finishes)
		}//if got valid stuff
		throw new BotLotPFException("LotGraph not ready to determine path.");
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
	 */
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
				System.out.println("\tNew Node.");
			}else{
				hitCounts.put(curEdge.getEndNode(), (hitCounts.get(curEdge.getEndNode()) + 1));
				System.out.println("\tBeen here before. Count: " + hitCounts.get(curEdge.getEndNode()));
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
	 */
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
	
	/**
	 * Determines if there is a path from the curNode to the destNode.
	 * <p>
	 * Uses a multi-threaded algorithm to accomplish this quickly for very large and/or complex data sets.
	 * 
	 * @param lotIn	The BotLot object to use.
	 * @param curNode	The node we are starting at.
	 * @param destNode	The node we want to go to.
	 * @return	If there is a path between curNode and destNode.
	 * @throws BotLotPFException	If the data is not ready, or does not have the given nodes.
	 */
	public static boolean hasPath(BotLot lotIn, LotNode curNode, LotNode destNode) throws BotLotPFException{
		final int minsToWait = Integer.MAX_VALUE;
		if(!lotIn.mainGraph.hasNode(destNode) || !lotIn.mainGraph.hasNode(curNode)){
			throw new BotLotPFException("LotGraph does not have given destination or current node in data.");
		}else{
			//check if curNode is directly connected to destNode (avoid much processing)
			if(curNode.getConnectedNodes().contains(destNode)){
				return true;
			}
			//else have to go find it, following edges
			ArrayList<LotNode> nodesConnected = curNode.getConnectedNodes();//nodes that are connected to the curNode (and nodes subsequently attatched to them, and so on...)
			ArrayList<LotNode> tempList = new ArrayList<LotNode>();//temporary list of connected nodes
			ArrayList<LotNode> nodesFinished = new ArrayList<LotNode>();//nodes we have hit in the past, to not go in circles
			ExecutorService es;//the thread pool for multithreading
			
			nodesFinished.add(curNode);//add the current node to list of nodes we have hit
			nodesConnected.removeAll(nodesFinished);//ensure none of the edges pointed back to curNode
			while(true){
				//for each in nodes connected, get nodes connected to them (if not one already searched)
				//	essentially does this by emptying the list as it goes, saving memory and an index to keep track of
				es = Executors.newCachedThreadPool();
				while(nodesConnected.size() != 0){
					//TODO:: this sometimes throws a null pointer exception (nodesConnected.get(0) == null). figure out why.
					LotNode curNodeInner = null;
					try{
						curNodeInner = nodesConnected.get(0);
					}catch(NullPointerException e){
						return hasPath(lotIn);
					}
					//System.out.println("On: " + curNodeInner);
					if(curNodeInner.getConnectedNodes().contains(destNode)){//if we found the node we are going to
						//System.out.println("\tFound! This node connects to the destination node.");
						return true;
					}else if(nodesFinished.contains(curNodeInner)){//in case we get to a node already finished
						nodesConnected.remove(0);
						continue;
					}
					//so we don't go back on this one
					nodesFinished.add(curNodeInner);
					
					es.execute(new BotLotPFHasPathThread(
							"BotLotPFHasPathThread-" + curNodeInner.getId(),
							nodesFinished,
							curNodeInner.getConnectedNodes(),
							tempList
							));
					nodesConnected.remove(0);
				}
				//System.out.println("done with this set.");
				es.shutdown();
				try {
					es.awaitTermination(minsToWait, TimeUnit.MINUTES);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
					System.exit(1);
				}
				//if we got this far and no nodes are in the temp list, there is nothing else we can do, so there is no path
				if(tempList.size() == 0){
					return false;
				}else{//else we move nodes we have connected from tempList to nodesConnected
					nodesConnected = new ArrayList<LotNode>(tempList);
					tempList = new ArrayList<LotNode>();
				}
			}//running loop (runs indefinitely, until the algorithm finishes)
		}//if got valid stuff
	}//hasPath(BotLot, LotNode, LotNode)
	
	/**
	 * Determines if there is a path from the curNode to the destNode.
	 * <p>
	 * Uses a multi-threaded algorithm to accomplish this quickly for very large and/or complex data sets.
	 * 
	 * @param lotIn	The BotLot that we are dealing with.
	 * @return	If there is a path from the curNode to the destNode of the BotLot given.
	 * @throws BotLotPFException If the BotLot object given isn't ready for path generation.
	 */
	public static boolean hasPath(BotLot lotIn) throws BotLotPFException{
		return hasPath(lotIn, lotIn.getCurNode(), lotIn.getDestNode());
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
		} catch (BotLotPFException e) {
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