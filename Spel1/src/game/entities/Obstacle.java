package game.entities;

import com.argetgames.arget2d.graphics.Image2D;
import com.argetgames.arget2d.graphics.Renderer2D;

public class Obstacle {

	private int width, height;
	private double x, y;
	private double startX;
	public double speed;
	
	public Obstacle(int x, int y, int width, int height, double speed){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.speed = speed;
		startX = this.x;
	}
	
	private void respawn(){
		x = startX;
	}
	
	public void update(){
		x -= speed;
		if(x < 0 - width){
			respawn();
		}
			
	}
	
	public void draw(Renderer2D renderer) {
		renderer.fillRect((int)x, (int)y, width, height, 0xFFCC8866);
	}
}


