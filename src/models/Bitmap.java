package models;

import java.awt.*;

public class Bitmap {
    int[][] contentBitmap;
    private Color contentColor;

    public Bitmap(int[][] contentBitmap, Color contentColor) {
        this.contentBitmap = contentBitmap;
        this.contentColor = contentColor;
    }

    public int[][] getContentBitmap() {
        return contentBitmap;
    }

    public Color getContentColor() {
        return contentColor;
    }
}
