package com.argetgames.arget2d.graphics;

public class Renderer2D {
	
	private final int WIDTH, HEIGHT;
	private int[] pixels;
	private boolean blending = false;
	public Camera2D camera;
	private boolean applyCamera = true;

	public Renderer2D(int width, int height) {
		WIDTH = width;
		HEIGHT = height;
		pixels = new int[WIDTH * HEIGHT];
		camera = new Camera2D(0, 0);
	}
	
	public int getWidth() {
		return WIDTH;
	}

	public int getHeight() {
		return HEIGHT;
	}

	/**
	 * Blends two colors together. Alpha determined by the new color.
	 * @param oldColor Previous color. Alpha is set to be "255 - alpha of newColor".
	 * Format RGB. Can be in ARGB but alpha is ignored
	 * @param newColor New color. Alpha determines blending. Format ARGB, 0xFF00FF00 gives full green.
	 * @return The blended color in format RGB.
	 */
	private int blendColors(int oldColor, int newColor){
		int r1 = (oldColor & 0xFF0000) >>> 16;
		int g1 = (oldColor & 0xFF00) >>> 8;
		int b1 = (oldColor & 0xFF);
		
		double alpha = ((newColor & 0xFF000000) >>> 24);
		int r2 = (newColor & 0xFF0000) >>> 16;
		int g2 = (newColor & 0xFF00) >>> 8;
		int b2 = (newColor & 0xFF);
		
		int r3 = (int) (r2 * (alpha / 255.0) + r1 * ((255.0-alpha)/255.0));
		int g3 = (int) (g2 * (alpha / 255.0) + g1 * ((255.0-alpha)/255.0));
		int b3 = (int) (b2 * (alpha / 255.0) + b1 * ((255.0-alpha)/255.0));
		
		int blendColor = (r3 << 16) + (g3 << 8) + (b3);
		
		return blendColor;
	}
	
	/**
	 * Checks if pixel position is inside the pixels buffer.
	 * @param x Pixel x-coordinate.
	 * @param y Pixel y-coordinate.
	 * @return true if inside buffer. false otherwise.
	 */
	private boolean insideBuffer(int x, int y){
		if( y >= 0 && y < HEIGHT){
			if( x >= 0 && x < WIDTH){	
				return true;
			}			
		}
		return false;
	}
	
	/**
	 * @param x Pixel x-coordinate.
	 * @param y Pixel y-coordinate.
	 * @param color Color to render the pixel. TODO: make a color class that handles mixing and alpha.
	 */
	private void renderPixel(int x, int y, int color){
		if(applyCamera){
			
			x += camera.getOffsetX();
			y += camera.getOffsetY();
		}
		
		if(insideBuffer(x, y)){
			//int oldColor = pixels[x + y * WIDTH];
			int index = (x) + (y) * WIDTH;
			if(blending)
				pixels[index] = blendColors(pixels[index], color);
			else 
				pixels[index] = (int) color;
		}
	}
	
	/**
	 * Tell the renderer if alpha should be used.
	 * @param value True enables alpha, and all colors renderer apply their alpha in the format ARGB. 
	 * False disables alpha, and the renderer interprets all colors as RGB even if given as ARGB.
	 */
	public void useAlpha(boolean value){
		blending = value;
	}
	
	/**
	 * Tell the renderer if camera should be used when drawing to the renderer.
	 * @param value True enables the camera, all positions will be offset by the camera and it's scale.
	 * False camera wont't be used.
	 */
	public void useCamera(boolean value){
		applyCamera = value;
	}
	
	/**
	 * Render a rectangle filled with color.
	 * @param startX Top-left x-coordinate of the rectangle. 
	 * @param startY Top-left y-coordinate of the rectangle.
	 * @param rectWidth Width of the rectangle from the top-left corner.
	 * @param rectHeight Height of the rectangle from the top-left corner.
	 * @param color Color to fill the rectangle with.
	 */
	public void fillRect(int startX, int startY, int rectWidth, int rectHeight, int color){
		
		for(int j = 0; j < rectHeight; j++ ){
			int y = startY + j;
			for(int i = 0; i < rectWidth; i++ ){
				int x = startX + i;
				renderPixel(x, y, color);
			}
		}
	}
	
	/**
	 * Renders an image to the pixel buffer.
	 * @param startX X-coordinate on the screen to render the image.
	 * @param startY Y-coordinate on the screen to render the image.
	 * @param image Image2D to render, colors are of type ARGB but alpha is only used if "useAlpha" has enabled it.
	 */
	public void renderImage2D(int startX, int startY, Image2D image){
		for(int j = 0; j < image.height; j++ ){
			int y = startY + j;
			for(int i = 0; i < image.width; i++ ){
				int x = startX + i;
				renderPixel(x, y, image.getColor(i + j * image.width));
			}
		}
	}
	
	/**
	 * Renders an image to the pixel buffer.
	 * @param startX X-coordinate on the screen to render the image.
	 * @param startY Y-coordinate on the screen to render the image.
	 * @param width The width to render the image at. This causes scaling.
	 * @param height The height to render the image at. This causes scaling.
	 * @param image Image2D to render, colors are of type ARGB but alpha is only used if "useAlpha" has enabled it.
	 */
	public void renderImage2D(int startX, int startY, int width, int height, Image2D image){
		double wScale = image.width / (double)width;
		double hScale = image.height / (double)height;
		
		for(int j = 0; j < height; j++ ){
			int y = startY + j;
			int ya = (int)(j*hScale);
			for(int i = 0; i < width; i++ ){
				int x = startX + i;
				int xa = (int)(i*wScale);
				renderPixel(x, y, image.getColor(xa + ya * image.width));
			}
		}
	}
	
	/**
	 * Get the pixel at index.
	 * @param index Index of the pixel in the array.
	 * @return integer of the pixels color. 0 if index not in pixels array.
	 */
	public int getPixel(int index) {
		int x = index % WIDTH;
		int y = index / WIDTH;
		if(insideBuffer(x, y)){
			return pixels[index];
		}
		return 0;
	}

	
	/**
	 * Fills the pixels buffer with 0. (Black color).
	 */
	public void clear() {
		for(int y = 0; y < HEIGHT; y++){
			for(int x = 0; x < WIDTH; x++){
				pixels[x + y * WIDTH] = 0xff000000;
			}
		}
	}

	
}
