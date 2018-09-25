package com.argetgames.roadtofive.entities;

import java.util.ArrayList;

import com.argetgames.roadtofive.PlatformGame;

public class Projectile extends Entity {

	protected double speed = 5;
	protected double angle = 0;
	public boolean dead = false;
	private int lifetime = PlatformGame.global_ups * 1;
	protected int parentID = -1;
	private ArrayList<Living> collisions = null;
	protected int DMG = 1;
	
	public Projectile(int x, int y, double speed, double angle, int DMG, Level level, int parentID) {
		super(x, y, 4, 4, 1.0, 1.0, level);
		applyGravity = false;
		this.speed = speed;
		this.angle = angle;
		this.parentID = parentID;
		this.DMG = DMG;
	}
	
	public Projectile(int x, int y, double speed, double angle, int DMG, double lifetimeSeconds, Level level, int parentID) {
		super(x, y, 4, 4, 1.0, 1.0, level);
		applyGravity = false;
		this.speed = speed;
		this.angle = angle;
		this.parentID = parentID;
		this.DMG = DMG;
		lifetime = (int)(PlatformGame.global_ups * lifetimeSeconds);
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
	
	public void onDynamicCollision(Living l) {
		l.hurt(DMG);
	}
	
	public void onDynamicCollision() {
		for(Living l : collisions) {
			onDynamicCollision(l);
		}
		dead = true;
	}
	
	public boolean handleCollisionDynamic(int dx, int dy) {
		collisions = level.checkCollisionDynamic(this, parentID, dx, dy);
		return (collisions.size() > 0);
	}
	
	@Override
	protected void onTileCollision() {
		dead = true;
	}

}
