package com.argetgames.roadtofive.entities;

import java.util.ArrayList;

import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.roadtofive.PlatformGame;
import com.argetgames.roadtofive.sound.SoundAPI;

public class Projectile extends Entity {

	protected double speed = 5;
	protected double angle = 0;
	public boolean dead = false;
	private int lifetime = PlatformGame.global_ups * 1;
	protected int parentID = -1;
	private ArrayList<Living> collisions = null;
	protected int DMG = 1;
	
	public Projectile(int x, int y, double speed, double angle, int DMG, Level level, int parentID) {
		super(x-4, y-4, 8, 8, 1.0, 1.0, level);
		applyGravity = false;
		this.speed = speed;
		this.angle = angle;
		this.parentID = parentID;
		this.DMG = DMG;
	}
	
	public Projectile(int x, int y, double speed, double angle, int DMG, double lifetimeSeconds, Level level, int parentID) {
		super(x-4, y-4, 8, 8, 1.0, 1.0, level);
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
		SoundAPI.testPlaySound("hurt_1.wav", 0.6f);
	}
	
	public boolean handleCollisionDynamic(int dx, int dy) {
		collisions = level.checkCollisionDynamic(this, parentID, dx, dy);
		return (collisions.size() > 0);
	}
	
	public boolean handleCollisionStatic(int dx, int dy) {
		return level.checkCollisionStaticRedOnly(this, dx, dy);
	}
	
	@Override
	protected void onTileCollision() {
		dead = true;
//		SoundAPI.testPlaySound("hurt_1.wav", 0.6f);
	}
	
	public void draw(Renderer2D renderer) {
		draw(renderer, 0xFFFFFF00);
	}
	
	public void draw(Renderer2D renderer, int color) {
		renderer.useCamera(true);
		String mask = "UUUU3UUUUUUU33UUUUUU33UUUU4UUUU4UUUUUUU5UUUUU";
		char character = mask.charAt(((x*y + width) % height + 5*y + x*31) % (mask.toCharArray().length));
		PlatformGame.textRenderer.drawText(renderer, x+1, y, width, "" + character, 0, 0xFF000000);
		PlatformGame.textRenderer.drawText(renderer, x, y+1, width, "" + character, 0, 0xFF000000);
		PlatformGame.textRenderer.drawText(renderer, x+1, y+1, width, "" + character, 0, 0xFF000000);
		PlatformGame.textRenderer.drawText(renderer, x, y, width, "" + character, 0, color);
//		renderer.renderImage2D(x, y, width, height, img);
		renderer.useCamera(false);
	}

}
