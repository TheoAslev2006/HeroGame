package com.main.Utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class FileHandling {
    public static BufferedImage load(File file) throws IOException {
        byte[] bytes = Files.readAllBytes(file.toPath());
        try (InputStream stream=new ByteArrayInputStream(bytes)){
            return ImageIO.read(stream);
        }
    }
    public static BufferedImage[] loadTileSet(String file, int tileWidth, int tileHeight) throws IOException {
        BufferedImage tileSet = ImageIO.read(new File(file));
        int columns = tileSet.getWidth()/tileWidth;
        int rows = tileSet.getHeight()/tileHeight;

        BufferedImage[] tileArray = new BufferedImage[rows*columns];
        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < columns; i++) {
                tileArray[j * columns + i] = tileSet.getSubimage(i *tileWidth, j * tileHeight, tileWidth, tileHeight);
            }
        }

        return tileArray;
    }
}
