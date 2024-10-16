package com.main.worldgen;

import com.main.Graphics.Game;
import com.main.Utils.FileHandling;
import com.main.Utils.OpenSimplex2S;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class World{
    public static final double frequencyBase = 1.0/250 ;
    public static final double lacunarity = 1.5;
    public static final double persistance = 0.4;
    public static final double amplitude = 1.0;
    public static final double octaves = 3;
    public static int treeSpawnRate = 25;
    int chunkVisibilityRange = 2;
    public int xOffset;
    public int yOffset;
    public HashMap<String, Chunk> chunkMap = new HashMap<>();
    public HashMap<String, Tree> treeMap = new HashMap<>();
    BufferedImage[] tileTextures;
    BufferedImage[] treeTextures;
    int tileVariants = 22;
    int treeVariants = 2;
    int tileSize = Game.textureTileSize;
    public static final long seed =10000;
    Random random = new Random(seed);
    public World(int x, int y, int height, int width){
        final String tileFileLoctation = "src\\Resource\\Textures\\WorldBuilding\\groundtileset1.png";
        final String objectFileLocation = "src\\Resource\\Textures\\WorldObjects\\Tree.png";
        tileTextures = new BufferedImage[tileVariants];
        treeTextures = new BufferedImage[treeVariants];
        try {
            tileTextures = FileHandling.loadTileSet(tileFileLoctation, 32, 32);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
             treeTextures[0] = FileHandling.load(new File(objectFileLocation));
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
        double treeNoise = generateMultipleLayerdNoise(1,  x / 10, y / 10, frequencyBase/10, octaves/2, lacunarity/2, amplitude/2, persistance/2);
        Random randomPlacement = new Random(seed);
        if (noise > -0.4){
            if (random.nextInt(100) < 2){
                return new Tree(x,y,treeTextures);
            }
        }
        if (noise > 0){
            if (random.nextInt(100) < 30) {
                return new Tree(x, y, treeTextures);
            }
        }
        return null;
    }
    public Tree getTree(int x, int y, boolean createNew){
        String treekey = x + "," + y;
        if (!treeMap.containsKey(treekey)){
            if (createNew){
                Tree tree = generateTree(x, y, getChunk(x, y));
                treeMap.put(treekey, tree);
            }else
            {
                return null;
            }
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
                else if (noise < -0.02){
                    chunkTiles[j][i] = 1;
                }else if(noise < 0){
                    chunkTiles[j][i] = random.nextInt(0,2);
                }else {
                    chunkTiles[j][i] = 0;
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
    public void renderTrees(Graphics2D g2d) {
        int treeXStart = (xOffset + 32 * 32) / 32;
        int treeYStart = (yOffset + 32 * 32) / 32;
        for (int y = treeYStart - chunkVisibilityRange * 32; y <= treeYStart; y++) {
            for (int x = treeXStart - chunkVisibilityRange * 32; x <= treeXStart; x++) {

                Tree tree = getTree(x, y, true);

                if (tree != null) {
                    int renderX = x * 32 - xOffset;
                    int renderY = y * 32 - yOffset;
                    tree.renderTree(g2d, renderX, renderY);

                }
            }
        }
    }
        public void moveWorld(int vectorX, int vectorY, int speed){
//        Tree[] trees = new Tree[4];
//        trees[0] = getTree(xOffset + (vectorX * speed), yOffset, false);
//        trees[1] = getTree(xOffset - (vectorX * speed), yOffset, false);
//        trees[2] = getTree(xOffset, yOffset  + (vectorX * speed), false);
//        trees[3] = getTree(xOffset, yOffset  - (vectorX * speed), false);
            if (vectorX != 0) {
//            if (trees[0] == null)
                xOffset += (vectorX * speed);
            }
            if (vectorY != 0) {
//            if (trees[2] == null || trees[3] == null)
                yOffset += (vectorY * speed);
            }
        }

}

