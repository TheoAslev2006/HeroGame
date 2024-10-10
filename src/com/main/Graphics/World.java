package com.main.Graphics;

import com.main.Utils.FileHandling;
import com.main.Utils.OpenSimplex2S;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class World {

    double frequencyBase = 1.0/10;
    double lacunarity = 2;
    double persistance = 0.4;
    double amplitude = 1.0;
    double octaves = 4;
    int xOffset;
    int yOffset;
    int[][] matrix;
    BufferedImage[] bufferedImages;
    int width;
    int height;
    int tileSize = Game.textureTileSize;
    public World(int x, int y, int height, int width, long seed){
        this.height = height;
        this.width = width;
        matrix = new int[(width)][(height)];
        TileTypes tileTypes;
        final String[] fileLoctation = {
                "src\\Resource\\Textures\\water.png",
                "src\\Resource\\Textures\\grass.png",
                "src\\Resource\\Textures\\stone.png",
        };
        bufferedImages = new BufferedImage[3];
        for (int i = 0; i < 3; i++) {
            try {
                bufferedImages[i] = FileHandling.load(new File(fileLoctation[i]));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        matrix = GenerateMatrix(width,height, 100);

    }
    public double generateMultipleLayerdNoise(long seed,int x, int y,double frequency, double octaves, double lacunarity, double amplitude, double persistance){
        double multiLayerdNoise = 0;
        for (int i = 0; i < octaves; i++) {
            double value = OpenSimplex2S.noise2_ImproveX(seed,x *frequency, y * frequency) * amplitude;

            multiLayerdNoise += value;

            frequency *= lacunarity;
            amplitude *= persistance;
        }
        return multiLayerdNoise;
    }
    public int[][] GenerateMatrix(int width, int height, int seed){
        int[][] Wmatrix = new int[width][height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                double noise = generateMultipleLayerdNoise(seed, j, i, frequencyBase, octaves, lacunarity, amplitude, persistance);
                if ( noise < -0.4 ) {
                    Wmatrix[j][i] = 0;
                }
                else if (noise < 0.6){
                    Wmatrix[j][i] = 1;
                }
                else{
                    Wmatrix[j][i] = 2;
                }
            }
        }

        return Wmatrix;

    }
    public void renderWorld(Graphics2D g2d){

        for (int i = 0; i <height; i++) {
            for (int j = 0; j<width; j++){
                g2d.drawImage(bufferedImages[matrix[j][i]], (j-xOffset) * tileSize, (i - yOffset) * tileSize, null);
            }
        }
    }
    public void moveWorld(int vectorX, int vectorY, double speed){
        xOffset += (int) (vectorX * speed);
        yOffset += (int) (vectorY * speed);
    }
}
