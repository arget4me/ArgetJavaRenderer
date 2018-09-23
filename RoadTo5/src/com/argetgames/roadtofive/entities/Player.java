package com.argetgames.roadtofive.entities;

import java.awt.event.KeyEvent;

import com.argetgames.arget2d.input.Keyboard;
import com.argetgames.arget2d.tilemap.Tilemap;

public class Player extends Entity {

	public Player(int x, int y, int width, int height, Tilemap map) {
		super(x, y, width, height, 16 * 3.0, 1.0 / 3.0, map);
	}

	@Override
	public void behavior() {
		if (Keyboard.getKey(KeyEvent.VK_LEFT) || Keyboard.getKey(KeyEvent.VK_A)) {
			move(-1, 0);
		} else if (Keyboard.getKey(KeyEvent.VK_RIGHT) || Keyboard.getKey(KeyEvent.VK_D)) {
			move(+1, 0);
		}

		if (Keyboard.getKey(KeyEvent.VK_SPACE)) {
			startJump();
		}
	}

}
