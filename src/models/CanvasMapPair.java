package models;

import models.CanvasEntities.CanvasEntity;
import models.CanvasEntities.ComplexCanvasEntity;

public class CanvasMapPair {
    long id;
    ComplexCanvasEntity canvasEntity;

    public CanvasMapPair(long id, ComplexCanvasEntity canvasEntity) {
        this.id = id;
        this.canvasEntity = canvasEntity;
    }

    public long getId() {
        return id;
    }
    public ComplexCanvasEntity getCanvasEntity() {
        return canvasEntity;
    }

    public void setCanvasEntity(ComplexCanvasEntity canvasEntity) {
        this.canvasEntity = canvasEntity;
    }

    public void setId(long id) {
        this.id = id;
    }
}
