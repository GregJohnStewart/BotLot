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
 * 
 * @author Greg Stewart
 * @version 1.0 12/7/15
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
	private final String edgeIdPred = "BOTLOTEDGE";
	/** The predicate of new node id's. */
	private final String nodeIdPred = "BOTLOTNODE";
	/** The max value of new ids' salts. */
	private final int idSaltRange = Integer.MAX_VALUE;

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
	 * @throws LotGraphException
	 *             If the node list was found to be invalid.
	 */
	public void setNodes(Collection<LotNode> nodesIn) throws LotGraphException {
		if (checkNodeEdges(nodesIn)) {
			this.nodes = (ArrayList<LotNode>)nodesIn;
		} else {
			throw new LotGraphException("Node list given is invalid.");
		}
	}// setNodes(ArrayList<LotNode>

	/**
	 * Sets a node's edge based on the given variables.
	 * 
	 * @param edgeIn
	 *            The edge to set to.
	 * @param fromNode
	 *            The node we are adding this edge to.
	 * @throws LotGraphException
	 *             If either node does not exist.
	 */
	public void setEdge(LotEdge edgeIn, LotNode fromNode) throws LotGraphException {
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
	}// setEdge(LotEdge, LotNode)

	/**
	 * Sets a node's edge based on the given variables.
	 * 
	 * @param edgeIn
	 *            The edge to give the node.
	 * @param fromNodeId
	 *            The first node's ID.
	 * @throws LotGraphException
	 *             If either node does not exist.
	 */
	public void setEdge(LotEdge edgeIn, String fromNodeId) throws LotGraphException {
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
	}// setEdge(LotEdge,String)

	/**
	 * Sets a node's edge based on the given variables.
	 * 
	 * @param edgeIn
	 *            The edge we are adding to a node.
	 * @param fromNodeIndex
	 *            The node we are dealing with.
	 * @throws LotGraphException
	 *             If either node does not exist.
	 */
	public void setEdge(LotEdge edgeIn, int fromNodeIndex) throws LotGraphException {
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
	

	public LotEdge createEdge(LotNode fromNode) throws LotGraphException{
		LotEdge newEdge = this.createEdge();
		try {
			this.setEdge(newEdge, fromNode);
		} catch (LotGraphException err) {
			throw new LotGraphException("Unable to create edge: " + err.getMessage());
		}
		return newEdge;
	}
	public String createEdgeGiveId(LotNode fromNode) throws LotGraphException{
		return this.createEdge(fromNode).getId();
	}
	
	public LotEdge createEdge(String fromNodeId) throws LotGraphException{
		LotEdge newEdge = this.createEdge();
		try {
			this.setEdge(newEdge, fromNodeId);
		} catch (LotGraphException err) {
			throw new LotGraphException("Unable to create edge: " + err.getMessage());
		}
		return newEdge;
	}
	public String createEdgeGiveId(String fromNodeId) throws LotGraphException{
		return this.createEdge(fromNodeId).getId();
	}
	
	public LotEdge createEdge(int fromNodeIndex) throws LotGraphException{
		LotEdge newEdge = this.createEdge();
		try {
			this.setEdge(newEdge, fromNodeIndex);
		} catch (LotGraphException err) {
			throw new LotGraphException("Unable to create edge: " + err.getMessage());
		}
		return newEdge;
	}
	public String createEdgeGiveId(int fromNodeIndex) throws LotGraphException{
		return this.createEdge(fromNodeIndex).getId();
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
	 * Removes the edge given.
	 * 
	 * @param edgeIn
	 *            The edge to remove.
	 * @throws LotGraphException
	 *             If the edge is not in the data set.
	 */
	public void removeEdge(LotEdge edgeIn) throws LotGraphException {
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
	}// removeEdge(LotEdge)

	/**
	 * Removes the edge given.
	 * 
	 * @param edgeIdIn
	 *            The id of the edge to remove.
	 * @throws LotGraphException
	 *             If the edge is not in the data set.
	 */
	public void removeEdge(String edgeIdIn) throws LotGraphException {
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
	}// removeEdge(String)

	/**
	 * Removes the edge given.
	 * 
	 * TODO:: test this extensively
	 * 
	 * @param edgeIndexIn
	 *            The index of the edge in the generated index list to remove.
	 * @throws LotGraphException
	 *             If the edge is not in the data set.
	 */
	public void removeEdge(int edgeIndexIn) throws LotGraphException {
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
	}// removeEdge(int)

	/**
	 * Removes an edge between two nodes.
	 * 
	 * @param fromNode
	 *            The first node.
	 * @param toNode
	 *            The second node.
	 * @throws LotGraphException
	 *             If either node is not found within {@link #nodes}.
	 */
	public void removeEdgeFromTo(LotNode fromNode, LotNode toNode) throws LotGraphException {
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
	}// removeEdge(LotNode, LotNode)

	/**
	 * Removes an edge between two nodes.
	 * 
	 * @param fromNodeId
	 *            The id of node one.
	 * @param toNodeId
	 *            The id of node two.
	 * @throws LotGraphException
	 *             If either node cannot be found in {@link #nodes}.
	 */
	public void removeEdgeFromTo(String fromNodeId, String toNodeId) throws LotGraphException {
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
	}// removeEdge(String, String)

	/**
	 * Removes an edge based on two node indexes.
	 * 
	 * @param fromNodeIndex
	 *            The index of the first index.
	 * @param toNodeIndex
	 *            The index of the second node.
	 * @throws LotGraphException
	 *             If either node cannot be found in {@link #nodes}.
	 */
	public void removeEdgeFromTo(int fromNodeIndex, int toNodeIndex) throws LotGraphException {
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
	}// removeNode(int,int)

	/**
	 * Adds a new node to {@link #nodes}.
	 * 
	 * @param newNode
	 *            The node to add.
	 * @throws LotGraphException
	 *             If the new node has the same ID of a node already present in
	 *             {@link #nodes}.
	 */
	public void addNode(LotNode newNode) throws LotGraphException {
		if (!checkNodeEdges(newNode)) {
			throw new LotGraphException("Node entered has invalid edges.");
		}
		if (this.idIsUnique('n', newNode.getId())) {
			this.getNodes().add(newNode);
		} else {
			throw new LotGraphException("Node entered has duplicate ID(" + newNode.getId() + ").");
		}
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
	 * Removes the given node from {@link #nodes}. Goes through all edges, nulling the head nodes where they are this node.
	 * 
	 * @param nodeToRemove
	 *            The node to remove from the list.
	 * @throws LotGraphException
	 *             If the node cannot be found.
	 */
	public void removeNode(LotNode nodeToRemove) throws LotGraphException {
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
	}// removeNode(LotNode)

	/**
	 * Removes the node with the given Id from {@link #nodes}. Goes through all edges, nulling the head nodes where they are this node.
	 * 
	 * @param nodeToRemoveId
	 *            The id of the node to remove.
	 * @throws LotGraphException
	 *             If the node cannot be found.
	 */
	public void removeNode(String nodeToRemoveId) throws LotGraphException {
		if (this.hasNode(nodeToRemoveId)) {
			LotNode tempNode = this.getNode(nodeToRemoveId);
			this.removeNode(tempNode);
		} else {
			throw new LotGraphException("The node given is not within stored data.");
		}
	}// removeNode(String)

	/**
	 * Removes the node at the specified index in {@link #nodes} from
	 * {@link #nodes}.  Goes through all edges, nulling the head nodes where they are this node.
	 * 
	 * @param nodeToRemoveIndex
	 *            The index of the node to remove.
	 * @throws LotGraphException
	 *             If the node index is not within the node list.
	 */
	public void removeNode(int nodeToRemoveIndex) throws LotGraphException {
		if (this.hasNode(nodeToRemoveIndex)) {
			LotNode tempNode = this.getNode(nodeToRemoveIndex);
			this.removeNode(tempNode);
		} else {
			throw new LotGraphException("The node given is not within sotred data.");
		}
	}// removeNode(int)

	/**
	 * Sets the random number generator.
	 * 
	 * @param randIn
	 *            The new random number generator.
	 */
	public void setRand(Random randIn) {
		this.rand = randIn;
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
			for (int i = 0; i < this.getNumNodes(); i++) {
				if (this.getNodes().get(i).getId().equals(nodeIdIn)) {
					return i;
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
		for (int i = 0; i < this.getNumNodes(); i++) {
			try {
				edgeList.addAll(this.getNode(i).getEdges());
			} catch (LotGraphException e) {
				System.out.println("FATAL ERROR- getEdgeList()- This should not happen. Error: " + e.getMessage());
				System.exit(1);
			}
		}
		return edgeList;
	}// getEdgeList()

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
	 * Returns an ASCII representation of the graph, with 1's representing
	 * filled edges, and 0's representing empty edges.
	 * 
	 *TODO:: test with numNodes greater than 9
	 * 
	 * @param outputNodeList Whether or not to add the node list after the graph.
	 * @return An ASCII representation of the graph.
	 */
	public String getASCIIGraph(boolean outputNodeList) {
		int numNodes = this.getNumNodes();
		int digits = String.valueOf(numNodes).length();
		String output = "";
		String topBar = "";
		for(int i = 0; i < digits; i++){
			output += " ";
			topBar += "-";
		}
		output += "|";
		topBar += "-";
		for (int i = 0; i < numNodes; i++) {
			output += i;
			for(int j = 0; j < String.valueOf(i).length(); j++){
				topBar += "-";
			}
			for(int j = 0; j < digits - String.valueOf(numNodes).length(); j++){
				output += " ";
				topBar += "-";
			}
			output += " ";
			topBar += "-";
		}
		output = output.substring(0, output.length()-1);
		topBar = topBar.substring(0, topBar.length()-1);
		output += "\n" + topBar;
		for(int i = 0; i < numNodes; i++){
			output += "\n" + i;
			for(int j = 0; j < String.valueOf(i).length() - String.valueOf(numNodes).length(); j++){
				output += " ";
			}
			output += "|";
			for(int j = 0; j < numNodes; j++){
				//TODO:: print out actual number of edges to that node
				if(this.hasEdgeFromTo(j, i)){
					output += "1 ";
				} else {
					output += "0 ";
				}
				for(int k = 0; k < String.valueOf(j).length() - String.valueOf(numNodes).length(); k++){
					output += " ";
				}
			}
			output = output.substring(0, output.length()-1);
		}
		if(outputNodeList){
			int i = 0;
			for(LotNode curNode : this.getNodes()){
				output += "\n\t" + i + " - " + curNode.toString();
				i++;
			}
		}
		return output;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LotGraph other = (LotGraph) obj;
		if (edgeIdPred == null) {
			if (other.edgeIdPred != null)
				return false;
		} else if (!edgeIdPred.equals(other.edgeIdPred))
			return false;
		if (idSaltRange != other.idSaltRange)
			return false;
		if (nodeIdPred == null) {
			if (other.nodeIdPred != null)
				return false;
		} else if (!nodeIdPred.equals(other.nodeIdPred))
			return false;
		if (nodes == null) {
			if (other.nodes != null)
				return false;
		} else if (!nodes.equals(other.nodes))
			return false;
		if (rand == null) {
			if (other.rand != null)
				return false;
		} else if (!rand.equals(other.rand))
			return false;
		return true;
	}//equals(Object)

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