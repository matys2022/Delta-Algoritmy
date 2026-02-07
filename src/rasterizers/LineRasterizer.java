package rasterizers;

import models.Line;

import java.awt.*;

public interface LineRasterizer {
    void setColor(Color color);

    void rasterize(Line line);

    Color getColor();
}
