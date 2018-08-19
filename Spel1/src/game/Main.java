package game;

import java.awt.event.KeyEvent;

import javax.swing.JFrame;

import com.argetgames.arget2d.game.Gameloop;
import com.argetgames.arget2d.graphics.Renderer2D;
import com.argetgames.arget2d.input.Keyboard;
import com.argetgames.arget2d.input.Mouse;
import com.argetgames.arget2d.input.Mouse.MouseButton;

public class Main {
	
	private static JumperGame game;
	private static JFrame frame;

	public static void main(String[] args) {
		frame = new JFrame("Game 1: Jumper");
		game = new JumperGame(360, 240, 2);
		frame.add(game);
		frame.setResizable(false);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		game.Start();
	}

}

