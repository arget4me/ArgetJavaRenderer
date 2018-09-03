package com.argetgames.treasurehunter.entities;

import com.argetgames.arget2d.graphics.Renderer2D;

public class Projectile {
	
	private double x, y, speed, angle, distance;
	public boolean dead = false;
	
	public Projectile(double x, double y, double speed, double angle, double distance) {
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.angle = angle;
		this.distance = distance;
	}
	
	public void update() {
		if(distance <= 0)
			dead = true;
		double ax = speed * Math.cos(angle);
		double ay = speed * Math.sin(angle);
		x += ax;
		y += ay;
		distance -= speed;
	}
	
	public double getX() {
		return x - getWidth()/2;
	}
	
	public double getY() {
		return y - getHeight()/2;
	}
	
	public int getWidth() {
		return 10;
	}
	
	public int getHeight() {
		return 10;
	}
	
	public void draw(Renderer2D renderer, Camera camera) {
		renderer.fillRect((int)getX() - (int)camera.getX(), (int)getY() - (int)camera.getY(), getWidth(), getHeight(), 0x88FFFF00);
	}

}
