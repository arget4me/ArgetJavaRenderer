package com.argetgames.treasurehunter;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import com.argetgames.arget2d.game.Gameloop;
import com.argetgames.arget2d.graphics.Animation2D;
import com.argetgames.arget2d.graphics.SpriteSheet;
import com.argetgames.treasurehunter.entities.Camera;
import com.argetgames.treasurehunter.entities.Enemy;
import com.argetgames.treasurehunter.entities.Player;
import com.argetgames.treasurehunter.entities.World;

@SuppressWarnings("serial")
public class TreasureHunterGame extends Gameloop{
	
	private static SpriteSheet playerSheet;
	public static Animation2D player_animation[] = new Animation2D[4];
	private Player player;
	private Enemy enemy;
	private World world;
	private Camera camera;
	public static int globalWidth ,globalHeight;
	
	private DatagramSocket clientSocket;
	private Thread net;
	
	public volatile static double netX = 0.0, netY = 0.0;
	
	
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
		
		for(int row = 0; row < player_animation.length; row++) {
			int[] order = {0,1,0,2};
			for(int i = 0; i < order.length; i++) {
				order[i] += row*6;
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
		net = new Thread(() -> listen());
	}
	
	private void listen() {
		byte[] data = new byte[1024];
		while(true) {
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				clientSocket.receive(packet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String msg = new String(packet.getData(), 0, packet.getLength());
			String [] parts = msg.split(":");
			int port = Integer.parseInt(parts[0]);
			netX = Double.parseDouble(parts[1]);
			netY = Double.parseDouble(parts[2]);
//			if(port == clientSocket.getLocalPort())
//				System.out.println("WRONG!!!_______________________________________________________________");
//			else
//				System.out.println(new String(packet.getData(), 0, packet.getLength()));
		}
	}

	String pos = "";
	@Override
	public void updateGame() {
		if(!net.isAlive())
			net.start();
		for(int i = 0; i < player_animation.length; i++) {
			player_animation[i].update();
		}
		enemy.update(player);
		player.update();
		try {
			pos = clientSocket.getLocalPort() + ":" + player.getX() + ":" + player.getY()+":";
			clientSocket.send(new DatagramPacket(pos.getBytes(), pos.getBytes().length, InetAddress.getByName("217.215.208.19"), 8192));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		camera.update();
		
	}

	
	@Override
	public void draw() {
		world.draw(renderer, camera);
		enemy.draw(renderer, camera);
		player.draw(renderer, camera);
	}

}
