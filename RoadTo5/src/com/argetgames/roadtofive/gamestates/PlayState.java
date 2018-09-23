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
import com.argetgames.roadtofive.entities.Player;
import com.argetgames.roadtofive.entities.Projectile;

public class PlayState extends GameState {
	
	private Tilemap map_00;
	private SpriteSheet tileSheet;
	private Player player;
	private ArrayList<Projectile> shoots;
	private int shootDelay = 0;
	private int shootsPerSecond = 6;

	public PlayState(GameStateManager gsm, SpriteSheet tileSheet) {
		super(gsm);
		this.tileSheet = tileSheet;
		map_00 = new Tilemap("res/maps/map_00", 16, 16, this.tileSheet);
		player = new Player(64, 128, 16, 16 , map_00);
		shoots = new ArrayList<Projectile>();
	}
	
	private void restart(){
		map_00 = new Tilemap("res/maps/map_00", 16, 16, tileSheet);
		player = new Player(64, 128, 16, 16 , map_00);
		PlatformGame.camera.set(player.getCenterX() - PlatformGame.globalWidth/2, 
				player.getCenterY() - PlatformGame.globalHeight/2);
		shoots = new ArrayList<Projectile>();
		
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
		
		if(shootDelay <= 0) {
			if(Mouse.getMouse().isButtonPress(MouseButton.LEFT)) {
				int mx = Mouse.getMouseX() - PlatformGame.globalWidth/2;
				int my = Mouse.getMouseY() - PlatformGame.globalHeight/2;
				double angle = Math.atan2(my, mx);
				
				shoots.add(new Projectile(player.getCenterX(), player.getCenterY(), 5, angle, map_00));
				shootDelay = PlatformGame.global_ups / shootsPerSecond;
			}
		}else {
			shootDelay--;
		}
		
		player.update();
		PlatformGame.camera.set(player.getCenterX() - PlatformGame.globalWidth/2, player.getCenterY() - PlatformGame.globalHeight/2);
		for(int i = 0; i < shoots.size(); i++) {
			Projectile p = shoots.get(i);
			p.update();
			if(p.dead) {
				shoots.remove(i);
				i--;
			}
		}
	}

	@Override
	public void draw(Renderer2D renderer) {
		map_00.draw(renderer);
		player.draw(renderer);
		for(int i = 0; i < shoots.size(); i++) {
			shoots.get(i).draw(renderer, 0xFFFF0000);
		}
		String numProjectiles = "(Num projectiles: " + shoots.size() + ")";
		PlatformGame.textRenderer.drawText(renderer, 10+1, 10+1, 8, numProjectiles, -1, 0xFF000000);
		PlatformGame.textRenderer.drawText(renderer, 10, 10, 8, numProjectiles, -1, 0xFFFFFFFF);
	}

}
