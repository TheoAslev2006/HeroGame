package com.main.Sprites;

import com.main.Utils.FileHandling;
import com.main.worldgen.World;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Tree {
    int x;
    int y;
    int width;
    int height;
    World world;
    BufferedImage texture;
    String textureFileLocation = "src\\Resource\\Textures\\WorldObjects\\tree1.png";
    public Tree(World world){
        this.world = world;
        try {
            texture = FileHandling.load(new File(textureFileLocation));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void renderTree(Graphics2D g2d, int x, int y){
        double value = world.generateMultipleLayerdNoise(world.seed, x, y, world.frequencyBase, world.octaves, world.lacunarity, world.amplitude, world.persistance);
        
        g2d.drawImage(texture, x, y, null);
    }
}
