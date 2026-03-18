package models.InterfaceEntities;

import models.Icons;

import java.util.ArrayList;
import java.util.function.Consumer;

public class WidthSelector extends ParentElement {
    private int width = 1;

    public WidthSelector(BoundingDimensions padding, ColorSet colorSet, ParentElement parent) {
        super(0, 0, padding, colorSet);

//        super.setMargin(margin);


        super.children = new ArrayList<>();

        this.setParent(parent);

        super.setWidth(0);
        super.setHeight(0);

    }

    public void addElement(Element element){

        element.setCoordinates(this.getX() + this.getMargin_left() + this.getPaddingLeft(),this.getY() + this.getContent_height() - this.getPaddingBottom() + this.getPaddingTop() + (this.children.isEmpty() ? 0 : 5));

        if(element.getWidth() > super.getContent_width() - getPaddingRight() - getPaddingLeft()){

            super.setWidth(element.getWidth() + getPaddingRight() + getPaddingLeft());
            super.setContent_width(element.getWidth() + getPaddingRight() + getPaddingLeft());

        }

        element.setParent(this);
        super.setContent_height(this.getContent_height()  + (!children.isEmpty() ? 5 : 0) + element.getHeight());
        super.setHeight(super.getContent_height());

        super.children.add(element);
        this.refresh();

    }

    @Override
    public void refresh() {
        this.setContent_height(this.getPaddingBottom() + this.getPaddingTop());
        this.setHeight(this.getPaddingBottom() + this.getPaddingTop());
        for(Element child : this.children){
            child.setCoordinates(this.getCoordinates().getX() + this.getMargin_left() + this.getPaddingLeft(),this.getY() + this.getContent_height() - this.getPaddingBottom() + this.getPaddingTop() + (!(this.getPaddingBottom() + this.getPaddingTop() == this.getContent_height()) ? 2 : 0));
            super.setContent_height(this.getContent_height()  + (!children.isEmpty() ? 2 : 0) + child.getHeight());
        }
        this.setHeight(this.getHeight() + this.getContent_height());
        if(super.getParent() != null){
            super.getParent().refresh();
        }
    }

    private static int getMaxLength(int[][] matrix) {
        int max = 0;
        if (matrix == null) return 0;

        for (int[] row : matrix) {
            if (row != null && row.length > max) {
                max = row.length;
            }
        }



        return max;
    }

}
