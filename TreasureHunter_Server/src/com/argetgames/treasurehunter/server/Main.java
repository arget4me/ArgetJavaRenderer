package com.argetgames.treasurehunter.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

public class Main {
	
	private static JFrame frame;
	private static boolean listening = false;
	
	public static void main(String[] args) {
		frame = new JFrame("Game 2: TreasureHunter Server");
		JTextArea text = new JTextArea();
		text.setEditable(false);
		DefaultCaret caret = (DefaultCaret)text.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		JScrollPane js=new JScrollPane(text);
		frame.setSize(400, 200);
		frame.add(js);
		frame.setResizable(false);
		//frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		DatagramSocket server;
		ArrayList<Client> clients = new ArrayList<Client>();
		try {
			server = new DatagramSocket(8192);
			listening = true;
			byte[] data = new byte[1024];
			while(listening) {
				DatagramPacket packet = new DatagramPacket(data, data.length);
				server.receive(packet);
				String msg = new String(packet.getData(), 0, packet.getLength());
//				text.append(msg + "\n");
//				String [] parts = msg.split(":");
				Client temp = new Client(packet.getAddress(), packet.getPort());
				
				boolean contains = false;
				for(Client c : clients) {
					if(c.address.getHostAddress().equals(temp.address.getHostAddress()) && c.port == temp.port) {
						contains = true;					
					}else {
						packet = new DatagramPacket(msg.getBytes(), msg.getBytes().length, c.address, c.port);
						server.send(packet);
					}
				}
				
				if(!contains) {
					clients.add(temp);
					text.append(temp.address.getHostAddress() +  " : " + temp.port+"\n");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

}

class Client {
	public int port;
	public InetAddress address;
	
	public Client(InetAddress address, int port) {
		this.address = address;
		this.port = port;
	}
}


