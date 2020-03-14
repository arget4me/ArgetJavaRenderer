package com.argetgames.arget2d.game;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import com.argetgames.arget2d.graphics.Camera2D;
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
	private final long NS_PER_UPDATE = 1000000000 / global_ups;
	public static int globalWidth, globalHeight;
	protected final int WIDTH, HEIGHT, SCALE;
	protected boolean running = false;
	private Thread gameThread;

	private BufferedImage displayImage;
	private int[] pixelBuffer;
	private BufferStrategy bs;
	protected Renderer2D renderer;
	private boolean useSleep;
	public static Camera2D camera;

	public Gameloop(int width, int height, int scale) {
		WIDTH = width;
		HEIGHT = height;
		SCALE = scale;
		init(true);
	}

	public Gameloop(int width, int height, int scale, boolean useSleep) {
		WIDTH = width;
		HEIGHT = height;
		SCALE = scale;
		init(useSleep);
	}

	private void init(boolean useSleep) {
		this.useSleep = useSleep;
		this.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		displayImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixelBuffer = ((DataBufferInt) displayImage.getRaster().getDataBuffer()).getData();
		renderer = new Renderer2D(WIDTH, HEIGHT, pixelBuffer);
		camera = renderer.camera;
		globalWidth = renderer.getWidth();
		globalHeight = renderer.getHeight();
		addKeyListener(Keyboard.getKeyboard());// TODO should also use process inputs so that inputs don't change during
												// updates
		addMouseListener(Mouse.getMouse());
		addMouseMotionListener(Mouse.getMouse());
	}

	public void Start() {
		if (!running) {
			gameThread = new Thread(this, "Arget2DGameloop");
			gameThread.start();

		}
	}

	public void Stop() {
		if (running) {
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
		if (useSleep) {
			loopWithParkNanosNoCatchup();
		} else {
			loopWithoutSleep();
		}

	}

	public void loopWithParkNanosNoCatchup() {
		if (debug_log)
			System.out.println("loopParkNanos()");
		running = true;
		long previous = System.nanoTime();
		long fpsTimer = previous;
		int frames = 0;
		long sleepTime;
		create();
		while (running) {
			update();

			render();
			frames++;
			if (debug_log) {
				if (System.nanoTime() - fpsTimer >= 1000) {
					System.out.println("FPS: " + frames);
					frames = 0;
					fpsTimer += 1000000000;
				}
			}
			previous += NS_PER_UPDATE;
			long now = System.nanoTime();
			sleepTime = (previous - now);
			if (sleepTime > 1) {
				java.util.concurrent.locks.LockSupport.parkNanos(sleepTime);
			}else if(sleepTime < 0){
				if(debug_log)
					System.out.println("frame drop!");
				previous = System.nanoTime();
			}
		}
	}

	public void loopWithoutSleep() {
		running = true;
		int updates = 0, frames = 0;
		long upsTimer = System.nanoTime();
		long secondsTimer = upsTimer;
		create();
		while (running) {
			while (System.nanoTime() - upsTimer >= NS_PER_UPDATE) {
				updates++;
				upsTimer += NS_PER_UPDATE;
				update();
			}
			frames++;
			render();

			if (System.nanoTime() - secondsTimer >= 1000000000) {
				secondsTimer += 1000000000;
				if (debug_log)
					System.out.println("FPS: " + frames + " | UPS: " + updates);
				updates = 0;
				frames = 0;
			}

		}
	}

	public void update() {
		updateGame();
	}

	private void create() {
		requestFocus();
		Mouse.setScale(getWidth()/(double)WIDTH, getHeight()/(double)HEIGHT);
		onCreate();
	}

	protected void onCreate() {
	}

	public abstract void updateGame();

	public abstract void draw();

	private void render() {
		bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		renderer.clear();
		// render stuff here..
		draw();
		
		//@NOTE: This is to support full screen
		g.drawImage(displayImage, 0, 0, getWidth(), getHeight(), null); 
//		g.drawImage(displayImage, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);

		g.dispose();
		bs.show();
	}
	
	public JFrame toggleFullscreen(JFrame frame){
		System.out.println("This function is removed");
		return frame;
		
		//Bad way this won't work reliably
		
//		if(frame == null)
//			return null;
//		
//		Image icon = frame.getIconImage();
//		String title = frame.getTitle();
//		boolean isFullscreen = (frame.getExtendedState() & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH;
//		boolean resizable = frame.isResizable();
//		int closeOperations = frame.getDefaultCloseOperation();		
//		
//		/** Must delete old frame and create a new to toggle undecorated. */
//		frame.dispose();
//		frame = null;
//		frame =  new JFrame();
//		/**@Note: can't be visible before setting full screen **/
//		frame.setVisible(false);
//		frame.setTitle(title);
//		if(icon != null)frame.setIconImage(icon);
//		
//		if(!isFullscreen){
//			if(debug_log)
//				System.out.println("Fullscreen");
//			frame.setUndecorated(true);
//			frame.setVisible(true);
//			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
//			setPreferredSize(frame.getSize());
//		}else {
//			if(debug_log)
//				System.out.println("Quit Fullscreen");
//			frame.setUndecorated(false);
//			frame.setVisible(true);
//			setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
//		}
//		
//		frame.add(this);
//		frame.setDefaultCloseOperation(closeOperations);
//		frame.setResizable(resizable);
//		frame.pack();
//		frame.setLocationRelativeTo(null);
//		displayImage = new BufferedImage(getWidth()/SCALE, getHeight()/SCALE, BufferedImage.TYPE_INT_RGB);
//		pixelBuffer = ((DataBufferInt) displayImage.getRaster().getDataBuffer()).getData();
//		renderer = new Renderer2D(getWidth()/SCALE, getHeight()/SCALE);
//		globalWidth = renderer.getWidth();
//		globalHeight = renderer.getHeight();
//		Mouse.setScale(SCALE, SCALE);
//		
//		requestFocus();
//		return frame;
	}
	
	public JFrame toggleStretchFullscreen(JFrame frame){
		System.out.println("This function is removed");
		return frame;
		//Bad way this won't work reliably
		
//		if(frame == null)
//			return null;
//		
//		Image icon = frame.getIconImage();
//		String title = frame.getTitle();
//		boolean isFullscreen = (frame.getExtendedState() & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH;
//		boolean resizable = frame.isResizable();
//		int closeOperations = frame.getDefaultCloseOperation();		
//		
//		/** Must delete old frame and create a new to toggle undecorated. */
//		frame.dispose();
//		frame = null;
//		frame =  new JFrame();
//		/**@Note: can't be visible before setting full screen **/
//		frame.setVisible(false);
//		frame.setTitle(title);
//		if(icon != null)frame.setIconImage(icon);
//		
//		if(!isFullscreen){
//			if(debug_log)
//				System.out.println("Fullscreen");
//			frame.setUndecorated(true);
//			frame.setVisible(true);
//			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
//			setPreferredSize(frame.getSize());
//		}else {
//			if(debug_log)
//				System.out.println("Quit Fullscreen");
//			frame.setUndecorated(false);
//			frame.setVisible(true);
//			setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
//		}
//		
//		frame.add(this);
//		frame.setDefaultCloseOperation(closeOperations);
//		frame.setResizable(resizable);
//		frame.pack();
//		frame.setLocationRelativeTo(null);
//		Mouse.setScale(getWidth()/(double)WIDTH, getHeight()/(double)HEIGHT);
//		
//		requestFocus();
//		return frame;
	}
	
	/* Gameloop test that aren't used anymore */

//	public void loopWithSleep() {
//		running = true;
//		long previous = System.nanoTime();
//		long fpsTimer = previous;
//		double lag = 0.0;
//		int updates = 0;
//		int frames = 0;
//		int tick = 0;
//		double wait;
//		create();
//		while (running) {
//			long current = System.nanoTime();
//			long elapsed = current - previous;
//			previous = current;
//			lag += elapsed;
//
//			int i = 0;
//			while (lag >= NS_PER_UPDATE) {
//				update();
//				updates++;
//				lag -= NS_PER_UPDATE;
//				i++;
//				if (i > 1)
//					tick++;
//			}
//			wait = (previous + (NS_PER_UPDATE) - System.nanoTime()) / 4000000.0;
//			try {
//				if (wait > 0)
//					Thread.sleep((long) wait);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//
//			render();
//			frames++;
//			if (System.nanoTime() - fpsTimer >= 1000000000) {
//				if (debug_log)
//					System.out.println("FPS: " + frames + " | UPS: " + updates + "| extra ticks: " + tick);
//				updates = 0;
//				frames = 0;
//				tick = 0;
//				fpsTimer += 1000000000;
//			}
//		}
//	}
//
//	public void loopWithParkNanos() {
//		running = true;
//		long previous = System.nanoTime();
//		long fpsTimer = previous;
//		double lag = 0.0;
//		int updates = 0;
//		int frames = 0;
//		int tick = 0;
//		long wait;
//		create();
//		while (running) {
//			long current = System.nanoTime();
//			long elapsed = current - previous;
//			previous = current;
//			lag += elapsed;
//
//			int i = 0;
//			while (lag >= NS_PER_UPDATE) {
//				update();
//				updates++;
//				lag -= NS_PER_UPDATE;
//				i++;
//				if (i > 1)
//					tick++;
//			}
//
//			render();
//			frames++;
//			wait = (previous + (NS_PER_UPDATE) - System.nanoTime()) / 4;
//			if (wait > 0) {
//				java.util.concurrent.locks.LockSupport.parkNanos(wait);
//			}
//			if (System.nanoTime() - fpsTimer >= 1000000000) {
//				if (debug_log)
//					System.out.println("FPS: " + frames + " | UPS: " + updates + "| extra ticks: " + tick);
//				updates = 0;
//				frames = 0;
//				tick = 0;
//				fpsTimer += 1000000000;
//			}
//		}
//	}

	/* Gameloop test that aren't used anymore */
}
