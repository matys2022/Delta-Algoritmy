package models.CanvasEntities;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Polygon {
    private ArrayList<Point> points;
    private ArrayList<Line> lines;

    public  Polygon(ArrayList<Point> points) {
        this.points = points;
        this.lines = new ArrayList<>();
    }
    public Polygon(Point startingPoint) {
        this(new ArrayList<>(List.of(startingPoint)));
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public ArrayList<Line> getLines() { return lines; }

    public void addPoint(Point point) {
        points.add(point);
    }

    public void popLine() {
        points.removeLast();
        lines.removeLast();
    }

    public void constructPoint(Point point, Color color, int width, int space, int step, boolean snapping){

        if(!points.isEmpty())
        {
            lines.add(new Line(points.getLast(), point, color,  width, space, step, snapping));
        }
        points.add(point);

    }

}
