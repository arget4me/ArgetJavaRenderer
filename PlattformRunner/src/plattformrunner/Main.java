package plattformrunner;

import javax.swing.JFrame;


public class Main {
	
	private static PlattformGame game;
	private static JFrame frame;

	public static void main(String[] args) {
		frame = new JFrame("PlattformRunner");
		boolean useSleep = false;
		if(args.length > 0) {
			if(args[0].equals("useSleep"))
				useSleep = true;
		}
		game = new PlattformGame(360, 240, 2);
		frame.add(game);
		frame.setResizable(false);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		game.Start();
	}

}