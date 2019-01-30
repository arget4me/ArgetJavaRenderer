package com.argetgames.bomberman_multiplayer.gamestates;

import java.awt.event.KeyEvent;

import com.argetgames.arget2d.gamestates.GameState;
import com.argetgames.arget2d.gamestates.GameStateManager;
import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.arget2d.input.Keyboard;
import com.argetgames.arget2d.tilemap.TilemapEditor;
import com.argetgames.bomberman_multiplayer.BombermanGame;

public class EditorState extends GameState {
	
	private TilemapEditor mapEditor;

	public EditorState(GameStateManager gsm) {
		super(gsm);
		mapEditor = new TilemapEditor(18, 18, 32, 32, BombermanGame.tileSheet);
		if(BombermanGame.tileSheetSolidMap != null)
			mapEditor.addSolidMap(BombermanGame.tileSheetSolidMap);
		mapEditor.setEntitiesSprite(BombermanGame.enitities);
	}

	@Override
	public void update() {
		if(Keyboard.getKey(KeyEvent.VK_CONTROL) && Keyboard.getKey(KeyEvent.VK_P)){
			switchState(BombermanGame.PLAY_STATE);
			return;
		}
		
		mapEditor.update();
	}

	@Override
	public void draw(Renderer2D renderer) {
		renderer.useColorMask(true);
		mapEditor.draw(renderer);
		renderer.useColorMask(false);
	}

}
