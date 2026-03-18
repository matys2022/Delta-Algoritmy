package models.CanvasEntities;

import java.awt.*;
import java.util.ArrayList;

public abstract class CanvasShape implements CanvasEntityBorers, CanvasEntityArea {
    private Color bordersColor;
    private Color infillColor;
    private boolean hasFill;
    private ArrayList<Point> fillPoints;
    private int bordersWidth;

    public CanvasShape(Color bordersColor, Color infillColor,  int bordersWidth) {
        this.bordersColor = bordersColor;
        this.infillColor = bordersColor;
        this.bordersWidth = bordersWidth;
        this.fillPoints = new ArrayList<>();
    }

    public void clearFillPoints(){
        this.fillPoints.clear();
    }

    public boolean hasFill() {
        return hasFill;
    }

    @Override
    public Color getBordersColor() {
        return bordersColor;
    }

    public void setBordersColor(Color bordersColor) {
        this.bordersColor = bordersColor;
    }

    @Override
    public int getBordersWidth() {
        return this.bordersWidth;
    }

    public void setBordersWidth(int bordersWidth) {
        this.bordersWidth = bordersWidth;
    }


    @Override
    public Color getInfillColor() {
        return infillColor;
    }

    public void setInfillColor(Color infillColor) {
        this.infillColor = infillColor;
    }

    public void setHasFill(boolean hasFill) {
        this.hasFill = hasFill;
    }

    public void addFillPoint(Point fillPoint) {
        this.fillPoints.add(fillPoint);
    }
}
