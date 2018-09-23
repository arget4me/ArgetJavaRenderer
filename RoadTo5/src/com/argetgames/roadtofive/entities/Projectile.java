package com.argetgames.roadtofive.entities;

import com.argetgames.arget2d.tilemap.Tilemap;
import com.argetgames.roadtofive.PlatformGame;

public class Projectile extends Entity {

	private double speed = 5;
	private double angle = 0;
	public boolean dead = false;
	private int lifetime = PlatformGame.global_ups * 5;
	
	public Projectile(int x, int y, double speed, double angle, Tilemap map) {
		super(x, y, 4, 4, 1.0, 1.0, map);
		applyGravity = false;
		this.speed = speed;
		this.angle = angle;
	}

	@Override
	protected void behavior() {
		if(!dead) {
			double xa = speed * Math.cos(angle);
			double ya = speed * Math.sin(angle);
			move(xa ,ya);
		}
		lifetime--;
		if(lifetime <= 0)
			dead = true;
	}

	@Override
	protected void onTileCollision() {
		dead = true;
	}

}
