package mnist;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;

public class Imagette implements Serializable {
    private final int[][] pixels;

    private final Integer etiquette;

    public Imagette(int width, int height, int etiquette) {
        this.pixels = new int[width][height];
        this.etiquette = etiquette;
    }

    public Imagette(int width, int height) {
        this.pixels = new int[width][height];
        this.etiquette = null;
    }

    public void setPixels(int x, int y, int grayscale) {
        pixels[x][y] = grayscale;
    }

    public void save(File file) throws IOException {
        BufferedImage bufferedImage = new BufferedImage(pixels.length, pixels[0].length, BufferedImage.TYPE_BYTE_GRAY);
        for (int x = 0; x < pixels.length; x++) {
            for (int y = 0; y < pixels[0].length; y++) {
                int raw_color = pixels[x][y];
//                int rgb = new Color(raw_color, raw_color, raw_color).getRGB();
//                bufferedImage.setRGB(x, y, rgb);
                bufferedImage.getRaster().setPixel(x, y, new int[]{raw_color});
            }
        }
        ImageIO.write(bufferedImage, "png", file);
    }



    @Override
    public String toString() {
        StringBuilder pixel = new StringBuilder();
        for (int[] ints : pixels) {
            for (int anInt : ints) {
                pixel.append(" ").append(anInt);
            }
            pixel.append("\n");
        }
        return "Imagette{" +
                "pixels=" + pixel +
                ", etiquette=" + etiquette +
                '}';
    }

    public Integer getEtiquette() {
        return etiquette;
    }

    public int getPixels(int x, int y) {
        return this.pixels[x][y];
    }

    public int getWidth() {
        return this.pixels.length;
    }

    public int getHeight() {
        return this.pixels[0].length;
    }
}
