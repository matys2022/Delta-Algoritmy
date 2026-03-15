package rasterizers;

import models.CanvasEntities.CanvasEntity;
import models.CanvasEntities.Circle;
import models.CanvasEntities.Point;
import models.WindowCanvasMap;
import rasters.Raster;

public class CircleRasterizer implements Rasterizer<Circle> {




    public CircleRasterizer() {
    }

    public void rasterize(Circle circle, Raster raster, WindowCanvasMap canvasMap){
        float detail = 0.0002f;
        int odd = 0;

        int width = circle.getBordersWidth();
        int space = circle.getSpace();
        int step = circle.getStep();

        int radius = circle.getRadius();

        int cx = circle.getCenter().getX();
        int cy = circle.getCenter().getY();


        for(int w = 0; w < width; w++) {
            int widthModifier = ((w % 2 == 0) ? 1 : -1) * (w / 2);
            int r = radius + widthModifier;
            for (double alpha = 0; alpha < Math.PI; alpha += detail) {
                if (odd++ == step) {
                    alpha += detail * space;
                }

                Point point = Circle.calculatePoint(alpha, r, cx, cy);
                int y = point.getY();
                int x = point.getX();

                if (y < raster.getHeight() && y > 0 && x < raster.getWidth() && x > 0) {
                    raster.setPixel(x, y, circle.getBordersColor().getRGB());
                }


            }
        }


    }
}
