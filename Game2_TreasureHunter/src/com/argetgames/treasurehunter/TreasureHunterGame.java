package com.argetgames.treasurehunter;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.argetgames.arget2d.game.Gameloop;
import com.argetgames.arget2d.graphics.Animation2D;
import com.argetgames.arget2d.graphics.SpriteSheet;
import com.argetgames.treasurehunter.entities.Camera;
import com.argetgames.treasurehunter.entities.Enemy;
import com.argetgames.treasurehunter.entities.NetPlayer;
import com.argetgames.treasurehunter.entities.Player;
import com.argetgames.treasurehunter.entities.World;

@SuppressWarnings("serial")
public class TreasureHunterGame extends Gameloop {

	private static SpriteSheet playerSheet;
	public static Animation2D player_animation[] = new Animation2D[4];
	private Player player;
	private volatile ArrayList<NetPlayer> netPlayers = new ArrayList<NetPlayer>();
	private Enemy enemy;
	private World world;
	private Camera camera;
	public static int globalWidth, globalHeight;

	private volatile boolean connected = false;
	private volatile long time;
	private DatagramSocket clientSocket;
	private Thread net, con;
	private int ID = -1;

	public TreasureHunterGame(int width, int height, int scale) {
		super(width, height, scale, true);
		globalWidth = WIDTH;
		globalHeight = HEIGHT;
		init();
	}

	private void init() {
		debug_log = false;
		renderer.useAlpha(true);
		playerSheet = new SpriteSheet("res/images/playerSpritesheet.png", 32, 32);

		for (int row = 0; row < player_animation.length; row++) {
			int[] order = { 0, 1, 0, 2 };
			for (int i = 0; i < order.length; i++) {
				order[i] += row * 6;
				player_animation[row] = new Animation2D(playerSheet, order, 12);
				player_animation[row].play(true);
			}
		}

		world = new World();
		player = new Player(0, 0);
		enemy = new Enemy(0, 0);
		camera = new Camera(player);
		try {
			clientSocket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		con = new Thread(() -> connect());
		net = new Thread(() -> listen());
	}
	
	private void connect() {
		String msg = "CONNECT";
		while (!connected) {
			try {
				clientSocket.send(new DatagramPacket(msg.getBytes(), msg.getBytes().length,
						InetAddress.getByName("217.215.208.19"), 8192));
				Thread.sleep(200);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

	private void listen() {
		byte[] data = new byte[1024];

		if(!con.isAlive())
			con.start();
		
		time = System.currentTimeMillis();		
		while (true) {
			DatagramPacket packet = new DatagramPacket(data, data.length);
			time = System.currentTimeMillis();
			try {
				clientSocket.receive(packet);
				String msg = new String(packet.getData(), 0, packet.getLength());
				if (!connected) {
					if(msg.startsWith("CONNECTED")) {
						ID = Integer.parseInt(msg.split("CONNECTED")[1]);
						connected = true;
					}
				} else {
					if(msg.startsWith("PING")) {
						clientSocket.send(new DatagramPacket(msg.getBytes(), msg.getBytes().length, packet.getAddress(),
								packet.getPort()));
					}else if(!msg.startsWith("CONNECTED")){
						String[] parts = msg.split(":");
						int senderID = Integer.parseInt(parts[0]);
						boolean contains = false;
						for(NetPlayer np : netPlayers) {
							if(np.ID == senderID) {
								double netX = Double.parseDouble(parts[1]);
								double netY = Double.parseDouble(parts[2]);
								np.newInput(netX, netY);
								contains = true;
								break;
							}
						}
						
						if(!contains) {
							double nx = Double.parseDouble(parts[1]);
							double ny = Double.parseDouble(parts[2]);
							netPlayers.add(new NetPlayer(senderID, nx, ny));
						}
						
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}

	String pos = "";

	@Override
	public void updateGame() {
		if(System.currentTimeMillis() - time >= 1000) {
			connected = false;
			con = new Thread(() -> connect());
			con.start();
		}
		if (!net.isAlive())
			net.start();
		for (int i = 0; i < player_animation.length; i++) {
			player_animation[i].update();
		}
		//enemy.update(player);
		for(int i = 0; i < netPlayers.size(); i++) {
			netPlayers.get(i).update();
			if(netPlayers.get(i).drop) {
				netPlayers.remove(i);
				i--;
			}
		}
		player.update();
		if (connected) {
			try {
				pos = ID + ":" + player.getX() + ":" + player.getY() + ":";
				clientSocket.send(new DatagramPacket(pos.getBytes(), pos.getBytes().length,
						InetAddress.getByName("217.215.208.19"), 8192));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		camera.update();

	}

	@Override
	public void draw() {
		world.draw(renderer, camera);
		//enemy.draw(renderer, camera);
		for(int i = 0; i < netPlayers.size(); i++) {
			netPlayers.get(i).draw(renderer, camera);
		}
		player.draw(renderer, camera);
	}

}
