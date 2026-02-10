package rasterizers;

import models.CanvasEntities.Line;
import models.CanvasEntities.Point;
import rasters.Raster;

import java.awt.*;

public class TrivialRasterizer implements LineRasterizer {

    private Color color;
    private Raster raster;

    public TrivialRasterizer(Color color, Raster raster) {
        this.color = color;
        this.raster = raster;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }


    @Override
    public void rasterize(Line line) {
        Point a = new Point(line.getPointA());
        Point b = new Point(line.getPointB());



        int diffX = b.getX() - a.getX();
        int diffY = b.getY() - a.getY();

        int step = line.getStep();
        int space = line.getSpace();
        boolean swapped = a.getX() > b.getX();
        boolean hasSnapping = line.isSnapping();

        double k = (double)(b.getY() - a.getY()) / (diffX != 0 ? diffX : 1 );

        double factor = Math.abs(k);

        double q = a.getY() - a.getX() * k;

        int odd = 0;

        if((factor < 1) && (diffX != 0 || diffY == 0))
        {
//            System.out.println("X loop");

            int start = Math.min(Math.max(a.getX(), 0), raster.getWidth() - space );
            int end = Math.min(Math.max(b.getX(), 0), raster.getWidth() - space);


//            System.out.println("Start: " + start + " | End: " + end);

//            if((a.getY() == b.getY() ))
//            {
//
//                if(start > end)
//                {
//                    int tmp = start;
//                    start = end;
//                    end = tmp;
//
//
//                }
//
//                for(int x = start; x < end; x++){
//                    System.out.println("Same!!!!");
//                    raster.setPixel(x, a.getY(), line.getColor().getRGB());
//                }
//
//
//            }
//            else {
                int currentStart = Math.min(start, end);
                int currentEnd = Math.max(start, end);

//                System.out.println("Start: " + currentStart + " | End: " + currentEnd);

                for (int x = currentStart; x < currentEnd; x++) {

                    if (odd++ == step) {
                        x += space;
                        odd = 0;

                        if (x >= currentEnd) break;
                    }

                    int y = (int) Math.round((k * (double) x + q));

                    // If the coordinates are outside the window, therefore the mouse gone out of the window.
                    if (y < raster.getHeight() && y > 0) {
                        raster.setPixel(x, y, line.getColor().getRGB());
                    }

                }
//            }


        }else{
//
//            System.out.println("Y loop");
//
            int start = Math.min(Math.max(a.getY(), 0), raster.getHeight() - space);
            int end = Math.min(Math.max(b.getY(), 0), raster.getHeight() - space);

//            System.out.println();


//            if((a.getX() == b.getX() ))
//            {
//
//                for(int y = start; y < end; y++){
//                    System.out.println("Same!!!!");
//                    raster.setPixel(a.getX(), y, line.getColor().getRGB());
//                }
//            }else {

                int currentStart = Math.min(start, end);
                int currentEnd = Math.max(start, end);

//                System.out.println("Start: " + currentStart + " | End: " + currentEnd);

                for (int y = currentStart; y < currentEnd; y++) {

                    if (odd++ == step) {
                        y += space;
                        odd = 0;
                        if (y >= currentEnd) break;
                    }

                    int x = (int) Math.round(((double) y - q) / k );

                    // When a straight line has been drawn, this will ensure, that it will actually be straight
                    if (diffX == 0) {
                        x = a.getX();
                    }

                    // If the coordinates are outside the window, therefore the mouse gone out of the window.
                    if (x < raster.getWidth() && x >= 0) {
                        raster.setPixel(x, y, line.getColor().getRGB());
                    }



                }
//            }
        }

    }

    @Override
    public Color getColor() {
        return this.color;
    }

}
