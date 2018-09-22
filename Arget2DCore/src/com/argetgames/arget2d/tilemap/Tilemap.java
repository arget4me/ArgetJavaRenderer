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
	protected boolean showSolids = false;

	public Tilemap(int numTilesWide, int numTilesHigh, int tileWidth, int tileHeight, SpriteSheet tileSprites) {
		this.numTilesWide = numTilesWide;
		this.numTilesHigh = numTilesHigh;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;

		tiles = new int[this.numTilesWide * this.numTilesHigh];
		this.tileSprites = tileSprites;
		fillWithEmptyTiles();
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
	
	private void resizeArrays(){
		int numNull = 0;
		for(int i = 0; i < redRectangles.length; i++){
			if(redRectangles[i] == null)
				numNull++;
		}
		
		for(int i = 0; i < redRectangles.length; i++){
			if(redRectangles[i] == null){
				int index = i;
				for(int j = i + 1; j < redRectangles.length; j++){
					redRectangles[index] = redRectangles[j];
					if(redRectangles[index] != null){
						index++;
					}
				}
			}
		}
		
		int newSize = redRectangles.length - numNull;
		if(newSize > 0){
			Rectangle[] temp = new Rectangle[newSize];
			System.arraycopy(redRectangles, 0, temp, 0, newSize);
			redRectangles = temp;
		}else {
			redRectangles = new Rectangle[0];
		}
		
		
		//check for blue:
		numNull = 0;
		for(int i = 0; i < blueRectangles.length; i++){
			if(blueRectangles[i] == null)
				numNull++;
		}
		
		for(int i = 0; i < blueRectangles.length; i++){
			if(blueRectangles[i] == null){
				int index = i;
				for(int j = i + 1; j < blueRectangles.length; j++){
					blueRectangles[index] = blueRectangles[j];
					if(blueRectangles[index] != null){
						index++;
					}
				}
			}
		}
		
		newSize = blueRectangles.length - numNull;
		if(newSize > 0){
			Rectangle[] temp = new Rectangle[newSize];
			System.arraycopy(blueRectangles, 0, temp, 0, newSize);
			blueRectangles = temp;
		}else {
			blueRectangles = new Rectangle[0];
		}
		
	}
	
	protected void removeRectangle(int xT, int yT, int width, int height) {
		Rectangle r = new Rectangle(xT, yT, width, height);
		
		for(int i = 0; i < redRectangles.length; i++){
			if(redRectangles[i].collision(r)){
				redRectangles[i] = null;
			}
		}
		
		for(int i = 0; i < blueRectangles.length; i++){
			if(blueRectangles[i].collision(r)){
				blueRectangles[i] = null;
			}
		}
		resizeArrays();
	}
	
	protected void removeRectangle(Rectangle r) {
		
		for(int i = 0; i < redRectangles.length; i++){
			if(redRectangles[i] == r){
				redRectangles[i] = null;
			}
		}
		
		for(int i = 0; i < blueRectangles.length; i++){
			if(blueRectangles[i] == r){
				blueRectangles[i] = null;
			}
		}
		resizeArrays();
	}
	
	protected void toggleGrid(){
		showGrid = !showGrid;
	}
	
	protected void toggleShowSolids(){
		showSolids = !showSolids;
	}
	
	public boolean checkCollision(Rectangle rect, int xa, int ya){
		
		Rectangle temp = new Rectangle(rect.x + xa, rect.y + ya, rect.width, rect.height);
		//red collision is solid from all sides.
		for(int i = 0; i < redRectangles.length; i++){
			if(temp.collision(redRectangles[i])){
				return true;
			}
		}
		
		for(int i = 0; i < blueRectangles.length; i++){
			if(temp.collision(blueRectangles[i])){
				if(!rect.collision(blueRectangles[i]) && ya > 0){
					return true;
				}
			}
		}
		return false;
	}

	private void drawSolids(Renderer2D renderer){
		if(showSolids){
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
		
		drawSolids(renderer);
	}

}
