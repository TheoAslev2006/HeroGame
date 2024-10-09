package com.main.Graphics;

import com.main.Utils.FileHandling;
import com.main.Utils.OpenSimplex2S;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class World {
    double Frequency = 1.0/25;
    int randint;
    int[][] matrix;
    BufferedImage[] bufferedImages;
    int width;
    int height;
    int tileSize = Game.textureTileSize;
    int startPosY = 0;
    int startPosX = 0;
    public World(int height, int width){
        this.height = height;
        this.width = width;
        randint = ThreadLocalRandom.current().nextInt(0,100);
        matrix = new int[(width)][(height)];
        final int[] tileType = {
                1,2,3,4,5,6
        };
        final String[] fileLoctation = {
                "src\\Resource\\Textures\\GrassBlock1.png",
                "src\\Resource\\Textures\\GrassBlockLeft.png",
                "src\\Resource\\Textures\\GrassBlockRight.png",
                "src\\Resource\\Textures\\DirtBlock1.png",
                "src\\Resource\\Textures\\DirtBlock2.png",
                "src\\Resource\\Textures\\StoneBlock1.png"
        };
        bufferedImages = new BufferedImage[tileType.length + 1];
        for (int i = 0; i < 6; i++) {
            try {
                bufferedImages[i] = FileHandling.load(new File(fileLoctation[i]));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
    public int[][] GenerateMatrix(int width, int height, int seed){
        double offSetY = OpenSimplex2S.noise2(seed, 10, 10);
        int[][] Wmatrix = new int[width][height];

            for (int y = 0; y<height; y++) {
                for (int x = 0; x < width; x++) {

                    if (y <= 20){
                        double value = OpenSimplex2S.noise2_ImproveX(randint, x * Frequency, y * Frequency);
                        int type = Math.min(3,(int) ((value + 1) * 1.5));
                        Wmatrix[x][y] = type;
                    }
                    if (y >= 20) {
                        double value = OpenSimplex2S.noise2_ImproveX(randint, x * Frequency, y * Frequency);
                        int type = Math.min(5,(int) ((value + 1) * 3.5));
                        Wmatrix[x][y] = type;

                    }

                }
            }
        return Wmatrix;

    }
    public void renderWorld(Graphics2D g2d){
        matrix = GenerateMatrix(width,height, 100);
        for (int i = 0; i <height; i++) {
            for (int j = 0; j<width; j++){
                if (matrix[j][i] != 0)
                    g2d.drawImage(bufferedImages[matrix[j][i]], j * tileSize, (i * tileSize), null);
            }
        }
    }
}
