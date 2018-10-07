package com.argetgames.roadtofive.entities;

import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.arget2d.menu.Rectangle;
import com.argetgames.roadtofive.PlatformGame;

public abstract class Entity extends Rectangle {
	

	protected Level level;
	private static int nextID = 0;
	private int ID = -1;
	
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

	public Entity(int x, int y, int width, int height, double jumpHeight, double secondsToJumpPeak, Level level) {
		super(x, y, width, height);
		xPos = this.x;
		yPos = this.y;
		this.level = level;
		this.ID = nextID++;
		if(nextID > 0xFFFFFF)nextID = 0;
		calculateJump(jumpHeight, secondsToJumpPeak);
	}
	
	private void calculateJump(double jumpHeight, double secondsToPeak){
		this.jumpHeight = jumpHeight;
		this.secondsToPeak = secondsToPeak;
		vel0_y = 2 * jumpHeight / secondsToPeak;
		acc_y = ((-2) * (jumpHeight)) / (secondsToPeak * secondsToPeak);
	}
	
	protected double getXMove() {
		return xMove;
	}
	
	public int getID() {
		return ID;
	}
	
	protected abstract void onTileCollision();
	
	public void onDynamicCollision() {
		
	}
	
	public boolean handleCollisionDynamic(int dx, int dy) {
		return false;
	}
	
	public boolean handleCollisionStatic(int dx, int dy) {
		return level.checkCollisionStatic(this, dx, dy);
	}
	
	
	/*TODO: other solution is to handle dynamic vs static differently. Draw a bounding rectangle between destination and origin.
	 * If the bounding rectangle collides then do more precise collision.
	 */
	private void moveSteps(double xa, double ya) {
		int deltaY = (int) (yPos + ya) - y;
		int stepsY = deltaY;
		int dy = 1;
		if (stepsY < 0) {
			stepsY = -stepsY;
			dy = -1;
		}
		boolean noCollisionStaticY = true;
		boolean noCollisionDynamicY = true;
		
		int deltaX = (int) (xPos + xa) - x;
		int stepsX = deltaX;
		int dx = 1;
		if (stepsX < 0) {
			stepsX = -stepsX;
			dx = -1;
		}
		boolean noCollisionStaticX = true;
		boolean noCollisionDynamicX = true;
		
		int steps = stepsY;
		if(steps < stepsX) steps = stepsX;
		int xx = 0, yy = 0;
		
		for(int i = 0; i < steps; i++){
			if(yy < stepsY){
				if (handleCollisionStatic(0, dy)) {
					if(noCollisionStaticY){
						if(jumping){
							jumping = false;
							vel_y =0;
						}
					}
					noCollisionStaticY = false;
				}else {
					y += dy;
					yy++;
				}
				if(handleCollisionDynamic(0, dy)) {
					noCollisionDynamicY = false;
				}
			}
			
			if(xx < stepsX){
				if (handleCollisionStatic(dx, 0)) {
					noCollisionStaticX = false;
				}else {
					x += dx;
					xx++;
				}
				if(handleCollisionDynamic(dx, 0)) {
					noCollisionDynamicX = false;
				}
			}
		}
		
		
		if(!noCollisionStaticY)yPos += yy * dy;
		if (noCollisionStaticY || stepsY == 0) {
			yPos += ya;
		}
		
		if(!noCollisionStaticX)xPos += xx * dx;
		if (noCollisionStaticX || stepsX == 0) {
			xPos += xa;
		}
		
		if(!noCollisionDynamicY || !noCollisionDynamicX) {
			onDynamicCollision();
		}
		
		if(!noCollisionStaticY || !noCollisionStaticX) {
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
			onGround = level.checkCollisionStatic(this, 0, 1);
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
