package com.argetgames.roadtofive.entities;

import com.argetgames.arget2d.graphics.Animation2D;
import com.argetgames.arget2d.graphics.Image2D;
import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.roadtofive.PlatformGame;

public class Speeder extends Enemy {

	int x_cycle_offset = 0;
	private static int CYCLE_VARIACE = 0;
	
	public Speeder(int x, int y, int width, int height, Level level) {
		super(x, y, width, height, level);
		agressionRange = 6 * 16;
		moveSpeed = 3.5;
		shootsPerSecond = 4;
		maxHealth = 32;
		health = maxHealth;
		x_cycle_offset = CYCLE_VARIACE++;
		shootDelay = (int)(PlatformGame.global_ups / shootsPerSecond);
		{
			int frames[] = {26, 27, 28, 29, 30, 31};
			stand = new Animation2D(PlatformGame.enemiesAnimation, frames, 24);
			stand.play(true);
		}
	}
	
	@Override
	protected void behavior() {
		
		stand.update();
		int mx = level.player.getCenterX() - getCenterX();
		int my = level.player.getCenterY() - getCenterY();
		if((x_cycle_offset++ %4) == 0)
		move(-1 + (x_cycle_offset++ %3), 0);
		double squaredDistance = mx*mx + my*my;
		if(squaredDistance > (2*agressionRange*agressionRange)) return;
		
		
		
		if(shootDelay <= 0) {
			if(squaredDistance <= 16*16) {
				double angle = Math.atan2(my, mx);
				
				level.spawnProjectile(new SpeederProjectile(getCenterX(), getCenterY(), angle, level, teamID));
				shootDelay = (int)(PlatformGame.global_ups / shootsPerSecond);
			}
		}else {
			shootDelay--;
		}
		
		if(getCenterY() - level.player.getCenterY() > height*4)
			if(onGround)
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
	
	public void draw(Renderer2D renderer) {
		drawHealthBar(renderer);
		renderer.useCamera(true);
		Image2D frame = stand.getCurrentFrame();
	
		renderer.renderImage2D(x, y, width, height, frame);
		renderer.useCamera(false);
	}

	public void draw(Renderer2D renderer, int color) {
		draw(renderer);
	}

}
