/**
 * BotLot.java
 * 
 * Holds the graph object and handles big operations like shortest path algorithms
 * 
 * 
 */

package BotLot;

import BotLot.LotGraph.*;

public class BotLot{
	public LotGraph mainGraph;//the main graph
	private LotNode curNode;//the node we are currently at in the graph
	

    //=========================================================================
    //    Constructors
    //region Constructors
    //=========================================================================
    
	
	/**
	 * Empty constructor. Nulls the 
	 * @throws BotLotException Shouldn't do this.
	 */
	public BotLot() throws BotLotException{
		this.setGraph(new LotGraph());
		try{
			this.setCurNode(null);
		}catch(BotLotException err){
			System.out.println("FATAL ERROR- BotLot()- Unable to do empty constructor. Error: " + err.getMessage());
			System.exit(1);
		}
	}//BotLot()
	
	
	//endregion
	

    //=========================================================================
    //    Setters
    //region Setters
    //=========================================================================
	
	public void setGraph(LotGraph graphIn){
		sdfdsfsdfds
	}
	
	public void setCurNode(LotNode nodeIn) throws BotLotException{
		if(nodeIn != null && this.mainGraph.hasNode(nodeIn)){
			this.curNode = nodeIn;
		}else{
			if(nodeIn == null){
				this.curNode = null;
			}else{
				throw new BotLotException("Node not found withing ");
			}
		}
	}
	
	public void clearCurNode(){
		this.setCurNode(null);
	}
	
	public void addEdge(LotEdge edgeIn){
		asdasdasdasd
	}
	
	public void remEdge(LotEdge edgeIn){
		saDASDAS
	}
	
	public void remEdge(String egdeIdIn){
		xzcdasdfas
	}
	
	
	//endregion
	

    //=========================================================================
    //    Getters
    //region getters
    //=========================================================================
	
	public LotNode getCurNode(){
		
	}
	
	public ArrayList<LotEdge> getCurNodeEdges(){
		
	}
	
	
	
	//endregion
	
	
	
	
	
	
	
	
}//class BotLot
