package plattformrunner;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.argetgames.arget2d.game.Gameloop;
import com.argetgames.arget2d.input.Keyboard;

public class PlattformGame extends Gameloop {
	public static int globalWidth, globalHeight;
	private RandomMap m = new RandomMap();

	private DatagramSocket server;
	private int serverPort = 30303;
	private Thread handleClients, listen; // send
	private ArrayList<Client> clients = new ArrayList<Client>();
	private boolean startServer = false;
	private int nextID = 1;

	public PlattformGame(int width, int height, int scale, boolean startServer) {
		super(width, height, scale, false);
		globalWidth = WIDTH;
		globalHeight = HEIGHT;
		this.startServer = startServer;
		debug_log = false;
		try {
			if (startServer)
				server = new DatagramSocket(serverPort);
			else
				server = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(1);
		}

		if (startServer) {
			handleClients = new Thread(() -> handleClients());
			listen = new Thread(() -> listen());

			// handleClients.start();
			listen.start();
		}
	}

	private void handleClients() {

	}

	private void listen() {
		byte[] data = new byte[1024];
		String msg;
		while (true) {
			try {
				DatagramPacket packet = new DatagramPacket(data, data.length);
				server.receive(packet);
				msg = new String(packet.getData(), 0, packet.getData().length);

				Client temp = new Client(packet.getAddress(), packet.getPort());

				boolean contains = false;
				for (Client c : clients) {
					if (c.address.getHostAddress().equals(temp.address.getHostAddress()) && c.port == temp.port) {
						contains = true;
						if (msg.startsWith("CONTROLLERS")) {
							c.controller.registerInput(msg);
						}
						break;
					}
				}

				if (!contains) {
					temp.ID = nextID++;
					clients.add(temp);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void sendToServer(){
		try {
			String msg = "CONTROLLERS";
			if (Keyboard.getKey(KeyEvent.VK_LEFT))
				msg += ",1";
			else
				msg += ",0";

			if (Keyboard.getKey(KeyEvent.VK_RIGHT))
				msg += ",1";
			else
				msg += ",0";

			if (Keyboard.getKey(KeyEvent.VK_UP))
				msg += ",1,";
			else
				msg += ",0,";

			DatagramPacket p = new DatagramPacket(msg.getBytes(), msg.getBytes().length, InetAddress.getLocalHost(),
					serverPort);

			server.send(p);
		} catch (IOException e) {
			e.printStackTrace();
		}
			
	}

	@Override
	public void updateGame() {
		if(startServer){
			m.update(clients);
		}else{
			sendToServer();
		}
		// send(data);
	}

	@Override
	public void draw() {
		renderer.useAlpha(true);
		m.draw(renderer);
	}

}
