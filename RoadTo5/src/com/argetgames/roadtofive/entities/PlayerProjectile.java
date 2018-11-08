package com.argetgames.roadtofive.entities;

import com.argetgames.arget2d.graphics.Animation2D;
import com.argetgames.roadtofive.PlatformGame;

public class PlayerProjectile extends Projectile{

	private final static int PLAYER_DAMAGE = 5;
	private final static double PLAYER_SHOOT_SPEED = 5;
	
	
	public PlayerProjectile(int x, int y, double angle, Level level, int parentID) {
		super(x, y, PLAYER_SHOOT_SPEED, angle, PLAYER_DAMAGE, level, parentID);
	}
	
	public PlayerProjectile(int x, int y, double angle, double lifetimeSeconds, Level level,
			int parentID) {
		super(x, y, PLAYER_SHOOT_SPEED, angle, PLAYER_DAMAGE, lifetimeSeconds, level, parentID);
	}
	
	protected void init() {
		int row = 12 * 1;
		
		{
			int frames[] = {row + 0, row + 1, row + 2};
			shoot = new Animation2D(PlatformGame.projectile_00_animation, frames, 60);
			shoot.play(true);
		}
		
		{
			int frames[] = {row + 3, row + 4, row + 5, row + 6};
			explode = new Animation2D(PlatformGame.projectile_00_animation, frames, 30);
			explode.play(false);
		}
	}

}
