package BotLot.LotGraph;

import java.util.LinkedList;

public class LotPath {
	public LinkedList<LotEdge> path;
	
	public LotPath(LinkedList<LotEdge> pathIn){
		this();
		this.path = pathIn;
	}
	
	public LotPath(){
		this.path = new LinkedList<LotEdge>();
	}
	
	public long getPathMetric(){
		long metricSum = 0;
		for(int i = 0; i < path.size(); i++){
			metricSum += path.get(i).getMetric();
		}
		return metricSum;
	}
}
