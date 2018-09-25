package com.argetgames.roadtofive.gamestates;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import com.argetgames.arget2d.gamestates.GameState;
import com.argetgames.arget2d.gamestates.GameStateManager;
import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.arget2d.graphics.SpriteSheet;
import com.argetgames.arget2d.input.Keyboard;
import com.argetgames.arget2d.input.Mouse;
import com.argetgames.arget2d.input.Mouse.MouseButton;
import com.argetgames.arget2d.tilemap.Tilemap;
import com.argetgames.roadtofive.PlatformGame;
import com.argetgames.roadtofive.entities.Level;
import com.argetgames.roadtofive.entities.Living;
import com.argetgames.roadtofive.entities.Projectile;

public class PlayState extends GameState {
	
	private Tilemap map_00;
	private Level level_00;
	private SpriteSheet tileSheet;


	public PlayState(GameStateManager gsm, SpriteSheet tileSheet) {
		super(gsm);
		this.tileSheet = tileSheet;
		map_00 = new Tilemap("res/maps/map_00", 16, 16, this.tileSheet);
		level_00 = new Level(map_00);
	}
	
	private void restart(){
		map_00 = new Tilemap("res/maps/map_00", 16, 16, tileSheet);
		level_00 = new Level(map_00);
	}

	@Override
	public void update() {
		if(Keyboard.getKey(KeyEvent.VK_ESCAPE)){
			switchState(PlatformGame.MENU_STATE);
			return;
		}
		
		if(level_00.player.dead || Mouse.getMouse().isButtonClicked(MouseButton.RIGHT))
			restart();
	
		level_00.update();
	}

	@Override
	public void draw(Renderer2D renderer) {
		level_00.draw(renderer);
	}

}
