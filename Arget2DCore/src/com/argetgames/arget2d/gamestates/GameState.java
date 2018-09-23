package com.argetgames.arget2d.gamestates;

import com.argetgames.arget2d.graphics.Renderer2D;

public abstract class GameState {

	protected GameStateManager gsm;
	
	public GameState(GameStateManager gsm) {
		this.gsm = gsm;
	}
	
	protected void switchState(int index){
		gsm.setState(index);
	}
	
	public abstract void update();
	
	public abstract void draw(Renderer2D renderer);

}
