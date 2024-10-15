package com.main.Graphics;

import com.main.Main;
import com.main.Utils.Keybindings;
import com.main.Sprites.Hero;

import com.main.worldgen.World;

import javax.swing.*;
import java.awt.*;

public class Game extends JPanel implements Runnable{
    public static final int WIDTH = Main.dimension.width;
    public static final int HEIGHT = Main.dimension.width;
    public static final int textureTileSize = 32;
    Hero hero;
    Thread thread;
    Keybindings keybindings;
    World world;
    int x;
    int y;
    int speed = 1;
    boolean walking;
    public Game(){
        world = new World(0, 0,56, 100);
        keybindings = new Keybindings();
        this.setPreferredSize(Main.dimension);
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
    public void printFps(Graphics2D g2d, String string){
        g2d.setFont(new Font("Comic Sans", Font.PLAIN, 20));
        g2d.drawString(string, 50, 50);
    }
    public void update(){

        if (keybindings.up){
            world.moveWorld(0,-2,speed);
        }
        if (keybindings.down){

            world.moveWorld(0,2,speed);
        }

        if (keybindings.right){
            world.moveWorld(2,0,speed);
        }

        if (keybindings.left) {
            world.moveWorld(-2,0,speed);
        }
        repaint();
    }
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        world.renderWorld(g2d);
        world.renderTrees(g2d);
        hero.draw(g2d);
        g2d.dispose();
    }

    @Override
    public void run() {

        int frames = 0;
        double secondsUP = 0;
        long previousTime = System.nanoTime();
        double secondsPerTick = 1 / 60.0;
        int tickCount = 0;

        while (thread!=null){
            long currentTime = System.nanoTime();
            long passedTime = currentTime - previousTime;
            previousTime = currentTime;
            secondsUP += passedTime / 1_000_000_000.0;

            while (secondsUP >= secondsPerTick){
                update();
                secondsUP -= secondsPerTick;

                tickCount++;
                if (tickCount % 120 == 0){
                    System.out.println(frames);
                    frames = 0;
                }
            }
            repaint();
            frames++;
        }
    }
}
