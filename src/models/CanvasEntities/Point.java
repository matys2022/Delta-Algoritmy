package models.CanvasEntities;

public class Point implements CanvasEntity{

    int x;
    int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
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
    public Point getClosestChild(Point point) {
        return this;
    }
}
