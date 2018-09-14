package plattformrunner;

import java.awt.event.KeyEvent;

import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.arget2d.input.Keyboard;

import plattformrunner.NetworkController.NetworkKey;

public class NetworkPlayer {
	public double xPos;
	public double yPos;
	boolean jumping = false;
	boolean  falling = false;
	private double jumpHeight = 100.0;
	private double dt = 1.0/60.0;
	private double vel_y = 0.0;
	private double secondsToPeak = 1.0/2;
	private double vel0_y = 2*jumpHeight / secondsToPeak;
	private double acc_y = ((-2) * (jumpHeight)) / (secondsToPeak*secondsToPeak);
	
	public NetworkPlayer(int startX, int startY){
		xPos = startX;
		yPos = startY - 25;
	}
	public void draw(Renderer2D renderer, int mapScroll) {
		renderer.fillRect((int)xPos - mapScroll, (int)yPos, 25, 25, 0xFF0000FF);
		
	}
	public void update(NetworkController controller) {
		if(controller.get(NetworkKey.JUMP)) {
			startJump();
		}
		if(controller.get(NetworkKey.RIGHT)) {
			xPos+=2;
		}
		if(controller.get(NetworkKey.LEFT)) {
			xPos-=2;
		}
		jump();
		if(!jumping) {
			fall();
		}
		
	}
	private void jump(){
		if(jumping) {
			yPos -= vel_y * dt + (1.0/2.0) * acc_y * dt * dt;
			vel_y += acc_y * dt;
		}

	}
	private void startJump(){
		if(jumping) {
			return;
		}
		jumping = true;
		vel_y = vel0_y;
	}
	public void checkCollision(int platforms[]) {
		for(int i = 0 ; i < platforms.length ; i++) {
			if(xPos > i*(100+50) && xPos < i*(100+50) + 100) {
				if(yPos + 25 > platforms[i]*50+50 && yPos + 25< platforms[i]*50+50 + 20) {
					vel_y = 0;
					jumping = false;
					falling = false;
					yPos = platforms[i]*50+50-25;
					return;
				}
			}
			if(xPos + 25 > i*(100+50) && xPos + 25 < i*(100+50) + 100) {
				if(yPos + 25 > platforms[i]*50+50 && yPos + 25< platforms[i]*50+50 + 20) {
					vel_y = 0;
					jumping = false;
					falling = false;
					yPos = platforms[i]*50+50-25;
					return;
				}
			}
		}	
		falling = true;
	}
	private void fall(){
		if(falling) {
			yPos -= vel_y * dt + (1.0/2.0) * acc_y * dt * dt;
			vel_y += acc_y * dt;
			if(vel_y > 1) {
				vel_y = 1;
			}
		}

	}
}
