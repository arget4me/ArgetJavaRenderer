package com.argetgames.treasurehunter;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Main {
	
	private static TreasueHunterGame game;
	private static JFrame frame;

	public static void main(String[] args) {
		ImageIcon img = new ImageIcon("res/images/icon.png");
		frame = new JFrame("Game 2: TreasureHunter");
		game = new TreasueHunterGame(360, 240, 3);
		frame.add(game);
		frame.setResizable(false);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setIconImage(img.getImage());
		frame.setVisible(true);
		
		game.Start();
	}
}
