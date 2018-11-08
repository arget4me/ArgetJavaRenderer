package com.argetgames.roadtofive.entities;

import com.argetgames.arget2d.graphics.Animation2D;
import com.argetgames.roadtofive.PlatformGame;

public class BossProjectile extends Projectile{

	private final static int BOSS_DAMAGE = 14;
	private final static double BOSS_SHOOT_SPEED = 4;
	private final static double BOSS_LIFETIME_PROJECTILE = 1.0;
	
	
	public BossProjectile(int x, int y, double angle, Level level, int parentID) {
		super(x, y, BOSS_SHOOT_SPEED, angle, BOSS_DAMAGE, BOSS_LIFETIME_PROJECTILE, level, parentID);
	}
	
	public BossProjectile(int x, int y, double angle, double lifetimeSeconds, Level level,
			int parentID) {
		super(x, y, BOSS_SHOOT_SPEED, angle, BOSS_DAMAGE, lifetimeSeconds, level, parentID);
	}
	
	protected void init() {
		int row = 12 * 3;
		
		{
			int frames[] = {row + 0, row + 1, row + 2, row + 3, row + 4, row + 5, row + 6, row + 7, row + 8};
			shoot = new Animation2D(PlatformGame.projectile_00_animation, frames, 30);
			shoot.play(true);
		}
		row = 12 * 4;
		{
			int frames[] = {row + 0, row + 1, row + 2, row + 3, row + 4, row + 5, row + 6};
			explode = new Animation2D(PlatformGame.projectile_00_animation, frames, 30);
			explode.play(false);
		}
	}

}
