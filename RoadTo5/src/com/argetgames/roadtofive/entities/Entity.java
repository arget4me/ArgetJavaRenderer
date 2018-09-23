package com.argetgames.roadtofive.entities;

import java.awt.event.KeyEvent;

import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.arget2d.input.Keyboard;
import com.argetgames.arget2d.menu.Rectangle;
import com.argetgames.arget2d.tilemap.Tilemap;
import com.argetgames.roadtofive.PlatformGame;

public abstract class Entity extends Rectangle {

	private Tilemap map;

	private double xPos, yPos;
	private double xMove = 0, yMove = 0;

	protected boolean jumping = false;
	protected boolean falling = false;
	protected boolean onGround = false;
	protected boolean applyGravity = true;
	private double dt = 1.0 / ((double)PlatformGame.global_ups);
	private double jumpHeight = 16 * 3.0;
	private double vel_y = 0.0;
	private double secondsToPeak = 1.0 / 3.0;
	private double vel0_y = 2 * jumpHeight / secondsToPeak;
	private double acc_y = ((-2) * (jumpHeight)) / (secondsToPeak * secondsToPeak);

	public Entity(int x, int y, int width, int height, double jumpHeight, double secondsToJumpPeak, Tilemap map) {
		super(x, y, width, height);
		xPos = this.x;
		yPos = this.y;
		this.map = map;
		calculateJump(jumpHeight, secondsToJumpPeak);
	}
	
	private void calculateJump(double jumpHeight, double secondsToPeak){
		this.jumpHeight = jumpHeight;
		this.secondsToPeak = secondsToPeak;
		vel0_y = 2 * jumpHeight / secondsToPeak;
		acc_y = ((-2) * (jumpHeight)) / (secondsToPeak * secondsToPeak);
	}
	
	protected abstract void onTileCollision();
	
	private void moveSteps(double xa, double ya) {
		int deltaY = (int) (yPos + ya) - y;
		int stepsY = deltaY;
		int dy = 1;
		if (stepsY < 0) {
			stepsY = -stepsY;
			dy = -1;
		}
		boolean noCollisionY = true;
		
		int deltaX = (int) (xPos + xa) - x;
		int stepsX = deltaX;
		int dx = 1;
		if (stepsX < 0) {
			stepsX = -stepsX;
			dx = -1;
		}
		boolean noCollisionX = true;
		
		int steps = stepsY;
		if(steps < stepsX) steps = stepsX;
		int xx = 0, yy = 0;
		
		for(int i = 0; i < steps; i++){
			if(yy < stepsY){
				if (map.checkCollision(this, 0, dy)) {
					if(noCollisionY){
						if(jumping){
							jumping = false;
							vel_y =0;
						}
					}
					noCollisionY = false;
				}else {
					y += dy;
					yy++;
				}
			}
			
			if(xx < stepsX){
				if (map.checkCollision(this, dx, 0)) {
					noCollisionX = false;
				}else {
					x += dx;
					xx++;
				}
			}
		}
		
		
		if(!noCollisionY)yPos += yy * dy;
		if (noCollisionY || stepsY == 0) {
			yPos += ya;
		}
		
		if(!noCollisionX)xPos += xx * dx;
		if (noCollisionX || stepsX == 0) {
			xPos += xa;
		}
		
		if(!noCollisionY || !noCollisionX) {
			onTileCollision();
		}
	}

	protected void move(double xa, double ya){
		xMove += xa;
		yMove += ya;
	}
	
	public void update() {
		xMove = 0;
		yMove = 0;
		checkGround();

		behavior();
		if(applyGravity){
			checkFalling();
			yMove += jump();
		}
		moveSteps(xMove, yMove);
	}
	
	protected abstract void behavior();
	
	protected void startJump() {
		if (!onGround || jumping) {
			return;
		}
		jumping = true;
		falling = false;
		vel_y = vel0_y;
	}
	
	private void checkGround(){
		if(!jumping)
			onGround = map.checkCollision(this, 0, 1);
	}
	
	private void checkFalling(){
		if(!onGround && !falling && !jumping){
			falling = true;
			vel_y /= 2;
			if(vel_y < 0)vel_y = 0;
		}else {
			if(onGround || jumping)
				falling = false;
		}
	}

	private double jump() {
		double yy = 0;
		if (jumping || falling) {
			yy -= vel_y * dt + (1.0 / 2.0) * acc_y * dt * dt;
			if(yy >= 0){
				jumping = false;
			}
			vel_y += acc_y * dt;
		}
		return yy;
	}

	
	public void draw(Renderer2D renderer) {
		renderer.useCamera(true);
		super.draw(renderer, 0xFF00FFFF);
		renderer.useCamera(false);
	}
	
	public void draw(Renderer2D renderer, int color) {
		renderer.useCamera(true);
		super.draw(renderer, color);
		renderer.useCamera(false);
	}

}
