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
    public InfillRasterizer infillRasterizer;
    public final WindowCanvasMap canvasMap;

    public CanvasRasterizer(int  width, int height) {
        previewRaster = new RasterBufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);;
        renderRaster = new RasterBufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        lineRasterizer = new LineRasterizer();
        circleRasterizer = new CircleRasterizer();
        infillRasterizer = new InfillRasterizer();

        canvasMap = new WindowCanvasMap(width, height);
    }

    public void rasterizeOutline(ComplexCanvasEntity entity, boolean preview){
        this.clearPreviewRaster();
        Raster raster = preview?previewRaster:renderRaster;
//        System.out.println("Rasterizing : " + entity.getClass().getName());
        entity.clearVisiblePoints();

        switch (entity) {
            case Line line -> {

                ArrayList<Point> points = lineRasterizer.rasterize(line, raster, canvasMap);
                line.addVisiblePoints(points);
            }
            case PolygonEntity polygon -> {
                for (Line line : polygon.getLines()) {
                    ArrayList<Point> points = lineRasterizer.rasterize(line, raster, canvasMap);
                    polygon.addVisiblePoints(points);
                }
            }
            case Circle circle -> {
                ArrayList<Point> points = circleRasterizer.rasterize(circle, raster, canvasMap);
//                System.out.println(circle.getSpace() + " " + circle.getStep());
                circle.addVisiblePoints(points);
            }
            default -> {
            }
        }


    }

    public void rasterizeInfill(ComplexCanvasEntity entity, boolean preview){

        Raster raster = preview?previewRaster:renderRaster;
        entity.clearVisiblePoints();

        if(entity instanceof CanvasShape shp) {
            shp.clearFillPoints();
            ArrayList<Point> points = infillRasterizer.rasterize(entity, raster, canvasMap);
            entity.addVisiblePoints(points);
        }
    }


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
