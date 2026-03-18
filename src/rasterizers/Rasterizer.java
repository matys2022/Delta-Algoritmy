package rasterizers;

import models.CanvasEntities.CanvasEntity;
import models.CanvasEntities.Line;
import models.CanvasEntities.Point;
import models.WindowCanvasMap;
import rasters.Raster;

import java.awt.*;
import java.util.ArrayList;

public interface Rasterizer<T extends CanvasEntity> {
    ArrayList<Point> rasterize(T entity, Raster raster, WindowCanvasMap canvasMap);
}
