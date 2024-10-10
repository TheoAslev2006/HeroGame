package com.main.worldgen;

import com.main.Graphics.Game;
import com.main.Sprites.Tree;
import com.main.Utils.FileHandling;
import com.main.Utils.OpenSimplex2S;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

public class World {
    Random random = new Random();
    public double frequencyBase = 1.0/200;
    public double lacunarity = 2;
    public double persistance = 0.4;
    public double amplitude = 1.0;
    public double octaves = 4;
    int xOffset;
    int yOffset;
    HashMap<String, Chunk>chunks = new HashMap<>();
    HashMap<String, Tree>trees = new HashMap<>();
    BufferedImage[] bufferedImages;
    int width;
    int height;
    int tileSize = Game.textureTileSize;
    public final long seed;
    public World(int x, int y, int height, int width, long seed){
        this.seed = seed;
        final String[] fileLoctation = {
                "src\\Resource\\Textures\\WorldBuilding\\Water.png",
                "src\\Resource\\Textures\\WorldBuilding\\grass1.png",
                "src\\Resource\\Textures\\WorldBuilding\\grass2.png",
                "src\\Resource\\Textures\\WorldBuilding\\grass3.png",
                "src\\Resource\\Textures\\WorldBuilding\\Mountain1.png",
                "src\\Resource\\Textures\\WorldBuilding\\Mountain2.png",
                "src\\Resource\\Textures\\WorldBuilding\\Mountain3.png",
        };
        bufferedImages = new BufferedImage[fileLoctation.length];
        for (int i = 0; i < fileLoctation.length; i++) {
            try {
                bufferedImages[i] = FileHandling.load(new File(fileLoctation[i]));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
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
    public Chunk generateChunk(int x, int y){
        int[][] chunkTiles = new int[16][16];

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {

                int xCord = (x * 16) + j;
                int yCord = (y * 16) + i;


                double noise = generateMultipleLayerdNoise(seed, xCord, yCord, frequencyBase, octaves, lacunarity, amplitude, persistance);

                if ( noise < -0.7 ) {
                    chunkTiles[j][i] = 0;
                }
                else if (noise < 0.6){
                    chunkTiles[j][i] = random.nextInt(1,3);

                }
                else{
                    chunkTiles[j][i] = random.nextInt(1,3);

                }
            }
        }

        return new Chunk(bufferedImages, chunkTiles);
    }
    public Chunk getChunk(int chunkX, int chunkY){
        String chunkKey = chunkX + "," + chunkY;
        if (!chunks.containsKey(chunkKey)) {
            chunks.put(chunkKey, generateChunk(chunkX, chunkY));
        }
        return chunks.get(chunkKey);
    }
    public void renderWorld(Graphics2D g2d){
        int chunkXStart = (xOffset) / (16 * tileSize);
        int chunkYStart = (yOffset) / (16 * tileSize);

        int chunkVisibilityRange = 3;

        for (int y = chunkYStart - chunkVisibilityRange; y <= chunkYStart + chunkVisibilityRange; y++) {
            for (int x = chunkXStart - chunkVisibilityRange; x<= chunkXStart + chunkVisibilityRange; x++){
                Chunk chunk = getChunk(x, y);

                int renderX = (x * 16 * tileSize) - xOffset;
                int renderY = (y * 16 * tileSize) - yOffset;

                chunk.renderChunk(g2d, renderX, renderY, tileSize);
            }
        }
    }
    public void moveWorld(int vectorX, int vectorY, double speed){
        xOffset += (int) (vectorX * speed);
        yOffset += (int) (vectorY * speed);
    }
}
