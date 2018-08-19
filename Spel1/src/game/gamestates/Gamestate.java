package game.gamestates;

import com.argetgames.arget2d.graphics.Renderer2D;

public abstract class Gamestate {
	
	public Gamestate(){
	}
	
	protected abstract void init();
	public abstract void update();
	public abstract void draw(Renderer2D renderer);
	

}
