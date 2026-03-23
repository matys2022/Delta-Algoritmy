package rasterizers;

import models.CanvasEntities.*;
import models.WindowCanvasMap;
import rasters.Raster;
import rasters.RasterBufferedImage;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class CanvasRasterizer {
    public Raster previewRaster;
    public Raster renderRaster;

    public LineRasterizer lineRasterizer;
    public CircleRasterizer circleRasterizer;
    public FillRasterizer fillRasterizer;
    public final WindowCanvasMap canvasMap;

    public CanvasRasterizer(int  width, int height, WindowCanvasMap  map) {
        previewRaster = new RasterBufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);;
        renderRaster = new RasterBufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        lineRasterizer = new LineRasterizer();
        circleRasterizer = new CircleRasterizer();
        fillRasterizer = new FillRasterizer();

        canvasMap = map;
    }



    public void rasterize(ComplexCanvasEntity entity, boolean preview){
        if(entity instanceof FillEntity fillEntity){
//            System.out.println("Points to be filled (Debug) rasterize service: " + canvasMap.getFloodPoints(fillEntity.getFillReferencePoint()).size());
        }

        this.clearPreviewRaster();
        Raster raster = preview?previewRaster:renderRaster;
        entity.clearVisiblePoints();

        switch (entity) {
            case Line line -> {

                ArrayList<Point> points = lineRasterizer.rasterize(line, raster, canvasMap);
                if(!preview){
                    line.addVisiblePoints(points);
                }
            }
            case PolygonEntity polygon -> {
//                entity.clearVisiblePoints();
                for (Line line : polygon.getLines()) {
                    ArrayList<Point> points = lineRasterizer.rasterize(line, raster, canvasMap);
                    if(!preview){
                        polygon.addVisiblePoints(points);
                    }
                }
            }
            case Circle circle -> {
//                entity.clearVisiblePoints();
                ArrayList<Point> points = circleRasterizer.rasterize(circle, raster, canvasMap);
//                System.out.println(circle.getSpace() + " " + circle.getStep());
                if(!preview){
                    circle.addVisiblePoints(points);
                }
            }
            case FillEntity fill -> {

                ArrayList<Point> points = fillRasterizer.rasterize(fill, raster, canvasMap);
                if(!preview){
                    fill.addVisiblePoints(points);
                }
            }
            default -> {
            }
        }


    }

//    public void rasterizeInfill(ComplexCanvasEntity entity, boolean preview){
//
//        Raster raster = preview?previewRaster:renderRaster;
//        entity.clearVisiblePoints();
//
//        if(entity instanceof CanvasShape shp) {
//            shp.clearFillPoints();
//            Collection<Point> points = fillRasterizer.rasterize(entity, raster, canvasMap);
//            entity.addVisiblePoints(points);
//        }
//    }


    public Raster getRenderRaster() {
        return renderRaster;
    }

    public Raster getPreviewRaster() {
        return previewRaster;
    }

    public void clearRenderRaster() {
        renderRaster.clear();
    }

    public void clearPreviewRaster() {
        previewRaster.clear();
    }
}
