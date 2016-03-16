package botLot.pathFinding.Algorithms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import botLot.BotLot;
import botLot.lotGraph.LotEdge;
import botLot.lotGraph.LotGraph;
import botLot.lotGraph.LotGraphException;
import botLot.lotGraph.LotNode;
import botLot.lotGraph.LotPath;
import botLot.lotGraph.LotPathException;

/**
 * The implementation of Dijkstra's algorithm for this application. 
 * <p>
 * Implementation based on: http://www.vogella.com/tutorials/JavaAlgorithmsDijkstra/article.html
 * <p>
 * Started: 3/7/16
 * 
 * @author Greg Stewart
 * @version	1.0 3/12/16
 */
public class BotLotPFDijkstra extends BotLotPFAlgorithm implements BotLotPFAlgInterface {
	/*
	 * Variables required for Dijkstra's algorithm.
	 * TODO:: describe what they are/do.
	 */
	/**  */
	private Set<LotNode> settledNodes = null;
	/** */
	private Set<LotNode> unSettledNodes = null;
	/** */
	private Map<LotNode, LotNode> predecessors = null;
	/** */
	private Map<LotNode, Double> distance = null;
	
	
	/**
	 * Basic Constructor
	 */
	public BotLotPFDijkstra(){
		super();
	}
	
	/**
	 * Constructor using a BotLot object and a list of edges to avoid.
	 * 
	 * @param lotIn	The BotLot object to take values from.
	 * @param edgesToAvoidIn	Edges to avoid going down.
	 */
	public BotLotPFDijkstra(BotLot lotIn, Collection<LotEdge> edgesToAvoidIn) {
		super(lotIn, edgesToAvoidIn);
	}
	
	/**
	 * Constructor taking in all the things needed for the thing to work.
	 * 
	 * @param graphIn	The graph object we are dealing with.
	 * @param curNodeIn	The current node we are at.
	 * @param destNodeIn	The node we are trying to get to.
	 * @param edgesToAvoidIn	Edges we want to avoid going down.
	 * @throws BotLotPFAlgException	If something went wrong setting values.
	 */
	public BotLotPFDijkstra(LotGraph graphIn, LotNode curNodeIn, LotNode destNodeIn, Collection<LotEdge> edgesToAvoidIn) throws BotLotPFAlgException{
		super(graphIn, curNodeIn, destNodeIn, edgesToAvoidIn);
	}
	
	/**
	 * Constructor using a BotLot object.
	 * 
	 * @param lotIn	The BotLot object to use.
	 */
	public BotLotPFDijkstra(BotLot lotIn) {
		super(lotIn);
	}
	
	@Override
	protected LotPath calculatePath() throws BotLotPFAlgException {
		settledNodes = new HashSet<LotNode>();
		unSettledNodes = new HashSet<LotNode>();
		
		distance = new HashMap<LotNode, Double>();
		predecessors = new HashMap<LotNode, LotNode>();
	    distance.put(this.getCurNode(), 0.0);
	    unSettledNodes.add(this.getCurNode());
	    while (unSettledNodes.size() > 0) {
	    	LotNode node = getMinimum(unSettledNodes);
	    	settledNodes.add(node);
	    	unSettledNodes.remove(node);
	    	findMinimalDistances(node);
	    }
	    
	    return this.getPath(this.getDestNode());
	}//calculatePath()
	
	/**
	 * Function for Dijkstra's Algorithm.
	 * TODO:: describe this
	 * 
	 * @param node	The node to get minimal distances to?
	 */
	private void findMinimalDistances(LotNode node) {
		List<LotNode> adjacentNodes = getNeighbors(node);
		for (LotNode target : adjacentNodes) {
			if (getShortestDistance(target) > getShortestDistance(node)
					+ getDistance(node, target)) {
				distance.put(target, getShortestDistance(node)
					+ getDistance(node, target));
				predecessors.put(target, node);
				unSettledNodes.add(target);
			}
		}
	}//findMinimalDistances(LotNode)
	
	/**
	 * Function for Dijktra's algorithm.
	 * TODO:: describe this
	 * 
	 * @param node	The starting node.
	 * @param target	The node we are trying to get to.
	 * @return	The distance from the first node to the second node.
	 */
	private double getDistance(LotNode node, LotNode target) {
	    for (LotEdge edge : this.getGraph().getEdgeList(this.getEdgesToAvoid())) {
	    	
	        try {
				if (this.getGraph().getNode(edge) == node
				  && edge.getEndNode().equals(target)) {
					return edge.getMetric();
				}
			} catch (LotGraphException e) {
				e.printStackTrace();
				System.out.println("FATAL ERROR- Should not happen. Error: " + e.getMessage());
				System.exit(1);
			}
	    }
	    throw new RuntimeException("Should not happen");
	}//getDistance(LotNode, LotNode)
	
	/**
	 * Function for Dijkstra's algorithm.
	 * TODO:: describe this
	 * 
	 * @param node	The node to get the neighbors of.
	 * @return	A list of the neighbors of the given node.
	 */
	private List<LotNode> getNeighbors(LotNode node) {
	    List<LotNode> neighbors = new ArrayList<LotNode>();
	    for (LotEdge edge : this.getGraph().getEdgeList(this.getEdgesToAvoid())) {
	    	try {
	    		if (this.getGraph().getNode(edge) == node && !isSettled(edge.getEndNode())) {
	    			neighbors.add(edge.getEndNode());
	    		}
	    	} catch (LotGraphException e) {
	    		e.printStackTrace();
	    		System.out.println("FATAL ERROR- this should not happen. Error: " + e.getMessage());
	    		System.exit(1);
	    	}
	    }
	    return neighbors;
	}//getNeighbors(LotNode)
	
	/**
	 * Gets the minimum vertex from the set given.
	 * <p>
	 * Function for Dijkstra's algorithm.
	 * 
	 * @param vertexes	The set of vertexes.
	 * @return	The minimum vertex from the set given.
	 */
	private LotNode getMinimum(Set<LotNode> vertexes) {
		LotNode minimum = null;
		for (LotNode vertex : vertexes) {
			if (minimum == null) {
				minimum = vertex;
			} else {
				if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
					minimum = vertex;
				}
			}
		}
		return minimum;
	}//getMinimum(Set<LotNode>)
	
	/**
	 * Determines if the node given is setled, i.e., in the settledNodes
	 * <p>
	 * Function for Dijkstra's algorithm.
	 * 
	 * @param vertex	The node we are searching for.
	 * @return	If the node given is settled.
	 */
	private boolean isSettled(LotNode vertex) {
		return settledNodes.contains(vertex);
	}//isSettled(LotNode)
	
	/**
	 * Function for Dijkstra's algorithm.
	 * TODO:: describe this
	 * 
	 * @param destination	The node we are trying to get to in this case.
	 * @return	The shortest distance to the given node.
	 */
	private double getShortestDistance(LotNode destination) {
		Double d = distance.get(destination);
		if (d == null) {
			return Double.MAX_VALUE;
		} else {
			return d;
		}
	}//getShortestDistance(LotNode)
	
	/**
	 * This method returns the path from the source to the selected target and NULL if no path exists (should never do this, as there is already a path existence check)
	 * <p>
	 * Function for Dijkstra's algorithm.
	 * 
	 * @param target	The node we are trying to get to?
	 * @return	The shortest path to the target.
	 * @throws BotLotPFAlgException	If something went wrong.
	 */
	private LotPath getPath(LotNode target) throws BotLotPFAlgException {
		LinkedList<LotNode> path = new LinkedList<LotNode>();
		LotNode step = target;
		// check if a path exists
		if (predecessors.get(step) == null) {
			return null;
		}
		
		path.add(step);
		  
		while (predecessors.get(step) != null) {
			step = predecessors.get(step);
			path.add(step);
		}
		// Put it into the correct order
		Collections.reverse(path);
		
		try {
			return new LotPath(path, this.getEdgesToAvoid());
		} catch (LotPathException e) {
			throw new BotLotPFAlgException("Could not use the given node list to make valid path. Error: " + e.getMessage());
		}
	}//getPath(LotNode)
	  
}//class BotLotPFDijkstra
