package plattformrunner;

import com.argetgames.arget2d.game.Gameloop;

public class PlattformGame extends Gameloop {

	public PlattformGame(int width, int height, int scale) {
		super(width, height, scale, false);

	}

	@Override
	public void updateGame() {
		
	}

	@Override
	public void draw() {
		renderer.fillRect(0, 0, 10, 10, 0x88FFFF00);
	}

}
