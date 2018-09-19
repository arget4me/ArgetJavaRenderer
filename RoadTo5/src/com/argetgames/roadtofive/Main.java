package com.argetgames.roadtofive;

import javax.swing.JFrame;

public class Main {

	public static PlatformGame game;
	public static JFrame mainFrame;
	
	public static void main(String []args){
		mainFrame = new JFrame("Road to 5");
		game = new PlatformGame(16*30*1, 9*30*1, 2);
		
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.add(game);
		mainFrame.pack();
		mainFrame.setResizable(false);
		mainFrame.setLocationRelativeTo(null);
		mainFrame = game.toggleStretchFullscreen(mainFrame);
		mainFrame.setVisible(true);
		game.Start();
	}
}
