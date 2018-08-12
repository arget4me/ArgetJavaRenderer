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
	int spawnX = 600;
	int x2 = spawnX, y2 = HEIGHT  / 2 +  -50;
	Block b = new Block(spawnX, y2);
	boolean mousePress = false;

	public TestGame(int width, int height, int scale) {
		super(width, height, scale);
		Image2D.test = new Image2D("res/images/test.png");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updateGame() {
		b.move();
		if(b.x < 0 - 40)
			b.respawn();
		
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
		//System.out.println("Y: " + y);
	}

	@Override
	public void draw() {
		int color = 0xAAFFFF00;
		renderer.fillRect(WIDTH / 2 - 20 + x,  HEIGHT  / 2 - 20 + y, 40, 40, color);
		b.draw(renderer);
	}
	
}
