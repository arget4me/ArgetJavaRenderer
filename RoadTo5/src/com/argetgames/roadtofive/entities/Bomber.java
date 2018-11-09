package com.argetgames.roadtofive.entities;

import com.argetgames.arget2d.graphics.Animation2D;
import com.argetgames.arget2d.graphics.Image2D;
import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.roadtofive.PlatformGame;
import com.argetgames.roadtofive.sound.SoundAPI;

public class Bomber extends Enemy {
	
	private boolean exploding = false;
	private boolean exploded = true;
	private Animation2D explosion;
	private int detonateDistance = 1 * 16;
	private int lifetime = PlatformGame.global_ups * 3;
	public boolean useTimer = false;
	
	public Bomber(int x, int y, int width, int height, Level level, boolean useTimer) {
		super(x, y, width, height, level);
		this.useTimer = useTimer;
		agressionRange = 10 * 16;
		moveSpeed = 1.3;
		shootsPerSecond = 30;
		maxHealth = 50;
		health = maxHealth;
		shootDelay = (int)(PlatformGame.global_ups / shootsPerSecond);
		{
			int frames[] = {4, 5};
			stand = new Animation2D(PlatformGame.enemiesAnimation, frames, 8);
			stand.play(true);
		}
		{
			int frames[] = {6, 7, 8, 9, 10};
			explosion = new Animation2D(PlatformGame.enemiesAnimation, frames, 24);
			explosion.play(false);
		}
		
	}
	
	protected void hurt(int amount) {
		health -= amount;
		if(health <= 0){
			exploding = true;
		}
	}
	
	@Override
	protected void behavior() {
		stand.update();
		
		int mx = level.player.getCenterX() - getCenterX();
		int my = level.player.getCenterY() - getCenterY();
		
		if(mx*mx + my*my < (2*detonateDistance*detonateDistance)) {
			exploding = true;
		}
		
		if(exploding) {
			if(explosion.playedOnce()) {
				dead = true;
				level.spawnProjectile(new BomberProjectile(getCenterX(), getCenterY(), 0.1, level, teamID));
				SoundAPI.testPlaySound("explosion_1.wav", 0.7f);
			}
			explosion.update();
		}else {
			if(useTimer) {
				if(--lifetime <= 0) {
					exploding = true;
				}
			}
			if(mx*mx + my*my > (2*agressionRange*agressionRange)) return;
			if(getCenterY() - level.player.getCenterY() > height*2)
				if(onGround)
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
	}
	
	public void draw(Renderer2D renderer) {
		draw(renderer, 0xFF00FF00);
	}

	public void draw(Renderer2D renderer, int color) {
		if(!exploding)drawHealthBar(renderer);
		renderer.useCamera(true);
		Image2D frame = stand.getCurrentFrame();
		 if(exploding) {
			frame = explosion.getCurrentFrame();
		}
		renderer.renderImage2D(x, y, width, height, frame);
		renderer.useCamera(false);
	}

}
