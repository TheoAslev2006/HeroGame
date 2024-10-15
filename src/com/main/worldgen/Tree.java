package com.main.worldgen;

import com.main.Utils.FileHandling;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Tree {
    int xCord, yCord ;
    String[] fileLocation = {
            "src\\Resource\\Textures\\WorldObjects\\Tree.png"
    };
    BufferedImage[] textures;
    public Tree(int x, int y){
        textures = new BufferedImage[fileLocation.length];
        try {
            for (int i = 0; i < textures.length; i++) {
                textures[i] = FileHandling.load(new File(fileLocation[i]));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void renderTree(Graphics2D g2d, int x, int y){
        g2d.drawImage(textures[0] , x, y , null);
    }
}

