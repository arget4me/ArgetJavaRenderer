package com.argetgames.arget2d.menu;

import com.argetgames.arget2d.graphics.Image2D;
import com.argetgames.arget2d.graphics.Renderer2D;

public class Image2DButton extends Button {
	private Image2D image;

	public Image2DButton(int x, int y, int width, int height, Image2D image) {
		super(x, y, width, height);
		this.image = image;
	}
	
	
	public void draw(Renderer2D renderer){
		super.draw(renderer);
		renderer.renderImage2D(x + 1, y + 1, width - 2, height - 2,image);
	}
	
	public void draw(Renderer2D renderer, int color){
		super.draw(renderer, color);
		renderer.renderImage2D(x + 1, y + 1, width - 2, height - 2,image);
		if(pressed){
			renderer.useAlpha(true);
			renderer.useColorMask(false);
			super.draw(renderer, 0x44000000);
			renderer.useAlpha(false);
			renderer.useColorMask(true);
		}
	}

}
