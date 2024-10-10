package com.main.Sprites;

import com.main.Utils.FileHandling;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WorldObject {
    int x;
    int y;
    int width;
    int height;
    BufferedImage[] texture;
    String[] textureFileLocation = {""};
    public WorldObject(int x, int y, int width, int height) throws IOException {
        texture = new BufferedImage[textureFileLocation.length];
        for (int i = 0; i < textureFileLocation.length; i++) {
            texture[i] = FileHandling.load(new File(textureFileLocation[i]));
        }
    }
}
