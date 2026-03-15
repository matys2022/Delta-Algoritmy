package models.CanvasEntities;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Point extends CanvasLine implements CanvasEntity{

    int x;
    int y;

    public Point(int x, int y, int bordersWidth, Color color) {
        super(color, bordersWidth);
        this.x = x;
        this.y = y;
    }

    public Point(int x, int y) {
        this(x, y, 1, Color.RED);
    }

    public Point(Point p) {
        this(p.x, p.y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public void modifyPoint(Point point, int x, int y) {
        setX(x);
        setY(y);
    }

    @Override
    public Point getPoint(int x, int y) {
        if(this.x == x && this.y == y){
            return this;
        }else{
            return null;
        }
    }

    @Override
    public Point getClosestPoint(int x, int y) {
        return this;
    }

    @Override
    public Point getClosestSibling(Point point) {
        return this;
    }

    @Override
    public ArrayList<Point> getPoints() {
        ArrayList<Point> list = new ArrayList<>(){};
        list.add(this);
        return list;
    }

    @Override
    public ArrayList<Point> getTransformationAffectedPoints(Point point) {
        return new ArrayList<>(List.of(point));
    }
}
