package com.argetgames.roadtofive;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Main {

	public static PlatformGame game;
	public static JFrame mainFrame;
	private static GraphicsDevice device;
	
	public static void main(String []args){
		game = new PlatformGame(16*30*1, 9*30*1, 2);
		
		//Set fullscreen
		{	
			GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice[] devices = env.getScreenDevices();
			
			String deviceNames[] = new String[devices.length + 1];
			deviceNames[0] = "No fullscreen";
			for(int i = 0; i < devices.length; i++) {
				deviceNames[i+1] = devices[i].toString();
			}
			boolean useFullscreen;
			int output = JOptionPane.showOptionDialog(null, "Please select monitor", "Fullscreen", 0, JOptionPane.QUESTION_MESSAGE, null, deviceNames, deviceNames[0]);
			if(output < 1 || output >= deviceNames.length) {
				useFullscreen = false;
				mainFrame = new JFrame();
			} else {
				useFullscreen = true;
				device = devices[output-1];
				mainFrame = new JFrame(device.getDefaultConfiguration());
				mainFrame.setUndecorated(true);
				mainFrame.setIgnoreRepaint(true);
			}
			
			mainFrame.setTitle("Road to 5");
			mainFrame.add(game);
			
			if(useFullscreen) {
				device.setFullScreenWindow(mainFrame);
			}else {
				mainFrame.pack();
				mainFrame.setLocationRelativeTo(null);
			}
		}	
		
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setResizable(false);
		mainFrame.setVisible(true);
		
		game.Start();
	}
}
