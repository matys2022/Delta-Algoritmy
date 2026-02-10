package rasterizers;

import models.CanvasEntities.Line;

import java.awt.*;

public interface LineRasterizer {
    void setColor(Color color);

    void rasterize(Line line);

    Color getColor();
}
