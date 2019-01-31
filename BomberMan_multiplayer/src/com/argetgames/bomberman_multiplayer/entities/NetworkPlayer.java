package com.argetgames.bomberman_multiplayer.entities;

import java.awt.event.KeyEvent;

import com.argetgames.bomberman_multiplayer.network.NetworkController;

public class NetworkPlayer extends Player {

	private NetworkController controller;
	
	public NetworkPlayer(int xTile, int yTile, int size, NetworkController controller) {
		super(xTile, yTile, size);
		this.controller = controller;
		controller.useKeyInput(KeyEvent.VK_W);
		controller.useKeyInput(KeyEvent.VK_S);
		controller.useKeyInput(KeyEvent.VK_A);
		controller.useKeyInput(KeyEvent.VK_D);
		controller.useKeyInput(KeyEvent.VK_E);
	}
	
	protected void checkInput(){
		UP = controller.getKey(KeyEvent.VK_W);
		DOWN = controller.getKey(KeyEvent.VK_S);
		LEFT = controller.getKey(KeyEvent.VK_A);
		RIGHT = controller.getKey(KeyEvent.VK_D);
		ATTACK = controller.getKey(KeyEvent.VK_E);
	}

}
