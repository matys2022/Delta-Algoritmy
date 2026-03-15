package models.CanvasEntities;

import java.awt.*;

public class CanvasShape implements CanvasEntityBorers, CanvasEntityArea {
    private Color bordersColor;
    private Color infillColor;
    private int bordersWidth;

    public CanvasShape(Color bordersColor, Color infillColor,  int bordersWidth) {
        this.bordersColor = bordersColor;
        this.infillColor = bordersColor;
        this.bordersWidth = bordersWidth;
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
}
