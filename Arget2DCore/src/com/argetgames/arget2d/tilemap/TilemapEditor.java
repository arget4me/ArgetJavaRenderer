package com.argetgames.arget2d.tilemap;

import com.argetgames.arget2d.game.Gameloop;
import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.arget2d.graphics.SpriteSheet;
import com.argetgames.arget2d.input.Mouse;
import com.argetgames.arget2d.input.Mouse.MouseButton;
import com.argetgames.arget2d.menu.Image2DButton;
import com.argetgames.arget2d.menu.Scroller;

public class TilemapEditor extends Tilemap {

	private int panelWidth;
	private int panelX;
	private Image2DButton[] buttons;
	private int currentTile = 0;
	private int buttonsPerLine, padding, scrollerSize, buttonWidth, scrollMax;
	private Scroller scroller;

	public TilemapEditor(SpriteSheet tileSprites, int width, int height) {
		super(width, height, tileSprites.getSpriteWidth(), tileSprites.getSpriteHeight(), tileSprites);
		int numSprites = tileSprites.getNumSprites();
		buttons = new Image2DButton[numSprites];
		calculateSizes();
	}

	public void calculateSizes() {
		panelWidth = Gameloop.globalWidth / 5;
		panelX = Gameloop.globalWidth - Gameloop.globalWidth / 5;
		buttonsPerLine = 6;
		padding = panelWidth / 80;
		scrollerSize = panelWidth / 8;
		scroller = new Scroller(Gameloop.globalWidth - scrollerSize - padding, padding, scrollerSize,
				Gameloop.globalHeight - padding * 2);
		buttonWidth = (panelWidth - scrollerSize - padding * 4) / buttonsPerLine - padding;
		for (int i = 0; i < buttons.length; i++) {
			int xa = (i % buttonsPerLine) * (buttonWidth + padding) + padding * 2;
			int ya = (i / buttonsPerLine) * (buttonWidth + padding) + padding;
			scrollMax = (i / buttonsPerLine);
			buttons[i] = new Image2DButton(panelX + xa, ya, buttonWidth, buttonWidth, tileSprites.getSprite(i));
		}

		scrollMax = padding + scrollMax * (buttonWidth + padding) - (Gameloop.globalHeight - (buttonWidth + padding));
		if (scrollMax < 0)
			scrollMax = 0;
	}

	public void update() {
		int mx = Mouse.getMouseX();
		int my = Mouse.getMouseY();
		scroller.update();
		for (int i = 0; i < buttons.length; i++) {
			int ya = (i / buttonsPerLine) * (buttonWidth + padding) + padding
					- (int) (scroller.getScrollValue() * scrollMax);
			buttons[i].y = ya;
			if (buttons[i].y > -tileHeight) {
				buttons[i].update(mx, my);
				if (buttons[i].getClicked()) {
					currentTile = i;
				}
			}
		}

		if (mx < panelX && Mouse.getMouse().isButtonPress(MouseButton.LEFT)) {
			int xa = mx - Gameloop.camera.getOffsetX();
			int ya = my - Gameloop.camera.getOffsetY();
			int xT = xa / tileWidth;
			int yT = ya / tileHeight;

			if (xT >= 0 && xT < numTilesWide) {
				if (yT >= 0 && yT < numTilesHigh) {
					tiles[xT + yT * numTilesWide] = currentTile;
				}
			}

		}
	}

	private void renderScroller(Renderer2D renderer) {
		scroller.draw(renderer);
	}

	public void draw(Renderer2D renderer) {
		super.draw(renderer);
		
		renderer.useCamera(false);
		renderer.fillRect(panelX, 0, panelWidth, Gameloop.globalHeight, 0xFF00FFFF);
		renderScroller(renderer);
		for (int i = 0; i < buttons.length; i++) {
			if (buttons[i].y > -tileHeight)
				buttons[i].draw(renderer, 0xFF333333);
		}
		renderer.useCamera(true);

	}

}
