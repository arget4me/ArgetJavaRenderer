package com.argetgames.roadtofive.entities;

import com.argetgames.arget2d.graphics.Animation2D;
import com.argetgames.arget2d.graphics.Image2D;
import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.roadtofive.PlatformGame;
import com.argetgames.roadtofive.sound.SoundAPI;

public class Pickup extends Living {

	Animation2D anim;
	
	public Pickup(int x, int y, int width, int height, Level level) {
		super(x, y, width, height, level);
		teamID = OTHER_TEAM_ID;
		dynamicSolid = false;
		applyGravity = false;
		{
			int frames[] = {12};
			anim = new Animation2D(PlatformGame.enitities, frames, 1);
			anim.play(true);
		}
	}

	@Override
	protected void behavior() {
		//animation...
		anim.update();
	}
	

	public void applyBuff(Living living) {
		SoundAPI.testPlaySound("pickup_1.wav", 0.7f);
		dead = true;
		living.health += 50;
		if(living.health > living.maxHealth)living.health = living.maxHealth;
	}
	
	public void draw(Renderer2D renderer){
		draw(renderer, 0xFFFFFFFF);
	}

	public void draw(Renderer2D renderer, int color) {
		drawHealthBar(renderer);
		renderer.useCamera(true);
		Image2D frame = anim.getCurrentFrame();
		renderer.renderImage2D(x, y, width, height, frame);
		renderer.useCamera(false);
	}

}
