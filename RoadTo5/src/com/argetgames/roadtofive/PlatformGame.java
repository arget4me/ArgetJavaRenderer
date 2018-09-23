package com.argetgames.roadtofive;

import com.argetgames.arget2d.game.Gameloop;
import com.argetgames.arget2d.gamestates.GameStateManager;
import com.argetgames.arget2d.graphics.Image2D;
import com.argetgames.arget2d.graphics.SpriteSheet;
import com.argetgames.arget2d.graphics.TextRenderer;
import com.argetgames.arget2d.input.Mouse;
import com.argetgames.arget2d.input.Mouse.MouseButton;
import com.argetgames.arget2d.tilemap.TilemapEditor;
import com.argetgames.roadtofive.gamestates.EditorState;
import com.argetgames.roadtofive.gamestates.MenuState;
import com.argetgames.roadtofive.gamestates.PlayState;

@SuppressWarnings("serial")
public class PlatformGame extends Gameloop {

	public static Image2D aspect, tempPlayer;
	private static SpriteSheet fontSheet, tileSheet, tileSheetSolidMap;
	public static TextRenderer textRenderer;

	private GameStateManager gsm;

	public final static int MENU_STATE = 0;
	public final static int PLAY_STATE = 1;
	public final static int EDITOR_STATE = 2;
	
	public PlatformGame(int width, int height, int scale) {
		super(width, height, scale, true);
		debug_log = false;
	}

	protected void onCreate() {
		
		aspect = new Image2D("res/images/aspectTest.png");
		tempPlayer = new Image2D("res/images/tempPlayer.png");
//		fontSheet = new SpriteSheet("res/images/free_assets/SpriteFonts/SpriteFonts16x16.png", 16, 16);
//		textRenderer = new TextRenderer(fontSheet, " !\"#$£€%&\'()*+,-./0123456789;:<=>?°`^@ABCDEFGHIJKLMNOPQRSTUVWXYZ|~[\\]_", 0xFFFFFFFF);
		fontSheet = new SpriteSheet("res/images/8bitFont.png", 8, 8);
		textRenderer = new TextRenderer(fontSheet, " !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~", 0xFFFFFFFF);
		
//		tileSheet = new SpriteSheet("res/images/free_assets/SunnyLand/tileset.png", 16, 16);
//		tileSheetSolidMap = new SpriteSheet("res/images/free_assets/SunnyLand/tileset_solidMap.png", 16, 16);
		
		tileSheet = new SpriteSheet("res/images/tileset.png", 16, 16);
		tileSheetSolidMap = new SpriteSheet("res/images/tilesetSolidMap.png", 16, 16);
		
		gsm = new GameStateManager(3);
		gsm.addAndSetState(new MenuState(gsm), MENU_STATE);
		gsm.addState(new PlayState(gsm, tileSheet), PLAY_STATE);
		gsm.addState(new EditorState(gsm, tileSheet, tileSheetSolidMap), EDITOR_STATE);
		

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
		renderer.fillRect(0, 0, globalWidth, globalHeight, 0xFF888888);
		gsm.draw(renderer);
	}

}
