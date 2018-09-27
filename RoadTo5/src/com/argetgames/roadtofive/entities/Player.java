package com.argetgames.roadtofive.entities;

import java.awt.event.KeyEvent;

import com.argetgames.arget2d.graphics.Animation2D;
import com.argetgames.arget2d.graphics.Image2D;
import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.arget2d.input.Keyboard;
import com.argetgames.arget2d.input.Mouse;
import com.argetgames.arget2d.input.Mouse.MouseButton;
import com.argetgames.roadtofive.PlatformGame;

public class Player extends Living {

	private int shootDelay = 0;
	private int shootsPerSecond = 6;
	private double regenPerSeconds = 1.0/2.0;
	private int regenDelay = (int)(PlatformGame.global_ups / regenPerSeconds);
	private Animation2D walkR, walkL, standR, standL, jumpL, jumpR;
	private int walking = 0;
	private boolean left = false, playJump = false;
	
	public Player(int x, int y, int width, int height, Level level) {
		super(x, y, width, height, level);
		moveSpeed = 1.5;
		{
			int frames[] = {0, 1, 2, 3, 4, 5};
			walkR = new Animation2D(PlatformGame.playerAnimation, frames, 20);
			walkR.play(true);
		}
		
		{
			int frames[] = {6, 7};
			standR = new Animation2D(PlatformGame.playerAnimation, frames, 8);
			standR.play(true);
		}
		
		{
			int frames[] = {8, 9};
			jumpR = new Animation2D(PlatformGame.playerAnimation, frames, 18);
			jumpR.play(true);
		}
		
		//left
		
		{
			int frames[] = {12, 13, 14, 15, 16, 17};
			walkL = new Animation2D(PlatformGame.playerAnimation, frames, 20);
			walkL.play(true);
		}
		
		{
			int frames[] = {18, 19};
			standL = new Animation2D(PlatformGame.playerAnimation, frames, 8);
			standL.play(true);
		}
		
		{
			int frames[] = {20, 21};
			jumpL = new Animation2D(PlatformGame.playerAnimation, frames, 18);
			jumpL.play(true);
		}
	}
	
	@Override
	public void behavior() {
		if(playJump) {
			if(onGround && !jumping)
				playJump = false;
			jumpL.update();
			jumpR.update();
		}
		if(walking > 0) {
			walking--;
			if(left)
				walkL.update();
			else
				walkR.update();
		}
		else {
			if(left)
				standL.update();
			else
				standR.update();
		}
		if(health < maxHealth) {
			if(regenDelay <= 0) {
				health++;
				regenDelay += (int)(PlatformGame.global_ups / regenPerSeconds);
			}else {
				regenDelay--;
			}
		}
		
		if (Keyboard.getKey(KeyEvent.VK_SPACE)) {
			if(!playJump) {
				jumpL.play(false);
				jumpR.play(false);
				playJump = true;
				startJump();
			}
		}
		
		if (Keyboard.getKey(KeyEvent.VK_LEFT) || Keyboard.getKey(KeyEvent.VK_A)) {
			move(-moveSpeed, 0);
		} else if (Keyboard.getKey(KeyEvent.VK_RIGHT) || Keyboard.getKey(KeyEvent.VK_D)) {
			move(+moveSpeed, 0);
		}
		
		if(shootDelay <= 0) {
			if(Mouse.getMouse().isButtonPress(MouseButton.LEFT)) {
				int mx = Mouse.getMouseX() - PlatformGame.globalWidth/2;
				int my = Mouse.getMouseY() - PlatformGame.globalHeight/2;
				double angle = Math.atan2(my, mx);
				
				level.spawnProjectile(getCenterX(), getCenterY(), 5, angle, 5, getID());
				shootDelay = PlatformGame.global_ups / shootsPerSecond;
			}
		}else {
			shootDelay--;
		}
		
		if(getXMove() != 0) {
			if(walking <= 0) {
				walking += 10;
			}
			left = (getXMove() < 0);
		}
		
	}
	
	public void draw(Renderer2D renderer) {
		drawHealthBar(renderer);
		renderer.useCamera(true);
		Image2D frame = standR.getCurrentFrame();
		if(left)
			frame = standL.getCurrentFrame();
		if(walking > 0) {
			frame = walkR.getCurrentFrame();
			if(left)
				frame = walkL.getCurrentFrame();
		}
		if(playJump) {
			frame = jumpR.getCurrentFrame();
			if(left)
				frame = jumpL.getCurrentFrame();
		}
		renderer.renderImage2D(x, y, width, height, frame);
		renderer.useCamera(false);
	}

	public void draw(Renderer2D renderer, int color) {
		super.draw(renderer, color);
		draw(renderer);
	}
}
