package com.argetgames.roadtofive.entities;

import java.util.ArrayList;

import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.arget2d.input.Mouse;
import com.argetgames.arget2d.input.Mouse.MouseButton;
import com.argetgames.arget2d.tilemap.Tilemap;
import com.argetgames.roadtofive.PlatformGame;
import com.argetgames.roadtofive.sound.SoundAPI;

public class Level {
	
	private Tilemap map;
	private ArrayList<Living> livings;
	public Living player = null;
	private Boss boss = null;
	
	private ArrayList<Projectile> projectiles;
	private boolean victory = false, defeat = false;
	
	
	public Level(Tilemap map) {
		this.map = map;
		projectiles = new ArrayList<Projectile>();
		livings = new ArrayList<Living>();
		
		int w = map.getWidth();
		int h = map.getHeight();
		int tw = map.getTileWidth();
		int th = map.getTileHeight();
		for(int y = 0; y < h; y++) {
			for(int x = 0; x < w; x++) {
				int ID = map.getEntityValue(x, y);
				spawn(ID, x, y, tw, th);
			}
		}
		
		if(livings.size() < 2 && (player == null || boss == null))
			return;
		
		if(player == null) {
			Living l = livings.get(0);
			livings.remove(0);
			player = new Player(l.x, l.y, l.width, l.height, this);
			PlatformGame.camera.set(player.getCenterX() - PlatformGame.globalWidth/2, 
					player.getCenterY() - PlatformGame.globalHeight/2);
		}
		
		if(boss == null) {
			Living l = livings.get(livings.size()-1);
			livings.remove(livings.size()-1);
			boss = new Boss(l.x, l.y, l.width*2, l.height*2, this);
		}
		
		livings.add(player);
		livings.add(boss);
	}
	
	private void spawn(int ID, int x, int y, int tw, int th) {
		Living l = null;
		switch(ID) {
		case 0:
			if(player == null) {
				player = new Player(x * tw, y*th, tw, th, this);
				PlatformGame.camera.set(player.getCenterX() - PlatformGame.globalWidth/2, 
						player.getCenterY() - PlatformGame.globalHeight/2);
			}
			break;
		case 1:
			l = new Jumper(x * tw, y*th, tw, th, this);
			break;
		case 2:
			l = new Enemy(x * tw, y*th, tw, th, this);
			break;
		case 3:
			if(boss == null) {
				boss = new Boss(x * tw, y*th, tw*2, th*2, this);
			}
			break;
		case 12:
			l = new Pickup(x * tw, y*th, 10, 10, this);
			break;
		default:
			break;
		}
		if(l != null) {
			livings.add(l);
		}
	}
	
	public ArrayList<Pickup> checkCollisionPickups(Entity entity, int parentID, int dx, int dy) {
		ArrayList<Pickup> collisions = new ArrayList<Pickup>();
		for(int i = 0; i < livings.size(); i++) {
			Living l = livings.get(i);
			if(l instanceof Pickup){
				if(!l.dead && l.getTeamID() != parentID && l.collision(entity)) {
					collisions.add((Pickup) l);
				}
			}
		}
		
		return collisions;
	}
	
	public ArrayList<Living> checkCollisionDynamic(Entity entity, int parentID, int dx, int dy) {
		ArrayList<Living> collisions = new ArrayList<Living>();
		for(int i = 0; i < livings.size(); i++) {
			Living l = livings.get(i);
			if(l.getDynamicSolid() && !l.dead && l.getTeamID() != parentID && l.collision(entity)) {
				collisions.add(l);
			}
		}
		
		return collisions;
	}
	
	
	public boolean checkCollisionStatic(Entity entity, int dx, int dy) {
		return map.checkCollision(entity, dx, dy);
	}
	
	public boolean checkCollisionStaticRedOnly(Entity entity, int dx, int dy) {
		return map.checkCollisionRedOnly(entity, dx, dy);
	}
	
	public boolean checkCollisionStaticBlueOnly(Entity entity, int dx, int dy) {
		return map.checkCollisionBlueOnly(entity, dx, dy);
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