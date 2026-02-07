package models;

import java.awt.*;
import java.util.ArrayList;
import java.util.Optional;

public abstract class Element {
    private Coordinates coordinates;
    private int width;
    private int height;
    private int margin_left;
    private int margin_right;
    private int margin_top;
    private int margin_bottom;
    private int content_width;
    private int content_height;
    protected ColorSet colorSet;
    protected BoundingDimensions border;
    protected ArrayList<Element> children;
    private BoundingDimensions padding;

    public Element(int content_width, int content_height, BoundingDimensions padding, ColorSet colorSet, BoundingDimensions border, Coordinates coordinates) {


        this.content_width = (content_width + padding.getLEFT() + padding.getRIGHT());
        this.content_height = (content_height + padding.getBOTTOM() + padding.getTOP());

        this.width = this.content_width;
        this.height = this.content_height;

        this.colorSet = colorSet;
        this.border = border;

        this.coordinates = coordinates;

        this.padding = padding;

    }
    public Element(int content_width, int content_height, BoundingDimensions padding, ColorSet colorSet) {
        this(content_width, content_height, padding, colorSet, new BoundingDimensions(0, 0, 0, 0), new Coordinates(0, 0));
    }


    private void _setMargin(int margin_left, int margin_top, int margin_right, int margin_bottom){
        this.margin_left = margin_left;
        this.margin_top = margin_top;
        this.margin_right = margin_right;
        this.margin_bottom = margin_bottom;

        this.height = content_height + margin_bottom + margin_top;
        this.width = content_width + margin_left + margin_right;
    }


    public void setMargin(int margin_left, int margin_top, int margin_right, int margin_bottom) {
        this._setMargin(margin_left, margin_top, margin_right, margin_bottom);
    }

    public void setMargin(int left_right, int bottom_top) {
        this._setMargin(left_right, bottom_top, left_right, bottom_top);
    }

    public void setMargin(int margin) {
        this._setMargin(margin, margin, margin, margin);
    }

    public void setMargin(BoundingDimensions dimensions) {
        _setMargin(dimensions.getLEFT(),  dimensions.getTOP(), dimensions.getRIGHT(), dimensions.getBOTTOM());
    }


    public int getX() {
        return coordinates.getX();
    }

    public int getY() {
        return coordinates.getY();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getMargin_left() {
        return margin_left;
    }

    public int getMargin_right() {
        return margin_right;
    }

    public int getMargin_top() {
        return margin_top;
    }

    public int getMargin_bottom() {
        return margin_bottom;
    }

    public int getContent_width() {
        return content_width;
    }

    public int getContent_height() {
        return content_height;
    }

    public Color getBACKGROUND() {
        return colorSet.getBACKGROUND();
    }

    public Color getBORDER() {
        return colorSet.getBORDER();
    }

    public int getLeftBorderWidth(){
        return border.getLEFT();
    }
    public int getRightBorderWidth(){
        return border.getRIGHT();
    }
    public int getTopBorderWidth(){
        return border.getTOP();
    }
    public int getBottomBorderWidth(){
        return border.getBOTTOM();
    }

    protected void setContent_width(int content_width) {
//        this.width = (this.width - this.content_width) + (content_width +  padding.getLEFT() + padding.getRIGHT());
        this.content_width = content_width;
//        this.width = content_width +  margin_left + margin_right;
    }

    protected void setContent_height(int content_height) {
//        this.height = (this.height - this.content_height) + (content_height +  padding.getTOP() + padding.getBOTTOM());
        this.content_height = content_height;

    }

    protected void setHeight(int height){
        this.height = height;
    }

    protected void setWidth(int width){
        this.width = width;
    }

    public ArrayList<Element> getChildren() {
        return children;
    }

    public void setCoordinates(int x, int y){
        this.coordinates = new Coordinates(x, y);
    }

    public void setCoordinates(Coordinates coordinates){
        this.coordinates = coordinates;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public BoundingDimensions getPadding() {
        return padding;
    }

    public int getPaddingLeft(){
        return padding.getLEFT();
    }

    public int getPaddingTop(){
        return padding.getTOP();
    }

    public int getPaddingRight(){
        return padding.getRIGHT();
    }

    public int getPaddingBottom(){
        return padding.getBOTTOM();
    }
}
