package com.github.salif.tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GameBoardPanel extends JPanel implements ActionListener {

    private static final long serialVersionUID = 6802492405004738658L;
    private final int BoardWidth = 10; // game board x size
    private final int BoardHeight = 22; // game board y size

    // game status & timer
    private Timer timer;
    private boolean isFallingDone = false;
    private boolean isStarted = false;
    private boolean isPaused = false;
    private int currentScore = 0; // removed lines == score

    // position of current block
    private int curX = 0;
    private int curY = 0;

    // current tetromino
    private Tetromino curBlock;

    // logical game block
    private Tetrominoes[] gameBoard;
    private Color[] colorTable;

    // adjusting game status
    private String currentStatus;
    private String currentLevel;
    private int currentTimerResolution;

    private final ResourceBundle m;

    public GameBoardPanel(GameWindow tetrisFrame, ResourceBundle m, int timerResolution) {
        setFocusable(true);
        setBackground(new Color(0, 30, 30));
        curBlock = new Tetromino();
        timer = new Timer(timerResolution, this);
        timer.start(); // activate timer
        currentTimerResolution = timerResolution;
        this.m = m;

        gameBoard = new Tetrominoes[BoardWidth * BoardHeight];

        // colour of tetrominoes
        colorTable = new Color[]{
            new Color(0, 0, 0),
            new Color(164, 135, 255),
            new Color(255, 128, 0),
            new Color(255, 0, 0),
            new Color(32, 128, 255),
            new Color(255, 0, 255),
            new Color(255, 255, 0),
            new Color(0, 255, 0)
        };

        // keyboard listener
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!isStarted || curBlock.getShape() == Tetrominoes.NO_BLOCK) {
                    return;
                }

                int keycode = e.getKeyCode();

                if (keycode == 'p' || keycode == 'P') {
                    pause();
                    return;
                }

                if (isPaused) {
                    return;
                }

                // Colemak
                switch (keycode) {
                    case 'a', 'A', KeyEvent.VK_LEFT ->
                        isMovable(curBlock, curX - 1, curY);
                    case 's', 'S', KeyEvent.VK_RIGHT ->
                        isMovable(curBlock, curX + 1, curY);
                    case 'w', 'W', KeyEvent.VK_UP ->
                        isMovable(curBlock.rotateRight(), curX, curY);
                    case 'r', 'R', KeyEvent.VK_DOWN ->
                        advanceOneLine();
                    case KeyEvent.VK_SPACE ->
                        advanceToEnd();
                    case 'p', 'P' ->
                        pause();
                }

            }
        });

        initBoard();
    }

    // adjusting game level
    private void setResolution() {
        // fix me later! it's lame :P"

        switch (currentScore / 10) {
            case 10 ->
                currentTimerResolution = 100;
            case 9 ->
                currentTimerResolution = 140;
            case 8 ->
                currentTimerResolution = 180;
            case 7 ->
                currentTimerResolution = 220;
            case 6 ->
                currentTimerResolution = 260;
            case 5 ->
                currentTimerResolution = 300;
            case 4 ->
                currentTimerResolution = 340;
            case 3 ->
                currentTimerResolution = 380;
            case 2 ->
                currentTimerResolution = 420;
            case 1 ->
                currentTimerResolution = 460;
        }

        timer.setDelay(currentTimerResolution);

    }

    // initialize game board
    private void initBoard() {
        for (int i = 0; i < BoardWidth * BoardHeight; i++) {
            gameBoard[i] = Tetrominoes.NO_BLOCK;
        }
    }

    // timer callback
    @Override
    public void actionPerformed(ActionEvent e) {
        if (isFallingDone) {
            isFallingDone = !isFallingDone; // toggle status
            newTetromino();
        } else {
            advanceOneLine();
        }
    }

    public void start() {
        if (isPaused) {
            return;
        }

        isStarted = true;
        isFallingDone = false;
        currentScore = 0;
        initBoard();

        newTetromino();
        timer.start();
    }

    public void pause() {
        if (!isStarted) {
            return;
        }

        isPaused = !isPaused;
        if (isPaused) {
            timer.stop();
        } else {
            timer.start();
        }

        repaint();
    }

    // calculates actual size of tetromino on screen
    private int blockWidth() {
        return (int) getSize().getWidth() / BoardWidth;
    }

    private int blockHeight() {
        return (int) getSize().getHeight() / BoardHeight;
    }

    // current tetromino position in array (atom)
    Tetrominoes curTetrominoPos(int x, int y) {
        return gameBoard[(y * BoardWidth) + x];
    }

    @Override
    public void paint(Graphics g) {

        super.paint(g);

        if (!isPaused) {
            currentStatus = this.m.getString("score") + ": " + currentScore;
            currentLevel = this.m.getString("level") + ": " + (currentScore / 10 + 1);
        } else {
            currentStatus = this.m.getString("paused");
            currentLevel = "";
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 28));
        g.drawString(currentStatus, 15, 35);
        g.drawString(currentLevel, 15, 70);

        Dimension size = getSize();
        int boardTop = (int) size.getHeight() - BoardHeight * blockHeight();

        // rendering - shadow of tetromino
        int tempY = curY;
        while (tempY > 0) {
            if (!atomIsMovable(curBlock, curX, tempY - 1, false)) {
                break;
            }
            tempY--;
        }
        for (int i = 0; i < 4; i++) {
            int x = curX + curBlock.getX(i);
            int y = tempY - curBlock.getY(i);
            drawTetromino(g, 0 + x * blockWidth(), boardTop + (BoardHeight - y - 1) * blockHeight(),
                    curBlock.getShape(),
                    true);
        }

        // rendering - game board
        for (int i = 0; i < BoardHeight; i++) {
            for (int j = 0; j < BoardWidth; j++) {
                Tetrominoes shape = curTetrominoPos(j, BoardHeight - i - 1);
                if (shape != Tetrominoes.NO_BLOCK) {
                    drawTetromino(g, 0 + j * blockWidth(), boardTop + i * blockHeight(), shape, false);
                }
            }
        }

        // rendering - current tetromino
        if (curBlock.getShape() != Tetrominoes.NO_BLOCK) {
            for (int i = 0; i < 4; i++) {
                int x = curX + curBlock.getX(i);
                int y = curY - curBlock.getY(i);
                drawTetromino(g, 0 + x * blockWidth(), boardTop + (BoardHeight - y - 1) * blockHeight(),
                        curBlock.getShape(), false);
            }
        }

    }

    private void drawTetromino(Graphics g, int x, int y, Tetrominoes bs, boolean isShadow) {
        Color curColor = colorTable[bs.ordinal()];

        if (!isShadow) {
            g.setColor(curColor);
            g.fillRect(x + 1, y + 1, blockWidth() - 2, blockHeight() - 2);
        } else {
            g.setColor(curColor.darker().darker());
            g.fillRect(x + 1, y + 1, blockWidth() - 2, blockHeight() - 2);
        }
    }

    private void removeFullLines() {
        int fullLines = 0;

        for (int i = BoardHeight - 1; i >= 0; i--) {
            boolean isFull = true;

            for (int j = 0; j < BoardWidth; j++) {
                if (curTetrominoPos(j, i) == Tetrominoes.NO_BLOCK) {
                    isFull = false;
                    break;
                }
            }

            if (isFull) {
                ++fullLines;
                for (int k = i; k < BoardHeight - 1; k++) {
                    for (int l = 0; l < BoardWidth; ++l) {
                        gameBoard[(k * BoardWidth) + l] = curTetrominoPos(l, k + 1);
                    }
                }
            }
        }

        if (fullLines > 0) {
            currentScore += fullLines;
            isFallingDone = true;
            curBlock.setShape(Tetrominoes.NO_BLOCK);
            setResolution();
            repaint();
        }

    }

    // true - actual tetromino pos
    // flase - shadow pos
    private boolean atomIsMovable(Tetromino chkBlock, int chkX, int chkY, boolean flag) {
        for (int i = 0; i < 4; i++) {
            int x = chkX + chkBlock.getX(i);
            int y = chkY - chkBlock.getY(i);
            if (x < 0 || x >= BoardWidth || y < 0 || y >= BoardHeight) {
                return false;
            }
            if (curTetrominoPos(x, y) != Tetrominoes.NO_BLOCK) {
                return false;
            }
        }

        if (flag) {
            curBlock = chkBlock;
            curX = chkX;
            curY = chkY;
            repaint();
        }

        return true;
    }

    private boolean isMovable(Tetromino chkBlock, int chkX, int chkY) {
        return atomIsMovable(chkBlock, chkX, chkY, true);
    }

    private void newTetromino() {
        curBlock.setRandomShape();
        curX = BoardWidth / 2 + 1;
        curY = BoardHeight - 1 + curBlock.minY();

        if (!isMovable(curBlock, curX, curY)) {
            curBlock.setShape(Tetrominoes.NO_BLOCK);
            timer.stop();
            isStarted = false;
            gameOver();
        }
    }

    private void tetrominoFixed() {
        for (int i = 0; i < 4; i++) {
            int x = curX + curBlock.getX(i);
            int y = curY - curBlock.getY(i);
            gameBoard[(y * BoardWidth) + x] = curBlock.getShape();
        }

        removeFullLines();

        if (!isFallingDone) {
            newTetromino();
        }
    }

    private void advanceOneLine() {
        if (!isMovable(curBlock, curX, curY - 1)) {
            tetrominoFixed();
        }
    }

    private void advanceToEnd() {
        int tempY = curY;
        while (tempY > 0) {
            if (!isMovable(curBlock, curX, tempY - 1)) {
                break;
            }
            --tempY;
        }
        tetrominoFixed();
    }

    private void gameOver() {
        int r = JOptionPane.showConfirmDialog(this, this.currentStatus + "\n" + this.currentLevel + "\n" + this.m.getString("restart"), this.m.getString("gameover"), JOptionPane.YES_NO_OPTION);
        if (r == JOptionPane.YES_OPTION) {
            start();
        }
    }

}
