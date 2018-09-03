package com.argetgames.treasurehunter;

import com.argetgames.arget2d.game.Gameloop;
import com.argetgames.arget2d.graphics.Animation2D;
import com.argetgames.arget2d.graphics.SpriteSheet;
import com.argetgames.treasurehunter.entities.Camera;
import com.argetgames.treasurehunter.entities.Player;
import com.argetgames.treasurehunter.entities.World;

@SuppressWarnings("serial")
public class TreasureHunterGame extends Gameloop{
	
	private static SpriteSheet playerSheet;
	public static Animation2D player_animation[] = new Animation2D[4];
	private Player player;
	private World world;
	private Camera camera;
	public static int globalWidth ,globalHeight;
	
	public TreasureHunterGame(int width, int height, int scale) {
		super(width, height, scale, true);
		globalWidth = WIDTH;
		globalHeight = HEIGHT;
		init();
	}
	
	private void init() {
		debug_log = false;
		renderer.useAlpha(true);
		playerSheet = new SpriteSheet("res/images/playerSpritesheet.png", 32, 32);
		
		for(int row = 0; row < player_animation.length; row++) {
			int[] order = {0,1,0,2};
			for(int i = 0; i < order.length; i++) {
				order[i] += row*6;
				player_animation[row] = new Animation2D(playerSheet, order, 12);
				player_animation[row].play(true);
			}
		}
		
		
		world = new World();
		player = new Player(0, 0);
		camera = new Camera(player);
	}

	@Override
	public void updateGame() {
		player.update();
		camera.update();
	}

	
	@Override
	public void draw() {
		world.draw(renderer, camera);
		player.draw(renderer, camera);
	}

}
