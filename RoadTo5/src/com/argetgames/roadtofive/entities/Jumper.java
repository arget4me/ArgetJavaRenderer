package com.argetgames.roadtofive.entities;

import com.argetgames.arget2d.graphics.Animation2D;
import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.roadtofive.PlatformGame;

public class Jumper extends Enemy {
	
	public Jumper(int x, int y, int width, int height, Level level) {
		super(x, y, width, height, level);
		agressionRange = 10 * 16;
		moveSpeed = 0.6;
		shootsPerSecond = 30;
		maxHealth = 30;
		health = maxHealth;
		shootDelay = (int)(PlatformGame.global_ups / shootsPerSecond);
		{
			int frames[] = {0, 1};
			stand = new Animation2D(PlatformGame.enemiesAnimation, frames, 8);
			stand.play(true);
		}
	}
	
	@Override
	protected void behavior() {
		stand.update();
		int mx = level.player.getCenterX() - getCenterX();
		int my = level.player.getCenterY() - getCenterY();
		if(mx*mx + my*my > (2*agressionRange*agressionRange)) return;
		
		if(shootDelay <= 0) {
			double angle = Math.atan2(my, mx);
			level.spawnProjectile(getCenterX(), getCenterY(), 0.8, angle, 1, 0.4, teamID);
			if(onGround)
				startJump();
			shootDelay = (int)(PlatformGame.global_ups / shootsPerSecond);
			
		}else {
			shootDelay--;
		}
		
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
	
	public void draw(Renderer2D renderer) {
		draw(renderer, 0xFF00FF00);
	}

	public void draw(Renderer2D renderer, int color) {
		super.draw(renderer, color);
	}

}
