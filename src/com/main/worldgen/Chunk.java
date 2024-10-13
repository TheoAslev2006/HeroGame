package com.main.worldgen;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Chunk {
    int[][] tile;
    BufferedImage[] tileImage;
    Random random = new Random();
    public final int CHUNKSIZE = 16;
    public Chunk(BufferedImage[] tileImage, int[][] tile) {
        this.tileImage= tileImage;
        this.tile = tile;
    }
    public Chunk(BufferedImage[] tileImage, int [][] tile, Chunk[] surroundingChunks){

    }
    public void renderChunk(Graphics2D g2d, int x, int y, int tileSize){
        for (int i = 0; i < tile.length; i++) {
            for (int j = 0; j < tile[i].length; j++) {

                int renderX = x + (i * tileSize);
                int renderY = y + (j * tileSize);

                if (tile[i][j] == 2){
                    int texture = setWaterTexture(getSurroundingTiles(tile, i, j));
                    g2d.drawImage(tileImage[texture], renderX, renderY, tileSize, tileSize, null);

                } else
                    g2d.drawImage(tileImage[tile[i][j]], renderX, renderY, tileSize, tileSize, null);
            }
        }
    }
    public int getTilesAt(int x, int y){
        return tile[x][y];
    }
    public int[] getSurroundingTiles(int[][] chunkTiles, int x, int y){

        int[]surroundingBlocks = new int[8];

        for (int i = 0; i < surroundingBlocks.length; i++) {
            surroundingBlocks[i] = -1;
        }

        if (y > 0)
            surroundingBlocks[0] = chunkTiles[x][y - 1]; // Top
        if (x < chunkTiles.length - 1 && y > 0)
            surroundingBlocks[1] = chunkTiles[x + 1][y - 1]; // Top-Right
        if (x < chunkTiles.length - 1)
            surroundingBlocks[2] = chunkTiles[x + 1][y]; // Right
        if (x < chunkTiles.length - 1 && y < chunkTiles[x].length - 1)
            surroundingBlocks[3] = chunkTiles[x + 1][y + 1]; // Bottom-Right
        if (y < chunkTiles[x].length - 1)
            surroundingBlocks[4] = chunkTiles[x][y + 1]; // Bottom
        if (x > 0 && y < chunkTiles[x].length - 1)
            surroundingBlocks[5] = chunkTiles[x - 1][y + 1]; // Bottom-Left
        if (x > 0)
            surroundingBlocks[6] = chunkTiles[x - 1][y]; // Left
        if (x > 0 && y > 0)
            surroundingBlocks[7] = chunkTiles[x - 1][y - 1]; // Top-Left



        return surroundingBlocks;
    }
    public int setWaterTexture(int[] surroundingBlocks){

        boolean top = (surroundingBlocks[0] == 2);
        boolean topRight = (surroundingBlocks[1] == 2);
        boolean right = (surroundingBlocks[2] == 2);
        boolean bottomRight = (surroundingBlocks[3] == 2);
        boolean bottom = (surroundingBlocks[4] == 2);
        boolean bottomLeft = (surroundingBlocks[5] == 2);
        boolean left = (surroundingBlocks[6] == 2);
        boolean topLeft = (surroundingBlocks[7] == 2);


        if (top && bottom && left && right && topLeft && topRight && bottomLeft && bottomRight) {
            return 2;
        }

        if (!top && right && left && bottom) return 3;
        if (top && !left && right && bottom) return 4;
        if (top && !right && left && bottom) return 5;
        if (top && right && left && !bottom) return 6;

        if (!top && bottom && !left && right) return 7;
        if (!top && bottom && left && !right) return 8;
        if (!left && right && top && !bottom) return 9;
        if (left && !right && top && !bottom) return 10;

        if (top && right && bottom && !bottomRight) return 11;
        if (top && left && bottom && !bottomLeft)return 12;
        if (top && right && bottom && !topRight) return 13;
        if (top && left && bottom && !topLeft) return 14;

        if (!bottom && !right && !left && top) return 15;
        if (!bottom && right && !left && !top) return 16;
        if (bottom && !right && !left && !top) return 17;
        if (!bottom && !right && left && !top) return 18;
        return 2;
    }
}
