package plattformrunner;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.argetgames.arget2d.game.Gameloop;
import com.argetgames.arget2d.graphics.Image2D;
import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.arget2d.input.Keyboard;
import com.argetgames.arget2d.input.Mouse;
import com.argetgames.arget2d.input.Mouse.MouseButton;

public class PlattformGame extends Gameloop {
	private RandomMap m = new RandomMap();
	private Image2D loadingImg;

	private DatagramSocket server;
	private int serverPort = 8192;
	private Thread handleClients, listen; // send
	private ArrayList<Client> clients = new ArrayList<Client>();
	private boolean startServer = false;
	private int nextID = 1;
	private boolean loadingDone = false;
	private InetAddress serverAddress = null;

	public PlattformGame(int width, int height, int scale, boolean startServer) {
		super(width, height, scale);
		loadingImg = new Image2D("res/images/loading.png");
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
			// handleClients.start();
		}
		
		
		listen = new Thread(() -> listen());
		if (startServer)
			loadingDone = true;
		listen.start();
		if(!startServer) {
			String address = JOptionPane.showInputDialog(null, "Input Server Address");
			try {
				if(InetAddress.getByName(address) == null) {
					System.exit(1);
				}
			} catch (UnknownHostException e) {
				System.exit(1);
				e.printStackTrace();
			}
			
			try {
				serverAddress = InetAddress.getByName(address);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
		
	}

	private void handleClients() {

	}

	private void listen() {
		byte[] data = new byte[1024];
		String msg;
		if (startServer) {
			if (debug_log)
				System.out.println("SERVER");
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
							} else if (msg.startsWith("CONNECT")) {
								String mapData = m.getMapData();
								DatagramPacket mapDataPacket = new DatagramPacket(mapData.getBytes(), mapData.getBytes().length, c.address, c.port);
								server.send(mapDataPacket);
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
		} else {
			if (debug_log)
				System.out.println("CLIENT");
			while (true) {
				try {
					DatagramPacket packet = new DatagramPacket(data, data.length);
					server.receive(packet);
					msg = new String(packet.getData(), 0, packet.getData().length);
					if (loadingDone) {
						if (msg.startsWith("RENDER")) {
							m.registerInput(msg);
						}
					}else {
						if(msg.startsWith("MAPDATA")) {
							m.registerMap(msg);
							loadingDone = true;
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	
	
	

	private void sendToServer() {
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					String msg;
					if (loadingDone) {
						msg = "CONTROLLERS";
						if (Keyboard.getKey(KeyEvent.VK_LEFT) || Keyboard.getKey(KeyEvent.VK_A))
							msg += ",1";
						else
							msg += ",0";

						if (Keyboard.getKey(KeyEvent.VK_RIGHT) || Keyboard.getKey(KeyEvent.VK_D))
							msg += ",1";
						else
							msg += ",0";

						if (Keyboard.getKey(KeyEvent.VK_UP) || Keyboard.getKey(KeyEvent.VK_W))
							msg += ",1,";
						else
							msg += ",0,";
					} else {
						msg = "CONNECT";
					}

					DatagramPacket p = new DatagramPacket(msg.getBytes(), msg.getBytes().length,
							serverAddress, serverPort);

					server.send(p);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		t.start();
	}

	private void sendToClients() {
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					String msg = "RENDER";
					msg += "," + m.mapScroll;
					msg += "," + (int) m.p.xPos + "," + (int) m.p.yPos;
					msg += "," + (int) m.p2.xPos + "," + (int) m.p2.yPos + ",";

					for (int i = 0; i < clients.size(); i++) {
						Client c = clients.get(i);
						DatagramPacket p = new DatagramPacket(msg.getBytes(), msg.getBytes().length, c.address, c.port);
						server.send(p);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		t.start();
	}

	@Override
	public void updateGame() {
		if (startServer) {
			m.update(clients);
			sendToClients();
		} else {
			sendToServer();
			if(Mouse.getMouse().isButtonClicked(MouseButton.MIDDLE))
				Main.frame = toggleFullscreen(Main.frame);
		}
		// send(data);
	}

	@Override
	public void draw() {
		if(!this.isFocusOwner())return;
		if (loadingDone) {
			renderer.useAlpha(true);
			m.draw(renderer);
		} else {
			renderer.useAlpha(true);
			renderer.renderImage2D(0, 0, globalWidth, globalHeight, loadingImg);
		}
	}

}
