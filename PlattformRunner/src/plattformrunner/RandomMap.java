package plattformrunner;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import com.argetgames.arget2d.graphics.Image2D;
import com.argetgames.arget2d.graphics.Renderer2D;

public class RandomMap extends Map {
	int platforms[];
	private Image2D background = new Image2D("res/images/background.png");
	private Image2D background2 = new Image2D("res/images/backgroundMountains.png");
	public int mapScroll = 0;
	public NetworkPlayer p, p2;

	public final int mapSize = 50;
	
	public RandomMap() {
		platforms = new int[mapSize];
		generatePlatform();
	}

	private void generatePlatform() {
		Random r = new Random();
		for (int i = 0; i < platforms.length; i++) {
			platforms[i] = r.nextInt(3);
		}
		// p = new Player(0,platforms[0]*50+50, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT,
		// KeyEvent.VK_SPACE);
		// p2 = new Player(0,platforms[0]*50+50, KeyEvent.VK_A, KeyEvent.VK_D,
		// KeyEvent.VK_W);
		p = new NetworkPlayer(0, platforms[0] * 50 + 50);
		p2 = new NetworkPlayer(0, platforms[0] * 50 + 50);
	}

	public void draw(Renderer2D renderer) {
		renderer.renderImage2D((-mapScroll / 4) % background.width, 0, background);
		renderer.renderImage2D((-mapScroll / 4) % background.width + background.width, 0, background);

		renderer.renderImage2D((-mapScroll / 2) % background2.width, 0, background2.width, PlattformGame.globalHeight,
				background2);
		renderer.renderImage2D((-mapScroll / 2) % background2.width + background2.width, 0, background2.width,
				PlattformGame.globalHeight, background2);
		for (int i = 0; i < platforms.length; i++) {
			renderer.fillRect(i * (100 + 50) - mapScroll, platforms[i] * 50 + 50, 100, 5, 0xFF432002);
		}

		p.draw(renderer, mapScroll);
		p2.draw(renderer, mapScroll);
	}

	public void update(ArrayList<Client> clients) {
		if (clients.size() < 1)
			return;
		p.update(clients.get(0).controller);
		p.checkCollision(platforms);
		if (clients.size() >= 2)
			p2.update(clients.get(1).controller);
		p2.checkCollision(platforms);
		if (p.xPos > PlattformGame.globalWidth * 2 / 3 + mapScroll) {
			mapScroll += 2;
		} else if (p2.xPos > PlattformGame.globalWidth * 2 / 3 + mapScroll) {
			mapScroll += 2;
		}
		if (PlattformGame.debug_log) {
			if (p.xPos + 25 < mapScroll) {
				System.out.println("player1 dead");
			}
			if (p2.xPos + 25 < mapScroll) {
				System.out.println("player2 dead");
			}
		}
	}
	
	public void registerMap(String msg) {
		String[] parts = msg.split(",");
		if(parts.length >= mapSize){
			for(int i = 1; i < mapSize; i++) {
				platforms[i-1] = Integer.parseInt(parts[i]);
			}
		}
	}

	public void registerInput(String msg) {
		String[] parts = msg.split(",");
		if(parts.length >= 6){
			int mScroll = Integer.parseInt(parts[1]);
			int pX = Integer.parseInt(parts[2]);
			int pY = Integer.parseInt(parts[3]);
			int p2X  = Integer.parseInt(parts[4]);
			int p2Y  = Integer.parseInt(parts[5]);
			
			mapScroll = mScroll;
			p.xPos = pX;
			p.yPos = pY;
			p2.xPos = p2X;
			p2.yPos = p2Y;
		}
	}

	public String getMapData() {
		String mapData = "MAPDATA";
		for(int i = 0; i < mapSize; i++) {
			mapData += "," + platforms[i];
		}
		mapData += ",";
		return mapData;
	}
}
