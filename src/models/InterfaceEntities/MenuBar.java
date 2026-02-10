package models.InterfaceEntities;

import java.util.ArrayList;

public class MenuBar extends Element {

    private int gap;

    public MenuBar(int width, int height, int gap, BoundingDimensions padding, BoundingDimensions margin, ColorSet colors) {
        super(0, 0, padding, colors);

        super.setMargin(margin);

        super.children = new ArrayList<>();

        super.setWidth(width);
        super.setHeight(height - margin.getTOP() - margin.getBOTTOM());

//        super.setContent_width(width - margin.getLEFT() - margin.getRIGHT());

        this.gap = gap;
    }

    public MenuBar(int width, int height, int gap, ColorSet colors) {
        this(width, height, gap, new BoundingDimensions(5, 5, 5, 5), new BoundingDimensions(0, 0, 0, 0), colors);
    }



    public void addElement(Element element){

        element.setCoordinates(this.getX() + this.getContent_width() + this.getMargin_left() + (!children.isEmpty() ? gap : 0) - getPaddingRight(),this.getY() + this.getPadding().getTOP() + this.getMargin_top());

        if(element.getHeight() > super.getContent_height() - getPaddingTop() - getPaddingBottom()){

            super.setHeight( (element.getHeight() + super.getPaddingTop() + super.getPaddingBottom()));
            super.setContent_height(element.getHeight());

        }

        element.setParent(this);
        super.setContent_width(this.getContent_width() + (!children.isEmpty() ? gap : 0) + element.getWidth());
        super.children.add(element);

    }

    public void removeElement(Element element){
        if(element.getHeight() == super.getContent_height()){
            int max = 0;
            for (Element button : super.children){
                if(button.getHeight() > max){
                    max = button.getHeight();
                }
            }

            super.setContent_height(max);
            super.setContent_width(super.getContent_width() - Math.max(element.getWidth() + gap, 0));

        }

        element.setParent(null);
        super.children.remove(element);
    }

    public void clear(){

        for(Element element : super.children){
            element.setParent(null);
        }
        super.children = new ArrayList<>();
        setContent_height(0);
        setContent_width(0);

    }

    public void removeButtonAt(int ix){
        removeElement(super.children.get(ix));
    }

    public void disableAll(){
        for(Element element : super.children){
            if(element instanceof ReactiveElement reactive){
                if(reactive.isActive()) {
                    reactive.toggleColorState();
                }
            }
        }
    }

}
