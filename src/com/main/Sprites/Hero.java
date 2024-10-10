package com.main.Sprites;

import com.main.Graphics.Game;
import com.main.Utils.FileHandling;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;

public class Hero{
    int x = 250;
    int y = 250;
    int width;
    int height;
    BufferedImage bufferedImage;
    public Hero(){
        File file = new File("src\\Resource\\Textures\\Sprites\\Sprite.png");
        try {
            bufferedImage = FileHandling.load(file);
        } catch (Exception e) {
            System.out.println("Image is not readable");
        }

    }
    public void draw(Graphics2D g2d){
        g2d.drawImage(bufferedImage, x, y, null);
    }
}
