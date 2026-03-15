package models.CanvasEntities;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Polygon extends CanvasShape implements CanvasEntity, PolygonEntity{
    private ArrayList<Point> points;
    private ArrayList<Line> lines;

    public Polygon(ArrayList<Point> points, Color borders, int bordersWidth, Color infill, ArrayList<Line> lines) {
        super(borders, infill, bordersWidth);
        this.points = points;
        this.lines = lines;
    }

    public Polygon(ArrayList<Point> points, Color borders, int bordersWidth, Color infill) {
        this(points, borders, bordersWidth, infill, new ArrayList<>());
    }

    public Polygon(Point startingPoint, Color borders, int bordersWidth, Color infill) {
        this(new ArrayList<>(List.of(startingPoint)), borders, bordersWidth, infill);
    }

    public Polygon(Polygon polygon) {
        this(new ArrayList<>(polygon.getPoints()), polygon.getBordersColor(), polygon.getBordersWidth(), polygon.getInfillColor(), new ArrayList<>(polygon.getLines()));
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    @Override
    public ArrayList<Point> getTransformationAffectedPoints(Point point) {
        return new ArrayList<>(List.of(point));
    }

    public ArrayList<Line> getLines() { return lines; }

    public void addPoint(Point point) {
        points.add(point);
    }

    public void popLine() {
        points.removeLast();
        lines.removeLast();
    }

    public void constructPoint(Point point, int width, int space, int step, boolean snapping){

        if(!points.isEmpty())
        {
            lines.add(new Line(points.getLast(), point, this.getBordersColor(),  width, space, step, snapping));
        }

        if(!point.equals(getPoints().getFirst())){
            points.add(point);
        }

    }

    @Override
    public void modifyPoint(Point point, int x, int y) {

        if(points.contains(point)){
            point.modifyPoint(point, x, y);
            for(Line line : lines){
                // if the line contains the point, modify it.
                line.modifyPoint(point, x, y);
            }

        }

    }

    @Override
    public Point getPoint(int x, int y) {

        for(Point point : points){
            if(point.getPoint(x, y) != null){
                return point;
            }
        }

        return null;
    }

    @Override
    public Point getClosestPoint(int x, int y) {

        double minHypot = Double.MAX_VALUE;
        Point closestPoint = null;

        for(Point point : points){
            int px = Math.abs(x - point.getX());
            int py = Math.abs(y - point.getY());
            double ph = Math.hypot(px, py);
            if(Math.hypot(px, py) < minHypot){
                closestPoint = point;
                minHypot = ph;
            }
        }

        return closestPoint;
    }

    @Override
    public Point getClosestSibling(Point point) {

        if (points.size() < 2) return null;

        if(points.contains(point)){
            double distanceAC;
            double distanceBC;
            int ix = points.indexOf(point);

            Point pointA = points.get(ix == 0 ? points.size() - 1 : ix -1);
            Point pointB = points.get(ix == points.size() - 1 ? 0 : ix + 1);

            int ax = Math.abs(pointA.getX() - point.getX());
            int ay = Math.abs(pointA.getY() - point.getY());
            int bx = Math.abs(pointB.getX() - point.getX());
            int by = Math.abs(pointB.getY() - point.getY());

            distanceAC = (Math.pow(ax, 2) + Math.pow(ay, 2));
            distanceBC = (Math.pow(bx, 2) + Math.pow(by, 2));

            if(distanceAC > distanceBC){
                return pointB;
            }else{
                return pointA;
            }
        }
        return null;
    }
}
