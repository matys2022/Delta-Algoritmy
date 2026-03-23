package models;

import models.CanvasEntities.ComplexCanvasEntity;
import models.CanvasEntities.Point;

import java.util.*;

public class WindowCanvasMap {
    private CanvasMapPair[][] baseMap;
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

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
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

    public void clear(){
        this.baseMap = new CanvasMapPair[height][width];
    }


    public boolean isBoundary(Point p){
        if (hasEntity(p.getX(), p.getY())) {
            return false;
        }

        int[][] neighbours = {{1,0}, {0,1}, {-1,0}, {0,-1}};

        for (int[] n : neighbours) {
            int nx = p.getX() + n[0];
            int ny = p.getY() + n[1];

            if (!isWithinBounds(nx, ny) || hasEntity(nx, ny)) {
                return true;
            }
        }

        return false;

    }



    public ArrayList<Point> getFloodPoints(Point start){
        return this.getFloodPoints(start,null);
    }

    public ArrayList<Point> getFloodPoints(Point start, List<Point> boundaryPoints){
        Set<Point> visited = new HashSet<>();
        Queue<Point> queue = new LinkedList<>();

        int[][] dirs = {{1,0}, {-1,0}, {0,1}, {0,-1}};

        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            Point current = queue.poll();

            for (int[] d : dirs) {
                int nx = current.getX() + d[0];
                int ny = current.getY() + d[1];

                if (!this.isWithinBounds(nx, ny)) continue;

                Point next = new Point(nx, ny);

                if(boundaryPoints == null){
                    if(hasEntity(nx, ny)){
                        continue;
                    }
                }else if(boundaryPoints.contains(next)){
                    continue;
                }


                if (!visited.contains(next)) {
                    visited.add(next);
                    queue.add(next);
                }
            }
        }

        return new ArrayList<>(visited);
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


    public List<ComplexCanvasEntity> getCanvasEntities(int x, int y){
        List<ComplexCanvasEntity> canvasEntities = new ArrayList<>();

        if(!isWithinBounds(x, y)){
            return null; // Point is out of the window bounds, thus it doesn't make any sense to retain its reference
        }

        CanvasMapPair canvasPoint = baseMap[y][x];

        if(canvasPoint == null){ // Point is not associated with any canvas entity
            return null;
        }

        if(canvasPoint.getId() == -1){ // There is just a single entity point at the requested coordinates
            canvasEntities.add(canvasPoint.getCanvasEntity());
        }
        ArrayList<ComplexCanvasEntity> entities = stackMap.get(canvasPoint.getId());

        if(entities != null){
            canvasEntities.addAll(entities);
        }
        return canvasEntities;

    }

    public boolean containsEntity(int x, int y, ComplexCanvasEntity entity){
        if(!this.hasEntity(x, y)){
            return true;
        }

        CanvasMapPair canvasPoint = baseMap[y][x];

        if(canvasPoint.getId() == -1){ // There is just a single entity point at the requested coordinates
            return canvasPoint.getCanvasEntity().equals(entity);
        }

        return stackMap.get(canvasPoint.getId()).contains(entity); // More than one canvas entity point exists for the requested coordinates

    }


}
