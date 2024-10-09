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
    public Game(){
        world = new World(56, 100);
        keybindings = new Keybindings();
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.WHITE);
        this.setOpaque(true);
        this.setDoubleBuffered(true);
        hero = new Hero();
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
        hero.setVx(0);
        hero.setVy(0);
        if (keybindings.up){
            if (!keybindings.right && !keybindings.left){
                hero.setVy(-10);
            }
            hero.setVy(-7);
        }
        if (keybindings.down){
            if (!keybindings.right && !keybindings.left){
                hero.setVy(10);
            }
            hero.setVy(7);
        }

        if (keybindings.right){
            if (!keybindings.down && !keybindings.up){
                hero.setVx(10);
            }
            hero.setVx(7);
        }

        if (keybindings.left){
            if (!keybindings.down && !keybindings.up){
                hero.setVx(-10);
            }
            hero.setVx(-7);
        }
        if (hero.getVy() != 0){
            hero.setY(hero.getY() + hero.getVy());

        }
        if (hero.getVx() != 0){
            hero.setX(hero.getX() + hero.getVx());
        }
        repaint();

    }
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        hero.draw(g2d);
        world.renderWorld(g2d);
    }

    @Override
    public void run() {
        while (thread!=null){
            update();
            repaint();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
