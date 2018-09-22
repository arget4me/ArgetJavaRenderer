package com.argetgames.arget2d.tilemap;

import java.awt.event.KeyEvent;

import javax.swing.JFileChooser;

import com.argetgames.arget2d.game.Gameloop;
import com.argetgames.arget2d.graphics.Image2D;
import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.arget2d.graphics.SpriteSheet;
import com.argetgames.arget2d.input.Keyboard;
import com.argetgames.arget2d.input.Mouse;
import com.argetgames.arget2d.input.Mouse.MouseButton;
import com.argetgames.arget2d.menu.Image2DButton;
import com.argetgames.arget2d.menu.Rectangle;
import com.argetgames.arget2d.menu.Scroller;

public class TilemapEditor extends Tilemap {

	private int panelWidth;
	private int panelX;
	private Image2DButton[] buttons;
	private Image2DButton gridButton, drawRedSolidButton, drawBlueSolidButton, editSolids, toggleShowSolids, erasorButton;
	private Image2DButton testButton, saveButton, loadButton;
	private static Image2D
	gridImg, redRectImg, blueBlueImg, editRectImg, showHideImg, eraserImg,
	playImg, saveImg, loadImg;
	private int buttonsPerLine, padding, scrollerSize, buttonWidth, scrollMax, startY;
	private Scroller scroller;
	private double panSpeed = 4.5;

	//Editor logic
	private int currentTile = 0;
	private boolean drawRedSolid = false, drawBlueSolid = false, editingSolids = false;

	private int rectStartX, rectStartY;
	private boolean drawNewRectangle = true;
	private Rectangle paintedRectangle = null, editingRectangle = null;
	private Rectangle[] selectionCorners = new Rectangle[4];
	private int rectangleIndex = -1;
	
	//Test play
	private boolean testPlaying = false;
	private Rectangle testPlayer;
	
	
	//auto draw rectangles
	private SpriteSheet solidMap;
	
	public TilemapEditor(int width, int height, int tileWidth, int tileHeight, SpriteSheet tileSprites) {
		super(width, height, tileWidth, tileHeight, tileSprites);
		int numSprites = tileSprites.getNumSprites();
		buttons = new Image2DButton[numSprites];
		gridImg = new Image2D("/images/grid.png", true);
		redRectImg = new Image2D("/images/redRect.png", true);
		blueBlueImg = new Image2D("/images/blueRect.png", true);
		editRectImg = new Image2D("/images/editRect.png", true);
		showHideImg = new Image2D("/images/showHide.png", true);
		eraserImg = new Image2D("/images/eraser.png", true);
		
		playImg = new Image2D("/images/play.png", true);
		saveImg = new Image2D("/images/save.png", true);
		loadImg = new Image2D("/images/load.png", true);
		testPlayer = new Rectangle(0, 0, tileWidth, tileHeight);
		for(int i = 0; i < selectionCorners.length; i++){
			selectionCorners[i] = new Rectangle(0, 0, 1, 1);
		}
		calculateSizes();
	}
	
	public void addSolidMap(SpriteSheet solidMap){
		this.solidMap = solidMap;
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
		
		//tool buttons:
		//row 0
		gridButton = new Image2DButton(panelX + padding, padding, buttonWidth, buttonWidth, gridImg);
		drawRedSolidButton = new Image2DButton(panelX + padding + (buttonWidth + padding), padding, buttonWidth, buttonWidth, redRectImg);
		drawBlueSolidButton = new Image2DButton(panelX + padding + (buttonWidth + padding)*2, padding, buttonWidth, buttonWidth, blueBlueImg);
		editSolids = new Image2DButton(panelX + padding + (buttonWidth + padding)*3, padding, buttonWidth, buttonWidth, editRectImg);
		toggleShowSolids = new Image2DButton(panelX + padding + (buttonWidth + padding)*4, padding, buttonWidth, buttonWidth, showHideImg);
		erasorButton = new Image2DButton(panelX + padding + (buttonWidth + padding)*5, padding, buttonWidth, buttonWidth, eraserImg);
		
		//row 1
		testButton = new Image2DButton(panelX + padding, padding + (buttonWidth + padding), buttonWidth, buttonWidth, playImg);
		saveButton = new Image2DButton(panelX + padding + (buttonWidth + padding), padding + (buttonWidth + padding), buttonWidth, buttonWidth, saveImg);
		loadButton = new Image2DButton(panelX + padding + (buttonWidth + padding) * 2, padding + (buttonWidth + padding), buttonWidth, buttonWidth, loadImg);
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
		showSolids = true;
		drawBlueSolid = false;
		editingSolids = false;
		drawRedSolid = true;
		editingRectangle = null;
	}
	
	private void setDrawBlueSolid(){
		showSolids = true;
		drawRedSolid = false;
		editingSolids = false;
		drawBlueSolid = true;
		editingRectangle = null;
	}
	
	private void setEditingSolids(){
		showSolids = true;
		drawRedSolid = false;
		drawBlueSolid = false;
		editingSolids = true;
		editingRectangle = null;
	}
	
	private void setCurrentTile(int i){
		drawRedSolid = false;
		drawBlueSolid = false;
		editingSolids = false;
		editingRectangle = null;
		currentTile = i;
	}
	
	private void updateToolButtons(int mx, int my){
		testButton.update(mx, my);
		if(testButton.getClicked()){
			setCurrentTile(0);
			testPlaying = true;
			return;
		}
		
		saveButton.update(mx, my);
		if(saveButton.getClicked()){
			setCurrentTile(0);
			final JFileChooser fc = new JFileChooser();
			fc.showOpenDialog(null);
			write(fc.getSelectedFile());
		}
		
		loadButton.update(mx, my);
		if(loadButton.getClicked()){
			setCurrentTile(0);
			final JFileChooser fc = new JFileChooser();
			fc.showOpenDialog(null);
			load(fc.getSelectedFile());
		}
		
		gridButton.update(mx, my);
		if (gridButton.getClicked())
			toggleGrid();
		drawRedSolidButton.update(mx, my);
		if(drawRedSolidButton.getClicked())
			setDrawRedSolid();
		drawBlueSolidButton.update(mx, my);
		if(drawBlueSolidButton.getClicked())
			setDrawBlueSolid();
		editSolids.update(mx, my);
		if(editSolids.getClicked())
			setEditingSolids();
		toggleShowSolids.update(mx, my);
		if(toggleShowSolids.getClicked()){
			editingRectangle = null;
			toggleShowSolids();
		}
		erasorButton.update(mx, my);
		if(erasorButton.getClicked())
			setCurrentTile(-1);
	}
	
	private void placeCorners(){
		selectionCorners[0].x = editingRectangle.x;
		selectionCorners[0].y = editingRectangle.y;
		
		selectionCorners[1].x = editingRectangle.x + editingRectangle.width - 1;
		selectionCorners[1].y = editingRectangle.y;
		
		selectionCorners[2].x = editingRectangle.x;
		selectionCorners[2].y = editingRectangle.y + editingRectangle.height - 1;
		
		selectionCorners[3].x = editingRectangle.x + editingRectangle.width- 1;
		selectionCorners[3].y = editingRectangle.y + editingRectangle.height - 1;
	}
	
	private void editSolids(int mx, int my) {
		if(Mouse.getMouse().isButtonPress(MouseButton.LEFT)){
			int xa = mx - Gameloop.camera.getOffsetX();
			int ya = my - Gameloop.camera.getOffsetY();
			if(editingRectangle == null){
				for(int i = 0; i < redRectangles.length; i++){
					if(redRectangles[i].containsPoint(xa, ya)){
						editingRectangle = redRectangles[i];
						placeCorners();
						return;
					}
				}
				for(int i = 0; i < blueRectangles.length; i++){
					if(blueRectangles[i].containsPoint(xa, ya)){
						editingRectangle = blueRectangles[i];
						placeCorners();
						return;
					}
				}
				
			}else {
				if(rectangleIndex == -1){
					for(int i = 0; i < selectionCorners.length; i++){
						if(selectionCorners[i].containsPoint(xa, ya)){
							rectangleIndex = i;
							rectStartX = xa;
							rectStartY = ya;
							break;
						}
					}
				}
				if(rectangleIndex > -1){
					int dx = rectStartX - xa;
					int dy = rectStartY - ya;
					rectStartX = xa;
					rectStartY = ya;
					
					if(rectangleIndex == 0){
						editingRectangle.x = xa;
						editingRectangle.width += dx;
	
						editingRectangle.y = ya;
						editingRectangle.height += dy;
					}else if(rectangleIndex == 1){
						editingRectangle.width += -dx;
	
						editingRectangle.y = ya;
						editingRectangle.height += dy;
					}else if(rectangleIndex == 2){
						editingRectangle.x = xa;
						editingRectangle.width += dx;
	
						editingRectangle.height += -dy;
					}else if(rectangleIndex == 3){
						editingRectangle.width += -dx;
						editingRectangle.height += -dy;
					}
					

					
					//adjust negative size;
					if(editingRectangle.width <= 1){
						editingRectangle.width = 2;
					}
					
					if(editingRectangle.height <= 1){
						editingRectangle.height = 2;
					}
					
					placeCorners();

				}
			}
		}else {
			rectangleIndex = -1;
			if(Mouse.getMouse().isButtonPress(MouseButton.RIGHT)){
				editingRectangle = null;
			}else if(Keyboard.getKey(KeyEvent.VK_DELETE)){
				if(editingRectangle != null){
					removeRectangle(editingRectangle);
					editingRectangle = null;
				}
			}
		}
	}
	
	private void paintSolid(int mx, int my){
		int xa = mx - Gameloop.camera.getOffsetX();
		int ya = my - Gameloop.camera.getOffsetY();
		if(Mouse.getMouse().isButtonPress(MouseButton.RIGHT) || mx >= panelX){
			resetDrawRectangle();
		}else if(Mouse.getMouse().isButtonPress(MouseButton.LEFT)){
			if(drawNewRectangle){
				paintedRectangle = new Rectangle(xa, ya, 2, 2);
				rectStartX = xa;
				rectStartY = ya;
				drawNewRectangle = false;
			}else {
				int ww = xa - rectStartX;
				int hh = ya - rectStartY;
				
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
				
				if(paintedRectangle.width <= 1)
					paintedRectangle.width = 2;
				
				if(paintedRectangle.height <= 1)
					paintedRectangle.height = 2;
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
	
	private void autoSolidRectangle(int xT, int yT, int index){
		if(solidMap == null) return;
	
		Image2D solidImg = solidMap.getSprite(index);
		xT *= solidImg.width;
		yT *= solidImg.height;
		removeRectangle(xT, yT, solidImg.width, solidImg.height);
		if(index != -1) {
			int xMin = -1, yMin = -1, xMax = -1, yMax = -1;
			int solidColor = -1;
			for(int y = 0; y < solidImg.height; y++){
				for(int x = 0; x < solidImg.width; x++){
					int color = solidImg.getColor(x + y * solidImg.width);
					if(solidColor == -1){
						if(color == 0xFFFF0000 || color == 0xFF0000FF){
							solidColor = color;
							xMin = x;
							yMin = y;
							xMax = x;
							yMax = y;
						}
					}else {
						if(color == solidColor){
							if(x > xMax)
								xMax = x;
							if(x < xMin)
								xMin = x;
							yMax = y;
						}
					}
				}
			}
			if(solidColor != -1){
				int ww = xMax - xMin + 1;
				int hh = yMax - yMin + 1;
				if(solidColor == 0xFFFF0000){
					addRedRectangle(new Rectangle(xT + xMin, yT + yMin, ww, hh));
				}else if(solidColor == 0xFF0000FF){
					addBlueRectangle(new Rectangle(xT + xMin, yT + yMin, ww, hh));
				}
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
					if(tiles[xT + yT * numTilesWide] != currentTile){
						tiles[xT + yT * numTilesWide] = currentTile;
						autoSolidRectangle(xT, yT, currentTile);
					}
				}
			}
		}
	}
	
	public void update() {
		int mx = Mouse.getMouseX();
		int my = Mouse.getMouseY();
		if(!testPlaying){
			if (Keyboard.getKey(KeyEvent.VK_W))
				Gameloop.camera.move(0, -panSpeed);
			if (Keyboard.getKey(KeyEvent.VK_S))
				Gameloop.camera.move(0, +panSpeed);
			if (Keyboard.getKey(KeyEvent.VK_A))
				Gameloop.camera.move(-panSpeed, 0);
			if (Keyboard.getKey(KeyEvent.VK_D))
				Gameloop.camera.move(+panSpeed, 0);
			
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
			
			if(editingSolids){
				editSolids(mx, my);
			}else if(drawRedSolid || drawBlueSolid){
				paintSolid(mx, my);
			}else {
				paintTile(mx, my);
			}
		}else {
			testButton.update(mx, my);
			int xa = 0, ya = 0;
			if (Keyboard.getKey(KeyEvent.VK_W))
				ya += -1;
			if (Keyboard.getKey(KeyEvent.VK_S))
				ya += +1;
			if (Keyboard.getKey(KeyEvent.VK_A))
				xa += -1;
			if (Keyboard.getKey(KeyEvent.VK_D))
				xa += +1;
			

			if(xa != 0){
				if(!checkCollision(testPlayer, xa, 0)){
					testPlayer.x += xa;
				}
			}
			if(ya != 0){	
				if(!checkCollision(testPlayer, 0, ya)){
					testPlayer.y += ya;
				}
			}
			
			Gameloop.camera.set(testPlayer.x - Gameloop.globalWidth/2, testPlayer.y - Gameloop.globalHeight/2);
			if(testButton.getClicked()){
				testPlaying = false;
			}
		}
	}

	private void drawScroller(Renderer2D renderer) {
		scroller.draw(renderer);
	}
	
	private void drawToolButtons(Renderer2D renderer){
		gridButton.draw(renderer, 0xFF444444);
		drawRedSolidButton.draw(renderer, 0xFFFF0000);
		drawBlueSolidButton.draw(renderer, 0xFF0000FF);
		editSolids.draw(renderer, 0xFF00FF00);
		toggleShowSolids.draw(renderer, 0xFFFFFF00);
		erasorButton.draw(renderer, 0xFF6666);
		testButton.draw(renderer, 0xFF444444);
		saveButton.draw(renderer, 0xFFFF0000);
		loadButton.draw(renderer, 0xFF0000FF);
	}

	public void draw(Renderer2D renderer) {
		super.draw(renderer);

		if(paintedRectangle != null){
			int color = 0xFFFF0000;
			if(drawBlueSolid)
				color = 0xFF0000FF;
			paintedRectangle.draw(renderer, color);
		}
		
		if(editingRectangle != null){
			renderer.drawRect(editingRectangle.x, editingRectangle.y, editingRectangle.width, editingRectangle.height, 0xFF008800);
			for(int i = 0; i < selectionCorners.length; i++){
				selectionCorners[i].draw(renderer, 0xFF00FF00);
			}
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
		if(testPlaying){
			testPlayer.draw(renderer, 0xFF00FF00);
		}
	}

}
