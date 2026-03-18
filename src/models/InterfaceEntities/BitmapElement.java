package models.InterfaceEntities;

import java.awt.*;

public class BitmapElement extends Element {

    private Bitmap bitmap;


    public BitmapElement(int content_width, int content_height, Bitmap bitmap, BoundingDimensions padding, ColorSet colorSet, BoundingDimensions border, Coordinates coordinates, Element parent) {
        super(content_width, content_height, padding, colorSet, border, coordinates, parent);
        this.bitmap = bitmap;
    }

    public BitmapElement(int content_width, int content_height, Bitmap bitmap, BoundingDimensions padding, ColorSet colorSet) {
        super(content_width, content_height, padding, colorSet);
        this.bitmap = bitmap;
    }

    public BitmapElement(Element element, Bitmap bitmap, BoundingDimensions padding){
        this(element.getContent_width(), element.getContent_height(), bitmap, padding, element.colorSet, element.border,  element.getCoordinates(), null);
    }

    public BitmapElement(BitmapElement bitmapElement) {
        this(bitmapElement.getContent_width(), bitmapElement.getContent_height(), bitmapElement.bitmap, bitmapElement.getPadding(), bitmapElement.colorSet, bitmapElement.border, bitmapElement.getCoordinates(), bitmapElement.getParent());
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
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Color getFOREGROUND() {
        return colorSet.getFOREGROUND();
    }



}
