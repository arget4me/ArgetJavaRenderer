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
		if(score > this.score)
			this.score = score;
	}

	@Override
	public void draw(Renderer2D renderer) {
		for(int i = 0; i < score; i++) {
			renderer.fillRect((4*i) % displayWidth, (4 * i) % displayHeight, 3, 3, 0xFF44FFEE);
		}
	}

}
