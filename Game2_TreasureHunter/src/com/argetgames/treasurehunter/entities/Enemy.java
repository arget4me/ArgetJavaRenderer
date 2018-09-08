package com.argetgames.treasurehunter.entities;

import com.argetgames.arget2d.graphics.Image2D;
import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.arget2d.input.Mouse;
import com.argetgames.arget2d.input.Mouse.MouseButton;
import com.argetgames.treasurehunter.TreasureHunterGame;

public class Enemy {

	private double x, y, speed;
	private int dir = 0;
	
	public Enemy(double startX, double startY) {
		x = startX;
		y = startY;
		speed = 0.5;
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
	
	public void update(Player player) {
		double dx = TreasureHunterGame.netX - x;
		double dy = TreasureHunterGame.netY - y;
		x = TreasureHunterGame.netX;
		y = TreasureHunterGame.netY;
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
		
		/*
		double mx = player.getX();
		double my = player.getY();
		double dx = mx - x;
		double dy = my - y;
		double dist = dx * dx + dy * dy;
		if (dist > 1 && dist < (32*4)*(32*4)) {
			double angle = Math.atan2(dy, dx);
			double ax = speed * Math.cos(angle);
			double ay = speed * Math.sin(angle);
			x += ax;
			y += ay;
			if(Math.abs(dx) > Math.abs(dy)) {
				if(ax < 0) {
					dir = 1;
				}else {
					dir = 2;
				}
			}else {
				if(ay < 0) {
					dir = 3;
				}else {
					dir = 0;
				}
			}
		}*/
	}
	
	public void draw(Renderer2D renderer, Camera camera) {
		Image2D frame = TreasureHunterGame.player_animation[dir].getCurrentFrame();
		renderer.renderImage2D((int)getX() - (int)camera.getX(), (int)getY() - (int)camera.getY(), getWidth(), getHeight(), frame);
		renderer.fillRect((int)getX() - (int)camera.getX(), (int)getY() - (int)camera.getY(), getWidth(), getHeight(), 0x88FFFF00);
	}

}
