/**
 * Starts the Gateway Server for Py4J
 * 		Src for code: 		https://www.py4j.org/getting_started.html
 * 		Main project page: 	https://www.py4j.org/index.html
 * 		Installing Py4J:	https://www.py4j.org/install.html
 * @author Greg
 *
 */
import py4j.GatewayServer;

public class StartServer {
	
	private BotLot navigator;
	
	public StartServer() {
		navigator = new BotLot();
	}
	
	public BotLot getNav() {
		return navigator;
	}

	public static void main(String[] args) {
		GatewayServer gatewayServer = new GatewayServer(new StartServer());
		gatewayServer.start();
		System.out.println("Gateway Server Started");
	}
	
}