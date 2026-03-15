package models.CanvasEntities;

import java.awt.*;

public class CanvasLine implements CanvasEntityBorers {
    private Color bordersColor;
    private int bordersWidth;

    public CanvasLine(Color bordersColor, int bordersWidth) {
        this.bordersColor = bordersColor;
        this.bordersWidth = bordersWidth;
    }

    public Color getBordersColor() {
        return bordersColor;
    }

    @Override
    public int getBordersWidth() {
        return this.bordersWidth;
    }

    public void setBordersWidth(int bordersWidth) {
        this.bordersWidth = bordersWidth;
    }

    public void setBordersColor(Color bordersColor) {
        this.bordersColor = bordersColor;
    }
}
