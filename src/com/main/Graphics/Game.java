package com.main.Graphics;

import com.main.Utils.Keybindings;
import com.main.Sprites.Hero;

import javax.swing.*;
import java.awt.*;

public class Game extends JPanel implements Runnable{
    public static final int WIDTH = 1600;
    public static final int HEIGHT = 900;
    public static final int textureTileSize = 16;
    Hero hero;
    Thread thread;
    Keybindings keybindings;
    World world;
    int x;
    int y;
    public Game(){
        world = new World(0, 0,56, 100, 1);
        keybindings = new Keybindings();
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.WHITE);
        this.setOpaque(true);
        this.setDoubleBuffered(true);
        hero = new Hero();
        hero.setX(WIDTH/2 + hero.getWidth()/2);
        hero.setY(HEIGHT/2 + hero.getHeight()/2);
        this.addKeyListener(keybindings);
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
        if (keybindings.up){
            world.moveWorld(0,-2,2);
        }
        if (keybindings.down){
            world.moveWorld(0,2,2);
        }

        if (keybindings.right){
            world.moveWorld(2,0,2);
        }

        if (keybindings.left) {
            world.moveWorld(-2,0,2);
        }
        repaint();
    }
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        world.renderWorld(g2d);
        hero.draw(g2d);

    }

    @Override
    public void run() {
        while (thread!=null){
            update();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
