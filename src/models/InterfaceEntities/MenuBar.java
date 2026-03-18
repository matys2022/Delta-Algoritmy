package models.InterfaceEntities;

import java.util.ArrayList;

public class MenuBar extends ParentElement {

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

    @Override
    public void refresh(){
        this.setContent_width(this.getPaddingLeft() + this.getPaddingRight());
//        setWidth(this.getPaddingLeft() + this.getPaddingRight());
        for(Element child : this.children){
//            child.setCoordinates(this.getX() + this.getContent_width() - this.getPaddingLeft() + this.getMargin_left() + (this.getContent_width()!=0 ? gap : 0) - getPaddingRight(),this.getY() + this.getPadding().getTOP() + this.getMargin_top());
            super.setContent_width(this.getContent_width() + (getContent_width() != 0 ? gap : 0) + child.getWidth());
//            this.setWidth(child.getWidth() + this.getWidth());

        }
        if(super.getParent() != null){
            super.getParent().refresh();
        }
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
        if(element instanceof ParentElement){
            System.out.println("Refresh " + element.getClass().getSimpleName());
            ((ParentElement) element).refresh();
        }
//        this.refresh();

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
//        this.refresh();
    }

    public void clear(){

        for(Element element : super.children){
            element.setParent(null);
        }
        super.children = new ArrayList<>();
        setContent_height(0);
        setContent_width(0);
//        this.refresh();

    }

    public void removeButtonAt(int ix){
        removeElement(super.children.get(ix));
        this.refresh();
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
