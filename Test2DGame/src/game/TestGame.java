package game;

import java.awt.event.KeyEvent;

import com.argetgames.arget2d.game.Gameloop;
import com.argetgames.arget2d.graphics.Image2D;
import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.arget2d.input.Keyboard;
import com.argetgames.arget2d.input.Mouse;
import com.argetgames.arget2d.input.Mouse.MouseButton;

public class TestGame extends Gameloop{
	int x = 0, y = 0;
	boolean mousePress = false;

	public TestGame(int width, int height, int scale) {
		super(width, height, scale);
		Image2D.test = new Image2D("res/images/test.png");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updateGame() {
		if(Keyboard.getKey(KeyEvent.VK_D)){ 
			x +=2;
		}
		if(Keyboard.getKey(KeyEvent.VK_A)){ 
			x -=2;
		}
		if(Keyboard.getKey(KeyEvent.VK_W)){ 
			y -=2;
		}
		if(Keyboard.getKey(KeyEvent.VK_S)){ 
			y +=2;
		}
		if(Mouse.getMouse().isButtonClicked(MouseButton.MIDDLE)){
			mousePress = !mousePress;
			renderer.useAlpha(mousePress);
		}
	}

	@Override
	public void draw() {
		int color = 0xAAFFFF00;
		renderer.fillRect(WIDTH / 2 - 20 + x+5,  HEIGHT  / 2 - 20 + y, 40, 40, 0xFFFFFFFF);
		renderer.fillRect(WIDTH / 2 - 20 + x,  HEIGHT  / 2 - 20 + y, 40, 40, color);
		if(Image2D.test != null)
			renderer.renderImage2D(10, 10, Image2D.test);
	}
	
}
