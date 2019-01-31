package com.argetgames.bomberman_multiplayer;

import com.argetgames.arget2d.game.Gameloop;
import com.argetgames.arget2d.gamestates.GameStateManager;
import com.argetgames.arget2d.graphics.SpriteSheet;
import com.argetgames.bomberman_multiplayer.gamestates.EditorState;
import com.argetgames.bomberman_multiplayer.gamestates.PlayState;

@SuppressWarnings("serial")
public class BombermanGame extends Gameloop {
	
	public final static int MENU_STATE = 0;
	public final static int PLAY_STATE = 1;
	public final static int EDITOR_STATE = 2;
	public final static int CAMPAIGN_EDITOR_STATE = 3;
	public static SpriteSheet tileSheet;
	public static SpriteSheet tileSheetSolidMap;
	public static SpriteSheet enitities;
	public static SpriteSheet playerSpritesheet;
	public static SpriteSheet bombSpritesheet;
	public static SpriteSheet explosionSpritesheet;
	
	
	private GameStateManager gsm;

	public BombermanGame(int width, int height, int scale, boolean useSleep) {
		super(width, height, scale, useSleep);
	}
	
	public BombermanGame(int width, int height, int scale) {
		super(width, height, scale, true);
	}
	
	protected void onCreate() {
		
		tileSheet = new SpriteSheet("res/images/tileset.png", 32, 32);
		tileSheetSolidMap = new SpriteSheet("res/images/tilesetSolidMap.png", 32, 32);
		enitities = new SpriteSheet("res/images/entities.png", 32, 32);
		playerSpritesheet = new SpriteSheet("res/images/playerSpritesheet.png", 32, 32);
		bombSpritesheet = new SpriteSheet("res/images/bombSpritesheet.png", 32, 32);
		explosionSpritesheet = new SpriteSheet("res/images/explosionSpritesheet.png", 32, 32);
		
		
		gsm = new GameStateManager(4);
		gsm.addAndSetState(new PlayState(gsm), PLAY_STATE);
		gsm.addState(new EditorState(gsm), EDITOR_STATE);
	}

	@Override
	public void updateGame() {
		gsm.update();
	}

	@Override
	public void draw() {
		gsm.draw(renderer);
	}

}
