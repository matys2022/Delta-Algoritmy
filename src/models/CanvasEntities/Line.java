package models.CanvasEntities;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Line extends CanvasLine implements CanvasEntity{

    Point pointA, pointB;
    int step, space;
    boolean snapping;


    public Line(Point pointA, Point pointB, Color lineColor, int bordersWidth, int space, int step, boolean snapping) {
        super(lineColor, bordersWidth);
        this.pointA = pointA;
        this.pointB = pointB;
        this.space = space;
        this.step = step;
        this.snapping = snapping;
    }

    public Line(Line line){
        this(line.getPointA(), line.getPointB(), line.getBordersColor(), line.getBordersWidth(), line.space, line.step, line.snapping);
    }


    public boolean isSnapping() {
        return snapping;
    }

    public void setSnapping(boolean snapping) {
        this.snapping = snapping;
    }

    public int getSpace() {
        return space;
    }

    public void setSpace(int space) {
        this.space = space;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public Point getPointA() {
        return pointA;
    }

    public Point getPointB() {
        return pointB;
    }

    public void setPointA(Point pointA) {
        this.pointA = pointA;
    }

    public void setPointB(Point pointB) {
        this.pointB = pointB;
    }


    @Override
    public void modifyPoint(Point point, int x, int y) {
        if(pointA.equals(point)){
            pointA.modifyPoint(point, x, y);
        }else if(pointB.equals(point)){
            pointB.modifyPoint(point, x, y);
        }
    }

    @Override
    public Point getPoint(int x, int y) {

        if(pointA.getPoint(x, y) != null){
            return pointA;
        }else if(pointB.getPoint(x, y) != null){
            return pointB;
        }
        return null;
    }

    @Override
    public Point getClosestPoint(int x, int y) {
        double minHypot = Double.MAX_VALUE;
        int bx = Math.abs(x - pointB.getX());
        int by = Math.abs(y - pointB.getY());
        int ax = Math.abs(x - pointA.getX());
        int ay = Math.abs(y - pointA.getY());
        if(Math.hypot(ax, ay) < Math.hypot(bx, by)){
            return pointA;
        }else{
            return pointB;
        }

    }

    @Override
    public Point getClosestSibling(Point point) {
        if(pointA.equals(point)){
            return pointB;
        }else{
            return pointA;
        }
    }

    @Override
    public ArrayList<Point> getPoints() {
        ArrayList<Point> points = new ArrayList<>();
        points.add(pointA);
        points.add(pointB);
        return points;
    }

    @Override
    public ArrayList<Point> getTransformationAffectedPoints(Point point) {
        return new ArrayList<>(List.of(pointA, pointB, point));
    }
}
