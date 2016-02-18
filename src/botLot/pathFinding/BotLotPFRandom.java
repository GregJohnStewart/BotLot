package botLot.pathFinding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

import botLot.BotLot;
import botLot.lotGraph.LotEdge;
import botLot.lotGraph.LotGraph;
import botLot.lotGraph.LotGraphException;
import botLot.lotGraph.LotNode;
import botLot.lotGraph.LotPath;

/**
 * Finds a path by randomly selecting edges on each node.
 * <p>
 * Obviously not a great way to do this. Done for testing simple things. Or directing something for the scenic route.
 * <p>
 * Started: 2/13/16
 * <p>
 * TODO:: rewrite to ignore edges to avoid.
 * 
 * @author Greg Stewart
 * @version	1.0 2/13/16
 */
public class BotLotPFRandom extends BotLotPFAlgorithm {	
	/**
	 * Constructor using a BotLot object.
	 * 
	 * @param lotIn	The BotLot object to use.
	 */
	public BotLotPFRandom(BotLot lotIn) {
		super(lotIn);
	}
	
	/**
	 * Constructor using a BotLot object and a list of edges to avoid.
	 * 
	 * @param lotIn
	 * @param edgesToAvoidIn
	 */
	public BotLotPFRandom(BotLot lotIn, Collection<LotEdge> edgesToAvoidIn) {
		super(lotIn, edgesToAvoidIn);
	}
	
	/**
	 * Basic constructor for path finding.
	 */
	public BotLotPFRandom() {
		super();
	}
	
	/**
	 * Constructor that initializes all the variables separately.
	 * 
	 * @param graphIn	The graph to use.
	 * @param curNodeIn	The node we are starting at.
	 * @param destNodeIn	The node we are going to.
	 * @throws BotLotPFException  If curNode and/or destNode cannot be set.
	 */
	public BotLotPFRandom(LotGraph graphIn, LotNode curNodeIn, LotNode destNodeIn) throws BotLotPFException {
		super(graphIn, curNodeIn, destNodeIn);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Constructor that initializes everything, actually everything. Including the list of nodes to avoid.
	 * 
	 * @param graphIn	The graph to use.
	 * @param curNodeIn	The node we are starting at.
	 * @param destNodeIn	The node we are going to.
	 * @param edgesToAvoidIn	Edges to not go down ever.
	 * @throws BotLotPFException	If curNode and/or destNode cannot be set.
	 */
	public BotLotPFRandom(LotGraph graphIn, LotNode curNodeIn, LotNode destNodeIn, ArrayList<LotEdge> edgesToAvoidIn) throws BotLotPFException {
		super(graphIn, curNodeIn, destNodeIn, edgesToAvoidIn);
		// TODO Auto-generated constructor stub
	}

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
	
	@Override
	protected LotPath calculatePath() throws BotLotPFException {
		//System.out.println("entered findRandomPath(BotLot)");
		LotPath tempPath = new LotPath();
		LotNode tempNode = this.getCurNode();
		LotNode lastNode = null;
		/*
		//int loopCount = 0;//# times caught in loop
		//System.out.println("Calculating new path...");
		try{
			//System.out.println("start loop...");
			int edgeIndexInTempListToGoDown = 0;
			lastNode = tempNode;
			
			while(tempNode != this.getDestNode()){
				//TODO:: have hashmap to tell it not to go down trapping edge again
				if(tempPath.hasWaypoint(tempNode, false) && tempPath.size() > 1){
					//System.out.println("In loop. Retrying...");
					//loopCount++;
					tempPath = new LotPath();
					tempNode = this.getCurNode();
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
								if(this.getGraph().hasNode(tempPath.path.getLast())){
									try{
										innerTempNode = this.getGraph().getNode(tempPath.path.getLast());
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
							lastNode = this.getGraph().getNode(tempPath.path.getLast());
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
					tempNode = this.getGraph().getOtherNode(tempNode, tempNode.getEdge(edgeIndexInTempListToGoDown));
				}else{//if the node we got was null, check to see if we are in a dead end, and account for it.
					
				}
				//System.out.println("\tEOI. Edges: " + tempPath.toString() + " tempNode: " + tempNode.toString());
			}//running loop
		}catch(LotGraphException err){
			throw new BotLotPFException("There was an error when trying to generate a random path. Error: " + err.getMessage());
			//System.exit(1);
		}
		*/
		//System.out.println("# times caught in loop: " + loopCount);
		return tempPath;
	}
}
