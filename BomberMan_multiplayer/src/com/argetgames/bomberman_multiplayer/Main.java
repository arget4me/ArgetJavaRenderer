package com.argetgames.bomberman_multiplayer;


import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Main {

	public static JFrame frame;
	private static GraphicsDevice device;
	public static boolean useFullscreen = false;
	private static final int WIDTH = 1280, HEIGHT = 720;//WIDTH / 16 * 9;
	public static boolean startServer = false;
	
	public static void main(String[] args) {		
		
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] devices = env.getScreenDevices();
		
		String deviceNames[] = new String[devices.length + 1];
		deviceNames[0] = "No fullscreen";
		for(int i = 0; i < devices.length; i++) {
			deviceNames[i+1] = devices[i].toString();
		}
		
		int output = JOptionPane.showOptionDialog(null, "Please select monitor", "Fullscreen", 0, JOptionPane.QUESTION_MESSAGE, null, deviceNames, deviceNames[0]);
		if(output < 1 || output >= deviceNames.length) {
			useFullscreen = false;
			frame = new JFrame();
		} else {
			useFullscreen = true;
			device = devices[output-1];
			frame = new JFrame(device.getDefaultConfiguration());
			frame.setUndecorated(true);
			frame.setIgnoreRepaint(true);
		}
		
		int host = JOptionPane.showConfirmDialog(null, "Do you want to host game?", "Host?", JOptionPane.YES_NO_OPTION);
		if(host == JOptionPane.YES_OPTION){
			startServer = true;
		}
		
		BombermanGame game = new BombermanGame(WIDTH, HEIGHT, 1, true);
		frame.setTitle("Bomberman Multiplayer");
		frame.add(game);
		
		if(useFullscreen) {
			device.setFullScreenWindow(frame);
		}else {
			frame.pack();
			frame.setLocationRelativeTo(null);
		}
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
		game.debug_log = false;
		
		game.Start();
	}
}
