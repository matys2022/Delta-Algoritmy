package models.CanvasEntities;

import com.sun.source.tree.CaseTree;

import java.awt.*;
import java.util.ArrayList;

public class Circle extends CanvasShape implements CanvasEntity {

    Point center;
    int radius;
    Point tmpCircumference;
    int step, space;

    ArrayList<Point> drawnPoints;

    public Circle(Point center, Color borders, int bordersWidth, int step, int space, Color infill, int radius) {

        super(borders, infill,  bordersWidth);

        this.step = step;
        this.space = space;
        this.center = center;
        this.radius = radius;
        this.drawnPoints = null;
    }

    public Circle(Line radius, Color borders, int bordersWidth, int step, int space, Color infill) {
        this(radius.pointA, borders, bordersWidth, step, space, infill, calculateHypotenuse(radius.getPointA(), radius.getPointB()));
    }


    public ArrayList<Point> calculatePoints() {
        ArrayList<Point> points = new ArrayList<>();
        for(double alpha = 0; alpha < Math.PI; alpha+=0.001){
            points.add(Circle.calculatePoint(alpha, radius, center.getX(), center.getY()));
        }

        return points;
    }

    private static int calculateHypotenuse(Point pointA, Point pointB){
        double a = Math.abs(pointA.getX() - pointB.getX());
        double b = Math.abs(pointA.getY() -  pointB.getY());

        return (int)Math.round(Math.hypot(a,b));
    }



    public static Point calculatePoint(double angle, int radius, int cx, int cy){
        double b = 2*radius*Math.cos(angle);

        int x = (int)Math.round(Math.cos(angle) * b + cx - radius);
        int y = (int)Math.round(Math.sin(angle) * b + cy);

        return new Point(x, y);
    }


    public Point getCenter() {
        return center;
    }

    public int getRadius() {
        return radius;
    }

    @Override
    public void modifyPoint(Point point, int x, int y) {
        if(center.equals(point)) {
            center = new Point(x, y);
        } else if(point.equals(tmpCircumference)) {
            radius = calculateHypotenuse(center, new Point(x, y));
        }else{
            return;
        }
        this.drawnPoints = null;
//        this.drawnPoints.add(point);
    }

    @Override
    public Point getPoint(int x, int y) {
        if(center.getPoint(x, y) != null) {
            return center;
        }

        Point p = new Point(x,y);

        if(Math.abs(calculateHypotenuse(center, p) - radius) <= 1) {
            tmpCircumference = new Point(p);
            return tmpCircumference;
        }

        return null;
    }

    @Override
    public Point getClosestPoint(int x, int y) {
        Point p = new Point(x,y);

        if(Math.abs(calculateHypotenuse(center, p) - radius) <= 1) {
            tmpCircumference = new Point(p);
            return tmpCircumference;
        }

        return center;
    }

    @Override
    public Point getClosestSibling(Point point) {
        if(center.equals(point)) {
            return center;
        }
        if(point.equals(tmpCircumference)) {
            return tmpCircumference;
        }

        return null;
    }

    @Override
    public ArrayList<Point> getPoints() {
        return null;
    }

    @Override
    public ArrayList<Point> getTransformationAffectedPoints(Point point) {
        if(this.drawnPoints == null) {
            this.drawnPoints = calculatePoints();
        }
        return drawnPoints;
    }

    public int getStep() {
        return step;
    }

    public int getSpace() {
        return space;
    }
}
