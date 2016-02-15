package botLot.pathFinding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import botLot.BotLot;
import botLot.lotGraph.LotEdge;
import botLot.lotGraph.LotGraph;
import botLot.lotGraph.LotGraphException;
import botLot.lotGraph.LotNode;
import botLot.lotGraph.LotPath;
import botLot.lotGraph.LotPathException;
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
	 * @throws BotLotPFException	If curNode and/or destNode cannot be set.
	 */
	public BotLotPFExact(LotGraph graphIn, LotNode curNodeIn, LotNode destNodeIn, Collection<LotEdge> edgesToAvoidIn)
			throws BotLotPFException {
		super(graphIn, curNodeIn, destNodeIn, edgesToAvoidIn);
	}
	
	/**
	 * Constructor that initializes all the variables separately.
	 * 
	 * @param graphIn	The graph to use.
	 * @param curNodeIn	The node we are starting at.
	 * @param destNodeIn	The node we are going to.
	 * @throws BotLotPFException  If curNode and/or destNode cannot be set.
	 */
	public BotLotPFExact(LotGraph graphIn, LotNode curNodeIn, LotNode destNodeIn) throws BotLotPFException {
		super(graphIn, curNodeIn, destNodeIn);
	}
	
	/**
	 * Recursive method to find an exact shortest path.
	 * 
	 * @param thisCurNode	The node this iteration is on.
	 * @param lastNode	The node whose iteration called this iteration.
	 * @param hitCounts A hashmap used to determine if we have been places before, and how often.
	 * @param edgesToAvoid	A list of edges to not go down.
	 * @return	A shortest path leading to the end node.
	 * @throws BotLotPFException	If there is no path to the end node.
	 */
	private LotPath getExactPath(LotNode thisCurNode, LotNode lastNode, HashMap<LotNode,Integer> hitCounts) throws BotLotPFException{
		//System.out.println("Doing the thing!!");
		LotPath pathOut = new LotPath();
		//take care of special cases
		if(thisCurNode.hasEdgeTo(this.getDestNode())){//if we found the destination node
			try {
				pathOut.append(thisCurNode.getEdgeTo(this.getDestNode()));
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
			if(curEdge.getEndNode() == thisCurNode || curEdge.getEndNode() == lastNode || this.getEdgesToAvoid().contains(curEdge)){
				continue;
			}
			try {
				if(hitCounts.containsKey(curEdge.getEndNode())){
					//TODO:: verify this works for all cases
					if( hitCounts.get(curEdge.getEndNode()) >= Math.pow(this.getGraph().getNumEdgesToNode(curEdge.getEndNode()),2) ){
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
				tempPath.append(getExactPath(curEdge.getEndNode(), thisCurNode, hitCounts));
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
	
	@Override
	protected LotPath calculatePath() throws BotLotPFException {
		try{
			return getExactPath(this.getCurNode(), this.getDestNode(), new HashMap<LotNode, Integer>());
		}catch(BotLotPFException e){
			if(e.getMessage().equals(eolString)){
				e.printStackTrace();
				System.out.println("FATAL ERROR- You should not ghet this. Error: " + e.getMessage());
				System.exit(1);
			}
			throw e;
		}
	}

}
