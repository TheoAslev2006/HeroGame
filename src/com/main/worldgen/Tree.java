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
    public Tree(int x, int y, BufferedImage[] bufferedImages){
        textures = bufferedImages;
    }
    public void renderTree(Graphics2D g2d, int x, int y)
    {
        g2d.drawImage(textures[0] , x - 4, y - 8 , null);

    }
}

