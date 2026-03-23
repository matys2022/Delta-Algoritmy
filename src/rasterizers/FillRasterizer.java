package rasterizers;

import models.CanvasEntities.*;
import models.CanvasEntities.Point;
import models.WindowCanvasMap;
import rasters.Raster;

import java.awt.*;
import java.util.*;

public class FillRasterizer implements Rasterizer<FillEntity> {

    @Override
    public ArrayList<Point> rasterize(FillEntity entity, Raster raster, WindowCanvasMap canvasMap) {


        ArrayList<Point> visiblePoints = new ArrayList<>();

        ArrayList<Point> floodPoints = canvasMap.getFloodPoints(entity.getFillReferencePoint(), entity.getPoints());


//        if(entity instanceof FillEntity fillEntity){
////            System.out.println("Points to be filled (Debug): " + canvasMap.getFloodPoints(fillEntity.getFillReferencePoint()).size());
//        }

//        System.out.println("Flood point x: " + entity.getFillReferencePoint().getX());
//        System.out.println("Flood point y: " + entity.getFillReferencePoint().getY());
//        System.out.println("Rasterizer flood points: " + floodPoints.size());

        for(Point p : floodPoints) {
            int x = p.getX();
            int y = p.getY();
            if(canvasMap.isWithinBounds(x, y)) {
                raster.setPixel(x, y, entity.getInfill().getRGB());

                visiblePoints.add(p);
            }
        }

        return visiblePoints;
    }

}
