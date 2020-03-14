package com.argetgames.arget2d.graphics;

public class Renderer2D {
	
	private final int WIDTH, HEIGHT;
	private int[] pixels;
	private boolean blending = false;
	public Camera2D camera;
	private boolean applyCamera = true;
	private static final int colorMask = 0xFFFF00FF;
	private boolean applyColorMask = false;
	
	//@NOTE: Offset dimension to only render what is inside renderer.
	private int xOffset = 0, yOffset = 0, wOffset = 0, hOffset = 0, xPadding = 0, yPadding = 0;

	public Renderer2D(int width, int height, int[] pixels) {
		WIDTH = width;
		HEIGHT = height;
		this.pixels = pixels;//new int[WIDTH * HEIGHT];
		camera = new Camera2D(0, 0);
	}
	
	public int getWidth() {
		return WIDTH;
	}

	public int getHeight() {
		return HEIGHT;
	}

	/**
	 * Blends two colors together. Alpha determined by the new color. Very expensive.
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
		if(insideBuffer(x, y)){
			int index = (x) + (y) * WIDTH;
			if(applyColorMask){
				if(color != colorMask){
					pixels[index] = (int) color;
				}
			} else if(blending){
				pixels[index] = blendColors(pixels[index], color);
			} else { 
				pixels[index] = (int) color;
			}
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
	 * Tell the renderer if color mask 0xFFFF00FF should be used. Has priority over alpha blending for performance.
	 * @param value If color mask should be used.
	 */
	public void useColorMask(boolean value){
		this.applyColorMask = value;
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
	 * Calculates new x, y, width, height to fill inside the renderer, it ignores parts outside the renderer. 
	 * x and y: Padding, w and h: Offset, has the new coordinates for rendering.
	 * @param startX
	 * @param startY
	 * @param rectWidth
	 * @param rectHeight
	 * @return 
	 */
	private boolean calcOffset(int startX, int startY, int rectWidth, int rectHeight){
		int xOff = 0;
		int yOff = 0;
		xPadding = 0;
		yPadding = 0;
		if(applyCamera){
			xOff += camera.getOffsetX();
			yOff += camera.getOffsetY();
		}
		int xPad = startX + xOff;
		int yPad = startY + yOff;
		
		if(xPad >= WIDTH)return false;
		if(xPad < 0){
			rectWidth += xPad;
			xPadding = xPad * -1;
			xPad = 0;
		}
		if(yPad >= HEIGHT)return false;
		if(yPad < 0){
			rectHeight += yPad;
			yPadding = yPad * -1;
			yPad = 0;
		}
		
		if(rectHeight + yPad > HEIGHT){
			rectHeight = HEIGHT - yPad;
		}
		if(rectWidth + xPad > WIDTH){
			rectWidth = WIDTH - xPad;
		}
		
		xOffset = xPad;
		yOffset = yPad;
		wOffset = rectWidth;
		hOffset = rectHeight;
		return true;
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
		if(!calcOffset(startX, startY, rectWidth, rectHeight))return;
		
		for(int j = 0; j < hOffset; j++ ){
			int y = yOffset + j;
			for(int i = 0; i < wOffset; i++ ){
				int x = xOffset + i;
				renderPixel(x, y, color);
			}
		}
	}
	
	/**
	 * Render a rectangle without filled color.
	 * @param startX Top-left x-coordinate of the rectangle. 
	 * @param startY Top-left y-coordinate of the rectangle.
	 * @param rectWidth Width of the rectangle from the top-left corner.
	 * @param rectHeight Height of the rectangle from the top-left corner.
	 * @param color The rectangle color.
	 */
	public void drawRect(int startX, int startY, int rectWidth, int rectHeight, int color){
		//top line
		fillRect(startX, startY, rectWidth, 1, color);
		
		//bottom line
		fillRect(startX, startY+rectHeight-1, rectWidth, 1, color);
		
		//left line
		fillRect(startX, startY+1, 1, rectHeight -2, color);
		
		//right line
		fillRect(startX + rectWidth -1, startY+1, 1, rectHeight -2, color);
	}
	
	/**
	 * Renders an image to the pixel buffer.
	 * @param startX X-coordinate on the screen to render the image.
	 * @param startY Y-coordinate on the screen to render the image.
	 * @param image Image2D to render, colors are of type ARGB but alpha is only used if "useAlpha" has enabled it.
	 */
	public void renderImage2D(int startX, int startY, Image2D image){
		if(image == null)return;
		if(!calcOffset(startX, startY, image.width, image.height))return;
		
		for(int j = 0; j < hOffset; j++ ){
			int y = yOffset + j;
			for(int i = 0; i < wOffset; i++ ){
				int x = xOffset + i;
				renderPixel(x, y, image.getColor((i + xPadding) + (j + yPadding) * image.width));
			}
		}
	}
	
	
	/**
	 * Renders an image to the pixel buffer.
	 * @param startX X-coordinate on the screen to render the image.
	 * @param startY Y-coordinate on the screen to render the image.
	 * @param image Image2D to render, colors are of type ARGB but alpha is only used if "useAlpha" has enabled it.
	 * @param colorMask the color in image to be replaced by newColor.
	 * @param newColor the color to replace maskColor in image with. 
	*/
	public void renderImage2D(int startX, int startY, Image2D image, int colorMask, int newColor){
		if(image == null) return;
		if(!calcOffset(startX, startY, image.width, image.height))return;
		
		for(int j = 0; j < hOffset; j++ ){
			int y = yOffset + j;
			for(int i = 0; i < wOffset; i++ ){
				int x = xOffset + i;
				int color = image.getColor((i + xPadding) + (j + yPadding) * image.width);
				if(color == colorMask)
					color = newColor;
				renderPixel(x, y, color);
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
		if(image == null) return;
		
		double wScale = image.width / (double)width;
		double hScale = image.height / (double)height;
		if(!calcOffset(startX, startY, width, height))return;
		
		for(int j = 0; j < hOffset; j++ ){
			int y = yOffset + j;
			int ya = (int)((j + yPadding)*hScale);
			for(int i = 0; i < wOffset; i++ ){
				int x = xOffset + i;
				int xa = (int)((i + xPadding)*wScale);
				renderPixel(x, y, image.getColor(xa + ya * image.width));
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
	 * @param colorMask the color in image to be replaced by newColor.
	 * @param newColor the color to replace maskColor in image with. 
	 */
	public void renderImage2D(int startX, int startY, int width, int height, Image2D image, int colorMask, int newColor){
		if(image == null) return;
		
		double wScale = image.width / (double)width;
		double hScale = image.height / (double)height;
		if(!calcOffset(startX, startY, width, height))return;
		
		for(int j = 0; j < hOffset; j++ ){
			int y = yOffset + j;
			int ya = (int)((j + yPadding)*hScale);
			for(int i = 0; i < wOffset; i++ ){
				int x = xOffset + i;
				int xa = (int)((i + xPadding)*wScale);
				int color = image.getColor(xa + ya * image.width);
				if(color == colorMask)
					color = newColor;
				renderPixel(x, y, color);
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
