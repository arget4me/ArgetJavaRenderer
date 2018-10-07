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
import com.argetgames.roadtofive.PlatformGame;
import com.argetgames.roadtofive.entities.Level;

public class PlayState extends GameState {
	
	private Tilemap map_00, entities_00;
	private Level level_00;
	private SpriteSheet tileSheet;
	private ArrayList<String> levelNames = new ArrayList<String>();
	private Tilemap[] maps, entities;
	private int currentMap = 0;

	public PlayState(GameStateManager gsm) {
		super(gsm);
		this.tileSheet = PlatformGame.tileSheet;
		try {
			Scanner input = new Scanner(new File("res/maps/storyOrder"));
			int count = 0;
		    while (input.hasNext()) {
		      String word = input.next();
		      System.out.println(word + " " + count);
		      levelNames.add(word);
		      count = count + 1;
		    }
			input.close();
			int n = levelNames.size();
			if(n > 2 && n % 2 == 1) {
				n--;
			}
			maps = new Tilemap[n/2];
			entities = new Tilemap[n/2];
			for(int i = 0; i < n/2; i++) {
				maps[i] = new Tilemap("res/maps/" + levelNames.get(i*2), 16, 16, this.tileSheet);
				entities[i] = new Tilemap("res/maps/" + levelNames.get(i*2 +1), 16, 16, this.tileSheet);
			}
			
//			map_00 = new Tilemap("res/maps/map_00", 16, 16, this.tileSheet);
//			entities_00 = new Tilemap("res/maps/entities_00", 16, 16, this.tileSheet);
			map_00 = maps[0];
			entities_00 = entities[0];
			level_00 = new Level(map_00, entities_00);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
	}
	
	private void restart(){
		currentMap = (currentMap + 1) % maps.length;
		map_00 = maps[currentMap];
		entities_00 = entities[currentMap];
		level_00 = new Level(map_00, entities_00);
	}

	@Override
	public void update() {
		if(Keyboard.getKey(KeyEvent.VK_ESCAPE)){
			switchState(PlatformGame.MENU_STATE);
			return;
		}
		
		if(Mouse.getMouse().isButtonClicked(MouseButton.RIGHT))
			restart();
	
		level_00.update();
	}

	@Override
	public void draw(Renderer2D renderer) {
		level_00.draw(renderer);
	}

}
