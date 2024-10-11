package com.main.worldgen;

import com.main.Graphics.Game;
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
    public static double frequencyBase = 1.0/100;
    public static double lacunarity = 2;
    public static double persistance = 0.4;
    public static double amplitude = 1.0;
    public static double octaves = 5;
    public static int treeSpawnRate = 10;
    int xOffset;
    int yOffset;
    HashMap<String, Chunk>chunks = new HashMap<>();
    HashMap<String, WorldObject>worldObjects = new HashMap<>();
    BufferedImage[] tileTextures;
    BufferedImage[] objectTextures;
    int tileVariants = 2;
    int objectVariants = 2;
    int width;
    int height;
    int tileSize = Game.textureTileSize;
    public static final long seed =10000;
    public World(int x, int y, int height, int width){
        final String[] fileLoctation = {
                "src\\Resource\\Textures\\WorldBuilding\\Water.png",
                "src\\Resource\\Textures\\WorldBuilding\\grass.png",
                "src\\Resource\\Textures\\WorldObjects\\Tree1.png"
        };
        tileTextures = new BufferedImage[tileVariants];
        objectTextures = new BufferedImage[objectVariants];
        for (int i = 0; i < tileVariants; i++) {
            try {
                tileTextures[i] = FileHandling.load(new File(fileLoctation[i]));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        objectTextures[0] = null;
        try {
            objectTextures[1] = FileHandling.load(new File(fileLoctation[fileLoctation.length-1]));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static double generateMultipleLayerdNoise(long seed, int x, int y, double frequency, double octaves, double lacunarity, double amplitude, double persistance){
        double multiLayerdNoise = 0;
        for (int i = 0; i < octaves; i++) {
            double value = OpenSimplex2S.noise2_ImproveX(seed,x *frequency, y * frequency) * amplitude;

            multiLayerdNoise += value;

            frequency *= lacunarity;
            amplitude *= persistance;
        }
        return multiLayerdNoise;
    }
    public WorldObject generateWorldObjects(int x, int y){
        int[][] chunkTiles = new int[16][16];
        Chunk chunk = getChunk(x,y);
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {

                int xCord = (x * 16) + j;
                int yCord = (y * 16) + i;
                //int randint = random.nextInt(treeSpawnRate);

                double noise = generateMultipleLayerdNoise(seed, xCord, yCord, frequencyBase, octaves, lacunarity, amplitude, persistance);

                if (chunk.getTilesAt(j,i) == 1 && random.nextInt(treeSpawnRate) == 1){
                    chunkTiles[j][i] = 1;
                }
            }
        }
        return new WorldObject(objectTextures, chunkTiles);
    }
    public Chunk generateChunk(int x, int y){
        int[][] chunkTiles = new int[16][16];

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {

                int xCord = (x * 16) + j;
                int yCord = (y * 16) + i;

                double noise = generateMultipleLayerdNoise(seed, xCord, yCord, frequencyBase, octaves, lacunarity, amplitude, persistance);

                if ( noise < -0.3 ) {
                    chunkTiles[j][i] = 0;
                }
                else if (noise < 0.){
                    chunkTiles[j][i] = 1;
                }
                /*else{
                    chunkTiles[j][i] = random.nextInt(4,6);
                }*/
            }
        }

        return new Chunk(tileTextures, chunkTiles);
    }
    public Chunk getChunk(int chunkX, int chunkY){
        String chunkKey = chunkX + "," + chunkY;
        if (!chunks.containsKey(chunkKey)) {
            chunks.put(chunkKey, generateChunk(chunkX, chunkY));
        }
        return chunks.get(chunkKey);
    }
    public WorldObject getWorldObjects(int chunkX, int chunkY){
        String chunkKey = chunkX + "," + chunkY;
        if (!worldObjects.containsKey(chunkKey)){
            worldObjects.put(chunkKey, generateWorldObjects(chunkX, chunkY));
        }
        return worldObjects.get(chunkKey);
    }
    public void removeObject(int chunkX, int chunkY){
        String chunkKey = chunkX + "," + chunkY;
        worldObjects.replace(chunkKey, null);
    }
    public void renderWorld(Graphics2D g2d){
        int chunkXStart = (xOffset) / (16 * tileSize);
        int chunkYStart = (yOffset) / (16 * tileSize);

        int chunkVisibilityRange = 1;

        for (int y = chunkYStart - chunkVisibilityRange; y <= chunkYStart + chunkVisibilityRange; y++) {
            for (int x = chunkXStart - chunkVisibilityRange; x<= chunkXStart + chunkVisibilityRange; x++){
                Chunk chunk = getChunk(x, y);
                WorldObject worldObject = getWorldObjects(x,y);
                int renderX = (x * 16 * tileSize) - xOffset;
                int renderY = (y * 16 * tileSize) - yOffset;

                chunk.renderChunk(g2d, renderX, renderY, tileSize);
                worldObject.renderObject(g2d, renderX, renderY, tileSize);
            }
        }
    }
    public void moveWorld(int vectorX, int vectorY, double speed){
        xOffset += (int) (vectorX * speed);
        yOffset += (int) (vectorY * speed);
    }
}
