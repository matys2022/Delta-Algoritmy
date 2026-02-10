package models.InterfaceEntities;

import java.util.ArrayList;

public class Root extends Element{

    public Root(int windowWidth, int windowHeight, BoundingDimensions padding, ColorSet colorSet, BoundingDimensions border){
        super(0, 0, padding, colorSet, border, new Coordinates(0, 0), null);

        this.children = new ArrayList<>();

        this.setHeight(windowHeight - 1);
        this.setWidth(windowWidth - 1 );
    }


    public void addElement(Element element){

//        if (element.getWidth() > this.getWidth() - getPaddingLeft() - getPaddingRight() - getContent_width()) {
//            setContent_width(element.getWidth() + getPaddingLeft());
            element.setCoordinates(getPaddingLeft(), getContent_height());
            setContent_height(element.getHeight() + getPaddingTop() + getPaddingBottom() + element.getMargin_bottom() + element.getMargin_top());
//        }

//        if(element.getHeight() > super.getContent_height() - getPaddingTop() - getPaddingBottom()){
//
//            super.setHeight( (element.getHeight() + super.getPaddingTop() + super.getPaddingBottom()));
//            super.setContent_height(element.getHeight());
//
//        }
//
//        super.setContent_width(this.getContent_width() + (!children.isEmpty() ? gap : 0) + element.getWidth());
        this.children.add(element);


    }

//    public void removeElement(Element element){
//        if(element.getHeight() == super.getContent_height()){
//            int max = 0;
//            for (Element button : super.children){
//                if(button.getHeight() > max){
//                    max = button.getHeight();
//                }
//            }
//
//            super.setContent_height(max);
//            super.setContent_width(super.getContent_width() - Math.max(element.getWidth() + gap, 0));
//
//        }
//
//        super.children.remove(element);
//    }
//
//    public void clear(){
//        super.children = new ArrayList<>();
//        setContent_height(0);
//        setContent_width(0);
//
//    }

}
