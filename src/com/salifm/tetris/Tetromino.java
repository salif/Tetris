package com.salifm.tetris;

import java.util.Random;

public class Tetromino {
    enum Tetrominoes {
        NO_BLOCK, Z_SHAPE, S_SHAPE, I_SHAPE, T_SHAPE, O_SHAPE, L_SHAPE, J_SHAPE
    }

    ;

    private Tetrominoes tetrominoes;
    private int coords[][];                // current shape of a tetromino
    private int tetrominoTable[][][];

    public Tetromino() {
        coords = new int[4][2];
        tetrominoTable = new int[][][]{
                {{0, 0}, {0, 0}, {0, 0}, {0, 0}},        // NO_BLOCK
                {{0, -1}, {0, 0}, {-1, 0}, {-1, 1}},    // Z_SHAPE
                {{0, -1}, {0, 0}, {1, 0}, {1, 1}},    // S_SHAPE
                {{0, -1}, {0, 0}, {0, 1}, {0, 2}},    // I_SHAPE
                {{-1, 0}, {0, 0}, {1, 0}, {0, 1}},    // T_SHAPE
                {{0, 0}, {1, 0}, {0, 1}, {1, 1}},    // O_SHAPE
                {{-1, -1}, {0, -1}, {0, 0}, {0, 1}},    // L_SHAPE
                {{1, -1}, {0, -1}, {0, 0}, {0, 1}}    // J_SHAPE
        };

        setShape(Tetrominoes.NO_BLOCK);
    }

    // set tetromino shape
    public void setShape(Tetrominoes tetromino) {

        for (int i = 0; i < coords.length; i++) {
            for (int j = 0; j < coords[i].length; j++) {
                coords[i][j] = tetrominoTable[tetromino.ordinal()][i][j];
            }
        }

        tetrominoes = tetromino;
    }

    public void setRandomShape() {
        Random r = new Random();
        int x = Math.abs(r.nextInt()) % 7 + 1;
        setShape(Tetrominoes.values()[x]);
    }

    public Tetrominoes getShape() {
        return tetrominoes;
    }

    // coordinate transform functions
    public void setX(int idx, int x) {
        coords[idx][0] = x;
    }

    public void setY(int idx, int y) {
        coords[idx][1] = y;
    }

    public int getX(int idx) {
        return coords[idx][0];
    }

    public int getY(int idx) {
        return coords[idx][1];
    }

    public int minX() {
        int ret = 0;
        for (int i = 0; i < coords.length; i++) {
            ret = Math.min(ret, coords[i][0]);
        }
        return ret;
    }

    public int minY() {
        int ret = 0;
        for (int i = 0; i < coords.length; i++) {
            ret = Math.min(ret, coords[i][1]);
        }
        return ret;
    }

    // rotate a tetromino
    public Tetromino rotateLeft() {
        if (tetrominoes == Tetrominoes.O_SHAPE) {
            return this;
        }

        Tetromino ret = new Tetromino();
        ret.tetrominoes = tetrominoes;

        for (int i = 0; i < coords.length; i++) {
            ret.setX(i, getY(i));
            ret.setY(i, -getX(i));
        }

        return ret;
    }

    public Tetromino rotateRight() {
        if (tetrominoes == Tetrominoes.O_SHAPE) {
            return this;
        }

        Tetromino ret = new Tetromino();
        ret.tetrominoes = tetrominoes;

        for (int i = 0; i < coords.length; i++) {
            ret.setX(i, -getY(i));
            ret.setY(i, getX(i));
        }

        return ret;
    }
}
