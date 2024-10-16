package com.main;

import com.main.Graphics.Game;
import com.main.Utils.Keybindings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serializable;

public class Main implements Serializable {
    public static final String VERSION = "1.0";
    public static final String TITLE = "Game";
    public static final Dimension dimension = new Dimension(500, 500);
    public static void main(String[] args) {
        JFrame gameFrame = new JFrame(TITLE);
        gameFrame.setSize(dimension);
        Game game = new Game();
        gameFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        gameFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                game.stop();
                System.exit(0);
            }
        });
        gameFrame.setResizable(false);
        gameFrame.add(game);
        gameFrame.pack();
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setVisible(true);
        game.start();
    }
}