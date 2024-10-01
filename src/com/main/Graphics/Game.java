package com.main.Graphics;

import com.main.Keybindings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;

public class Game extends JPanel implements Runnable{
    public static final int WIDTH = 1600;
    public static final int HEIGHT = 900;
    final int textureTileSize = 20;

    Thread thread;

    public Game(){
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.black);
        this.setOpaque(true);
        this.setDoubleBuffered(true);
        KeyListener Keybindings = new Keybindings();
        this.addKeyListener(Keybindings);
        this.setFocusable(true);
    }

    public void start(){
        if (thread == null){
            thread = new Thread(this);
            thread.start();
        }
    }
    public void stop(){
        if (thread != null){
            thread = null;
        }
    }

    public void update(){

    }
    public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;

    }

    @Override
    public void run() {
        start();
        while (thread!=null){
            update();
            repaint();
        }
    }
}
