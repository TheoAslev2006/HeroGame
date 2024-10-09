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
}
