package com.main.worldgen;

import java.awt.*;
import java.awt.image.BufferedImage;

public class WorldObject {
    int[][] tile;
    BufferedImage[] tileImage;
    public final int CHUNKSIZE = 16;
    public WorldObject(BufferedImage[] tileImage, int[][] tile) {
        this.tileImage= tileImage;
        this.tile = tile;
    }
    public void renderObject(Graphics2D g2d, int x, int y, int tileSize){
        for (int i = 0; i < CHUNKSIZE; i++) {
            for (int j = 0; j < CHUNKSIZE; j++) {

                int renderX = x + (i * tileSize);
                int renderY = y + (j * tileSize);

                g2d.drawImage(tileImage[tile[i][j]], renderX, renderY, null);
            }
        }
    }
}
