package models.CanvasEntities;

import java.awt.*;
import java.util.*;

public class FillEntity implements ComplexCanvasEntity {
    ArrayList<Point> points;
    ArrayList<Point> visiblePoints;
    Color infill;
    Point fillReferencePoint;

    public FillEntity(ArrayList<Point> points, Point reference, Color infill) {
        this.points = points;
        this.visiblePoints = new ArrayList<>();
        this.infill = infill;
        this.fillReferencePoint = reference;
    }

    public Color getInfill() {
        return infill;
    }

    public void setInfill(Color infill) {
        this.infill = infill;
    }

    public void setFillReferencePoint(Point fillReferencePoint) {
        this.fillReferencePoint = fillReferencePoint;
    }

    @Override
    public ArrayList<Point> getVisiblePoints() {
        return this.visiblePoints;
    }

    @Override
    public void addVisiblePoint(Point point) {
        this.visiblePoints.add(point);
//        this.points.add(point);
    }

    @Override
    public void addVisiblePoints(ArrayList<Point> point) {
        this.visiblePoints.addAll(point);
    }

    @Override
    public void clearVisiblePoints() {
        this.visiblePoints.clear();
    }


    @Override
    public void move(int diffX, int diffY) {
        for(Point point : points){
            point.modifyPoint(point, point.getX() + diffX, point.getY() + diffY);
        }
        fillReferencePoint.modifyPoint(fillReferencePoint, fillReferencePoint.getX() + diffX, fillReferencePoint.getY() + diffY);
    }

    @Override
    public void modifyPoint(Point point, int x, int y) {

        visiblePoints = new ArrayList<>();
    }


    @Override
    public Point getPoint(int x, int y) {
        Point point = new Point(x, y);
        if(points.contains(point)) {
            point.modifyPoint(point,  x, y);
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

    @Override
    public ArrayList<Point> getPoints() {
        return this.points;
    }

    @Override
    public ArrayList<Point> getTransformationAffectedPoints(Point point) {
        return this.points;
    }


    public Point getFillReferencePoint() {
        return fillReferencePoint;
    }
}

