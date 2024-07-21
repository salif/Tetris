package com.github.salif.tetris;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.SwingUtilities;

public class App {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new GameWindow(ResourceBundle.getBundle("m", Locale.getDefault())));
    }
}
