package game.gamestates;

import java.awt.event.KeyEvent;

import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.arget2d.input.Keyboard;

import game.JumperGame;
import game.entities.Obstacle;
import game.entities.Player;

public class PlayState extends Gamestate {
	
	private Player player;
	private Obstacle obst, obst2;
	private int displayWidth, displayHeight;
	private int groundY;
	private int obstH;
	private int distBetweenObstacles;
	private int score = 0;
	
	public PlayState(int displayWidth, int displayHeight){
		this.displayWidth = displayWidth;
		this.displayHeight = displayHeight;
		init();
	}

	@Override
	protected void init() {
		groundY = (int)(displayHeight * (9/10.0));
		obstH = 60;
		distBetweenObstacles = displayWidth;
		player = new Player((int)(displayWidth * (1/8.0)), groundY - 37, 37, 37);
		obst = new Obstacle(displayWidth, groundY - obstH, 40, obstH,  4.0);
		obst2 = new Obstacle((int)(displayWidth*1.5) + 10, groundY - (obstH*2), 40, obstH*2, 4.0);
	}
	
	private void handleObstacles() {
		obst.update();
		obst2.update();
		
		if(obst.getRightX() < player.getX()) {
			if(!obst.getPassed()) {
				if(!obst.hasCollided())
					score++;
			}
		}
		if(obst2.getRightX() < player.getX()) {
			if(!obst2.getPassed()) {
				if(!obst2.hasCollided())
					score++;
			}
		}
		
		if(obst.getRightX() < 0) {
			obst = new Obstacle(obst2.getRightX()+distBetweenObstacles, groundY - obstH, 40, obstH,  4.0);
		}
		if(obst2.getRightX() < 0) {
			obst2 = new Obstacle(obst.getRightX()+distBetweenObstacles, groundY - (obstH*2), 40, obstH*2,  4.0);
		}
	}
	
	private void handleCollisions() {
		if(obst.collision(player.getX(), player.getY(), player.getWidth(), player.getHeight()) ||
				obst2.collision(player.getX(), player.getY(), player.getWidth(), player.getHeight()))
					player.hit();
	}
	

	@Override
	public void update() {
		//TODO: remove hack for testing collision.
		if(!player.isAlive()) {
			if(Keyboard.getKey(KeyEvent.VK_ESCAPE))
				player.revive();
			else
				return;
		}
		
		
		player.update();
		handleObstacles();
		handleCollisions();
		if(!player.isAlive()) {
			JumperGame.defeat.setScore(score);
			JumperGame.setState(JumperGame.defeat);
		}
	}

	@Override
	public void draw(Renderer2D renderer) {
		obst.draw(renderer);
		obst2.draw(renderer);
		player.draw(renderer);
	}

}
