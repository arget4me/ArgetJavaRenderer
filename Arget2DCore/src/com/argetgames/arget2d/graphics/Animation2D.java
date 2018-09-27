package com.argetgames.arget2d.graphics;

import com.argetgames.arget2d.game.Gameloop;

public class Animation2D {

	private SpriteSheet sheet;
	private int[] frameOrder;
	private int fps;
	private int currentFrame;
	private int frameCounter;
	private boolean loop;
	private boolean playedOnce;
	private boolean playing;

	public Animation2D(SpriteSheet sheet, int[] frameOrder, int fps) {
		this.sheet = sheet;
		this.frameOrder = frameOrder;
		this.fps = fps;
		currentFrame = 0;
		frameCounter = 0;
		loop = false;
		playedOnce = false;
		playing = false;
	}

	public void update() {
		if (playing) {
			frameCounter++;
			if (fps * frameCounter / Gameloop.global_ups > 1) {
				frameCounter = 0;
				currentFrame++;
				if (currentFrame >= frameOrder.length) {
					playedOnce = true;
					if(!loop) {
						currentFrame =  frameOrder.length -1;
						playing = false;
					} else {
						currentFrame = 0;
					}
				}
			}
		}
	}

	public void reset() {
		playedOnce = false;
		frameCounter = 0;
		currentFrame = 0;
	}
	
	public void play(boolean loop) {
		reset();
		this.loop = loop;
		playing = true;
	}
	
	public void stop() {
		reset();
		playing = false;
	}
	
	public void pause() {
		playing = false;
	}
	
	public void resume() {
		playing = true;
	}

	public Image2D getCurrentFrame() {
		return sheet.getSprite(frameOrder[currentFrame]);
	}

	public boolean playedOnce() {
		return playedOnce;
	}

}
