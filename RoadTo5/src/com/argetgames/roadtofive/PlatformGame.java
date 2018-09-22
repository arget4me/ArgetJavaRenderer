package com.argetgames.roadtofive;

import java.awt.event.KeyEvent;

import com.argetgames.arget2d.game.Gameloop;
import com.argetgames.arget2d.graphics.Camera2D;
import com.argetgames.arget2d.graphics.Image2D;
import com.argetgames.arget2d.graphics.SpriteSheet;
import com.argetgames.arget2d.graphics.TextRenderer;
import com.argetgames.arget2d.input.Keyboard;
import com.argetgames.arget2d.input.Mouse;
import com.argetgames.arget2d.input.Mouse.MouseButton;
import com.argetgames.arget2d.tilemap.TilemapEditor;

@SuppressWarnings("serial")
public class PlatformGame extends Gameloop {

	public static Image2D aspect, tempPlayer;
	private static SpriteSheet fontSheet, tileSheet, tileSheetSolidMap;
	public static TextRenderer textRenderer;
	private TilemapEditor mapEditor;

	public PlatformGame(int width, int height, int scale) {
		super(width, height, scale, true);
//		debug_log = false;
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
		mapEditor = new TilemapEditor(400, 400, 16, 16, tileSheet);
		mapEditor.addSolidMap(tileSheetSolidMap);
	}

	@Override
	public void updateGame() {
		if(Mouse.getMouse().isButtonClicked(MouseButton.MIDDLE))
			Main.mainFrame = toggleStretchFullscreen(Main.mainFrame);
		mapEditor.update();
	}

	@Override
	public void draw() {
		renderer.useColorMask(true);
		renderer.useCamera(false);
		renderer.fillRect(0, 0, globalWidth, globalHeight, 0xFF888888);
		renderer.useCamera(true);
		mapEditor.draw(renderer);
		renderer.useColorMask(false);
	}

}
