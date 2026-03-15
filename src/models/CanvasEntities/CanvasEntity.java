package models.CanvasEntities;

import java.util.ArrayList;

public interface CanvasEntity {

    void modifyPoint(Point point, int x, int y);

    Point getPoint(int x, int y);
    Point getClosestPoint(int x, int y);

    Point getClosestSibling(Point point);

    public ArrayList<Point> getPoints();

    public ArrayList<Point> getTransformationAffectedPoints(Point point);

}
