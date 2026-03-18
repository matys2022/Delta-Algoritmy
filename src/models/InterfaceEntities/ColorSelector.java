package models.InterfaceEntities;

import models.Icons;

import java.awt.*;
import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ColorSelector extends ParentElement {

    BiConsumer<ReactiveElement, Color> onColorChange;
    ArrayList<Color> colorPalette;
    Color selectedColor;

    public ColorSelector(BoundingDimensions padding, ColorSet colorSet, BiConsumer<ReactiveElement, Color> onColorChange, Color defaultColor, ParentElement parent) {
        super(0, 0, padding, colorSet);
        super.children = new ArrayList<>();

//        super.setWidth(20);
        super.setHeight(20);

        this.onColorChange = onColorChange;
        this.colorPalette = new ArrayList<>();

        this.setParent(parent);

        this.selectedColor = defaultColor;

    }


    public void addColor(Color color) {
        if(colorPalette.contains(color)){
            return;
        }

        Icons icons = new Icons();
        ColorSet polygonBtnColors = new ColorSet(color, color, color);
        ColorSet polygonBtnHoverColors = new ColorSet(color, Color.BLACK, color);
        Button colorBtn = new Button(icons.getIconData(icons.blockIcon), new BoundingDimensions(0), polygonBtnColors, polygonBtnHoverColors, new BoundingDimensions(2), new Coordinates(0, 0));

        colorBtn.setCoordinates(this.getX() + this.getContent_width() + this.getMargin_left() + (!children.isEmpty() ? 5 : 0) - getPaddingRight(),this.getY() + this.getPadding().getTOP() + this.getMargin_top());

        colorBtn.setButtonConsumer(button -> {onColorChange.accept(colorBtn, color); this.selectedColor = color;});

        if(colorBtn.getHeight() > super.getContent_height() - getPaddingTop() - getPaddingBottom()){

            super.setHeight( (colorBtn.getHeight() + super.getPaddingTop() + super.getPaddingBottom()));
            super.setContent_height(colorBtn.getHeight());

        }

        colorBtn.setParent(this);
        super.setContent_width(this.getContent_width() + (!children.isEmpty() ? 5 : 0) + colorBtn.getWidth());
        setWidth(this.getContent_width());

        children.add(colorBtn);
        if(color==this.selectedColor){
            colorBtn.toggleColorState();
        }
        colorPalette.add(color);

        this.refresh();
    }

    public Color getSelectedColor() {
        return selectedColor;
    }

    @Override
    public void refresh(){
        setWidth(this.getPaddingLeft() + this.getPaddingRight());
        setContent_width(this.getPaddingLeft() + this.getPaddingRight());

        for(Element child : this.children){
            child.setCoordinates(this.getX() + this.getContent_width() + this.getMargin_left() + (!(this.getPaddingLeft() + this.getPaddingRight() == this.getContent_height()) ? 5 : 0) - getPaddingRight(),this.getY() + this.getPadding().getTOP() + this.getMargin_top());
            super.setContent_width(this.getContent_width() + (!children.isEmpty() ? 5 : 0) + child.getWidth());
            setWidth(this.getContent_width());
        }
        if(super.getParent() != null){
            super.getParent().refresh();
        }
    }

}
