package factories;

import models.CanvasEntities.Circle;
import models.CanvasEntities.Line;
import models.CanvasEntities.Point;
import models.CanvasEntities.Rectangle;
import models.WindowCanvasMap;

import java.awt.Color;

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
        return createFinalLine(line.getPointA(), line.getPointB(), line.getBordersColor(), line.getBordersWidth(), line.getSpace(), line.getStep(), line.isSnapping());
    }

    public Line createPreviewLine(models.CanvasEntities.Point pointA, models.CanvasEntities.Point pointB, Color color, int width, int space, int step, boolean snapping) {
        return createLine(pointA, pointB, color, width, space, step, snapping);
    }

    public Line createPreviewLine(Line line) {
        return createLine(line.getPointA(), line.getPointB(), line.getBordersColor(), line.getBordersWidth(), line.getSpace(), line.getStep(), line.isSnapping());
    }

    private Line createLine(models.CanvasEntities.Point pointA, models.CanvasEntities.Point pointB, Color color,  int width, int space, int step, boolean snapping){
        return new Line(pointA, pointB, color,  width, space, step, snapping);
    }

    public models.CanvasEntities.Polygon createPolygon(models.CanvasEntities.Point startingPoint, Color borders, int bordersWidth, Color infill) {
        models.CanvasEntities.Polygon polygon = new models.CanvasEntities.Polygon(startingPoint, borders, bordersWidth, infill);
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

    public Rectangle createRectangle(Line hypotenuse, Color borders, int bordersWidth, Color infill) {
        Rectangle rectangle = new Rectangle(hypotenuse, borders, bordersWidth, infill);
        for(Point point : rectangle.getPoints()){
            windowCanvasMap.addCanvasEntityPoint(point, rectangle);
        }
        return rectangle;
    }

    public Circle createCircle(Line hypotenuse,  Color borders, int bordersWidth, int step, int space, Color infill) {
        Circle circle = new Circle(hypotenuse, borders, bordersWidth, step, space, infill);
        return  circle;
    }

}
