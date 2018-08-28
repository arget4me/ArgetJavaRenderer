package game.entities;

import com.argetgames.arget2d.graphics.Image2D;
import com.argetgames.arget2d.graphics.Renderer2D;

import game.JumperGame;

public class Obstacle {

	private int width, height;
	private double x, y;
	private double startX;
	public double speed;
	
	private boolean passed = false;
	private boolean collided = false;
	
	public Obstacle(int x, int y, int width, int height, double speed){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.speed = speed;
		startX = this.x;
	}
	
	public void update(){
		x -= speed;
	}
	
	public int getRightX() {return (int)x + width;}
	public boolean getPassed() {
		boolean value = passed;
		passed = true;
		return value;
	}

	private boolean containsPoint(int xa, int ya){
		if(ya >= y && ya < y + height) {
			if(xa >= x && xa < x + width) {
				return true;
			}	
		}
		return false;
	}
	
	private boolean pointInsideRect(int x, int y, int width, int height, int xa, int ya) {
		if(ya >= y && ya < y + height) {
			if(xa >= x && xa < x + width) {
				return true;
			}	
		}
		return false;
	}
	
	public boolean collision(int rectX, int rectY, int rectWidth, int rectHeight) {
		if(collided)
			return false;
		if((width*width + height * height + rectWidth*rectWidth + rectHeight*rectHeight) < (x - rectX)*(x -rectX) + (y - rectY)*(y - rectY))
			return false;
		
		if(containsPoint(rectX, rectY) ||
				containsPoint(rectX + rectWidth-1, rectY) || 
				containsPoint(rectX, rectY + rectHeight -1) || 
				containsPoint(rectX + rectWidth-1, rectY + rectHeight -1)) {
			collided = true;
			return true;
		}
		
		//TODO: if rect is bigger -> return true
		if(width < rectWidth || height < rectHeight) {
			if(pointInsideRect(rectX, rectY, rectWidth, rectHeight, (int)x, (int)y) || 
					pointInsideRect(rectX, rectY, rectWidth, rectHeight, (int)x + width -1, (int)y) || 
					pointInsideRect(rectX, rectY, rectWidth, rectHeight, (int)x, (int)y + height -1) || 
					pointInsideRect(rectX, rectY, rectWidth, rectHeight, (int)x + width -1, (int)y + height -1)) {
				collided = true;
				return true;
			}
		}
		return false;
	}
	
	public void draw(Renderer2D renderer) {
		//renderer.fillRect((int)x, (int)y, width, height, 0xFFCC8866);
		renderer.renderImage2D((int)x, (int)y, width, height, JumperGame.obstacle);
	}

	public boolean hasCollided() {
		return collided;
	}

	
}


