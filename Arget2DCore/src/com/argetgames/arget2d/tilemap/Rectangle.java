package com.argetgames.arget2d.tilemap;

import com.argetgames.arget2d.graphics.Renderer2D;

public class Rectangle {

	public int x, y, width, height;
	
	
	public Rectangle(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	protected boolean containsPoint(int xa, int ya){
		if(xa >= x && xa < x + width){
			if(ya >= y && ya < y + height){
				return true;
			}
		}
		return false;
	}
	
	protected int getCenterX(){
		return x + width/2;
	}
	
	protected int getCenterY(){
		return y + height/2;
	}
	
	public void draw(Renderer2D renderer, int color){
		renderer.fillRect(x, y, width, height, color);
	}
	
	public boolean collision(Rectangle other){
		if(Math.abs(getCenterX() - other.getCenterX()) > width + other.width)return false;
		if(Math.abs(getCenterY() - other.getCenterY()) > height + other.height)return false;
		
		//top left
		if(containsPoint(other.x, other.y))return true;
		
		//top right
		if(containsPoint(other.x + other.width, other.y))return true;
		
		//bottom left
		if(containsPoint(other.x, other.y + other.width))return true;
		
		//bottom right
		if(containsPoint(other.x + other.width, other.y + other.height))return true;
		
		//check if inside other
		//top left
		if(other.containsPoint(x, y))return true;
		
		//top right
		if(other.containsPoint(x + width, y))return true;
		
		//bottom left
		if(other.containsPoint(x, y + height))return true;
		
		//bottom right
		if(other.containsPoint(x + width, y + height))return true;
		
		return false;
	}

}
