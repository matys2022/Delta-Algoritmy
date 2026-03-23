package models.CanvasEntities;

import java.awt.*;

public interface CanvasEntityArea extends ComplexCanvasEntity {
    Color getInfillColor();
    public void clearFillPoints();
    public void addFillPoint(Point fillPoint);
    public void setHasFill(boolean hasFill);
}
