package com.argetgames.bomberman_multiplayer.entities;

import com.argetgames.arget2d.graphics.Animation2D;
import com.argetgames.arget2d.graphics.Image2D;
import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.arget2d.menu.Rectangle;
import com.argetgames.bomberman_multiplayer.BombermanGame;

public class Explosion extends Rectangle {

	public boolean isDead = false;
	
	private Animation2D animation;

	private static final int ANIMATION_FPS = 20;
	
	public Explosion(int xTile, int yTile, int size, int dir, boolean endPiece)  {
		super(xTile * size, yTile * size, size, size);
		if(endPiece)dir += 4;
		
		switch(dir){
		case 0:
			{
				//up animation
				int frameOrder[] = {6, 7, 8, 9};
				animation = new Animation2D(BombermanGame.explosionSpritesheet, frameOrder, ANIMATION_FPS);
			}break;
		case 1:
			{
				//left animation
				int frameOrder[] = {12, 13, 14, 15};
				animation = new Animation2D(BombermanGame.explosionSpritesheet, frameOrder, ANIMATION_FPS);
			}break;
		case 2:
			{
				//down animation
				int frameOrder[] = {18, 19, 20, 21};
				animation = new Animation2D(BombermanGame.explosionSpritesheet, frameOrder, ANIMATION_FPS);
			}break;
		case 3:
			{
				//right animation
				int frameOrder[] = {24, 25, 26, 27};
				animation = new Animation2D(BombermanGame.explosionSpritesheet, frameOrder, ANIMATION_FPS);
			}break;
		case 4:
			{
				//up-end animation
				int frameOrder[] = {30, 31, 32, 33};
				animation = new Animation2D(BombermanGame.explosionSpritesheet, frameOrder, ANIMATION_FPS);
			}break;
		case 5:
			{
				//left-end animation
				int frameOrder[] = {36, 37, 38, 39};
				animation = new Animation2D(BombermanGame.explosionSpritesheet, frameOrder, ANIMATION_FPS);
			}break;
		case 6:
			{
				//down-end animation
				int frameOrder[] = {42, 43, 44, 45};
				animation = new Animation2D(BombermanGame.explosionSpritesheet, frameOrder, ANIMATION_FPS );
			}break;
		case 7:
			{
				//right-end animation
				int frameOrder[] = {48, 49, 50, 51};
				animation = new Animation2D(BombermanGame.explosionSpritesheet, frameOrder, ANIMATION_FPS);
			}break;
		default:
			{
				//center animation
				int frameOrder[] = {0, 1, 2, 3};
				animation = new Animation2D(BombermanGame.explosionSpritesheet, frameOrder, 20);
			}break;
		}
		
		animation.play(false);
	}
	
	public void update() {
		animation.update();
	    if(animation.playedOnce() == true)isDead = true;
	}
	
	public void draw(Renderer2D renderer) {
		if(!isDead) {
			Image2D frame = animation.getCurrentFrame();
			renderer.useCamera(true);
			renderer.useColorMask(true);
			renderer.renderImage2D(x, y, width, height, frame);
			renderer.useColorMask(false);
			renderer.useCamera(false);
		}
	}

}
