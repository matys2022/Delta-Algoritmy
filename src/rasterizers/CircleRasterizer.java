package rasterizers;

import models.CanvasEntities.CanvasEntity;
import models.CanvasEntities.Circle;
import models.CanvasEntities.Point;
import models.WindowCanvasMap;
import rasters.Raster;

import java.util.ArrayList;

public class CircleRasterizer implements Rasterizer<Circle> {




    public CircleRasterizer() {
    }

    public ArrayList<Point> rasterize(Circle circle, Raster raster, WindowCanvasMap canvasMap){

        ArrayList<Point> visiblePoints = new ArrayList<>();
        float detail = 0.0002f;


        int width = circle.getBordersWidth();
        int space = circle.getSpace();
        int step = circle.getStep();

        int radius = circle.getRadius();

        int cx = circle.getCenter().getX();
        int cy = circle.getCenter().getY();


        for(int w = 0; w < width; w++) {
            int odd = 0;
            int widthModifier = ((w % 2 == 0) ? 1 : -1) * (w / 2);
            int r = radius + widthModifier;
            double scale;

            if (radius <= 30) {
                scale = radius;
            } else {
                scale = 30 + Math.sqrt(radius );
            }

            double dynamicDetail = step * scale;
            double dynamicGap = space * scale;

            for (double alpha = 0; alpha < Math.PI; alpha += detail) {

                Point point = Circle.calculatePoint(alpha, r, cx, cy);
                int x = point.getX();
                int y = point.getY();

                if (y < raster.getHeight() && y > 0 && x < raster.getWidth() && x > 0) {
                    raster.setPixel(x, y, circle.getBordersColor().getRGB());
                    visiblePoints.add(point);
                }

                if(odd++ >= dynamicDetail){
                    alpha += detail * dynamicGap;
                    odd = 0;
                }

            }
        }

        return visiblePoints;


    }
}
