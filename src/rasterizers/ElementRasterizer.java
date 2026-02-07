package rasterizers;

import models.Element;
import models.Line;

import java.awt.*;

public interface ElementRasterizer  {

    <T extends Element> void rasterize(T element);

}
