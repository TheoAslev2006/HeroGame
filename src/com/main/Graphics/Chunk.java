package com.main.Graphics;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Chunk {
    int[][] tile;
    BufferedImage[] tileImage;
    public final int CHUNKSIZE = 16;
    public Chunk(BufferedImage[] tileImage, int[][] tile) {
        this.tileImage= tileImage;
        this.tile = tile;
    }
    public void renderChunk(Graphics2D g2d, int x, int y, int tileSize){
        for (int i = 0; i < tile.length; i++) {
            for (int j = 0; j < tile[i].length; j++) {

                int renderX = x + (i * tileSize);
                int renderY = y + (j * tileSize);

                g2d.drawImage(tileImage[tile[i][j]], renderX, renderY, tileSize, tileSize,null);
            }
        }
    }
}
