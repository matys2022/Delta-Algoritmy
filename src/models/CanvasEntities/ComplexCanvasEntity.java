package models.CanvasEntities;

import java.util.ArrayList;

public interface ComplexCanvasEntity extends CanvasEntity {
    public ArrayList<Point> getVisiblePoints();
    public void addVisiblePoint(Point point);
    public void addVisiblePoints(ArrayList<Point> point);
    public void clearVisiblePoints();
    public void move(int diffX, int diffY);
}
