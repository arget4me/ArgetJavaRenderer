package com.argetgames.bomberman_multiplayer.gamestates;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.argetgames.arget2d.gamestates.GameState;
import com.argetgames.arget2d.gamestates.GameStateManager;
import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.arget2d.input.Keyboard;
import com.argetgames.bomberman_multiplayer.BombermanGame;
import com.argetgames.bomberman_multiplayer.entities.Map;
import com.argetgames.bomberman_multiplayer.entities.NetworkPlayer;
import com.argetgames.bomberman_multiplayer.network.Client;
import com.argetgames.bomberman_multiplayer.network.Server;

public class ServerPlayState extends GameState {
	
	private Map map = new Map();
	private Server server;
	private Client client;
	private boolean resetButtonDown = false;
	private boolean mapFull = false;
	
	public ServerPlayState(GameStateManager gsm) {
		super(gsm);
		server = new Server();
		server.start();
		client = new Client();
		client.controller.useKeyInput(KeyEvent.VK_W);
		client.controller.useKeyInput(KeyEvent.VK_A);
		client.controller.useKeyInput(KeyEvent.VK_S);
		client.controller.useKeyInput(KeyEvent.VK_D);
		client.controller.useKeyInput(KeyEvent.VK_E);
		try {
			client.start(InetAddress.getLocalHost());
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private void init(){		
		map.init();
		while(!mapFull && map.getNumPlayers() < server.numConnectedClients){
			Point spawn = map.getSpawnPoint();
			if(spawn != null){
				NetworkPlayer player = new NetworkPlayer(spawn.x, spawn.y, map.getTileWidth(), server.controllers[map.getNumPlayers()]);
				map.addPlayer(player);
			}else {
				mapFull = true;
			}
		}
	}
	
	@Override
	public void update() {
		if(Keyboard.getKey(KeyEvent.VK_ESCAPE)){
			switchState(BombermanGame.EDITOR_STATE);
			return;
		}
		if(Keyboard.getKey(KeyEvent.VK_CONTROL) && Keyboard.getKey(KeyEvent.VK_R)){
			if(!resetButtonDown){
				resetButtonDown = true;
				init();
			}
		}else {
			resetButtonDown = false;
		}
		client.update();
		server.update();
		map.update();
		server.addToSendQueue(map.getMapData());
	}

	@Override
	public void draw(Renderer2D renderer) {
		map.draw(renderer);
		server.draw(renderer);
	}

}
