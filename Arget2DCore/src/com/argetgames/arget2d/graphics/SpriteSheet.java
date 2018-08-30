package com.argetgames.arget2d.graphics;

public class SpriteSheet {

	private final int SPRITE_WIDTH, SPRITE_HEIGHT;
	private final int WIDTH, HEIGHT;
	private final int SPRITES_WIDE, SPRITES_HIGH;
	private Image2D sheet;
	private Image2D[] sprites;
	
	public SpriteSheet(String path, int spriteWidth, int spriteHeight) {
		this.SPRITE_WIDTH = spriteWidth;
		this.SPRITE_HEIGHT = spriteHeight;
		sheet = new Image2D(path);
		this.WIDTH = sheet.width;
		this.HEIGHT = sheet.height;
		SPRITES_WIDE = WIDTH / SPRITE_WIDTH;
		SPRITES_HIGH = HEIGHT / SPRITE_HEIGHT;
		grabSprites();
	}
	
	private void grabSprites() {
		sprites = new Image2D[SPRITES_WIDE * SPRITES_HIGH];
		for(int y = 0; y < SPRITES_HIGH; y++) {
			for(int x = 0; x < SPRITES_WIDE; x++) {
				sprites[x + y * SPRITES_WIDE] = new Image2D(sheet, x * SPRITE_WIDTH, y * SPRITE_HEIGHT, SPRITE_WIDTH, SPRITE_HEIGHT);
			}
		}
	}
	
	public Image2D getSprite(int index) {
		return sprites[index % sprites.length];
	}
	
	public Image2D getSpritesheet() {
		return sheet;
	}
	
}
