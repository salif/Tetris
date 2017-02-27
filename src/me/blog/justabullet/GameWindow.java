package me.blog.justabullet;

import java.awt.GridLayout;

import javax.swing.JFrame;

/**
 * Created by incognito on 16. 12. 09.
 */
public class GameWindow extends JFrame {

	// it is meaningless number for suppressing dumb warning.
	private static final long serialVersionUID = 8737819995837542594L;

	public GameWindow() {
		setTitle("Tetris :D");
		setSize(400, 814);
		setResizable(false);

		setLayout(new GridLayout(1, 2));

		// you can adjust timer resolution here. but it's ideal value for this game.
		GameBoardPanel gameBoard = new GameBoardPanel(this, 400);
		add(gameBoard);
		gameBoard.start();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}

}
