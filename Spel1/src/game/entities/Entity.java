package game.entities;

import com.argetgames.arget2d.graphics.Image2D;
import com.argetgames.arget2d.graphics.Renderer2D;

public class Entity {
	
	protected int width, height;
	protected double x, y, z;
	protected int startZ;
	
	
	public Entity(int x, int y, int z, int width, int height){
		this.x = x;
		this.y = y;
		this.z = startZ = z;
		this.width = width;
		this.height = height;
	}
	
	private void respawn() {
		z = startZ;
	}
	
	public void update() {
		z -= 0.05;
		if(z < 0.5)
			respawn();
	}
	
	public void draw(Renderer2D renderer) {
		if(z > 0) {
			int dw = (int)((width/2) / z);
			int dh =  (int)((height/2) / z);
			renderer.renderImage2D((int)x - dw, (int)y - dh, dw * 2, dh * 2, Image2D.test);
		}
	}
	

}
