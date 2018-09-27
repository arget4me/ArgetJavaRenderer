package com.argetgames.roadtofive.entities;

import com.argetgames.arget2d.graphics.Animation2D;
import com.argetgames.arget2d.graphics.Image2D;
import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.roadtofive.PlatformGame;

public class Enemy extends Living{

	protected double shootsPerSecond = 2;
	protected int shootDelay = (int)(PlatformGame.global_ups / shootsPerSecond);
	protected int dir = 0;
	protected int agressionRange = 7 * 16;
	protected Animation2D stand;
	
	public Enemy(int x, int y, int width, int height, Level level) {
		super(x, y, width, height, level);
		maxHealth = 60;
		health = maxHealth;
		moveSpeed = 1.2;
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
			level.spawnProjectile(getCenterX(), getCenterY(), 6, angle, 6, 0.4, getID());
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
//		super.draw(renderer, color);
		drawHealthBar(renderer);
		renderer.useCamera(true);
		Image2D frame = stand.getCurrentFrame();
		renderer.renderImage2D(x, y, width, height, frame);
		renderer.useCamera(false);
	}
}
