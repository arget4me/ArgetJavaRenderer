package plattformrunner;

import java.awt.event.KeyEvent;
import java.util.Random;

import com.argetgames.arget2d.graphics.Image2D;
import com.argetgames.arget2d.graphics.Renderer2D;

public class RandomMap extends Map {
	int platforms[];
	private Image2D background = new Image2D("res/images/background.png");
	private Image2D background2 = new Image2D("res/images/backgroundMountains.png");
	public int mapScroll = 0;
	public RandomMap() {
		platforms = new int[50];
		generatePlatform();
	}
	private void generatePlatform() {
		Random r = new Random();
		for(int i = 0 ; i < platforms.length ; i++) {
			platforms[i] = r.nextInt(3);	
		}		
		p = new Player(0,platforms[0]*50+50, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_SPACE);
		p2 = new Player(0,platforms[0]*50+50, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_W);
	}
	public void draw(Renderer2D renderer) {
		renderer.renderImage2D((-mapScroll/4)%background.width, 0, background);
		renderer.renderImage2D((-mapScroll/4)%background.width + background.width, 0, background);
		
		renderer.renderImage2D((-mapScroll/2)%background2.width, 0, background2.width , PlattformGame.globalHeight,background2);
		renderer.renderImage2D((-mapScroll/2)%background2.width + background2.width, 0, background2.width , PlattformGame.globalHeight,background2);
		for(int i = 0 ; i < platforms.length ; i++) {
			renderer.fillRect(i*(100+50)-mapScroll, platforms[i]*50+50, 100, 5, 0xFF432002);
		}
		
		p.draw(renderer, mapScroll);
		p2.draw(renderer, mapScroll);
	}
	public void update() {
		p.update();
		p.checkCollision(platforms);
		p2.update();
		p2.checkCollision(platforms);
		if(p.xPos>PlattformGame.globalWidth*2/3 + mapScroll) {
			mapScroll+=2;
		}
		else if(p2.xPos>PlattformGame.globalWidth*2/3 + mapScroll) {
			mapScroll+=2;
		}
		if(p.xPos+25<mapScroll) {
			System.out.println("player1 dead");
		}
		if(p2.xPos+25<mapScroll) {
			System.out.println("player2 dead");
		}
	}
}
