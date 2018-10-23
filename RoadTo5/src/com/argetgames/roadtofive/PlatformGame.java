package com.argetgames.roadtofive;

import com.argetgames.arget2d.game.Gameloop;
import com.argetgames.arget2d.gamestates.GameStateManager;
import com.argetgames.arget2d.graphics.Image2D;
import com.argetgames.arget2d.graphics.SpriteSheet;
import com.argetgames.arget2d.graphics.TextRenderer;
import com.argetgames.arget2d.input.Mouse;
import com.argetgames.arget2d.input.Mouse.MouseButton;
import com.argetgames.roadtofive.gamestates.CampaignEditorState;
import com.argetgames.roadtofive.gamestates.EditorState;
import com.argetgames.roadtofive.gamestates.MenuState;
import com.argetgames.roadtofive.gamestates.PlayState;
import com.argetgames.roadtofive.sound.SoundAPI;

@SuppressWarnings("serial")
public class PlatformGame extends Gameloop {

	public static Image2D aspect, tempPlayer;
	private static SpriteSheet fontSheet;
	public static SpriteSheet tileSheet, tileSheetSolidMap, enitities;
	public static SpriteSheet playerAnimation, enemiesAnimation; 
	public static TextRenderer textRenderer;

	private GameStateManager gsm;

	public final static int MENU_STATE = 0;
	public final static int PLAY_STATE = 1;
	public final static int EDITOR_STATE = 2;
	public final static int CAMPAIGN_EDITOR_STATE = 3;
	
	public PlatformGame(int width, int height, int scale) {
		super(width, height, scale, true);
		debug_log = false;
	}

	protected void onCreate() {
		aspect = new Image2D("res/images/aspectTest.png");
		tempPlayer = new Image2D("res/images/tempPlayer2.png");
		playerAnimation = new SpriteSheet("res/images/tempPlayerAnimation.png", 16, 16);
		enemiesAnimation = new SpriteSheet("res/images/enemiesAnimation.png", 16, 16);

		fontSheet = new SpriteSheet("res/images/8bitFont.png", 8, 8);
		textRenderer = new TextRenderer(fontSheet, " !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~", 0xFFFFFFFF);
		
		tileSheet = new SpriteSheet("res/images/tileset.png", 16, 16);
		tileSheetSolidMap = new SpriteSheet("res/images/tilesetSolidMap.png", 16, 16);
		
		enitities = new SpriteSheet("res/images/entities.png", 16, 16);
		
		SoundAPI.loadSound("explosion_1.wav");
		SoundAPI.loadSound("hurt_1.wav");
		SoundAPI.loadSound("jump_1.wav");
		SoundAPI.loadSound("pickup_1.wav");
		SoundAPI.loadSound("shoot_1.wav");
		SoundAPI.loadSound("testSound.wav");
		
		gsm = new GameStateManager(4);
		gsm.addAndSetState(new MenuState(gsm), MENU_STATE);
		gsm.addState(new PlayState(gsm), PLAY_STATE);
		gsm.addState(new EditorState(gsm), EDITOR_STATE);
		gsm.addState(new CampaignEditorState(gsm), CAMPAIGN_EDITOR_STATE);
	}

	@Override
	public void updateGame() {
		if(Mouse.getMouse().isButtonClicked(MouseButton.MIDDLE))
			Main.mainFrame = toggleStretchFullscreen(Main.mainFrame);
		gsm.update();
	}

	@Override
	public void draw() {
		renderer.useColorMask(true);
		renderer.useCamera(false);
		renderer.fillRect(0, 0, globalWidth, globalHeight, 0xFF8888BB);
		gsm.draw(renderer);
	}

}
