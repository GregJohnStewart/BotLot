package botLot.pathFinding;

import java.util.ArrayList;

import botLot.BotLot;
import botLot.lotGraph.LotEdge;
import botLot.lotGraph.LotGraph;
import botLot.lotGraph.LotGraphException;
import botLot.lotGraph.LotNode;

/**
 * Worker methods for path finding.
 * <p>
 * Started: 2/13/16
 * <p>
 * TODO:: Javadoc stuff
 * 
 * @author Greg Stewart
 * @version	1.0 2/13/16
 */
public final class BotLotPFWorkers {
	/** The string to be used when throwing exceptions when the data given is not ready. */
	public static final String notReadyMessage = "The data entered is invalid; not possible to do operation with the data entered. Possible issues: One or more of them are null, the nodes entered are not part of the graph entered, there is no path between the two nodes entered.";
	
	/**
	 * Checks if the data given is ready to have actions performed on them.
	 * 
	 * @param graphIn	The graph we are testing about.
	 * @param curNode	The current node we are at.
	 * @param destNode	The node we are trying to get to.
	 * @param checkPath	If we want to check if a path exists between the two.
	 * @return	If all the data is ready.
	 */
	public static boolean readyCheck(LotGraph graphIn, LotNode curNode, LotNode destNode, boolean checkPath){
		if(graphIn != null || graphIn != new LotGraph()){
			if(graphIn.hasNode(curNode) && graphIn.hasNode(destNode)){
				if(checkPath){
					try {
						return hasPath(graphIn, curNode, destNode);
					} catch (BotLotPFException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.out.println("FATAL ERR- readyCheck(LotGraph, curNode, destNode);- this should not happen. Error: " + e.getMessage());
						System.exit(1);
					}
				}
				return true;
			}
		}
		return false;
	}//readyCheck(LotGraph)
	
	/**
	 * Checks if the data in a BotLot object is ready to have actions performed on it.
	 * 
	 * @param lotIn	The BotLot object we are testing.
	 * @param checkPath	If we want to check the path or not.
	 * @return	If the data in a BotLot object is ready to have actions performed on it.
	 */
	public static boolean readyCheck(BotLot lotIn, boolean checkPath){
		return readyCheck(lotIn.mainGraph, lotIn.getCurNode(), lotIn.getDestNode(), checkPath);
	}
	
	/**
	 * Checks if the data in an algorithm object is ready to have actions performed on it.
	 * 
	 * @param algIn	The algorithm object we are testing.
	 * @param checkPath	If we want to check the path or not.
	 * @return	If the data in an algorithm object is ready to have actions performed on it.
	 */
	public static boolean readyCheck(BotLotPFAlgorithm algIn, boolean checkPath){
		return readyCheck(algIn.getGraph(), algIn.getCurNode(), algIn.getDestNode(), checkPath);
	}
	
	/**
	 * Gets a list of edges that would put a searching algorithm into a trapped or dead end state. Trapped meaning they can no longer reach the destination node.
	 * 
	 * @param lotIn	The BotLot object we are dealing with.
	 * @param curNode The current node we are at.
	 * @param destNode The node we are trying to get to.
	 * @return	A list of edges that will trap a navigational driver from getting to the destination node.
	 * @throws BotLotPFException If the object given is not ready for path generation.
	 * 
	 * TODO:: rewrite to make use of new input
	 * TODO:: review javadoc documentation
	 */
	public static ArrayList<LotEdge> getTrapEdges(LotGraph graphIn, LotNode curNode, LotNode destNode) throws BotLotPFException{
		if(readyCheck(graphIn, curNode, destNode, true)){
			//System.out.println("Entered getTrapEdges()");
			ArrayList<LotEdge> trapEdgeList = new ArrayList<LotEdge>();//nodes that will trap an algorithm from getting to the destination
			ArrayList<LotNode> nodesConnected = new ArrayList<LotNode>();//nodes that are connected to the curNode (and nodes subsequently attatched to them, and so on...)
			nodesConnected.add(curNode);
			ArrayList<LotNode> tempList = new ArrayList<LotNode>();//temporary list of connected nodes
			ArrayList<LotNode> workingList = new ArrayList<LotNode>();//temporary list of connected nodes
			ArrayList<LotNode> nodesFinished = new ArrayList<LotNode>();//nodes we have hit in the past, to not go in circles
			LotNode workingNode = null;
			
			//nodesFinished.add(curNode);
			//nodesConnected.removeAll(nodesFinished);

			//System.out.println("Start finding the trapping edges....");
			while(true){
				while(nodesConnected.size() != 0){
					workingNode = nodesConnected.get(0);
					workingList = workingNode.getConnectedNodes();
					//System.out.println("\tWorking off of node: " + workingNode);
					for(LotNode tempNode : workingList){
						try {
							//System.out.println("\t\tExamining edge to: " + tempNode);
							//only deal with it if we haven't been here before
							if(!nodesFinished.contains(tempNode) && !nodesConnected.contains(tempNode) && !tempList.contains(tempNode)){
								
								if(!hasPath(graphIn, tempNode, destNode, trapEdgeList)){
									//System.out.println("\t\t\tHas no path to destination********");
									trapEdgeList.addAll(graphIn.getEdgesFromTo(workingNode, tempNode));
								}else{
									//System.out.println("\t\t\tHas path to destination.");
									tempList.add(tempNode);
								}
							}else{
								//System.out.println("\t\t\tHas been examined before.");
							}
							
						} catch (LotGraphException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							System.out.println("FATAL ERR- hasPath();- this should not happen. Error: " + e.getMessage());
							System.exit(1);
						}
					}		
					nodesFinished.add(nodesConnected.remove(0));
				}//for each in nodes connectdd
				//if we got this far and no nodes are in the temp list, there is nothing else we can do, so there is no path
				if(tempList.size() == 0){
					return trapEdgeList;
				}else{//else we move nodes we have connected from tempList to nodesConnected
					nodesConnected = new ArrayList<LotNode>(tempList);
					tempList = new ArrayList<LotNode>();
				}
			}//main loop
		}//if got valid stuff
		throw new BotLotPFException("LotGraph not ready to determine path.");
	}//getTrapEdges(LotGraph, LotNode, LotNode) throws BotLotPFException
	
	/**
	 * Gets a list of edges that would trap an algorithm.
	 * 
	 * @param lotIn	The BotLot object we are dealing with.
	 * @return	A list of edges that would trap an algorithm.
	 * @throws BotLotPFException	If something is not ready, or went wrong.
	 */
	public static ArrayList<LotEdge> getTrapEdges(BotLot lotIn) throws BotLotPFException{
		return getTrapEdges(lotIn.mainGraph, lotIn.getCurNode(), lotIn.getDestNode());
	}
	
	/**
	 * Adds edges that would trap an algorithm to the algorithm object. 
	 * 
	 * @param algIn	The algorithm object to deal with.
	 * @throws BotLotPFException	If something is not ready, or went wrong.
	 */
	public static void addTrapEdges(BotLotPFAlgorithm algIn) throws BotLotPFException{
		algIn.addEdgesToAvoid(getTrapEdges(algIn.getGraph(), algIn.getCurNode(), algIn.getDestNode()));
	}
	
	/**
	 * Determines if there is a path from the curNode to the destNode.
	 * <p>
	 * Uses a multi-threaded algorithm to accomplish this quickly for very large and/or complex data sets.
	 * TODO:: fix, always returns false
	 * 
	 * @param lotGraph	The graph object to use.
	 * @param curNode	The node we are starting at.
	 * @param destNode	The node we want to go to.
	 * @param edgesToAvoid	A list of edges to not go down.
	 * @return	If there is a path between curNode and destNode.
	 * @throws BotLotPFException	If the data is not ready, or does not have the given nodes.
	 */
	public static boolean hasPath(LotGraph graphIn, LotNode curNode, LotNode destNode, ArrayList<LotEdge> edgesToAvoid) throws BotLotPFException{
		//final int minsToWait = Integer.MAX_VALUE;
		if(readyCheck(graphIn, curNode, destNode, false)){
			ArrayList<LotNode> nodesConnected = new ArrayList<LotNode>();//nodes that are connected to the curNode (and nodes subsequently attatched to them, and so on...)
			nodesConnected.add(curNode);
			ArrayList<LotNode> tempList = new ArrayList<LotNode>();//temporary list of connected nodes
			ArrayList<LotNode> workingList = new ArrayList<LotNode>();//temporary list of connected nodes
			ArrayList<LotNode> nodesFinished = new ArrayList<LotNode>();//nodes we have hit in the past, to not go in circles
			LotNode workingNode = null;
			
			//nodesFinished.add(curNode);
			//nodesConnected.removeAll(nodesFinished);
			while(true){
				/*go through each node,
				 * 1- get nodes connected to it
				 * 2- remove connected nodes that are connected via avoiding edges
				 * 3- test if the nodes left are the destination node
				 * 		-return true if destNode found
				 * 4- remove it (the current node) from connected, place it into nodesFinished
				 */
				while(nodesConnected.size() != 0){
					workingNode = nodesConnected.get(0);
					workingList = workingNode.getConnectedNodes();
					
					for(LotNode tempNode : workingList){
						try {
							//only deal with nodes we can get to (avoiding certain edges)
							if(!edgesToAvoid.contains(graphIn.getEdgeFromTo(workingNode, tempNode))){
								if(tempNode == destNode){
									return true;
								}
								//only add to tempList if we haven't been here before
								if(!nodesFinished.contains(tempNode) && !nodesConnected.contains(tempNode) && !tempList.contains(tempNode)){
									tempList.add(tempNode);
								}
							}
						} catch (LotGraphException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							System.out.println("FATAL ERR- hasPath();- this should not happen. Error: " + e.getMessage());
							System.exit(1);
						}
					}
					nodesFinished.add(nodesConnected.remove(0));
				}//for each in nodes connected
				//if we got this far and no nodes are in the temp list, there is nothing else we can do, so there is no path
				if(tempList.size() == 0){
					return false;
				}else{//else we move nodes we have connected from tempList to nodesConnected
					nodesConnected = new ArrayList<LotNode>(tempList);
					tempList = new ArrayList<LotNode>();
				}
			}//main loop
		}
		throw new BotLotPFException(notReadyMessage);
	}//hasPath(BotLot, LotNode, LotNode)
	
	/**
	 * Determines if there is a path from the curNode to the destNode.
	 * 
	 * @param graphIn	The graph object to use.
	 * @param curNode	The node we are starting at.
	 * @param destNode	The node we want to go to.
	 * @return	If there is a path between curNode and destNode.
	 * @throws BotLotPFException	If the data is not ready, or does not have the given nodes.
	 */
	public static boolean hasPath(LotGraph graphIn, LotNode curNode, LotNode destNode) throws BotLotPFException{
		return hasPath(graphIn, curNode, destNode, new ArrayList<LotEdge>());
	}
	
	/**
	 * Determines if there is a path from the curNode to the destNode.
	 * 
	 * @param lotIn	The BotLot that we are dealing with.
	 * @return	If there is a path from the curNode to the destNode of the BotLot given.
	 * @throws BotLotPFException If the BotLot object given isn't ready for path generation.
	 */
	public static boolean hasPath(BotLot lotIn) throws BotLotPFException{
		return hasPath(lotIn.mainGraph, lotIn.getCurNode(), lotIn.getDestNode(), new ArrayList<LotEdge>());
	}//hasPath(BotLot)
	
	/**
	 * Determines if there is a path from the curNode to the destNode.
	 * <p>
	 * Uses a multi-threaded algorithm to accomplish this quickly for very large and/or complex data sets.
	 * 
	 * @param lotIn	The BotLot that we are dealing with.
	 * @return	If there is a path from the curNode to the destNode of the BotLot given.
	 * @throws BotLotPFException If the BotLot object given isn't ready for path generation.
	 */
	public static boolean hasPath(BotLotPFAlgorithm algIn) throws BotLotPFException{
		return hasPath(algIn.getGraph(), algIn.getCurNode(), algIn.getDestNode(), algIn.getEdgesToAvoid());
	}//hasPath(BotLot)
	
}//class BotLotPFWorkers
