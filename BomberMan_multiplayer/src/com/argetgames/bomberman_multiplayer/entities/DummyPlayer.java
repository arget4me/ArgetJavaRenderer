package com.argetgames.bomberman_multiplayer.entities;

public class DummyPlayer extends Player {

	public DummyPlayer(int xTile, int yTile, int size) {
		super(xTile, yTile, size);
	}
	
	protected void checkInput(){
		
	}
	
	protected void die(){
		
	}
	
	protected void move(Map map) {
		if(stepsLeft > 0 && !checkCollision(map)) {
			stepsLeft--;
			if(stepsLeft <= 0) {
				walking = false;
			}
		}else {
			walking = false;
		}
		
	}
	
}
