package plattformrunner;

import javax.swing.JFrame;


public class Main {
	
	private static PlattformGame game;
	private static JFrame frame;

	public static void main(String[] args) {
		frame = new JFrame("PlatformRunner");
		boolean startServer = false;
		if(args.length >= 1){
			if(args[0].equals("startServer")){
				startServer = true;
				frame.setTitle("PlatformServer");
			}
		}
		game = new PlattformGame(360, 240, 1, startServer);
		frame.add(game);
		frame.setResizable(false);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		game.Start();
	}

}