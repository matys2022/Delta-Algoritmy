package models.CanvasEntities;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Rectangle extends CanvasShape implements CanvasEntity, PolygonEntity{

    private Line[] lines;
    private Point[] points;


    public Rectangle(Line line, Color bordersColor, int bordersWidth, Color infillColor) {

        super(bordersColor, infillColor, bordersWidth);

        lines = new Line[4];
        points = new Point[4];
        int ax = line.pointA.getX();
        int ay = line.pointA.getY();
        int bx = line.pointB.getX();
        int by = line.pointB.getY();


        Point first = new Point(ax, ay);
        Point second = new Point(bx, ay);
        Line top = new Line(line);
        points[0] = top.pointA = first;
        points[1] = top.pointB = second;

        lines[0] = top;

        Point third = new Point(bx, by);
        Line right = new Line(line);
        right.pointA = second;
        points[2] = right.pointB = third;

        lines[1] = right;

        Point fourth = new Point(ax, by);
        Line bottom = new Line(line);
        bottom.pointA = third;
        points[3] = bottom.pointB = fourth;

        lines[2] = bottom;
        Line left = new Line(line);
        left.pointA = fourth;
        left.pointB = first;

        lines[3] = left;
    }

    public int getWidth(){
        return Math.abs(lines[0].pointA.x - lines[0].pointB.x);
    }

    public int getHeight(){
        return Math.abs(lines[1].pointA.y - lines[1].pointB.y);
    }

    public ArrayList<Line> getLines() {
        return new ArrayList<>(Arrays.asList(lines));
    }

    @Override
    public ArrayList<Point> getPoints() {
        return new ArrayList<>(Arrays.asList(points));
    }

    @Override
    public ArrayList<Point> getTransformationAffectedPoints(Point point) {
        return new ArrayList<>(List.of(points));
    }

    @Override
    public void modifyPoint(Point pointRef, int x, int y) {
        if(Arrays.asList(points).contains(pointRef)) {

            for (int i = 0; i < points.length; i++) {
                Point point = points[i];
                if(point.equals(pointRef)) {
                    Point inPoint = points[(i ^ 2)];
                    int prevIx = i - 1;
                    int nextIx = i + 1;
                    if(i == 0)
                    {
                        prevIx = points.length - 1;
                        nextIx = 1;
                    }
                    if(i == points.length - 1){
                        prevIx = i - 1;
                        nextIx = 0;
                    }

                    Point previousPoint = points[prevIx];
                    Point nextPoint = points[nextIx];

                    previousPoint.setX(x);
                    previousPoint.setY(inPoint.getY());

                    nextPoint.setX(inPoint.getX());
                    nextPoint.setY(y);

                    pointRef.setX(x);
                    pointRef.setY(y);
                }


            }
        }
    }


    @Override
    public Point getPoint(int x, int y) {

        for (Point point : points) {
            if(point.getPoint(x, y) != null) {
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
        for (int i = 0; i < lines.length; i++) {
            if(lines[i].pointA.equals(point)) {
                return lines[(i ^ 2)].pointA;
            }
        }

        return null;
    }
}
