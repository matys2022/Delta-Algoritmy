package factories;

import models.CanvasEntities.Line;
import models.CanvasEntities.Point;
import models.WindowCanvasMap;

import java.awt.*;

public class CanvasEntityFactory {

    WindowCanvasMap windowCanvasMap;

    public CanvasEntityFactory(WindowCanvasMap windowCanvasMap) {
        this.windowCanvasMap = windowCanvasMap;
    }

    public Line createFinalLine(models.CanvasEntities.Point pointA, models.CanvasEntities.Point pointB, Color color, int width, int space, int step, boolean snapping) {
        Line line = new Line(pointA, pointB, color, width, space, step, snapping);
        windowCanvasMap.addCanvasEntityPoint(pointA, line);
        windowCanvasMap.addCanvasEntityPoint(pointB, line);
        return line;
    }

    public Line createFinalLine(Line line) {
        return createFinalLine(line.getPointA(), line.getPointB(), line.getColor(), line.getWidth(), line.getSpace(), line.getStep(), line.isSnapping());
    }

    public Line createPreviewLine(models.CanvasEntities.Point pointA, models.CanvasEntities.Point pointB, Color color, int width, int space, int step, boolean snapping) {
        return createLine(pointA, pointB, color, width, space, step, snapping);
    }

    public Line createPreviewLine(Line line) {
        return createLine(line.getPointA(), line.getPointB(), line.getColor(), line.getWidth(), line.getSpace(), line.getStep(), line.isSnapping());
    }

    private Line createLine(models.CanvasEntities.Point pointA, models.CanvasEntities.Point pointB, Color color, int width, int space, int step, boolean snapping){
        return new Line(pointA, pointB, color, width, space, step, snapping);
    }

    public models.CanvasEntities.Polygon createPolygon(models.CanvasEntities.Point startingPoint) {
        models.CanvasEntities.Polygon polygon = new models.CanvasEntities.Polygon(startingPoint);
//        windowCanvasMap.addCanvasEntityPoint(startingPoint, polygon);
        return polygon;
    }
    public models.CanvasEntities.Polygon createPolygon(models.CanvasEntities.Polygon polygon) {
        models.CanvasEntities.Polygon newPolygon = new models.CanvasEntities.Polygon(polygon);
        for(Point point : newPolygon.getPoints()){
            windowCanvasMap.addCanvasEntityPoint(point, newPolygon);
        }
        return newPolygon;
    }

}
