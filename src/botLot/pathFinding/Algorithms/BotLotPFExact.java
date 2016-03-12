package botLot.pathFinding.Algorithms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import botLot.BotLot;
import botLot.lotGraph.LotEdge;
import botLot.lotGraph.LotGraph;
import botLot.lotGraph.LotNode;
import botLot.lotGraph.LotPath;
import botLot.lotGraph.LotPathException;
import botLot.pathFinding.BotLotPFException;
/**
 * Finds an exact shortest path.
 * <p>
 * Started: 2/13/16
 * <p>
 * TODO:: rewrite to ignore edges to avoid.
 * 
 * @author Greg Stewart
 * @version	1.0 2/13/16
 */
public class BotLotPFExact extends BotLotPFAlgorithm {
	/** The string passed to the BotLotPFException when the gets to the end of a line. */
	public static final String eolString = "End of the line of nodes.";
	
	/**
	 * Basic constructor for path finding.
	 */
	public BotLotPFExact() {
		super();
	}
	
	/**
	 * Constructor using a BotLot object.
	 * 
	 * @param lotIn	The BotLot object to use.
	 */
	public BotLotPFExact(BotLot lotIn) {
		super(lotIn);
	}
	
	/**
	 * Constructor using a BotLot object and a list of edges to avoid.
	 * 
	 * @param lotIn
	 * @param edgesToAvoidIn
	 */
	public BotLotPFExact(BotLot lotIn, Collection<LotEdge> edgesToAvoidIn) {
		super(lotIn, edgesToAvoidIn);
	}
	
	/**
	 * Constructor that initializes everything, actually everything. Including the list of nodes to avoid.
	 * 
	 * @param graphIn	The graph to use.
	 * @param curNodeIn	The node we are starting at.
	 * @param destNodeIn	The node we are going to.
	 * @param edgesToAvoidIn	Edges to not go down ever.
	 * @throws BotLotPFAlgException	If curNode and/or destNode cannot be set.
	 */
	public BotLotPFExact(LotGraph graphIn, LotNode curNodeIn, LotNode destNodeIn, Collection<LotEdge> edgesToAvoidIn)
			throws BotLotPFAlgException {
		super(graphIn, curNodeIn, destNodeIn, edgesToAvoidIn);
	}
	
	/**
	 * Constructor that initializes all the variables separately.
	 * 
	 * @param graphIn	The graph to use.
	 * @param curNodeIn	The node we are starting at.
	 * @param destNodeIn	The node we are going to.
	 * @throws BotLotPFAlgException  If curNode and/or destNode cannot be set.
	 */
	public BotLotPFExact(LotGraph graphIn, LotNode curNodeIn, LotNode destNodeIn) throws BotLotPFAlgException {
		super(graphIn, curNodeIn, destNodeIn);
	}
	
	/**
	 * Recursive method to find an exact shortest path.
	 * <p>
	 * TODO:: redo this
	 * 
	 * @param thisCurNode	The node this iteration is on.
	 * @param lastNode	The node whose iteration called this iteration.
	 * @param hitCounts A hashmap used to determine if we have been places before, and how often.
	 * @param edgesToAvoid	A list of edges to not go down.
	 * @return	A shortest path leading to the end node.
	 * @throws BotLotPFException	If there is no path to the end node.
	 */
	private LotPath getExactPath(LotNode thisCurNode, LotNode lastNode, HashMap<LotNode, LotPath> paths) throws BotLotPFAlgException{
		ArrayList<LotPath> pathList = new ArrayList<LotPath>();
		LotPath tempPath = new LotPath();
		
		for(LotEdge tempEdge : thisCurNode.getConnectedEdges(this.getEdgesToAvoid())){
			tempPath = new LotPath();
			try {
				tempPath.append(tempEdge);
				if(tempEdge.getEndNode() != this.getDestNode()){
					if(!paths.containsKey(tempEdge.getEndNode())){
						tempPath.append(getExactPath(tempEdge.getEndNode(), thisCurNode, paths));
					}else{
						continue;
					}
				}
			} catch (LotPathException e) {
				e.printStackTrace();
				System.out.println("FATAL ERR- This should not happen. Error: " + e.getMessage());
				System.exit(1);
			}
			if(!tempPath.hasLoops()){
				pathList.add(tempPath);
			}
		}
		
		tempPath = new LotPath();
		tempPath.infSizeFlag = true;
		for(LotPath curPath : pathList){
			if(tempPath.isLonger(curPath)){
				tempPath = curPath;
			}
		}
		paths.put(thisCurNode, tempPath);
		return tempPath;
	}//getExactPath(BotLot, LotNode, LotNode)
	
	@Override
	protected LotPath calculatePath() throws BotLotPFAlgException {
		ArrayList<LotNode> nodesHit = new ArrayList<LotNode>();
		HashMap<LotNode, LotPath> pathList = new HashMap<LotNode, LotPath>();
		nodesHit.add(this.getDestNode());
		pathList.put(this.getCurNode(), new LotPath());
		
		
		
		
		
		
		try{
			return getExactPath(this.getCurNode(), this.getDestNode(), pathList);
		}catch(BotLotPFAlgException e){
			if(e.getMessage().equals(eolString)){
				e.printStackTrace();
				System.out.println("FATAL ERROR- You should not ghet this. Error: " + e.getMessage());
				System.exit(1);
			}
			throw e;
		}
	}

}
