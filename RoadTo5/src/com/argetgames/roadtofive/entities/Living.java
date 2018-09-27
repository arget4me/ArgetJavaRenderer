package com.argetgames.roadtofive.entities;

import com.argetgames.arget2d.graphics.Renderer2D;

public abstract class Living extends Entity {
	
	public boolean dead = false;
	protected int maxHealth = 100;
	protected int health = maxHealth;
	protected double moveSpeed = 1.0;

	public Living(int x, int y, int width, int height, Level level) {
		super(x, y, width, height, 16 * 4.0, 1.0 / 2.7, level);
	}

	protected void hurt(int amount) {
		health -= amount;
		if(health <= 0)
			dead = true;
	}

	@Override
	protected void onTileCollision() {
		//DO NOTHING
	}
	
	protected void drawHealthBar(Renderer2D renderer) {
		renderer.useCamera(true);
		if(health < maxHealth) {
			renderer.fillRect(x, y-5, width, 4, 0xFFFF0000);
			if(health < 0)health = 0;
			int healthWidth = (int)Math.ceil(width*(double)health/ (double) maxHealth);
			renderer.fillRect(x, y-5, healthWidth, 4, 0xFF00FF00);
		}
		renderer.useCamera(false);
	}
	
	public void draw(Renderer2D renderer) {
		super.draw(renderer);
		drawHealthBar(renderer);
	}
	
	public void draw(Renderer2D renderer, int color) {
		super.draw(renderer, color);
		drawHealthBar(renderer);
	}

}
