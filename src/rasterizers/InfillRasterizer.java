package rasterizers;

import models.CanvasEntities.CanvasEntity;
import models.CanvasEntities.CanvasShape;
import models.CanvasEntities.ComplexCanvasEntity;
import models.CanvasEntities.Point;
import models.WindowCanvasMap;
import rasters.Raster;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class InfillRasterizer implements Rasterizer {

    @Override
    public ArrayList<Point> rasterize(CanvasEntity entity, Raster raster, WindowCanvasMap canvasMap) {

        ArrayList<Point> filled = new ArrayList<>();
        ArrayList<Point> seeds = entity.getPoints();

        if (!(entity instanceof CanvasShape shp && shp.hasFill())) {
            return filled;
        }

        Set<Point> visited = new HashSet<>();
        Queue<Point> queue = new LinkedList<>();

        int[][] dirs = {{1,0}, {-1,0}, {0,1}, {0,-1}};

        for (Point p : seeds) {
            queue.add(p);
            visited.add(p);
        }

        boolean startedFilling = false;

        while (!queue.isEmpty()) {
            Point current = queue.poll();
            int x = current.getX();
            int y = current.getY();

            if (!canvasMap.isWithinBounds(x, y)) continue;

            ComplexCanvasEntity atPixel = canvasMap.peekCanvasEntityPoint(x, y);

            boolean isBorder = (atPixel != null && atPixel.equals(entity));

            if (!startedFilling) {
                if (isBorder) {
                    for (int[] d : dirs) {
                        Point next = new Point(x + d[0], y + d[1]);
                        if (!visited.contains(next)) {
                            visited.add(next);
                            queue.add(next);
                        }
                    }
                    continue;
                } else {
                    startedFilling = true;
                }
            }

            if (isBorder) continue;

            raster.setPixel(x, y, shp.getInfillColor().getRGB());
            filled.add(current);

            for (int[] d : dirs) {
                int nx = x + d[0];
                int ny = y + d[1];

                Point next = new Point(nx, ny);

                if (!canvasMap.isWithinBounds(nx, ny)) continue;

                if (!visited.contains(next)) {
                    visited.add(next);
                    queue.add(next);
                }
            }
        }

        return filled;
    }
}
