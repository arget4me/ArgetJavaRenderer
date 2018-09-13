package plattformrunner;

import com.argetgames.arget2d.graphics.Renderer2D;

public class Map {
	public Player p,p2;
	public Map(){
		
	}
	
	public void draw(Renderer2D renderer) {
		renderer.fillRect(0, 0, PlattformGame.globalWidth, PlattformGame.globalHeight, 0xFFFF00FF);
	}
	public void update() {
		
	}
}
