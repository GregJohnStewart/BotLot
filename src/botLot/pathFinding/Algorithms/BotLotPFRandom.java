package botLot.pathFinding.Algorithms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

import botLot.BotLot;
import botLot.lotGraph.LotEdge;
import botLot.lotGraph.LotGraph;
import botLot.lotGraph.LotNode;
import botLot.lotGraph.LotPath;
import botLot.lotGraph.LotPathException;
import botLot.pathFinding.BotLotPFException;
import botLot.pathFinding.BotLotPFWorkers;

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
 * @version	1.0 3/7/16
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
	 * @param lotIn	The BotLot object to take values from.
	 * @param edgesToAvoidIn	Edges to avoid going down.
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
	public BotLotPFRandom(LotGraph graphIn, LotNode curNodeIn, LotNode destNodeIn) throws BotLotPFAlgException {
		super(graphIn, curNodeIn, destNodeIn);
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
	public BotLotPFRandom(LotGraph graphIn, LotNode curNodeIn, LotNode destNodeIn, ArrayList<LotEdge> edgesToAvoidIn) throws BotLotPFAlgException {
		super(graphIn, curNodeIn, destNodeIn, edgesToAvoidIn);
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
	}//getRandNum(int, int)
	
	/**
	 * Returns a random edge out of the set given.
	 * 
	 * @param edgeList	The list of edges to choose from. 
	 * @return	A random edge from the set.
	 */
	private static LotEdge getRandEdge(ArrayList<LotEdge> edgeList){
		if(edgeList.size() == 1){
			return edgeList.get(0);
		}else{
			return edgeList.get(getRandNum(0,edgeList.size()));
		}
	}//getRandEdge(ArrayList<LotEdge>)
	
	@Override
	protected LotPath calculatePath() throws BotLotPFAlgException {
		//System.out.println("entered findRandomPath(BotLot)");
		LotPath curPath = new LotPath();
		LotNode curNode = this.getCurNode();
		LotEdge tempEdge = null;
		ArrayList<LotEdge> tempList = null;
		while(true){
			//choose path to go down at random
			tempList = curNode.getConnectedEdges();
			tempList.removeAll(this.getEdgesToAvoid());			
			try {
				do{
					tempEdge = getRandEdge(tempList);	
				}while(!BotLotPFWorkers.hasPath(this.getGraph(), tempEdge.getEndNode(), this.getDestNode()));
			} catch (BotLotPFException e1) {
				e1.printStackTrace();
				System.out.println("FATAL ERROR- This should not happen.");
				System.exit(1);
			}
			try {
				curPath.append(tempEdge);
				curNode = tempEdge.getEndNode();
			} catch (LotPathException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("FATAL ERROR- This should not happen.");
				System.exit(1);
			}
			if(curNode == this.getDestNode()){
				return curPath;
			}
		}//main loop
	}//calculatePath()
}//class BotLotPFRandom
