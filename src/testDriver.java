
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import botLot.*;

public class testDriver {

	public static void main(String[] args) throws Exception {
		Scanner keyboard = new Scanner(System.in);
		int numNodes = 8;
		String xmlPath = "map.xml";
		boolean useXML = false;
		boolean needNewMap = true;
		String temp;

		ArrayList<String> nodeIds = new ArrayList<String>();
		ArrayList<String> edgeIds = new ArrayList<String>();

		System.out.println("Begin test run");

		BotLot navigator = new BotLot();
		BotLotDS dataSource = new BotLotDS();

		System.out.print("Use XML file (" + xmlPath + ")? (y/n): ");
		temp = keyboard.nextLine();

		if (temp.equals("y") || temp.equals("Y")) {
			useXML = true;
			needNewMap = false;
		}

		if (useXML) {
			try{
			dataSource = new BotLotDS(
					BotLotDS.XML_DATASRC,
					xmlPath
					);
				
			}catch(BotLotDSException e){
				System.out.println("No XML file found. Will create when saving.");
			}
			System.out.println("Data source:\n\t" + dataSource.getType() + "\n\t" + dataSource.getPathLogin() );
			if(dataSource.ready()){
				navigator.mainGraph = dataSource.getDataFromSource();
			}else{
				needNewMap = true;
			}
		}
		if(needNewMap){
			System.out.println("Adding new nodes...");

			for (int i = 0; i < numNodes; i++) {
				// System.out.println("...");
				nodeIds.add(navigator.mainGraph.createNodeGiveId());
				navigator.mainGraph.getNode(nodeIds.get(i)).setActNumEdges(1);
				// System.out.println("\tNode added. Id: " + nodeIds.get(i));
			}

			navigator.mainGraph.getNode(nodeIds.get(1)).setActNumEdges(2);
			navigator.mainGraph.getNode(nodeIds.get(2)).setActNumEdges(3);
			navigator.mainGraph.getNode(nodeIds.get(3)).setActNumEdges(2);
			navigator.mainGraph.getNode(nodeIds.get(5)).setActNumEdges(0);

			System.out.println("Nodes:");

			for (int i = 0; i < numNodes; i++) {
				System.out.println("\t" + navigator.mainGraph.getNodes().get(i));
			}

			System.out.println("Setting current node...");
			try {
				navigator.setCurNode(nodeIds.get(0));
			} catch (BotLotException err) {
				System.out.println("Unable to set the current node. Error: " + err.getMessage());
				System.exit(1);
			}

			System.out.println("Adding edges...");

			edgeIds.add(navigator.mainGraph.createEdge(nodeIds.get(0), nodeIds.get(1)).getId());
			navigator.mainGraph.getEdgeFromTo(0, 1).setMetric(ThreadLocalRandom.current().nextInt(1, 10 + 1));
			edgeIds.add(navigator.mainGraph.createEdge(nodeIds.get(1), nodeIds.get(2)).getId());
			navigator.mainGraph.getEdgeFromTo(1, 2).setMetric(ThreadLocalRandom.current().nextInt(1, 10 + 1));
			edgeIds.add(navigator.mainGraph.createEdge(nodeIds.get(2), nodeIds.get(3)).getId());
			navigator.mainGraph.getEdgeFromTo(2, 3).setMetric(ThreadLocalRandom.current().nextInt(1, 10 + 1));
			edgeIds.add(navigator.mainGraph.createEdge(nodeIds.get(3), nodeIds.get(0)).getId());
			navigator.mainGraph.getEdgeFromTo(3, 0).setMetric(ThreadLocalRandom.current().nextInt(1, 10 + 1));

			edgeIds.add(navigator.mainGraph.createEdge(nodeIds.get(1), nodeIds.get(1)).getId());
			navigator.mainGraph.getEdgeFromTo(1, 1).setMetric(ThreadLocalRandom.current().nextInt(1, 10 + 1));
			edgeIds.add(navigator.mainGraph.createEdge(nodeIds.get(2), nodeIds.get(1)).getId());
			navigator.mainGraph.getEdgeFromTo(2, 1).setMetric(ThreadLocalRandom.current().nextInt(1, 10 + 1));
			edgeIds.add(navigator.mainGraph.createEdge(nodeIds.get(3), nodeIds.get(4)).getId());
			navigator.mainGraph.getEdgeFromTo(3, 4).setMetric(ThreadLocalRandom.current().nextInt(1, 10 + 1));
			edgeIds.add(navigator.mainGraph.createEdge(nodeIds.get(4), nodeIds.get(5)).getId());
			navigator.mainGraph.getEdgeFromTo(4, 5).setMetric(ThreadLocalRandom.current().nextInt(1, 10 + 1));
			edgeIds.add(navigator.mainGraph.createEdge(nodeIds.get(2), nodeIds.get(6)).getId());
			navigator.mainGraph.getEdgeFromTo(2, 6).setMetric(ThreadLocalRandom.current().nextInt(1, 10 + 1));
			edgeIds.add(navigator.mainGraph.createEdge(nodeIds.get(6), nodeIds.get(7)).getId());
			navigator.mainGraph.getEdgeFromTo(6, 7).setMetric(ThreadLocalRandom.current().nextInt(1, 10 + 1));
			edgeIds.add(navigator.mainGraph.createEdge(nodeIds.get(7), nodeIds.get(3)).getId());
			navigator.mainGraph.getEdgeFromTo(7, 3).setMetric(ThreadLocalRandom.current().nextInt(1, 10 + 1));

			System.out.println("Edges:");
			for (int i = 0; i < navigator.mainGraph.getEdgeList().size(); i++) {
				System.out.println("\t" + navigator.mainGraph.getEdgeList().get(i).toString());
			}
		}
		
		navigator.setCurNode(navigator.mainGraph.getNode(0));
		
		if (navigator.mainGraph.graphIsComplete()) {
			System.out.println("Graph is complete.");
		} else {
			System.out.println("Graph is NOT complete.");
		}

		System.out.println(
				"# Nodes: " + navigator.mainGraph.getNumNodes() + "\n# Edges: " + navigator.mainGraph.getNumEdges());
		System.out.println(navigator.mainGraph.getASCIIGraph());

		System.out.println("Calculating path...");
		navigator.calcNewPath(navigator.mainGraph.getNode(3));

		System.out.println("Path found: " + navigator.getCurPath().toString());

		System.out.println("Following path...");
		navigator.movedToEndOfPath();
		System.out.println("Ended up at: " + navigator.getCurNode().toString());
		
		if(useXML){
			dataSource.saveDataToSource(navigator.mainGraph);
		}
		
		System.out.println("Program complete.");

	}// main()

}
