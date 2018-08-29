package game;

import com.argetgames.arget2d.game.Gameloop;
import com.argetgames.arget2d.graphics.Image2D;

import game.gamestates.DefeatState;
import game.gamestates.Gamestate;
import game.gamestates.PlayState;

public class JumperGame extends Gameloop{
	boolean mousePress = false;
	public static PlayState play;
	public static DefeatState defeat;
	private static int globalWidth, globalHeight;
	
	public static Image2D runAnimation[] = new Image2D[6];
	public static Image2D fallAnimation[] = new Image2D[2];
	public static Image2D jumpAnimation[] = new Image2D[4];
	public static Image2D dieAnimation[] = new Image2D[7];
	public static Image2D ground, hills, sky, obstacle, life, defeatImg;
	private static Gamestate activeState;

	public JumperGame(int width, int height, int scale) {
		super(width, height, scale, true);
		globalWidth = width;
		globalHeight = height;
		loadContent();
		play = new PlayState(width, height);
		defeat = new DefeatState(width, height, 0);
		setState(play);
		renderer.useAlpha(true);
	}
	
	private void loadContent(){
		Image2D.test = new Image2D("res/images/test.png");
		for(int i = 0; i < runAnimation.length; i++){
			runAnimation[i] =  new Image2D("res/images/free_assets/adventurer/adventurer-run-0"+i+".png");
		}
		for(int i = 0; i < fallAnimation.length; i++){
			fallAnimation[i] =  new Image2D("res/images/free_assets/adventurer/adventurer-fall-0"+i+".png");
		}
		for(int i = 0; i < jumpAnimation.length; i++){
			jumpAnimation[i] =  new Image2D("res/images/free_assets/adventurer/adventurer-jump-0"+i+".png");
		}
		for(int i = 0; i < dieAnimation.length; i++){
			dieAnimation[i] =  new Image2D("res/images/free_assets/adventurer/adventurer-die-0"+i+".png");
		}
		ground =  new Image2D("res/images/ground.png");
		hills =  new Image2D("res/images/hills.png");
		sky =  new Image2D("res/images/clouds.png");
		obstacle =  new Image2D("res/images/obstacle.png");
		life  =  new Image2D("res/images/heart.png");
		defeatImg  =  new Image2D("res/images/defeat.png");
	}

	@Override
	public void updateGame() {
		activeState.update();
		
	}
	
	public static void setState(Gamestate state) {
		activeState = state;
	}

	@Override
	public void draw() {
		activeState.draw(renderer);
	}

	public static void resetPlaystate() {
		play = new PlayState(globalWidth, globalHeight);
		setState(play);
	}
	
}
