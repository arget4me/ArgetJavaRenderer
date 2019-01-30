package com.argetgames.bomberman_multiplayer.entities;

import java.util.ArrayList;

import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.arget2d.menu.Rectangle;
import com.argetgames.arget2d.tilemap.Tilemap;
import com.argetgames.bomberman_multiplayer.BombermanGame;

public class Map {

	private Tilemap level_0;
	private ArrayList<Player> players =  new ArrayList<Player>();
	private ArrayList<Bomb> bombs = new ArrayList<Bomb>();
	
	public Map() {
		level_0 = new Tilemap("res/maps/level0.agtm", 32, 32, BombermanGame.tileSheet);
		int w = level_0.getWidth();
		int h = level_0.getHeight();
		int tw = level_0.getTileWidth();
		int th = level_0.getTileHeight();
		for(int y = 0; y < h; y++) {
			for(int x = 0; x < w; x++) {
				int ID = level_0.getEntityValue(x, y);
				spawn(ID, x, y, tw, th);
			}
		}
	}
	
	private void spawn(int ID, int x, int y, int tw, int th) {
		switch(ID) {
		case 0:
		{
			if(players.isEmpty())
			players.add(new Player(x, y, tw));
			
		}break;
		default:
			break;
		}
	}
	
	public int getNumPlayers() {
		return players.size();
	}
	
	public Player getPlayer(int index) {
		if(index >= 0 && index < players.size()) {
			return players.get(index);
		}
		return null;
	}
	
	
	public int getTileHeight() {
		return level_0.getTileHeight();
	}

	public int getTileWidth() {
		return level_0.getTileHeight();
	}

	public boolean checkCollisionMapOnly(Rectangle rectangle, int xa, int ya) {
		return level_0.checkCollisionRedOnly(rectangle, xa, ya);
	}
	
	public boolean checkCollision(Rectangle rectangle, int xa, int ya) {
		
		return (level_0.checkCollisionRedOnly(rectangle, xa, ya) || (checkBombCollision(rectangle, xa, ya)));
	}
	
	private boolean checkBombCollision(Rectangle rect, int xa, int ya) {
		Rectangle temp = new Rectangle(rect.x + xa, rect.y + ya, rect.width, rect.height);
		for(int i = 0; i < bombs.size(); i++){
			if(temp.collision(bombs.get(i))){
				//@TODO: if sliding over a player. Make player slide with bomb. auto kill...
				
				bombs.get(i).startSlide(xa, ya);
				return true;
			}
		}
		return false;
	}

	public boolean spawnBomb(int xTile, int yTile, int size, Player player) {
		if(!checkCollision(new Rectangle(xTile * size, yTile * size, size, size), 0, 0)) {
			bombs.add(new Bomb(xTile, yTile, size, player));
			return true;
		}
		
		return false;
	}

	
	public void update() {
		for(int i = 0; i < players.size(); i++) {
			if(!players.get(i).isDead)
				players.get(i).update(this);
			else {
				players.remove(i);
				i--;
				continue;
			}
				
		}
		for(int i = 0; i < bombs.size(); i++) {
			bombs.get(i).update(this);
			if(bombs.get(i).isDead) {
				bombs.remove(i);
				i--;
				continue;
			}
		}
	}
	
	public void draw(Renderer2D renderer) {
		renderer.camera.set(-(renderer.getWidth()/2 - level_0.getWidth()*level_0.getTileWidth()/2), 
				-(renderer.getHeight()/2 - level_0.getHeight()*level_0.getTileHeight()/2));
		level_0.draw(renderer);
		for(int i = 0; i < bombs.size(); i++) {
			if(!bombs.get(i).isDead)
				bombs.get(i).draw(renderer);
		}
		for(int i = 0; i < players.size(); i++) {
			if(!players.get(i).isDead)
				players.get(i).draw(renderer);
		}
		
	}

	
	
	
}
