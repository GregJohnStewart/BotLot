package botLot.pathFinding;

import java.util.ArrayList;

import botLot.BotLot;
import botLot.lotGraph.LotEdge;
import botLot.lotGraph.LotGraph;
import botLot.lotGraph.LotGraphException;
import botLot.lotGraph.LotNode;
import botLot.lotGraph.LotPath;
import botLot.pathFinding.Algorithms.BotLotPFAlgorithm;

/**
 * Worker methods for path finding.
 * <p>
 * Started: 2/13/16
 * <p>
 * TODO:: Javadoc stuff
 * 
 * @author Greg Stewart
 * @version	1.0 3/7/16
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
		//System.out.println("in readyCheck()");
		if(graphIn != null || graphIn != new LotGraph()){
			//System.out.println("has graph. " + curNode + " " + destNode);
			if(graphIn.hasNode(curNode) && graphIn.hasNode(destNode)){
				//System.out.println("has cur and dest");
				if(checkPath){
					try {
						return hasPath(graphIn, curNode, destNode);
					} catch (BotLotPFException e) {
						e.printStackTrace();
						System.out.println("FATAL ERR- readyCheck(LotGraph, curNode, destNode);- this should not happen. Error: " + e.getMessage());
						System.exit(1);
					}
				}
				//System.out.println("ready.");
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
	 * @param graphIn	The graph we are dealing with.
	 * @param curNode	The node we are starting at.
	 * @param destNode	The node we are trying to get to.
	 * @return	A list of edges that would trap a path finding algorithm.
	 * @throws BotLotPFException	If something went wrong.
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
	 * 
	 * @param graphIn	The graph we are dealing with.
	 * @param curNode	The node we are starting at.
	 * @param destNode	The node we are trying to get to.
	 * @param edgesToAvoid	Edges we are trying to avoid.
	 * @return	If there is a path from the current node to the dest node.
	 * @throws BotLotPFException	if something went wrong.
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
	 * 
	 * @param algIn	The algorithm to get information from.
	 * @return	If there is a path between the curNode and destNode of the algorithm object.
	 * @throws BotLotPFException	If something went wrong.
	 */
	public static boolean hasPath(BotLotPFAlgorithm algIn) throws BotLotPFException{
		return hasPath(algIn.getGraph(), algIn.getCurNode(), algIn.getDestNode(), algIn.getEdgesToAvoid());
	}//hasPath(BotLot)
	
	/**
	 * Checks if the given path is valid, and actually describes a path through the graph.
	 * 
	 * @param pathIn	The path to check if it is valid.
	 * @param graphIn	The graph this path is associated with.
	 * @param curNode	The node we are currently at.
	 * @param destNode	The node we are trying to get to.
	 * @param edgesToAvoid	Edges to not go down.
	 * @return	If the path given is valid.
	 * @throws BotLotPFException	If something goes wrong.
	 */
	public static boolean pathIsValid(LotPath pathIn, LotGraph graphIn, LotNode curNode, LotNode destNode, ArrayList<LotEdge> edgesToAvoid) throws BotLotPFException{
		//System.out.println("Entered PathIsValid");
		if(readyCheck(graphIn, curNode, destNode, false)){
			if(pathIn.size() == 0){
				return false;
			}
			if(!pathIn.pathIsContinuous() | pathIn.path.getLast().getEndNode() != destNode){
				//System.out.println("\tPath not continuous");
				return false;
			}
			LotNode curTempNode = curNode;
			ArrayList<LotEdge> tempEdgeList = null;
			for(LotEdge curEdge : pathIn.path){
				tempEdgeList = curTempNode.getConnectedEdges(edgesToAvoid);
				if(tempEdgeList.contains(curEdge)){
					curTempNode = curEdge.getEndNode();
					if(curTempNode == destNode){
						//System.out.println("\tPath Founds to be Valid");
						return true;
					}
				}else{
					//System.out.println("\t" + curTempNode + " does not have edge " + curEdge);
					return false;
				}
			}
			//System.out.println("Didnt reach end.");
			return false;
		}else{
			throw new BotLotPFException("Data given is not ready to be operated on.");
		}
	}//pathIsValid(LotPath, LotGraph, LotNode, LotNode, ArrayList<LotEdge>) throws BotLotPFException
	
	/**
	 * Checks if the given path is valid, and actually describes a path through the graph. 
	 * 
	 * @param pathIn	The path to check.
	 * @param lotIn	The BotLot object to check against.
	 * @return	If the path given is valid.
	 * @throws BotLotPFException	If something went wrong.
	 */
	public static boolean pathIsValid(LotPath pathIn, BotLot lotIn) throws BotLotPFException{
		//System.out.println("Data: " + lotIn.getCurNode() + " " + lotIn.getDestNode());
		return pathIsValid(pathIn, lotIn.mainGraph, lotIn.getCurNode(), lotIn.getDestNode(), new ArrayList<LotEdge>());
	}
	
	/**
	 * Checks if the path held in the BotLot object is valid, and actually describes a path through the graph.
	 * 
	 * @param lotIn	The BotLot object we care about.
	 * @return	If the curPath in the object is valid.
	 * @throws BotLotPFException	If something went wrong.
	 */
	public static boolean pathIsValid(BotLot lotIn) throws BotLotPFException{
		return pathIsValid(lotIn.getCurPath(), lotIn);
	}
	
	/**
	 * If the path given is valid based on the values held in the given algorithm.
	 * 
	 * @param pathIn	The path to check.
	 * @param algIn	The algorithm to get information about.
	 * @return	If the path given is valid.
	 * @throws BotLotPFException	If something went wrong.
	 */
	public static boolean pathIsValid(LotPath pathIn, BotLotPFAlgorithm algIn) throws BotLotPFException{
		return pathIsValid(pathIn, algIn.getGraph(), algIn.getCurNode(), algIn.getDestNode(), algIn.getEdgesToAvoid());
	}
	
}//classBotLotPFWorkers
