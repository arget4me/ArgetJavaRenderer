package com.argetgames.roadtofive.entities;

import com.argetgames.arget2d.graphics.Animation2D;
import com.argetgames.arget2d.graphics.Image2D;
import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.roadtofive.PlatformGame;

public class Frogger extends Enemy {

	protected Animation2D jump;
	protected Animation2D fallingAnim;
	protected Animation2D preJump;
	

	private boolean playPreJump = false;
	private boolean playJump = false;

	private static final int TARGET_JUMP_DELAY = 60 * 2;
	private int jumpDelay = TARGET_JUMP_DELAY;
	
	public Frogger(int x, int y, int width, int height, Level level) {
		super(x, y, width, height, level);
		agressionRange = 10 * 16;
		moveSpeed = 0.6;
		shootsPerSecond = 2;
		maxHealth = 30;
		health = maxHealth;
		shootDelay = (int)(PlatformGame.global_ups / shootsPerSecond);
		{
			int frames[] = {21};
			stand = new Animation2D(PlatformGame.enemiesAnimation, frames, 8);
			stand.play(true);
		}
		{
			int frames[] = {22};
			preJump = new Animation2D(PlatformGame.enemiesAnimation, frames, 12);
		}
		{
			int frames[] = {23, 24};
			jump = new Animation2D(PlatformGame.enemiesAnimation, frames, 8);
		}

		{
			int frames[] = {24, 25};
			fallingAnim = new Animation2D(PlatformGame.enemiesAnimation, frames, 30);
		}
	}
	
	@Override
	protected void behavior() {
		if(playPreJump)
		{
			preJump.update();
		}else if(playJump) {
			if(onGround && !jumping) {
				playJump = false;
				jumpDelay = TARGET_JUMP_DELAY;		
				fallingAnim.reset();
				jump.reset();
				preJump.reset();
			}
			jump.update();
			if(falling)
			{
				if(!fallingAnim.isPlaying() && !fallingAnim.playedOnce())
					fallingAnim.play(false);
				fallingAnim.update();
			}
		}else {
			jumpDelay--;
		}
		stand.update();
		int mx = level.player.getCenterX() - getCenterX();
		int my = level.player.getCenterY() - getCenterY();
		if(mx*mx + my*my > (2*agressionRange*agressionRange)) return;
		
		
		if(onGround && jumpDelay < 0)
		{
			if(!playPreJump) {
				preJump.play(false);
				playPreJump = true;
			}
			if(preJump.playedOnce())
			{
				jump.play(false);
				playJump = true;
				playPreJump = false;
				startJump();
			}
		}
		
		
		
		
		if(shootDelay <= 0) {
			double angle = Math.atan2(my, mx);
			level.spawnProjectile(new BlobbyProjectile(getCenterX(), getCenterY(), angle, 0.4, level, teamID));
			
			shootDelay = (int)(PlatformGame.global_ups / shootsPerSecond);
			
		}else {
			shootDelay--;
		}
	}
	
	public void draw(Renderer2D renderer) {
		drawHealthBar(renderer);
		renderer.useCamera(true);
		Image2D frame = stand.getCurrentFrame();
		if(playPreJump){
			frame = preJump.getCurrentFrame();
		}
		else if(playJump) {
			if(!falling) {				
				frame = jump.getCurrentFrame();
			}else
			{
				frame = fallingAnim.getCurrentFrame();
			}
		}
		renderer.renderImage2D(x, y, width, height, frame);
		renderer.useCamera(false);
	}

	public void draw(Renderer2D renderer, int color) {
		draw(renderer);
	}

}
