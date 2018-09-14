package plattformrunner;

import java.net.InetAddress;

public class Client{

	public int ID = 0;
	public InetAddress address;
	public int port;
	public NetworkController controller;
	
	public Client(InetAddress address, int port){
		this.address = address;
		this.port = port;
		controller = new NetworkController();
	}
}
