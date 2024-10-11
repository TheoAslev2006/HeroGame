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
    int width = 16;
    int height = 16;
    int x = Game.WIDTH/2 + width/2;
    int y = Game.HEIGHT/2 + height/2;

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
