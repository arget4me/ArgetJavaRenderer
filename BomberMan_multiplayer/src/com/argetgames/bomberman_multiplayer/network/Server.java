package com.argetgames.bomberman_multiplayer.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

public class Server {

	private static final int MAX_CLIENTS = 4;
	private volatile int numConnectedClients;
	private volatile boolean clientConnected[];
	private volatile Address clientAddresses[];
	private Thread listenThread;
	private DatagramSocket server;
	private int serverPort = 8192;
	private volatile boolean serverActive = false;
	private volatile ArrayList<DatagramPacket> sendQueue = new ArrayList<DatagramPacket>();
	
	public final static int CONNECTION_REQUEST_PACKET = 0xFF1111FF;
	public final static int CONNECTION_ACCEPTED_PACKET = 0xFF2222FF;
	public final static int CONNECTION_DENIED_PACKET = 0xFF2222FF;
	
	
	public Server() {
		clientConnected = new boolean[MAX_CLIENTS];
		clientAddresses = new Address[MAX_CLIENTS];
		try {
			server = new DatagramSocket(serverPort);
		} catch (SocketException e) {
			e.printStackTrace();
			return;
		}
		serverActive = true;
		listenThread = new Thread(() -> listen(), "Server Listening Thread");
		listenThread.start();
	}
	
	public void listen() {
		byte[] data = new byte[1024];
		while(serverActive){
			try {
				DatagramPacket packet = new DatagramPacket(data, data.length);
				server.receive(packet);
				int index = 0;
				int values[] = new int[1];
				index = Serialize.deserializeInteger(data, index, values);
				if(values[0] == CONNECTION_REQUEST_PACKET){
					int nextIndex = findExistingClientIndex(new Address(packet.getAddress(), packet.getPort()));
					if(nextIndex == -1){
						nextIndex = findFreeClientIndex();
						numConnectedClients++;
						clientConnected[nextIndex] = true;
						clientAddresses[nextIndex] = new Address(packet.getAddress(), packet.getPort());
					}
					if(nextIndex > -1){
						byte[] sendData = new byte[1024];
						int[] responseValue = {CONNECTION_ACCEPTED_PACKET};
						Serialize.serializeInteger(sendData, 0, responseValue);
						
						DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length);
						sendQueue.add(sendPacket);
					}
					
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public void sendToClients(){
		Thread send = new Thread(new Runnable(){
			public void run (){
				while(!sendQueue.isEmpty()){
					try {
						server.send(sendQueue.get(0));
						sendQueue.remove(0);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}, "Server Sending Thread");
		send.start();
	}
	
	public void update(){
		sendToClients();
	}
	
	public final int findFreeClientIndex(){
	    for ( int i = 0; i < MAX_CLIENTS; ++i )
	    {
	        if ( !clientConnected[i] )
	            return i;
	    }
	    return -1;
	}
	
	public final int findExistingClientIndex(Address address )
	{
	    for ( int i = 0; i < MAX_CLIENTS; ++i )
	    {
	        if ( clientConnected[i] && clientAddresses[i].equals(address) )
	            return i;
	    }
	    return -1;
	}
	
	public final boolean isClientConnected( int clientIndex )
	{
	    return clientConnected[clientIndex];
	}

}

class Address {
	
	public InetAddress ip;
	public int port;
	
	public Address(InetAddress ip, int port){
		this.ip = ip;
		this.port = port;
	}
	
	public boolean equals(Object obj){
		if(obj instanceof Address){
			if(((Address)obj).ip.equals(ip) && ((Address)obj).port == port){
				return true;
			}
		}
		return false;
	}
	
	
}