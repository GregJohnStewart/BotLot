package botLot;

import java.util.ArrayList;
import botLot.lotGraph.LotNode;

//http://www.tutorialspoint.com/java/java_multithreading.htm
class BotLotPFHasPathThread implements Runnable {
	private Thread t;
	private String threadName;
	private ArrayList<LotNode> completed;
	private ArrayList<LotNode> connected;
	private ArrayList<LotNode> temporary;
	private LotNode ourNode;
	
	
	BotLotPFHasPathThread(String name, ArrayList<LotNode> compList, ArrayList<LotNode> connList, ArrayList<LotNode> tempList, LotNode ourNodeIn){
		this.threadName = name;
		this.completed = compList;
		this.connected = connList;
		this.temporary = tempList;
		this.ourNode = ourNodeIn;
	}
	
	@Override
	public void run() {
		
		//check if dest node is in this node's connected set
		if(nodesConnected.get(0).getConnectedNodes().contains(lotIn.getDestNode())){
			return true;
		}
		//put nodes connected to curNode into tempList
		for(LotNode curNode : nodesConnected.get(0).getConnectedNodes()){
			if(!nodesFinished.contains(curNode) && curNode != null){
				tempList.add(curNode);
			}
		}
		//put node in nodesFinished and remove it from nodesConnected
		nodesFinished.add(nodesConnected.remove(0));
	}
	
	public void setThreadName(String newName){
		this.threadName = newName;
	}
	
	//TODO:: finish this

}
