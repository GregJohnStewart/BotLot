package botLot.pathFinding;

import java.util.ArrayList;
import java.util.Collection;

import botLot.BotLot;
import botLot.lotGraph.*;

/**
 * Interface to set up the methods needed for all path generation.
 * <p>
 * Started: 2/13/16
 * 
 * @author Greg Stewart
 * @version	1.0 2/13/16
 */
public abstract class BotLotPFAlgorithm {
	/** The graph we are using in the path finding. */
	private LotGraph mainGraph;
	/** The node we are starting at. */
	private LotNode curNode;
	/** The node we are going to. */
	private LotNode destNode;
	/** Edges that we will not even try to go down. */
	private ArrayList<LotEdge> edgesToAvoid;
	
	/**
	 * Constructor that sets everything all at once.
	 * 
	 * @param lotIn	The BotLot object to use.
	 */
	public BotLotPFAlgorithm(BotLot lotIn){
		this();
		this.setAll(lotIn);
	}
	
	/**
	 * Constructor that initializes all the variables separately.
	 * 
	 * @param graphIn	The graph to use.
	 * @param curNodeIn	The node we are starting at.
	 * @param destNodeIn	The node we are going to.
	 * @throws BotLotPFException  If curNode and/or destNode cannot be set.
	 */
	public BotLotPFAlgorithm(LotGraph graphIn, LotNode curNodeIn, LotNode destNodeIn) throws BotLotPFException{
		this();
		this.setAll(graphIn, curNodeIn, destNodeIn);
	}
	
	/**
	 * Basic constructor for path finding.
	 */
	public BotLotPFAlgorithm(){
		this.mainGraph = null;
		this.curNode = null;
		this.destNode = null;
		this.edgesToAvoid = new ArrayList<LotEdge>();
	}
	
	/**
	 * Put the actual path finding code here, already checked for object readiness.
	 * <p>
	 * DO NOT CALL THIS FROM ANY OTHER CLASS THAN THE SUB CLASS YOU IMPLEMENTED THIS IN
	 */
	protected abstract LotPath calculatePath();
	
	/**
	 * Executes this method of path finding. Checks for object readiness.
	 * 
	 * @return	A path from the cur node to the destination node.
	 * @throws BotLotPFException	If the object does not have the data it needs, or if something goes wrong.
	 */
	public LotPath findPath() throws BotLotPFException{
		if(this.ready()){
			return calculatePath();
		}else{
			throw new BotLotPFException("Object not ready for path generation.");
		}
	}
	
	/**
	 * Sets the data using the inputed BotLot, then runs the path finding code.
	 * 
	 * @param lotIn	The BotLot object to use to set data.
	 * @return	A path from the cur node to the destination node.
	 * @throws BotLotPFException	If the object does not have the data it needs, or if something goes wrong.
	 */
	public LotPath findPath(BotLot lotIn) throws BotLotPFException{
		this.setAll(lotIn);
		return this.findPath();
	}
	
	/**
	 * Sets the data, and runs the path finding code.
	 * 
	 * @param graphIn	The graph data to work off of.
	 * @param curNodeIn	The node we are currently at.
	 * @param destNodeIn	The node we are going to.
	 * @return	A path from the newly set current node to the also newly set destination node.
	 * @throws BotLotPFException	If something was not set, or something went wrong.
	 */
	public LotPath findPath(LotGraph graphIn, LotNode curNodeIn, LotNode destNodeIn) throws BotLotPFException{
		this.setAll(graphIn, curNodeIn, destNodeIn);
		return this.findPath();
	}
	
	/**
	 * Sets {@link #mainGraph}, {@link #curNode}, and {@link #destNode} using a BotLot object.
	 * 
	 * @param lotIn The BotLot object to set everything using..
	 * @return	This object.
	 */
	public BotLotPFAlgorithm setAll(BotLot lotIn){
		try {
			this.setAll(lotIn.mainGraph, lotIn.getCurNode(), lotIn.getDestNode());
		} catch (BotLotPFException e) {
			e.printStackTrace();
			System.out.println("FATAL ERROR- BotLotPFAlgorithm.setAll(BotLot lotIn)- This should not happen. Error: " + e.getMessage() );
		}
		return this;
	}
	
	/**
	 * Sets {@link #mainGraph}, {@link #curNode}, and {@link #destNode} using separate variables.
	 * 
	 * @param graphIn	The graph to set.
	 * @param curNodeIn	The curNode to be at.
	 * @param destNodeIn	The node we are trying to get to.
	 * @throws BotLotPFException If curNode and/or destNode cannot be set.
	 */
	public BotLotPFAlgorithm setAll(LotGraph graphIn, LotNode curNodeIn, LotNode destNodeIn) throws BotLotPFException{
		this.setGraph(graphIn);
		try {
			this.setCurNode(curNodeIn);
			this.setDestNode(destNodeIn);
		} catch (BotLotPFException e) {
			throw new BotLotPFException("Unable to set curNode and/or destNode; Either are not in the given graph.");
		}
		return this;
	}
	
	/**
	 * Sets the graph we are using.
	 * 
	 * @param graphIn	The graph to set this to.
	 * @return	This object.
	 */
	public BotLotPFAlgorithm setGraph(LotGraph graphIn){
		this.mainGraph = graphIn;
		return this;
	}
	
	/**
	 * Sets the graph we are using using a BotLot object.
	 * 
	 * @param lotIn	The BotLot object to get the graph from.
	 * @return	This object.
	 */
	public BotLotPFAlgorithm setGraph(BotLot lotIn){
		this.mainGraph = lotIn.mainGraph;
		return this;
	}
	
	/**
	 * Sets the current node. Checks if it is in the graph structure.
	 * 
	 * @param nodeIn	The node we are setting to the curNode
	 * @return	This object.
	 * @throws BotLotPFException	If the node given is not in the graph data.
	 */
	public BotLotPFAlgorithm setCurNode(LotNode nodeIn) throws BotLotPFException{
		if(this.getGraph() != null){
			if(this.getGraph().hasNode(nodeIn)){
				this.curNode = nodeIn;
				return this;
			}else{
				throw new BotLotPFException("Cannot set current node, node not within present data.");
			}
		}else{
			throw new BotLotPFException("Cannot set current node, no graph data present.");
		}
	}
	
	/**
	 * Sets the destination node. Checks if it is in the graph structure.
	 * 
	 * @param nodeIn	The node we are setting to the destination node.
	 * @return	This object.
	 * @throws BotLotPFException	If the node given is not in the graph data.
	 */
	public BotLotPFAlgorithm setDestNode(LotNode nodeIn) throws BotLotPFException{
		if(this.hasGraph()){
			if(this.getGraph().hasNode(nodeIn)){
				this.destNode = nodeIn;
				return this;
			}else{
				throw new BotLotPFException("Cannot set destination node, node not within present data.");
			}
		}else{
			throw new BotLotPFException("Cannot set destination node, no graph data present.");
		}
	}
	
	/**
	 * Gets the graph object held by this object.
	 * 
	 * @return The graph object held by this object.
	 */
	public LotGraph getGraph(){
		return this.mainGraph;
	}
	
	/**
	 * Determines if this object has a graph or not. 
	 * 
	 * @return	If this object has a graph or not. 
	 */
	public boolean hasGraph(){
		if(this.getGraph() != null){
			return true;
		}
		return false;
	}
	
	/**
	 * Gets the current node held by this object.
	 * 
	 * @return	The curNode held by this object.
	 */
	public LotNode getCurNode(){
		return this.curNode;
	}
	
	/**
	 * Determines if the object has a current node set.
	 * 
	 * @return	If the object has a current node set or not.
	 */
	public boolean hasCurNode(){
		if(this.getCurNode() != null){
			return true;
		}
		return false;
	}
	
	/**
	 * Gets the destination node held by this object.
	 * 
	 * @return	The destNode held by this object.
	 */
	public LotNode getDestNode(){
		return this.destNode;
	}
	
	/**
	 * Determines if the object has a destination node set.
	 * 
	 * @return	If the object has a destination node set or not.
	 */
	public boolean hasDestNode(){
		if(this.getDestNode() != null){
			return true;
		}
		return false;
	}
	
	/**
	 * Gets the list of edges to avoid going down.
	 * 
	 * @return	The list of edges to avoid going down.
	 */
	public ArrayList<LotEdge> getEdgesToAvoid(){
		return this.edgesToAvoid;
	}
	
	/**
	 * Sets the list of edges to avoid.
	 * 
	 * @param edgesToAvoidIn	The new list of edges to avoid.
	 * @return This object.
	 */
	public BotLotPFAlgorithm setEdgesToAvoid(Collection<LotEdge> edgesToAvoidIn){
		this.edgesToAvoid = new ArrayList<LotEdge>(edgesToAvoidIn);
		return this;
	}
	
	/**
	 * Adds the given list of edges to the list of edges to avoid going down. 
	 * 
	 * @param edgesToAvoidIn	The list to add.
	 * @return	This object.
	 */
	public BotLotPFAlgorithm addEdgesToAvoid(Collection<LotEdge> edgesToAvoidIn){
		this.edgesToAvoid.addAll(edgesToAvoidIn);
		return this;
	}
	
	/**
	 * Clears the list of edges to avoid..
	 * 
	 * @return This object.
	 */
	public BotLotPFAlgorithm clearEdgesToAvoid(){
		this.edgesToAvoid = new ArrayList<LotEdge>();
		return this;
	}
	
	/**
	 * Determines if the object is ready to find a path.
	 * 
	 * @return
	 */
	public boolean ready(){
		if(this.hasGraph()){
			if(this.hasCurNode() && this.hasDestNode()){
				return true;
			}
		}
		return false;
	}
	
}//interface BotLotPFAlgorithm
