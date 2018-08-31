package com.argetgames.treasurehunter;

import java.awt.event.KeyEvent;

import com.argetgames.arget2d.game.Gameloop;
import com.argetgames.arget2d.graphics.Animation2D;
import com.argetgames.arget2d.graphics.SpriteSheet;
import com.argetgames.arget2d.input.Keyboard;

public class TreasueHunterGame extends Gameloop{
	
	private SpriteSheet test;
	private Animation2D anim[] = new Animation2D[4];

	public TreasueHunterGame(int width, int height, int scale) {
		super(width, height, scale, true);
		init();
	}
	
	private void init() {
		debug_log = false;
		renderer.useAlpha(true);
		test = new SpriteSheet("res/images/playerSpritesheet.png", 32, 32);
		
		for(int row = 0; row < anim.length; row++) {
			int[] order = {0,1,0,2};
			for(int i = 0; i < order.length; i++) {
				order[i] += row*6;
				anim[row] = new Animation2D(test, order, 12);
				anim[row].play(true);
			}
		}
		
	}
	int dir = 0;

	@Override
	public void updateGame() {
		if(Keyboard.getKey(KeyEvent.VK_UP)) {
			dir = 3;
			anim[dir].resume();
			anim[(dir + 1)%anim.length].stop();
			anim[(dir + 2)%anim.length].stop();
			anim[(dir + 3)%anim.length].stop();
		}
		if(Keyboard.getKey(KeyEvent.VK_DOWN)) {
			dir = 0;
			anim[dir].resume();
			anim[(dir + 1)%anim.length].stop();
			anim[(dir + 2)%anim.length].stop();
			anim[(dir + 3)%anim.length].stop();
		}
		if(Keyboard.getKey(KeyEvent.VK_LEFT)) {
			dir = 1;
			anim[dir].resume();
			anim[(dir + 1)%anim.length].stop();
			anim[(dir + 2)%anim.length].stop();
			anim[(dir + 3)%anim.length].stop();
		}
		if(Keyboard.getKey(KeyEvent.VK_RIGHT)) {
			dir = 2;
			anim[dir].resume();
			anim[(dir + 1)%anim.length].stop();
			anim[(dir + 2)%anim.length].stop();
			anim[(dir + 3)%anim.length].stop();
		}
		for(int i = 0; i < anim.length; i++) {
			anim[i].update();
		}
			
	}

	
	@Override
	public void draw() {
		renderer.renderImage2D(10, 10, anim[0].getCurrentFrame());
		renderer.renderImage2D(50, 10, anim[1].getCurrentFrame());
		renderer.renderImage2D(10, 50, anim[2].getCurrentFrame());
		renderer.renderImage2D(50, 50, anim[3].getCurrentFrame());
	}

}
