package com.argetgames.roadtofive.entities;

import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.arget2d.input.Mouse;
import com.argetgames.arget2d.input.Mouse.MouseButton;
import com.argetgames.roadtofive.PlatformGame;

public class Enemy extends Living{

	protected double shootsPerSecond = 2;
	protected int shootDelay = (int)(PlatformGame.global_ups / shootsPerSecond);
	protected int dir = 0;
	protected int agressionRange = 5 * 16;
	
	public Enemy(int x, int y, int width, int height, Level level) {
		super(x, y, width, height, level);
		maxHealth = 60;
		health = maxHealth;
		moveSpeed = 1.2;
	}
	
	@Override
	protected void behavior() {
		int mx = level.player.getCenterX() - getCenterX();
		int my = level.player.getCenterY() - getCenterY();
		if(mx*mx + my*my > (2*agressionRange*agressionRange)) return;
		
		if(shootDelay <= 0) {
			double angle = Math.atan2(my, mx);
			level.spawnProjectile(getCenterX(), getCenterY(), 6, angle, 10, 0.4, getID());
			shootDelay = (int)(PlatformGame.global_ups / shootsPerSecond);
			
		}else {
			shootDelay--;
		}
		
		if(getCenterY() - level.player.getCenterY() > height*4)
			startJump();
		
		if(level.player.getCenterX() > getCenterX()) {
			dir = 1;
			move(+moveSpeed, 0);
		}else if(level.player.getCenterX() < getCenterX()){
			dir = -1;
			move(-moveSpeed, 0);
		}else {
			dir = 0;
		}
	}
	
	protected void onTileCollision() {
		if(dir == 0)return;
		if(level.checkCollisionStatic(this, dir, 0)){
			startJump();
		}
	}
	
	public void draw(Renderer2D renderer) {
		draw(renderer, 0xFFFFFF00);
	}

	public void draw(Renderer2D renderer, int color) {
		super.draw(renderer, color);
	}
}
