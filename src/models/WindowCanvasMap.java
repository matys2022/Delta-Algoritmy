package models;

import models.CanvasEntities.CanvasEntity;
import models.CanvasEntities.ComplexCanvasEntity;
import models.CanvasEntities.Point;

import java.util.*;

public class WindowCanvasMap {
    private final CanvasMapPair[][] baseMap;
    private final int width;
    private final int height;

    private final Map<Long, ArrayList<ComplexCanvasEntity>> stackMap;

    private final ArrayDeque<Long> availableIds = new ArrayDeque<Long>();

    public WindowCanvasMap(int width, int height){
        this.width = width;
        this.height = height;
        baseMap = new CanvasMapPair[height][width];
        stackMap = new HashMap<>();
    }

    private long nextId = 0;

    public void addCanvasEntityPoint(int x, int y, ComplexCanvasEntity canvasEntity){
        if(y > baseMap.length - 1 || x >  baseMap[0].length - 1 || y < 0 || x < 0){
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
            ArrayList<ComplexCanvasEntity> stack = new ArrayList<>();
            stack.add(canvasPoint.getCanvasEntity()); // Duplicated value, might be unnecessary
            stack.add(canvasEntity);
            stackMap.put(stackId, stack);

        } else {
            // Stack already exists, just push the new entity
            stackMap.get(stackId).add(canvasEntity);
        }
    }

    public void addCanvasEntityPoint(Point point, ComplexCanvasEntity canvasEntity) {
        addCanvasEntityPoint(point.getX(),point.getY(), canvasEntity);
    }

    public ComplexCanvasEntity removeCanvasEntityPoint(ComplexCanvasEntity entity, int x, int y){
//        try{
            if(y > baseMap.length - 1 || x > baseMap[0].length - 1 || y < 0 || x < 0){
                return null; // Point is out of the window bounds, thus it doesn't make any sense to retain its reference
            }
            CanvasMapPair canvasPoint = baseMap[y][x];

            if(canvasPoint == null){
                throw new NoSuchElementException("Element not found at: " + x + ", " + y);
            }


            ComplexCanvasEntity tmp = canvasPoint.getCanvasEntity();

            if(canvasPoint.getId() == -1){ // There is a single entity point, because no stack has been initialized
                canvasPoint.setCanvasEntity(null);
                return tmp;
            }

            ArrayList<ComplexCanvasEntity> canvasPointEntities = stackMap.get(canvasPoint.getId());
            canvasPointEntities.remove(entity);

            if(canvasPointEntities.size() == 1){ // Bring the last remaining point to the "Header" of the canvas pair and scrap the stack
                ComplexCanvasEntity entityToBeMoved = canvasPointEntities.getFirst();
                canvasPoint.setCanvasEntity(entityToBeMoved);
                canvasPointEntities.removeFirst();
                availableIds.push(canvasPoint.getId());

                stackMap.remove(canvasPoint.getId());
                canvasPoint.setId(-1);

            }

            return tmp;

//        }catch(NoSuchElementException e){
//            System.err.println("Element not found at: " + x + ", " + y);
//            return null;
//        }

    }

    public boolean hasEntity(int x, int y) {

        if (x >= 0 && x < baseMap[0].length && y >= 0 && y < baseMap.length) { // Check window boundaries
            return this.peekCanvasEntityPoint(x, y) instanceof ComplexCanvasEntity;
        }
        return false;
    }

    public boolean isWithinBounds(int x, int y){
        return y < this.height && x <  width && y >= 0 && x >= 0;
    }

    public ComplexCanvasEntity peekCanvasEntityPoint(Point point){
        return this.peekCanvasEntityPoint(point.getX(), point.getY());
    }
    public ComplexCanvasEntity peekCanvasEntityPoint(int x, int y){
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

        return stackMap.get(canvasPoint.getId()).getLast(); // More than one canvas entity point exists for the requested coordinates
    }


}
