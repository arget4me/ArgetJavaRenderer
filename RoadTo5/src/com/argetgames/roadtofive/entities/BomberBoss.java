package com.argetgames.roadtofive.entities;

import com.argetgames.arget2d.graphics.Animation2D;
import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.roadtofive.PlatformGame;

public class BomberBoss extends Boss{

	public BomberBoss(int x, int y, int width, int height, Level level) {
		super(x, y, width, height, level);
		maxHealth = 300;
		health = maxHealth;
		moveSpeed = 0.4;
		agressionRange = 16 * 14;
		shootsPerSecond = 0.25;
		shootDelay = (int)(PlatformGame.global_ups / shootsPerSecond);
		{
			int frames[] = {4, 5};
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
			double angle = 0;
			level.spawnProjectile(new BomberSpawnerProjectile(getCenterX(), getCenterY(), angle - 3.14/6, level, teamID));
			level.spawnProjectile(new BomberSpawnerProjectile(getCenterX(), getCenterY(), angle, level, teamID));
			level.spawnProjectile(new BomberSpawnerProjectile(getCenterX(), getCenterY(), angle + 3.14, level, teamID));
			level.spawnProjectile(new BomberSpawnerProjectile(getCenterX(), getCenterY(), angle + 3.14 + 3.14/6, level, teamID));
			shootDelay = (int)(PlatformGame.global_ups / shootsPerSecond);
			
		}else {
			shootDelay--;
		}
		
		if(getCenterY() - level.player.getCenterY() > height*2)
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
