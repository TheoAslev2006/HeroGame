package com.main.Sprites;

import com.main.Utils.FileHandling;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;

public class Hero{
    int x = 100;
    int y = 100;
    int vy;
    int vx;
    int width;
    int height;
    BufferedImage bufferedImage;
    public Hero(){
        File file = new File("src\\Resource\\Textures\\Sprite.png");
        try {
            bufferedImage = FileHandling.load(file);
        } catch (Exception e) {
            System.out.println("Image is not readable");
        }

    }
    public void draw(Graphics2D g2d){
        g2d.drawImage(bufferedImage, x, y, null);
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getVy() {
        return vy;
    }

    public void setVy(int vy) {
        this.vy = vy;
    }

    public int getVx() {
        return vx;
    }

    public void setVx(int vx) {
        this.vx = vx;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
