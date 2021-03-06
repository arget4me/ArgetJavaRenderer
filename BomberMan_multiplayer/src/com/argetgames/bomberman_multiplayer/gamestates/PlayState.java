package com.argetgames.bomberman_multiplayer.gamestates;

import java.awt.Point;
import java.awt.event.KeyEvent;

import com.argetgames.arget2d.gamestates.GameState;
import com.argetgames.arget2d.gamestates.GameStateManager;
import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.arget2d.input.Keyboard;
import com.argetgames.bomberman_multiplayer.BombermanGame;
import com.argetgames.bomberman_multiplayer.Main;
import com.argetgames.bomberman_multiplayer.entities.Map;
import com.argetgames.bomberman_multiplayer.entities.NetworkPlayer;
import com.argetgames.bomberman_multiplayer.network.Client;
import com.argetgames.bomberman_multiplayer.network.Client;
import com.argetgames.bomberman_multiplayer.network.NetworkController;
import com.argetgames.bomberman_multiplayer.network.Server;

public class PlayState extends GameState {
	
	private Map map = new Map();
	private NetworkController controller;
	private NetworkPlayer player;
	private boolean resetButtonDown = false;
	
	public PlayState(GameStateManager gsm) {
		super(gsm);
		init();
	}
	
	void init() {
		map.init();
		Point spawn = map.getSpawnPoint();
		if(spawn != null){
			controller = new NetworkController();
			player = new NetworkPlayer(spawn.x, spawn.y, map.getTileWidth(), controller);
			map.addPlayer(player);
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
		controller.update();
		controller.parseInput(controller.getInputData());
		map.update();
	}

	@Override
	public void draw(Renderer2D renderer) {
		map.draw(renderer);
//		if(Main.startServer)
//			renderer.fillRect(0, 0, renderer.getWidth(), 5, 0xFFFFFFFF - (0x00555555)*server.numConnectedClients);//server.getcontrollers.
//		else{
//			int color = 0xFF0000FF;
//			if(client.state == Client.ClientState.CONNECTING)
//				color = 0xFFFF0000;
//			else if(client.state == Client.ClientState.CONNECTED)
//				color = 0xFF00FF00;
//			renderer.fillRect(0, 5, renderer.getWidth(), 5, color);
//			client.update();
//		}
	}

}
