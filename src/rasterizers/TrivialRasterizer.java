package rasterizers;

import models.Line;
import models.Point;
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

        boolean swapped = a.getX() > b.getX();

        int diffX = b.getX() - a.getX();
        int diffY = b.getY() - a.getY();

        int step = line.getStep();
        int space = line.getSpace();
        boolean hasSnapping = line.isSnapping();

        double k = (double)(b.getY() - a.getY()) / (diffX != 0 ? diffX : 1 );

        double factor = Math.abs(k);

        if(hasSnapping){



            if (factor < 0.5 && factor > 0){
                // Snap to X axis
                k = 0;
            }else

            if (factor <= 1.5 && factor >= 1){
                // Halve it
                k =  (k > 0 ? 1 : -1);
            }else

            if (factor > 0.5 &&  factor <= 1){
                k = (k > 0 ? 1 : -1);
            }else

            if(factor > 1.5){
                k = (k > 0 ? raster.getHeight() : -(raster.getHeight())) * (swapped ? -1 : 1);
            }

        }


        double q = a.getY() - a.getX() * k;




        int odd = 0;

        System.out.println(k);

        if(((factor <= 1 && factor >= 0) || diffX > Math.abs(diffY)) && diffX != 0)
        {
            System.out.println("X loop");

            int start = a.getX();
            int end = Math.min(Math.max(b.getX(), 0), raster.getWidth() - 1);

            if(start >= end){
                int tmp = start;
                start = end;
                end = tmp;
            }

            for(int x = start; x < end; x++){

                if(odd++ == step){
                    x+=space;
                    odd = 0;
                }

                int y = (int)Math.round((k*(double) x + q));

                // If the coordinates are outside the window, therefore the mouse gone out of the window.
                if(y < raster.getHeight() && y > 0)
                {
                    raster.setPixel(x, y, color.getRGB());
                }
            }

        }else{

            System.out.println("Y loop");

            int start = a.getY();
            int end = b.getY();

            if(start >= end){
                int tmp = start;
                start = end;
                end = tmp;
            }

            System.out.println("Start: " + start);
            System.out.println("End: " + end);


            for(int y = Math.max(start, 0); y < Math.min(end, raster.getHeight() - space); y++){

                if(odd++ == step){
                    y+=space;
                    odd = 0;
                }


                int x = (int)Math.round(((double)y-q)/k);



                // When a straight line has been drawn, this will ensure, that it will actually be straight
                if(diffX == 0){
                    x = a.getX();
                }

                // If the coordinates are outside the window, therefore the mouse gone out of the window.
                try{
                    if(x < raster.getWidth() && x >= 0){
                        raster.setPixel(x, y, color.getRGB());
                    }
                }catch(Exception e){
                    System.out.println("X : " + x);
                    System.out.println("Y : " + y);
                    throw e;
                }


            }
        }

    }

    @Override
    public Color getColor() {
        return this.color;
    }

}
