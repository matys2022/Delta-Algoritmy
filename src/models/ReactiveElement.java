package models;

import java.awt.*;

public class ReactiveElement extends BitmapElement {

    protected ColorSet secondaryColorSet;
    private boolean isActive = false;

    public ReactiveElement(int content_width, int content_height, Bitmap bitmap, BoundingDimensions padding, ColorSet colorSet, ColorSet secondaryColorSet, BoundingDimensions border, Coordinates coordinates) {
        super(content_width, content_height, bitmap, padding, colorSet, border, coordinates);
        this.secondaryColorSet = secondaryColorSet;

    }

    public ReactiveElement(int content_width, int content_height, Bitmap bitmap, BoundingDimensions padding, ColorSet colorSet, ColorSet secondaryColorSet) {
        super(content_width, content_height, bitmap, padding, colorSet);
        this.secondaryColorSet = secondaryColorSet;
    }

    public ReactiveElement(BitmapElement bitmapElement, ColorSet secondaryColorSet) {
       this(bitmapElement.getContent_width(), bitmapElement.getContent_height(), bitmapElement.getBitmap(), bitmapElement.getPadding(), bitmapElement.colorSet, secondaryColorSet,  bitmapElement.border, bitmapElement.getCoordinates());
    }

    public ReactiveElement(ReactiveElement reactiveElement) {
        this(reactiveElement.getContent_width(), reactiveElement.getContent_height(), reactiveElement.getBitmap(), reactiveElement.getPadding(), reactiveElement.colorSet, reactiveElement.secondaryColorSet,  reactiveElement.border, reactiveElement.getCoordinates());
    }

    public Color getBACKGROUND_SELECTED() {
        return secondaryColorSet.getBACKGROUND();
    }

    public Color getBORDER_SELECTED() {
        return secondaryColorSet.getBORDER();
    }

    public Color getFOREGROUND_SELECTED() {
        return secondaryColorSet.getFOREGROUND();
    }

    public void toggleColorState(){
        ColorSet colorSet = super.colorSet;
        super.colorSet = this.secondaryColorSet;
        this.secondaryColorSet = colorSet;

        this.isActive = !this.isActive;
    }

    public boolean isActive() {
        return isActive;
    }
}
