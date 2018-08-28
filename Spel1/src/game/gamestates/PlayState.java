package game.gamestates;

import java.util.Random;

import com.argetgames.arget2d.graphics.Renderer2D;

import game.JumperGame;
import game.entities.Obstacle;
import game.entities.Player;

public class PlayState extends Gamestate {
	
	private Player player;
	private Obstacle obst, obst2;
	private int displayWidth, displayHeight;
	private int groundY;
	private int obstH;
	private int obstW;
	private int distBetweenObstacles;
	private int score = 0;
	private Random random = new Random();
	
	//background images
	private int panning = 0;
	int backgroundPan;
	int hillsPan;
	int groundPan;
	int hillsY;
	int imgGroundY;
	
	
	public PlayState(int displayWidth, int displayHeight){
		this.displayWidth = displayWidth;
		this.displayHeight = displayHeight;
		init();
	}

	@Override
	protected void init() {
		groundY = (int)(displayHeight * (9/10.0));
		obstH = 60;
		obstW = 40;
		distBetweenObstacles = displayWidth;
		player = new Player((int)(displayWidth * (1/8.0)), groundY - 37, 37, 37);
		obst = new Obstacle(displayWidth, groundY - obstH, obstW, obstH,  4.0);
		obst2 = new Obstacle(obst.getRightX()+distBetweenObstacles, groundY - (obstH*2), 40, obstH*2,  4.0);
		//obst2 = new Obstacle((int)(displayWidth*1.5) + 10, groundY - (obstH*2), obstW, obstH*2, 4.0);
		hillsY = displayHeight - JumperGame.ground.height - JumperGame.hills.height +2;
		imgGroundY = displayHeight - JumperGame.ground.height;
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
			int h = random.nextInt(2)+1;
			obst = new Obstacle(obst2.getRightX()+distBetweenObstacles, groundY - (obstH*h), 40, obstH*h,  4.0);
		}
		if(obst2.getRightX() < 0) {
			int h = random.nextInt(2)+1;
			obst2 = new Obstacle(obst.getRightX()+distBetweenObstacles, groundY - (obstH*h), 40, obstH*h,  4.0);
		}
	}
	
	private void handleCollisions() {
		if(obst.collision(player.getX(), player.getY(), player.getWidth(), player.getHeight()) ||
				obst2.collision(player.getX(), player.getY(), player.getWidth(), player.getHeight()))
					player.hit();
	}
	

	@Override
	public void update() {
		
		player.update();
		if(player.isAlive()) {
			panning++;
			backgroundPan = -(panning % (JumperGame.sky.width-2));
			hillsPan = - ((2*panning) % (JumperGame.hills.width-2));
			groundPan = - ((4*panning) % (JumperGame.ground.width-2));
			handleObstacles();
			handleCollisions();
		}
		if(player.deathAnimationDone()) {
			JumperGame.defeat.setScore(score);
			JumperGame.setState(JumperGame.defeat);
		}
	}

	@Override
	public void draw(Renderer2D renderer) {
		
		renderer.renderImage2D(backgroundPan, 0, JumperGame.sky);
		renderer.renderImage2D(JumperGame.sky.width-2 + backgroundPan, 0, JumperGame.sky);
		
		
		
		renderer.renderImage2D(hillsPan, hillsY, JumperGame.hills);
		renderer.renderImage2D((JumperGame.hills.width-2) + hillsPan, hillsY, JumperGame.hills);
		
		
		
		renderer.renderImage2D(groundPan, imgGroundY, JumperGame.ground);
		renderer.renderImage2D((JumperGame.hills.width-2) + groundPan, imgGroundY, JumperGame.ground);
		
		obst.draw(renderer);
		obst2.draw(renderer);
		player.draw(renderer);
	}

}
