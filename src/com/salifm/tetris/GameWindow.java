package com.salifm.tetris;

import java.awt.GridLayout;
import javax.swing.JFrame;

public class GameWindow extends JFrame {

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
