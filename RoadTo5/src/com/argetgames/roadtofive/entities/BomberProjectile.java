package com.argetgames.roadtofive.entities;

import com.argetgames.arget2d.graphics.Animation2D;
import com.argetgames.arget2d.graphics.Image2D;
import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.roadtofive.PlatformGame;

public class BomberProjectile extends Projectile{

	private final static int BOMBER_DAMAGE = 40;
	private final static double BOMBER_SHOOT_SPEED = 0;
	private final static double BOMBER_LIFETIME_PROJECTILE = 0.1;
	
	public BomberProjectile(int x, int y, double angle, Level level, int parentID) {
		super(x - 32/2, y - 32/2, BOMBER_SHOOT_SPEED, angle, BOMBER_DAMAGE, BOMBER_LIFETIME_PROJECTILE, level, parentID);	
	}
	
	public BomberProjectile(int x, int y, double angle, double lifetimeSeconds, Level level,
			int parentID) {
		super(x - 32/2, y - 32/2, BOMBER_SHOOT_SPEED, angle, BOMBER_DAMAGE, lifetimeSeconds, level, parentID);
	}
	
	protected void init() {
		width += 32;
		height += 32;
		{
			int frames[] = {11, 12, 13};
			shoot = new Animation2D(PlatformGame.enemiesAnimation, frames, 30);
			shoot.play(false);
		}
		exploding = true;
		
		{
			int frames[] = {14, 15, 16, 17, 18, 19, 20};
			explode = new Animation2D(PlatformGame.enemiesAnimation, frames, 30);
			explode.play(false);
		}
		handleCollisionDynamic(0, 0);
		onDynamicCollision();
	}
	
	protected void behavior() {
		if(exploding) {
			if(explode.playedOnce())dead = true;
			explode.update();
		}else {
			if(shoot.playedOnce()) {
				exploding = true;
			}
			shoot.update();
		}
	}
	
	public void onDynamicCollision() {
		for(Living l : collisions) {
			onDynamicCollision(l);
		}
	}
	
	public boolean handleCollisionStatic(int dx, int dy) {
		return false;
	}
	
	@Override
	protected void onTileCollision() {
	}
	
}
