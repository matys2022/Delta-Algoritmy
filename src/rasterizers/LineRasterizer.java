package rasterizers;

import models.CanvasEntities.Line;
import models.CanvasEntities.Point;
import models.WindowCanvasMap;
import rasters.Raster;

import java.util.ArrayList;

public class LineRasterizer implements Rasterizer<Line> {

//    private Raster raster;

    public LineRasterizer() {
    }

    public ArrayList<Point> rasterize(Line line, Raster raster, WindowCanvasMap canvasMap) {
        Point a = new Point(line.getPointA());
        Point b = new Point(line.getPointB());
        ArrayList<Point> visiblePoints = new ArrayList<>();

//        canvasMap.addCanvasEntityPoint(a, line);
//        canvasMap.addCanvasEntityPoint(b, line);

        int diffX = b.getX() - a.getX();
        int diffY = b.getY() - a.getY();

        int step = line.getStep();
        int space = line.getSpace();
        int width = line.getBordersWidth();


        double k = (double)(b.getY() - a.getY()) / (diffX != 0 ? diffX : 1 );

        double factor = Math.abs(k);

        double q = a.getY() - a.getX() * k;




//        System.out.println(a.getX() + " " + a.getY() + " " + b.getX() + " " + b.getY());
//        System.out.println("I :" + (a.getX() <= b.getX() && a.getY() >= b.getY())  + " II :" +  (a.getX() >= b.getX() && a.getY() >= b.getY()) + " III :" + (a.getX() >= b.getX() && a.getY() <= b.getY()) + " IV :" + (a.getX() <= b.getX() && a.getY() <= b.getY()));

        for(int w = 0; w < width; w++) {
            int odd = 0;

            int widthModifier = ((w%2==0)?1:-1)*(w/2);

            if ((factor < 1) && (diffX != 0 || diffY == 0)) { // Calculate Y (X loop) (>–<)

                int start = Math.min(Math.max(a.getX(), 0), raster.getWidth() - space);
                int end = Math.min(Math.max(b.getX(), 0), raster.getWidth() - space);

                int currentStart = Math.min(start, end);
                int currentEnd = Math.max(start, end);

                for (int x = currentStart; x < currentEnd; x++) {

                    if (odd++  == step) {
                        x += space;
                        odd = 0;

                        if (x >= currentEnd) break;
                    }

                    int y = (int) Math.round((k * (double) x + q)) + widthModifier;

                    // If the coordinates are outside the window, therefore the mouse gone out of the window.
                    if (y < raster.getHeight() && y > 0) {
                        raster.setPixel(x, y, line.getBordersColor().getRGB());
                        visiblePoints.add(new Point(x, y));
                    }
                }


            } else { // Calculate X (Y loop) (Y)
//                                            ^
                int start = Math.min(Math.max(a.getY(), 0), raster.getHeight() - space);
                int end = Math.min(Math.max(b.getY(), 0), raster.getHeight() - space);

                int currentStart = Math.min(start, end);
                int currentEnd = Math.max(start, end);


                for (int y = currentStart; y < currentEnd; y++) {

                    if (odd++ == step) {
                        y += space;
                        odd = 0;
                        if (y >= currentEnd) break;
                    }

                    int x = (int) Math.round(((double) y - q) / k);


                    // When a straight line has been drawn, this will ensure, that it will actually be straight
                    if (diffX == 0) {
                        x = a.getX();
                    }

                    x+= widthModifier;

                    // If the coordinates are outside the window, therefore the mouse gone out of the window.
                    if (x < raster.getWidth() && x >= 0) {
                        raster.setPixel(x, y, line.getBordersColor().getRGB());
                        visiblePoints.add(new Point(x, y));
                    }

                }
            }
        }

        return  visiblePoints;
    }
}
