package com.argetgames.roadtofive.gamestates;

import java.awt.event.KeyEvent;

import com.argetgames.arget2d.gamestates.GameState;
import com.argetgames.arget2d.gamestates.GameStateManager;
import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.arget2d.graphics.SpriteSheet;
import com.argetgames.arget2d.input.Keyboard;
import com.argetgames.arget2d.input.Mouse;
import com.argetgames.arget2d.input.Mouse.MouseButton;
import com.argetgames.arget2d.tilemap.Tilemap;
import com.argetgames.roadtofive.PlatformGame;
import com.argetgames.roadtofive.entities.Entity;
import com.argetgames.roadtofive.entities.Player;

public class PlayState extends GameState {
	
	private Tilemap map_00;
	private SpriteSheet tileSheet;
	private Entity player;

	public PlayState(GameStateManager gsm, SpriteSheet tileSheet) {
		super(gsm);
		this.tileSheet = tileSheet;
		map_00 = new Tilemap("res/maps/map_00", 16, 16, this.tileSheet);
		player = new Player(64, 128, 16, 16 , map_00);
	}
	
	private void restart(){
		map_00 = new Tilemap("res/maps/map_00", 16, 16, tileSheet);
		player = new Player(64, 128, 16, 16 , map_00);
		PlatformGame.camera.set(player.getCenterX() - PlatformGame.globalWidth/2, 
				player.getCenterY() - PlatformGame.globalHeight/2);
	}

	@Override
	public void update() {
		if(Keyboard.getKey(KeyEvent.VK_ESCAPE)){
			switchState(PlatformGame.MENU_STATE);
			return;
		}
		
		if(Mouse.getMouse().isButtonClicked(MouseButton.RIGHT)){
			restart();
		}
		player.update();
		PlatformGame.camera.set(player.getCenterX() - PlatformGame.globalWidth/2, 
				player.getCenterY() - PlatformGame.globalHeight/2);
		
	}

	@Override
	public void draw(Renderer2D renderer) {
		map_00.draw(renderer);
		player.draw(renderer);
		PlatformGame.textRenderer.drawText(renderer, 10+1, 10+1, 8, "Play state", -1, 0xFF000000);
		PlatformGame.textRenderer.drawText(renderer, 10, 10, 8, "Play state", -1, 0xFFFFFFFF);
	}

}
