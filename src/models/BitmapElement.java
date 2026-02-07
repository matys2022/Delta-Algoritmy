package models;

import java.awt.*;

public class BitmapElement extends Element{

    private Bitmap bitmap;


    public BitmapElement(int content_width, int content_height, Bitmap bitmap, BoundingDimensions padding, ColorSet colorSet, BoundingDimensions border, Coordinates coordinates) {
        super(content_width + padding.getLEFT() + padding.getRIGHT(), content_height + padding.getBOTTOM() + padding.getTOP(), padding, colorSet, border, coordinates);
        this.bitmap = bitmap;
    }

    public BitmapElement(int content_width, int content_height, Bitmap bitmap, BoundingDimensions padding, ColorSet colorSet) {
        super(content_width + padding.getLEFT() + padding.getRIGHT(), content_height + padding.getBOTTOM() + padding.getTOP(), padding, colorSet);
        this.bitmap = bitmap;
    }

    public BitmapElement(Element element, Bitmap bitmap, BoundingDimensions padding){
        this(element.getContent_width(), element.getContent_height(), bitmap, padding, element.colorSet, element.border,  element.getCoordinates());
    }

    public BitmapElement(BitmapElement bitmapElement) {
        this(bitmapElement.getContent_width(), bitmapElement.getContent_height(), bitmapElement.bitmap, bitmapElement.getPadding(), bitmapElement.colorSet, bitmapElement.border, bitmapElement.getCoordinates());
    }

    public int[][] getBitmapContent() {
        return bitmap.getContentBitmap();
    }

    public Color getBitmapColor(int[][] contentBitmap) {
        return bitmap.getContentColor();
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Color getFOREGROUND() {
        return colorSet.getFOREGROUND();
    }



}
