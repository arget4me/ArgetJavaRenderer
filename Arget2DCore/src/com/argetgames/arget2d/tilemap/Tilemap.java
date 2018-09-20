package com.argetgames.arget2d.tilemap;

import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.arget2d.graphics.SpriteSheet;
import com.argetgames.arget2d.menu.Rectangle;

public class Tilemap {
	protected int[] tiles;
	protected int numTilesWide;
	protected int numTilesHigh;
	protected int tileWidth;
	protected int tileHeight;
	protected SpriteSheet tileSprites;
	protected Rectangle[] redRectangles = new Rectangle[0];
	protected Rectangle[] blueRectangles = new Rectangle[0];
	private boolean showGrid = true;

	public Tilemap(int numTilesWide, int numTilesHigh, int tileWidth, int tileHeight, SpriteSheet tileSprites) {
		this.numTilesWide = numTilesWide;
		this.numTilesHigh = numTilesHigh;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;

		tiles = new int[this.numTilesWide * this.numTilesHigh];
		this.tileSprites = tileSprites;
		fillWithEmptyTiles();
	}
	
	protected void addRedRectangle(Rectangle rectangle){
		if(rectangle == null)return;
		Rectangle[] temp = new Rectangle[redRectangles.length + 1];
		System.arraycopy(redRectangles, 0, temp, 0, redRectangles.length);
		temp[redRectangles.length] = rectangle;
		redRectangles = temp;
	}
	
	protected void addBlueRectangle(Rectangle rectangle){
		if(rectangle == null)return;
		Rectangle[] temp = new Rectangle[blueRectangles.length + 1];
		System.arraycopy(blueRectangles, 0, temp, 0, blueRectangles.length);
		temp[blueRectangles.length] = rectangle;
		blueRectangles = temp;
	}

	protected void fillWithEmptyTiles() {
		for (int i = 0; i < tiles.length; i++) {
			tiles[i] = -1;
		}
	}

	protected void fillTilesWithColors() {
		for (int i = 0; i < tiles.length; i++) {
			tiles[i] = 0xFF000000 | ((i * 12) % 0xFFFFFF);
		}
	}
	
	protected void toggleGrid(){
		showGrid = !showGrid;
	}

	public void draw(Renderer2D renderer) {
		if(showGrid){
			int color = 0xFF333333;
			for (int y = 0; y < numTilesHigh; y++) {
				for (int x = 0; x < numTilesWide; x++) {
					renderer.drawRect(x * tileWidth, y * tileHeight, tileWidth+1, tileHeight+1, color);
				}
			}
		}
		
		for (int y = 0; y < numTilesHigh; y++) {
			int ya = y * numTilesWide;
			for (int x = 0; x < numTilesWide; x++) {
				int color = tiles[x + ya];
				if(color != -1)
				renderer.renderImage2D(x * tileWidth, y * tileHeight, tileWidth, tileHeight, tileSprites.getSprite(color));
			}
		}
		
		renderer.useColorMask(false);
		renderer.useAlpha(true);
		for(int i = 0; i < redRectangles.length; i++){
			redRectangles[i].draw(renderer, 0x99FF0000);
		}
		for(int i = 0; i < blueRectangles.length; i++){
			blueRectangles[i].draw(renderer, 0x990000FF);
		}
		renderer.useAlpha(false);
		renderer.useColorMask(true);
		
	}

}
