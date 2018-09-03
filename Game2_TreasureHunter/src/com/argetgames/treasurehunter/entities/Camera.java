package com.argetgames.treasurehunter.entities;

import com.argetgames.treasurehunter.TreasureHunterGame;

public class Camera {

	private Player target;
	private double x, y;
	
	public Camera(Player target) {
		this.target = target;
		moveTo(target.getX(), target.getY());
	}
	
	private void moveTo(double newX, double newY) {
		x = newX;
		y = newY;
	}
	
	public double getX() {
		return x - TreasureHunterGame.globalWidth/2;
	}
	
	public double getY() {
		return y - TreasureHunterGame.globalHeight/2;
	}
	
	public void update() {
		x = target.getX();
		y = target.getY();
	}
}
