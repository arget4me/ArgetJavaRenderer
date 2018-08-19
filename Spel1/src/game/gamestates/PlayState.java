package game.gamestates;

import com.argetgames.arget2d.graphics.Renderer2D;

import game.entities.Obstacle;
import game.entities.Player;

public class PlayState extends Gamestate {
	
	private Player player;
	private Obstacle obst, obst2;
	private int displayWidth, displayHeight;
	
	public PlayState(int displayWidth, int displayHeight){
		this.displayWidth = displayWidth;
		this.displayHeight = displayHeight;
		init();
	}

	@Override
	protected void init() {
		int groundY = (int)(displayHeight * (9/10.0));
		player = new Player((int)(displayWidth * (1/8.0)), groundY - 37, 37, 37);
		int obstH = 60;
		obst = new Obstacle(displayWidth, groundY - obstH, 40, obstH,  4.0);
		obst2 = new Obstacle((int)(displayWidth*1.5) + 10, groundY - (obstH*2), 40, obstH*2, 4.0);
	}

	@Override
	public void update() {
		player.update();
		obst.update();
		obst2.update();
	}

	@Override
	public void draw(Renderer2D renderer) {
		obst.draw(renderer);
		obst2.draw(renderer);
		player.draw(renderer);
	}

}
