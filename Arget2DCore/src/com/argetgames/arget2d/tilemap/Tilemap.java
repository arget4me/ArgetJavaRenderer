package com.argetgames.arget2d.tilemap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

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
	
	protected void load(String path){
		
	}
	
	protected void load(File file){
		if(file == null)return;
		try {
			byte[] data = Files.readAllBytes(file.toPath());
			load(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void load(byte[] tilemapData){
		//TODO: errorcheck bounds of tilemapData..
		int tilesWide = 0x00000000;
		int offset = 0;
		for(int i = 0; i < Integer.BYTES; i++){
			tilesWide |= ((tilemapData[offset] & 0xFF) << (8*i));
			offset++;
		}
		
		int tilesHigh = 0x00000000;
		for(int i = 0; i < Integer.BYTES; i++){
			tilesHigh |= ((tilemapData[offset] & 0xFF) << (8*i));
			offset++;
		}
		int[] localTiles = new int[tilesHigh * tilesWide];
		for(int i = 0; i < localTiles.length; i++){
			for(int j = 0; j < Integer.BYTES; j++) {
				localTiles[i] |= (tilemapData[offset] & 0xFF) << (8*j);
				offset++;
			}
		}
		
		
		int redLength = 0;
		for(int i = 0; i < Integer.BYTES; i++){
			redLength |= (tilemapData[offset] & 0xFF) << (8*i);
			offset++;
		}
		
		Rectangle[] red = new Rectangle[redLength];
		for(int i = 0; i < red.length; i++){
			int x = 0; int y = 0; int width = 0; int height = 0;
			for(int j = 0; j < Integer.BYTES; j++) {
				x |= (tilemapData[offset] & 0xFF) << (8*j);
				offset++;
			}
			for(int j = 0; j < Integer.BYTES; j++) {
				y |= (tilemapData[offset] & 0xFF) << (8*j);
				offset++;
			}
			for(int j = 0; j < Integer.BYTES; j++) {
				width |= (tilemapData[offset] & 0xFF) << (8*j);
				offset++;
			}
			for(int j = 0; j < Integer.BYTES; j++) {
				height |= (tilemapData[offset] & 0xFF) << (8*j);
				offset++;
			}
			red[i] = new Rectangle(x, y, width, height);
		}
		
		

		int blueLength = 0;
		for(int i = 0; i < Integer.BYTES; i++){
			blueLength |= (tilemapData[offset] & 0xFF) << (8*i);
			offset++;
		}
		
		Rectangle[] blue = new Rectangle[blueLength];
		for(int i = 0; i < blue.length; i++){
			int x = 0; int y = 0; int width = 0; int height = 0;
			for(int j = 0; j < Integer.BYTES; j++) {
				x |= (tilemapData[offset] & 0xFF) << (8*j);
				offset++;
			}
			for(int j = 0; j < Integer.BYTES; j++) {
				y |= (tilemapData[offset] & 0xFF) << (8*j);
				offset++;
			}
			for(int j = 0; j < Integer.BYTES; j++) {
				width |= (tilemapData[offset] & 0xFF) << (8*j);
				offset++;
			}
			for(int j = 0; j < Integer.BYTES; j++) {
				height |= (tilemapData[offset] & 0xFF) << (8*j);
				offset++;
			}
			blue[i] = new Rectangle(x, y, width, height);
		}
		tiles = localTiles;
		numTilesWide = tilesWide;
		numTilesHigh = tilesHigh;
		redRectangles = red;
		blueRectangles = blue;
	}
	
	protected byte[] getDataTilemap(){
		int tilesWideSize = 1 * Integer.BYTES;
		int tilesHighSize = 1 * Integer.BYTES;
		int tilesSize = tiles.length * Integer.BYTES;
		int redLengthNumberSize = 1 * Integer.BYTES;
		int redAttributesSize = (redRectangles.length * 4) * Integer.BYTES;
		int blueLengthNumberSize = 1 * Integer.BYTES;
		int blueAttributesSize = (blueRectangles.length * 4) * Integer.BYTES;
//		String spritesheetPath
//		int spriteSheetLengthSize = 1 * Integer.BYTES;
//		int spriteSheetLength = spritesheetPath.getBytes().length;
		
		int size = 0;
		size += tilesWideSize;
		size += tilesHighSize;
		size += tilesSize;
		size += redLengthNumberSize;
		size += redAttributesSize;
		size += blueLengthNumberSize;
		size += blueAttributesSize;
//		size += spriteSheetLengthSize;
//		size += spriteSheetLength;
		byte[] data = new byte[size];
		
		int offset = 0;
		for(int i = 0; i < Integer.BYTES; i++){
			data[offset] = (byte) ((numTilesWide >> (8*i)) & 0xFF);
			offset++;
		}
		for(int i = 0; i < Integer.BYTES; i++){
			data[offset] = (byte) ((numTilesHigh >> (8*i)) & 0xFF);
			offset++;
		}
		for(int i = 0; i < tiles.length; i++){
			for(int j = 0; j < Integer.BYTES; j++) {
				data[offset] = (byte) ((tiles[i] >> (8*j)) & 0xFF);
				offset++;
			}
		}
		
		for(int i = 0; i < Integer.BYTES; i++){
			data[offset] = (byte) ((redRectangles.length >> (8*i)) & 0xFF);
			offset++;
		}
		
		for(int i = 0; i < redRectangles.length; i++){
			for(int j = 0; j < Integer.BYTES; j++) {
				data[offset] = (byte) ((redRectangles[i].x >> (8*j)) & 0xFF);
				offset++;
			}
			for(int j = 0; j < Integer.BYTES; j++) {
				data[offset] = (byte) ((redRectangles[i].y >> (8*j)) & 0xFF);
				offset++;
			}
			for(int j = 0; j < Integer.BYTES; j++) {
				data[offset] = (byte) ((redRectangles[i].width >> (8*j)) & 0xFF);
				offset++;
			}
			for(int j = 0; j < Integer.BYTES; j++) {
				data[offset] = (byte) ((redRectangles[i].height >> (8*j)) & 0xFF);
				offset++;
			}
		}
		
		
		for(int i = 0; i < Integer.BYTES; i++){
			data[offset] = (byte) ((blueRectangles.length >> (8*i)) & 0xFF);
			offset++;
		}
		
		for(int i = 0; i < blueRectangles.length; i++){
			for(int j = 0; j < Integer.BYTES; j++) {
				data[offset] = (byte) ((blueRectangles[i].x >> (8*j)) & 0xFF);
				offset++;
			}
			for(int j = 0; j < Integer.BYTES; j++) {
				data[offset] = (byte) ((blueRectangles[i].y >> (8*j)) & 0xFF);
				offset++;
			}
			for(int j = 0; j < Integer.BYTES; j++) {
				data[offset] = (byte) ((blueRectangles[i].width >> (8*j)) & 0xFF);
				offset++;
			}
			for(int j = 0; j < Integer.BYTES; j++) {
				data[offset] = (byte) ((blueRectangles[i].height >> (8*j)) & 0xFF);
				offset++;
			}
		}
		
		return data;
	}
	
	protected void write(File file){
		if(file == null)return;
		byte[] data = getDataTilemap();
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(data, 0, data.length);
			fos.flush();
			fos.close();
		} catch (IOException e) {
			
			e.printStackTrace();
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
	
	
	//TODO: make this nicer to read.
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
