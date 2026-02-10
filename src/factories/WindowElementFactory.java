package factories;

import models.InterfaceEntities.*;

import java.util.function.Consumer;

public class WindowElementFactory {

    public WindowElementFactory() {

    }

    public Button CreateButton(int[][] contentBitMap, BoundingDimensions padding, ColorSet colorSet, ColorSet secondaryColorSet, BoundingDimensions border, Coordinates coordinates, Consumer<Element> action) {
        return new Button(contentBitMap, padding, colorSet, secondaryColorSet, border, coordinates, action);
    }

    public MenuBar CreateMenuBar(int width, int height,  int gap, BoundingDimensions padding, BoundingDimensions margin, ColorSet colors) {
        return new MenuBar(width, height, gap, padding, margin, colors);
    }

}
