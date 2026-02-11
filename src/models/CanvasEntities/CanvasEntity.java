package models.CanvasEntities;

public interface CanvasEntity {

    void modifyPoint(Point point, int x, int y);

    Point getPoint(int x, int y);

}
