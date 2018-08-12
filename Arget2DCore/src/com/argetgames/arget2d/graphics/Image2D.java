package com.argetgames.arget2d.graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Image2D {
	
	public static Image2D test;
	private BufferedImage img;
	public int[] imagePixels;
	public int width, height;

	public Image2D(String path) {
		load(path);
	}
	
	private void load(String path){
		try {
			img = ImageIO.read(new File(path));
			width = img.getWidth();
			height = img.getHeight();
			imagePixels = new int[width * height];
			img.getRGB(0, 0, width, height, imagePixels, 0, width);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Checks if pixel position is inside the imagePixels buffer.
	 * @param x Pixel x-coordinate.
	 * @param y Pixel y-coordinate.
	 * @return true if inside buffer. false otherwise.
	 */
	private boolean insideBuffer(int x, int y){
		if( y >= 0 && y < height){
			if( x >= 0 && x < width){	
				return true;
			}			
		}
		return false;
	}
	
	/**
	 * Get the color at index.
	 * @param index Index of the color in the array.
	 * @return Integer of the pixels color of type ARGB. 0 if index not in pixels array.
	 */
	public int getColor(int index) {
			int x = index % width;
			int y = index / width;
			if(insideBuffer(x, y)){
				return imagePixels[index];
			}
			return 0x00000000;
	}

}
