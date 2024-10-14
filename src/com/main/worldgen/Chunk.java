package com.main.worldgen;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Chunk {
    int[][] tile;
    BufferedImage[] tileImage;
    Random random = new Random();
    public Chunk(BufferedImage[] tileImage, int[][] tile) {
        this.tileImage= tileImage;
        this.tile = tile;
    }

    public void renderChunk(Graphics2D g2d, int x, int y, int tileSize, int[] topChunks, int[] bottomChunks, int[] rightChunks, int[] leftChunks, int topRightChunks, int topLeftChunks, int bottomRightChunks, int bottomLeftChunks){
        for (int i = 0; i < tile.length; i++) {
            for (int j = 0; j < tile[i].length; j++) {

                int renderX = x + (i * tileSize);
                int renderY = y + (j * tileSize);

                if (tile[i][j] == 2){
                    int texture = setWaterTexture(getSurroundingTiles(tile, i, j, topChunks, bottomChunks, rightChunks, leftChunks, topRightChunks, topLeftChunks, bottomRightChunks, bottomLeftChunks));
                    g2d.drawImage(tileImage[texture], renderX, renderY, tileSize, tileSize, null);

                } else
                    g2d.drawImage(tileImage[tile[i][j]], renderX, renderY, tileSize, tileSize, null);
            }
        }
    }
    public int getTilesAt(int x, int y){
        return tile[x][y];
    }

    public int[] getSurroundingTiles(int[][] chunkTiles, int x, int y,int[] topChunks, int[] bottomChunks, int[] leftChunks, int[] rightChunks, int topRightChunks, int topLeftChunks, int bottomRightChunks, int bottomLeftChunks){

        int[][] extendedChunkTiles = new int[18][18];

        for (int i = 0; i < chunkTiles.length; i++) {
            for (int j = 0; j < chunkTiles.length; j++) {
                extendedChunkTiles[i +1][j + 1] = chunkTiles[i][j];
            }
        }

        for (int i = 0; i < topChunks.length; i++) {
            extendedChunkTiles[0][ i +  1] = topChunks[i];
        }
        for (int i = 0; i < bottomChunks.length; i++) {
            extendedChunkTiles[17][ i +  1] = bottomChunks[i];
        }
        for (int i = 0; i < leftChunks.length; i++) {
            extendedChunkTiles[i + 1][0] = leftChunks[i];
        }
        for (int i = 0; i < rightChunks.length; i++) {
            extendedChunkTiles[i + 1][ 17] = rightChunks[i];
        }

        extendedChunkTiles[0][0] = topLeftChunks;
        extendedChunkTiles[0][17] = topRightChunks;
        extendedChunkTiles[17][0] = bottomLeftChunks;
        extendedChunkTiles[17][17] = bottomRightChunks;

        int[]surroundingBlocks = new int[8];

        surroundingBlocks[0] = extendedChunkTiles[x][y];
        surroundingBlocks[1] = extendedChunkTiles[x + 1][y];
        surroundingBlocks[2] = extendedChunkTiles[x + 2][y];
        surroundingBlocks[3] = extendedChunkTiles[x][y + 1];
        surroundingBlocks[4] = extendedChunkTiles[x + 2][y + 1];
        surroundingBlocks[5] = extendedChunkTiles[x][y + 2];
        surroundingBlocks[6] = extendedChunkTiles[x + 1][y + 2];
        surroundingBlocks[7] = extendedChunkTiles[x + 2][y + 2];

        /*
        if (y > 0)
            surroundingBlocks[0] = extendedChunkTiles[x][y - 1];
        if (x < extendedChunkTiles.length - 1 && y > 0)
            surroundingBlocks[1] = extendedChunkTiles[x + 1][y - 1];
        if (x < extendedChunkTiles.length - 1)
            surroundingBlocks[2] = extendedChunkTiles[x + 1][y];
        if (x < extendedChunkTiles.length - 1 && y < chunkTiles[x].length - 1)
            surroundingBlocks[3] = extendedChunkTiles[x + 1][y + 1];
        if (y < extendedChunkTiles[x].length - 1)
            surroundingBlocks[4] = extendedChunkTiles[x][y + 1];
        if (x > 0 && y < extendedChunkTiles[x].length - 1)
            surroundingBlocks[5] = extendedChunkTiles[x - 1][y + 1];
        if (x > 0)
            surroundingBlocks[6] = extendedChunkTiles[x - 1][y];
        if (x > 0 && y > 0)
            surroundingBlocks[7] = extendedChunkTiles[x - 1][y - 1];
        */

        /*
        surroundingBlocks[0] = extendedChunkTiles[x - 1][y- 1];
        surroundingBlocks[1] = extendedChunkTiles[x][y - 1];
        surroundingBlocks[2] = extendedChunkTiles[x + 1][ y- 1];
        surroundingBlocks[3] = extendedChunkTiles[x-1][y];
        surroundingBlocks[4] = extendedChunkTiles[x + 1][y];
        surroundingBlocks[5] = extendedChunkTiles[x- 1][y + 1];
        surroundingBlocks[6] = extendedChunkTiles[x][y + 1];
        surroundingBlocks[7] = extendedChunkTiles[x + 1][y + 1];
        */
        return surroundingBlocks;
    }
    public int setWaterTexture(int[] surroundingBlocks){

        boolean topLeft = (surroundingBlocks[0] == 2);
        boolean top = (surroundingBlocks[1] == 2);
        boolean topRight = (surroundingBlocks[2] == 2);
        boolean left = (surroundingBlocks[3] == 2);
        boolean right = (surroundingBlocks[4] == 2);
        boolean bottomLeft = (surroundingBlocks[5] == 2);
        boolean bottom = (surroundingBlocks[6] == 2);
        boolean bottomRight = (surroundingBlocks[7] == 2);


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
        return 0;
    }
    public int[] getBottomRow(){
        int[] bottomRow = new int[16];

        for (int i = 0; i < tile.length; i++) {
            bottomRow[i] = getTilesAt(i, 15);
        }

        return bottomRow;
    }
    public int[] getTopRow(){
        int[] topRow = new int[16];

        for (int i = 0; i < tile.length; i++) {
            topRow[i] = getTilesAt(i, 0);
        }

        return topRow;
    }
    public int[] getLeftRow(){
        int[] leftRow = new int[16];

        for (int i = 0; i < tile.length; i++) {
            leftRow[i] = getTilesAt(0 , i);
        }

        return leftRow;
    }
    public int[] getRightRow(){
        int[] rightRow = new int[16];

        for (int i = 0; i < tile.length; i++) {
            rightRow[i] = getTilesAt(0, i);
        }

        return rightRow;
    }
}
