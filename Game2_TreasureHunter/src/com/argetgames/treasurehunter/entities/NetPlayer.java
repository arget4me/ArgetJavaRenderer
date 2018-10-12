package com.argetgames.treasurehunter.entities;

import com.argetgames.arget2d.graphics.Image2D;
import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.arget2d.input.Mouse;
import com.argetgames.arget2d.input.Mouse.MouseButton;
import com.argetgames.treasurehunter.TreasureHunterGame;

public class NetPlayer {

	private double x, y, speed;
	private int dir = 0;
	public int ID = -1;
	
	public double netX = 0.0, netY = 0.0;
	private long time;
	
	public boolean drop = false;
	public Projectile p;
	public int numProjectiles = 0;
	
	public boolean hasHit = false;
	
	public NetPlayer(int ID, double startX, double startY) {
		x = netX = startX;
		y = netY = startY;
		this.ID = ID;
		speed = 1.5;
		time = System.currentTimeMillis();
	}
	
	public double getX() {
		return x - getWidth()/2;
	}
	
	public double getY() {
		return y - getHeight()/2;
	}
	
	public int getWidth() {
		return 32;
	}
	
	public int getHeight() {
		return 32;
	}
	
	public void update() {
		if(System.currentTimeMillis() - time >= 1000) {
			drop = true;
		}
		double dx = netX - x;
		double dy = netY - y;
		x = netX;
		y = netY;
		if(dx != 0 && dy != 0)
		if(Math.abs(dx) > Math.abs(dy)) {
			if(dx < 0) {
				dir = 1;
			}else {
				dir = 2;
			}
		}else {
			if(dy < 0) {
				dir = 3;
			}else {
				dir = 0;
			}
		}
	}
	
	public void draw(Renderer2D renderer, Camera camera) {
		if (numProjectiles > 0) {
			p.draw(renderer, camera);
		}
		
		Image2D frame = TreasureHunterGame.player_animation[dir].getCurrentFrame();
		renderer.renderImage2D((int)getX() - (int)camera.getX(), (int)getY() - (int)camera.getY(), getWidth(), getHeight(), frame);
		renderer.fillRect((int)getX() - (int)camera.getX(), (int)getY() - (int)camera.getY(), getWidth(), getHeight(), 0x88FFFF00);
	}

	public void newInput(double netX, double netY) {
		this.netX = netX;
		this.netY = netY;
		time = System.currentTimeMillis();
	}

}
