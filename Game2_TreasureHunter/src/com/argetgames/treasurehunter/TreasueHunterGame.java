package com.argetgames.treasurehunter;

import com.argetgames.arget2d.game.Gameloop;
import com.argetgames.arget2d.graphics.SpriteSheet;

public class TreasueHunterGame extends Gameloop{
	
	private SpriteSheet test;

	public TreasueHunterGame(int width, int height, int scale) {
		super(width, height, scale, true);
		init();
	}
	
	private void init() {
		renderer.useAlpha(true);
		test = new SpriteSheet("res/images/playerSpritesheet.png", 32, 32);
	}

	@Override
	public void updateGame() {
		
	}

	int i = 0;
	@Override
	public void draw() {
		renderer.renderImage2D(10, 10, test.getSprite(i++));
	}

}
