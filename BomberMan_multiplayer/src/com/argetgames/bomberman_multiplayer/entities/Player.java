package com.argetgames.bomberman_multiplayer.entities;

import java.awt.event.KeyEvent;

import com.argetgames.arget2d.graphics.Animation2D;
import com.argetgames.arget2d.graphics.Image2D;
import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.arget2d.input.Keyboard;
import com.argetgames.arget2d.menu.Rectangle;
import com.argetgames.arget2d.tilemap.Tilemap;
import com.argetgames.bomberman_multiplayer.BombermanGame;

public class Player extends Rectangle {

	public int size;
	private double xPos, yPos;
	private Animation2D[] animations = new Animation2D[8];

	private int activeAnimation = 0;
	private boolean animating = false;
	private int DIR = 0;
	private boolean startWalking = false;
	private boolean walking = false;
	private int stepsLeft = 0;
	private int dx = 0, dy = 0;
	public boolean hasSpawned = false;
	public boolean isDead = false;
	private boolean hurt = false;
	protected boolean UP, LEFT, DOWN, RIGHT, ATTACK;
	
	
	public Player(int xTile, int yTile, int size) {
		super(xTile * size, yTile * size, size, size);
		xPos = xTile * size;
		yPos = yTile * size;
		
		this.size = size;
		
		{
			int frameOrder[] = {18, 19, 18, 20};
			animations[0] = new Animation2D(BombermanGame.playerSpritesheet, frameOrder, 8);
			animations[0].stop();
		}
		
		{
			int frameOrder[] = {6, 7, 6, 8};
			animations[1] = new Animation2D(BombermanGame.playerSpritesheet, frameOrder, 8);
			animations[1].stop();
		}
		
		{
			int frameOrder[] = {0, 1, 0, 2};
			animations[2] = new Animation2D(BombermanGame.playerSpritesheet, frameOrder, 8);
			animations[2].stop();
		}
		
		{
			int frameOrder[] = {12, 13, 12, 14};
			animations[3] = new Animation2D(BombermanGame.playerSpritesheet, frameOrder, 8);
			animations[3].stop();
		}
		
		{
			int frameOrder[] = {23, 18, 23, 18, 23, 18, 23};
			animations[4] = new Animation2D(BombermanGame.playerSpritesheet, frameOrder, 30);
			animations[4].stop();
		}
		
		{
			int frameOrder[] = {11, 6, 11, 6, 11, 6, 11};
			animations[5] = new Animation2D(BombermanGame.playerSpritesheet, frameOrder, 30);
			animations[5].stop();
		}
		
		{
			int frameOrder[] = {5, 0, 5, 0, 5, 0, 5};
			animations[6] = new Animation2D(BombermanGame.playerSpritesheet, frameOrder, 30);
			animations[6].stop();
		}
		
		{
			int frameOrder[] = {17, 12, 17, 12, 17, 12, 17};
			animations[7] = new Animation2D(BombermanGame.playerSpritesheet, frameOrder, 30);
			animations[7].stop();
		}
	}
	
	
	public void hurt() {
		if(hurt)return;
		hurt = true;
		activeAnimation += 4;
		animations[activeAnimation].play(false);
	}
	
	protected void checkInput(){
		UP = Keyboard.getKey(KeyEvent.VK_W);
		DOWN = Keyboard.getKey(KeyEvent.VK_S);
		LEFT = Keyboard.getKey(KeyEvent.VK_A);
		RIGHT = Keyboard.getKey(KeyEvent.VK_D);
		ATTACK = Keyboard.getKey(KeyEvent.VK_E);
	}
	
	protected void handleInput(Map map) {
		checkInput();
		boolean startAnimation = false;
		
		int animationID = 0;
		
		if(UP) {
			animationID = 0;
			DIR = 0;
			startAnimation = true;
		}else if(DOWN) {
			animationID = 2;
			DIR = 2;
			startAnimation = true;
		}else if(LEFT) {
			animationID = 1;
			DIR = 1;
			startAnimation = true;
		}else if(RIGHT) {
			animationID = 3;
			DIR = 3;
			startAnimation = true;
		}else if(ATTACK) {
			if(!hasSpawned) {
				int xa = 0; 
				int ya = 0;
				switch(DIR) {
				case 0:
					{
						ya = -1;
					}break;
				case 1:
					{
						xa = -1;
					}break;
				case 2:
					{
						ya = 1;
					}break;
				case 3:
					{
						xa = 1;
					}break;
				default:
					{
						
					}break;	
				}
				
				hasSpawned  = map.spawnBomb((int)xPos / size + xa, (int)yPos / size + ya, size, this);
				
			}
		}
		
		if(startAnimation) {
			if(animationID < 4)startWalking = true;
			animating = true;
			if(animationID != activeAnimation || !animations[animationID].isPlaying())
				animations[animationID].play(false);
			activeAnimation = animationID;
		}
	}
	

	
	public void startWalking(Map map) {
		startWalking = false;
		walking = true;
		switch(DIR) {
		case 0:
			{
				stepsLeft = map.getTileHeight();
				dy = -1;
				dx =  0;
			}
			break;
		case 1:
			{
				stepsLeft = map.getTileWidth();
				dy =  0;
				dx = -1;
			}
			break;
		case 2:
			{
				stepsLeft = map.getTileHeight();
				dy =  1;
				dx =  0;
			}
			break;
		case 3:
			{
				stepsLeft = map.getTileWidth();
				dy =  0;
				dx =  1;
			}
			break;
		default:
			break;
		}
	}
	
	private boolean checkCollision(Map map) {
		return map.checkCollision(this, dx, dy);
	}
	
	public void move(Map map) {
		if(stepsLeft > 0 && !checkCollision(map)) {
			xPos += dx;
			yPos += dy;
			x = (int)xPos;
			y = (int)yPos;
			stepsLeft--;
			if(stepsLeft <= 0) {
				walking = false;
			}
		}else {
			xPos = ((int)xPos / size) * size;
			yPos = ((int)yPos / size) * size;
			x = (int)xPos;
			y = (int)yPos;
			walking = false;
		}
		
	}
	
	public void update(Map map) {
		if(isDead)return;
		
		if(hurt) {
			animations[activeAnimation].update();
			if(animations[activeAnimation].playedOnce()) {
				animating = false;
				animations[activeAnimation].reset();
				isDead = true;
			}
			return;
		}
		
		if(!walking)
			handleInput(map);
		
		if(startWalking) {
			startWalking(map);
		}
		
		if(walking) {
			move(map);
			//for(int i = 0; i < animations.length; i++) {
			animations[activeAnimation].update();
			//}
			if(animations[activeAnimation].playedOnce()) {
				animating = false;
				animations[activeAnimation].reset();
			}
		}
		
	}

	public void draw(Renderer2D renderer) {
		Image2D frame = animations[activeAnimation].getCurrentFrame();
		renderer.useCamera(true);
		renderer.useColorMask(true);
		renderer.renderImage2D(x, y, width, height, frame);
		renderer.useColorMask(false);
		renderer.useCamera(false);
	}
	
	
	
}
