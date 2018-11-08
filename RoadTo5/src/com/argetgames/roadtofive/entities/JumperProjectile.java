package com.argetgames.roadtofive.entities;

import com.argetgames.arget2d.graphics.Animation2D;
import com.argetgames.roadtofive.PlatformGame;

public class JumperProjectile extends Projectile{

	private final static int JUMPER_DAMAGE = 1;
	private final static double JUMPER_SHOOT_SPEED = 0.8;
	private final static double JUMPER_LIFETIME_PROJECTILE = 0.4;
	
	public JumperProjectile(int x, int y, double angle, Level level, int parentID) {
		super(x, y, JUMPER_SHOOT_SPEED, angle, JUMPER_DAMAGE, JUMPER_LIFETIME_PROJECTILE, level, parentID);
	}
	
	public JumperProjectile(int x, int y, double angle, double lifetimeSeconds, Level level,
			int parentID) {
		super(x, y, JUMPER_SHOOT_SPEED, angle, JUMPER_DAMAGE, lifetimeSeconds, level, parentID);
	}
	
	protected void init() {
		int row = 12 * 0;
		
		{
			int frames[] = {row + 0, row + 1, row + 0, row + 2};
			shoot = new Animation2D(PlatformGame.projectile_00_animation, frames, 20);
			shoot.play(true);
		}
		
		{
			int frames[] = {row + 3, row + 4, row + 5, row + 6};
			explode = new Animation2D(PlatformGame.projectile_00_animation, frames, 30);
			explode.play(false);
		}
	}

}
