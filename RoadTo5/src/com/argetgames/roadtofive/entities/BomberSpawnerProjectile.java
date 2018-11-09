package com.argetgames.roadtofive.entities;

import com.argetgames.arget2d.graphics.Animation2D;
import com.argetgames.arget2d.graphics.Image2D;
import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.roadtofive.PlatformGame;

public class BomberSpawnerProjectile extends Projectile{

	private final static int BOMBER_DAMAGE = 0;
	private final static double BOMBER_SHOOT_SPEED = 2.5;
	
	public BomberSpawnerProjectile(int x, int y, double angle, Level level, int parentID) {
		super(x-4, y-4, BOMBER_SHOOT_SPEED, angle, BOMBER_DAMAGE, level, parentID);	
	}
	
	public BomberSpawnerProjectile(int x, int y, double angle, double lifetimeSeconds, Level level,
			int parentID) {
		super(x-4, y-4, BOMBER_SHOOT_SPEED, angle, BOMBER_DAMAGE, lifetimeSeconds, level, parentID);
	}
	
	protected void init() {
		width  += 8;
		height  += 8;
		applyGravity = true;
		calculateJump(16 * 3.0, 1.0 / 3.0);
		startJump();
		{
			int frames[] = {4, 5};
			shoot = new Animation2D(PlatformGame.enemiesAnimation, frames, 30);
			shoot.play(true);
		}
		{
			int frames[] = {14, 15, 16, 17, 18, 19, 20};
			explode = new Animation2D(PlatformGame.enemiesAnimation, frames, 30);
			explode.play(false);
		}
	}
	
	protected void behavior() {
		if(!dead) {
			if(!exploding) {
				double xa = speed * Math.cos(angle);
				double ya = speed * Math.sin(angle);
				move(xa ,ya);
			}
			if(onGround) {
				level.spawn(new Bomber(x, y, 16, 16, level, true));
				dead = true;
			}
		}
	}
	
	public boolean handleCollisionDynamic(int dx, int dy) {
		return false;
	}
	
	public boolean handleCollisionStatic(int dx, int dy) {
		return level.checkCollisionStatic(this, dx, dy);
	}
	
	@Override
	protected void onTileCollision() {
//		if(!dead) {
//			level.spawn(new Bomber(x, y, 16, 16, level, true));
//			dead = true;
//		}
	}
	
}
