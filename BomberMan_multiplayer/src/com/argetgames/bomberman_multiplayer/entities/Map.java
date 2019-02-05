package com.argetgames.bomberman_multiplayer.entities;

import java.awt.Point;
import java.util.ArrayList;

import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.arget2d.menu.Rectangle;
import com.argetgames.arget2d.tilemap.Tilemap;
import com.argetgames.bomberman_multiplayer.BombermanGame;
import com.argetgames.bomberman_multiplayer.network.Serialize;

public class Map {
	
	private static final int MAP_HEADER = 0xFF1919FF;

	private Tilemap level_0;
	private ArrayList<Point> spawns;
	private ArrayList<Player> players;
	private ArrayList<Bomb> bombs;
	
	public Map() {
		level_0 = new Tilemap("res/maps/level0.agtm", 32, 32, BombermanGame.tileSheet);
		init();
	}
	
	public void init(){
		//@TODO: Change this to only load once.
		spawns = new ArrayList<Point>();
		int w = level_0.getWidth();
		int h = level_0.getHeight();
		//int tw = level_0.getTileWidth();
		//int th = level_0.getTileHeight();
		for(int y = 0; y < h; y++) {
			for(int x = 0; x < w; x++) {
				int ID = level_0.getEntityValue(x, y);
				spawn(ID, x, y);//, tw, th);
			}
		}
		players = new ArrayList<Player>(spawns.size());
		bombs = new ArrayList<Bomb>();
	}
	
	public Point getSpawnPoint(){
		if(!spawns.isEmpty()){
			Point spawn = spawns.get(0);
			spawns.remove(0);
			return spawn;
		}
		return null;
	}
	
	public boolean addPlayer(Player player){
		if(!players.contains(player)){
			players.add(player);
			return true;
		}
		return false;
	}
	
	private void spawn(int ID, int x, int y){//, int tw, int th) {
		switch(ID) {
		case 0:
		{
//			if(players.isEmpty())
//			players.add(new Player(x, y, tw));
			spawns.add(new Point(x, y));
			
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
	
	public void parseMapData(byte[] data){
		if(data.length < Integer.BYTES * 3)return;
		int index = 0;
		{
			int values[] = new int[1];
			index = Serialize.deserializeInteger(data, index, values);
			if(values[0] != MAP_HEADER)return;
		}
		int numIntegers = 0;
		{
			int values[] = new int[2];
			index = Serialize.deserializeInteger(data, index, values);
			if(values[0] > data.length)return;
			numIntegers = values[1];
		}
		{
			for(int i = 0; i < numIntegers; i++){
				byte[] playerData = new byte[Player.NUM_PLAYER_BYTES];
				for(int j = 0; j < playerData.length; j++){
					playerData[j] = data[index++];
				}
				
				if(i < players.size()){
					players.get(i).parsePlayerData(playerData);
				}else {
					DummyPlayer p = new DummyPlayer(0, 0, level_0.getTileWidth());
					p.parsePlayerData(playerData);
					players.add(p);
				}
			}
			while(numIntegers < players.size()){
				players.remove(players.size() -1);
			}
		}
		{
			int values[] = new int[1];
			index = Serialize.deserializeInteger(data, index, values);
			numIntegers = values[0];
		}
		{
			
			for(int i = 0; i < numIntegers; i++){
				byte[] bombData = new byte[Bomb.NUM_BOMB_BYTES];
				for(int j = 0; j < bombData.length; j++){
					bombData[j] = data[index++];
				}
				
				if(i < bombs.size()){
					bombs.get(i).parsePlayerData(bombData);
				}else {
					Player p;
					if(players.isEmpty())
						p = new Player(0, 0, 32);
					else
						p = players.get(0);
					DummyBomb b = new DummyBomb(0, 0, level_0.getTileWidth(), p);
					p.parsePlayerData(bombData);
					bombs.add(b);
				}
			}
			while(numIntegers < bombs.size()){
				bombs.remove(bombs.size() -1);
			}
		}
		
	}
	
	public byte[] getMapData(){
		int numBytes = Integer.BYTES * 2 + Integer.BYTES * 1 + Player.NUM_PLAYER_BYTES * players.size() + Integer.BYTES * 1 + Bomb.NUM_BOMB_BYTES * bombs.size();
		byte[] data = new byte[numBytes];
		int index = 0;
		{
			int[] values = {MAP_HEADER, numBytes, players.size()};
			index = Serialize.serializeInteger(data, index, values);
		}
		
		for(int i = 0; i < players.size(); i++){
			byte[] playerData = players.get(i).getPlayerData();
			for(int j = 0; j < playerData.length; j++){
				data[index++] = playerData[j];
			}
		}

		{	
			int[] values = {bombs.size()};
			index = Serialize.serializeInteger(data, index, values);
			for(int i = 0; i < bombs.size(); i++){
				byte[] bombData = bombs.get(i).getBombData();
				for(int j = 0; j < bombData.length; j++){
					data[index++] = bombData[j];
				}
			}
		}
		
		return data;
	}

	
	
	
}
