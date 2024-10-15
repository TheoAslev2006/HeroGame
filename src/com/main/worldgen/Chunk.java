package com.main.worldgen;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Chunk {
    int[][] tile;
    BufferedImage[] tileImage;


    public Chunk(BufferedImage[] tileImage, int[][] tile) {
        this.tileImage = tileImage;
        this.tile = tile;
    }

    public void renderChunk(Graphics2D g2d, int x, int y, int tileSize){
        g2d.drawLine(x, y, x, y + 32 * 16);
        g2d.drawLine(x, y, x + 16 * 32, y);
        for (int i = 0; i < tile.length; i++) {
            for (int j = 0; j < tile[i].length; j++) {

                int renderX = x + (i * tileSize);
                int renderY = y + (j * tileSize);

                g2d.drawImage(tileImage[tile[i][j]], renderX, renderY, tileSize, tileSize, null);
                g2d.setColor(Color.gray);
                g2d.drawLine(renderX, renderY, renderX, renderY + 32*16);
                g2d.drawLine(renderX, renderY, renderX + 16*32, renderY);
            }
        }
    }

    public int getTilesAt(int x, int y) {
        return tile[x][y];
    }
}
