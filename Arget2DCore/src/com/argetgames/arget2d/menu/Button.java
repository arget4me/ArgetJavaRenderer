package com.argetgames.arget2d.menu;

import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.arget2d.input.Mouse;
import com.argetgames.arget2d.input.Mouse.MouseButton;
import com.argetgames.arget2d.tilemap.Rectangle;

public class Button extends Rectangle {

	public Button(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	protected boolean pressed = false;
	protected boolean clicked = false;

	public void update(int x, int y) {
		boolean contains = containsPoint(x, y);
		if (Mouse.getMouse().isButtonPress(MouseButton.LEFT)) {
				pressed = contains;
		} else {
			if(contains && pressed) {
				clicked = true;
			}
			pressed = false;
		}
	}
	
	public boolean getPressed(){
		return pressed;
	}

	public boolean getClicked() {
		boolean value = clicked;
		clicked = false;
		return value;
	}
	
	public void draw(Renderer2D renderer, int color){
		if(clicked)
			super.draw(renderer, color);
		else if(pressed)
			super.draw(renderer, color);		
		else
			super.draw(renderer, color);
		
		
	}
	
	public void draw(Renderer2D renderer){
		if(clicked)
			super.draw(renderer, 0xFFFFFF00);
		else if(pressed)
			super.draw(renderer, 0xFF00FF00);
		else
			super.draw(renderer, 0xFF0000FF);
	}

}
