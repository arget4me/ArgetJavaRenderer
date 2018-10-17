package com.argetgames.roadtofive.gamestates;

import java.awt.event.KeyEvent;

import com.argetgames.arget2d.gamestates.GameState;
import com.argetgames.arget2d.gamestates.GameStateManager;
import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.arget2d.input.Keyboard;
import com.argetgames.arget2d.input.Mouse;
import com.argetgames.arget2d.input.Mouse.MouseButton;
import com.argetgames.arget2d.tilemap.Tilemap;
import com.argetgames.arget2d.tilemap.TilemapEditor;
import com.argetgames.roadtofive.PlatformGame;

public class EditorState extends GameState {
	
	private TilemapEditor mapEditor;
	private TilemapEditor entityEditor;
	private boolean editEntities = false;

	public EditorState(GameStateManager gsm) {
		super(gsm);
		mapEditor = new TilemapEditor(400, 400, 16, 16, PlatformGame.tileSheet);
		if(PlatformGame.tileSheetSolidMap != null)
			mapEditor.addSolidMap(PlatformGame.tileSheetSolidMap);
		mapEditor.setEntitiesSprite(PlatformGame.enitities);
//		entityEditor = new TilemapEditor(400, 400, 16, 16, PlatformGame.enitities);
//		entityEditor.hideTools = !editEntities;
//		entityEditor.toggleGrid();
	}

	@Override
	public void update() {
		if(Keyboard.getKey(KeyEvent.VK_ESCAPE)){
			switchState(PlatformGame.MENU_STATE);
			return;
		}
//		if(Mouse.getMouse().isButtonClicked(MouseButton.RIGHT)) {
//			entityEditor.hideTools = editEntities;
//			editEntities = !editEntities;
//			mapEditor.hideTools = editEntities;
//			System.out.println("Switch");
//		}
		
		if(editEntities) {
//			entityEditor.update();
		}else {
			mapEditor.update();
		}
	}

	@Override
	public void draw(Renderer2D renderer) {
		mapEditor.draw(renderer);
//		entityEditor.draw(renderer);
	}

}
