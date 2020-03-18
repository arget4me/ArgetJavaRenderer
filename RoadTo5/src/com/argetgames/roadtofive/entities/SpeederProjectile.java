package com.argetgames.roadtofive.entities;

import com.argetgames.arget2d.graphics.Animation2D;
import com.argetgames.arget2d.graphics.Image2D;
import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.roadtofive.PlatformGame;

public class SpeederProjectile extends Projectile{

	private final static int SPEEDER_DAMAGE = 5;
	private final static double SPEEDER_SHOOT_SPEED = 0;
	private final static double SPEEDER_LIFETIME_PROJECTILE = 0.1;
	
	public SpeederProjectile(int x, int y, double angle, Level level, int parentID) {
		super(x - 4/2, y - 4/2, SPEEDER_SHOOT_SPEED, angle, SPEEDER_DAMAGE, SPEEDER_LIFETIME_PROJECTILE, level, parentID);	
	}
	
	public SpeederProjectile(int x, int y, double angle, double lifetimeSeconds, Level level,
			int parentID) {
		super(x - 4/2, y - 4/2, SPEEDER_SHOOT_SPEED, angle, SPEEDER_DAMAGE, lifetimeSeconds, level, parentID);
	}
	
	protected void init() {
		width += 4;
		height += 4;
		{
			int frames[] = {5, 6};
			shoot = new Animation2D(PlatformGame.projectile_00_animation, frames, 24);
			shoot.play(false);
		}
		{
			int frames[] = {5, 6};
			explode = new Animation2D(PlatformGame.projectile_00_animation, frames, 24);
			explode.play(false);
		}
		handleCollisionDynamic(0, 0);
		onDynamicCollision();
		exploding = true;
	}
	
	protected void behavior() {
		if(shoot.playedOnce()) {
			dead = true;
		}
		shoot.update();
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
