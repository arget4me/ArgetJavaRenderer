package com.argetgames.treasurehunter.entities;

import com.argetgames.arget2d.graphics.Image2D;
import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.arget2d.input.Mouse;
import com.argetgames.arget2d.input.Mouse.MouseButton;
import com.argetgames.treasurehunter.TreasureHunterGame;

public class Player {

	private double x, y, speed;
	private int dir = 0;
	public Projectile p;
	public int numProjectiles = 0;
	
	public int maxLife = 20, life = maxLife;
	public CollisionBox boundary;

	public Player(double startX, double startY) {
		TreasureHunterGame.player_animation[0].play(true);
		x = startX;
		y = startY;
		speed = 1.5;
		boundary = new CollisionBox((int)x-8, (int)y-16, 16, 32);
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
		if (p == null) {
			numProjectiles = 1;
			p = new Projectile(x, y, 32, angle, 1 * 4);
		}
	}
	
	public void hurt() {
		life--;
		System.out.println(life);
	}
	
	public void update() {
		if (p != null) {
			if (p.dead) {
				numProjectiles = 0;
				p = null;
			}else
				p.update();
		}
//		TreasureHunterGame.player_animation[dir].update();

		int mx = Mouse.getMouseX();
		int my = Mouse.getMouseY();
		double dx = mx - TreasureHunterGame.globalWidth / 2;
		double dy = my - TreasureHunterGame.globalHeight / 2;
		if (dx * dx + dy * dy > 1) {
			double angle = Math.atan2(dy, dx);
			double ax = speed * Math.cos(angle);
			double ay = speed * Math.sin(angle);
			if (Mouse.getMouse().isButtonPress(MouseButton.RIGHT)) {
				x += ax;
				y += ay;
				boundary.setPos((int) x, (int) y);
			}
			if (Math.abs(dx) > Math.abs(dy)) {
				if (ax < 0) {
					dir = 1;
				} else {
					dir = 2;
				}
			} else {
				if (ay < 0) {
					dir = 3;
				} else {
					dir = 0;
				}
			}
			if (Mouse.getMouse().isButtonPress(MouseButton.LEFT)) {
				spawnProjectile(angle);
			}
		}
	}

	public void draw(Renderer2D renderer, Camera camera) {
		if (p != null) {
			p.draw(renderer, camera);
		}

		
		Image2D frame = TreasureHunterGame.player_animation[dir].getCurrentFrame();
		if (camera != null) {
			renderer.fillRect((int) x - frame.width / 2 - (int) camera.getX(), - 6 +
					(int) y - frame.height / 2 - (int) camera.getY(), frame.width, 4, 0xFF880000);
			renderer.fillRect((int) x - frame.width / 2 - (int) camera.getX(), - 6 +
					(int) y - frame.height / 2 - (int) camera.getY(), (int)((life / (double)maxLife) * frame.width), 4, 0xFF00FF00);
			renderer.renderImage2D((int) x - frame.width / 2 - (int) camera.getX(),
					(int) y - frame.height / 2 - (int) camera.getY(), frame);
		} else
			renderer.renderImage2D((int) x - frame.width / 2, (int) y - frame.height / 2, frame);
	}

	public boolean collision(Projectile p2) {
		return boundary.collision(p2.boundary);
	}

}
