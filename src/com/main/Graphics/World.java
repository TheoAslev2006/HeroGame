package com.main.Graphics;

import com.main.Utils.FileHandling;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class World {
    int[][] matrix;
    BufferedImage[] bufferedImages;
    int width;
    int height;
    int tileSize = Game.textureTileSize;
    int startPosY = 500;
    int startPosX = 0;
    public World(int height, int width){
        this.height = height;
        this.width = width;

        matrix = new int[(width)][(height)];
        final int[] tileType = {
                1,2,3
        };
        final String[] fileLoctation = {
                "src\\Resource\\Textures\\dirtblock.png",
                "src\\Resource\\Textures\\grassblock.png",

                "src\\Resource\\Textures\\stoneblock.png"
        };
        bufferedImages = new BufferedImage[tileType.length];
        for (int i = 0; i < 2; i++) {
            try {
                bufferedImages[i] = FileHandling.load(new File(fileLoctation[i]));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }



        }

    }
    public int[][] GenerateMatrix(int width, int height){
        int[][] Wmatrix = new int[width][height];
        for (int i = 0; i < width; i++){
            for (int j = 0; j <height; j++){
                Wmatrix[i][j] = 1;
            }
        }

        return Wmatrix;
    }
    public void renderWorld(Graphics2D g2d){
        matrix = GenerateMatrix(width,height);
        for (int i = 0; i <width; i++) {
            for (int j = 0; j<height; j++){
                if (matrix[i][j] != 0)
                    g2d.drawImage(bufferedImages[matrix[i][j]],j * tileSize, i * tileSize, null );
            }
        }
    }
}
