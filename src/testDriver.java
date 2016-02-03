
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import botLot.*;

public class testDriver {

	public static void main(String[] args) throws Exception {
		Scanner keyboard = new Scanner(System.in);
		String xmlPath = "map.xml";//the path to the XML file
		boolean useXML = false;
		boolean needNewMap = true;
		String temp;

		System.out.println("Begin test run");

		BotLot navigator = new BotLot();
		BotLotDS dataSource = new BotLotDS();

		System.out.print("Use XML file (" + xmlPath + ")? (y/n): ");
		temp = keyboard.nextLine();
		keyboard.close();
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

			for (int i = 0; i < 9; i++) {
				// System.out.println("...");
				System.out.println("New Node: " + navigator.mainGraph.createNodeGiveId());
				navigator.mainGraph.getNode(i).setActNumEdges(1);
				// System.out.println("\tNode added. Id: " + nodeIds.get(i));
			}

			navigator.mainGraph.getNode(1).setActNumEdges(2);
			navigator.mainGraph.getNode(2).setActNumEdges(3);
			navigator.mainGraph.getNode(3).setActNumEdges(3);

			System.out.println("Nodes:");

			for (int i = 0; i < navigator.mainGraph.getNumNodes(); i++) {
				System.out.println("\t" + navigator.mainGraph.getNodes().get(i));
			}
			
			//set edges, giving each a random metric between 1 and 10
			System.out.println("Adding edges...");
			navigator.mainGraph.createEdge(0, 1);
			navigator.mainGraph.getEdgeFromTo(0, 1).setMetric(ThreadLocalRandom.current().nextInt(1, 10 + 1));
			navigator.mainGraph.createEdge(1, 2);
			navigator.mainGraph.getEdgeFromTo(1, 2).setMetric(ThreadLocalRandom.current().nextInt(1, 10 + 1));
			navigator.mainGraph.createEdge(2, 3);
			navigator.mainGraph.getEdgeFromTo(2, 3).setMetric(ThreadLocalRandom.current().nextInt(1, 10 + 1));
			navigator.mainGraph.createEdge(3, 0);
			navigator.mainGraph.getEdgeFromTo(3, 0).setMetric(ThreadLocalRandom.current().nextInt(1, 10 + 1));

			navigator.mainGraph.createEdge(1, 1);
			navigator.mainGraph.getEdgeFromTo(1, 1).setMetric(ThreadLocalRandom.current().nextInt(1, 10 + 1));
			navigator.mainGraph.createEdge(2, 1);
			navigator.mainGraph.getEdgeFromTo(2, 1).setMetric(ThreadLocalRandom.current().nextInt(1, 10 + 1));
			navigator.mainGraph.createEdge(3, 4);
			navigator.mainGraph.getEdgeFromTo(3, 4).setMetric(ThreadLocalRandom.current().nextInt(1, 10 + 1));
			navigator.mainGraph.createEdge(4, 5);
			navigator.mainGraph.getEdgeFromTo(4, 5).setMetric(ThreadLocalRandom.current().nextInt(1, 10 + 1));
			navigator.mainGraph.createEdge(2, 6);
			navigator.mainGraph.getEdgeFromTo(2, 6).setMetric(ThreadLocalRandom.current().nextInt(1, 10 + 1));
			navigator.mainGraph.createEdge(6, 7);
			navigator.mainGraph.getEdgeFromTo(6, 7).setMetric(ThreadLocalRandom.current().nextInt(1, 10 + 1));
			navigator.mainGraph.createEdge(7, 3);
			navigator.mainGraph.getEdgeFromTo(7, 3).setMetric(ThreadLocalRandom.current().nextInt(1, 10 + 1));
			navigator.mainGraph.createEdge(3, 2);
			navigator.mainGraph.getEdgeFromTo(3, 2).setMetric(ThreadLocalRandom.current().nextInt(1, 10 + 1));
			navigator.mainGraph.createEdge(5, 8);
			navigator.mainGraph.getEdgeFromTo(5, 8).setMetric(ThreadLocalRandom.current().nextInt(1, 10 + 1));
			navigator.mainGraph.createEdge(8, 5);
			navigator.mainGraph.getEdgeFromTo(8, 5).setMetric(ThreadLocalRandom.current().nextInt(1, 10 + 1));

			System.out.println("Edges:");
			for (int i = 0; i < navigator.mainGraph.getEdgeList().size(); i++) {
				System.out.println("\t" + navigator.mainGraph.getEdgeList().get(i).toString());
			}
		}

		System.out.println("Setting current node...");
		try {
			navigator.setCurNode(0);
		} catch (BotLotException e) {
			System.out.println("Unable to set the current node. Error: " + e.getMessage());
			System.exit(1);
		}
		
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
		System.out.println("\tDone.");
		System.out.println("Path found: " + navigator.getCurPath().toString());

		System.out.println("Following path...");
		navigator.movedToEndOfPath();
		System.out.println("\tDone.");
		System.out.println("Ended up at: " + navigator.getCurNode().toString());
		
		if(useXML){
			System.out.println("Saving data to XML...");
			dataSource.saveDataToSource(navigator.mainGraph);
			System.out.println("\tDone.");
		}
		
		System.out.println("Program complete.");

	}// main()

}
