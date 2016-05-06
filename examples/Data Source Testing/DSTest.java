
import java.io.IOException;
import botLot.*;
import botLot.lotGraph.LotGraph;
import botLot.lotGraph.LotGraphException;
import botLot.dataSource.*;

/**
 * Tests the BotLot Data sourcing using the latest .jar.
 * 
 * @author Greg Stewart
 */
public class DSTest {

	public final static String xmlLoc = "testMap.xml";

	private static void promptForEnter(){
		try{
			System.in.read();
		}catch(IOException e){
			
		}
	}
	
	private static void doneFail(){
		System.out.println("\n\nExecution complete. Press any key to continue...");
		promptForEnter();
		System.exit(1);
	}
	
	private static void done(){
		System.out.println("\n\nExecution complete. Press any key to continue...");
		promptForEnter();
	}
	
	public static void main(String[] args) {
		BotLot origLot = new BotLot();

		System.out.println("\nBotLot Data Source Test\n" + "Tests the BotLot Data Source utilities\n");

		System.out.println("Creating test data...");
		System.out.println("\tCreating Nodes...");
		try {
			origLot.setCurNode(origLot.mainGraph.createNode());
			origLot.mainGraph.getNode(origLot.getCurNode()).setAtt("name", "Node 0");

			origLot.mainGraph.createNode().setAtt("name", "Node 1");
			origLot.mainGraph.createNode().setAtt("name", "Node 2");
			origLot.setDestNode(origLot.mainGraph.getNodeWithAtt("name", "Node 2"));
			origLot.mainGraph.createNode().setAtt("name", "Node 3");

		} catch (BotLotException e) {
			System.out.println("ERROR:: Failed to create nodes. This should not happen. Error: " + e.getMessage());
			doneFail();
		}

		System.out.println("\tDone.");
		System.out.println("\tCreating Edges...");

		try {
			origLot.mainGraph.createEdge(origLot.getCurNode(), origLot.mainGraph.getNodeWithAtt("name", "Node 1"));
			origLot.mainGraph.createEdge(origLot.mainGraph.getNodeWithAtt("name", "Node 1"), origLot.getCurNode());

			origLot.mainGraph.createEdge(origLot.mainGraph.getNodeWithAtt("name", "Node 1"),
					origLot.mainGraph.getNodeWithAtt("name", "Node 2"));
			origLot.mainGraph.createEdge(origLot.mainGraph.getNodeWithAtt("name", "Node 2"),
					origLot.mainGraph.getNodeWithAtt("name", "Node 1"));

			origLot.mainGraph.createEdge(origLot.mainGraph.getNodeWithAtt("name", "Node 1"),
					origLot.mainGraph.getNodeWithAtt("name", "Node 3"));
			origLot.mainGraph.createEdge(origLot.mainGraph.getNodeWithAtt("name", "Node 3"),
					origLot.mainGraph.getNodeWithAtt("name", "Node 1"));

		} catch (LotGraphException e) {
			System.out.println("ERROR:: Failed to create edges. This should not happen. Error: " + e.getMessage());
			doneFail();
		}

		System.out.println("\tDone.");
		System.out.println("\tCreating path from Node 0 to Node 2...");
		try {
			origLot.calcNewPath();
		} catch (BotLotException e) {
			System.out.println("ERROR:: Unable to generate path. This should not happen. Error: " + e.getMessage());
			doneFail();
		}
		System.out.println("\tDone.");

		System.out.println("Done.\n");
		
		System.out.println("Graph generated:\n\n"+origLot.mainGraph.getASCIIGraph(true, true)+"\n\n");

		System.out.println("Setting up XML Data Source Object...");
		System.out.println("\tSetting location to: " + xmlLoc);
		BotLotXMLDS dataSource = new BotLotXMLDS(xmlLoc);

		System.out.println("Done.\n");

		if (dataSource.ready()) {
			System.out.println("Data source ready to read and write.\n");
		} else if (dataSource.readyToSave()) {
			System.out.println("*Data source only ready to write.\n");
		} else {
			System.out.println("*** Data source NOT ready to read or write.\n");
		}

		System.out.println("Testing writing out of GRAPH...");
		try {
			dataSource.saveGraph(origLot.mainGraph);
		} catch (BotLotDSException e) {
			System.out.println("ERROR:: Unable to write graph out to file. Error: " + e.getMessage());
			doneFail();
		}
		System.out.println("Done.\n");

		System.out.println("Testing reading the GRAPH back...");
		try {
			System.out.println("\tRetrieving the data...");
			LotGraph graphRetrieved = dataSource.getGraph();
			System.out.println("\tDone.");

			System.out.println("\tTesting for correctness...");
			
			//System.out.println("----------------------");
			//System.out.println(origLot.mainGraph.toString());
			//System.out.println("----------------------");
			//System.out.println(graphRetrieved.toString());
			//System.out.println("----------------------");
			
			if (!graphRetrieved.equals(origLot.mainGraph)) {
				System.out.println("FAILED -- graphs not equal.");
				doneFail();
			} else {
				System.out.println("\t\tGraphs are equal!");
			}
			System.out.println("\tDone.");

		} catch (BotLotDSException e) {
			System.out.println("ERROR:: Unable to read graph back from file. Error: " + e.getMessage());
			doneFail();
		}
		System.out.println("Done.\n");
		
		
		System.out.println("Testing writing out of BOTLOT...");
		try {
			dataSource.saveBotLot(origLot);
		} catch (BotLotDSException e) {
			System.out.println("ERROR:: Unable to write graph out to file. Error: " + e.getMessage());
			doneFail();
		}
		System.out.println("Done.\n");

		System.out.println("Testing reading the BOTLOT back...");
		try {
			System.out.println("\tRetrieving the data...");
			BotLot lotRetrieved = dataSource.getBotLot();
			System.out.println("\tDone.");

			System.out.println("\tTesting for correctness...");

			if (!lotRetrieved.equals(origLot)) {
				System.out.println("FAILED -- BOTLOTs not equal.");
				doneFail();
			} else {
				System.out.println("\t\tBOTLOTs are equal!");
			}
			System.out.println("\tDone.");

		} catch (BotLotDSException e) {
			System.out.println("ERROR:: Unable to read BOTLOT back from file. Error: " + e.getMessage());
			doneFail();
		}
		System.out.println("Done.\n");

		System.out.println("\n\nTesting successful!\nAble to store and retrieve graph/botlot data from XML file without issue.");
		done();
	}

}
