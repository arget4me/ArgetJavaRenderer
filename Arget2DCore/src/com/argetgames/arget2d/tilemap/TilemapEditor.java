package com.argetgames.arget2d.tilemap;

import java.awt.event.KeyEvent;

import com.argetgames.arget2d.game.Gameloop;
import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.arget2d.graphics.SpriteSheet;
import com.argetgames.arget2d.input.Keyboard;
import com.argetgames.arget2d.input.Mouse;
import com.argetgames.arget2d.input.Mouse.MouseButton;
import com.argetgames.arget2d.menu.Button;
import com.argetgames.arget2d.menu.Image2DButton;
import com.argetgames.arget2d.menu.Rectangle;
import com.argetgames.arget2d.menu.Scroller;

public class TilemapEditor extends Tilemap {

	private int panelWidth;
	private int panelX;
	private Image2DButton[] buttons;
	private Button gridButton, drawRedSolidButton, drawBlueSolidButton;
	private int buttonsPerLine, padding, scrollerSize, buttonWidth, scrollMax, startY;
	private Scroller scroller;
	private double panSpeed = 4.5;

	//Editor logic
	private int currentTile = 0;
	private boolean drawRedSolid = false, drawBlueSolid = false;

	private int rectStartX, rectStartY;
	private boolean drawNewRectangle = true;
	private Rectangle paintedRectangle = null;
	
	public TilemapEditor(int width, int height, int tileWidth, int tileHeight, SpriteSheet tileSprites) {
		super(width, height, tileWidth, tileHeight, tileSprites);
		int numSprites = tileSprites.getNumSprites();
		buttons = new Image2DButton[numSprites];
		calculateSizes();
	}

	private int getButtonX(int index) {
		return (index % buttonsPerLine) * (buttonWidth + padding) + padding * 2;
	}

	private int getButtonY(int index) {
		return (index / buttonsPerLine) * (buttonWidth + padding) + startY;
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
		gridButton = new Button(panelX + padding, padding, buttonWidth, buttonWidth);
		drawRedSolidButton = new Button(panelX + padding + (buttonWidth + padding), padding, buttonWidth, buttonWidth);
		drawBlueSolidButton = new Button(panelX + padding + (buttonWidth + padding)*2, padding, buttonWidth, buttonWidth);
		startY = (buttonWidth + padding * 2) * 4;
		for (int i = 0; i < buttons.length; i++) {
			int xa = getButtonX(i);
			int ya = getButtonY(i);
			scrollMax = (i / buttonsPerLine);
			buttons[i] = new Image2DButton(panelX + xa, ya, buttonWidth, buttonWidth, tileSprites.getSprite(i));
		}

		scrollMax = startY + scrollMax * (buttonWidth + padding) - (Gameloop.globalHeight - (buttonWidth + padding));
		if (scrollMax < 0)
			scrollMax = 0;
	}
	
	private void resetDrawRectangle(){
		drawNewRectangle = true;
		paintedRectangle = null;
	}

	private void setDrawRedSolid(){
		drawRedSolid = true;
		drawBlueSolid = false;
	}
	
	private void setDrawBlueSolid(){
		drawBlueSolid = true;
		drawRedSolid = false;
	}
	
	private void setCurrentTile(int i){
		drawRedSolid = false;
		drawBlueSolid = false;
		currentTile = i;
	}
	
	private void updateToolButtons(int mx, int my){
		gridButton.update(mx, my);
		if (gridButton.getClicked())
			toggleGrid();
		drawRedSolidButton.update(mx, my);
		if(drawRedSolidButton.getClicked())
			setDrawRedSolid();
		drawBlueSolidButton.update(mx, my);
		if(drawBlueSolidButton.getClicked())
			setDrawBlueSolid();
	}
	
	private void paintSolid(int mx, int my){
		int xa = mx - Gameloop.camera.getOffsetX();
		int ya = my - Gameloop.camera.getOffsetY();
		if(Mouse.getMouse().isButtonPress(MouseButton.RIGHT) || mx >= panelX){
			resetDrawRectangle();
		}else if(Mouse.getMouse().isButtonPress(MouseButton.LEFT)){
			if(drawNewRectangle){
				paintedRectangle = new Rectangle(xa, ya, 1, 1);
				rectStartX = xa;
				rectStartY = ya;
				drawNewRectangle = false;
			}else {
				int ww = paintedRectangle.width;
				int hh = paintedRectangle.height;
				
				ww = xa - rectStartX;
				hh = ya - rectStartY;
				
				if(ww > 0){
					paintedRectangle.width = ww;
				}else if(ww < 0){
					paintedRectangle.width = ww*-1;
					paintedRectangle.x = xa;
				}
				
				if(hh > 0){
					paintedRectangle.height = hh;
				}else if(hh < 0){
					paintedRectangle.height = hh*-1;
					paintedRectangle.y = ya;
				}
			}
		}else {
			if(paintedRectangle != null){
				if(drawRedSolid)
					addRedRectangle(paintedRectangle);
				else if(drawBlueSolid){
					addBlueRectangle(paintedRectangle);
				}
				resetDrawRectangle();
			}
		}
	}
	
	private void paintTile(int mx, int my){
		if (mx < panelX) {
			if(!Mouse.getMouse().isButtonPress(MouseButton.LEFT))return;
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
	
	public void update() {
		if (Keyboard.getKey(KeyEvent.VK_W))
			Gameloop.camera.move(0, -panSpeed);
		if (Keyboard.getKey(KeyEvent.VK_S))
			Gameloop.camera.move(0, +panSpeed);
		if (Keyboard.getKey(KeyEvent.VK_A))
			Gameloop.camera.move(-panSpeed, 0);
		if (Keyboard.getKey(KeyEvent.VK_D))
			Gameloop.camera.move(+panSpeed, 0);
		
		int mx = Mouse.getMouseX();
		int my = Mouse.getMouseY();
		updateToolButtons(mx, my);
		scroller.update();
		for (int i = 0; i < buttons.length; i++) {
			int ya = getButtonY(i) - (int) (scroller.getScrollValue() * scrollMax);
			buttons[i].y = ya;
			if (buttons[i].y > startY - tileWidth && buttons[i].y < Gameloop.globalHeight  && my >= startY) {
				buttons[i].update(mx, my);
				if (buttons[i].getClicked()) {
					setCurrentTile(i);
				}
			}
		}
		
		
		if(drawRedSolid || drawBlueSolid){
			paintSolid(mx, my);
		}else {
			paintTile(mx, my);
		}
	}

	private void drawScroller(Renderer2D renderer) {
		scroller.draw(renderer);
	}
	
	private void drawToolButtons(Renderer2D renderer){
		gridButton.draw(renderer, 0xFF444444);
		drawRedSolidButton.draw(renderer, 0xFFFF0000);
		drawBlueSolidButton.draw(renderer, 0xFF0000FF);
	}

	public void draw(Renderer2D renderer) {
		super.draw(renderer);
		if(paintedRectangle != null){
			int color = 0xFFFF0000;
			if(drawBlueSolid)
				color = 0xFF0000FF;
			paintedRectangle.draw(renderer, color);
		}

		renderer.useCamera(false);
		renderer.fillRect(panelX, 0, panelWidth, Gameloop.globalHeight, 0xFF00FFFF);
		for (int i = 0; i < buttons.length; i++) {
			if (buttons[i].y > startY - tileWidth)
				buttons[i].draw(renderer, 0xFF333333);
		}
		renderer.fillRect(panelX, 0, panelWidth, startY, 0x5500FFFF);

		drawToolButtons(renderer);
		drawScroller(renderer);
		renderer.useCamera(true);

	}

}
