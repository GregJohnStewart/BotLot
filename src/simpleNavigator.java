import java.util.Scanner;

import botLot.*;
import botLot.lotGraph.*;

public class simpleNavigator {
	
	public static void pauseDaThing(){
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println("thread.sleep( actually threw an exception. huh."); 
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public static int getChoice(Scanner kybd){
		int tempInput;
		try{
			tempInput = Integer.parseInt(kybd.nextLine());
		}catch(NumberFormatException e){
			System.out.println("\nInvalid Input. Numerals only.");
			pauseDaThing();
			tempInput = -1;
		}
		return tempInput;
	}

	public static void main(String[] args) {
		BotLot navigator = new BotLot();
		boolean running = true;
		boolean useDataSrc = false;
		boolean gettingInput = false;
		BotLotDataSource dataSource = null;
		Scanner kybd = new Scanner(System.in);
		
		System.out.println("Simple Navigator using the BotLot object.\n");
		
		if(args.length > 0){
			System.out.println("Getting data from XML file...");
			useDataSrc = true;
			try {
				dataSource = new BotLotDataSource(BotLotDataSource.XML_DATASRC, args[0]);
			} catch (BotLotDataSourceException e) {
				System.out.println("\tUnable to setup datasource with XML path given. Error: " + e.getMessage());
				e.printStackTrace();
				System.exit(1);
			}
			try {
				navigator.setGraph(dataSource.getDataFromSource());
			} catch (BotLotDataSourceException e) {
				System.out.println("\tUnable to correctly get data from source. Error: " + e.getMessage());
				e.printStackTrace();
				System.exit(1);
			}
			System.out.print("Done.\n");
			gettingInput = true;
			while(gettingInput){
				System.out.println("Enter ID of the node currently at: ");
				String temp = kybd.nextLine();
				if(navigator.mainGraph.hasNode(temp)){
					gettingInput = false;
					try {
						navigator.setCurNode(temp);
					} catch (BotLotException e) {
						System.out.println("\tThis shouldn't happen. Error: " + e.getMessage());
						e.printStackTrace();
						System.exit(1);
					}
				}else{
					System.out.println("Invalid ID entered.");
				}
			}
			System.out.println("CurNode set. Node ID: " + navigator.getCurNode().getId());
		}
		
		while(running){
			//if empty, create a node
			if(navigator.mainGraph.isEmpty()){
				System.out.println("No data currently set. Adding a node...");
				LotNode tempNode = navigator.mainGraph.createNode();
				try {
					navigator.setCurNode(tempNode);
				} catch (BotLotException e) {
					System.out.println("\tThis shouldn't happen. Error: " + e.getMessage());
					e.printStackTrace();
					System.exit(1);
				}
				System.out.println("Done.\n");
			}
			//prompt for action
			gettingInput = true;
			int mainInput = 0;
			while(gettingInput){
				System.out.println("Nodes:");
				int i = 1;
				for(LotNode curNode : navigator.mainGraph.getNodes()){
					System.out.print("\t" + i + ". " + curNode.getId());
					if(curNode.getAtt("name") != null){
						System.out.print(" No name.");
					}else{
						System.out.print(" " + curNode.getAtt("name"));
					}
					System.out.print("\n");
					i++;
				}
				System.out.println("\nCurrent Node:\n\t" + navigator.getCurNode().getId() + "\n\nEdges: ");
				i = 1;
				for(LotEdge curEdge : navigator.getCurNode().getEdges()){
					System.out.print("\t" + i + " " + curEdge.getId());
					if(curEdge.getAtt("name") != null){
						System.out.print(" No name.");
					}else{
						System.out.print(" " + curEdge.getAtt("name"));
					}
					System.out.print("\n");
					i++;
				}
				System.out.print("\nOptions:\n\t" + 
						"1: Travel down edge\n\t" +
						"2: Set Edge(s)\n\t" + 
						"3: Add node(s)\n\t" + 
						"4: Find Directions\n\t" + 
						"5: Exit\n" + 
						"Input: ");
				mainInput = getChoice(kybd);
				if(mainInput > -1){
					gettingInput = false;
				}
			}//getting main input
			System.out.println();
			int tempChoice;
			boolean tempGettingInput = true;
			switch(mainInput){
			case 1:{
				//travel down an edge
				if(navigator.getCurNode().getNumEdges() == 0){
					System.out.println("No edges outbound from this node.\n");
					break;
				}
				System.out.println("Traveling down an edge. Choices:");
				while(tempGettingInput){
					int i = 0;
					for(LotEdge curEdge : navigator.getCurNode().getEdges()){
						System.out.print("\t" + i + ". " + curEdge.getId());
						if(curEdge.getAtt("name") != null){
							System.out.print(" No name.");
						}else{
							System.out.print(" " + curEdge.getAtt("name"));
						}
						System.out.print("\n");
						i++;
					}
					System.out.print("Input: ");
					tempChoice = getChoice(kybd);
					if(tempChoice > -1){
						tempGettingInput = false;
					}else{
						System.out.println("Invalid input.");
					}
				}
				
				
				break;
			}
			case 2:{
				System.out.println("Set Edge(s)");
				break;
			}
			case 3:{
				System.out.println("Add Node(s)");
				break;
			}
			case 4:{
				System.out.println("Find Directions");
				break;
			}
			case 5:{
				System.out.println("Exiting...");
				running = false;
				break;
			}
			default:
				System.out.println("Invalid Input. Enter one of the options given.");
				break;
			}//main switch
			pauseDaThing();
		}//main running loop
		
		
		if(useDataSrc){
			try {
				dataSource.saveDataToSource(navigator.mainGraph);
			} catch (BotLotDataSourceException e) {
				System.out.println("Unable to save data to source. Error: " + e.getMessage());
				e.printStackTrace();
				System.exit(1);
			}
		}

	}//main()

}//class simpleNavigator
