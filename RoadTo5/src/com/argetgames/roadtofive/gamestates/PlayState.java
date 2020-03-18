package com.argetgames.roadtofive.gamestates;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import com.argetgames.arget2d.gamestates.GameState;
import com.argetgames.arget2d.gamestates.GameStateManager;
import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.arget2d.graphics.SpriteSheet;
import com.argetgames.arget2d.input.Keyboard;
import com.argetgames.arget2d.input.Mouse;
import com.argetgames.arget2d.input.Mouse.MouseButton;
import com.argetgames.arget2d.tilemap.Tilemap;
import com.argetgames.roadtofive.Main;
import com.argetgames.roadtofive.PlatformGame;
import com.argetgames.roadtofive.entities.Level;

public class PlayState extends GameState {
	
	private Tilemap map_00;
	private Level level_00;
	private SpriteSheet tileSheet;
	private ArrayList<String> levelNames = new ArrayList<String>();
	private Tilemap[] maps;
	private int currentMap = 0;

	public PlayState(GameStateManager gsm) {
		super(gsm);
		this.tileSheet = PlatformGame.tileSheet;
		loadMaps();
	}
	
	private void loadMaps(){
		try {
			Scanner input = new Scanner(new File("res/maps/storyOrder"));
			int count = 0;
			levelNames.clear();
			while (input.hasNext()) {
		    	String name = input.next();
				if(name.endsWith(Tilemap.FILE_TYPE)){
					String name_check = name.substring(0, name.length() - ".".length() -Tilemap.FILE_TYPE.length());
					if(name_check.length() > 0){
						levelNames.add(name);
						if(PlatformGame.debug_log)System.out.println(name + " " + count);
						count = count + 1;
					}
				}
		    }
			input.close();
			int n = levelNames.size();
			maps = new Tilemap[n];
			for(int i = 0; i < n; i++) {
				maps[i] = new Tilemap("res/maps/" + levelNames.get(i), 16, 16, this.tileSheet);
			}
			
			currentMap = 0;
			map_00 = maps[0];
			level_00 = new Level(map_00);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	protected void switched(boolean active){
		if(active){
			loadMaps();
			Main.mainFrame.setCursor(Main.crosshairCursor);
		}else
		{
			Main.mainFrame.setCursor(Main.normalCursor);
		}
	}
	
	private void restart(){
		currentMap = (currentMap + 1) % maps.length;
		if(currentMap < 0)currentMap += maps.length;
		map_00 = maps[currentMap];
		level_00 = new Level(map_00);
	}

	@Override
	public void update() {
		if(Keyboard.getKey(KeyEvent.VK_ESCAPE)){
			switchState(PlatformGame.MENU_STATE);
			return;
		}
		
		if(Mouse.getMouse().isButtonClicked(MouseButton.MIDDLE))
			restart();
	
		level_00.update();
	}

	@Override
	public void draw(Renderer2D renderer) {
		level_00.draw(renderer);
	}

}
