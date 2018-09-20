package com.argetgames.arget2d.menu;

import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.arget2d.input.Mouse;
import com.argetgames.arget2d.input.Mouse.MouseButton;

public class Scroller extends Button{
	
	private int padding, numScrollPlaces, scrollerHeight, scrollerWidth, minY, maxY, valueRange;
	private int currentMouseY = -1, currentScrollY;
	
	public Scroller(int x, int y, int width, int height) {
		super(x,y, width, height);
		padding = width / 10;
		scrollerWidth = width - padding * 2;
		numScrollPlaces = 20;
		scrollerHeight = height / numScrollPlaces;
		minY = y + padding;
		maxY = y + height - padding - scrollerHeight;
		valueRange = maxY - minY;
		
		currentScrollY = minY;
	}

	private boolean holdPressed = false;
	public void update(){
		int mx = Mouse.getMouseX();
		int my = Mouse.getMouseY();
		super.update(mx, my);
		if(!Mouse.getMouse().isButtonPress(MouseButton.LEFT)){
			holdPressed = false;
		}
		if(holdPressed){
			pressed = true;
		}
		if(pressed){
			holdPressed = true;
			if(currentMouseY == -1){
				currentMouseY = my;
			}else {
				currentScrollY = currentMouseY - scrollerHeight/2;
				currentMouseY = my;
				if(currentScrollY < minY)currentScrollY = minY;
				else if(currentScrollY > maxY)currentScrollY = maxY;
			}
		}else{
			currentMouseY = -1;
		}
	}
	
	public double getScrollValue(){
		return (double)(currentScrollY - minY)/(double)valueRange;
	}
	
	public void draw(Renderer2D renderer){
		super.draw(renderer, 0xFF999999);
		renderer.fillRect(x+padding, currentScrollY, scrollerWidth, scrollerHeight, 0xFF444444);
	}
}
