package rasterizers;

import models.InterfaceEntities.Element;

public interface ElementRasterizer  {

    <T extends Element> void rasterize(T element);

}
