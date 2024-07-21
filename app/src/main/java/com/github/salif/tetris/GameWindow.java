package com.github.salif.tetris;

import java.awt.GridLayout;
import java.util.ResourceBundle;

import javax.swing.JFrame;

public class GameWindow implements Runnable {

    private final JFrame mainFrame;
    private final GameBoardPanel gameBoard;

    public GameWindow(ResourceBundle m) {
        this.mainFrame = new JFrame();
        this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.mainFrame.setTitle(m.getString("title"));
        this.mainFrame.setSize(400, 814);
        this.mainFrame.setResizable(false);

        this.mainFrame.setLayout(new GridLayout(1, 2));

        // you can adjust timer resolution here. but it's ideal value for this game.
        this.gameBoard = new GameBoardPanel(this, m, 500);
        this.mainFrame.add(gameBoard);
    }

    @Override
    public void run() {
        this.mainFrame.setLocationRelativeTo(null);
        this.mainFrame.setVisible(true);
        this.gameBoard.start();
    }
}
