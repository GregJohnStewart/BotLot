package botLot.lotGraph;

import java.util.Random;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Class LotGraph
 * <p>
 * The main class to set up the LotGraph object.
 * <p>
 * Includes everything needed to add, remove, and get/set nodes and edges.
 * <p>
 * Started: 10/7/15
 * <p>
 * Future plans:
 * 		Keep a list of paths for later use, use ID's to keep track of them, etc.
 * <p>
 * TODO:: review methods for cleanliness and best practice
 * 
 * TODO:: add methods to operate on node(s)/edge(s) with particular att/val?
 * 
 * @author Greg Stewart
 * @version 1.0 5/2/16
 */
public class LotGraph {
	/**
	 * crucial to operation
	 */
	/** The nodes we currently have. */
	private ArrayList<LotNode> nodes;

	/*
	 * stuff for id generation
	 */
	/** Random number generator for making new id's */
	private Random rand;
	/** The predicate of new edge id's. */
	private final static String edgeIdPred = "BOTLOTEDGE";
	/** The predicate of new node id's. */
	private final static String nodeIdPred = "BOTLOTNODE";
	/** The max value of new ids' salts. */
	private final static int idSaltRange = Integer.MAX_VALUE;

	// =========================================================================
	// Constructors
	// region Constructors
	// =========================================================================

	/**
	 * Essentially copies the info from the input graph.
	 * 
	 * @param graphIn
	 *            THe graph to get info from.
	 */
	public LotGraph(LotGraph graphIn) {
		this();
		try {
			this.setNodes(graphIn.getNodes());
		} catch (LotGraphException err) {
			System.out.println("FATAL ERR- LotGraph(LotGraph)- This should not happen. Error: " + err.getMessage());
			System.exit(1);
		}
		this.setRand(graphIn.getRand());
	}// LotGraph(LotGraph)

	/**
	 * Sets the nodes and random number generator for the graph.
	 * 
	 * @param nodesIn
	 *            The nodes to set the initial nodes to.
	 * @param randIn
	 *            The pre-instantiated random number generator.
	 * @throws LotGraphException
	 *             If the node list given is invalid.
	 */
	public LotGraph(Collection<LotNode> nodesIn, Random randIn) throws LotGraphException {
		this(nodesIn);
		this.setRand(randIn);
	}// LotGraph(ArrayList<LotNode>, Random)

	/**
	 * Sets the initial node list {@link #nodes}.
	 * 
	 * @param nodesIn
	 *            The nodes to set the initial nodes to.
	 * @throws LotGraphException
	 *             If the node list given is invalid.
	 */
	public LotGraph(Collection<LotNode> nodesIn) throws LotGraphException {
		this();
		this.setNodes(nodesIn);
	}// LotGraph(LotNode)
	
	/**
	 * Constructor to set the random number generator.
	 * 
	 * @param randIn	The random number generator.
	 */
	public LotGraph(Random randIn){
		this();
		this.setRand(randIn);
	}// LotGraph(LotNode)

	/**
	 * Basic constructor, initializes everything to empty lists, initializes
	 * {@link #rand} with empty parameters.
	 */
	public LotGraph() {
		this.nodes = new ArrayList<LotNode>();
		this.rand = new Random();
		// System.out.println("DEBUG- Initial Sizes: \n\tnodeList: " +
		// this.getNodeListSize() + "\n\t# edges: " + this.getNumEdges());
	}// LotGraph()

	// endregion

	// =========================================================================
	// Setters
	// region Setters
	// =========================================================================

	/**
	 * Sets the {@link #nodes} to the list given.
	 * 
	 * @param nodesIn
	 *            The node list in.
     * @return	This graph.
	 * @throws LotGraphException
	 *             If the node list was found to be invalid.
	 */
	public LotGraph setNodes(Collection<LotNode> nodesIn) throws LotGraphException {
		if (checkNodeEdges(nodesIn)) {
			this.nodes = (ArrayList<LotNode>)nodesIn;
		} else {
			throw new LotGraphException("Node list given is invalid.");
		}
		return this;
	}// setNodes(ArrayList<LotNode>

	/**
	 * Sets a node's edge based on the given variables.
	 * 
	 * @param edgeIn
	 *            The edge to set to.
	 * @param fromNode
	 *            The node we are adding this edge to.
     * @return	This graph.
	 * @throws LotGraphException
	 *             If either node does not exist.
	 */
	public LotGraph setEdge(LotEdge edgeIn, LotNode fromNode) throws LotGraphException {
		if (this.hasNode(fromNode)) {
			if (this.hasNode(edgeIn.getEndNode()) | edgeIn.getEndNode() == null) {
				if (!this.nodeIsFull(fromNode)) {
					try {
						this.getNode(fromNode).addEdge(edgeIn);
					} catch (LotNodeException e) {
						System.out.println("FATAL ERR- setEdge(LotEdge, LotNode). This should not happen. Error: "
								+ e.getMessage());
						System.exit(1);
					}
				} else {
					throw new LotGraphException("Node is already full- cannot add another edge.");
				}
			} else {
				throw new LotGraphException("Edge given has invalid end node.");
			}
		} else {
			throw new LotGraphException("fromNode is not inside the stored data.");
		}
		return this;
	}// setEdge(LotEdge, LotNode)

	/**
	 * Sets a node's edge based on the given variables.
	 * 
	 * @param edgeIn
	 *            The edge to give the node.
	 * @param fromNodeId
	 *            The first node's ID.
     * @return	This graph.
	 * @throws LotGraphException
	 *             If either node does not exist.
	 */
	public LotGraph setEdge(LotEdge edgeIn, String fromNodeId) throws LotGraphException {
		if (this.hasNode(fromNodeId)) {
			if (this.hasNode(edgeIn.getEndNode()) | edgeIn.getEndNode() == null) {
				if (!this.nodeIsFull(fromNodeId)) {
					try {
						this.getNode(fromNodeId).addEdge(edgeIn);
					} catch (LotNodeException e) {
						System.out.println("FATAL ERR- setEdge(LotEdge, LotNode). This should not happen. Error: "
								+ e.getMessage());
						System.exit(1);
					}
				} else {
					throw new LotGraphException("Node is already full- cannot add another edge.");
				}
			} else {
				throw new LotGraphException("Edge given has invalid end node.");
			}
		} else {
			throw new LotGraphException("fromNodeId does not point to any node in the set.");
		}
		return this;
	}// setEdge(LotEdge,String)

	/**
	 * Sets a node's edge based on the given variables.
	 * 
	 * @param edgeIn
	 *            The edge we are adding to a node.
	 * @param fromNodeIndex
	 *            The node we are dealing with.
     * @return	This graph.
	 * @throws LotGraphException
	 *             If either node does not exist.
	 */
	public LotGraph setEdge(LotEdge edgeIn, int fromNodeIndex) throws LotGraphException {
		if (this.hasNode(fromNodeIndex)) {
			if (this.hasNode(edgeIn.getEndNode()) | edgeIn.getEndNode() == null) {
				if (!this.nodeIsFull(fromNodeIndex)) {
					try {
						this.getNode(fromNodeIndex).addEdge(edgeIn);
					} catch (LotNodeException e) {
						System.out.println("FATAL ERR- setEdge(LotEdge, LotNode). This should not happen. Error: "
								+ e.getMessage());
						System.exit(1);
					}
				} else {
					throw new LotGraphException("Node is already full- cannot add another edge.");
				}
			} else {
				throw new LotGraphException("Edge given has invalid end node.");
			}
		} else {
			throw new LotGraphException("fromNodeIndex does not point to any node in the set.");
		}
		return this;
	}// setEdge(LotEdge edgeIn, int fromNodeIndex)

	/**
	 * A simple method to return a new edge with a unique ID, used to give the
	 * other createEdge() functions a standard new edge.
	 * 
	 * @return A new edge
	 */
	private LotEdge createEdge() {
		LotEdge newEdge = new LotEdge();
		newEdge.setId(this.getNewUniqueId('e'));
		return newEdge;
	}// createEdge()
	
	/**
	 * Creates an edge going out from a given node.
	 * 
	 * @param fromNode	The node the edges is going out from.
	 * @return	The edge created.
	 * @throws LotGraphException	If the edge could not be created for some reason.
	 */
	public LotEdge createEdge(LotNode fromNode) throws LotGraphException{
		LotEdge newEdge = this.createEdge();
		try {
			this.setEdge(newEdge, fromNode);
		} catch (LotGraphException err) {
			throw new LotGraphException("Unable to create edge: " + err.getMessage());
		}
		return newEdge;
	}
	
	/**
	 * Creates an edge going out from a given node, and returns the id.
	 * 
	 * @param fromNode	The node the edges is going out from.
	 * @return	The id of the edge created.
	 * @throws LotGraphException	If the edge could not be created for some reason.
	 */
	public String createEdgeGiveId(LotNode fromNode) throws LotGraphException{
		return this.createEdge(fromNode).getId();
	}
	
	/**
	 * Creates an edge going out from a given node, and returns the index of it.
	 * 
	 * @param fromNode	The node this edge goes out from.
	 * @return	The index of the new edge in the generated edge list.
	 * @throws LotGraphException	If the fromNode is not in the graph. 
	 */
	public int createEdgeGiveIndex(LotNode fromNode) throws LotGraphException{
		return this.getEdgeIndex(this.createEdge(fromNode));
	}
	
	/**
	 * Creates an edge going out from a given node.
	 * 
	 * @param fromNodeId	The id of the node the edges is going out from.
	 * @return	The edge created.
	 * @throws LotGraphException	If the edge could not be created for some reason.
	 */
	public LotEdge createEdge(String fromNodeId) throws LotGraphException{
		LotEdge newEdge = this.createEdge();
		try {
			this.setEdge(newEdge, fromNodeId);
		} catch (LotGraphException err) {
			throw new LotGraphException("Unable to create edge: " + err.getMessage());
		}
		return newEdge;
	}
	
	/**
	 * Creates an edge going out from a given node, and returns the id.
	 * 
	 * @param fromNodeId	The id of the node the edges is going out from.
	 * @return	The id of the edge created.
	 * @throws LotGraphException	If the edge could not be created for some reason.
	 */
	public String createEdgeGiveId(String fromNodeId) throws LotGraphException{
		return this.createEdge(fromNodeId).getId();
	}
	
	/**
	 * Creates an edge going out from a given node, and returns the index of it.
	 * 
	 * @param fromNodeId	The ID of the node this edge goes out from.
	 * @return	The index of the new edge in the generated edge list.
	 * @throws LotGraphException	If the fromNode is not in the graph. 
	 */
	public int createEdgeGiveIndex(String fromNodeId) throws LotGraphException{
		return this.getEdgeIndex(this.createEdge(fromNodeId));
	}
	
	/**
	 * Creates an edge going out from a given node.
	 * 
	 * @param fromNodeIndex	The index of the node in the structure that the new edge will start at.
	 * @return	The newly created edge.
	 * @throws LotGraphException	If the edge could not be created for some reason.
	 */
	public LotEdge createEdge(int fromNodeIndex) throws LotGraphException{
		LotEdge newEdge = this.createEdge();
		try {
			this.setEdge(newEdge, fromNodeIndex);
		} catch (LotGraphException err) {
			throw new LotGraphException("Unable to create edge: " + err.getMessage());
		}
		return newEdge;
	}
	
	/**
	 * Creates an edge going out from a given node, and returns the id.
	 * 
	 * @param fromNodeIndex	The index of the node in the structure that the new edge will start at.
	 * @return	The id of the newly created edge.
	 * @throws LotGraphException	If the edge could not be created for some reason.
	 */
	public String createEdgeGiveId(int fromNodeIndex) throws LotGraphException{
		return this.createEdge(fromNodeIndex).getId();
	}
	
	/**
	 * Creates an edge going out from a given node, and returns the index of it.
	 * 
	 * @param fromNodeIndex	The node this edge goes out from.
	 * @return	The index of the new edge in the generated edge list.
	 * @throws LotGraphException	If the fromNode is not in the graph. 
	 */
	public int createEdgeGiveIndex(int fromNodeIndex) throws LotGraphException{
		return this.getEdgeIndex(this.createEdge(fromNodeIndex));
	}
	
	/**
	 * Creates a new edge, adds it between two nodes, and
	 * returns the new edge.
	 * 
	 * @param fromNode
	 *            The first node.
	 * @param toNode
	 *            The second node.
	 * @return The new edge.
	 * @throws LotGraphException
	 *             If something cannot be added or done.
	 */
	public LotEdge createEdge(LotNode fromNode, LotNode toNode) throws LotGraphException {
		LotEdge newEdge = this.createEdge();
		newEdge.setEndNode(toNode);
		try {
			this.setEdge(newEdge, fromNode);
		} catch (LotGraphException err) {
			throw new LotGraphException("Unable to create edge: " + err.getMessage());
		}
		return newEdge;
	}// createEdge(LotNode, LotNode)

	/**
	 * Creates a new edge between two nodes, and
	 * returns the new edge's Id.
	 * <p>
	 * Essentially a wrapper for {@link #createEdge(LotNode, LotNode)}.
	 * 
	 * @param fromNode
	 *            The first node.
	 * @param toNode
	 *            The second node.
	 * @return The ID of the new edge.
	 * @throws LotGraphException
	 *             If something cannot be added or done.
	 */
	public String createEdgeGiveId(LotNode fromNode, LotNode toNode) throws LotGraphException {
		return this.createEdge(fromNode, toNode).getId();
	}// createEdgeGiveId(LotNode, LotNode)
	
	/**
	 * Creates an edge going out from a given node, and returns the index of it.
	 * 
	 * @param fromNode	The node this edge goes out from.
	 * @param toNode	The node this edge goes to.
	 * @return	The index of the new edge in the generated edge list.
	 * @throws LotGraphException	If the fromNode or toNode is not in the graph. 
	 */
	public int createEdgeGiveIndex(LotNode fromNode, LotNode toNode) throws LotGraphException{
		return this.getEdgeIndex(this.createEdge(fromNode, toNode));
	}
	
	/**
	 * Creates a new edge, adds it between two nodes, and
	 * returns the new edge.
	 * 
	 * @param fromNodeId
	 *            The Id of the first node.
	 * @param toNodeId
	 *            The Id of the second node.
	 * @return The new edge.
	 * @throws LotGraphException
	 *             If something cannot be added or done.
	 */
	public LotEdge createEdge(String fromNodeId, String toNodeId) throws LotGraphException {
		LotEdge newEdge = this.createEdge();
		newEdge.setEndNode(this.getNode(toNodeId));
		try {
			this.setEdge(newEdge, fromNodeId);
		} catch (LotGraphException err) {
			throw new LotGraphException("Unable to create edge: " + err.getMessage());
		}
		return newEdge;
	}// createEdge(String, String)

	/**
	 * Creates an edge, puts it between two nodes, and
	 * returns the new edge ID.
	 * <p>
	 * Essentially a wrapper for {@link #createEdge(String, String)}.
	 * 
	 * @param fromNodeId
	 *            The ID of the first node.
	 * @param toNodeId
	 *            The ID of the second node.
	 * @return The Id of the new node.
	 * @throws LotGraphException
	 *             If something cannot be added or done.
	 */
	public String createEdgeGiveId(String fromNodeId, String toNodeId) throws LotGraphException {
		return this.createEdge(fromNodeId, toNodeId).getId();
	}// createEdgeGiveId(String,String)
	
	/**
	 * Creates an edge going out from a given node, and returns the index of it.
	 * 
	 * @param fromNodeId	The Id of the node this edge goes out from.
	 * @param toNodeId	The Id of the node this edge goes to.
	 * @return	The index of the new edge in the generated edge list.
	 * @throws LotGraphException	If the fromNode or toNode is not in the graph. 
	 */
	public int createEdgeGiveIndex(String fromNodeId, String toNodeId) throws LotGraphException{
		return this.getEdgeIndex(this.createEdge(fromNodeId, toNodeId));
	}

	/**
	 * Creates an edge, puts it between two nodes, and
	 * returns the new edge.
	 * 
	 * @param fromNodeIndex
	 *            The index of the first node.
	 * @param toNodeIndex
	 *            The index of the second node.
	 * @return The edge that was created.
	 * @throws LotGraphException
	 *             If something cannot be added or done.
	 */
	public LotEdge createEdge(int fromNodeIndex, int toNodeIndex) throws LotGraphException {
		LotEdge newEdge = this.createEdge();
		newEdge.setEndNode(this.getNode(toNodeIndex));
		try {
			this.setEdge(newEdge, fromNodeIndex);
		} catch (LotGraphException err) {
			throw new LotGraphException("Unable to create edge: " + err.getMessage());
		}
		return newEdge;
	}// createEdge(int,int)

	/**
	 * Creates an edge, puts it between two nodes, and
	 * returns the ID.
	 * <p>
	 * Essentially a wrapper for {@link #createEdge(int, int)}.
	 * 
	 * @param fromNodeIndex
	 *            The index of the first node.
	 * @param toNodeIndex
	 *            The index of the second node.
	 * @return The ID of the new edge.
	 * @throws LotGraphException
	 *             If something cannot be added or done.
	 */
	public String createEdgeGiveId(int fromNodeIndex, int toNodeIndex) throws LotGraphException {
		return this.createEdge(fromNodeIndex, toNodeIndex).getId();
	}// createEdgeGiveId(int,int)
	
	/**
	 * Creates an edge going out from a given node, and returns the index of it.
	 * 
	 * @param fromNodeIndex	The index of the node this edge goes out from.
	 * @param toNodeIndex	The index of the node this edge goes to.
	 * @return	The index of the new edge in the generated edge list.
	 * @throws LotGraphException	If the fromNode or toNode is not in the graph. 
	 */
	public int createEdgeGiveIndex(int fromNodeIndex, int toNodeIndex) throws LotGraphException{
		return this.getEdgeIndex(this.createEdge(fromNodeIndex, toNodeIndex));
	}

	/**
	 * Removes the edge given.
	 * 
	 * @param edgeIn
	 *            The edge to remove.
     * @return	This graph.
	 * @throws LotGraphException
	 *             If the edge is not in the data set.
	 */
	public LotGraph removeEdge(LotEdge edgeIn) throws LotGraphException {
		if (this.hasEdge(edgeIn)) {
			// find which node has the edge
			outerLoop: for (int i = 0; i < this.getNumNodes(); i++) {
				if (this.getNode(i).hasEdge(edgeIn)) {
					try {
						this.getNode(i).remEdge(edgeIn);
					} catch (LotNodeException e) {
						System.out.println(
								"FATAL ERR- removeEdge(LotEdge)- This should not happen. Cannot remove edge. Error: "
										+ e.getMessage());
						System.exit(1);
					}
					break outerLoop;
				}
			}
		} else {
			throw new LotGraphException("Edge not found in stored data.");
		}
		return this;
	}// removeEdge(LotEdge)

	/**
	 * Removes the edge given.
	 * 
	 * @param edgeIdIn
	 *            The id of the edge to remove.
     * @return	This graph.
	 * @throws LotGraphException
	 *             If the edge is not in the data set.
	 */
	public LotGraph removeEdge(String edgeIdIn) throws LotGraphException {
		if (this.hasEdge(edgeIdIn)) {
			outerLoop: for (int i = 0; i < this.getNumNodes(); i++) {
				if (this.getNode(i).hasEdge(edgeIdIn)) {
					try {
						this.getNode(i).remEdge(edgeIdIn);
					} catch (LotNodeException e) {
						System.out.println(
								"FATAL ERR- removeEdge(LotEdge)- This should not happen. Cannot remove edge. Error: "
										+ e.getMessage());
						System.exit(1);
					}
					break outerLoop;
				}
			}
		} else {
			throw new LotGraphException("Edge not found in stored data.");
		}
		return this;
	}// removeEdge(String)

	/**
	 * Removes the edge given.
	 * 
	 * TODO:: test this extensively
	 * 
	 * @param edgeIndexIn
	 *            The index of the edge in the generated index list to remove.
     * @return	This graph.
	 * @throws LotGraphException
	 *             If the edge is not in the data set.
	 */
	public LotGraph removeEdge(int edgeIndexIn) throws LotGraphException {
		if (this.hasEdge(edgeIndexIn)) {
			// get node
			LotNode tempNode = null;
			int count = 0;
			outerLoop: for (int i = 0; i < this.getNumNodes(); i++) {
				count += this.getNode(i).getNumEdges() - 1;
				if (count >= edgeIndexIn) {
					tempNode = this.getNode(i);
					break outerLoop;
				}
			}
			if (tempNode == null) {
				System.out.println("FATAL ERR- removeEdge(int)- This should not happen. No node found.");
				System.exit(1);
			}
			try {
				this.getNode(tempNode).remEdge(count - edgeIndexIn);
			} catch (LotNodeException e) {
				System.out.println("FATAL ERR- removeEdge(int)- This should not happen. Error: " + e.getMessage());
				System.exit(1);
			}
		} else {
			throw new LotGraphException("Edge not found in stored data.");
		}
		return this;
	}// removeEdge(int)

	/**
	 * Removes an edge between two nodes.
	 * 
	 * @param fromNode
	 *            The first node.
	 * @param toNode
	 *            The second node.
     * @return	This graph.
	 * @throws LotGraphException
	 *             If either node is not found within {@link #nodes}.
	 */
	public LotGraph removeEdgeFromTo(LotNode fromNode, LotNode toNode) throws LotGraphException {
		if (this.hasNode(fromNode) && this.hasNode(toNode)) {
			if (this.getNode(fromNode).hasEdgeTo(toNode)) {
				try {
					this.getNode(fromNode).remEdgeTo(toNode);
				} catch (LotNodeException e) {
					System.out.println("FATAL ERROR- remEdgeFromTo(LotNode, LotNode)- This should not happen. Error: "
							+ e.getMessage());
					System.exit(1);
				}
			}
		} else {
			if (!this.hasNode(fromNode) && !this.hasNode(toNode)) {
				throw new LotGraphException("Neither fromNode nor toNode is within the stored data.");
			} else if (!this.hasNode(fromNode)) {
				throw new LotGraphException("fromNode is not within the stored data.");
			} else if (!this.hasNode(toNode)) {
				throw new LotGraphException("toNode is not within the stored data.");
			}
		}
		return this;
	}// removeEdge(LotNode, LotNode)

	/**
	 * Removes an edge between two nodes.
	 * 
	 * @param fromNodeId
	 *            The id of node one.
	 * @param toNodeId
	 *            The id of node two.
     * @return	This graph.
	 * @throws LotGraphException
	 *             If either node cannot be found in {@link #nodes}.
	 */
	public LotGraph removeEdgeFromTo(String fromNodeId, String toNodeId) throws LotGraphException {
		if (this.hasNode(fromNodeId) && this.hasNode(toNodeId)) {
			if (this.getNode(fromNodeId).hasEdgeTo(toNodeId)) {
				try {
					this.getNode(fromNodeId).remEdgeTo(toNodeId);
				} catch (LotNodeException e) {
					System.out.println("FATAL ERROR- remEdgeFromTo(String, String)- This should not happen. Error: "
							+ e.getMessage());
					System.exit(1);
				}
			}
		} else {
			if (!this.hasNode(fromNodeId) && !this.hasNode(toNodeId)) {
				throw new LotGraphException("Neither fromNodeId nor toNodeId is within the stored data.");
			} else if (!this.hasNode(fromNodeId)) {
				throw new LotGraphException("fromNodeId is not within the stored data.");
			} else if (!this.hasNode(toNodeId)) {
				throw new LotGraphException("toNodeId is not within the stored data.");
			}
		}
		return this;
	}// removeEdge(String, String)

	/**
	 * Removes an edge based on two node indexes.
	 * 
	 * @param fromNodeIndex
	 *            The index of the first index.
	 * @param toNodeIndex
	 *            The index of the second node.
     * @return	This graph.
	 * @throws LotGraphException
	 *             If either node cannot be found in {@link #nodes}.
	 */
	public LotGraph removeEdgeFromTo(int fromNodeIndex, int toNodeIndex) throws LotGraphException {
		if (this.hasNode(fromNodeIndex) && this.hasNode(toNodeIndex)) {
			if (this.getNode(fromNodeIndex).hasEdgeTo(this.getNode(toNodeIndex))) {
				try {
					this.getNode(fromNodeIndex).remEdgeTo(this.getNode(toNodeIndex));
				} catch (LotNodeException e) {
					System.out.println("FATAL ERROR- remEdgeFromTo(LotNode, LotNode)- This should not happen. Error: "
							+ e.getMessage());
					System.exit(1);
				}
			}
		} else {
			if (!this.hasNode(fromNodeIndex) && !this.hasNode(toNodeIndex)) {
				throw new LotGraphException("Neither fromNodeIndex nor toNodeIndex is within the stored data.");
			} else if (!this.hasNode(fromNodeIndex)) {
				throw new LotGraphException("fromNodeIndex is not within the stored data.");
			} else if (!this.hasNode(toNodeIndex)) {
				throw new LotGraphException("toNodeIndex is not within the stored data.");
			}
		}
		return this;
	}// removeNode(int,int)

	/**
	 * Adds a new node to {@link #nodes}.
	 * 
	 * @param newNode
	 *            The node to add.
     * @return	This graph.
	 * @throws LotGraphException
	 *             If the new node has the same ID of a node already present in
	 *             {@link #nodes}.
	 */
	public LotGraph addNode(LotNode newNode) throws LotGraphException {
		if (!checkNodeEdges(newNode)) {
			throw new LotGraphException("Node entered has invalid edges.");
		}
		if (this.idIsUnique('n', newNode.getId())) {
			this.getNodes().add(newNode);
		} else {
			throw new LotGraphException("Node entered has duplicate ID (" + newNode.getId() + ").");
		}
		return this;
	}// addNode(LotNode newNode)

	/**
	 * Creates a new node, adds it to {@link #nodes}.
	 * 
	 * @return The new node.
	 */
	public LotNode createNode() {
		// System.out.println("Creating node.");
		LotNode newNode = new LotNode();
		newNode.setId(this.getNewUniqueId('n'));
		// System.out.println("Set up new node");
		try {
			this.addNode(newNode);
		} catch (LotGraphException err) {
			System.out
					.println("FATAL-ERROR- createNode(). You should not get this error. Message: " + err.getMessage());
			System.exit(1);
		}
		// System.out.println("Created new node. returning it...");
		return newNode;
	}// createNode()

	/**
	 * Creates a new node, adds it to {@link #nodes}.
	 * <p>
	 * Wrapper for {@link #createNode()}
	 * 
	 * @return The ID of the new node.
	 */
	public String createNodeGiveId() {
		// System.out.println("Creating node and giving ID");
		return this.createNode().getId();
	}// createNodeGiveString()
	
	/**
	 * Creates a new node, adds it to {@link #nodes}.
	 * <p>
	 * Wrapper for {@link #createNode()}
	 * 
	 * @return	The index of this new node in the node list.
	 */
	public int createNodeGiveIndex(){
		return this.getNodeIndex(this.createNode());
	}

	/**
	 * Removes the given node from {@link #nodes}. Goes through all edges, nulling the head nodes where they are this node.
	 * 
	 * @param nodeToRemove
	 *            The node to remove from the list.
     * @return	This graph.
	 * @throws LotGraphException
	 *             If the node cannot be found.
	 */
	public LotGraph removeNode(LotNode nodeToRemove) throws LotGraphException {
		if (this.hasNode(nodeToRemove)) {
			this.getNodes().remove(nodeToRemove);
			for(int i = 0; i < this.getEdgeList().size(); i++){
				if(this.getEdge(i).getEndNode() == nodeToRemove){
					this.getEdge(i).setEndNode(null);
				}
			}
		} else {
			throw new LotGraphException("The node given is not within sotred data.");
		}
		return this;
	}// removeNode(LotNode)

	/**
	 * Removes the node with the given Id from {@link #nodes}. Goes through all edges, nulling the head nodes where they are this node.
	 * 
	 * @param nodeToRemoveId
	 *            The id of the node to remove.
     * @return	This graph.
	 * @throws LotGraphException
	 *             If the node cannot be found.
	 */
	public LotGraph removeNode(String nodeToRemoveId) throws LotGraphException {
		if (this.hasNode(nodeToRemoveId)) {
			LotNode tempNode = this.getNode(nodeToRemoveId);
			this.removeNode(tempNode);
		} else {
			throw new LotGraphException("The node given is not within stored data.");
		}
		return this;
	}// removeNode(String)

	/**
	 * Removes the node at the specified index in {@link #nodes} from
	 * {@link #nodes}.  Goes through all edges, nulling the head nodes where they are this node.
	 * 
	 * @param nodeToRemoveIndex
	 *            The index of the node to remove.
     * @return	This graph.
	 * @throws LotGraphException
	 *             If the node index is not within the node list.
	 */
	public LotGraph removeNode(int nodeToRemoveIndex) throws LotGraphException {
		if (this.hasNode(nodeToRemoveIndex)) {
			LotNode tempNode = this.getNode(nodeToRemoveIndex);
			this.removeNode(tempNode);
		} else {
			throw new LotGraphException("The node given is not within sotred data.");
		}
		return this;
	}// removeNode(int)

	/**
	 * Sets the random number generator.
	 * 
	 * @param randIn
	 *            The new random number generator.
     * @return	This graph.
	 */
	public LotGraph setRand(Random randIn) {
		this.rand = randIn;
		return this;
	}// setRand(Random)

	// =========================================================================
	// Getters
	// region Getters
	// =========================================================================

	/**
	 * Gets {@link #nodes}.
	 * 
	 * @return The node list.
	 */
	public ArrayList<LotNode> getNodes() {
		return this.nodes;
	}// getNodes()

	/**
	 * Gets the number of nodes we have currently in {@link #nodes}.
	 * 
	 * @return Gets the number of nodes we have currently.
	 */
	public int getNumNodes() {
		return this.getNodes().size();
	}// getNumNodes()

	/**
	 * Gets the specified node in {@link #nodes}.
	 * <p>
	 * Essentially tests if the node is in the data set, and returns the node
	 * input if found.
	 * 
	 * @param nodeIn
	 *            The node we are getting.
	 * @return The node in, if found. Null if not found.
	 */
	public LotNode getNode(LotNode nodeIn) {
		if (this.getNodes().contains(nodeIn)) {
			return this.getNodes().get(this.getNodeIndex(nodeIn));
		}
		return null;
	}// getNode(LotNode)

	/**
	 * Gets the specified node in {@link #nodes}.
	 * 
	 * @param nodeIdIn
	 *            The ID of the node we are getting.
	 * @return The node found. Null if nothing found.
	 */
	public LotNode getNode(String nodeIdIn) {
		for (int i = 0; i < this.getNumNodes(); i++) {
			if (this.getNodes().get(i).getId().equals(nodeIdIn)) {
				return this.getNodes().get(i);
			}
		}
		return null;
	}// getNode(String)

	/**
	 * Gets the node at the specified index in {@link #nodes}.
	 * 
	 * @param nodeIndexIn
	 *            The index of the node we are trying to get.
	 * @return The node found.
	 * @throws LotGraphException
	 *             If the index is out of bounds.
	 */
	public LotNode getNode(int nodeIndexIn) throws LotGraphException {
		if (this.getNumNodes() > nodeIndexIn && nodeIndexIn > -1) {
			return this.getNodes().get(nodeIndexIn);
		} else {
			throw new LotGraphException("The given index is out of bounds of the stored data. Size of data=" + this.getNumNodes() + " Given index=" + nodeIndexIn);
		}
	}// getNode(int)
	
	/**
	 * Gets the node that is at the beginning of the given edge.
	 * 
	 * @param edgeIn The edge we are dealing with.
	 * @return The node we find.
	 * @throws LotGraphException	If the node given is not in the data set.
	 */
	public LotNode getNode(LotEdge edgeIn) throws LotGraphException{
		if(this.hasEdge(edgeIn)){
			for(LotNode curNode : this.getNodes()){
				if(curNode.hasEdge(edgeIn)){
					return curNode;
				}
			}
		}else{
			throw new LotGraphException("The edge given is not in the dataset.");
		}
		return null;//shouldn't ever do this
	}//getNode(LotEdge)

	/**
	 * Gets the node with a particular key/value pair.
	 * 
	 * @param key	The key to test for.
	 * @param value	The value to test for. Null if it doesn't matter.
	 * @return	The node being searched for. Null if not found.
	 */
	public LotNode getNode(String key, String value){
		for(LotNode curNode : this.getNodes()){
			if(curNode.hasAtt(key, value)){
				return curNode;
			}
		}
		return null;//shouldn't ever do this
	}//getNode(String, String)

	/**
	 * Determines if the node given is present in {@link #nodes}.
	 * 
	 * @param nodeIn
	 *            The node we are dealing with.
	 * @return If the node is present or not.
	 */
	public boolean hasNode(LotNode nodeIn) {
		if (this.getNode(nodeIn) != null) {
			return true;
		}
		return false;
	}// hasNode(LotNode)

	/**
	 * Determines if there is a node present with the given ID in {@link #nodes}
	 * .
	 * 
	 * @param nodeIdIn
	 *            The id of the node to deal with.
	 * @return If the node is present or not.
	 */
	public boolean hasNode(String nodeIdIn) {
		if (this.getNode(nodeIdIn) != null) {
			return true;
		}
		return false;
	}// hasNode(String)

	/**
	 * Determines if there is a node present at the given index in
	 * {@link #nodes}.
	 * 
	 * @param nodeIndexIn
	 *            The index of the node.
	 * @return If the node is present or not.
	 */
	public boolean hasNode(int nodeIndexIn) {
		try {
			this.getNode(nodeIndexIn);
		} catch (LotGraphException err) {
			return false;
		}
		return true;
	}// hasNode(int)
	
	/**
	 * Determines if there is a node that is the start of a given edge.
	 * 
	 * @param edgeIn The edge we are testing on.
	 * @return If there is a node that is the start of a given edge.
	 */
	public boolean hasNode(LotEdge edgeIn){
		try{
			this.getNode(edgeIn);
		}catch (LotGraphException err) {
			return false;
		}
		return true;
	}//hasNode(LotEdge)
	
	/**
	 * Determines if there is a node with a particular key/value pair.
	 * 
	 * @param key	The key to test for.
	 * @param value	The value to test for. Null if it doesn't matter.
	 * @return	If the node exists with this key/val pair.
	 */
	public boolean hasNode(String key, String value){
		if(this.getNode(key, value) == null){
			return false;
		}
		return true;
	}//hasNode(String, String)
	
	/**
	 * Gets the index of the specified node in the list in {@link #nodes}.
	 * 
	 * @param nodeIn
	 *            The node to find the index of.
	 * @return The index of the node, if found. -1 if not found.
	 */
	public int getNodeIndex(LotNode nodeIn) {
		return this.getNodes().indexOf(nodeIn);
	}// getNodeIndex(LotNode)

	/**
	 * Gets the index of the specified node in {@link #nodes}.
	 * 
	 * @param nodeIdIn
	 *            The Id of the node to find in {@link #nodes}.
	 * @return The index of the node, if found. -1 if not found.
	 */
	public int getNodeIndex(String nodeIdIn) {
		if (this.hasNode(nodeIdIn)) {
			for(LotNode curNode : this.getNodes()){
				if(curNode.getId().equals(nodeIdIn)){
					return this.getNodes().indexOf(curNode);
				}
			}
		}
		return -1;
	}// getNodeIndex(String)

	/**
	 * Gets the node at the other side of an edge.
	 * 
	 * @param nodeFromIn
	 *            The node we are starting at.
	 * @param edgeIn
	 *            The edge we are going down.
	 * @return The node at the other end of the node.
	 * @throws LotGraphException	If something given was not in the data set.
	 */
	public LotNode getOtherNode(LotNode nodeFromIn, LotEdge edgeIn) throws LotGraphException {
		if (this.hasNode(nodeFromIn) && nodeFromIn.hasEdge(edgeIn)) {
			return this.getNode(nodeFromIn).getEdge(edgeIn).getEndNode();
		} else {
			if (!this.hasNode(nodeFromIn) && !this.hasEdge(edgeIn)) {
				throw new LotGraphException(
						"getOtherNode(LotNode, LotEdge)- Unable to get other node. Neither node nor edge present in data.");
			} else if (!this.hasNode(nodeFromIn)) {
				throw new LotGraphException(
						"getOtherNode(LotNode, LotEdge)- Unable to get other node. The node in was not in data.");
			} else {
				throw new LotGraphException(
						"getOtherNode(LotNode, LotEdge)- Unable to get other node. The edge in did not start form the given node.");
			}
		}
	}// getOtherNode(LotNode, LotEdge)
	
	/**
	 * Gets the node at the other side of an edge.
	 * 
	 * @param nodeFromId	The Id of the node.
	 * @param edgeInId	The Id of the edge.
	 * @return	If the node has a specified edge.
	 * @throws LotGraphException	If one or both of the inputs were invalid.
	 */
	public LotNode getOtherNode(String nodeFromId, String edgeInId) throws LotGraphException {
		if (this.hasNode(nodeFromId) && this.getNode(nodeFromId).hasEdge(edgeInId)) {
			return this.getNode(nodeFromId).getEdge(edgeInId).getEndNode();
		} else {
			if (!this.hasNode(nodeFromId) && !this.hasEdge(edgeInId)) {
				throw new LotGraphException(
						"getOtherNode(LotNode, LotEdge)- Unable to get other node. Neither node nor edge present in data.");
			} else if (!this.hasNode(nodeFromId)) {
				throw new LotGraphException(
						"getOtherNode(LotNode, LotEdge)- Unable to get other node. The node in was not in data.");
			} else {
				throw new LotGraphException(
						"getOtherNode(LotNode, LotEdge)- Unable to get other node. The edge in did not start form the given node.");
			}
		}
	}// getOtherNode(String, String)

	/**
	 * Gets the node at the other side of an edge.
	 * 
	 * @param nodeFromIndex	The Index of the node in {@link #nodes}.
	 * @param edgeIndex	The index of the edge in the specified node.
	 * @return	If the node has a specified edge.
	 * @throws LotGraphException	If one or both of the inputs were invalid.
	 */
	public LotNode getOtherNode(int nodeFromIndex, int edgeIndex) throws LotGraphException {
		if (this.hasNode(nodeFromIndex) && this.getNode(nodeFromIndex).hasEdge(edgeIndex)) {
			return this.getNode(nodeFromIndex).getEdge(edgeIndex).getEndNode();
		} else {
			if (!this.hasNode(nodeFromIndex) && !this.hasEdge(edgeIndex)) {
				throw new LotGraphException(
						"getOtherNode(LotNode, LotEdge)- Unable to get other node. Neither node nor edge present in data.");
			} else if (!this.hasNode(nodeFromIndex)) {
				throw new LotGraphException(
						"getOtherNode(LotNode, LotEdge)- Unable to get other node. The node in was not in data.");
			} else {
				throw new LotGraphException(
						"getOtherNode(LotNode, LotEdge)- Unable to get other node. The edge in did not start form the given node.");
			}
		}
	}// getOtherNode(int, int)

	/**
	 * Sees if there is a node at the other side of an edge.
	 * 
	 * @param nodeFromIn
	 *            The node we are starting at.
	 * @param edgeIn
	 *            The edge we are going down.
	 * @return If there is a node there or not.
	 */
	public boolean hasOtherNode(LotNode nodeFromIn, LotEdge edgeIn) {
		try {
			if (this.getOtherNode(nodeFromIn, edgeIn) != null) {
				return true;
			}
		} catch (LotGraphException err) {

		}
		return false;
	}// hasOtherNode(LotNode, LotEdge)

	/**
	 * Gets an arrayList of initialized edges in {@link #nodes}.
	 * <p>
	 * Generates this with every call.
	 * 
	 * @return The ArrayList of initialized edges.
	 */
	public ArrayList<LotEdge> getEdgeList() {
		ArrayList<LotEdge> edgeList = new ArrayList<LotEdge>();
		for (LotNode curNode : this.getNodes()) {
			edgeList.addAll(curNode.getEdges());
		}
		return edgeList;
	}// getEdgeList()
	
	/**
	 * Gets an arrayList of initialized edges in {@link #nodes}, excluding the ones in the given list.
	 * 
	 * @param edgesToAvoid	Edges to not include in the returned set.
	 * @return	An arrayList of initialized edges in {@link #nodes}, excluding the ones in the given list.
	 */
	public ArrayList<LotEdge> getEdgeList(Collection<LotEdge> edgesToAvoid){
		ArrayList<LotEdge> listOut = this.getEdgeList();
		listOut.removeAll(edgesToAvoid);
		return listOut;
	}//getEdgeList(Collection<LotEdge>)

	/**
	 * Gets the number of edges currently set.
	 * 
	 * @return The number of edges currently set.
	 */
	public int getNumEdges() {
		return this.getEdgeList().size();
	}// getNumEdges()
	
	/**
	 * Gets the specified edge.
	 * 
	 * @param edgeIn
	 *            The edge in question.
	 * @return The edge, null if not found.
	 */
	public LotEdge getEdge(LotEdge edgeIn) {
		ArrayList<LotEdge> tempEdgeList = this.getEdgeList();
		for (int i = 0; i < tempEdgeList.size(); i++) {
			if (tempEdgeList.get(i).equals(edgeIn)) {
				return tempEdgeList.get(i);
			}
		}
		return null;
	}// getEdge(LotEdge)

	/**
	 * Gets the edge with the specified ID.
	 * 
	 * @param edgeIdIn
	 *            The ID of the node to find.
	 * @return The edge, null if not found.
	 */
	public LotEdge getEdge(String edgeIdIn) {
		ArrayList<LotEdge> tempEdgeList = this.getEdgeList();
		for (int i = 0; i < tempEdgeList.size(); i++) {
			if (tempEdgeList.get(i).getId().equals(edgeIdIn)) {
				return tempEdgeList.get(i);
			}
		}
		return null;
	}// getEdge(String)

	/**
	 * Gets the edge at the specified index of the generated Edge list.
	 * <p>
	 * Only consistent when not adding or removing edges between calls.
	 * 
	 * @param edgeIndex
	 *            The index of the edge to get.
	 * @return The edge at the index.
	 * @throws LotGraphException
	 *             If the index is out of bounds.
	 */
	public LotEdge getEdge(int edgeIndex) throws LotGraphException {
		ArrayList<LotEdge> tempEdges = this.getEdgeList();
		if (tempEdges.size() > edgeIndex) {
			return tempEdges.get(edgeIndex);
		} else {
			throw new LotGraphException("edgeIndex(" + edgeIndex + ") is out of bounds (max: " + tempEdges.size() + "");
		}
	}// getEdge(int)
	
	/**
	 * Gets the Edge with a particular key/value pair.
	 * 
	 * @param key	The key to test for.
	 * @param value	The value to test for. Null if it doesn't matter.
	 * @return	The Edge being searched for. Null if not found.
	 */
	public LotEdge getEdge(String key, String value){
		for(LotEdge curEdge : this.getEdgeList()){
			if(curEdge.hasAtt(key, value)){
				return curEdge;
			}
		}
		return null;
	}//getEdge(String, String)

	/**
	 * Gets the index of a specified edge in the generated index list.
	 * <p>
	 * Only consistent when not adding or removing edges between calls.
	 * 
	 * @param edgeIn
	 *            The edge to find.
	 * @return The index of the edge, if found. -1 if not found.
	 */
	public int getEdgeIndex(LotEdge edgeIn) {
		ArrayList<LotEdge> tempEdgeList = this.getEdgeList();
		for (int i = 0; i < tempEdgeList.size(); i++) {
			if (tempEdgeList.get(i) == edgeIn) {
				return i;
			}
		}
		return -1;
	}// getEdgeIndex(LotEdge)

	/**
	 * Gets the index of a specified edge in the generated index list.
	 * <p>
	 * Only consistent when not adding or removing edges between calls.
	 * 
	 * @param edgeIdIn
	 *            The Id of the edge to find.
	 * @return The index of the edge, if found. -1 if not found.
	 */
	public int getEdgeIndex(String edgeIdIn) {
		ArrayList<LotEdge> tempEdgeList = this.getEdgeList();
		for (int i = 0; i < tempEdgeList.size(); i++) {
			if (tempEdgeList.get(i).getId().equals(edgeIdIn)) {
				return i;
			}
		}
		return -1;
	}// getEdgeIndex(String)

	/**
	 * Tests if the edge given is present in the graph.
	 * 
	 * @param edgeIn
	 *            The edge to look for
	 * @return If the edge was found or not
	 */
	public boolean hasEdge(LotEdge edgeIn) {
		if (this.getEdge(edgeIn) != null) {
			return true;
		}
		return false;
	}// hasEdge(LotEdge)

	/**
	 * Tests if there is an edge with the given edge ID.
	 * 
	 * @param edgeIdIn
	 *            The id of the edge to find.
	 * @return If the edge was found or not.
	 */
	public boolean hasEdge(String edgeIdIn) {
		if (this.getEdge(edgeIdIn) != null) {
			return true;
		}
		return false;
	}// hasEdge(String)

	/**
	 * Tests if there is an edge at the specified index of the generated edge
	 * list.
	 * <p>
	 * Only consistent if not adding or removing edges between calls.
	 * 
	 * @param edgeIndex
	 *            The index of the edge in the generated list.
	 * @return If there is a node there or not
	 * @throws LotGraphException
	 *             If either node cannot be found.
	 */
	public boolean hasEdge(int edgeIndex) throws LotGraphException {
		try {
			if (this.getEdge(edgeIndex) != null) {
				return true;
			}
		} catch (LotGraphException e) {
			throw e;
		}
		return false;
	}// hasEdge(int,int)
	

	/**
	 * Determines if there is a node with a particular key/value pair.
	 * 
	 * @param key	The key to test for.
	 * @param value	The value to test for. Null if it doesn't matter.
	 * @return	If the node exists with this key/val pair.
	 */
	public boolean hasEdge(String key, String value){
		if(this.getEdge(key, value) == null){
			return false;
		}
		return true;
	}//hasEdge(String, String)

	/**
	 * Gets the edge that goes from one node to another.
	 * 
	 * @param fromNode
	 *            The first node.
	 * @param toNode
	 *            The second node.
	 * @return The edge that goes from the first node to the second node.
	 * @throws LotGraphException
	 *             If either node cannot be found.
	 */
	public LotEdge getEdgeFromTo(LotNode fromNode, LotNode toNode) throws LotGraphException {
		if (this.hasNode(fromNode) && this.hasNode(toNode)) {
			if (this.getNode(fromNode).hasEdgeTo(toNode)) {
				return this.getNode(fromNode).getEdgeTo(this.getNode(toNode));
			} else {
				throw new LotGraphException("nodeOne has no edge pointing to nodeTwo.");
			}
		} else {
			if (!this.hasNode(fromNode) && !this.hasNode(toNode)) {
				throw new LotGraphException("Neither nodeOne nor nodeTwo found in stored data.");
			} else if (!this.hasNode(fromNode)) {
				throw new LotGraphException("nodeOne not found in stored data.");
			} else if (!this.hasNode(toNode)) {
				throw new LotGraphException("nodeTwo not found in stored data.");
			} else {
				throw new LotGraphException(
						"Unspecified error in getEdgeFromTo(LotNode,LotNode). You should not get this.");
			}
		}
	}// getEdgeFromTo(LotNode,LotNode)

	/**
	 * Gets the edge that goes from one node to another.
	 * 
	 * @param fromNodeId
	 *            The id of the first node.
	 * @param toNodeId
	 *            The id of the second node.
	 * @return The edge that goes from nodeOne to nodeTwo.
	 * @throws LotGraphException
	 *             If either node cannot be found.
	 */
	public LotEdge getEdgeFromTo(String fromNodeId, String toNodeId) throws LotGraphException {
		if (this.hasNode(fromNodeId) && this.hasNode(toNodeId)) {
			if (this.getNode(fromNodeId).hasEdgeTo(toNodeId)) {
				return this.getNode(fromNodeId).getEdgeTo(this.getNode(toNodeId));
			} else {
				throw new LotGraphException("nodeOne has no edge pointing to nodeTwo.");
			}
		} else {
			if (!this.hasNode(fromNodeId) && !this.hasNode(toNodeId)) {
				throw new LotGraphException("Neither nodeOne nor nodeTwo found in stored data.");
			} else if (!this.hasNode(fromNodeId)) {
				throw new LotGraphException("nodeOne not found in stored data.");
			} else if (!this.hasNode(toNodeId)) {
				throw new LotGraphException("nodeTwo not found in stored data.");
			} else {
				throw new LotGraphException(
						"Unspecified error in getEdgeFromTo(LotNode,LotNode). You should not get this.");
			}
		}
	}// getEdgeFromTo(String,String)

	/**
	 * Gets the edge from one index to another. Wrapper for 'getEdge(int,int)',
	 * for method consistency. Probably best to use 'getEdge(int,int)' for
	 * simplicity's sake.
	 * 
	 * @param fromNodeIndex
	 *            The index of the first node in the node list.
	 * @param toNodeIndex
	 *            The index of the second node in the node list.
	 * @return The edge between the nodes.
	 * @throws LotGraphException
	 *             If either node cannot be found.
	 */
	public LotEdge getEdgeFromTo(int fromNodeIndex, int toNodeIndex) throws LotGraphException {
		if (this.hasNode(fromNodeIndex) && this.hasNode(toNodeIndex)) {
			if (this.getNode(fromNodeIndex).hasEdgeTo(this.getNode(toNodeIndex))) {
				try {
					return this.getNode(fromNodeIndex).getEdgeTo(this.getNode(toNodeIndex));
				} catch (LotGraphException e) {
					System.out.println("FATAL-ERROR- getEdgeFromTo(LotNode,LotNode)- you should not get this. Error: "
							+ e.getMessage());
					System.exit(1);
				}
			} else {
				throw new LotGraphException("nodeOne has no edge pointing to nodeTwo.");
			}
		} else {
			if (!this.hasNode(fromNodeIndex) && !this.hasNode(toNodeIndex)) {
				throw new LotGraphException("Neither nodeOne nor nodeTwo found in stored data.");
			} else if (!this.hasNode(fromNodeIndex)) {
				throw new LotGraphException("nodeOne not found in stored data.");
			} else if (!this.hasNode(toNodeIndex)) {
				throw new LotGraphException("nodeTwo not found in stored data.");
			} else {
				throw new LotGraphException(
						"Unspecified error in getEdgeFromTo(LotNode,LotNode). You should not get this.");
			}
		}
		return null;
	}// getEdgeFromTo(int,int)
	
	/**
	 * Gets a list of edges going from one node to another.
	 * 
	 * @param fromNode	The node we are coming from.
	 * @param toNode	The node we are going to.
	 * @return	A list of edges going from one node to another
	 * @throws LotGraphException	If either node given is not in the data set.
	 */
	public ArrayList<LotEdge> getEdgesFromTo(LotNode fromNode, LotNode toNode) throws LotGraphException{
		if (this.hasNode(fromNode) && this.hasNode(toNode)) {
			ArrayList<LotEdge> edgesFromTo = new ArrayList<LotEdge>();
			
			for(LotEdge curEdge : fromNode.getConnectedEdges()){
				if(curEdge.getEndNode() == toNode){
					edgesFromTo.add(curEdge);
				}
			}
			
			return edgesFromTo;
		} else {
			if (!this.hasNode(fromNode) && !this.hasNode(toNode)) {
				throw new LotGraphException("Neither nodeOne nor nodeTwo found in stored data.");
			} else if (!this.hasNode(fromNode)) {
				throw new LotGraphException("nodeOne not found in stored data.");
			} else if (!this.hasNode(toNode)) {
				throw new LotGraphException("nodeTwo not found in stored data.");
			} else {
				throw new LotGraphException(
						"Unspecified error in getEdgeFromTo(LotNode,LotNode). You should not get this.");
			}
		}
	}

	/**
	 * Determines if there is an edge from the first node to the second node.
	 * 
	 * @param fromNode
	 *            The first node.
	 * @param toNode
	 *            The second node.
	 * @return If there is an edge going from the first to the second node.
	 */
	public boolean hasEdgeFromTo(LotNode fromNode, LotNode toNode) {
		try {
			if (this.getEdgeFromTo(fromNode, toNode) != null) {
				return true;
			}
		} catch (LotGraphException e) {
			// throw err;
		}
		return false;
	}// hasEdgeFromTo(LotNode,LotNode)

	/**
	 * Determines if there is an edge from the first node to the second node.
	 * 
	 * @param fromNodeId
	 *            The ID of the first node.
	 * @param toNodeId
	 *            The ID of the second node.
	 * @return If there is an edge going from the first to the second node.
	 */
	public boolean hasEdgeFromTo(String fromNodeId, String toNodeId) {
		try {
			if (this.getEdgeFromTo(fromNodeId, toNodeId) != null) {
				return true;
			}
		} catch (LotGraphException e) {
			// throw err;
		}
		return false;
	}// hasEdgeFromTo(String, String)

	/**
	 * Tests if there are edges going from one node to another
	 * 
	 * @param fromNodeIndex
	 *            The index of the first node in the node list
	 * @param toNodeIndex
	 *            The index of the second node in the node list
	 * @return If there is an edge going from the first to the second node.
	 */
	public boolean hasEdgeFromTo(int fromNodeIndex, int toNodeIndex) {
		try {
			if ((this.getEdgeFromTo(fromNodeIndex, toNodeIndex) != null)) {
				return true;
			}
		} catch (LotGraphException e) {

		}
		return false;
	}// hasEdge(int,int)

	/**
	 * Checks if the two nodes have edges both ways.
	 * 
	 * @param nodeOneIndex
	 *            The index of the first node in the node list.
	 * @param nodeTwoIndex
	 *            The index of the second node in the node list.
	 * @return If the nodes have an edge to each other or not.
	 * @throws LotGraphException
	 *             If either of the nodes can't be found.
	 */
	public boolean hasEdgeBothWays(int nodeOneIndex, int nodeTwoIndex) throws LotGraphException {
		if (this.hasEdgeFromTo(nodeOneIndex, nodeTwoIndex) && this.hasEdgeFromTo(nodeTwoIndex, nodeOneIndex)) {
			return true;
		}
		return false;
	}// hasEdgeBothWays(int, int)

	/**
	 * Checks if the two nodes have edges both ways.
	 * 
	 * @param nodeOne
	 *            The first node.
	 * @param nodeTwo
	 *            The second node.
	 * @return If the nodes have an edge to each other or not.
	 */
	public boolean hasEdgeBothWays(LotNode nodeOne, LotNode nodeTwo){
		if (this.hasEdgeFromTo(nodeOne, nodeTwo) && this.hasEdgeFromTo(nodeTwo, nodeOne)) {
			return true;
		}
		return false;
	}// hasEdgeBothWays(LotNode, LotNode)

	/**
	 * Checks if the two nodes have edges both ways.
	 * 
	 * @param nodeOneId
	 *            The first node's ID.
	 * @param nodeTwoId
	 *            The second node's ID.
	 * @return If the nodes have an edge to each other or not.
	 * @throws LotGraphException
	 *             If either of the nodes can't be found.
	 */
	public boolean hasEdgeBothWays(String nodeOneId, String nodeTwoId) throws LotGraphException {
		if (this.hasEdgeFromTo(nodeOneId, nodeTwoId) && this.hasEdgeFromTo(nodeTwoId, nodeOneId)) {
			return true;
		}
		return false;
	}// hasEdgeBothWays(String, String)
	
	/**
	 *  Gets all the edges pointing to a particular node.
	 *  
	 * @param nodeIn	The the node we are getting the edges to.
	 * @return	All the edges pointing to the node given.
	 * @throws LotGraphException	If the node given is not found.
	 */
	public ArrayList<LotEdge> getEdgesToNode(LotNode nodeIn) throws LotGraphException{
		if(this.hasNode(nodeIn)){
			if(!this.hasEdgeToNode(nodeIn)){
				return new ArrayList<LotEdge>();
			}
			ArrayList<LotEdge> edgesToNode = new ArrayList<LotEdge>();
			for(LotNode curNode : this.getNodes()){
				if(curNode.hasEdgeTo(nodeIn)){
					for(LotEdge curEdge : curNode.getEdges()){
						if(curEdge.getEndNode() == nodeIn){
							edgesToNode.add(curEdge);
						}
					}
				}//if something goes to the node given
			}//main loop
			return edgesToNode;
		}else{
			throw new LotGraphException("The id given does not belong to any held nodes.");
		}
	}
	
	/**
	 * Gets all the edges pointing to a particular node.
	 * 
	 * @param nodeIdIn	The Id of the node we are getting the edges to.
	 * @return	All the edges pointing to the node given.
	 * @throws LotGraphException	If the node given is not found.
	 */
	public ArrayList<LotEdge> getEdgesToNode(String nodeIdIn) throws LotGraphException{
		if(this.hasNode(nodeIdIn)){
			return this.getEdgesToNode(this.getNode(nodeIdIn));
		}else{
			throw new LotGraphException("The id given does not belong to any held nodes.");
		}
	}
	
	/**
	 * Gets all the edges pointing to a particular node.
	 * 
	 * @param nodeIndexIn	The index of the node we care about.
	 * @return	All the edges pointing to a particular node.
	 * @throws LotGraphException	If the node given is not found.
	 */
	public ArrayList<LotEdge> getEdgesToNode(int nodeIndexIn) throws LotGraphException{
		if(this.hasNode(nodeIndexIn)){
			return this.getEdgesToNode(this.getNode(nodeIndexIn));
		}else{
			throw new LotGraphException("The id given does not belong to any held nodes.");
		}
	}
	
	/**
	 * Gets all the edges pointing to a particular node, excluding ones contained in the given list.
	 * 
	 * @param nodeIn	The node we are getting the edges to. 
	 * @param edgesToAvoid	A list of edges to not concern ourselves with.
	 * @return	All the edges pointing to a particular node, excluding ones contained in the given list.
	 * @throws LotGraphException If the node given is not contained within the data.
	 */
	public ArrayList<LotEdge> getEdgesToNode(LotNode nodeIn, Collection<LotEdge> edgesToAvoid) throws LotGraphException{
		ArrayList<LotEdge> tempList = this.getEdgesToNode(nodeIn);
		tempList.removeAll(edgesToAvoid);
		return tempList;
	}
	
	/**
	 * Gets all the edges pointing to a particular node, excluding ones contained in the given list.
	 * 
	 * @param nodeIdIn	The Id of the node we are getting the edges to. 
	 * @param edgesToAvoid	A list of edges to not concern ourselves with.
	 * @return	All the edges pointing to a particular node, excluding ones contained in the given list.
	 * @throws LotGraphException If the node Id given is not contained within the data.
	 */
	public ArrayList<LotEdge> getEdgesToNode(String nodeIdIn, Collection<LotEdge> edgesToAvoid) throws LotGraphException{
		ArrayList<LotEdge> tempList = this.getEdgesToNode(nodeIdIn);
		tempList.removeAll(edgesToAvoid);
		return tempList;
	}
	
	/**
	 * Gets all the edges pointing to a particular node, excluding ones contained in the given list.
	 * 
	 * @param nodeIndexIn	The index of the node we are getting the edges to. 
	 * @param edgesToAvoid	A list of edges to not concern ourselves with.
	 * @return	All the edges pointing to a particular node, excluding ones contained in the given list.
	 * @throws LotGraphException If the node index given is not contained within the data.
	 */
	public ArrayList<LotEdge> getEdgesToNode(int nodeIndexIn, Collection<LotEdge> edgesToAvoid) throws LotGraphException{
		ArrayList<LotEdge> tempList = this.getEdgesToNode(nodeIndexIn);
		tempList.removeAll(edgesToAvoid);
		return tempList;
	}
	
	/**
	 * Determines if there are edges pointing to the given node.
	 * 
	 * @param nodeIn	The node we are testing.
	 * @return	If there are any edges pointing to the given node.
	 * @throws LotGraphException	If the node given is not within the dataset.
	 */
	public boolean hasEdgeToNode(LotNode nodeIn) throws LotGraphException{
		if(this.hasNode(nodeIn)){
			for(LotNode curNode : this.getNodes()){
				if(curNode.hasEdgeTo(nodeIn)){
					return true;
				}//if something goes to the node given
			}//main loop
		}else{
			throw new LotGraphException("The given node is not within the dataset.");
		}
		return false; 
	}
	
	/**
	 * Determines if there are edges pointing to the given node.
	 * 
	 * @param nodeIdIn	The id of the node we are testing.
	 * @return	If the node has any edges going to it.
	 * @throws LotGraphException	If the node given is not within the dataset.
	 */
	public boolean hasEdgeTo(String nodeIdIn) throws LotGraphException{
		if(this.hasNode(nodeIdIn)){
			return this.hasEdgeToNode(this.getNode(nodeIdIn));
		}else{
			throw new LotGraphException("The id given does not belong to any held nodes.");
		}
	}
	
	/**
	 * Determines if there are edges pointing to the given node.
	 * 
	 * @param nodeIndexIn	The index of the node we are testing.
	 * @return	If the node has any edges going to it.
	 * @throws LotGraphException	If the node given is not within the dataset.
	 */
	public boolean hasEdgeToNode(int nodeIndexIn) throws LotGraphException{
		if(this.hasNode(nodeIndexIn)){
			return this.hasEdgeToNode(this.getNode(nodeIndexIn));
		}else{
			throw new LotGraphException("The id given does not belong to any held nodes.");
		}
	}
	
	/**
	 * Determines if there are any edges pointing to a specified node, not counting edges in the given list.
	 * 
	 * @param nodeIn	The node we are dealing with.
	 * @param edgesToAvoid	Edges we are not considering.
	 * @return	If there are any edges pointing to the specified node, not counting edges in the given list.
	 * @throws LotGraphException	If the node given isn't in the data set.
	 */
	public boolean hasEdgeToNode(LotNode nodeIn, Collection<LotEdge> edgesToAvoid) throws LotGraphException{
		if(this.hasNode(nodeIn)){
			if(this.getEdgesToNode(nodeIn, edgesToAvoid).size() > 0){
				return true;
			}
		}else{
			throw new LotGraphException("The given node is not within the dataset.");
		}
		return false; 
	}//hasEdgeToNode(LotNode, Sollection<LotEdge>)
	
	/**
	 * Determines if there are any edges pointing to a specified node, not counting edges in the given list.
	 * 
	 * @param nodeIdIn	The id of the node we are dealing with.
	 * @param edgesToAvoid	Edges we are not considering.
	 * @return	If there are any edges pointing to the specified node, not counting edges in the given list.
	 * @throws LotGraphException	If the node given isn't in the data set.
	 */
	public boolean hasEdgeToNode(String nodeIdIn, Collection<LotEdge> edgesToAvoid) throws LotGraphException{
		if(this.hasNode(nodeIdIn)){
			if(this.getEdgesToNode(nodeIdIn, edgesToAvoid).size() > 0){
				return true;
			}
		}else{
			throw new LotGraphException("The given node is not within the dataset.");
		}
		return false; 
	}//hasEdgeToNode(LotNode, Sollection<LotEdge>)
	
	/**
	 * Determines if there are any edges pointing to a specified node, not counting edges in the given list.
	 * 
	 * @param nodeIndexIn	The index of the node we are dealing with.
	 * @param edgesToAvoid	Edges we are not considering.
	 * @return	If there are any edges pointing to the specified node, not counting edges in the given list.
	 * @throws LotGraphException	If the node given isn't in the data set.
	 */
	public boolean hasEdgeToNode(int nodeIndexIn, Collection<LotEdge> edgesToAvoid) throws LotGraphException{
		if(this.hasNode(nodeIndexIn)){
			if(this.getEdgesToNode(nodeIndexIn, edgesToAvoid).size() > 0){
				return true;
			}
		}else{
			throw new LotGraphException("The given node is not within the dataset.");
		}
		return false; 
	}//hasEdgeToNode(LotNode, Sollection<LotEdge>)
	
	/**
	 * Gets the number of edges the node has going to it.
	 * 
	 * @param nodeIn	The node we are testing.
	 * @return	The number of edges going to the node.
	 * @throws LotGraphException	If that node is not within the dataset.
	 */
	public int getNumEdgesToNode(LotNode nodeIn) throws LotGraphException{
		return this.getEdgesToNode(nodeIn).size();
	}
	
	/**
	 * Gets the number of edges the node has going to it.
	 * 
	 * @param nodeIdIn	The id of the node we are testing.
	 * @return	The number of edges going to the node.
	 * @throws LotGraphException	If that node is not within the dataset.
	 */
	public int getNumEdgesToNode(String nodeIdIn) throws LotGraphException{
		return this.getEdgesToNode(nodeIdIn).size();
	}
	
	/**
	 * Gets the number of edges the node has going to it.
	 * 
	 * @param nodeIndexIn	The index of the node we are testing.
	 * @return	The number of edges going to the node.
	 * @throws LotGraphException	If that node is not within the data set.
	 */
	public int getNumEdgesToNode(int nodeIndexIn) throws LotGraphException{
		return this.getEdgesToNode(nodeIndexIn).size();
	}
	
	/**
	 * Gets the number of edges the node has going to it, excluding the ones in the given list.
	 * 
	 * @param nodeIn	The node we are dealing with.
	 * @param edgesToAvoid	A list of edges to not consider.
	 * @return	The number of edges going to the node not in the list given.
	 * @throws LotGraphException	If the node given is not in the data set.
	 */
	public int getNumEdgesToNode(LotNode nodeIn, Collection<LotEdge> edgesToAvoid) throws LotGraphException{
		return this.getEdgesToNode(nodeIn, edgesToAvoid).size();
	}
	
	/**
	 * Gets the number of edges the node has going to it, excluding the ones in the given list.
	 * 
	 * @param nodeIdIn	The Id of the node we are dealing with.
	 * @param edgesToAvoid	A list of edges to not consider.
	 * @return	The number of edges going to the node not in the list given.
	 * @throws LotGraphException	If the node given is not in the data set.
	 */
	public int getNumEdgesToNode(String nodeIdIn, Collection<LotEdge> edgesToAvoid) throws LotGraphException{
		return this.getEdgesToNode(nodeIdIn, edgesToAvoid).size();
	}
	
	/**
	 * Gets the number of edges the node has going to it, excluding the ones in the given list.
	 * 
	 * @param nodeIndexIn	The Index of the node we are dealing with.
	 * @param edgesToAvoid	A list of edges to not consider.
	 * @return	The number of edges going to the node not in the list given.
	 * @throws LotGraphException	If the node given is not in the data set.
	 */
	public int getNumEdgesToNode(int nodeIndexIn, Collection<LotEdge> edgesToAvoid) throws LotGraphException{
		return this.getEdgesToNode(nodeIndexIn, edgesToAvoid).size();
	}
	
	/**
	 * Gets a list of edges with a particular attribute, no matter the value.
	 * 
	 * @param attKeyIn	The attribute key to check for.
	 * @return	A list of edges with the given attribute key.
	 */
	public ArrayList<LotEdge> getEdgesWithAtt(String attKeyIn){
		ArrayList<LotEdge> edgeList = this.getEdgeList();
		for(int i = 0; i < edgeList.size(); i++){
			if(!edgeList.get(i).hasAtt(attKeyIn)){
				edgeList.remove(i);
				i--;
			}
		}
		return edgeList;
	}//getEdgesWithAtt(String)
	
	/**
	 * Gets a list of edges with a particular key-value pair.
	 * 
	 * @param attKeyIn	The attribute key.
	 * @param attValIn	The attribute value.
	 * @return	A list of edges with the given attribute value pair.
	 */
	public ArrayList<LotEdge> getEdgesWithAtt(String attKeyIn, String attValIn){
		ArrayList<LotEdge> edgeList = this.getEdgeList();
		for(int i = 0; i < edgeList.size(); i++){
			if(!edgeList.get(i).hasAtt(attKeyIn, attValIn)){
				edgeList.remove(i);
				i--;
			}
		}
		return edgeList;
	}//getEdgesWithAtt(String, String)
	
	/**
	 * Gets the first occurrence of an edge with the given attribute key.
	 * 
	 * @param attKeyIn	The attribute key to search for.
	 * @return	The first occurrence of an edge with the given attribute key. Null if none found.
	 */
	public LotEdge getEdgeWithAtt(String attKeyIn){
		for(LotEdge curEdge : this.getEdgeList()){
			if(curEdge.hasAtt(attKeyIn)){
				return curEdge;
			}
		}
		return null;
	}
	
	/**
	 * Gets the first occurrence of an edge with the given attribute key/value pair.
	 * 
	 * @param attKeyIn	The attribute key to search for.
	 * @param attValIn	The attribute value to search for.
	 * @return	The first occurrence of an edge with the given attribute key/value pair. Null if none found.
	 */
	public LotEdge getEdgeWithAtt(String attKeyIn, String attValIn){
		for(LotEdge curEdge : this.getEdgeList()){
			if(curEdge.hasAtt(attKeyIn, attValIn)){
				return curEdge;
			}
		}
		return null;
	}
	
	/**
	 * Gets a list of nodes with a particular attribute, no matter the value.
	 * 
	 * @param attKeyIn	The key of the attribute.
	 * @return	A list of nodes with the given attribute.
	 */
	public ArrayList<LotNode> getNodesWithAtt(String attKeyIn){
		ArrayList<LotNode> nodeList = this.getNodes();
		for(int i = 0; i < nodeList.size(); i++){
			if(!nodeList.get(i).hasAtt(attKeyIn)){
				nodeList.remove(i);
				i--;
			}
		}
		return nodeList;
	}//getNodesWithAtt(String)
	
	/**
	 * Gets a list of nodes with a particular attribute key-value pair.
	 * 
	 * @param attKeyIn	The key of the attribute.
	 * @param attValIn	The value of the attribute.
	 * @return	A list of nodes with a particular attribute key-value pair.
	 */
	public ArrayList<LotNode> getNodesWithAtt(String attKeyIn, String attValIn){
		ArrayList<LotNode> nodeList = this.getNodes();
		for(int i = 0; i < nodeList.size(); i++){
			if(!nodeList.get(i).hasAtt(attKeyIn, attValIn)){
				nodeList.remove(i);
				i--;
			}
		}
		return nodeList;
	}//getNodesWithAtt(String, String)
	
	/**
	 * Gets the first occurrence of a node with the given attribute key.
	 * @param attKeyIn	The key to search for.
	 * @return	The first occurrence of a node with the given attribute key. Null if none found.
	 */
	public LotNode getNodeWithAtt(String attKeyIn){
		for(LotNode curNode: this.getNodes()){
			if(curNode.hasAtt(attKeyIn)){
				return curNode;
			}
		}
		return null;
	}
	
	/**
	 * Gets the first occurrence of a node with the given attribute key/value pair.
	 * @param attKeyIn	The key to search for.
	 * @param attValIn	The value to search for.
	 * @return	The first occurrence of a node with the given attribute key/value pair. Null if none found.
	 */
	public LotNode getNodeWithAtt(String attKeyIn, String attValIn){
		for(LotNode curNode: this.getNodes()){
			if(curNode.hasAtt(attKeyIn, attValIn)){
				return curNode;
			}
		}
		return null;
	}
	
	/**
	 * Gets a list of edges going to nodes with a particular attribute no matter the value.
	 * 
	 * @param attKeyIn	The key of the attribute.
	 * @return	A list of edges going to nodes with a particular attribute no matter the value.
	 */
	public ArrayList<LotEdge> getEdgesToNodesWithAtt(String attKeyIn){
		ArrayList<LotNode> nodeList = this.getNodesWithAtt(attKeyIn);
		ArrayList<LotEdge> edgeList = new ArrayList<LotEdge>();
		for(LotNode curNode : nodeList){
			try {
				edgeList.addAll(this.getEdgesToNode(curNode));
			} catch (LotGraphException e) {
				e.printStackTrace();
				System.out.println("FATAL ERROR- This should not happen. Error: " + e.getMessage());
				System.exit(1);
			}
		}
		
		return edgeList;
	}//getEdgesToNodesWithAtt(String)
	
	/**
	 * Gets a list of edges going to nodes with a particular attribute key-value pair.
	 * 
	 * @param attKeyIn	The key of the attribute.
	 * @param attValIn	The value of the attribute.
	 * @return	A list of edges going to nodes with a particular attribute key-value pair.
	 */
	public ArrayList<LotEdge> getEdgesToNodesWithAtt(String attKeyIn, String attValIn){
		ArrayList<LotNode> nodeList = this.getNodesWithAtt(attKeyIn, attValIn);
		ArrayList<LotEdge> edgeList = new ArrayList<LotEdge>();
		for(LotNode curNode : nodeList){
			try {
				edgeList.addAll(this.getEdgesToNode(curNode));
			} catch (LotGraphException e) {
				e.printStackTrace();
				System.out.println("FATAL ERROR- This should not happen. Error: " + e.getMessage());
				System.exit(1);
			}
		}
		
		return edgeList;
	}//getEdgesToNodesWithAtt(String, String)

	/**
	 * Gets a list of edges without the specified attribute key.
	 * @param attKeyIn The key for attributes not to have in them.
	 * @return A list of edges without the specified attribute key.
	 */
	public ArrayList<LotEdge> getEdgesWithOutAtt(String attKeyIn){
		ArrayList<LotEdge> edgeList = this.getEdgeList();
		edgeList.removeAll(this.getEdgesWithAtt(attKeyIn));
		return edgeList;
	}
	
	/**
	 * Gets a list of edges without the specified attribute key/value pair.
	 * @param attKeyIn The key for attributes not to have in them.
	 * @param attValIn The value associated with the key for attributes not to have in them.
	 * @return A list of edges without the specified attribute key/value pair.
	 */
	public ArrayList<LotEdge> getEdgesWithOutAtt(String attKeyIn, String attValIn){
		ArrayList<LotEdge> edgeList = this.getEdgeList();
		edgeList.removeAll(this.getEdgesWithAtt(attKeyIn, attValIn));
		return edgeList;
	}
	
	/**
	 * Gets a list of nodes without the given attribute key.
	 * @param attKeyIn The key for the nodes not to have.
	 * @return A list of nodes without the given attribute key.
	 */
	public ArrayList<LotNode> getNodesWithOutAtt(String attKeyIn){
		ArrayList<LotNode> nodeList = new ArrayList<LotNode>(this.nodes);//new one to not remove portions of the main node list
		nodeList.removeAll(this.getNodesWithAtt(attKeyIn));
		return nodeList;
	}
	
	/**
	 * Gets a list of nodes without the given attribute key/value pair.
	 * @param attKeyIn The key for the nodes not to have.
	 * @param attValIn The value associated with the key for attributes not to have in them.
	 * @return A list of nodes without the given attribute key/value pair.
	 */
	public ArrayList<LotNode> getNodesWithOutAtt(String attKeyIn, String attValIn){
		ArrayList<LotNode> nodeList = new ArrayList<LotNode>(this.nodes);//new one to not remove portions of the main node list
		nodeList.removeAll(this.getNodesWithAtt(attKeyIn, attValIn));
		return nodeList;
	}
	
	/**
	 * Gets a list of edges that point to nodes that don't have a specified attribute key.
	 * @param attKeyIn The key to not have in the nodes to get edges for.
	 * @return A list of edges that point to nodes that don't have a specified attribute key.
	 */
	public ArrayList<LotEdge> getEdgesToNodesWithOutAtt(String attKeyIn){
		ArrayList<LotNode> nodeList = this.getNodesWithOutAtt(attKeyIn);
		ArrayList<LotEdge> edgeList = new ArrayList<LotEdge>();
		for(LotNode curNode : nodeList){
			try {
				edgeList.addAll(this.getEdgesToNode(curNode));
			} catch (LotGraphException e) {
				e.printStackTrace();
				System.out.println("FATAL ERROR- This should not happen. Error: " + e.getMessage());
				System.exit(1);
			}
		}
		return edgeList;
	}
	
	/**
	 * Gets a list of edges that point to nodes that don't have a specified attribute key/value pair.
	 * @param attKeyIn The key to not have in the nodes to get edges for.
	 * @param attValIn The value associated with the key.
	 * @return A list of edges that point to nodes that don't have a specified attribute key/value pair.
	 */
	public ArrayList<LotEdge> getEdgesToNodesWithOutAtt(String attKeyIn, String attValIn){
		ArrayList<LotNode> nodeList = this.getNodesWithOutAtt(attKeyIn, attValIn);
		ArrayList<LotEdge> edgeList = new ArrayList<LotEdge>();
		for(LotNode curNode : nodeList){
			try {
				edgeList.addAll(this.getEdgesToNode(curNode));
			} catch (LotGraphException e) {
				e.printStackTrace();
				System.out.println("FATAL ERROR- This should not happen. Error: " + e.getMessage());
				System.exit(1);
			}
		}
		return edgeList;
	}

	/**
	 * Gets a list of connected edges.
	 * 
	 * @param curNode	The node we are currently at.
	 * @return	A list of connected edges.
	 */
	public ArrayList<LotEdge> getConnectedEdges(LotNode curNode){
		return this.getConnectedEdges(curNode, new ArrayList<LotEdge>());
	}
	
	/**
	 * Gets a list of connected edges, not considering the ones in the given collection.
	 * 
	 * @param curNode	The node we are currently at.
	 * @param edgesToAvoid	A list of edges to ignore.
	 * @return	A list of connected edges, not considering the ones in the given collection.
	 */
	public ArrayList<LotEdge> getConnectedEdges(LotNode curNode, Collection<LotEdge> edgesToAvoid){
		ArrayList<LotNode> connectedNodeList = this.getConnectedNodes(curNode, edgesToAvoid);
		ArrayList<LotEdge> connectedEdgeList = new ArrayList<LotEdge>();
		ArrayList<LotEdge> tempEdgeList = null;
		for(LotNode currentNode : connectedNodeList){
			for(LotNode innerCurNode : connectedNodeList){
				tempEdgeList = new ArrayList<LotEdge>();
				tempEdgeList.addAll(currentNode.getEdgesTo(innerCurNode));
				tempEdgeList.removeAll(edgesToAvoid);
				for(LotEdge curTempEdge : tempEdgeList){
					if(!connectedEdgeList.contains(curTempEdge)){
						connectedEdgeList.add(curTempEdge);
					}
				}
			}
		}// for each connected node
		return connectedEdgeList;
	}
	
	/**
	 * Gets a list of nodes that are connected to the curNode.
	 * 
	 * @param	curNode	The node we are currently at.
	 * @return	A list of nodes that are connected to the curNode.
	 */
	public ArrayList<LotNode> getConnectedNodes(LotNode curNode){
		return this.getConnectedNodes(curNode, new ArrayList<LotEdge>());
	}
	
	/**
	 * Gets a list of nodes that are connected to the curNode.
	 * 
	 * @param curNode	The node we are currently at.
	 * @param edgesToAvoid	A list of edges to avoid going down.
	 * @return	A list of nodes that are connected to the curNode.
	 */
	public ArrayList<LotNode> getConnectedNodes(LotNode curNode, Collection<LotEdge> edgesToAvoid){
		ArrayList<LotNode> nodesConnected = new ArrayList<LotNode>();//nodes that are connected to the curNode (and nodes subsequently attatched to them, and so on...)
		nodesConnected.add(curNode);
		ArrayList<LotNode> tempList = new ArrayList<LotNode>();//temporary list of connected nodes
		ArrayList<LotNode> workingList = new ArrayList<LotNode>();//temporary list of connected nodes
		ArrayList<LotNode> nodesFinished = new ArrayList<LotNode>();//nodes we have hit in the past, to not go in circles
		LotNode workingNode = null;
		
		while(true){
			while(nodesConnected.size() != 0){
				workingNode = nodesConnected.get(0);
				workingList = workingNode.getConnectedNodes();
				
				for(LotNode tempNode : workingList){
					if(!nodesFinished.contains(tempNode) && !nodesConnected.contains(tempNode) && !tempList.contains(tempNode)){
						tempList.add(tempNode);
					}
				}
				nodesFinished.add(nodesConnected.remove(0));
			}//for each in nodes connected
			if(tempList.size() == 0){
				return nodesFinished;
			}else{//else we move nodes we have connected from tempList to nodesConnected
				nodesConnected = new ArrayList<LotNode>(tempList);
				tempList = new ArrayList<LotNode>();
			}
		}//main loop
	}
	
	/**
	 * Gets the ratio of connected nodes to edges (connected to curNode), excluding the edges given.
	 * 
	 * @param	curNode	The node we are currently at.
	 * @param edgesToAvoid	A list of edges not to consider.
	 * @return	The ratio of connected nodes to edges (connected to curNode), excluding the edges given.
	 */
	public double getConnectedNodeEdgeRatio(LotNode curNode, Collection<LotEdge> edgesToAvoid){
		return ((double)this.getConnectedNodes(curNode, edgesToAvoid).size() / (double)this.getConnectedEdges(curNode, edgesToAvoid).size());
	}
	
	/**
	 * Gets the ratio of connected nodes to edges (connected to curNode).
	 * 
	 * @param	curNode	The node we are currently at.
	 * @return	The ratio of connected nodes/edges to curNode 
	 */
	public double getConnectedNodeEdgeRatio(LotNode curNode){
		return this.getConnectedNodeEdgeRatio(curNode, new ArrayList<LotEdge>());
	}
	
	
	/**
	 * Checks to see if the node list is empty
	 * 
	 * @return If the node list is empty
	 */
	public boolean isEmpty() {
		if (this.getNumNodes() > 0) {
			return false;
		} else {
			return true;
		}
	}// isEmpty

	/**
	 * Checks to see if the node list is not empty
	 * 
	 * @return If the node list is not empty
	 */
	public boolean isNotEmpty() {
		return !this.isEmpty();
	}// isEmpty

	/**
	 * Determines if the node with the given ID is full.
	 * <p>
	 * Full meanining that the node has the number of expected edges. ('-1' or
	 * 'LotNode.UNDETERMINED_NUM_EDGES' edges means unsure, and will always be
	 * complete)
	 * 
	 * @param nodeIn
	 *            The node to determine if full.
	 * @return If the node is full or not.
	 * @throws LotGraphException	If the given node is not within the data.
	 */
	public boolean nodeIsFull(LotNode nodeIn) throws LotGraphException {
		if (this.hasNode(nodeIn)) {
			return this.getNode(nodeIn).isFull();
		} else {
			throw new LotGraphException("Node not found within data.");
		}
	}// nodeIsFull(LotNode)

	/**
	 * Determines if the node with the given ID is full.
	 * <p>
	 * Full meanining that the node has the number of expected edges. ('-1'
	 * edges means unsure, and will always be complete)
	 * 
	 * @param nodeIdIn
	 *            The id of the node.
	 * @return If the node is complete or not.
	 * @throws LotGraphException
	 *             If the ID given does not associated with any nodes in the
	 *             graph.
	 */
	public boolean nodeIsFull(String nodeIdIn) throws LotGraphException {
		if (this.hasNode(nodeIdIn)) {
			return this.getNode(nodeIdIn).isFull();
		} else {
			throw new LotGraphException("Node not found within data.");
		}
	}// nodeIsFull(String)

	/**
	 * Determines if the node at the given index (of the node list) is full.
	 * <p>
	 * Complete meanining that the node has the number of expected edges. ('-1'
	 * edges means unsure, and will always be complete)
	 * 
	 * @param nodeIndexIn
	 *            The index of the node in the node list.
	 * @return If the node is complete or not.
	 * @throws LotGraphException
	 *             If the index given is out of bounds.
	 */
	public boolean nodeIsFull(int nodeIndexIn) throws LotGraphException {
		if (this.hasNode(nodeIndexIn)) {
			return this.getNode(nodeIndexIn).isFullAndConnected();
		} else {
			throw new LotGraphException("Node not found within data.");
		}
	}// nodeIsFull(int)

	/**
	 * Checks if the graph is complete, and has been summoned by a dark wizird.
	 * <p>
	 * ... Also goes through each node and sees if they are full. Returns false
	 * if not.
	 * <p>
	 * Recall that nodes with '-1'(default) number of edges are always
	 * considered full.
	 * 
	 * @return If all the nodes are complete (have all the edges they are
	 *         supposed to have) or not.
	 */
	public boolean graphIsComplete() {
		if (this.getNumNodes() < 1) {
			return false;
		}
		for (LotNode curNode : this.getNodes()) {
			if (!curNode.isFullAndConnected()) {
				return false;
			}
		}
		return true;
	}// graphIsComplete()
	
	/**
	 * Returns a list of nodes that are incomplete.
	 * 
	 * @return A list of nodes that are incomplete.
	 */
	public ArrayList<LotNode> getIncompleteNodes(){
		ArrayList<LotNode> incompleteList = new ArrayList<LotNode>();
		for(LotNode curNode : this.getNodes()){
			if(!curNode.isFullAndConnected()){
				incompleteList.add(curNode);
			}
		}
		return incompleteList;
	}//getIncompleteNodes()
	
	/**
	 * Returns an ASCII representation of the graph in an adjacency matrix format. 
	 * 
	 * TODO:: create version that takes in cur and dest node to highlight them somehow
	 * 
	 * @param outputCounts Whether or not to output the counts of nodes and edges at the top of the graph.
	 * @param outputNodeList Whether or not to add the node list after the graph.
	 * @return An ASCII representation of the graph.
	 */
	public String getASCIIGraph(boolean outputCounts, boolean outputNodeList) {
		int numNodes = this.getNumNodes();
		int maxDigits = String.valueOf(numNodes).length();
		String[][] outputArray = new String[numNodes + 2][ numNodes + 2];
		String outputStr = "\n";
		
		if(outputCounts){
			outputStr += "# Nodes: " + this.getNumNodes() + "\n# Edges: " + this.getNumEdges() + "\n  Ratio: " + this.getNodeEdgeRatio() + "\n\n";
		}
		
		//null out graph
		for(int i = 0; i < outputArray.length; i++){
			for(int j = 0; j < outputArray.length; j++){
				outputArray[i][j] = "";
			}
		}
		//fill graph
		for(int i = 2; i < outputArray.length; i++){
			for(int j = 2; j < outputArray.length; j++){
				try {
					outputArray[i][j] = "" + this.getNode(i - 2).getNumEdgesTo(this.getNode(j - 2));
				} catch (LotGraphException e) {
					e.printStackTrace();
					System.out.println("FATAL ERR- getASCIIGraph(boolean)- This should not happen. Error: " + e.getMessage());
					System.exit(1);
				}
			}
		}
		//calculate number of digits to account for
		for(int i = 2; i < outputArray.length; i++){
			for(int j = 2; j < outputArray.length; j++){
				if(String.valueOf(outputArray[i][j]).length() > maxDigits){
					maxDigits = String.valueOf(outputArray[i][j]).length();
				}
			}
		}
		//fix these all in the graph
		for(int i = 0; i < outputArray.length; i++){//do scaffolding
			for(int j = 0; j < (maxDigits + 1);j++){
				outputArray[i][1] += "-";
			}
			outputArray[1][i] = "|";
		}
		for(int j = 0; j < (maxDigits + 1);j++){
			outputArray[0][0] += " ";
		}
		String format = "%"+ (maxDigits + 1) +"d";
		for(int i = 2; i < outputArray.length; i++){//setup numbers
			outputArray[i][0] = String.format(format, (i - 2));
			outputArray[0][i] = String.format(format, (i - 2));
		}
		for(int i = 2; i < outputArray.length; i++){//setup rest
			for(int j = 2; j < outputArray.length; j++){
				outputArray[i][j] = String.format(format, Integer.parseInt(outputArray[i][j]));
			}
		}
		
		//output graph
		for(int i = 0; i < outputArray.length; i++){
			for(int j = 0; j < outputArray.length; j++){
				outputStr += "" + outputArray[j][i];
			}
			outputStr += "\n";
		}
		
		if(outputNodeList){
			int i = 0;
			for(LotNode curNode : this.getNodes()){
				outputStr += "\n\t" + String.format(format,i) + " - " + curNode.toString();
				i++;
			}
		}
		return outputStr;
	}// getASCIIGraph()

	/**
	 * Returns {@link #rand}
	 * 
	 * @return {@link #rand}
	 */
	public Random getRand() {
		return this.rand;
	}// getRand()

	/**
	 * Gets the ratio of Nodes to edges.
	 * 
	 * @return	The ratio of Nodes to completed edges.
	 */
	public double getNodeEdgeRatio(){
		return ((double)this.getNumNodes() / (double)this.getNumEdges());
	}//getNodeEdgeRatio()
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final int maxLen = 10;
		return "LotGraph [nodes=" + (nodes != null ? nodes.subList(0, Math.min(nodes.size(), maxLen)) : null)
				+ ", rand=" + rand + ", edgeIdPred=" + edgeIdPred + ", nodeIdPred=" + nodeIdPred + ", idSaltRange="
				+ idSaltRange + "]";
	}//toString()

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((edgeIdPred == null) ? 0 : edgeIdPred.hashCode());
		result = prime * result + idSaltRange;
		result = prime * result + ((nodeIdPred == null) ? 0 : nodeIdPred.hashCode());
		result = prime * result + ((nodes == null) ? 0 : nodes.hashCode());
		result = prime * result + ((rand == null) ? 0 : rand.hashCode());
		return result;
	}//hashCode()

	// endregion

	// =========================================================================
	// Workers
	// region Workers
	// =========================================================================

	/**
	 * Checks if the edges held by the node are valid.
	 * 
	 * @param nodeIn	The node to check against.
	 * @return	If the edges held by the node are valid.
	 */
	public boolean checkNodeEdges(LotNode nodeIn) {
		for(int i = 0; i < nodeIn.getNumEdges(); i++){
			if(!this.hasNode(nodeIn.getEdge(i).getEndNode()) && (nodeIn.getEdge(i).getEndNode() != null) && nodeIn.getEdge(i).getEndNode() != nodeIn ){
				return false;
			}
		}
		return true;
	}//checkNodeEdges(LotNode)

	/**
	 * Checks if all the nodes in the collection have valid edges.
	 * <p>
	 * Checks against both the nodes already in the graph and in the collection.
	 * 
	 * @param nodesIn	The collection of nodes to check.
	 * @return	If the collection given is valid or not.
	 */
	public boolean checkNodeEdges(Collection<LotNode> nodesIn) {
		for(LotNode curNode : nodesIn){
			if(!checkNodeEdges(curNode) && !nodesIn.contains(curNode)){
				return false;
			}
		}
		return true;
	}//checkNodeEdges(ArrayList<LotNode>)

	/**
	 * Generates a new ID for new nodes or edges.
	 * <p>
	 * Does this by combining two random integers (ideally never getting the
	 * same one twice).
	 * <p>
	 * Not guaranteed unique, use {@link #getNewUniqueId(char)} for that.
	 * 
	 * @param idType
	 *            The type of id to generate (node='n', edge='e').
	 * @return The new ID.
	 */
	private String getNewId(char idType) {
		String output = "";
		switch (idType) {
		case 'n':
			output = nodeIdPred;
			break;
		case 'e':
			output = edgeIdPred;
			break;
		default:
			return null;
		}
		output += String.format("%010d", (new Integer(rand.nextInt(idSaltRange))))
				+ String.format("%010d", (new Integer(rand.nextInt(idSaltRange))));
		return output;
	}// getNewId()

	/**
	 * Gets a guaranteed unique id.
	 * 
	 * @param idType
	 *            The type of ID to get ('n'=node, 'e'=edge).
	 * @return A new, unique ID.
	 */
	public String getNewUniqueId(char idType) {
		String newId = getNewId(idType);
		while (!this.idIsUnique(idType, newId)) {
			newId = getNewId(idType);
		}
		return newId;
	}// getNewUniqueId(char)

	/**
	 * Determines if the id given is unique in the data set.
	 * 
	 * @param idType
	 *            The type of ID ('n'=node, 'e'=edge).
	 * @param idIn
	 *            The id to check.
	 * @return If the given ID is unique.
	 */
	public boolean idIsUnique(char idType, String idIn) {
		switch (idType) {
		case 'n':
			// System.out.println("# nodes: " + this.getNodeListSize() );
			if (this.getNumNodes() == 0) {
				return true;
			}
			for (int i = 0; i < this.getNumNodes(); i++) {
				try {
					if (this.getNode(i).getId().equals(idIn)) {
						return false;
					}
				} catch (LotGraphException err) {
					System.out.println(
							"FATAL-ERROR- idIsUnique(). You should not get this error. Message: " + err.getMessage());
					System.exit(1);
				}
			}
			break;
		case 'e':
			if (this.getNumEdges() == 0) {
				return true;
			}
			ArrayList<LotEdge> tempEdgeList = this.getEdgeList();
			for (int i = 0; i < tempEdgeList.size(); i++) {
				if (tempEdgeList.get(i).getId().equals(idIn)) {
					return false;
				}
			}
			break;
		}
		return true;
	}// idIsUnique(char, String)

	// endregion
}// class LotGraph