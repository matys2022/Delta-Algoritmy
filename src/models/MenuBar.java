package models;

import java.awt.*;
import java.util.ArrayList;

public class MenuBar extends Element {

    private int gap;

    public MenuBar(int width, int height,  int gap, BoundingDimensions padding, BoundingDimensions margin, ColorSet colors) {
        super(0, 0, padding, colors);

        super.setMargin(margin);

        super.children = new ArrayList<>();

        super.setWidth(width);
//        super.setHeight(height - margin.getTOP() - margin.getBOTTOM());

//        super.setContent_width(width - margin.getLEFT() - margin.getRIGHT());

        this.gap = gap;
    }

    public MenuBar(int width, int height, int gap, ColorSet colors) {
        this(width, height, gap, new BoundingDimensions(5, 5, 5, 5), new BoundingDimensions(0, 0, 0, 0), colors);
    }



    public void addButton(Button button){

        button.setCoordinates(this.getContent_width() + this.getMargin_left() + (!children.isEmpty() ? gap : 0) - getPaddingRight(),this.getPadding().getTOP() + this.getMargin_top());

        if(button.getHeight() > super.getContent_height()){

            super.setHeight((super.getHeight() - super.getContent_height()) + (button.getHeight() +  getPaddingTop() + getPaddingBottom()));
            super.setContent_height(button.getHeight());

        }

        super.setContent_width(this.getContent_width() + (!children.isEmpty() ? gap : 0) + button.getWidth());
        super.children.add(button);


//        super.setContent_width(super.getContent_width() + button.getWidth());




    }

    public void removeButton(Element new_button){
        if(new_button.getHeight() == super.getContent_height()){
            int max = 0;
            for (Element button : super.children){
                if(button.getHeight() > max){
                    max = button.getHeight();
                }
            }

            super.setContent_height(max);
            super.setContent_width(super.getContent_width() - Math.max(new_button.getWidth() + gap, 0));

        }

        super.children.remove(new_button);
    }

    public void removeButtonAt(int ix){
        removeButton(super.children.get(ix));
    }



}
