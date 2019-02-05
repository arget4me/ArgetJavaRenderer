package com.argetgames.bomberman_multiplayer.entities;

import java.util.ArrayList;

import com.argetgames.arget2d.graphics.Animation2D;
import com.argetgames.arget2d.graphics.Image2D;
import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.arget2d.menu.Rectangle;
import com.argetgames.bomberman_multiplayer.BombermanGame;
import com.argetgames.bomberman_multiplayer.network.Serialize;

public class Bomb extends Rectangle {

	public int size;
	public boolean isDead = false;
	private Animation2D animation;
	private ArrayList<Explosion> center = new ArrayList<Explosion>();
	private ArrayList<Explosion> up = new ArrayList<Explosion>();
	private ArrayList<Explosion> left = new ArrayList<Explosion>();
	private ArrayList<Explosion> down = new ArrayList<Explosion>();
	private ArrayList<Explosion> right = new ArrayList<Explosion>();
	private boolean sliding = false;
	private int dir = -1;
	private boolean detonated = false;
	private int ticksToDetonate = 3 * BombermanGame.global_ups;
	private int animationTime = 2 * BombermanGame.global_ups;
	private Player player;
	private boolean hasSlided = false;
	
	public Bomb(int xTile, int yTile, int size, Player player) {
		super(xTile * size, yTile * size, size, size);
		this.size = size;
		this.player = player;
		
		{
			int frameOrder[] = {0, 0, 0, 0, 0, 0,
								0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
			animation = new Animation2D(BombermanGame.bombSpritesheet, frameOrder, 20);
			animation.play(false);
		}
		
	}
	
	private void detonate(Map map) {
		if(detonated)return;
		detonated = true;
		// fill explosions
		center.add(new Explosion(x / size, y / size, size, -1, false));
		
		{
			int i = 1;
		    while(!map.checkCollisionMapOnly(this, 0, -i*size)){
		        up.add(new Explosion(x / size, y / size - i, size, 0, map.checkCollisionMapOnly(this, 0, -(i+1)*size)));
		        i++;
		    }
		}
		
		{
			int i = 1;
		    while(!map.checkCollisionMapOnly(this, -i*size, 0)){
		        left.add(new Explosion(x / size  - i, y / size, size, 1, map.checkCollisionMapOnly(this, -(i+1)*size, 0)));
		        i++;
		    }
		}
		
		{
			int i = 1;
		    while(!map.checkCollisionMapOnly(this, 0, +i*size)){
		        down.add(new Explosion(x / size, y / size + i, size, 2, map.checkCollisionMapOnly(this, 0, +(i+1)*size)));
		        i++;
		    }
		}
		
		{
			int i = 1;
		    while(!map.checkCollisionMapOnly(this, +i*size, 0)){
		        right.add(new Explosion(x / size  + i, y / size, size, 3, map.checkCollisionMapOnly(this, +(i+1)*size, 0)));
		        i++;
		    }
		}
		player.hasSpawned = false;
		//@Note: Play sound effect here.
	}
	
	private void growExplosion() {
		//update explosions
		for(Explosion e : center){
            e.update();
            if(e.isDead) {
            	isDead = true;
            	break;
            }
        }
        for(Explosion e : up){
        	e.update();
        	if(e.isDead) {
            	isDead = true;
            	break;
            }
        }
        for(Explosion e : down){
        	e.update();
        	if(e.isDead) {
            	isDead = true;
            	break;
            }
        }
        for(Explosion e : left){
        	e.update();
        	if(e.isDead) {
            	isDead = true;
            	break;
            }
        }
        for(Explosion e : right){
        	e.update();
        	if(e.isDead) {
            	isDead = true;
            	break;
            }
        }
        
        if(isDead) {
        	center.clear();
        	up.clear();
        	left.clear();
        	down.clear();
        	right.clear();
        }
	}
	
	public void startSlide(int xa, int ya) {
		if(hasSlided )return;
		if(xa == ya)return;
		if(ya < 0)dir = 0;
		if(xa < 0)dir = 1;
		if(ya > 0)dir = 2;
		if(xa > 0)dir = 3;
		
		sliding = true;
		hasSlided = true;
	}
	
	public boolean isSliding() {
		return sliding;
	}
	
	public void stopSlide(Map map) {
		sliding = false;
		int xa = 0; int ya = 0;
		switch(dir) {
		case 0:
			if(!map.checkCollisionMapOnly(this,  0, -1))
				ya = -1;
	        break;
	    case 1:
	    	if(!map.checkCollisionMapOnly(this, -1,  0))
	    		xa = -1;
	        break;
	    case 2:
	    	if(!map.checkCollisionMapOnly(this,  0,  1))
	    		ya = 1;
	        break;
	    case 3:
	    	if(!map.checkCollisionMapOnly(this,  1,  0))
	    		xa = 1;
	    	break;
    	default:
    		break;
		}
		x = (x / size + xa) * size;
		y = (y / size + ya) * size;
	}
	
	protected void bombSlide(Map map) {
		int speed = 8;
		switch(dir){
	    case 0:
	        if(!map.checkCollisionMapOnly(this, 0, -1))
	            y -= speed;//1.0
	        else
	            sliding = false;
	        break;
	    case 1:
	        if(!map.checkCollisionMapOnly(this, -1, 0))
	            x -= speed;//1.0;
	        else
	            sliding = false;
	        break;
	    case 2:
	        if(!map.checkCollisionMapOnly(this, 0, +1))
	            y += speed;//1.0;
	        else
	            sliding = false;
	        break;
	    case 3:
	        if(!map.checkCollisionMapOnly(this, +1, 0))
	            x += speed;//1.0;
	        else
	            sliding = false;
	        break;
	    default:
	        sliding = false;
	    }
		
	}
	
	private void dealDamage(Map map) {
		int numPlayers = map.getNumPlayers();
		
	    for(Explosion e : center){
	    	for(int i = 0; i < numPlayers; i++) {
	    		Player p = map.getPlayer(i);
		    	if(e.collision(p))
		            p.hurt();
	    	}
	    }
	    for(Explosion e : up){
	    	for(int i = 0; i < numPlayers; i++) {
	    		Player p = map.getPlayer(i);
		    	if(e.collision(p))
		    		p.hurt();
	    	}
	    }
	    for(Explosion e : down){
	    	for(int i = 0; i < numPlayers; i++) {
	    		Player p = map.getPlayer(i);
		    	if(e.collision(p))
		    		p.hurt();
	    	}
	    }
	    for(Explosion e : left){
	    	for(int i = 0; i < numPlayers; i++) {
	    		Player p = map.getPlayer(i);
		    	if(e.collision(p))
		    		p.hurt();
	    	}
	    }
	    for(Explosion e : right){
	    	for(int i = 0; i < numPlayers; i++) {
	    		Player p = map.getPlayer(i);
		    	if(e.collision(p))
		    		p.hurt();
	    	}
	    }
	}
	
	public void update(Map map) {
		if(!sliding) {
			animation.update();
			if(animation.playedOnce()) {
				detonate(map);
				growExplosion();
				dealDamage(map);
			}
		}else {
			bombSlide(map);
		}
	}
	
	public void draw(Renderer2D renderer) {
		if(!detonated) {
			Image2D frame = animation.getCurrentFrame();
			renderer.useCamera(true);
			renderer.useColorMask(true);
			renderer.renderImage2D(x, y, width, height, frame);
			renderer.useColorMask(false);
			renderer.useCamera(false);
		}else {
			//render explosion
			for(Explosion e : center){
	            e.draw(renderer);
	        }
	        for(Explosion e : up){
	        	e.draw(renderer);
	        }
	        for(Explosion e : down){
	        	e.draw(renderer);
	        }
	        for(Explosion e : left){
	        	e.draw(renderer);
	        }
	        for(Explosion e : right){
	        	e.draw(renderer);
	        }
		}
	}
	
	
	public static final int BOMB_HEADER = 0xFF2744FF;
	public static final int NUM_BOMB_BYTES = Integer.BYTES * 3 + 1;//Header + X + Y + 1 Booleans(1byte)
	
	public void parsePlayerData(byte[] data){
		if(data.length < NUM_BOMB_BYTES)return;
		int index = 0;
		{
			int values[] = new int[1];
			index = Serialize.deserializeInteger(data, index, values);
			if(values[0] != BOMB_HEADER)return;
		}
		
		{
			int values[] = new int[2];
			index = Serialize.deserializeInteger(data, index, values);
			x = values[0];
			y = values[1];
		}
		{
			boolean values[] = new boolean[2];
			index = Serialize.deserializeBoolean(data, index, values);
			sliding = values[0];
			isDead = values[1];
		}	
	}
		
	public byte[] getBombData(){
		byte[] data = new byte[NUM_BOMB_BYTES];
		int index = 0;
		{
			int values[] = {BOMB_HEADER, x, y};
			index = Serialize.serializeInteger(data, index, values);
		}
		{
			boolean values[] = {sliding, isDead};
			index = Serialize.serializeBoolean(data, index, values);
		}
		return data;
	}

	

}
