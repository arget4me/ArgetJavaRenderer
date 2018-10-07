package com.argetgames.roadtofive.entities;

import com.argetgames.arget2d.graphics.Animation2D;
import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.roadtofive.PlatformGame;

public class Boss extends Enemy{

	
	public Boss(int x, int y, int width, int height, Level level) {
		super(x, y, width, height, level);
		maxHealth = 150;
		health = maxHealth;
		moveSpeed = 0.6;
		agressionRange = 16 * 14;
		shootsPerSecond = 2;
		shootDelay = (int)(PlatformGame.global_ups / shootsPerSecond);
		{
			int frames[] = {2, 3};
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
			level.spawnProjectile(getCenterX(), getCenterY(), 4, angle, 14, 1.0, teamID);
			shootDelay = (int)(PlatformGame.global_ups / shootsPerSecond);
			
		}else {
			shootDelay--;
		}
		
		if(getCenterY() - level.player.getCenterY() > height*4)
			startJump();
		
		if(level.player.getCenterX() > getCenterX()+2) {
			dir = 1;
			move(+moveSpeed, 0);
		}else if(level.player.getCenterX() < getCenterX()-2){
			dir = -1;
			move(-moveSpeed, 0);
		}else {
			dir = 0;
		}
	}
	
	protected void drawHealthBar(Renderer2D renderer) {
		renderer.useCamera(true);
		if(health < maxHealth) {
			renderer.fillRect(x, y-5, width, 4, 0xFFFF0000);
			if(health < 0)health = 0;
			int healthWidth = (int)Math.ceil(width*(double)health/ (double) maxHealth);
			renderer.fillRect(x, y-5, healthWidth, 4, 0xFFffdd11);
		}
		renderer.useCamera(false);
	}
	
}
