package botLot.dataSource;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import botLot.BotLot;
import botLot.BotLotException;
import botLot.lotGraph.LotEdge;
import botLot.lotGraph.LotGraph;
import botLot.lotGraph.LotGraphException;
import botLot.lotGraph.LotNode;
import botLot.lotGraph.LotPath;
import botLot.lotGraph.LotPathException;

/**
 * Class to save BotLot/LotGraph data to XML.
 * <p>
 * Started: 5/4/16
 * 
 * @author Greg
 * @version	1.0 5/4/16
 */

public class BotLotXMLDS extends BotLotDataSource implements BotLotDataSourceInterface {
	
	/** Where the XML file to store/retrieve data from is. */
	private String dataFileLoc = "";
	
	/**
	 * Constructor to take in a XML file location.
	 * @param fileLocationIn	The file location to set.
	 */
	public BotLotXMLDS(String fileLocationIn){
		this();
		this.setFileLocation(fileLocationIn);
	}
	
	/**
	 * Empty constructor.
	 */
	public BotLotXMLDS() {
		super();
	}

	@Override
	public boolean ready() {
		return checkFileLocation(this.getFileLocation(), true);
	}
	
	/**
	 * Determines if the object is ready to save or not.
	 * @return	If the object is ready to save or not.
	 */
	public boolean readyToSave(){
		return checkFileLocation(this.getFileLocation(), false);
	}

	@Override
	public LotGraph getGraph() throws BotLotDSException {
		if(!this.ready()){
			throw new BotLotDSException("XML Data source not ready to get data.");
		}
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(true);
		factory.setIgnoringElementContentWhitespace(true);
		System.out.println("Done");
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			File file = new File(this.getFileLocation());
			//builder.setErrorHandler();
			Document doc = builder.parse(file);
			// Do something with the document here.
			
			LotGraph graphOut = new LotGraph();
			Element rootElement = doc.getDocumentElement();
			
			//get in all nodes
			System.out.println("Getting Nodes...");
			Element curNode = (Element)rootElement.getFirstChild();
			while(curNode != null){
				if(curNode.getNodeName() != "node"){
					curNode = (Element)curNode.getNextSibling();
					continue;
				}
				
				LotNode newNode = new LotNode();
				//get ID
				newNode.setId(curNode.getAttribute("id"));
				//get numEdges
				newNode.setActNumEdges(Integer.parseInt(curNode.getAttribute("numEdges")));
				//get rest of attributes
				NamedNodeMap attList = curNode.getAttributes();
				for(int i = 0; i < attList.getLength(); i++){
					Attr attr = (Attr) attList.item(i);
					String attrName = attr.getNodeName();
					if(!attrName.equals("id") && !attrName.equals("numEdges")){
						newNode.setAtt(attrName, attr.getNodeValue());
					}
				}
				graphOut.addNode(newNode);
				System.out.println("\tGot Node: " + newNode.toString());
				curNode = (Element) curNode.getNextSibling();
			}
			//get in all edges
			System.out.println("Getting Edges...");
			curNode = (Element)rootElement.getFirstChild();
			while(curNode != null){
				if(curNode.getNodeName() != "node"){
					curNode = (Element)curNode.getNextSibling();
					continue;
				}
				NodeList edges = curNode.getChildNodes();
				for(int i = 0; i < edges.getLength(); i++){
					Element curEdge = (Element)edges.item(i);
					LotEdge newEdge = new LotEdge();
					//get ID
					newEdge.setId(curEdge.getAttribute("id"));
					//get numEdges
					newEdge.setMetric(Double.parseDouble(curEdge.getAttribute("metric")));
					//get end node
					newEdge.setEndNode(graphOut.getNode(curEdge.getAttribute("endNode")));
					//get rest of attributes
					NamedNodeMap attList = curEdge.getAttributes();
					for(int j = 0; j < attList.getLength(); j++){
						Attr attr = (Attr) attList.item(j);
						String attrName = attr.getNodeName();
						if(!attrName.equals("id") && !attrName.equals("metric") && !attrName.equals("endNode")){
							newEdge.setAtt(attrName, attr.getNodeValue());
						}
					}
					System.out.println("\tGot Edge: " + newEdge.toString());
					graphOut.setEdge(newEdge, curNode.getAttribute("id"));
				}
				System.out.println("Got all edges for " + curNode.getAttribute("id"));
				curNode = (Element)curNode.getNextSibling();
			}
			return graphOut;
		} catch (SAXException e) {
			throw new BotLotDSException("" + e.getMessage());
		} catch (IOException e) {
			throw new BotLotDSException("Unable to open XML file (presumably). Error: " + e.getMessage());
		}catch(LotGraphException e){
			throw new BotLotDSException("Unable to process graph data retrieved from XML data. Inner error: " + e.getMessage());
		} catch (ParserConfigurationException e) {
			throw new BotLotDSException("Unable to parse XML. Error: " + e.getMessage());
		}
	}//getGraph()

	@Override
	public BotLot getBotLot() throws BotLotDSException {
		BotLot lotOut = new BotLot(this.getGraph());
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(true);
		factory.setIgnoringElementContentWhitespace(true);
		try{
			DocumentBuilder builder = factory.newDocumentBuilder();
			File file = new File(this.getFileLocation());
			//builder.setErrorHandler();
			Document doc = builder.parse(file);
			Element rootElement = doc.getDocumentElement();
			
			Element curNode = (Element)rootElement.getFirstChild();
			while(curNode != null){
				if(curNode.getNodeName() != "curNode"){
					curNode = (Element)curNode.getNextSibling();
					continue;
				}
				lotOut.setCurNode(curNode.getAttribute("id"));
			}//get curNode
			
			curNode = (Element)rootElement.getFirstChild();
			while(curNode != null){
				if(curNode.getNodeName() != "destNode"){
					curNode = (Element)curNode.getNextSibling();
					continue;
				}
				lotOut.setDestNode(curNode.getAttribute("id"));
			}//get destNode
			
			if(lotOut.ready(false)){
				curNode = (Element)rootElement.getFirstChild();
				while(curNode != null){
					if(curNode.getNodeName() != "path"){
						curNode = (Element)curNode.getNextSibling();
						continue;
					}
					LotPath pathIn = new LotPath();
					
					NodeList edges = curNode.getChildNodes();
					for(int i = 0; i < edges.getLength(); i++){
						Element curEdge = (Element)edges.item(i);
						pathIn.append(lotOut.mainGraph.getEdge(curEdge.getAttribute("id")));
					}
					lotOut.setCurPath(pathIn);
				}//get path
			}//if got enought for a path to exist
			
		} catch (SAXException e) {
			throw new BotLotDSException("" + e.getMessage());
		} catch (IOException e) {
			throw new BotLotDSException("Unable to open XML file (presumably). Error: " + e.getMessage());
			//}catch(LotGraphException e){
			//throw new BotLotDSException("Unable to process graph data retrieved from XML data. Inner error: " + e.getMessage());
		} catch (ParserConfigurationException e) {
			throw new BotLotDSException("Unable to parse XML. Error: " + e.getMessage());
		} catch (BotLotException e) {
			throw new BotLotDSException("Unable to process BotLot data retrieved from XML data. Inner error: " + e.getMessage());
		} catch (LotPathException e) {
			throw new BotLotDSException("Unable to process BotLot data retrieved from XML data. Could not build path from data. Inner error: " + e.getMessage());
		}
		return lotOut;
	}//getBotLot()

	@Override
	public void saveGraph(LotGraph graphIn) throws BotLotDSException {
		this.saveBotLot(new BotLot(graphIn));
	}//saveGraph(LotGraph)

	@Override
	public void saveBotLot(BotLot lotIn) throws BotLotDSException {
		if(!this.readyToSave()){
			throw new BotLotDSException("XML data source not ready to save.");
		}
		LotGraph graphToSave = lotIn.mainGraph;
		try{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dbf.newDocumentBuilder();
			Document doc = builder.newDocument();
			//set comment detailing purpose of data
			doc.appendChild(doc.createComment("This data builds a LotGraph, to be used with the BotLotDS object. https://github.com/GregJohnStewart/BotLot "));
			// create the root element node
			Element rootElement = doc.createElement("root");
			doc.appendChild(rootElement);
			
			for(int i = 0; i < graphToSave.getNumNodes(); i++){
				Element newNode = doc.createElement("node");
				LotNode curNode = graphToSave.getNode(i);
				//add id, numEdges
				newNode.setAttribute("id", curNode.getId());
				newNode.setAttribute("numEdges", "" + curNode.getNumEdges());
				//add other attributes
				Iterator<Entry<String, String>> nodeAttIterator = curNode.getAtts().entrySet().iterator();
				while (nodeAttIterator.hasNext()) {
					Map.Entry<String, String> pair = (Map.Entry<String, String>)nodeAttIterator.next();
					newNode.setAttribute((String)pair.getKey(), (String)pair.getValue());
					//it.remove(); // avoids a ConcurrentModificationException
				}
				rootElement.appendChild(newNode);
				//add edges
				ArrayList<LotEdge> curEdgeList = graphToSave.getNode(curNode).getEdges();
				for(int j = 0; j < curEdgeList.size(); j++){
					Element newEdge = doc.createElement("edge");
					LotEdge curEdge = curEdgeList.get(j);
					//add id, metric, end node id
					newEdge.setAttribute("id", curEdge.getId());
					newEdge.setAttribute("metric", "" + curEdge.getMetric());
					newEdge.setAttribute("endNode", curEdge.getEndNode().getId());
					
					//add other attributes
					Iterator<Entry<String, String>> edgeAttIterator = curEdge.getAtts().entrySet().iterator();
					while (edgeAttIterator.hasNext()) {
						Map.Entry<String, String> pair = (Map.Entry<String, String>)edgeAttIterator.next();
						newNode.setAttribute((String)pair.getKey(), (String)pair.getValue());
						//it.remove(); // avoids a ConcurrentModificationException
					}
					newNode.appendChild(newEdge);
				}//for each edge on that node
			}//for each node in graph
			
			if(lotIn.hasCurNode()){
				Element newNode = doc.createElement("curNode");
				newNode.setAttribute("id", lotIn.getCurNode().getId());
				rootElement.appendChild(newNode);
			}
			
			if(lotIn.hasDestNode()){
				Element newNode = doc.createElement("destNode");
				newNode.setAttribute("id", lotIn.getDestNode().getId());
				rootElement.appendChild(newNode);
			}
			
			if(lotIn.hasPath()){
				Element path = doc.createElement("destNode");
				if(lotIn.getCurPath().infSizeFlag){
					path.setAttribute("infSizeFlag", "true");
				}else{
					path.setAttribute("infSizeFlag", "false");
				}
				rootElement.appendChild(path);
				
				for(LotEdge curEdge : lotIn.getCurPath().path){
					Element newEdge = doc.createElement("pathStep");
					newEdge.setAttribute("id", curEdge.getId());
					path.appendChild(newEdge);
				}
			}
			
			//output to file
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			Result output = new StreamResult(new File(this.getFileLocation()));
			Source input = new DOMSource(doc);
			//TODO:: figure out why this breaks reading in XML:
			//transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			//transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			transformer.transform(input, output);
		}catch(Exception e){
			throw new BotLotDSException("Unable to create XML file- " + e.getClass() + " - " + e.getMessage());
		}
	}//saveBotLot(BotLot)

	@Override
	public boolean sourceHasData() {
		try {
			if(this.getGraph() != new LotGraph()){
				return true;
			}
		} catch (BotLotDSException e) {
			
		}
		return false;
	}//sourceHasData
	
	/**
	 * Sets the location of the XML file to set. 
	 * @param fileLocationIn	The new file location.
	 */
	public void setFileLocation(String fileLocationIn){
		this.dataFileLoc = fileLocationIn;
	}
	
	/**
	 * Gets the XML file location set.
	 * @return	The XML file location set.
	 */
	public String getFileLocation(){
		return this.dataFileLoc;
	}
	
	/**
	 * Checks if the given file location is valid.
	 * @param fileLocationIn	The file location to check.
	 * @param exists	Flag to check if the file exists or not. Use 'False' when intending to write out to new file.
	 * @return	If the given file location is valid.
	 */
	public static boolean checkFileLocation(String fileLocationIn, boolean exists){
		File f = new File(fileLocationIn);
		if(exists){
			if(f.exists() && !f.isDirectory()) { 
				return true;
			}
		}else{
			f = new File(f.getParent());
			if(f.exists() && f.isDirectory()) { 
				return true;
			}
		}
		return false;
	}//checkFileLocation(String, boolean)

}//class BotLotXMLDS extends BotLotDataSource implements BotLotDataSourceInterface
