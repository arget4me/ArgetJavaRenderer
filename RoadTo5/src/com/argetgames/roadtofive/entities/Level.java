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
	private Boss boss;
	
	private ArrayList<Projectile> projectiles;
	private boolean victory = false, defeat = false;
	
	
	public Level(Tilemap map) {
		this.map = map;
		projectiles = new ArrayList<Projectile>();
		livings = new ArrayList<Living>();
		player = new Player(64, 128, 16, 16 , this);
		livings.add(player);
		livings.add(new Enemy(16*16, 64, 16, 16 , this));
		livings.add(new Jumper(16*36, 128, 16, 16 , this));
		livings.add(new Jumper(16*70, 64, 16, 16 , this));
		livings.add(new Enemy(16*73, 64, 16, 16 , this));
		livings.add(new Jumper(16*104, 16, 16, 16 , this));
		livings.add(new Enemy(16*98, 16, 16, 16 , this));
		
		livings.add(new Jumper(16*134, 16, 16, 16 , this));
		livings.add(new Enemy(16*135, 16, 16, 16 , this));
		livings.add(new Enemy(16*136, 16, 16, 16 , this));
		livings.add(new Jumper(16*137, 16, 16, 16 , this));
		boss = new Boss(16*150, 16, 32, 32, this);
		livings.add(boss);
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
		if(!victory && !defeat) {
			for(int i = 0; i < livings.size(); i++) {
				Living l = livings.get(i);
				if(!l.dead) {
					l.update();
				}else {
					livings.remove(i);
					i--;
				}
			}
			if(boss.dead)
				victory = true;
			if(player.dead)
				defeat = true;
			
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
	}
	
	public void draw(Renderer2D renderer) {
		renderer.useCamera(true);
		map.draw(renderer);
		for(int i = 0; i < projectiles.size(); i++) {
			projectiles.get(i).draw(renderer, 0xFFFF0000);
		}
		for(int i = 0; i < livings.size(); i++) {
			livings.get(i).draw(renderer);
		}
//		renderer.useCamera(false);
//		String numProjectiles = "(Num projectiles: " + projectiles.size() + ")";
//		PlatformGame.textRenderer.drawText(renderer, 10+1, 10+1, 8, numProjectiles, -1, 0xFF666666);
//		PlatformGame.textRenderer.drawText(renderer, 10, 10, 8, numProjectiles, -1, 0xFFFFFFFF);
//		
//		String playerHealth = "(Player HP: " + player.health + ")";
//		PlatformGame.textRenderer.drawText(renderer, 10+1, 24+1, 8, playerHealth, -1, 0xFF660000);
//		PlatformGame.textRenderer.drawText(renderer, 10, 24, 8, playerHealth, -1, 0xFFFF0000);
		if(defeat) {
			renderer.useColorMask(false);
			renderer.useAlpha(true);
			renderer.fillRect(0, 0, PlatformGame.globalWidth, PlatformGame.globalHeight, 0xAA440000);
			renderer.useAlpha(false);
			renderer.useColorMask(true);
			String defeatMessage = "Defeat!!";
			int xx = PlatformGame.globalWidth/2 - defeatMessage.length()*32/2;
			int yy = PlatformGame.globalHeight/3 - 16;
			PlatformGame.textRenderer.drawText(renderer, xx+1, yy+1, 32, defeatMessage, -1, 0xFF660000);
			PlatformGame.textRenderer.drawText(renderer, xx, yy, 32, defeatMessage, -1, 0xFFFF0000);
			String restartMessage = "Right click to restart...";
			xx = PlatformGame.globalWidth/2 - restartMessage.length()*8/2;
			PlatformGame.textRenderer.drawText(renderer, xx+1, yy+32+12+1, 8, restartMessage, -1, 0xFF660000);
			PlatformGame.textRenderer.drawText(renderer, xx, yy+32+12, 8, restartMessage, -1, 0xFFFF0000);
		}else if(victory) {
			renderer.useColorMask(false);
			renderer.useAlpha(true);
			renderer.fillRect(0, 0, PlatformGame.globalWidth, PlatformGame.globalHeight, 0xAA552200);
			renderer.useAlpha(false);
			renderer.useColorMask(true);
			String victoryMessage = "Victory!!";
			int xx = PlatformGame.globalWidth/2 - victoryMessage.length()*32/2;
			int yy = PlatformGame.globalHeight/3 - 16;
			PlatformGame.textRenderer.drawText(renderer, xx+1, yy+1, 32, victoryMessage, -1, 0xFF662200);
			PlatformGame.textRenderer.drawText(renderer, xx, yy, 32, victoryMessage, -1, 0xFFFFDD00);
			String restartMessage = "Right click to restart...";
			xx = PlatformGame.globalWidth/2 - restartMessage.length()*8/2;
			PlatformGame.textRenderer.drawText(renderer, xx+1, yy+32+12+1, 8, restartMessage, -1, 0xFF662200);
			PlatformGame.textRenderer.drawText(renderer, xx, yy+32+12, 8, restartMessage, -1, 0xFFFFDD00);
		}
	}


	

}
