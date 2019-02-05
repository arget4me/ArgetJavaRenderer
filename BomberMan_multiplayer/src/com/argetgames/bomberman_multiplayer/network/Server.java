package com.argetgames.bomberman_multiplayer.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.bomberman_multiplayer.entities.NetworkPlayer;

public class Server {

	private static final int MAX_CLIENTS = 4;
	public volatile int numConnectedClients;
	private volatile boolean clientConnected[];
	private volatile Address clientAddresses[];
	
	private volatile byte clientControllerData[][] = new byte[MAX_CLIENTS][];
	public NetworkController controllers[] = new NetworkController[MAX_CLIENTS];
	
	private Thread listenThread;
	private DatagramSocket server;
	private int serverPort = 8192;
	private volatile boolean serverActive = false;
	private volatile ArrayList<DatagramPacket> sendQueue = new ArrayList<DatagramPacket>();
	
	
	public final static int CONNECTION_REQUEST_PACKET = 0xFF1111FF;
	public final static int CONNECTION_ACCEPTED_PACKET = 0xFF2222FF;
	public final static int CONNECTION_DENIED_PACKET = 0xFF2222FF;
	public final static int GAME_DATA_PACKET = 0xFF3333FF;
	public final static int GAME_STATE_PACKET = 0xFF4444FF;


	
	
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
	
	public void start(){
		
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
				int clientIndex = findExistingClientIndex(new Address(packet.getAddress(), packet.getPort()));
				if(values[0] == CONNECTION_REQUEST_PACKET){
					if(clientIndex == -1){
						System.out.println("Client connected: " + packet.getAddress() +":"+ packet.getPort());
						clientIndex = findFreeClientIndex();
						numConnectedClients++;
						clientConnected[clientIndex] = true;
						clientAddresses[clientIndex] = new Address(packet.getAddress(), packet.getPort());
						controllers[clientIndex] = new NetworkController();
					}
					if(clientIndex > -1){
						byte[] sendData = new byte[1024];
						int[] responseValue = {CONNECTION_ACCEPTED_PACKET};
						Serialize.serializeInteger(sendData, 0, responseValue);
						DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddresses[clientIndex].ip, clientAddresses[clientIndex].port);
						sendQueue.add(sendPacket);
					}
					
				}else if(values[0] == GAME_DATA_PACKET){
					byte[] controllerData = new byte[data.length - index];
					for(int i = 0; i < controllerData.length; i++){
						controllerData[i] = data[index + i];
					}
					clientControllerData[clientIndex] = controllerData;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public void sendToClients(){
		if(serverActive){
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
	}
	
	public void addToSendQueue(byte[] gameData) {
		if(gameData != null && gameData.length <= 1024 - Integer.BYTES){
			byte[] sendData = new byte[1024];
			int[] messageHeader = {GAME_STATE_PACKET};
			int index = Serialize.serializeInteger(sendData, 0, messageHeader);
			for(int i = 0; i < gameData.length; i++){
				sendData[index + i] = gameData[i];
			}
			for(int i = 0; i < numConnectedClients; i++){				
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddresses[i].ip, clientAddresses[i].port);
				sendQueue.add(sendPacket);
			}
		}
	}
	
	public void update(){
		sendToClients();
		for(int i = 0; i < numConnectedClients; i++){
			if(isClientConnected(i) && clientControllerData[i] != null){
				controllers[i].parseInput(clientControllerData[i]);
			}
		}
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

	public void draw(Renderer2D renderer) {
		for(int i = 0; i < numConnectedClients; i++){
			renderer.fillRect(i*12, 0, 10, 10, 0xFF00FF00);
		}
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