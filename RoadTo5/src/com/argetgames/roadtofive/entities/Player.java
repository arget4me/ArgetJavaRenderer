package com.argetgames.roadtofive.entities;

import java.awt.event.KeyEvent;

import com.argetgames.arget2d.input.Keyboard;
import com.argetgames.arget2d.input.Mouse;
import com.argetgames.arget2d.input.Mouse.MouseButton;
import com.argetgames.roadtofive.PlatformGame;

public class Player extends Living {

	private int shootDelay = 0;
	private int shootsPerSecond = 6;
	private double regenPerSeconds = 1.0/2.0;
	private int regenDelay = (int)(PlatformGame.global_ups / regenPerSeconds);
	
	public Player(int x, int y, int width, int height, Level level) {
		super(x, y, width, height, level);
		moveSpeed = 1.5;
	}
	
	@Override
	public void behavior() {
		if(health < maxHealth) {
			if(regenDelay <= 0) {
				health++;
				regenDelay += (int)(PlatformGame.global_ups / regenPerSeconds);
			}else {
				regenDelay--;
			}
		}
		
		if (Keyboard.getKey(KeyEvent.VK_SPACE)) {
			startJump();
		}
		
		if (Keyboard.getKey(KeyEvent.VK_LEFT) || Keyboard.getKey(KeyEvent.VK_A)) {
			move(-moveSpeed, 0);
		} else if (Keyboard.getKey(KeyEvent.VK_RIGHT) || Keyboard.getKey(KeyEvent.VK_D)) {
			move(+moveSpeed, 0);
		}
		
		if(shootDelay <= 0) {
			if(Mouse.getMouse().isButtonPress(MouseButton.LEFT)) {
				int mx = Mouse.getMouseX() - PlatformGame.globalWidth/2;
				int my = Mouse.getMouseY() - PlatformGame.globalHeight/2;
				double angle = Math.atan2(my, mx);
				
				level.spawnProjectile(getCenterX(), getCenterY(), 5, angle, 5, getID());
				shootDelay = PlatformGame.global_ups / shootsPerSecond;
			}
		}else {
			shootDelay--;
		}
		
	}

}
