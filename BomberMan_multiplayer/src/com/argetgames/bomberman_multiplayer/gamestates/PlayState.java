package com.argetgames.bomberman_multiplayer.gamestates;

import java.awt.event.KeyEvent;

import com.argetgames.arget2d.gamestates.GameState;
import com.argetgames.arget2d.gamestates.GameStateManager;
import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.arget2d.input.Keyboard;
import com.argetgames.bomberman_multiplayer.BombermanGame;
import com.argetgames.bomberman_multiplayer.entities.Map;

public class PlayState extends GameState {
	
	Map map;

	public PlayState(GameStateManager gsm) {
		super(gsm);
		
		map = new Map();
	}
	
	void restart() {
		map = new Map();
	}

	@Override
	public void update() {
		if(Keyboard.getKey(KeyEvent.VK_ESCAPE)){
			switchState(BombermanGame.EDITOR_STATE);
			return;
		}
		map.update();
//		if(map.getNumPlayers() <= 1)restart();
	}

	@Override
	public void draw(Renderer2D renderer) {
		map.draw(renderer);
		
	}

}
