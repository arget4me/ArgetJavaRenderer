package com.argetgames.arget2d.tilemap;

import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.arget2d.graphics.SpriteSheet;

public class Tilemap {
	protected int[] tiles;
	protected int numTilesWide;
	protected int numTilesHigh;
	protected int tileWidth;
	protected int tileHeight;
	protected SpriteSheet tileSprites;

	public Tilemap(int numTilesWide, int numTilesHigh, int tileWidth, int tileHeight, SpriteSheet tileSprites) {
		this.numTilesWide = numTilesWide;
		this.numTilesHigh = numTilesHigh;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;

		tiles = new int[this.numTilesWide * this.numTilesHigh];
		this.tileSprites = tileSprites;
		fillTiles();
	}

	protected void fillTiles() {
		for (int i = 0; i < tiles.length; i++) {
			tiles[i] = 0;
		}
	}

	protected void fillTilesWithColors() {
		for (int i = 0; i < tiles.length; i++) {
			tiles[i] = 0xFF000000 | ((i * 12) % 0xFFFFFF);
		}
	}

	public void update() {

	}

	public void draw(Renderer2D renderer) {
		int cX = renderer.camera.getOffsetX();
		int cY = renderer.camera.getOffsetY();
		int rH = renderer.getHeight();
		int rW = renderer.getWidth();
		int rY;
		int rX;
		for (int y = 0; y < numTilesHigh; y++) {
			int ya = y * numTilesWide;
			rY = y * tileHeight + cY;
			if (rY >= -tileHeight && rY < rH) {
				for (int x = 0; x < numTilesWide; x++) {
					rX = x * tileWidth + cX;
					if (rX >= -tileWidth && rX < rW) {
						int color = tiles[x + ya];
						renderer.renderImage2D(x * tileWidth, y * tileHeight, tileWidth, tileHeight,
								tileSprites.getSprite(color));
					}
				}
			}

		}
	}

}
