package com.main.worldgen;

import com.main.Graphics.Game;
import com.main.Utils.FileHandling;
import com.main.Utils.OpenSimplex2S;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class World {
    public static final double frequencyBase = 1.0/100 ;
    public static final double lacunarity = 1.5;
    public static final double persistance = 0.4;
    public static final double amplitude = 1.0;
    public static final double octaves = 3;
    public static int treeSpawnRate = 15;
    int chunkVisibilityRange = 2;
    int xOffset;
    int yOffset;
    HashMap<String, Chunk> chunkMap = new HashMap<>();
    HashMap<String, Tree> treeMap = new HashMap<>();
    BufferedImage[] tileTextures;
    int tileVariants = 22;
    int objectVariants = 2;
    int tileSize = Game.textureTileSize;
    public static final long seed =10000;
    Random random = new Random(seed);
    public World(int x, int y, int height, int width){
        final String tileFileLoctation = "src\\Resource\\Textures\\WorldBuilding\\groundtileset.png";
        final String objectFileLocation = "src\\Resource\\Textures\\WorldObjects\\Tree.png";
        tileTextures = new BufferedImage[tileVariants];
        try {
            tileTextures = FileHandling.loadTileSet(tileFileLoctation, 32, 32);
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
    public Tree generateTree(int x, int y, Chunk chunk){
        double noise = generateMultipleLayerdNoise(seed, x, y, frequencyBase, octaves, lacunarity, amplitude,  persistance);
        if (noise > -0.3 && chunk.tile[x][y] < 2){
            return new Tree(x, y);
        }
        return null;
    }
    public Tree getTree(int x, int y){
        String treekey = x + "," + y;
        if (!treeMap.containsKey(treekey)){
            Tree tree = generateTree(x, y, getChunk(x / (16*32), y / (16*32)));
            treeMap.put(treekey, tree);
        }
        return treeMap.get(treekey);
    }
    public Chunk generateChunk(int x, int y, int width, int height){
        int[][] chunkTiles = new int[width][height];

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {

                int xCord = (x * 16) + j;
                int yCord = (y * 16) + i;

                double noise = generateMultipleLayerdNoise(seed, xCord, yCord, frequencyBase, octaves, lacunarity, amplitude, persistance);

                if ( noise < -0.45 ) {
                    chunkTiles[j][i] = random.nextInt(2,4);
                }
                else if (noise < -0.4){
                    chunkTiles[j][i] = 4;
                }
                else{
                    chunkTiles[j][i] = random.nextInt(0,2);
                }
            }
        }

        return new Chunk(tileTextures, chunkTiles);
    }
    public Chunk getChunk(int chunkX, int chunkY){
        String chunkKey = chunkX + "," + chunkY;
        if (!chunkMap.containsKey(chunkKey)) {
            Chunk generatedChunk = generateChunk(chunkX, chunkY, 16, 16);
            chunkMap.put(chunkKey, generatedChunk);
        }
        return chunkMap.get(chunkKey);
    }
//    public WorldObjectChunk getWorldObjects(int chunkX, int chunkY){
//        String chunkKey = chunkX + "," + chunkY;
//        if (!worldObjectChunkMap.containsKey(chunkKey)){
//            worldObjectChunkMap.put(chunkKey, generateWorldObjects(chunkX, chunkY));
//        }
//        return worldObjectChunkMap.get(chunkKey);
//    }
//    public void removeObject(int x, int y){
//        WorldObjectChunk worldObjectChunk = getWorldObjects(x,y);
//        worldObjectChunk.tile[x][y] = 0;
//    }

    public void renderWorld(Graphics2D g2d){
        int chunkXStart = (xOffset) / (16 * tileSize);
        int chunkYStart = (yOffset) / (16 * tileSize);

        for (int y = chunkYStart - chunkVisibilityRange; y <= chunkYStart + chunkVisibilityRange; y++) {
            for (int x = chunkXStart - chunkVisibilityRange; x<= chunkXStart + chunkVisibilityRange; x++){
                Chunk chunk = getChunk(x, y);
                int renderX = (x * 16 * tileSize) - xOffset;
                int renderY = (y * 16 * tileSize) - yOffset;

                chunk.renderChunk(g2d, renderX, renderY, tileSize);
            }
        }
    }
    public void renderTrees(Graphics2D g2d){
        int treeXStart = xOffset / 32;
        int treeYStart = yOffset / 32;
        for (int y = 0; y < treeYStart - chunkVisibilityRange; y++) {
            for (int x = 0; x < treeXStart - chunkVisibilityRange; x++) {
                Tree tree = getTree(x, y);
                int renderX = x * 32 - xOffset;
                int renderY = y * 32 - yOffset;

                tree.renderTree(g2d, renderX, renderY);
            }
        }
    }
    public void moveWorld(int vectorX, int vectorY, double speed){
        xOffset += (int) (vectorX * speed);
        yOffset += (int) (vectorY * speed);
    }
}
