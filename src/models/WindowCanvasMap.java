package models;

import models.CanvasEntities.CanvasEntity;
import models.CanvasEntities.Point;
import models.InterfaceEntities.Element;

import java.awt.*;
import java.util.*;

public class WindowCanvasMap {
    private final CanvasMapPair[][] baseMap;

    private final Map<Long, Deque<CanvasEntity>> stackMap;

    private final ArrayDeque<Long> availableIds = new ArrayDeque<Long>();

    public WindowCanvasMap(int width, int height){
        baseMap = new CanvasMapPair[height][width];
        stackMap = new HashMap<>();
    }

    private long nextId = 0;

    public void addCanvasEntityPoint(int x, int y, CanvasEntity canvasEntity){
        if(y > baseMap.length || x >  baseMap[0].length || y < 0 || x < 0){
            return; // Point is out of the window bounds, thus it doesn't make any sense to retain its reference
        }
        CanvasMapPair canvasPoint = baseMap[y][x];

        if (canvasPoint == null || canvasPoint.getCanvasEntity() == null) { // If the pixel is totally empty, add the entity
            baseMap[y][x] = new CanvasMapPair(-1, canvasEntity);
            return;
        }

        // If it is already occupied by the SAME entity
        if (canvasPoint.getCanvasEntity().equals(canvasEntity)) {
            return;
        }

        // If it is already occupied by a different entity, get its id
        long stackId = canvasPoint.getId();

        if (stackId == -1) { // The stack wasn't initialized yet
            stackId = availableIds.isEmpty() ? nextId++ : availableIds.pop();
            baseMap[y][x] = new CanvasMapPair(stackId, canvasPoint.getCanvasEntity());

            // Create the stack
            Deque<CanvasEntity> stack = new ArrayDeque<>();
            stack.push(canvasPoint.getCanvasEntity()); // Duplicated value, might be unnecessary
            stack.push(canvasEntity);
            stackMap.put(stackId, stack);

        } else {
            // Stack already exists, just push the new entity
            stackMap.get(stackId).push(canvasEntity);
        }
    }

    public void addCanvasEntityPoint(Point point, CanvasEntity canvasEntity) {
        addCanvasEntityPoint(point.getX(),point.getY(), canvasEntity);
    }

    public CanvasEntity popCanvasEntityPoint(int x, int y){
        if(y > baseMap.length || x >  baseMap[0].length || y < 0 || x < 0){
            return null; // Point is out of the window bounds, thus it doesn't make any sense to retain its reference
        }
        CanvasMapPair canvasPoint = baseMap[y][x];

        if(canvasPoint == null){
            throw new NoSuchElementException("Element not found at: " + x + ", " + y);
        }


        if(canvasPoint.getId() == -1){ // There is a single entity point, therefore no stack has been initialized
            CanvasEntity tmp = canvasPoint.getCanvasEntity();
            canvasPoint.setCanvasEntity(null);
            return tmp;
        }


        CanvasEntity tmp = stackMap.get(canvasPoint.getId()).pop();

        if(stackMap.get(canvasPoint.getId()).size() == 1){ // Bring the last remaining point to the "Header" of the canvas pair and scrap the stack

            canvasPoint.setCanvasEntity(stackMap.get(canvasPoint.getId()).pop());
            availableIds.push(canvasPoint.getId());

            stackMap.remove(canvasPoint.getId());
            canvasPoint.setId(-1);

        }

        return tmp;
    }

    public CanvasEntity peekCanvasEntityPoint(int x, int y){
        if(y > baseMap.length || x >  baseMap[0].length || y < 0 || x < 0){
            return null; // Point is out of the window bounds, thus it doesn't make any sense to retain its reference
        }

        CanvasMapPair canvasPoint = baseMap[y][x];

        if(canvasPoint == null){ // Point is not associated with any canvas entity
            return null;
        }

        if(canvasPoint.getId() == -1){ // There is just a single entity point at the requested coordinates
            return canvasPoint.getCanvasEntity();
        }

        return stackMap.get(canvasPoint.getId()).peek(); // More than one canvas entity point exists for the requested coordinates
    }


}
