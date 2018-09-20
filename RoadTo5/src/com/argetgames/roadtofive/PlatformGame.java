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
	private static SpriteSheet fontSheet, tileSheet;
	public static TextRenderer textRenderer;
	private TilemapEditor mapEditor;

	public PlatformGame(int width, int height, int scale) {
		super(width, height, scale, true);
//		debug_log = false;
	}

	protected void onCreate() {
		
		aspect = new Image2D("res/images/aspectTest.png");
		tempPlayer = new Image2D("res/images/tempPlayer.png");
		fontSheet = new SpriteSheet("res/images/free_assets/SpriteFonts/SpriteFonts16x16.png", 16, 16);
		textRenderer = new TextRenderer(fontSheet, " !\"#$£€%&\'()*+,-./0123456789;:<=>?°`^@ABCDEFGHIJKLMNOPQRSTUVWXYZ|~[\\]_", 0xFFFFFFFF);
		fontSheet = new SpriteSheet("res/images/8bitFont.png", 8, 8);
		textRenderer = new TextRenderer(fontSheet, " !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~", 0xFFFFFFFF);
		tileSheet = new SpriteSheet("res/images/free_assets/SunnyLand/tileset.png", 16, 16);
		mapEditor = new TilemapEditor(4000, 20, 16, 16, tileSheet);
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
		renderer.renderImage2D(0, 0, globalWidth, globalHeight, aspect);
		renderer.useCamera(true);
		mapEditor.draw(renderer);
		renderer.renderImage2D(50, 50, tempPlayer);
		renderer.renderImage2D(150, 50, 64, 64, tempPlayer);
		renderer.renderImage2D(50, 150, tempPlayer, 0xFFFF00FF, 0xFFFFFF00);
		renderer.renderImage2D(150, 150, 64, 64, tempPlayer, 0xFFFF00FF, 0xFFFFFF00);
		renderer.useColorMask(false);
	}

}
