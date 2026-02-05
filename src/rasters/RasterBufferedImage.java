package rasters;

import java.awt.*;
import java.awt.image.BufferedImage;

public class RasterBufferedImage implements Raster {

    private final BufferedImage img;
    private int color;

    public BufferedImage getImg() {
        return img;
    }

    public RasterBufferedImage(int width, int height, int type) {
        img = new BufferedImage(width, height,  type);
    }

    public void repaint(Graphics graphics) {
        graphics.drawImage(img, 0, 0, null);
    }

    public Graphics getGraphics(){
        return img.getGraphics();
    }

    @Override
    public int getPixel(int x, int y) {
        return img.getRGB(x, y);
    }

    @Override
    public void setPixel(int x, int y, int color) {
        img.setRGB(x, y, color);
    }

    @Override
    public void clear() {
        Graphics2D g = (Graphics2D) img.getGraphics();

        g.setComposite(AlphaComposite.Clear);
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
        g.dispose();
    }

    @Override
    public void setClearColor(int color) {
        this.color = color;

    }

    @Override
    public int getWidth() {
        return img.getWidth();
    }

    @Override
    public int getHeight() {
        return img.getHeight();
    }

}
