package plattformrunner;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {

	private static PlattformGame game;
	public static JFrame frame;

	public static void main(String[] args) {
		frame = new JFrame("PlatformRunner");
		boolean startServer = false;
		int scale = 3;
		if (args.length >= 1) {
			if (args[0].equals("startServer")) {
				startServer = true;
				scale = 1;
				frame.setTitle("PlatformServer");
			}
		}
		game = new PlattformGame(360, 240, scale, startServer);
		frame.add(game);
		frame.setResizable(false);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		game.Start();
	}

}