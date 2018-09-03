package com.argetgames.arget2d.game;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.arget2d.input.Keyboard;
import com.argetgames.arget2d.input.Mouse;

public abstract class Gameloop extends Canvas implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static int global_ups = 60;
	public static boolean debug_log = true;
	private final long NS_PER_UPDATE = 1000000000/global_ups;
	protected final int WIDTH, HEIGHT, SCALE;
	protected boolean running = false;
	private Thread gameThread;
	
	private BufferedImage displayImage;
	private int[] pixelBuffer;
	private BufferStrategy bs;
	protected Renderer2D renderer;
	private boolean useSleep;

	public Gameloop(int width, int height, int scale) {
		WIDTH = width;
		HEIGHT = height;
		SCALE = scale;
		init(false);
	}
	
	public Gameloop(int width, int height, int scale, boolean useSleep) {
		WIDTH = width;
		HEIGHT = height;
		SCALE = scale;
		init(useSleep);
	}
	
	private void init(boolean useSleep) {
		this.useSleep = useSleep;
		this.setSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		displayImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixelBuffer = ((DataBufferInt) displayImage.getRaster().getDataBuffer()).getData();
		renderer = new Renderer2D(WIDTH, HEIGHT);
		addKeyListener(Keyboard.getKeyboard());//TODO should also use process inputs so that inputs don't change during updates
		addMouseListener(Mouse.getMouse());
		addMouseMotionListener(Mouse.getMouse());
		Mouse.setScale(SCALE);
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
		if(useSleep) {
			//loopWithSleep();
			//loopWithParkNanosNoCatchup();
			loopWithParkNanos();
		}else {
			loopWithoutSleep();
		}
		
	}
	
	public void loopWithoutSleep() {
		running = true;
		int updates = 0, frames = 0;
		long upsTimer = System.nanoTime();
		long secondsTimer = upsTimer;
		
		while(running){
			while(System.nanoTime() - upsTimer >= NS_PER_UPDATE){
				updates++;
				upsTimer += NS_PER_UPDATE;
				update();
			}
			frames++;
			render();
			
			if(System.nanoTime() - secondsTimer >= 1000000000){
				secondsTimer += 1000000000;
				if(debug_log)
					System.out.println("FPS: " + frames + " | UPS: " + updates);
				updates = 0;
				frames = 0;
			}
			
		}
	}
	

	public void loopWithSleep() {
		running = true;
		long previous = System.nanoTime();
		long fpsTimer = previous;
		double lag = 0.0;
		int updates = 0;
		int frames = 0;
		int tick = 0;
		double wait;
		while (running)
		{
			 long current = System.nanoTime();
			 long elapsed = current - previous;
			 previous = current;
			 lag += elapsed;
	
			 int i = 0;
			 while (lag >= NS_PER_UPDATE)
			 {
				 update();
				 updates++;
				 lag -= NS_PER_UPDATE;
				 i++;
				 if(i > 1)
					 tick++;
			 }
			 wait = (previous + (NS_PER_UPDATE) - System.nanoTime())/4000000.0;
			 try {
				 if(wait > 0)
					 Thread.sleep((long)wait);
			 } catch (InterruptedException e) {
				 e.printStackTrace();
			 }
			 
			 render();
			 frames++;
			 if(System.nanoTime() - fpsTimer >= 1000000000) {
				 if(debug_log)
					System.out.println("FPS: " + frames + " | UPS: " + updates + "| extra ticks: " + tick);
				 updates = 0;
				 frames = 0;
				 tick = 0;
				 fpsTimer += 1000000000;
			 }
		}
	}
	
	public void loopWithParkNanos() {
		running = true;
		long previous = System.nanoTime();
		long fpsTimer = previous;
		double lag = 0.0;
		int updates = 0;
		int frames = 0;
		int tick = 0;
		long wait;
		while (running)
		{
			 long current = System.nanoTime();
			 long elapsed = current - previous;
			 previous = current;
			 lag += elapsed;
	
			 int i = 0;
			 while (lag >= NS_PER_UPDATE)
			 {
				 update();
				 updates++;
				 lag -= NS_PER_UPDATE;
				 i++;
				 if(i > 1)
					 tick++;
			 }
			 
			 render();
			 frames++;
			 wait = (previous + (NS_PER_UPDATE) - System.nanoTime())/4;
			 if(wait > 0) {
				 java.util.concurrent.locks.LockSupport.parkNanos(wait);
			 }
			 if(System.nanoTime() - fpsTimer >= 1000000000) {
				 if(debug_log)
					System.out.println("FPS: " + frames + " | UPS: " + updates + "| extra ticks: " + tick);
				 updates = 0;
				 frames = 0;
				 tick = 0;
				 fpsTimer += 1000000000;
			 }
		}
	}
	
	public void loopWithParkNanosNoCatchup() {
		running = true;
		long previous = System.nanoTime();
		long fpsTimer = previous;
		int updates = 0;
		int frames = 0;
		long sleepTime;
		while (running)
		{
			 update();
			 updates++;
			
			 render();
			 frames++;
			 previous += NS_PER_UPDATE;
			 sleepTime = (previous - System.nanoTime());
			 if(sleepTime > 0) {
				 java.util.concurrent.locks.LockSupport.parkNanos(sleepTime);
			 }
			 
			 if(System.nanoTime() - fpsTimer >= 1000) {
				 if(debug_log)
					System.out.println("FPS: " + frames + " | UPS: " + updates);
				 updates = 0;
				 frames = 0;
				 fpsTimer += 1000000000;
			 }
		}
	}
	
	
	public void update(){
		requestFocus();
		updateGame();
		
	}
	
	public abstract void updateGame();
	
	public abstract void draw();
	
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
		g.drawImage(displayImage, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		
		g.dispose();
		bs.show();
	}

}
