package com.main.worldgen;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class WorldObjectChunk {
    int[][] tile;
    BufferedImage[] tileImage;
    public final int CHUNKSIZE = 16;
    int randomOffsetX, randomOffsetY;
    Random random = new Random();
    public WorldObjectChunk(BufferedImage[] tileImage, int[][] tile) {
        this.tileImage= tileImage;
        this.tile = tile;

    }
    public void renderObject(Graphics2D g2d, int x, int y, int tileSize){
        for (int i = 0; i < CHUNKSIZE- 1; i++) {
            for (int j = 0; j < CHUNKSIZE- 1; j++) {

                int renderX = x + (i * tileSize);
                int renderY = y + (j * tileSize);
                if (tile[i][j] == 1)
                    g2d.drawImage(tileImage[tile[i][j]], renderX - 4, renderY, 40, 64, null);
            }
        }
    }
}
