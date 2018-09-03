package com.argetgames.treasurehunter.entities;

import com.argetgames.arget2d.graphics.Image2D;
import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.arget2d.input.Mouse;
import com.argetgames.arget2d.input.Mouse.MouseButton;
import com.argetgames.treasurehunter.TreasureHunterGame;

public class Player {

	private double x, y, speed;
	private int dir = 0;
	private Projectile p;

	public Player(double startX, double startY) {
		TreasureHunterGame.player_animation[0].play(true);
		x = startX;
		y = startY;
		speed = 1.5;
	}
	
	public void setSpeed(double newSpeed) {
		speed = newSpeed;
	}

	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	private void spawnProjectile(double angle) {
		if(p == null) {
			p = new Projectile(x, y, 10, angle, 5*32);
		}
	}

	public void update() {
		if(p!= null) {
			if(p.dead)
				p = null;
			else
				p.update();
		}
		TreasureHunterGame.player_animation[dir].update();
		int mx = Mouse.getMouseX();
		int my = Mouse.getMouseY();
		double dx = mx - TreasureHunterGame.globalWidth/2;
		double dy = my - TreasureHunterGame.globalHeight/2;
		if (dx * dx + dy * dy > 1) {
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
			if(Mouse.getMouse().isButtonPress(MouseButton.LEFT)) {
				spawnProjectile(angle);
			}
		}
	}

	public void draw(Renderer2D renderer, Camera camera) {
		if(p != null) {
			p.draw(renderer, camera);
		}
		
		Image2D frame = TreasureHunterGame.player_animation[dir].getCurrentFrame();
		if(camera != null) {
			renderer.renderImage2D((int) x - frame.width/2 - (int)camera.getX(), (int) y - frame.height/2 - (int)camera.getY(), frame);
		}
		else
			renderer.renderImage2D((int) x - frame.width/2, (int) y - frame.height/2, frame);
	}

}
