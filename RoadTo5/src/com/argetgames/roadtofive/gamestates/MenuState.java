package com.argetgames.roadtofive.gamestates;

import java.awt.event.KeyEvent;

import com.argetgames.arget2d.gamestates.GameState;
import com.argetgames.arget2d.gamestates.GameStateManager;
import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.arget2d.graphics.TextRenderer;
import com.argetgames.arget2d.input.Keyboard;
import com.argetgames.roadtofive.PlatformGame;
import com.argetgames.roadtofive.sound.SoundAPI;

public class MenuState extends GameState {
	
	private String[] list = {
			"Play",
			"Editor",
			"Campaign Editor",
			"Quit"
	};
	private final int PLAY = 0, EDITOR = 1, CAMPAIGN_EDITOR = 2, QUIT = 3;
	private int currentSelection = 0;
	private int switchRepeatDelay = 0;
	private final int delayTime = (int)(PlatformGame.global_ups * 0.2);

	public MenuState(GameStateManager gsm) {
		super(gsm);
	}

	private void switchSelection(int delta){
		if(switchRepeatDelay > 0)return;
		switchRepeatDelay += delayTime;
		currentSelection += delta;
		currentSelection %= list.length;
		if(currentSelection < 0)
			currentSelection += list.length;
	}
	
	@Override
	public void update() {
		if(switchRepeatDelay > 0)switchRepeatDelay--;
		if(Keyboard.getKey(KeyEvent.VK_ENTER)){
			if(currentSelection == PLAY){
				switchState(PlatformGame.PLAY_STATE);
			}else if(currentSelection == EDITOR){
				switchState(PlatformGame.EDITOR_STATE);
			}else if(currentSelection == CAMPAIGN_EDITOR){
				switchState(PlatformGame.CAMPAIGN_EDITOR_STATE);
			}else if(currentSelection == QUIT){
				System.exit(0);
			}
			return;
		}
			
		if(Keyboard.getKey(KeyEvent.VK_UP) || Keyboard.getKey(KeyEvent.VK_W))
			switchSelection(-1);
		else if(Keyboard.getKey(KeyEvent.VK_DOWN) || Keyboard.getKey(KeyEvent.VK_S))
			switchSelection(+1);
		
	}

	@Override
	public void draw(Renderer2D renderer) {
		for(int i = 0; i < list.length; i++){
			int color = 0xFF0000FF;
			if(i == currentSelection)color = 0xFF00FF00;
			PlatformGame.textRenderer.drawText(renderer, (2 + PlatformGame.globalWidth - list[i].length()*6)/2, 1+50 + 20*i, 8, list[i], -1, 0xFF000000);
			PlatformGame.textRenderer.drawText(renderer, (PlatformGame.globalWidth - list[i].length()*6)/2, 50 + 20*i, 8, list[i], -1, color);
		}
	}

}
