package game.gamestates;

import java.awt.event.KeyEvent;

import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.arget2d.input.Keyboard;
import com.argetgames.arget2d.input.Mouse;
import com.argetgames.arget2d.input.Mouse.MouseButton;

import game.JumperGame;
import game.Main;

public class DefeatState extends Gamestate {

	private int score;
	
	public DefeatState(int score) {
		this.score = score;
	}
	
	@Override
	protected void init() {
		
	}

	@Override
	public void update() {
		if(Mouse.getMouse().isButtonClicked(MouseButton.MIDDLE)){
			Main.frame = Main.game.toggleFullscreen(Main.frame);
			Main.game.play.calculateScale();
		}
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
		renderer.fillRect(0, 0, JumperGame.globalWidth, JumperGame.globalHeight, 0xDD000000);
		int size = 16;
		for(int i = 0; i < score; i++) {
			int x = (i * size) % (JumperGame.globalWidth - size);
			int y = (i * size) / (JumperGame.globalWidth - size);
			renderer.renderImage2D(x % (JumperGame.globalWidth - size), y * size % (JumperGame.globalWidth - size), 15, 15, JumperGame.obstacle);
		}
		renderer.fillRect(0, 0, JumperGame.globalWidth, JumperGame.globalWidth, 0x44000000);
		renderer.renderImage2D(0, 0, JumperGame.globalWidth, JumperGame.globalHeight, JumperGame.defeatImg);
	}

}
