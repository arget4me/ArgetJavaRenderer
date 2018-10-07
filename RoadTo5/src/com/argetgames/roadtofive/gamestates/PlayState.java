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
//			map_00 = new Tilemap("res/maps/map_00", 16, 16, this.tileSheet);
//			entities_00 = new Tilemap("res/maps/entities_00", 16, 16, this.tileSheet);
			map_00 = new Tilemap("res/maps/" + levelNames.get(0), 16, 16, this.tileSheet);
			entities_00 = new Tilemap("res/maps/" + levelNames.get(1), 16, 16, this.tileSheet);
			level_00 = new Level(map_00, entities_00);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
	}
	
	private void restart(){
		map_00 = new Tilemap("res/maps/map_00", 16, 16, tileSheet);
		entities_00 = new Tilemap("res/maps/entities_00", 16, 16, this.tileSheet);
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
