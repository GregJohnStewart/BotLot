

import java.util.ArrayList;

import botLot.*;

public class testDriver {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Begin test run");
		
		BotLot navigator = new BotLot();
		
		System.out.println("Adding new nodes...");
		//create 4 nodes
		ArrayList<String> nodeIds = new ArrayList<String>();
		
		for(int i = 0; i < 4; i++){
			//System.out.println("...");
			nodeIds.add(navigator.mainGraph.createNodeGiveId());
			//System.out.println("\tNode added. Id: " + nodeIds.get(i));
		}
		navigator.mainGraph.getNode(nodeIds.get(0)).setActNumEdges(1);
		navigator.mainGraph.getNode(nodeIds.get(1)).setActNumEdges(1);
		navigator.mainGraph.getNode(nodeIds.get(2)).setActNumEdges(1);
		navigator.mainGraph.getNode(nodeIds.get(3)).setActNumEdges(1);
		
		System.out.println("Nodes:");
		
		for(int i = 0; i < 4; i++){
			System.out.println("\t" + navigator.mainGraph.getNodes().get(i));
		}
		
		
		System.out.println("Setting current node...");
		try{
			navigator.setCurNode(nodeIds.get(0));
		}catch(BotLotException err){
			System.out.println("Unable to set the current node. Error: " + err.getMessage());
			System.exit(1);
		}
		
		System.out.println("Adding edges...");
		
		ArrayList<String> edgeIds = new ArrayList<String>();
		
		edgeIds.add(navigator.mainGraph.createEdge(nodeIds.get(0), nodeIds.get(1)).getId());
		edgeIds.add(navigator.mainGraph.createEdge(nodeIds.get(1), nodeIds.get(2)).getId());
		edgeIds.add(navigator.mainGraph.createEdge(nodeIds.get(2), nodeIds.get(3)).getId());
		edgeIds.add(navigator.mainGraph.createEdge(nodeIds.get(3), nodeIds.get(0)).getId());
		
		System.out.println("Edges:");
		for(int i = 0; i < navigator.mainGraph.getEdgeList().size(); i++){
			System.out.println("\t" + navigator.mainGraph.getEdgeList().get(i).toString());
		}
		
		if(navigator.mainGraph.graphIsComplete()){
			System.out.println("Graph is complete.");
		}else{
			System.out.println("Graph is NOT complete.");
		}
		
		
		System.out.println("# Nodes: " + navigator.mainGraph.getNumNodes() + "\n# Edges: " + navigator.mainGraph.getNumEdges());
		System.out.println(navigator.mainGraph.getASCIIGraph());
		
		System.out.println("Calculating path...");
		navigator.calcNewPath(nodeIds.get(3));
		
		System.out.println("Path found: " + navigator.getCurPath().toString());
		
		System.out.println("Following path...");
		navigator.movedToEndOfPath();
		System.out.println("Ended up at: " + navigator.getCurNode().toString());
		System.out.println("Program complete.");
		
	}//main()

}
