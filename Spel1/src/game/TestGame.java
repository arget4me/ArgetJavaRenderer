package game;

import java.awt.event.KeyEvent;

import com.argetgames.arget2d.game.Gameloop;
import com.argetgames.arget2d.graphics.Image2D;
import com.argetgames.arget2d.input.Keyboard;
import com.argetgames.arget2d.input.Mouse;
import com.argetgames.arget2d.input.Mouse.MouseButton;

import game.entities.Entity;

public class TestGame extends Gameloop{
	int x = 0, y = 0;
	int spawnX = 600;
	int x2 = spawnX, y2 = HEIGHT  / 2 +  -50;
	Entity e = new Entity(WIDTH / 2, HEIGHT / 2, 10, 100, 100);
	boolean mousePress = false;

	public TestGame(int width, int height, int scale) {
		super(width, height, scale);
		Image2D.test = new Image2D("res/images/test.png");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updateGame() {
		e.update();
		
		if(Keyboard.getKey(KeyEvent.VK_SPACE)) {
			y -= 5;
		}else {
			if(y < 0) {
				y++;
				y++;
				y++;
				y++;
				y++;
			}
		}
		
		if(Mouse.getMouse().isButtonClicked(MouseButton.RIGHT)){
			mousePress = !mousePress;
			renderer.useAlpha(mousePress);
		}
	}

	@Override
	public void draw() {
		int color = 0xAAFFFF00;
		e.draw(renderer);
		renderer.fillRect(WIDTH / 2 - 20 + x,  HEIGHT / 6 * 4 + y, 40, 40, color);
	}
	
}
