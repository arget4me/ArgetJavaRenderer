package com.argetgames.treasurehunter.entities;

public class CollisionBox {
	
	protected int x, y, width, height;
	public CollisionBox(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void setPos(int newX, int newY) {
		this.x = newX;
		this.y = newY;
	}
	
	protected boolean containsPoint(int xp, int yp) {
		if(yp >= y && yp < y + height ) {
			if(xp >= x && xp < x + width ) {
				return true;
			}
		}
		return false;
	}
	
	public boolean collision(CollisionBox otherBox) {
		//check top left
		if(containsPoint(otherBox.x, otherBox.y))
			return true;
		
		//check top right
		if(containsPoint(otherBox.x + otherBox.width, otherBox.y))
			return true;
		
		//check bottom left
		if(containsPoint(otherBox.x, otherBox.y + otherBox.width))
			return true;
		
		//check bottom right
		if(containsPoint(otherBox.x + otherBox.width, otherBox.y + otherBox.width))
			return true;
		
		/** check is inside other box **/
		//check top left
		if (otherBox.containsPoint(x, y))
			return true;

		// check top right
		if (otherBox.containsPoint(x + width, y))
			return true;

		// check bottom left
		if (otherBox.containsPoint(x, y + width))
			return true;

		// check bottom right
		if (otherBox.containsPoint(x + width, y + width))
			return true;
		
		return false;
	}

}
