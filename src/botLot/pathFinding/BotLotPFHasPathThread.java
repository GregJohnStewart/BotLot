package botLot.pathFinding;

import java.util.ArrayList;
import botLot.lotGraph.LotNode;

/**
 * Class to do the multi-threadable part of the path existence finding algorithm in class BotLotPF.
 * 
 * Started: 2/1/15
 * 
 * @author Greg Stewart
 * @version	1.0 2/1/16
 */
class BotLotPFHasPathThread extends Thread {
	private Thread t;
	private String threadName;
	private ArrayList<LotNode> finished;
	private ArrayList<LotNode> connected;
	private ArrayList<LotNode> temporary;
	
	
	BotLotPFHasPathThread(String name, ArrayList<LotNode> finList, ArrayList<LotNode> connList, ArrayList<LotNode> tempList){
		this.threadName = name;
		this.finished = finList;
		this.connected = connList;
		this.temporary = tempList;
	}
	
	@Override
	public void run() {
		//put nodes connected to curNode into tempList
		for(LotNode curNode : this.connected){
			if(!finished.contains(curNode) && curNode != null){
				temporary.add(curNode);
			}
		}
	}
	
	public void start (){
		if (t == null){
			t = new Thread (this, this.threadName);
			t.start();
		}
	}
	
	public void setThreadName(String newName){
		this.threadName = newName;
	}
	
	public String getThreadName(){
		return this.threadName;
	}
}
