package plattformrunner;

import java.awt.event.KeyEvent;

import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.arget2d.input.Keyboard;

public class Player {
	double xPos;
	double yPos;
	int keyCode[] = new int[3];
	boolean jumping = false;
	boolean  falling = false;
	private double jumpHeight = 100.0;
	private double dt = 1.0/60.0;
	private double vel_y = 0.0;
	private double secondsToPeak = 1.0/2;
	private double vel0_y = 2*jumpHeight / secondsToPeak;
	private double acc_y = ((-2) * (jumpHeight)) / (secondsToPeak*secondsToPeak);
	
	public Player(int startX, int startY, int leftKey, int rightKey, int jumpKey){
		xPos = startX;
		yPos = startY - 25;
		keyCode[0] = leftKey;
		keyCode[1] = rightKey;
		keyCode[2] = jumpKey;
	}
	public void draw(Renderer2D renderer, int mapScroll) {
		renderer.fillRect((int)xPos - mapScroll, (int)yPos, 25, 25, 0xFF0000FF);
		
	}
	public void update() {
		if(Keyboard.getKey(keyCode[2])) {
			startJump();
		}
		if(Keyboard.getKey(keyCode[1])) {
			xPos+=2;
		}
		if(Keyboard.getKey(keyCode[0])) {
			xPos-=2;
		}
		jump();
		if(!jumping) {
			fall();
		}
//		if(Keyboard.getKey(KeyEvent.VK_ENTER)) {
//			startJump();
//		}
		
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
