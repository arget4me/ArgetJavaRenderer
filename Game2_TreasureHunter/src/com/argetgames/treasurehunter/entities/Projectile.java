package com.argetgames.treasurehunter.entities;

import com.argetgames.arget2d.graphics.Renderer2D;

public class Projectile {

	private double x, y, speed, angle, distance;
	public CollisionBox boundary;
	public boolean dead = false;

	/**
	 * 
	 * @param x Start x
	 * @param y Start y
	 * @param speed Speed to increment with each update
	 * @param angle Angle form the positive x-axis in radians.
	 * @param distance How many multiples of speed should it travel.
	 */
	public Projectile(double x, double y, double speed, double angle, double distance) {
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.angle = angle;
		this.distance = distance * speed;
		boundary = new CollisionBox((int)getDrawX(), (int)getDrawY(), getWidth(), getHeight());
	}

	public void update() {
		if (distance <= 0)
			dead = true;
		else {
			double ax = speed * Math.cos(angle);
			double ay = speed * Math.sin(angle);
			x += ax;
			y += ay;
			distance -= speed;
		}
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public int getWidth() {
		return 10;
	}

	public int getHeight() {
		return 10;
	}

	public double getDrawX() {
		return x - getWidth() / 2;
	}

	public double getDrawY() {
		return y - getHeight() / 2;
	}

	public void draw(Renderer2D renderer, Camera camera) {
		renderer.fillRect((int) getDrawX() - (int) camera.getX(), (int) getDrawY() - (int) camera.getY(), getWidth(),
				getHeight(), 0x88FFFF00);
	}

	public void setPos(double newX, double newY) {
		x = newX;
		y = newY;
		boundary.setPos((int)x, (int)y);
	}

}
