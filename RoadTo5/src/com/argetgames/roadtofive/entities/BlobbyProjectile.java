package com.argetgames.roadtofive.entities;

import com.argetgames.arget2d.graphics.Animation2D;
import com.argetgames.roadtofive.PlatformGame;

public class BlobbyProjectile extends Projectile{

	private final static int BLOBBY_DAMAGE = 6;
	private final static double BLOBBY_SHOOT_SPEED = 6;
	
	public BlobbyProjectile(int x, int y, double angle, Level level, int parentID) {
		super(x, y, BLOBBY_SHOOT_SPEED, angle, BLOBBY_DAMAGE, level, parentID);
	}
	
	public BlobbyProjectile(int x, int y, double angle, double lifetimeSeconds, Level level,
			int parentID) {
		super(x, y, BLOBBY_SHOOT_SPEED, angle, BLOBBY_DAMAGE, lifetimeSeconds, level, parentID);
	}
	
	protected void init() {
		int row = 12 * 2;
		
		{
			int frames[] = {row + 0, row + 1, row + 0, row + 2};
			shoot = new Animation2D(PlatformGame.projectile_00_animation, frames, 20);
			shoot.play(true);
		}
		
		{
			int frames[] = {row + 3, row + 4, row + 5, row + 6, row + 7};
			explode = new Animation2D(PlatformGame.projectile_00_animation, frames, 30);
			explode.play(false);
		}
	}

}
