package com.argetgames.roadtofive.gamestates;

import java.awt.event.KeyEvent;

import com.argetgames.arget2d.gamestates.GameState;
import com.argetgames.arget2d.gamestates.GameStateManager;
import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.arget2d.graphics.SpriteSheet;
import com.argetgames.arget2d.input.Keyboard;
import com.argetgames.arget2d.tilemap.TilemapEditor;
import com.argetgames.roadtofive.PlatformGame;

public class EditorState extends GameState {
	
	private TilemapEditor mapEditor;

	public EditorState(GameStateManager gsm, SpriteSheet tileSheet, SpriteSheet tileSheetSolidMap) {
		super(gsm);
		mapEditor = new TilemapEditor(400, 400, 16, 16, tileSheet);
		if(tileSheetSolidMap != null)
			mapEditor.addSolidMap(tileSheetSolidMap);
	}

	@Override
	public void update() {
		if(Keyboard.getKey(KeyEvent.VK_ESCAPE)){
			switchState(PlatformGame.MENU_STATE);
			return;
		}
		mapEditor.update();
	}

	@Override
	public void draw(Renderer2D renderer) {
		mapEditor.draw(renderer);
	}

}
