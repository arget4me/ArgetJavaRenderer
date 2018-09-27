package com.argetgames.arget2d.graphics;

public class TextRenderer {

	private SpriteSheet fontSheet;
	private int fontPixelWidth;
	private int fontPixelHeight;
	private String characters;
	private int colorMask;
	
	public TextRenderer(SpriteSheet fontSheet, String characters, int colorMask) {
		this.fontSheet = fontSheet;
		fontPixelWidth = fontSheet.getSprite(0).width;
		fontPixelHeight = fontSheet.getSprite(0).height;
		this.characters = characters;
		this.colorMask = colorMask;
		
	}
	
	public String getCharacters() {
		return characters;
	}
	
	private void drawCharacter(Renderer2D renderer, int x, int y, char character, int color){
		int index = characters.indexOf(character);
		if(index < 0)index = 0;
		
		renderer.renderImage2D(x, y, fontSheet.getSprite(index), colorMask, color);
	}
	
	private void drawCharacter(Renderer2D renderer, int x, int y, int size, char character, int color){
		int index = characters.indexOf(character);
		if(index < 0)index = 0;

		renderer.renderImage2D(x, y, size, size, fontSheet.getSprite(index), colorMask, color);
	}
	
	public void drawText(Renderer2D renderer, int x, int y, String text, int spacing, int color){
		for(int i = 0; i < text.length(); i++){
			drawCharacter(renderer, x + i * (spacing + fontPixelWidth), y, text.charAt(i), color);
		}
	}
	
	public void drawText(Renderer2D renderer, int x, int y, int size, String text, int spacing, int color){
		for(int i = 0; i < text.length(); i++){
			drawCharacter(renderer, x + i * (spacing+size), y, size, text.charAt(i), color);
		}
	}
	
	

}
