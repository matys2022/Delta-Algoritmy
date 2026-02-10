package models.CanvasEntities;

import java.awt.*;

public class Line {

    Point pointA, pointB;
    Color color;
    int width;
    int step, space;
    boolean snapping;



    public Line(Point pointA, Point pointB, Color color, int width, int space, int step, boolean snapping) {
        this.pointA = pointA;
        this.pointB = pointB;
        this.color = color;
        this.space = space;
        this.width = width;
        this.step = step;
        this.snapping = snapping;
    }

    public Line(Point pointA, Point pointB, Color color) {
        this(pointA, pointB, color, 1, 0, 0, false);
    }
    public Line(Point pointA, Point pointB, Color color, int width) {
        this(pointA, pointB, color, width, 0, 0, false);
    }
    public Line(Point pointA, Point pointB, Color color, int width, boolean snapping) {
        this(pointA, pointB, color, 1, 0, 0, snapping);
    }
    public Line(Point pointA, Point pointB, Color color, boolean snapping) {
        this(pointA, pointB, color, 1, 0, 0, snapping);
    }
    public Line(Point pointA, Point pointB, Color color, int space, int step, boolean snapping) {
        this(pointA, pointB, color, 1, space, step, snapping);
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

    public int getWidth() {
        return width;
    }

    public int getStep() {
        return step;
    }

    public void setWidth(int width) {
        this.width = width;
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

    public Color getColor() {
        return color;
    }


    public void setPointA(Point pointA) {
        this.pointA = pointA;
    }

    public void setPointB(Point pointB) {
        this.pointB = pointB;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
