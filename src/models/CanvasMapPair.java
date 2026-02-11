package models;

import models.CanvasEntities.CanvasEntity;

public class CanvasMapPair {
    long id;
    CanvasEntity canvasEntity;

    public CanvasMapPair(long id, CanvasEntity canvasEntity) {
        this.id = id;
        this.canvasEntity = canvasEntity;
    }

    public long getId() {
        return id;
    }
    public CanvasEntity getCanvasEntity() {
        return canvasEntity;
    }

    public void setCanvasEntity(CanvasEntity canvasEntity) {
        this.canvasEntity = canvasEntity;
    }

    public void setId(long id) {
        this.id = id;
    }
}
