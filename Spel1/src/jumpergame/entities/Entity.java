package jumpergame.entities;

import com.argetgames.arget2d.graphics.Image2D;
import com.argetgames.arget2d.graphics.Renderer2D;

public class Entity {
	
	protected int width, height;
	protected int x, y, z;
	protected int startZ;
	
	
	public Entity(int x, int y, int z, int width, int height){
		this.x = x - width / 2;
		this.y = y - height / 2;
		this.z = startZ = z;
		this.width = width;
		this.height = height;
	}
	
	private void respawn() {
		z = startZ;
	}
	
	public void update() {
		z--;
		if(z < 0)
			respawn();
	}
	
	public void draw(Renderer2D renderer) {
		if(z > 0)
			renderer.renderImage2D(x/z, y/z, width/z, height/z, Image2D.test);
	}
	

}
