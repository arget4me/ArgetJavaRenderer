package com.argetgames.arget2d.gamestates;

import com.argetgames.arget2d.graphics.Renderer2D;

public class GameStateManager {

	private GameState[] gameStates;
	public final int NUM_GAME_STATES;
	private int currentState = -1;
	
	public GameStateManager(int numGameStates) {
		NUM_GAME_STATES = numGameStates;
		gameStates = new GameState[NUM_GAME_STATES];
	}
	
	public boolean addState(GameState state, int index){
		if(index < 0 || index >= gameStates.length)return false;
		
		gameStates[index] = state;
		return true;
	}
	
	public boolean addAndSetState(GameState state, int index){
		if(index < 0 || index >= gameStates.length)return false;
		
		gameStates[index] = state;
		currentState = index;
		return true;
	}
	
	public void setState(int index){
		if(currentState != -1)gameStates[currentState].switched(false);
		if(index < 0 || index >= gameStates.length)currentState = -1;
		else {
			currentState = index;
			gameStates[currentState].switched(true);
		}
	}
	
	public void update(){
		if(currentState != -1){
			if(gameStates[currentState] != null)
				gameStates[currentState].update();
		}
	}
	
	public void draw(Renderer2D renderer){
		if(currentState != -1){
			if(gameStates[currentState] != null)
				gameStates[currentState].draw(renderer);
		}
	}

}
