package com.argetgames.arget2d.menu;

import com.argetgames.arget2d.graphics.Renderer2D;

public class Rectangle {

	public int x, y, width, height;
	
	
	public Rectangle(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public boolean containsPoint(int xa, int ya){
		if(xa >= x && xa < x + width){
			if(ya >= y && ya < y + height){
				return true;
			}
		}
		return false;
	}
	
	public int getCenterX(){
		return x + width/2;
	}
	
	public int getCenterY(){
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
		if(containsPoint(other.x + other.width-1, other.y))return true;
		
		//bottom left
		if(containsPoint(other.x, other.y + other.height -1))return true;
		
		//bottom right
		if(containsPoint(other.x + other.width - 1, other.y + other.height -1))return true;
		
		//check if inside other
		//top left
		if(other.containsPoint(x, y))return true;
		
		//top right
		if(other.containsPoint(x + width -1, y))return true;
		
		//bottom left
		if(other.containsPoint(x, y + height -1))return true;
		
		//bottom right
		if(other.containsPoint(x + width -1, y + height -1))return true;
		
		return false;
	}

}
