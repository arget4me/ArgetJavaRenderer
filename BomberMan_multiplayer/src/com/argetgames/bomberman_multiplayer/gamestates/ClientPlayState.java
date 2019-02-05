package com.argetgames.bomberman_multiplayer.gamestates;

import java.awt.event.KeyEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import com.argetgames.arget2d.gamestates.GameState;
import com.argetgames.arget2d.gamestates.GameStateManager;
import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.bomberman_multiplayer.Main;
import com.argetgames.bomberman_multiplayer.entities.Map;
import com.argetgames.bomberman_multiplayer.network.Client;

public class ClientPlayState extends GameState {

	private Client client;
	private Map map = new Map();
	
	public ClientPlayState(GameStateManager gsm) {
		super(gsm);
		init();
		client = new Client();
		client.controller.useKeyInput(KeyEvent.VK_W);
		client.controller.useKeyInput(KeyEvent.VK_A);
		client.controller.useKeyInput(KeyEvent.VK_S);
		client.controller.useKeyInput(KeyEvent.VK_D);
		client.controller.useKeyInput(KeyEvent.VK_E);
		InetAddress serverAddress = null;
		while(true){
			String address = JOptionPane.showInputDialog(Main.frame, "Input Server Address");
			try {
				if(InetAddress.getByName(address) != null){
					serverAddress = InetAddress.getByName(address);
					break;
				}
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
		client.start(serverAddress);
	}
	
	private void init() {
		
	}

	
	public void update() {
		client.update();
		if(client.statusData != null)
			map.parseMapData(client.statusData);
		map.update();
	}


	public void draw(Renderer2D renderer) {
		map.draw(renderer);
	}
	
	
	
	

}
