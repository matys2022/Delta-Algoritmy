package rasterizers;

import models.CanvasEntities.CanvasEntity;
import models.CanvasEntities.Point;
import models.WindowCanvasMap;
import rasters.Raster;

import java.util.ArrayList;

public interface Rasterizer<T extends CanvasEntity> {
    ArrayList<Point> rasterize(T entity, Raster raster, WindowCanvasMap canvasMap);
}
