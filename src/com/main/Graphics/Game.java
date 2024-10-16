package com.main.Graphics;

import com.main.Main;
import com.main.Utils.Keybindings;
import com.main.Sprites.Hero;

import com.main.worldgen.World;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.Arrays;

public class Game extends JPanel implements Runnable{
    public static final int WIDTH = Main.dimension.width;
    public static final int HEIGHT = Main.dimension.width;
    public static final int textureTileSize = 32;
    boolean[] playerPointingDirections = new boolean[9];
    Hero hero;
    Thread thread;
    Keybindings keybindings;
    World world;
    int x;
    int y;
    int speed = 1;
    boolean walking;
    public Game(){
        Arrays.fill(playerPointingDirections, false);
        playerPointingDirections[4] = true;
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

        boolean[] previousPointingDirection = playerPointingDirections;
        if (keybindings.up){
            playerPointingDirections[4] = false;
            playerPointingDirections[1] = true;
            playerPointingDirections[7] = false;
            world.moveWorld(0,-2,speed);
            System.out.println(playerPointingDirections[1]);
        }
        if (keybindings.down){
            playerPointingDirections[4] = false;
            playerPointingDirections[7] = true;
            playerPointingDirections[1] = false;
            world.moveWorld(0,2,speed);
        }

        if (keybindings.right){
            playerPointingDirections[4] = false;
            playerPointingDirections[5] = true;
            playerPointingDirections[3] = false;
            world.moveWorld(2,0,speed);
        }

        if (keybindings.left) {
            playerPointingDirections[4] = false;
            playerPointingDirections[3] = true;
            playerPointingDirections[5] = false;
            world.moveWorld(-2,0,speed);
        }
        if (keybindings.destroy){
            if (playerPointingDirections[1]){
                int directionX = world.xOffset;
                int directionY = world.yOffset;
                int x = this.x;
                int y = this.y;
                String treeKey = directionX + "," + directionY;
                if ( Math.abs(x - directionX) <= 32 || Math.abs(x - directionX) >= 0 ){
                    world.treeMap.remove(treeKey);
                    System.out.println("Removed tree at coordinates(x,y): " + directionX + "," + directionY);
                }

            }
        }
//        if(playerPointingDirections == previousPointingDirection){
//            for (boolean playerPointingDirection : playerPointingDirections) {
//                System.out.println(playerPointingDirection);
//            }
//        }

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

            while (secondsUP > secondsPerTick){
                update();
                secondsUP -= secondsPerTick ;

                tickCount++;
                if (tickCount % 60 == 0){
                    System.out.println(frames);
                    frames = 0;
                }

            }
            repaint();
            frames++;
        }
    }
}
