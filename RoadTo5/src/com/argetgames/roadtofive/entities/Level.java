package com.argetgames.roadtofive.entities;

import java.util.ArrayList;

import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.arget2d.input.Mouse;
import com.argetgames.arget2d.input.Mouse.MouseButton;
import com.argetgames.arget2d.tilemap.Tilemap;
import com.argetgames.roadtofive.PlatformGame;

public class Level {
	
	private Tilemap map;
	private ArrayList<Living> livings;
	public Living player;
	
	private ArrayList<Projectile> projectiles;

	
	public Level(Tilemap map) {
		this.map = map;
		projectiles = new ArrayList<Projectile>();
		livings = new ArrayList<Living>();
		player = new Player(64, 128, 16, 16 , this);
		livings.add(player);
		livings.add(new Enemy(16*16, 64, 16, 16 , this));
		livings.add(new Jumper(16*74, 128, 16, 16 , this));
		livings.add(new Jumper(16*36, 128, 16, 16 , this));
		PlatformGame.camera.set(player.getCenterX() - PlatformGame.globalWidth/2, 
				player.getCenterY() - PlatformGame.globalHeight/2);
	}
	
	public ArrayList<Living> checkCollisionDynamic(Entity entity, int parentID, int dx, int dy) {
		ArrayList<Living> collisions = new ArrayList<Living>();
		for(int i = 0; i < livings.size(); i++) {
			Living l = livings.get(i);
			if(!l.dead && l.getID() != parentID && l.collision(entity)) {
				collisions.add(l);
			}
		}
		
		return collisions;
	}
	
	
	public boolean checkCollisionStatic(Entity entity, int dx, int dy) {
		return map.checkCollision(entity, dx, dy);
	}
	
	public void spawnProjectile(int x, int y, double speed, double angle, int DMG, int parentID) {
		projectiles.add(new Projectile(x, y, speed, angle, DMG, this, parentID));
	}
	
	public void spawnProjectile(int x, int y, double speed, double angle, int DMG, double lifetimeSeconds, int parentID) {
		projectiles.add(new Projectile(x, y, speed, angle, DMG, lifetimeSeconds, this, parentID));
	}
	
	public void update() {
		for(int i = 0; i < livings.size(); i++) {
			Living l = livings.get(i);
			if(!l.dead) {
				l.update();
			}else {
				livings.remove(i);
				i--;
			}
		}
		
		
		PlatformGame.camera.set(player.getCenterX() - PlatformGame.globalWidth/2, player.getCenterY() - PlatformGame.globalHeight/2);
		
		for(int i = 0; i < projectiles.size(); i++) {
			Projectile p = projectiles.get(i);
			p.update();
			if(p.dead) {
				projectiles.remove(i);
				i--;
			}
		}
	}
	
	public void draw(Renderer2D renderer) {
		renderer.useCamera(true);
		map.draw(renderer);
		for(int i = 0; i < livings.size(); i++) {
			livings.get(i).draw(renderer);
		}
		for(int i = 0; i < projectiles.size(); i++) {
			projectiles.get(i).draw(renderer, 0xFFFF0000);
		}
		renderer.useCamera(false);
		String numProjectiles = "(Num projectiles: " + projectiles.size() + ")";
		PlatformGame.textRenderer.drawText(renderer, 10+1, 10+1, 8, numProjectiles, -1, 0xFF666666);
		PlatformGame.textRenderer.drawText(renderer, 10, 10, 8, numProjectiles, -1, 0xFFFFFFFF);
		
		String playerHealth = "(Player HP: " + player.health + ")";
		PlatformGame.textRenderer.drawText(renderer, 10+1, 24+1, 8, playerHealth, -1, 0xFF660000);
		PlatformGame.textRenderer.drawText(renderer, 10, 24, 8, playerHealth, -1, 0xFFFF0000);
	}


	

}
