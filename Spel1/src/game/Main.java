package game;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Main {
	
	private static JumperGame game;
	private static JFrame frame;

	public static void main(String[] args) {
		ImageIcon img = new ImageIcon("res/images/icon.png");
		frame = new JFrame("Game 1: Jumper");
		boolean useSleep = false;
		if(args.length > 0) {
			if(args[0].equals("useSleep"))
				useSleep = true;
		}
		game = new JumperGame(360, 240, 3, useSleep);
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

