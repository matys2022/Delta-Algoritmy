package rasterizers;

import models.CanvasEntities.CanvasEntity;
import models.CanvasEntities.Circle;
import models.CanvasEntities.Line;
import models.CanvasEntities.PolygonEntity;
import models.WindowCanvasMap;
import rasters.Raster;
import rasters.RasterBufferedImage;

import java.awt.image.BufferedImage;

public class CanvasRasterizer {
    public Raster previewRaster;
    public Raster renderRaster;

    public LineRasterizer lineRasterizer;
    public CircleRasterizer circleRasterizer;
    protected final WindowCanvasMap canvasMap;

    public CanvasRasterizer(int  width, int height) {
        previewRaster = new RasterBufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);;
        renderRaster = new RasterBufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        lineRasterizer = new LineRasterizer();
        circleRasterizer = new CircleRasterizer();

        canvasMap = new WindowCanvasMap(width, height);
    }

    public void rasterize(CanvasEntity entity, boolean preview){
        this.clearPreviewRaster();
        Raster raster = preview?previewRaster:renderRaster;

        if(entity instanceof Line line){
            lineRasterizer.rasterize(line, raster, canvasMap);
        }else if (entity instanceof PolygonEntity polygon){
            for(Line line : polygon.getLines()){
                lineRasterizer.rasterize(line, raster, canvasMap);
            }
        }else if(entity instanceof Circle circle){
            circleRasterizer.rasterize(circle, raster, canvasMap);
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
