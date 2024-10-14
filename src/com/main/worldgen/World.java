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
    public static double frequencyBase = 1.0/100 ;
    public static double lacunarity = 2;
    public static double persistance = 0.4;
    public static double amplitude = 1.0;
    public static double octaves = 5;
    public static int treeSpawnRate = 10;
    int chunkVisibilityRange = 1;
    int xOffset;
    int yOffset;
    HashMap<String, Chunk> chunkMap = new HashMap<>();
    HashMap<String, WorldObjectChunk> worldObjectChunkMap = new HashMap<>();
    BufferedImage[] tileTextures;
    BufferedImage[] objectTextures;
    int tileVariants = 22;
    int objectVariants = 2;
    int width;
    int height;
    int tileSize = Game.textureTileSize;
    public static final long seed =10000;
    Random random = new Random(seed);
    public World(int x, int y, int height, int width){
        final String fileLoctation = "src\\Resource\\Textures\\WorldBuilding\\WaterGroundTileSet.png";

        tileTextures = new BufferedImage[tileVariants];
        //objectTextures = new BufferedImage[objectVariants];
        try {
            tileTextures = FileHandling.loadTileSet(fileLoctation, 32, 32);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        objectTextures[0] = null;
//        try {
//            objectTextures[1] = FileHandling.load(new File(fileLoctation[fileLoctation.length-1]));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
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
    public WorldObjectChunk generateWorldObjects(int x, int y){
        int[][] chunkTiles = new int[16][16];
        Chunk chunk = getChunk(x,y);
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {

                int xCord = (x * 16) + j;
                int yCord = (y * 16) + i;

                double noise = generateMultipleLayerdNoise(seed, xCord, yCord, frequencyBase, octaves, lacunarity, amplitude, persistance);

                if (chunk.getTilesAt(j,i) == 1 && random.nextInt(treeSpawnRate) == 1 && noise < 0.8){
                    chunkTiles[j][i] = 1;
                }
            }
        }
        return new WorldObjectChunk(objectTextures, chunkTiles);
    }
    public Chunk generateChunk(int x, int y, int width, int height){
        int[][] chunkTiles = new int[width][height];

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {

                int xCord = (x * 16) + j;
                int yCord = (y * 16) + i;

                double noise = generateMultipleLayerdNoise(seed, xCord, yCord, frequencyBase, octaves, lacunarity, amplitude, persistance);

                if ( noise < -0.45 ) {
                    chunkTiles[j][i] = 2;
                }
                else if (noise < 0.7){
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
    public WorldObjectChunk getWorldObjects(int chunkX, int chunkY){
        String chunkKey = chunkX + "," + chunkY;
        if (!worldObjectChunkMap.containsKey(chunkKey)){
            worldObjectChunkMap.put(chunkKey, generateWorldObjects(chunkX, chunkY));
        }
        return worldObjectChunkMap.get(chunkKey);
    }
    public void removeObject(int x, int y){
        int worldX = x + xOffset;
        int worldY = y + yOffset;
        int chunkX = (worldX) / (16 * tileSize);
        int chunkY = (worldY) / (16 * tileSize);
        String chunkKey = chunkX + "," + chunkY;
        int tileX = ( worldX % (16 * tileSize)) / tileSize;
        int tileY = ( worldY % (16 * tileSize)) / tileSize;
        WorldObjectChunk worldObjectChunk = worldObjectChunkMap.get(chunkKey);
    }
    public void renderWorld(Graphics2D g2d){
        int chunkXStart = (xOffset) / (16 * tileSize);
        int chunkYStart = (yOffset) / (16 * tileSize);

        for (int y = chunkYStart - chunkVisibilityRange; y <= chunkYStart + chunkVisibilityRange; y++) {
            for (int x = chunkXStart - chunkVisibilityRange; x<= chunkXStart + chunkVisibilityRange; x++){
                Chunk chunk = getChunk(x, y);
//                WorldObjectChunk worldObject = getWorldObjects(x,y);
                int renderX = (x * 16 * tileSize) - xOffset;
                int renderY = (y * 16 * tileSize) - yOffset;

                Chunk topChunk = getChunk(x, y - 1);
                Chunk bottomChunk = getChunk(x, y + 1);
                Chunk leftChunk = getChunk(x - 1, y);
                Chunk rightChunk = getChunk(x + 1, y);
                Chunk topLeftChunk = getChunk(x - 1, y - 1);
                Chunk topRightChunk = getChunk(x + 1, y - 1);
                Chunk bottomLeftChunk = getChunk(x - 1, y + 1);
                Chunk bottomRightChunk = getChunk(x + 1, y + 1);

                int[] topChunkRow = topChunk != null ? topChunk.getBottomRow(): new int[16];
                int[] bottomChunkRow = bottomChunk != null ? bottomChunk.getTopRow(): new int[16];
                int[] rightChunkRow = rightChunk != null ?  rightChunk.getLeftRow(): new int[16];
                int[] leftChunkRow = leftChunk!=null ? leftChunk.getRightRow(): new int[16];

                int topLeftChunkTile = (topLeftChunk != null) ? topLeftChunk.getTilesAt(15,15): 0;
                int topRightChunkTile = (topRightChunk != null) ? topRightChunk.getTilesAt(0,15) : 0;
                int bottomLeftChunkTile = (bottomLeftChunk != null) ? bottomLeftChunk.getTilesAt(0,0): 0;
                int bottomRightChunkTile = (bottomRightChunk != null) ? bottomRightChunk.getTilesAt(15,0): 0;
//                Chunk extendedChunk= generateChunk(chunkXStart - 16, chunkYStart - 16, 18, 18);
//                int[][] extendedChunkTiles = extendedChunk.tile;
//                int[] topChunkRow = new int[16];
//                int[] bottomChunkRow = new int[16];
//                int[] leftChunkRow = new int[16];
//                int[] rightChunkRow = new int[16];
//
//                int topLeftChunkTile = extendedChunkTiles[0][0];
//                int topRightChunkTile = extendedChunkTiles[17][0];
//                int bottomLeftChunkTile = extendedChunkTiles[0][17];
//                int bottomRightChunkTile = extendedChunkTiles[17][17];
//
//                for (int i = 0; i < topChunkRow.length; i++) {
//                    topChunkRow[i] = extendedChunkTiles[0][i + 1];
//                }
//                for (int i = 0; i < bottomChunkRow.length; i++) {
//                    bottomChunkRow[i] = extendedChunkTiles[17][i + 1];
//                }
//                for (int i = 0; i < rightChunkRow.length; i++) {
//                    rightChunkRow[i] = extendedChunkTiles[i + 1][0];
//                }
//                for (int i = 0; i < leftChunkRow.length; i++) {
//                    leftChunkRow[i] = extendedChunkTiles[i + 1][17];
//                }

                chunk.renderChunk(g2d, renderX, renderY, tileSize, topChunkRow, bottomChunkRow, leftChunkRow, rightChunkRow, topRightChunkTile, topLeftChunkTile, bottomRightChunkTile, bottomLeftChunkTile);
                //worldObject.renderObject(g2d, renderX, renderY, tileSize);
            }
        }
    }
    public void moveWorld(int vectorX, int vectorY, double speed){
        xOffset += (int) (vectorX * speed);
        yOffset += (int) (vectorY * speed);
    }
}
