package models.InterfaceEntities;

import models.Icons;

import java.util.ArrayList;

public class BitmapLabel extends BitmapElement {

    public BitmapLabel(Bitmap bitmap, BoundingDimensions padding, ColorSet colorSet, BoundingDimensions border, Coordinates coordinates, Element parent) {
        super(Bitmap.getMaxLength(bitmap.contentBitmap), bitmap.contentBitmap.length, bitmap, padding, colorSet, border, coordinates, parent);

        super.setWidth(Bitmap.getMaxLength(bitmap.contentBitmap));
        super.setHeight(bitmap.contentBitmap.length + getPaddingBottom() + getPaddingTop());

    }

    public void UpdateBitmap(Bitmap bitmap){
        setContent_width(Bitmap.getMaxLength(bitmap.getContentBitmap()));
        setContent_height(bitmap.getContentBitmap().length);

        super.setWidth(Bitmap.getMaxLength(bitmap.getContentBitmap()));
        super.setHeight(bitmap.getContentBitmap().length + getPaddingBottom() + getPaddingTop());

        super.setBitmap(bitmap);

    }





}
