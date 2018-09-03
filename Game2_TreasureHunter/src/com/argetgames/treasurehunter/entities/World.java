package com.argetgames.treasurehunter.entities;

import com.argetgames.arget2d.graphics.Renderer2D;

public class World {
	
	private int width, height;
	private int[] ground;
	private int tilesize = 32;
	
	public World() {
		init(100, 100);
		fillRandom();
	}
	
	private void init(int width, int height) {
		this.width = width;
		this.height = height;
		ground = new int[width * height];
	}

	private void fillRandom() {
		for(int i = 0; i < ground.length; i++) {
			int color = 0xFF000000;
			color |= (i % 0xFF) << 16;
			color |= ((i*i + 50) % 0xFF) << 8;
			color |= ((i*4 + 25) % 0xFF);
			
			ground[i] = color;
		}
	}
	
	public void draw(Renderer2D renderer, Camera camera) {
		int xOffset = 0;
		int yOffset = 0;
		if(camera != null) {
			xOffset = (int)camera.getX();
			yOffset = (int)camera.getY();
		}
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				renderer.fillRect(x * tilesize - xOffset, y * tilesize - yOffset, tilesize, tilesize, ground[x + y * width]);
			}
		}
	}
}
