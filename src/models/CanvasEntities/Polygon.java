package models.CanvasEntities;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Polygon implements CanvasEntity{
    private ArrayList<Point> points;
    private ArrayList<Line> lines;

    public Polygon(ArrayList<Point> points, ArrayList<Line> lines) {
        this.points = points;
        this.lines = lines;
    }

    public  Polygon(ArrayList<Point> points) {
        this.points = points;
        this.lines = new ArrayList<>();
    }

    public Polygon(Point startingPoint) {
        this(new ArrayList<>(List.of(startingPoint)));
    }

    public Polygon(Polygon polygon) {
        this(new ArrayList<>(polygon.getPoints()), new ArrayList<>(polygon.getLines()));
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

        if(!point.equals(getPoints().getFirst())){
            points.add(point);
        }


    }

    @Override
    public void modifyPoint(Point point, int x, int y) {

        if(points.contains(point)){
            point.modifyPoint(point, x, y);
            for(Line line : lines){
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
    public Point getClosestChild(Point point) {

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
