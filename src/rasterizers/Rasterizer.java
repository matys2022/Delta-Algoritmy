package rasterizers;

import models.CanvasEntities.CanvasEntity;
import models.CanvasEntities.Line;
import models.WindowCanvasMap;
import rasters.Raster;

import java.awt.*;

public interface Rasterizer<T extends CanvasEntity> {
    void rasterize(T entity, Raster raster, WindowCanvasMap canvasMap);
}
