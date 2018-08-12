package com.argetgames.arget2d.game;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import com.argetgames.arget2d.graphics.Image2D;
import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.arget2d.input.Keyboard;
import com.argetgames.arget2d.input.Mouse;
import com.argetgames.arget2d.input.Mouse.MouseButton;

public abstract class Gameloop extends Canvas implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected final int WIDTH, HEIGHT, SCALE;
	protected boolean running = false;
	private Thread gameThread;
	
	private BufferedImage displayImage;
	private int[] pixelBuffer;
	private BufferStrategy bs;
	protected Renderer2D renderer;

	public Gameloop(int width, int height, int scale) {
		WIDTH = width;
		HEIGHT = height;
		SCALE = scale;
		this.setSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		displayImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixelBuffer = ((DataBufferInt) displayImage.getRaster().getDataBuffer()).getData();
		renderer = new Renderer2D(WIDTH, HEIGHT);
		addKeyListener(Keyboard.getKeyboard());//TODO should also use process inputs so that inputs don't change during updates
		addMouseListener(Mouse.getMouse());
	}
	
	public void Start(){
		if(!running) {
			gameThread = new Thread(this, "Arget2DGameloop");
			gameThread.start();
			
		}
	}
	
	public void Stop(){
		if(running) {
			running = false;
			try {
				gameThread.join();
				System.exit(0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}

	@Override
	public void run() {
		running = true;
		long upsTime = 1000000000/60;
		long minRenderTime = 1000000000/90;
		int updates = 0, frames = 0;
		
		long upsTimer = System.nanoTime();
		long renderTimer = upsTimer;
		long secondsTimer = upsTimer;
		
		while(running){
			while(System.nanoTime() - upsTimer >= upsTime){
				updates++;
				upsTimer += upsTime;
				update();
			}
			frames++;
			render();
			
			if(System.nanoTime() - secondsTimer >= 1000000000){
				secondsTimer += 1000000000;
				//System.out.println("Updates: " + updates + " | Frames: " + frames + "\n");
				updates = 0;
				frames = 0;
			}
			
		}
	}
	
	
	public void update(){
		requestFocus();
		updateGame();
		
	}
	
	public abstract void updateGame();
	
	public abstract void draw();/*{
		
	}*/
	
	private void render(){
		bs = getBufferStrategy();
		if(bs == null){
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		renderer.clear();
		//render stuff here..
		draw();
		for(int i = 0; i < pixelBuffer.length; i++){
			pixelBuffer[i] = renderer.getPixel(i);
		}
		//g.drawImage(displayImage, 0, 0, getWidth(), getHeight(), null);
		g.drawImage(displayImage, 0, 0, WIDTH, HEIGHT, null);
		
		g.dispose();
		bs.show();
		
	}

}
