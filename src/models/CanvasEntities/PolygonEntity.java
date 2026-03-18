package models.CanvasEntities;

import java.util.ArrayList;

public interface PolygonEntity extends ComplexCanvasEntity{
    public ArrayList<Line> getLines();
    public boolean isPointInPolygon(Point p);
}
