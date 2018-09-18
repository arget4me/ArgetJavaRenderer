package com.argetgames.arget2d.graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class Image2D {
	
	public static Image2D test;
	private BufferedImage img;
	public int[] imagePixels;
	public int width, height;

	public Image2D(String path) {
		load(path);
	}
	
	public Image2D(Image2D src, int x, int y, int width, int height) {
		load(src, x, y, width, height);
	}
	
	/**
	 * Load image from file. 
	 * @TODO Implement exception handling if image can't be loaded. Fill a temporary buffer with pink. 0xFFFF00FF
	 * @param path Relative path to file
	 */
	private void load(String path){
		try {
			img = ImageIO.read(new File(path));
			width = img.getWidth();
			height = img.getHeight();
			imagePixels = new int[width * height];
			img.getRGB(0, 0, width, height, imagePixels, 0, width);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error: Couldn't load image with path: " + path,
                    "Error loading image!",
                    JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Grabs a sub image from bigger image.
	 * @param src Source image to grab sub image from
	 * @param x Start x of sub image
	 * @param y Start y of sub image
	 * @param width Width of sub image
	 * @param height Height of sub image
	 */
	private void load(Image2D src, int x, int y, int width, int height){
		this.width = width;
		this.height = height;
		imagePixels = new int[this.width * this.height];
		for(int j = 0; j < height; j++) {
			for(int i = 0; i < width; i++) {
				imagePixels[i + j * width] = src.getColor((x + i) + (y + j) * src.width);
			}
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
