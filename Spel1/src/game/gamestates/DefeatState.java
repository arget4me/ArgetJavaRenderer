package game.gamestates;

import java.awt.event.KeyEvent;

import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.arget2d.input.Keyboard;

import game.JumperGame;

public class DefeatState extends Gamestate {

	private int score;
	private int displayWidth, displayHeight;
	
	public DefeatState(int displayWidth, int displayHeight, int score) {
		this.score = score;
		this.displayWidth = displayWidth;
		this.displayHeight = displayHeight;
	}
	
	@Override
	protected void init() {
		
	}

	@Override
	public void update() {
		if(Keyboard.getKey(KeyEvent.VK_ESCAPE)) {
			JumperGame.resetPlaystate();
		}
	}
	
	public void setScore(int score) {
		this.score = score;
	}

	@Override
	public void draw(Renderer2D renderer) {
		JumperGame.play.draw(renderer);
		renderer.fillRect(0, 0, displayWidth, displayHeight, 0xDD000000);
		int size = 16;
		for(int i = 0; i < score; i++) {
			int x = (i * size) % (displayWidth - size);
			int y = (i * size) / (displayWidth - size);
			renderer.renderImage2D(x % (displayWidth - size), y * size % (displayHeight - size), 15, 15, JumperGame.obstacle);
		}
		renderer.fillRect(0, 0, displayWidth, displayHeight, 0x44000000);
		renderer.renderImage2D(0, 0, JumperGame.defeatImg);
	}

}
