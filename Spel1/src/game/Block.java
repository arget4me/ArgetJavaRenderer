package game;

import java.util.Random;

import com.argetgames.arget2d.graphics.Renderer2D;

public class Block {
	public int x, y;
	private int startX;
	private int speed;
	private int color;
	
	public Block(int x, int y) {
		this.x = x;
		startX = x;
		this.y = y;
		respawn();
	}
	
	public void respawn() {
		x = startX;
		Random r = new Random();
		speed = r.nextInt(3)*3 + 1;
		color = (0xFFFF / speed) & 0xFFFF;
		color |= 0x7F220000;
		System.out.println(speed);
	}
	
	public void move() {
		x -= speed;
	}
	
	public void draw(Renderer2D renderer) {
		renderer.fillRect(x, y, 40, 70, color);
	}

}
