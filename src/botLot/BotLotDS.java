package botLot;
//import java.sql.*;//for SQL connections
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
//import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
//import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import botLot.lotGraph.*;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
/**
 * Class BotLotDS "BotLot Data Source"
 * <p>
 * Handles the retrieval and saving of LotGraphs.
 * <p>
 * Supports: XML
 * <p>
 * Planned to support: MySQL, MSSql, POSTGRESQL.
 * <p>
 * Started: 10/26/15
 * 
 * @author Greg Stewart
 * @version	1.0 12/10/15
 */
public class BotLotDS {
	/** The kind of data source this is. Should be one of the constants. */
	private String type;
	/** The path or login information. */
	private String pathLogin;
	
	/*
	 * Globals for different data source types. These should be what needs to be put in for the database connection string.
	 */
	/** The specification for XML as a data source. */
	public final static String XML_DATASRC = "xml";
	/** The specification for a POSTGRES server as a data source. */
	public final static String POSTGRESQL_DATASRC = "postgresql";
	/** The specification for a Microsoft SQL database as a data source. */
	public final static String MSSQL_DATASRC = "microsoft:sqlserver";
	/** The specification for a MySQL database as a data source. */
	public final static String MYSQL_DATASRC = "mysql";

	/** The name of the url in the DB */
	public final static String DB_URL_NAME = "url";
	/** The name of the user in the DB */
	public final static String DB_USER_NAME = "user";
	/** The name of the pass in the DB */
	public final static String DB_PASS_NAME = "pass";
	/** A hashmap of the various names used in the database ops */
	public final static HashMap<String, String> DB_NAMES = getDatabaseNames();
	
	/**
	 * Constructor for the data source object.
	 * 
	 * @param dataSourceType	The data type. Use this class's *_DATASRC variables.
	 * @param pathLoginIn	Either the path to the XML file, or string XML data giving the data needed for connecting to a database (Use {@link #getServerConnectionXML(String, String, String)} to generate this.).
	 * @throws BotLotDSException If something went wrong, mostly if invalid data is entered.
	 */
	public BotLotDS(String dataSourceType, String pathLoginIn) throws BotLotDSException{
		dataSourceType = dataSourceType.toLowerCase();
		if(XML_DATASRC.equals(dataSourceType)){
			this.type = XML_DATASRC;
			this.pathLogin = pathLoginIn;
		}else if(isDatabaseSource(dataSourceType)){
			this.type = dataSourceType;
			if(this.isValidConnectionData(pathLoginIn)){
				this.pathLogin = pathLoginIn;
			}else{
				throw new BotLotDSException("XML String given is invalid.");
			}
		}else{
			throw new BotLotDSException("Invalid or unsupported data source type entered.");
		}
	}//BotLotDataSource(String, String)
	
	/**
	 * Empty constructor, for initialization when you don't want to specify things
	 */
	public BotLotDS(){
		
	}
	
	/**
	 * Creates an XML string that contains the information needed to setup a database connection.
	 * <p>
	 * Use this to give the constructor the pathLoginIn information.
	 * <p>
	 * Based on this tutorial: http://examples.javacodegeeks.com/core-java/xml/dom/create-dom-document-from-scratch/
	 * 
	 * @param serverURL	The URL of the server, including port number.
	 * @param userName	The username to use when connecting.
	 * @param password	The password of the user.
	 * @return	A string form of an XML document holding the information needed to login to a database.
	 * @throws BotLotException	If something went wrong.
	 */
	public static String getServerConnectionXML(String serverURL, String userName, String password) throws BotLotException{
		try{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dbf.newDocumentBuilder();
			Document doc = builder.newDocument();
			// create the root element node
			Element rootElement = doc.createElement("root");
			doc.appendChild(rootElement);
			//add the URL given to the XML
			Element itemElement = doc.createElement(DB_URL_NAME);
			itemElement.setAttribute("value", sanitizeStringData(serverURL));
			rootElement.appendChild(itemElement);
			//add the username given to the XML
			itemElement = doc.createElement(DB_USER_NAME);
			itemElement.setAttribute("value", sanitizeStringData(userName));
			rootElement.appendChild(itemElement);
			//add the password given to the XML
			itemElement = doc.createElement(DB_PASS_NAME);
			itemElement.setAttribute("value", sanitizeStringData(password));
			rootElement.appendChild(itemElement);
			//output to a string
			Transformer tf = TransformerFactory.newInstance().newTransformer();
			tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			tf.setOutputProperty(OutputKeys.INDENT, "yes");
			Writer out = new StringWriter();
			tf.transform(new DOMSource(doc), new StreamResult(out));
			return out.toString();
		}catch(Exception e){
			throw new BotLotException("Unable to create XML string- " + e.getMessage());
		}
	}//getSreverConnectionXML(String, String, String);
	
	/**
	 * Sanitizes strings for use in database connections.
	 * 
	 * @param stringIn	The string to sanitize.
	 * @return	The sanitized string.
	 */
	public static String sanitizeStringData(String stringIn){
		//TODO:: actually sanitize this
		return stringIn;
	}//sanitizeStringData(String)
	
	/**
	 * Gets the {@link #type}.
	 * @return	The type of the data source.
	 */
	public String getType(){
		return this.type;
	}
	
	/**
	 * Gets the {@link #pathLogin}
	 * @return	The pathLogin of the structure;
	 */
	public String getPathLogin(){
		return this.pathLogin;
	}
	
	/**
	 * Checks if the object is ready to do anything.
	 * 
	 * @return	If the object is ready to do anything.
	 */
	public boolean ready(){
		if(isDatabaseSource(this.type)){
			try {
				return this.dbConnects();
			} catch (BotLotDSException e) {
				return false;
			}
		}else{
			try{
				return this.hasValidXMLPath();
			}catch(BotLotDSException e){
				return false;
			}
		}
	}//ready()
	
	/**
	 * Gets the data from wherever this object is set up to get it.
	 * 
	 * @return	The LotGraph data retrieved from the source.
	 * @throws BotLotDSException	If something went wrong.
	 */
	public LotGraph getDataFromSource() throws BotLotDSException{
		if(!this.ready()){
			throw new BotLotDSException("Cannot get data; Source not ready.");
		}else if(isDatabaseSource(this.type)){
			switch(this.type){
				case MYSQL_DATASRC:
					return this.getDataFromMySQL();
					default:
						throw new BotLotDSException("Cannot get data; Unsupported database source.");
			}
		}else if(this.type.equals(XML_DATASRC)){
			return this.getDataFromXML();
		}else{
			throw new BotLotDSException("Cannot get data; Invalid type.");
		}
	}//getDataFromSource()
	
	/**
	 * Gets LotGraph data from an XML document.
	 * 
	 * @return	The LotGraph generated from this data.
	 * @throws BotLotDSException	If something goes wrong getting the graph data from the XML
	 */
	private LotGraph getDataFromXML() throws BotLotDSException{
		if(!this.hasValidXMLPath()){
			throw new BotLotDSException("XML file does not exist at the given path.");
		}
		System.out.println("Building factory...");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    factory.setValidating(true);
	    factory.setIgnoringElementContentWhitespace(true);
	    System.out.println("Done");
	    try {
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        File file = new File(this.pathLogin);
	        //builder.setErrorHandler();
	        Document doc = builder.parse(file);
	        // Do something with the document here.
	        
	        LotGraph graphOut = new LotGraph();
	        Element rootElement = doc.getDocumentElement();
	        
	        //get in all nodes
	        System.out.println("Getting Nodes...");
	        Element curNode = (Element)rootElement.getFirstChild();
	        while(curNode != null){
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
	}//getDataFromXML()
	
	/**
	 * Gets the LotGraph data from a MySQL database, specified in {@link #pathLogin}
	 * 
	 * @return The LotGraph data gotten from the database.
	 */
	private LotGraph getDataFromMySQL(){
		LotGraph lotOut = new LotGraph();
		//TODO:: do this
		return lotOut;
	}//getDataFromMySQL()
	
	/**
	 * Saves the LotGraph data to wherever this object is setup to save it to.
	 * 
	 * @param lotIn	The graph data to save.
	 * @throws BotLotDSException	If something went wrong.
	 */
	public void saveDataToSource(LotGraph lotIn) throws BotLotDSException{
		if(!this.ready()){
			//try to create file
			File testingFile = new File(this.pathLogin);
			try {
				testingFile.createNewFile();
			} catch (IOException e) {
				throw new BotLotDSException("Cannot create file.");
			}
			if(!this.ready()){
				throw new BotLotDSException("Cannot save data; Source not ready.");
			}
			this.saveDataToSource(lotIn);
		}else if(isDatabaseSource(this.type)){
			switch(this.type){
				case MYSQL_DATASRC:
					this.saveDataToMySQL(lotIn);
					break;
					default:
						throw new BotLotDSException("Cannot save data; Unsupported database source.");
			}
		}else if(this.type.equals(XML_DATASRC)){
			this.saveDataToXML(lotIn);
		}else{
			throw new BotLotDSException("Cannot get data; Invalid type.");
		}
	}//saveToDataSource(LotGraph)
	
	/**
	 * Saves the data given to an XML document at the location this object holds.
	 * 
	 * @param lotIn	The data to store in an XML document.
	 * @throws BotLotDSException	If something went wrong.
	 */
	private void saveDataToXML(LotGraph lotIn) throws BotLotDSException{
		try{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dbf.newDocumentBuilder();
			Document doc = builder.newDocument();
			//set comment detailing purpose of data
			doc.appendChild(doc.createComment("This data builds a LotGraph, to be used with the BotLotDS object. https://github.com/GregJohnStewart/BotLot "));
			// create the root element node
			Element rootElement = doc.createElement("root");
			doc.appendChild(rootElement);
			
			for(int i = 0; i < lotIn.getNumNodes(); i++){
				Element newNode = doc.createElement("node");
				LotNode curNode = lotIn.getNode(i);
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
				ArrayList<LotEdge> curEdgeList = lotIn.getNode(curNode).getEdges();
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
			//output to file
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			Result output = new StreamResult(new File(this.pathLogin));
			Source input = new DOMSource(doc);
			//TODO:: figure out why this breaks reading in XML:
			//transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			//transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			transformer.transform(input, output);
		}catch(Exception e){
			throw new BotLotDSException("Unable to create XML file- " + e.getClass() + " - " + e.getMessage());
		}
	}//saveDataToXML(LotGraph)
	
	/**
	 * Saves the graph data to a MySQL database. clears out the current data in the database in the process?
	 * 
	 * @param lotIn	The graph data to save.
	 */
	private void saveDataToMySQL(LotGraph lotIn){
		//TODO:: do this
	}//saveDataToMYSQL(LotGraph)
	
	/**
	 * Tests if the database will connect with the current setup of the data source.
	 * 
	 * @return	If we can connect to the data or not.
	 * @throws BotLotDSException	If the data source type is not a database.
	 */
	public boolean dbConnects() throws BotLotDSException{
		if(type.equals(MYSQL_DATASRC) || type.equals(MSSQL_DATASRC) || type.equals(POSTGRESQL_DATASRC)){
			//String connString = 
					this.buildDBConnectionString();
			//TODO:: finish this
			return false;
		}else{
			throw new BotLotDSException("Data Source type is not a database.");
		}
	}//dbConnects();
	
	/**
	 * Tests if the XML data (As a string) is valid and makes a valid connection string.
	 * 
	 * @param XMLStringIn	The XML data as a string.
	 * @return	If the data is valid or not.
	 */
	private boolean isValidConnectionData(String XMLStringIn){
		//TODO:: do this.
		return false;
	}//isValidConnectionData(String)
	
	/**
	 * Builds a valid connection string for use by the database connectors.
	 * 
	 * @return	A database connection string.
	 * @throws BotLotDSException	If the data source type is not database.
	 */
	private String buildDBConnectionString() throws BotLotDSException{
		if(isDatabaseSource(this.type)){
			//parse XML for variables
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		    factory.setValidating(true);
		    factory.setIgnoringElementContentWhitespace(true);
		    try {
		    	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = dbf.newDocumentBuilder();
				InputSource is = new InputSource(new StringReader(this.pathLogin));
				Document doc = builder.parse(is);
				//make sure XML has everything we need.
				Element rootElement = doc.getDocumentElement();
				if(!rootElement.hasChildNodes()){
					throw new BotLotDSException("No nodes found.");
				}
				NodeList elementList = rootElement.getChildNodes();
				int sum = 0;
				String url = null, 
						user = null, 
						pass = null;
				for(int i = 0; i < elementList.getLength(); i++){
					if(elementList.item(i).getLocalName().equals(DB_URL_NAME)){
						url = sanitizeStringData(((Element)elementList.item(i)).getAttribute("value"));
						sum++;
					}
					if(elementList.item(i).getLocalName().equals(DB_USER_NAME)){
						user = sanitizeStringData(((Element)elementList.item(i)).getAttribute("value"));
						sum++;
					}
					if(elementList.item(i).getLocalName().equals(DB_PASS_NAME)){
						pass = sanitizeStringData(((Element)elementList.item(i)).getAttribute("value"));
						sum++;
					}
				}
				if(sum != 3){
					throw new BotLotDSException("Not all nodes needed in XML are present.");
				}
				return "jdbc:"+this.type+"://"+url+";DatabaseName="+DB_NAMES.get("DB_NAME")+";user="+user+";Password="+pass+"";
		    }catch(Exception e ){
		    	throw new BotLotDSException("Unable to get needed information from XML string. Error: " + e.getClass() + " - " + e.getMessage());
		    }
		}else{
			throw new BotLotDSException("");
		}
	}//buildDBConnectionString()
	
	/**
	 * Tests if {@link #pathLogin} is a valid path to an XML file. 
	 * 
	 * @return	If the path to an XML document is valid.
	 * @throws BotLotDSException	If the type of datasource is not XML.
	 */
	public boolean hasValidXMLPath() throws BotLotDSException{
		if(XML_DATASRC.equals(this.type)){
			File testingFile = new File(this.pathLogin);
			if(testingFile.exists() && !testingFile.isDirectory() && testingFile.canWrite() && testingFile.canRead()){ 
			    return true;
			}
		}else{
			throw new BotLotDSException("The type of data source is not XML.");
		}
		return false;
	}//hasValidXMLPath()
	
	/**
	 * Determines if the string entered denotes a database data source.
	 * 
	 * @param dataSourceType	The data source to test against.
	 * @return	If the datasource type given is a database or not.
	 */
	public static boolean isDatabaseSource(String dataSourceType){
		if(MYSQL_DATASRC.equals(dataSourceType) 
				|| MSSQL_DATASRC.equals(dataSourceType) 
				|| POSTGRESQL_DATASRC.equals(dataSourceType)){
			return true;
		}else{
			return false;
		}
	}//isDatabaseSource(String)
	
	/**
	 * Gets the default important database name, table names, etc. for databases.
	 * 
	 * @return	A HashMap of names for database operations.
	 */
	public static HashMap<String, String> getDatabaseNames(){
		HashMap<String, String> output = new HashMap<String, String>();
		output.put("DB_NAME", "BOTLOT");
		output.put("NODE_TABLE", "NODES");
		output.put("NODE_FIELD_ID", "ID");
		output.put("NODE_FIELD_NUMEDGES", "NUM_EDGES");
		output.put("NODE_ATT_TABLE", "NODE_ATTS");
		output.put("EDGE_TABLE", "EDGES");
		output.put("EDGE_ATT_TABLE", "EDGE_ATTS");
		return output;
	}//getDatabaseNames()
}//class BotLotDataSource