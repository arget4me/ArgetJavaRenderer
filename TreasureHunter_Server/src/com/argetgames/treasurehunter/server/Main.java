package com.argetgames.treasurehunter.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;

public class Main {

	private static JFrame frame;
	private static boolean listening = false;

	public static void main(String[] args) {
		frame = new JFrame(
				"Game 2: TreasureHunter Server");/*
													 * JTextArea text = new JTextArea(); text.setEditable(false);
													 * DefaultCaret caret = (DefaultCaret)text.getCaret();
													 * caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE); JScrollPane
													 * js=new JScrollPane(text);
													 * 
													 * JTextArea dataArea = new JTextArea();
													 * dataArea.setEditable(false); DefaultCaret caret2 =
													 * (DefaultCaret)dataArea.getCaret();
													 * caret2.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE); JScrollPane
													 * dataScrollPane =new JScrollPane(dataArea);
													 * 
													 * frame.setSize(400, 200); frame.add(js);
													 * frame.add(dataScrollPane); frame.setResizable(false); BoxLayout
													 * layout = new BoxLayout(frame.getContentPane(), BoxLayout.X_AXIS);
													 * frame.setLayout(layout);
													 */
		Window w = new Window();
		frame.add(w);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		DatagramSocket server;
		ArrayList<Client> clients = new ArrayList<Client>();
		int nextID = 1;
		try {
			server = new DatagramSocket(8192);
			listening = true;
			byte[] data = new byte[1024];
			Thread handleClients = new Thread(new Runnable() {
				public void run() {
					long now = 0;
					while (listening) {
						boolean droppedClient = false;
						now = System.currentTimeMillis();
						for (int i = 0; i < clients.size(); i++) {
							Client c = clients.get(i);
							if (now - c.time >= 100) {
								if (now - c.time >= 1000) {
									// drop client
									w.connectionArea.append("Dropped client: " + c.address.getHostName() + " : " + c.port + "\n");
									clients.remove(i);
									i--;
									droppedClient = true;
								} else {
									DatagramPacket packet = new DatagramPacket("PING".getBytes(),
											"PING".getBytes().length, c.address, c.port);
									try {
										server.send(packet);
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							}
						}
						if(droppedClient) {
							DefaultListModel dlm = new DefaultListModel();
							for(int j = 0; j < clients.size(); j++) {
								Client cc = clients.get(j);
								dlm.addElement("Client " + cc.ID +": " + cc.address.getHostAddress() +" : " + cc.port);
							}
							w.clients.setModel(dlm);
						}
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			});
			handleClients.start();
			while (listening) {
				DatagramPacket packet = new DatagramPacket(data, data.length);
				server.receive(packet);
				String msg = new String(packet.getData(), 0, packet.getLength());
				Client temp = new Client(packet.getAddress(), packet.getPort(), System.currentTimeMillis());
				if (msg.startsWith("CONNECT")) {

					boolean contains = false;
					for (Client c : clients) {
						if (c.address.getHostAddress().equals(temp.address.getHostAddress()) && c.port == temp.port) {
							String response = "CONNECTED" + c.ID + "CONNECTED";
							packet = new DatagramPacket(response.getBytes(), response.getBytes().length, c.address,
									c.port);
							server.send(packet);
							contains = true;
						}
					}

					if (!contains) {
						temp.ID = nextID++;
						clients.add(temp);
						w.connectionArea.append("Client connected: " + temp.address.getHostAddress() + " : " + temp.port + "\n");
						DefaultListModel dlm = new DefaultListModel();
						for (int i = 0; i < clients.size(); i++) {
							Client c = clients.get(i);
							dlm.addElement("Client " + c.ID + ": " + c.address.getHostAddress() + " : " + c.port);
						}
						w.clients.setModel(dlm);
					}
				} else {
					if (msg.startsWith("PING")) {
						for (Client c : clients) {
							if (c.address.getHostAddress().equals(temp.address.getHostAddress())
									&& c.port == temp.port) {
								c.time = System.currentTimeMillis();
								break;
							}
						}
					} else {
						w.dataArea.append(msg + "\n");
						// String [] parts = msg.split(":");

						for (Client c : clients) {
							if (!(c.address.getHostAddress().equals(temp.address.getHostAddress())
									&& c.port == temp.port)) {
								packet = new DatagramPacket(msg.getBytes(), msg.getBytes().length, c.address, c.port);
								server.send(packet);
							}
						}
					}
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
	public long time;
	public int ID = 0;

	public Client(InetAddress address, int port, long time) {
		this.address = address;
		this.port = port;
		this.time = time;
	}
}
