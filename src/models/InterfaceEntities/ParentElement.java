package models.InterfaceEntities;

public abstract class ParentElement extends Element implements ParentEntity{
    public ParentElement(int content_width, int content_height, BoundingDimensions padding, ColorSet colorSet, BoundingDimensions border, Coordinates coordinates, Element parent) {
        super(content_width, content_height, padding, colorSet, border, coordinates, parent);
    }

    public ParentElement(int content_width, int content_height, BoundingDimensions padding, ColorSet colorSet) {
        super(content_width, content_height, padding, colorSet);
    }
}
