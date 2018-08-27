package game.entities;

import java.awt.event.KeyEvent;

import com.argetgames.arget2d.graphics.Image2D;
import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.arget2d.input.Keyboard;

import game.JumperGame;

public class Player {
	
	private int width, height;
	private double x, y;
	private boolean jumping = false;
	private boolean doubleJumping = false;
	private boolean handledInput = false;
	private boolean falling = false;
	private double startY;
	private double posY;
	private double jumpHeight = 80.0;
	private double dt = 1.0/60.0;
	private double vel_y = 0.0;
	private double secondsToPeak = 1.0/3;
	private double vel0_y = 2*jumpHeight / secondsToPeak;
	private double acc_y = ((-2) * (jumpHeight)) / (secondsToPeak*secondsToPeak);
	
	//animation hack...
	private int i = 0;
	private int count = 1;
	//animation hack...


	//gameplay
	private int livesLeft = 3;
	private boolean alive = true;
	
	public Player(int x, int y, int width, int height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		startY = this.y;
		posY = 0.0;
	}
	
	public void update() {
		count++;
		if(count % 5 == 0){
			i++;
		}
		handleInput();
		if(jumping){
			jump();
		}
	}
	
	private void handleInput(){
		if(Keyboard.getKey(KeyEvent.VK_SPACE)){
			if(!handledInput){
				handledInput = true;
				if(!jumping){
					startJump();
				}else if(!doubleJumping){
					startDoubleJump();
				}
			}
		}else {
			handledInput = false;
		}
	}
	
	private void jump(){
		posY += vel_y * dt + (1.0/2.0) * acc_y * dt * dt;
		vel_y += acc_y * dt;
		if(!falling && vel_y <= 0){
			i = 0;
			falling = true;
		}
		if(posY < 0){
			stopJump();
		}
		y = startY - posY;
	}
	
	private void stopJump(){
		posY = 0.0;
		jumping = false;
		doubleJumping = false;
		falling = false;
	}
	
	private void startJump(){
		i = 0;
		jumping = true;
		vel_y = vel0_y;
	}
	private void startDoubleJump(){
		i = 0;
		doubleJumping = true;
		falling = false;
		vel_y = vel0_y;
	}
	
	public void hit() {
		livesLeft--;
		if(livesLeft <= 0)
			alive = false;
	}
	
	//TODO: only for testing
	public void revive() {
		alive = true;
	}

	public boolean isAlive() {return alive; }
	public int getX() {return (int)x+width/3; }
	public int getY() {return (int)y+height/5; }
	public int getWidth() {return width/3; }
	public int getHeight() {return height*3/5; }
	
	public void draw(Renderer2D renderer) {
		/*Max Jump heights*/
		/*
		renderer.fillRect((int)x, (int)startY - (int)jumpHeight, width, height, 0x22FF00FF);
		renderer.fillRect((int)x, (int)startY - (int)jumpHeight*2, width, height, 0x22FF00FF);
		*/
		
		/*Render box*/
		//renderer.fillRect((int)x, (int)y, width, height, 0x88FF00FF);
		
		/*Hit box*/
		if(alive)
			renderer.fillRect(getX(), getY(), getWidth(), getHeight(), 0x22FF00FF);
		else
			renderer.fillRect(getX(), getY(), getWidth(), getHeight(), 0x88FF00FF);
		/*Animation*/
		if(jumping){
			if(falling){
				renderer.renderImage2D((int)x, (int)y, width, height, JumperGame.fallAnimation[i%2]);
			}else {
				renderer.renderImage2D((int)x, (int)y, width, height, JumperGame.jumpAnimation[3]);
			}
		}else {
			renderer.renderImage2D((int)x, (int)y, width, height, JumperGame.runAnimation[i%6]);
		}
		
		
	}

}
