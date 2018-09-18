package com.argetgames.arget2d.graphics;

public class Camera2D {
	
	private double xOffset, yOffset;

	public Camera2D(double x, double y) {
		xOffset = x;
		yOffset = y;
	}
	
	public void set(double newX, double newY){
		xOffset = newX;
		yOffset = newY;
	}
	
	public void move(double dx, double dy){
		xOffset += dx;
		yOffset += dy;
	}
	
	public int getOffsetX(){
		return -(int)xOffset;
	}
	
	public int getOffsetY(){
		return -(int)yOffset;
	}

}
