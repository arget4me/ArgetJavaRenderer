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
	
	public void follow(double newX, double newY, double maxOffset)
	{
		double dx = newX - xOffset;
		double dy = newY - yOffset;
		double angle = Math.atan2(dy, dx);
		double minSpeed = 2;
		double maxSpeed = 2;
		double length = Math.sqrt(dx*dx + dy*dy);
		double panSpeed = (maxSpeed) * length / maxOffset;
		
		if(Math.abs(dx) <= 1)
		{
			set(newX, yOffset);
		}else
		{
			if(Math.abs(dx) >= maxOffset){
				if(dx > 0)
					xOffset += (dx - maxOffset) * 1.01;
				else
					xOffset += (dx + maxOffset) * 1.01;
			}else
			{
				double xSpeed = panSpeed * Math.cos(angle);
				if(Math.abs(xSpeed) < minSpeed){
					if(xSpeed < 0)
						xSpeed = -minSpeed;
					else 
						xSpeed = minSpeed;
				}
				xOffset += xSpeed;
			}
		}
		if(Math.abs(dy) <= 1)
		{
			set(xOffset, newY);
		}else
		{	
			
			if(Math.abs(dy) >= maxOffset)
			{
				if(dy > 0)
					yOffset += (dy - maxOffset) * 1.01;
				else 
					yOffset += (dy + maxOffset) * 1.01;
			}else
			{
				double ySpeed = panSpeed * Math.sin(angle);
				if(Math.abs(ySpeed) < minSpeed){
					if(ySpeed < 0)
						ySpeed = -minSpeed;
					else 
						ySpeed = minSpeed;
				}
				yOffset += ySpeed;
			}
		}
		
		
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
