package plattformrunner;

import com.argetgames.arget2d.graphics.Image2D;
import com.argetgames.arget2d.graphics.Renderer2D;

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
	private int color = 0xFF000000;
	public int speed;
	public static int count = 1;
	private static Image2D player1 = new Image2D("res/images/player_00.png"); 
	private static Image2D player2 = new Image2D("res/images/player_01.png");
	private int playerNr = 0;
	private static int nextPlayerNr = 0;
	
	
	public NetworkPlayer(int startX, int startY){
		xPos = startX;
		yPos = startY - 25;
		speed = 0;
		color += (0xFF * count);
		count += 255;
		playerNr = nextPlayerNr++;
	}
	public void draw(Renderer2D renderer, int mapScroll) {
		
		if(playerNr % 2 == 0) {
			renderer.renderImage2D((int)xPos - mapScroll, (int)yPos, 25, 25, player1);
		}else {
			renderer.renderImage2D((int)xPos - mapScroll, (int)yPos, 25, 25, player2);	
		}
			
//		renderer.fillRect((int)xPos - mapScroll, (int)yPos, 25, 25, color);
		
	}
	
	public void update(NetworkController controller) {
		if(controller.get(NetworkKey.JUMP)) {
			startJump();
		}
		if(controller.get(NetworkKey.RIGHT)) {
			xPos+=2 + speed;
		}
		if(controller.get(NetworkKey.LEFT)) {
			xPos-=2 + speed;
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
				if(yPos + 25 > platforms[i]*50+100 && yPos + 25< platforms[i]*50+100 + 20) {
					vel_y = 0;
					jumping = false;
					falling = false;
					yPos = platforms[i]*50+100-25;
					return;
				}
			}
			if(xPos + 25 > i*(100+50) && xPos + 25 < i*(100+50) + 100) {
				if(yPos + 25 > platforms[i]*50+100 && yPos + 25< platforms[i]*50+100 + 20) {
					vel_y = 0;
					jumping = false;
					falling = false;
					yPos = platforms[i]*50+100-25;
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
