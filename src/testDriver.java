

import java.util.ArrayList;
import BotLot.BotLot;

public class testDriver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Begin test run");
		
		BotLot navigator = new BotLot();
		
		//create 4 nodes
		ArrayList<String> nodeIds = new ArrayList<String>();
		
		for(int i = 0; i < 4; i++){
			nodeIds.add(navigator.mainGraph.createNodeGiveId());
		}
		
		System.out.println("Nodes: \n");
		
		for(int i = 0; i < 4; i++){
			System.out.println("\t" + navigator.mainGraph.getNodes().get(i));
		}

	}

}
