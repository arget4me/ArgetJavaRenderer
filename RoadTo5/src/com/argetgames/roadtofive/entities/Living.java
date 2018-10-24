package com.argetgames.roadtofive.entities;

import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.roadtofive.sound.SoundAPI;

public abstract class Living extends Entity {
	
	protected static final int PLAYER_TEAM_ID = 0;
	protected static final int ENEMY_TEAM_ID = 1;
	protected static final int OTHER_TEAM_ID = 2;
	
	public boolean dead = false;
	protected int maxHealth = 100;
	protected int health = maxHealth;
	protected double moveSpeed = 1.0;
	protected int teamID = -1;
	
	protected boolean dynamicSolid = true;

	public Living(int x, int y, int width, int height, Level level) {
		super(x, y, width, height, 16 * 4.0, 1.0 / 2.7, level);
	}

	protected void hurt(int amount) {
		health -= amount;
		if(health <= 0){
			dead = true;
			SoundAPI.testPlaySound("explosion_1.wav", 0.7f);
		}
	}

	@Override
	protected void onTileCollision() {
		//DO NOTHING
	}
	
	public int getTeamID() {
		return teamID;
	}
	
	public boolean getDynamicSolid(){
		return dynamicSolid;
	}
	
	protected void drawHealthBar(Renderer2D renderer) {
		renderer.useCamera(true);
		if(health < maxHealth) {
			renderer.fillRect(x, y-5, width, 4, 0xFFFF0000);
			if(health < 0)health = 0;
			int healthWidth = (int)Math.ceil(width*(double)health/ (double) maxHealth);
			renderer.fillRect(x, y-5, healthWidth, 4, 0xFF00FF00);
		}
		renderer.useCamera(false);
	}
	
	public void draw(Renderer2D renderer) {
		super.draw(renderer);
		drawHealthBar(renderer);
	}
	
	public void draw(Renderer2D renderer, int color) {
		super.draw(renderer, color);
		drawHealthBar(renderer);
	}

}
