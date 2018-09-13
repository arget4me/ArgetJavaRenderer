package plattformrunner;

import java.awt.event.KeyEvent;

import com.argetgames.arget2d.game.Gameloop;
import com.argetgames.arget2d.graphics.Image2D;
import com.argetgames.arget2d.input.Keyboard;

public class PlattformGame extends Gameloop {
	private Image2D img;
	public static int globalWidth, globalHeight;
	public PlattformGame(int width, int height, int scale) {
		super(width, height, scale, false);
		globalWidth = WIDTH;
		globalHeight = HEIGHT;
		img= new Image2D("res/images/craftPose.png");
	}
	Map m = new RandomMap();
	int pixel = 0;
	int x = 0;
	int y = 0;
	@Override
	public void updateGame() {
		m.update();
		for(int i = 0; i < img.width*img.height; i++) {
			pixel = img.getColor(i);
		}
		if(Keyboard.getKey(KeyEvent.VK_RIGHT)) {
			x++;		
		}
		if(Keyboard.getKey(KeyEvent.VK_LEFT)) {
			x--;
		}
		if(Keyboard.getKey(KeyEvent.VK_UP)) {
			y--;
		}
		if(Keyboard.getKey(KeyEvent.VK_DOWN)) {
			y++;
		}		
	}

	@Override
	public void draw() {
		renderer.useAlpha(true);
		m.draw(renderer);
//		renderer.fillRect(0, 0, 10, 10, 0xffff00ff);
//		renderer.renderImage2D(x, y, 150, 150 ,img);
	}

}
