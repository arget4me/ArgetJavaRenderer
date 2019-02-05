package com.argetgames.bomberman_multiplayer.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.argetgames.bomberman_multiplayer.Main;

public class Client {

	private InetAddress serverAddress;
	private int serverPort;
	private DatagramSocket clientSocket;
	private Thread listeningThread;
	private int connectionAttemps = 0;
	
	public enum ClientState {
		DISCONNECTED, CONNECTING, CONNECTED
	};
	
	public volatile ClientState state;
	private boolean clientActive = false;
	private volatile ArrayList<byte[]> sendQueue = new ArrayList<byte[]>();
	public volatile byte[] statusData = null;
	public NetworkController controller = new NetworkController();
	
	public Client() {
		try {
			clientSocket = new DatagramSocket();
			clientActive = true;
			state = ClientState.DISCONNECTED;
			listeningThread = new Thread(() -> listen(), "Client Listening Thread");
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public void start(InetAddress address){
		serverAddress = address;
		serverPort = 8192;
		state = ClientState.CONNECTING;
		clientActive = true;
		listeningThread.start();
	}
	
	private void listen(){
		byte[] data = new byte[1024];
		while(clientActive){
			try {
				DatagramPacket packet = new DatagramPacket(data, data.length);
				clientSocket.receive(packet);
				
				int index = 0;
				int values[] = new int[1];
				index = Serialize.deserializeInteger(data, index, values);
				int packetHeader = values[0];
				switch(state){
				case DISCONNECTED:
				{
				}break;
				
				case CONNECTING:
				{
					if(packetHeader == Server.CONNECTION_ACCEPTED_PACKET){
						state = ClientState.CONNECTED;
					}
				}break;
				
				case CONNECTED:
				{
					if(packetHeader == Server.GAME_STATE_PACKET){
						byte[] gateStateData = new byte[data.length - index];
						for(int i = 0; i < gateStateData.length; i++){
							gateStateData[i] = data[index + i];
						}
						statusData = gateStateData;
					}
				}break;
				}
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public void sendToServer(){
		if(clientActive){
			Thread send = new Thread(new Runnable(){
				public void run (){
					while(!sendQueue.isEmpty()){
						try {
							byte[] sendData = sendQueue.get(0);
							sendQueue.remove(0);
							DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);
							clientSocket.send(sendPacket);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}, "Server Sending Thread");
			send.start();
		}
	}
	
	public void update(){
		switch(state){
		case DISCONNECTED:
		{
			clientActive = false;
		}break;
		
		case CONNECTING:
		{
			byte[] data = new byte[1024];
			int values[] = {Server.CONNECTION_REQUEST_PACKET};
			Serialize.serializeInteger(data, 0, values);
			sendQueue.add(data);
		}break;

		case CONNECTED:
		{
			controller.update();
			byte[] data = new byte[1024];
			byte[] controllerData = controller.getInputData();
			int values[] = {Server.GAME_DATA_PACKET};
			int index = Serialize.serializeInteger(data, 0, values);
			for(int i = 0; i < controllerData.length && (index + i) < data.length; i++){
				data[index + i] = controllerData[i];
			}
			sendQueue.add(data);
		}break;
		}
		sendToServer();
	}
	
	
	
	
	
	
	
	
	

}
